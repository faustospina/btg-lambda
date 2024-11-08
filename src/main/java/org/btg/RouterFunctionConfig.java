package org.btg;


import org.btg.handler.ClienteHandler;
import org.btg.handler.FondoHandler;
import org.btg.handler.TransaccionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterFunctionConfig {

    @Bean
    public RouterFunction<ServerResponse> routesCliente(ClienteHandler handler) {
        return route(POST("cliente"),handler::createCliente)
                .andRoute(GET("cliente/{id}"),handler::findCliente)
                .andRoute(GET("clientes"),handler::findAllClientes)
                .andRoute(POST("cliente/{id}/fondos/agregar"), handler::addFondosCliente)
                .andRoute(POST("cliente/{id}/fondos/eliminar"), handler::removeFondosCliente);
    }

    @Bean
    public RouterFunction<ServerResponse> routesFondo(FondoHandler handler) {
        return route(POST("fondo"), handler::createFondo)
                .andRoute(GET("fondo/{id}"), handler::findFondo)
                .andRoute(GET("fondos"), handler::findAllFondos);
    }

    @Bean
    public RouterFunction<ServerResponse> routesTransaccion(TransaccionHandler handler) {
        return route(POST("transaccion"), handler::createTransaccion)
                .andRoute(GET("transaccion/{id}"), handler::findTransaccion)
                .andRoute(GET("transacciones"), handler::findAllTransacciones)
                .andRoute(GET("transaccion/cliente/{idCliente}"), handler::findTransaccionByCliente);
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:4200"); // Cambia esto por el origen de tu aplicación Angular
        config.addAllowedMethod("*"); // Permite todos los métodos HTTP
        config.addAllowedHeader("*"); // Permite todos los encabezados
        config.setAllowCredentials(true); // Habilita el envío de credenciales

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Aplica la configuración a todas las rutas

        return new CorsWebFilter(source);
    }




}
