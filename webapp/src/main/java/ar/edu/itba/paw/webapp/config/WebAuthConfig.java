package ar.edu.itba.paw.webapp.config;


import ar.edu.itba.paw.service.JwtService;
import ar.edu.itba.paw.webapp.auth.ApiEntryPoint;
import ar.edu.itba.paw.webapp.auth.filters.BasicAuthenticationWithJwtHeaderFilter;
import ar.edu.itba.paw.webapp.auth.filters.CorsFilter;
import ar.edu.itba.paw.webapp.auth.filters.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan({"ar.edu.itba.paw.webapp.auth", "ar.edu.itba.paw.webapp.utils", "ar.edu.itba.paw.webapp.controller"})
@Configuration
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtService tokenService;

    public static String getFileFromResources(String fileName) throws Exception {
        ClassLoader classLoader = WebAuthConfig.class.getClassLoader();
        InputStream stream = classLoader.getResourceAsStream(fileName);
        String text = null;
        try (Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8.name())) {
            text = scanner.useDelimiter("\\A").next();
        }
        return text;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // Configure filters
        http.userDetailsService(userDetailsService)
                .addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class)
                .addFilterBefore(new JwtRequestFilter(userDetailsService, tokenService), BasicAuthenticationFilter.class)
                .addFilterAt(new BasicAuthenticationWithJwtHeaderFilter(authenticationManager(),
                        new ApiEntryPoint(),
                        userDetailsService,
                        tokenService), BasicAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers()
                .cacheControl().disable()
                .and()
                .csrf().disable()
                .logout().disable()
                .rememberMe().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,
                        "/api/restaurants",
                        "/api/restaurants/*",
                        "/api/restaurants/*/image",
                        "/api/restaurants/*/menu",
                        "/api/restaurants/*/menu/*",
                        "/api/comments",
                        "/api/comments/*",
                        "/api/tags"
                ).permitAll()
                .antMatchers(HttpMethod.POST, "/api/users").permitAll()
                // For activation and password recovery
                .antMatchers(HttpMethod.PATCH, "/api/users/*").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
                .antMatchers("/api/**").authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new ApiEntryPoint())
                .and()
                .httpBasic();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/static/**", "/index.html", "/", "/locales/**")
                .antMatchers("/**.png", "/**.json", "/**.ico", "/**.txt");
    }
}
