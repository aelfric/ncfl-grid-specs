package org.ncfl.specs;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@Path("/")
public class GreetingResource {

    private final RoomSpecReport roomSpecReport;

    @Inject
    public GreetingResource(RoomSpecReport roomSpecReport) {
        this.roomSpecReport = roomSpecReport;
    }

    @Path("/hello")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from RESTEasy Reactive";
    }

    @Path("/upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_HTML)
    public String upload(@RestForm("file") FileUpload file) {
        return roomSpecReport.slurp(file.uploadedFile().toFile());
    }
}
