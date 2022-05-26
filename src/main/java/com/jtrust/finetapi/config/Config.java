package com.jtrust.finetapi.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.jtrust.finetapi.ErrorResponse;
import com.jtrust.finetapi.SwaggerPublicApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

@Configuration
@EnableSwagger2
@Slf4j
public class Config {


    @Autowired
    private TypeResolver typeResolver;

    @Autowired
    private Environment env;

    @Value("#{${aws.secret.manager}}")
    private Map<String, String> awsConfigProperties;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Bean()
    public FilterRegistrationBean basicAuthenticationFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        final Filter filter = new AuthenticationFilter();

        registration.addInitParameter("username", env.getProperty("basic.username"));
        registration.addInitParameter("secret", env.getProperty("basic.password"));
        registration.setFilter(filter);
        registration.addUrlPatterns("/api/*");
        registration.addUrlPatterns("/swagger-ui.html");
        registration.addUrlPatterns("/swagger-ui/*");
        registration.addUrlPatterns("/actuator/*");
        registration.setOrder(1);

        return registration;
    }

    @Bean
    public Map<String,String> loadAwsSecretBean(){
        log.info("=========LOADING AWS SECRET MANAGER CONFIG=============");
        log.info(gson.toJson(awsConfigProperties));
        return awsConfigProperties;
    }

    @Bean
    public  ApiInfo apiEndPointsInfo() {
        return (new ApiInfoBuilder())
                .title("Finet API")
                .description("Finet API")
                .termsOfServiceUrl("TCs")
                .version("1.0.0")
                .contact(new Contact("Sophea Mak", "JTRB", "sopheamak@gmail.com")).build();
    }

    @Bean
    public Docket documentApi() {
        ModelRef errorResponseModelRef = new ModelRef("ErrorResponse");
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiEndPointsInfo())
                .select()
               // .apis(RequestHandlerSelectors.basePackage("com.jtrust"))
                .apis(RequestHandlerSelectors.withMethodAnnotation(SwaggerPublicApi.class))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
                .ignoredParameterTypes(Resource.class)
                .useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.GET,
                        Arrays.asList(
                                new ResponseBuilder()
                                        .code(HttpStatus.BAD_REQUEST.value() + "")
                                        .description("Bad Request")
                                        .build()
                                ,
                                new ResponseBuilder()
                                        .code(HttpStatus.NOT_FOUND.value() + "")
                                        .description("Not Found")
                                        .build()
                        ))
                .globalResponses(HttpMethod.POST,
                        Arrays.asList(
                                new ResponseBuilder()
                                        .code(HttpStatus.BAD_REQUEST.value() +"")
                                        .description("Bad Request")
                                        .build()
                                ,
                                new ResponseBuilder()
                                        .code(HttpStatus.CONFLICT.value() +"")
                                        .description("Conflict")
                                        .build()
                ))
                .globalResponses(HttpMethod.PUT,
                        Arrays.asList(
                                new ResponseBuilder()
                                        .code(HttpStatus.BAD_REQUEST.value() +"")
                                        .description("Bad Request")
                                        .build()
                                ,
                                new ResponseBuilder()
                                        .code(HttpStatus.NOT_FOUND.value() +"")
                                        .description("Not Found")
                                        .build()
                ))
                .additionalModels(typeResolver.resolve(ErrorResponse.class))
        ;
    }


    /**
     * 2 beans below to fix with springfox version 3.x with springboot 2.6.x with application.properties
     * spring.mvc.pathmatch.matching-strategy = ANT_PATH_MATCHER
     *
     * Ref : https://github.com/springfox/springfox/issues/3462#issuecomment-1010721223
     * */

    @Bean
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier, ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier controllerEndpointsSupplier, EndpointMediaTypes endpointMediaTypes,
                                                                         CorsEndpointProperties corsProperties, WebEndpointProperties webEndpointProperties, Environment environment) {
        List<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
        String basePath = webEndpointProperties.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes, corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath), shouldRegisterLinksMapping);
    }



    private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties, Environment environment, String basePath) {
        return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath) || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
    }

    /**
     * @author Sophea Mak
     * @since Sept-2020
     *
     */
    @Slf4j
    @Component
    public static class AuthenticationFilter extends OncePerRequestFilter {

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
}
