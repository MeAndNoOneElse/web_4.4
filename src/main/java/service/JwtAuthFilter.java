package service;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
@Component // <<< ВАЖНО: чтобы Spring создавал бин
public class JwtAuthFilter implements ContainerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        return;
//        String path = requestContext.getUriInfo().getPath();
//
//        if (path.startsWith("auth/register") || path.startsWith("auth/login")) {
//            return;
//        }
//
//        String authHeader = requestContext.getHeaderString("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
//                    .entity("Missing or invalid Authorization header")
//                    .build());
//            return;
//        }
//
//        String token = authHeader.substring("Bearer ".length());
//        if (!jwtService.validateToken(token)) {
//            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
//                    .entity("Invalid token")
//                    .build());
//            return;
//        }
//
//        String username = jwtService.getUsername(token);
//        requestContext.setProperty("username", username);
    }
}
