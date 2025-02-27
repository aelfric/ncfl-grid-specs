package org.ncfl.specs;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.CaffeineCache;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.ncfl.specs.model.Hotel;
import org.ncfl.specs.reports.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static j2html.TagCreator.option;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class SpecsResource {

    private static final Logger log = LoggerFactory.getLogger(SpecsResource.class);
    private final Reporter roomSpecReport;
    private final Reporter roomSetReport;
    private final Reporter competitionGridReport;
    private final ScheduleReport scheduleReport;
    private final io.quarkus.cache.Cache hotelCache;
    private final GoogleSheetHandler googleSheetHandler;
    private final S3Client s3;
    private final String bucketName;
    private static final String GRID_CACHE_KEY = "the grid";

    @Inject
    public SpecsResource(@CacheName("room-grid") Cache hotelCache,
                         GoogleSheetHandler googleSheetHandler,
                         S3Client s3,
                         @ConfigProperty(name = "bucket.name") String bucketName
    ) {
        this.hotelCache = hotelCache;
        this.competitionGridReport = new CompetitionGridReport();
        this.roomSpecReport = new RoomSpecReport();
        this.scheduleReport = new ScheduleReport();
        this.roomSetReport = new RoomSetReport();
        this.googleSheetHandler = googleSheetHandler;
        this.s3 = s3;
        this.bucketName = bucketName;
    }

    @Path("/refresh")
    @POST
    @Produces(MediaType.TEXT_HTML)
    public Uni<String> refresh(@QueryParam("save") boolean shouldSave) {
        return hotelCache.invalidate(GRID_CACHE_KEY).chain(() ->
                hotelCache.getAsync(
                        GRID_CACHE_KEY,
                        key -> googleSheetHandler.getHotelRoomUsage()
                    )
                    .map(roomSpecReport::process))
            .onItem()
            .invoke(
                s -> {
                    if (shouldSave) {
                        log.info("Uploading to S3");
                        s3.putObject(
                            PutObjectRequest
                                .builder()
                                .bucket(bucketName)
                                .key(LocalDateTime.now().toString())
                                .build(),
                            RequestBody.fromString(s)
                        );
                    }
                }
            );
    }

    @Path("/specs")
    @GET
    public Uni<String> specs() {
        return getTheGrid().map(roomSpecReport::process);
    }

    @Path("/snapshots")
    @GET
    public Uni<String> snapshots() {
        return Uni.createFrom().item(
            () -> s3.listObjects(
                    ListObjectsRequest
                        .builder()
                        .bucket(bucketName)
                        .build()
                )
                .contents()
                .stream()
                .map(S3Object::key)
                .map(key -> option(key).attr("value", key).toString())
                .collect(Collectors.joining())
        );
    }

    @Path("/snapshots/{key}")
    @GET
    public Uni<String> snapshots(@PathParam("key") String key) {
        return Uni.createFrom().item(
            Unchecked.supplier(() -> {
                ResponseInputStream<GetObjectResponse> object = s3.getObject(
                    GetObjectRequest
                        .builder()
                        .bucket(bucketName)
                        .key(key)
                        .build()
                );
                return new String(object.readAllBytes(), StandardCharsets.UTF_8);
            })
        );
    }


    @Path("/sets")
    @GET
    public Uni<String> sets() {
        return Uni.createFrom().item(roomSetReport.process(Collections.emptyList()));
    }

    private Uni<List<Hotel>> getTheGrid() {
        return Uni.createFrom().completionStage(
            hotelCache.as(CaffeineCache.class)
                .getIfPresent(GRID_CACHE_KEY));
    }

    @Path("/sched.json")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Uni<List<ScheduleReport.ScheduleBlock>> schedule() {
        return getTheGrid().map(scheduleReport::process);
    }

    @Path("/grid")
    @GET
    public Uni<String> grid() {
        return getTheGrid().map(competitionGridReport::process);
    }

}
