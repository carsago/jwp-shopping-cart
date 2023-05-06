package cart.auth;

import cart.dto.request.AuthRequest;
import cart.service.AuthService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final AuthService authService;
    private final CredentialThreadLocal credentialThreadLocal;
    private final BasicAuthParser basicAuthParser;

    public AuthInterceptor(AuthService authService, CredentialThreadLocal credentialThreadLocal,
                           BasicAuthParser basicAuthParser) {
        this.authService = authService;
        this.credentialThreadLocal = credentialThreadLocal;
        this.basicAuthParser = basicAuthParser;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        AuthRequest authRequest = basicAuthParser.parse(authorization);
        Credential credential = authService.findCredential(authRequest);
        credentialThreadLocal.set(credential);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        credentialThreadLocal.clear();
    }
}