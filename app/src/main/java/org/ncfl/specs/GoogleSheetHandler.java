package org.ncfl.specs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.InternalServerErrorException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.ncfl.specs.model.Hotel;
import org.ncfl.specs.model.RoomUsage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.*;

@Singleton
public class GoogleSheetHandler {
    protected static final Logger logger = Logger.getLogger(GoogleSheetHandler.class);
    private final Path credentialPath;

    final ObjectMapper objectMapper;

    final String sheetId;

    @Inject
    public GoogleSheetHandler(
            ObjectMapper objectMapper,
            @ConfigProperty(name = "google.sheet-id") String sheetId,
            @ConfigProperty(name = "google.credential-path") Path credentialPath
    ) {
        this.objectMapper = objectMapper;
        this.sheetId = sheetId;
        this.credentialPath = credentialPath;

    }

    public Uni<List<Hotel>> getHotelRoomUsage() {
        return Uni
                .createFrom()
                .item(this::readSheets)
                .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
    }

    private static final String APPLICATION_NAME = "NCFL Grid Specs";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES =
            Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);

    /**
     * Creates an authorized Credential object.
     *
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private HttpRequestInitializer getCredentials()
            throws IOException {
        GoogleCredentials credential = ServiceAccountCredentials.fromStream(Files.newInputStream(credentialPath))
                .createScoped(SCOPES);
        credential.refreshIfExpired();
        credential.getAccessToken();
        return new HttpCredentialsAdapter(credential);
    }

    public List<Hotel> readSheets() {
        try {
            // Build a new authorized API client service.
            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            Sheets service =
                    new Sheets.Builder(httpTransport, JSON_FACTORY, getCredentials())
                            .setApplicationName(APPLICATION_NAME)
                            .build();
            return List.of(
                    getHotel(service, "Hilton Chicago!A1:P", "Hilton Chicago"),
                    getHotel(service, "Palmer House!A1:P", "Palmer House")
            );
        } catch (IOException | GeneralSecurityException e) {
            throw new InternalServerErrorException(e);
        }
    }

    private Hotel getHotel(Sheets service, String range, String hotelName) throws IOException {
        ValueRange response = service.spreadsheets().values()
                .get(sheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        List<RoomUsage> data;
        if (values == null || values.isEmpty()) {
            logger.warn("No data found.");
            data = Collections.emptyList();
        } else {
            List<String> headers = values.getFirst().stream().map(Object::toString).toList();

            data = values.parallelStream()
                    .skip(1)
                    .map(row -> {
                        final Map<String, String> datum = rowToMap(row, headers);

                        try {
                            if (!datum.getOrDefault("Day", "").isEmpty()) {
                                logger.infov("Record: {0}", datum);
                                return objectMapper.convertValue(datum, RoomUsage.class);
                            } else {
                                return null;
                            }
                        } catch (IllegalArgumentException e) {
                            logger.error("Could not read row {}", datum, e);
                            return null;
                        }

                    })
                    .filter(Objects::nonNull)
                    .toList();
        }
        return new Hotel(hotelName, data);
    }

    private static Map<String, String> rowToMap(List<Object> row, List<String> headers) {
        Map<String, String> datum = new HashMap<>();
        for (int i = 0; i < headers.size(); i++) {
            try {
                datum.put(headers.get(i), row.get(i).toString());
            } catch (IndexOutOfBoundsException e) {
                // ignore
            }
        }
        return datum;
    }


}
