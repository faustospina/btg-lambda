package org.btg;


import org.btg.handler.ClienteHandler;
import org.btg.handler.FondoHandler;
import org.btg.handler.TransaccionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.servlet.function.RouterFunctions;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterFunctionConfig {

    @Bean
    public RouterFunction<ServerResponse> routesCliente(ClienteHandler handler) {
        return route(POST("cliente"),handler::createCliente)
                .andRoute(GET("cliente/{id}"),handler::findCliente)
                .andRoute(GET("producto"),handler::findAllClientes);
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
                .andRoute(GET("transacciones"), handler::findAllTransacciones);
    }




}
