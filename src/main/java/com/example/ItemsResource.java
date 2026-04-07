package com.example;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Map;

/**
 * Primary resource for /items. Handles list, get-by-id, and details sub-paths.
 */
@Path("items")
public class ItemsResource {

    private static final Map<String, String> ITEMS = Map.of(
            "1", "Alpha",
            "2", "Bravo",
            "3", "Charlie"
    );

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> list() {
        return List.copyOf(ITEMS.values());
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String get(@PathParam("id") String id) {
        String item = ITEMS.get(id);
        if (item == null)
            return "Not found";
        return item;
    }

    @GET
    @Path("{id}/details")
    @Produces(MediaType.TEXT_PLAIN)
    public String details(@PathParam("id") String id) {
        String item = ITEMS.get(id);
        if (item == null)
            return "Not found";
        return "Details for: " + item;
    }

}
