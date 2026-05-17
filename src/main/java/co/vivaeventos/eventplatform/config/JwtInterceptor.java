package co.vivaeventos.eventplatform.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final String SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        String path = request.getRequestURI();
        String method = request.getMethod();
        
        // GET requests: permitir sin autenticación (clientes viendo eventos)
        if (method.equals("GET")) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(401);
            response.getWriter().write("Token no proporcionado");
            return false;
        }

        String token = authHeader.substring(7);
        
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            String role = claims.get("role", String.class);
            
            // Permitir reserva de boletos para CLIENT, ORGANIZER y ADMIN
            if (path.contains("/ticket-types/") && path.contains("/reserve")) {
                if (role.equals("CLIENT") || role.equals("ORGANIZER") || role.equals("ADMIN")) {
                    return true;
                }
            }
            
            // POST, PUT, DELETE en eventos: solo ORGANIZER o ADMIN
            if (method.equals("POST") || method.equals("PUT") || method.equals("DELETE")) {
                if (!role.equals("ORGANIZER") && !role.equals("ADMIN")) {
                    response.setStatus(403);
                    response.getWriter().write("No tienes permiso para realizar esta acción");
                    return false;
                }
            }
            
            request.setAttribute("userId", claims.getSubject());
            request.setAttribute("userRole", role);
            
            return true;
            
        } catch (Exception e) {
            response.setStatus(401);
            response.getWriter().write("Token inválido");
            return false;
        }
    }
}