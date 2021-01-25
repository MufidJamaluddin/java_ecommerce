package io.github.mufidjamaluddin.ecommerce.config;

import io.github.mufidjamaluddin.ecommerce.member.services.MemberService;
import io.github.mufidjamaluddin.ecommerce.security.TokenAuthenticationConverter;
import io.github.mufidjamaluddin.ecommerce.security.TokenProvider;
import io.github.mufidjamaluddin.ecommerce.security.jwt.JWTHeadersExchangeMatcher;
import io.github.mufidjamaluddin.ecommerce.security.jwt.JWTReactiveAuthenticationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;

@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
@ComponentScan("io.github.mufidjamaluddin.ecommerce")
public class SecurityConfiguration {

    private final ReactiveUserDetailsService reactiveUserDetailsService;
    private final TokenProvider tokenProvider;

    public SecurityConfiguration(
            MemberService memberService,
            TokenProvider tokenProvider
    ) {
        this.reactiveUserDetailsService = memberService;
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        http.httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .logout().disable();

        http.addFilterAt(webFilter(), SecurityWebFiltersOrder.AUTHORIZATION)
                .cors()
                .and()
                .authorizeExchange()
                .pathMatchers("member/profile")
                .authenticated();

        return http.build();
    }

    @Bean
    public AuthenticationWebFilter webFilter() {
        AuthenticationWebFilter authenticationWebFilter =
                new AuthenticationWebFilter(repositoryReactiveAuthenticationManager());

        authenticationWebFilter.setServerAuthenticationConverter(
                new TokenAuthenticationConverter(tokenProvider)::apply);

        authenticationWebFilter.setRequiresAuthenticationMatcher(
                new JWTHeadersExchangeMatcher());

        authenticationWebFilter.setSecurityContextRepository(
                new WebSessionServerSecurityContextRepository());

        return authenticationWebFilter;
    }

    @Bean
    public JWTReactiveAuthenticationManager repositoryReactiveAuthenticationManager() {
        return new JWTReactiveAuthenticationManager(
                reactiveUserDetailsService, passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
