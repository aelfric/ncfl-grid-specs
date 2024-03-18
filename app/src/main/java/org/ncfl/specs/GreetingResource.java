package org.ncfl.specs;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.CaffeineCache;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Path("/")
public class GreetingResource {

    private final Reporter roomSpecReport;
    private final Reporter competitionGridReport;
    private final io.quarkus.cache.Cache hotelCache;
    private final SpreadsheetHandler spreadsheetHandler;

    @Inject
    public GreetingResource(@CacheName("room-grid") Cache hotelCache1, SpreadsheetHandler spreadsheetHandler) {
        this.hotelCache = hotelCache1;
        this.spreadsheetHandler = spreadsheetHandler;
        this.competitionGridReport = new CompetitionGridReport();
        this.roomSpecReport = new RoomSpecReport();
    }

    @Path("/upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_HTML)
    public String upload(@RestForm("file") FileUpload file) {
        final List<Hotel> hotelRoomUsage = spreadsheetHandler.getHotelRoomUsage(file.uploadedFile().toFile());
        hotelCache.get("the grid", key -> spreadsheetHandler.getHotelRoomUsage(file.uploadedFile().toFile()))
            .await()
            .indefinitely();
        return roomSpecReport.process(hotelRoomUsage);
    }

    @Path("/specs")
    @GET
    public CompletableFuture<String> specs(){
        return getTheGrid().thenApply(roomSpecReport::process);
    }

    private CompletableFuture<List<Hotel>> getTheGrid() {
        return hotelCache.as(CaffeineCache.class)
            .getIfPresent("the grid");
    }

    @Path("/sched")
    @GET
    public CompletableFuture<String> schedule(){
        return getTheGrid().thenApply(
            o -> o.get(0).name()
        );
    }

    @Path("/grid")
    @GET
    public CompletableFuture<String> grid(){
        return getTheGrid().thenApply(competitionGridReport::process);
    }
}
