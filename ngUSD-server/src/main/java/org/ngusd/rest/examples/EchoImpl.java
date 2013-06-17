package org.ngusd.rest.examples;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ngusd.rest.generated.examples.Echo;

public class EchoImpl implements Echo {

    @Override
    public Response getEcho(final String resourceParam, final String methodParam) {
        return Response.ok().type(MediaType.TEXT_PLAIN).entity("getEcho: resourceParam=" + resourceParam + ", methodParam=" + methodParam).build();
    }

    @Override
    public void postEcho(final String resourceParam) {
        System.out.println("postEcho: resourceParam="+resourceParam);
    }

}
