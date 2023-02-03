package com.luv2code.ecommerce.config;

import com.okta.spring.boot.oauth.Okta;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // protect endpoint /api/orders
        http.authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api",
                                "/api/profile",
                                "/api/products/**",
                                "/api/product-category/**",
                                "/api/countries/**",
                                "/api/states/**"
                        )
                        .anonymous()
                        .requestMatchers(HttpMethod.POST, "/api/checkout/purchase").anonymous()
                        .requestMatchers(HttpMethod.POST, "/api/checkout/payment-intent").anonymous()
                        .anyRequest()
                        .authenticated())
                .oauth2ResourceServer()
                .jwt();

        // add CORS filters
        http.cors();

        // disable CSRF
        http.csrf().disable();

        // add content negotiation strategy
        http.setSharedObject(ContentNegotiationStrategy.class,
                new HeaderContentNegotiationStrategy());

        // force a non-empty response body for 401's to make the response more friendly
        Okta.configureResourceServer401ResponseBody(http);

        return http.build();
    }
}
