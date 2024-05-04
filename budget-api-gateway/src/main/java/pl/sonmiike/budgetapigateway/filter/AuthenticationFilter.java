package pl.sonmiike.budgetapigateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import pl.sonmiike.budgetapigateway.util.ApiError;
import pl.sonmiike.budgetapigateway.util.JwtUtil;
import reactor.core.publisher.Mono;

@Component
@RefreshScope
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {


    @Autowired
    private RouteValidator validator;
    @Autowired
    private JwtUtil jwtUtils;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (validator.isSecured.test(request)) {
                if (authMissing(request)) {
                    return onError(exchange);
                }

                final String token = request.getHeaders().getOrEmpty("Authorization").get(0);

                if (jwtUtils.isExpired(token)) {
                    return onError(exchange);
                }
            }
            return chain.filter(exchange);
        });
    }

    private Mono<Void> onError(ServerWebExchange exchange) {
        String errorMessage = "Authorization header is missing or token is expired";

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ApiError apiError = new ApiError(
                exchange.getRequest().getPath().value(),
                errorMessage,
                HttpStatus.UNAUTHORIZED.value()
        );

        ObjectMapper objectMapper = new ObjectMapper();
        byte[] data;
        try {
            data = objectMapper.writeValueAsBytes(apiError);
        } catch (Exception e) {
            data = new byte[0];
        }

        DataBuffer buffer = response.bufferFactory().wrap(data);
        return response.writeWith(Mono.just(buffer));
    }

    private boolean authMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    public static class Config {
    }

}
