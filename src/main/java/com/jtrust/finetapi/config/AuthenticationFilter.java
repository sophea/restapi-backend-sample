package com.jtrust.finetapi.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

@Slf4j
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION = "Authorization";

    private static final String BASIC = "BASIC ";

    private String username;

    protected String secret;

    @Override
    public void initFilterBean()  {
        final FilterConfig filterConfig = getFilterConfig();
        if (filterConfig != null) {
            username = filterConfig.getInitParameter("username");
            secret = filterConfig.getInitParameter("secret");
        }
        log.info("username :{}", username);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String header = request.getHeader(AUTHORIZATION);
        //check path
        final String uriPath = request.getRequestURI();

        if (uriPath.startsWith("/actuator/info") || uriPath.startsWith("/actuator/health")) {
            filterChain.doFilter(request, response);
            return;
        }

        //basic
        if ("/swagger-ui.html".equals(uriPath) || uriPath.startsWith("/swagger-ui") || uriPath.startsWith("/actuator/") || uriPath.startsWith("/api")) {
            checkBasicAuthentication(request, response, filterChain, header);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void checkBasicAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String header) throws IOException, ServletException {
        if (isBasicAuthenticated(header)) {
            filterChain.doFilter(request, response);
        } else {
            response.setHeader("WWW-Authenticate", "Basic realm=\"Backend API\"");
            response.setStatus(401);
        }
    }

    protected boolean isBasicAuthenticated(String authorization) {

        if (authorization == null || !authorization.toUpperCase(Locale.ENGLISH).startsWith(BASIC)) {
            return false;
        }
        // Get encoded user and password, comes after "BASIC "
        // Decode it, using any base 64 decoder

        final String authValue = org.apache.commons.lang3.StringUtils.toEncodedString(Base64.decodeBase64(authorization.substring(BASIC.length())),
                Charset.defaultCharset());
        final String clientId = getClientUsername(authValue);
        final String pwd = getClientPassword(authValue);

        return username.equals(clientId) && secret.equals(pwd);
    }

    private String getClientUsername(final String authValue) {
        String username = authValue;
        final int endIndex = authValue.indexOf(':');
        if (-1 < endIndex) {
            username = authValue.substring(0, endIndex);
        }
        return username;
    }

    private String getClientPassword(final String authValue) {
        String password = authValue;
        final int beginIndex = authValue.indexOf(':');
        if (-1 < beginIndex) {
            password = authValue.substring(beginIndex + 1);
        }
        return password;
    }

}
