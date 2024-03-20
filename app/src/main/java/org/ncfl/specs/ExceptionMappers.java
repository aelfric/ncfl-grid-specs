package org.ncfl.specs;

import jakarta.inject.Singleton;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

@Singleton
public class ExceptionMappers {
    @ServerExceptionMapper
    public RestResponse<String> mapException(NullPointerException x) {
        return RestResponse.status(Response.Status.OK, "NOT FOUND - UPLOAD A FILE");
    }
}
