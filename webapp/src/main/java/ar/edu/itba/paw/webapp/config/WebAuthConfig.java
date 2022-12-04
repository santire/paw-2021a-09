package ar.edu.itba.paw.webapp.config;


import ar.edu.itba.paw.webapp.auth.filters.login.LoginAuthFailureHandler;
import ar.edu.itba.paw.webapp.auth.filters.login.LoginAuthFilter;
import ar.edu.itba.paw.webapp.auth.filters.login.LoginAuthSuccessHandler;
import ar.edu.itba.paw.webapp.auth.filters.session.SessionAuthFailureHandler;
import ar.edu.itba.paw.webapp.auth.filters.session.SessionAuthFilter;
import ar.edu.itba.paw.webapp.auth.filters.session.SessionAuthSuccessHandler;
import ar.edu.itba.paw.webapp.auth.token.JWTUserDetailsAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.Filter;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan({"ar.edu.itba.paw.webapp.auth"})
@Configuration
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetails;

    @Autowired
    private JWTUserDetailsAuthProvider jwtUserDetailsAuthProvider;

    @Autowired
    private LoginAuthSuccessHandler loginAuthSuccessHandler;

    @Autowired
    private LoginAuthFailureHandler loginAuthFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
        }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.authenticationProvider(jwtUserDetailsAuthProvider)
                .userDetailsService(userDetails)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // Configure filters
        http.userDetailsService(userDetails)
                .addFilterBefore(createLoginAuthFilter(), (Class<? extends Filter>) UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(createSessionAuthFilter(), (Class<? extends Filter>) UsernamePasswordAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().logout().disable()
                .rememberMe().disable()
                .csrf().disable();
    }

    @Bean
    public Filter createLoginAuthFilter() throws Exception {
        LoginAuthFilter filter = new LoginAuthFilter();
        filter.setRequiresAuthenticationRequestMatcher(new RegexRequestMatcher("/login", "POST"));
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(loginAuthSuccessHandler);
        filter.setAuthenticationFailureHandler(loginAuthFailureHandler);
        return filter;
    }

    @Bean
    public Filter createSessionAuthFilter() throws Exception {
        SessionAuthFilter filter = new SessionAuthFilter();
        filter.setAuthenticationManager(authenticationManager());
        //filter.setRequiresAuthenticationRequestMatcher(needAuthEndpointsMatcher());
        filter.setAuthenticationSuccessHandler(new SessionAuthSuccessHandler());
        filter.setAuthenticationFailureHandler(new SessionAuthFailureHandler());
        return filter;
    }

    @Bean
    public RequestMatcher needAuthEndpointsMatcher() {
        return new OrRequestMatcher(
                new AntPathRequestMatcher("/hello/me", "GET"),
                new AntPathRequestMatcher("/users/*", "GET"),
                optionalAuthEndpointsMatcher(),
                restaurantOwnerEndpointsMatcher()
        );
    }

    @Bean
    public RequestMatcher optionalAuthEndpointsMatcher() {
        return new OrRequestMatcher(
                new AntPathRequestMatcher("/", "GET"),
                new AntPathRequestMatcher("/hello", "GET"),
                new AntPathRequestMatcher("/login", "POST"),
                new AntPathRequestMatcher("/login", "GET")
        );
    }

    @Bean
    public RequestMatcher restaurantOwnerEndpointsMatcher() {
        return new OrRequestMatcher(
                new AntPathRequestMatcher("/restaurant/*", "GET")
        );
    }


//    @Bean
//    public RequestMatcher needAuthEndpointsMatcher() {
//        return new OrRequestMatcher(
//                new AntPathRequestMatcher("/api/users/?", "POST"),
//                new AntPathRequestMatcher("/api/users/?", "GET"),
//                new AntPathRequestMatcher("/api/users/**", "POST"),
//                new AntPathRequestMatcher("/api/users/**", "GET"),
//                new AntPathRequestMatcher("/api/events", "GET"),
//                new AntPathRequestMatcher("/api/users", "GET"),
//                new AntPathRequestMatcher("/api/users/profile/", "GET"),
//                new AntPathRequestMatcher("/api/users/profile/**", "GET"),
//                optionalAuthEndpointsMatcher(),
//                organizerEndpointsMatcher(),
//                racerEndpointsMatcher()
//        );
//    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        String key ="";
//        try {
//            key = getFileFromResources("key.txt");
//        } catch (Exception e) {
//            // Ignore
//        }
//
//
//        http.sessionManagement()
//                .invalidSessionUrl("/")
//                .and().authorizeRequests()
//                .antMatchers("/login", "/register").anonymous()
//                .antMatchers("/user/*",
//                             "/user/edit",
//                             "/register/restaurant",
//                             "/restaurants/user/*").hasRole("USER")
//                .antMatchers("/reservations",
//                             "/reservations/*/cancel",
//                             "/reservations/history",
//                             "/restaurant/*/rate",
//                             "/restaurant/*/dislike",
//                             "/restaurant/*/like",
//                             "/restaurant/*/reviews/delete"
//                             ).hasRole("USER")
//                .antMatchers("/restaurant/*/edit",
//                             "/restaurant/*/delete",
//                             "/restaurant/*/menu",
//                             "/restaurant/*/delete/*",
//                             "/restaurant/*/manage/confirmed",
//                             "/reservations/*/*/cancel",
//                             "/reservations/*/*/reject",
//                             "/reservations/*/*/confirm",
//                             "/restaurant/*/manage/pending").hasRole("RESTAURANTOWNER")
//                .antMatchers("/**").permitAll()
//                .and().formLogin()
//                .loginPage("/login")
//                .usernameParameter("email")
//                .passwordParameter("password")
//                .defaultSuccessUrl("/", false)
//                .and().rememberMe()
//                .rememberMeParameter("rememberme")
//                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(365))
//                .key(key)
//                .and().logout()
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/login")
//                .and().exceptionHandling()
//                .accessDeniedPage("/403")
//                .and().csrf().disable();
//    }

    @Override
    public void configure(WebSecurity web) throws Exception{
              web.ignoring()
                      .antMatchers("/css/**", "/images/**", "/js/**", "/favicon.ico" );
            }



    public static String getFileFromResources(String fileName) throws Exception {
        ClassLoader classLoader = WebAuthConfig.class.getClassLoader();
        InputStream stream = classLoader.getResourceAsStream(fileName);
        String text = null;
        try (Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8.name())) {
            text = scanner.useDelimiter("\\A").next();
        }
        return text;
    }
}
