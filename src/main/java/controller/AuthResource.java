package controller;

import dto.AuthResponse;
import dto.LoginRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import service.AuthService;

@Component
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(origins = "http://localhost:4200")
public class AuthResource {

    @Autowired
    private AuthService authService;

    @POST
    @Path("/register")
    public Response register(LoginRequest request) {
        try {
            String token = authService.register(request);
            // используем конструктор (String token, String username)
            AuthResponse authResponse = new AuthResponse(token, request.getUsername());
            return Response.ok(authResponse).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        try {
            String token = authService.login(request);
            AuthResponse authResponse = new AuthResponse(token, request.getUsername());
            return Response.ok(authResponse).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/logout")
    public Response logout() {
        // при JWT чаще всего просто удаляем токен на фронте
        return Response.noContent().build();
    }
}
