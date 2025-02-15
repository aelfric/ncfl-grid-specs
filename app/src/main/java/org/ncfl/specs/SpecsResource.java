package org.ncfl.specs;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.CaffeineCache;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.ncfl.specs.model.Hotel;
import org.ncfl.specs.reports.*;

import java.util.Collections;
import java.util.List;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class SpecsResource {

    private final Reporter roomSpecReport;
    private final Reporter roomSetReport;
    private final Reporter competitionGridReport;
    private final Reporter scheduleReport;
    private final io.quarkus.cache.Cache hotelCache;
    private final GoogleSheetHandler googleSheetHandler;
    private static final String GRID_CACHE_KEY = "the grid";

    @Inject
    public SpecsResource(@CacheName("room-grid") Cache hotelCache, GoogleSheetHandler googleSheetHandler) {
        this.hotelCache = hotelCache;
        this.competitionGridReport = new CompetitionGridReport();
        this.roomSpecReport = new RoomSpecReport();
        this.scheduleReport = new ScheduleReport();
        this.roomSetReport = new RoomSetReport();
        this.googleSheetHandler = googleSheetHandler;
    }

    @Path("/refresh")
    @POST
    @Produces(MediaType.TEXT_HTML)
    public Uni<String> refresh() {
        return hotelCache.invalidate(GRID_CACHE_KEY).chain(()->
            hotelCache.getAsync(GRID_CACHE_KEY, key ->
                googleSheetHandler.getHotelRoomUsage()
            )
            .map(roomSpecReport::process));
    }

    @Path("/specs")
    @GET
    public Uni<String> specs(){
        return getTheGrid().map(roomSpecReport::process);
    }


    @Path("/sets")
    @GET
    public Uni<String> sets(){
        return Uni.createFrom().item(roomSetReport.process(Collections.emptyList()));
    }

    private Uni<List<Hotel>> getTheGrid() {
        return Uni.createFrom().completionStage(
        hotelCache.as(CaffeineCache.class)
            .getIfPresent(GRID_CACHE_KEY));
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
