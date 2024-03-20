package org.ncfl.specs;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.CaffeineCache;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class SpecsResource {

    private final Reporter roomSpecReport;
    private final Reporter competitionGridReport;
    private final Reporter scheduleReport;
    private final io.quarkus.cache.Cache hotelCache;
    private final SpreadsheetHandler spreadsheetHandler;

    @Inject
    public SpecsResource(@CacheName("room-grid") Cache hotelCache1, SpreadsheetHandler spreadsheetHandler) {
        this.hotelCache = hotelCache1;
        this.spreadsheetHandler = spreadsheetHandler;
        this.competitionGridReport = new CompetitionGridReport();
        this.roomSpecReport = new RoomSpecReport();
        this.scheduleReport = new ScheduleReport();
    }

    @Path("/upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_HTML)
    public Uni<String> upload(@RestForm("file") FileUpload file) {
        return hotelCache.get("the grid", key ->
                spreadsheetHandler.getHotelRoomUsage(file.uploadedFile().toFile())
            )
            .map(roomSpecReport::process);
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
        return getTheGrid().thenApply(scheduleReport::process);
    }

    @Path("/grid")
    @GET
    public CompletableFuture<String> grid(){
        return getTheGrid().thenApply(competitionGridReport::process);
    }

}
