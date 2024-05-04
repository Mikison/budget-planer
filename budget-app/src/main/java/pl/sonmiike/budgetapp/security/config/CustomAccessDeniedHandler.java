package pl.sonmiike.budgetapp.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import pl.sonmiike.budgetapp.exceptions.custom.ApiError;

import java.io.IOException;
import java.util.List;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String ACCESS_DENIED_MESSAGE = "Access denied";
        ApiError apiError = new ApiError(request.getRequestURI(), List.of(ACCESS_DENIED_MESSAGE), HttpStatus.FORBIDDEN.value());

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(apiError));
        response.getOutputStream().flush();
    }
}
