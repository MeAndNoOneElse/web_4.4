package config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        // Пакет(ы), где лежат твои @Path‑ресурсы
        packages("controller", "config");

        // Регистрация CORS фильтра
        register(CorsFilter.class);
    }
}
