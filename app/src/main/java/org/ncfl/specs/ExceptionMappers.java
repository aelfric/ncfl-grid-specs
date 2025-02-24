package org.ncfl.specs;

import jakarta.inject.Singleton;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

@Singleton
public class ExceptionMappers {
    protected static final Logger logger = Logger.getLogger(ExceptionMappers.class);

    @ServerExceptionMapper
    public RestResponse<String> mapException(NullPointerException x) {
        logger.error("Unexpected null value", x);
        return RestResponse.status(Response.Status.OK, "NOT FOUND - UPLOAD A FILE");
    }
}
