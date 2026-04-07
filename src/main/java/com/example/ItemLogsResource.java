package com.example;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Separate resource with @Path("items/{id}") — causes RESTEasy Classic to
 * claim the entire /items/{id} prefix, breaking ItemsResource's sub-paths.
 */
@Path("items/{id}")
public class ItemLogsResource {

    @GET
    @Path("logs")
    @Produces(MediaType.TEXT_PLAIN)
    public String logs(@PathParam("id") String id) {
        return "Logs for item " + id;
    }

}
