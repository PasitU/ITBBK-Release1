package sit.syone.itbkkapi.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import sit.syone.itbkkapi.exceptions.ExceptionForm;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class CustomAuthnEntrypoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ExceptionForm exceptionForm = new ExceptionForm(401, authException.getMessage(), "");
        response.setContentType("application/json");
        response.setStatus(401);
        OutputStream responseStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(responseStream, exceptionForm);
        responseStream.flush();
    }
}
