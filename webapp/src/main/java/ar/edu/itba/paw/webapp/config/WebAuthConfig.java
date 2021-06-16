package ar.edu.itba.paw.webapp.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan({"ar.edu.itba.paw.webapp.auth"})
@Configuration
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetails;

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
        auth.userDetailsService(userDetails)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String key ="";
        try {
            key = getFileFromResources("key.txt");
        } catch (Exception e) {
            // Ignore
        }


        http.sessionManagement()
                .invalidSessionUrl("/")
                .and().authorizeRequests()
                .antMatchers("/login", "/register").anonymous()
                .antMatchers("/user/*",
                             "/user/edit",
                             "/register/restaurant",
                             "/restaurants/user/*").hasRole("USER")
                .antMatchers("/reservations",
                             "/reservations/*/cancel",
                             "/reservations/history",
                             "/restaurant/*/rate",
                             "/restaurant/*/dislike",
                             "/restaurant/*/like",
                             "/restaurant/*/reviews/delete"
                             ).hasRole("USER")
                .antMatchers("/restaurant/*/edit",
                             "/restaurant/*/delete",
                             "/restaurant/*/menu",
                             "/restaurant/*/delete/*",
                             "/restaurant/*/manage/confirmed",
                             "/reservations/*/*/cancel",
                             "/reservations/*/*/reject",
                             "/reservations/*/*/confirm",
                             "/restaurant/*/manage/pending").hasRole("RESTAURANTOWNER")
                .and().formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/", false)
                .and().rememberMe()
                .rememberMeParameter("rememberme")
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(365))
                .key(key)
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .and().exceptionHandling()
                .accessDeniedPage("/403")
                .and().csrf().disable();
    }

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
