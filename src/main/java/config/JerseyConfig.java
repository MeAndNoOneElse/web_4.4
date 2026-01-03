package config;

import controller.AuthResource;
import controller.ResultResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;
//import service.JwtAuthFilter;

@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(AuthResource.class);
        register(ResultResource.class);
//        register(JwtAuthFilter.class);
    }
}
