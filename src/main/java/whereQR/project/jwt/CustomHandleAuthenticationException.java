package whereQR.project.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class CustomHandleAuthenticationException {
    public static void handleAuthenticationException(HttpServletResponse response, String message ) throws IOException {
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> responseBody = new HashMap<>();
        Map<String, Object> responseBodyData = new HashMap<>();
        responseBodyData.put("message", message);
        responseBodyData.put("errorType", "UNAUTHORIZED"); // filterchain exceptino-> unauthorized로 처리
        responseBody.put("status", "FAILED");
        responseBody.put("data", responseBodyData);

        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(responseBody));
        writer.flush();
        writer.close();
    }
}
