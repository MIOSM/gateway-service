package MIOSM.gateway_service.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/auth/**")
                        .uri("lb://auth-service"))
                .route("auth-service-user-update", r -> r.path("/user/update")
                        .uri("lb://auth-service"))
                .route("auth-service-user-profile", r -> r.path("/user/profile")
                        .filters(f -> f.rewritePath("/user/profile", "/auth/me"))
                        .uri("lb://auth-service"))
                .route("user-service", r -> r.path("/user/**")
                        .uri("lb://user-service"))
                .route("user-service-images", r -> r.path("/api/images/**")
                        .uri("lb://user-service"))
                .route("post-service", r -> r.path("/post-service/**")
                        .filters(f -> f.rewritePath("/post-service/(?<segment>.*)", "/${segment}"))
                        .uri("lb://post-service"))
                .build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("http://localhost:4200");
        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedHeader("*");
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}
