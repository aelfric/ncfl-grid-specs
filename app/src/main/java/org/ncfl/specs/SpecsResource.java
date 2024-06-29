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
import org.ncfl.specs.model.Hotel;
import org.ncfl.specs.reports.CompetitionGridReport;
import org.ncfl.specs.reports.Reporter;
import org.ncfl.specs.reports.RoomSpecReport;
import org.ncfl.specs.reports.ScheduleReport;

import java.util.List;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class SpecsResource {

    private final Reporter roomSpecReport;
    private final Reporter competitionGridReport;
    private final Reporter scheduleReport;
    private final io.quarkus.cache.Cache hotelCache;
    private final SpreadsheetHandler spreadsheetHandler;
    private final GoogleSheetHandler googleSheetHandler;

    @Inject
    public SpecsResource(@CacheName("room-grid") Cache hotelCache1, SpreadsheetHandler spreadsheetHandler, GoogleSheetHandler googleSheetHandler) {
        this.hotelCache = hotelCache1;
        this.spreadsheetHandler = spreadsheetHandler;
        this.competitionGridReport = new CompetitionGridReport();
        this.roomSpecReport = new RoomSpecReport();
        this.scheduleReport = new ScheduleReport();
        this.googleSheetHandler = googleSheetHandler;
    }

    @Path("/upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_HTML)
    public Uni<String> upload(@RestForm("file") FileUpload file) {
        return hotelCache.getAsync("the grid", key ->
                googleSheetHandler.getHotelRoomUsage()
            )
            .map(roomSpecReport::process);
    }

    @Path("/refresh")
    @POST
    @Produces(MediaType.TEXT_HTML)
    public Uni<String> refresh() {
        return hotelCache.invalidate("the grid").chain(()->
            hotelCache.getAsync("the grid", key ->
                googleSheetHandler.getHotelRoomUsage()
            )
            .map(roomSpecReport::process));
    }

    @Path("/specs")
    @GET
    public Uni<String> specs(){
        return getTheGrid().map(roomSpecReport::process).onSubscription().invoke(googleSheetHandler::doSomething);
    }

    private Uni<List<Hotel>> getTheGrid() {
        return Uni.createFrom().completionStage(
        hotelCache.as(CaffeineCache.class)
            .getIfPresent("the grid"));
    }

    @Path("/sched")
    @GET
    public Uni<String> schedule(){
        return getTheGrid().map(scheduleReport::process);
    }

    @Path("/grid")
    @GET
    public Uni<String> grid(){
        return getTheGrid().map(competitionGridReport::process);
    }

}
