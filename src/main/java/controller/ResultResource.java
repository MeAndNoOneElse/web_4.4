package controller;

import dto.PointRequest;
import dto.ResultResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import service.ResultService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Path("/results")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(origins ="http://localhost:4200")
public class ResultResource {

    @Autowired
    private ResultService resultService;

    // временно: используем одного «тестового» пользователя
    private static final String TEST_USERNAME = "demo";
    @POST
    @Path("/check")
    public Response checkPoint(PointRequest request, @Context ContainerRequestContext ctx) {
//        String username = (String) ctx.getProperty("username");
        String username = TEST_USERNAME;
        if (username == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build();
        }

        ResultResponse result = resultService.checkAndSave(username, request);
        return Response.ok(result).build();
    }

    @GET
    public Response getUserResults(@Context ContainerRequestContext ctx) {
        String username = (String) ctx.getProperty("username");
        if (username == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build();
        }

        List<ResultResponse> results = resultService.getUserResults(username);
        return Response.ok(results).build();
    }

    @DELETE
    public Response clearResults(@Context ContainerRequestContext ctx) {
        String username = (String) ctx.getProperty("username");
        if (username == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build();
        }

        resultService.clearUserResults(username);
        return Response.noContent().build();
    }
    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }

    private Map<String, String> createSuccessResponse(String message) {
        Map<String, String> success = new HashMap<>();
        success.put("message", message);
        return success;
    }
}
