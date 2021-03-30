package ar.edu.itba.paw.webapp.config;

import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.sql.DataSource;
import java.util.Properties;

@EnableWebMvc
@ComponentScan({
        "ar.edu.itba.paw.webapp.controller",
        "ar.edu.itba.paw.service",
        "ar.edu.itba.paw.persistence",
})



@Configuration
public class WebConfig {
  @Bean
  public ViewResolver viewResolver() {
    final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

    viewResolver.setViewClass(JstlView.class);
    viewResolver.setPrefix("/WEB-INF/jsp/");
    viewResolver.setSuffix(".jsp");

    return viewResolver;
  }

  @Bean
  public DataSource dataSource(){

    final SimpleDriverDataSource ds = new SimpleDriverDataSource();
    ds.setDriverClass(org.postgresql.Driver.class);
    ds.setUrl("jdbc:postgresql://localhost/paw");
    ds.setUsername("root");
    ds.setPassword("root");

    return ds;
  }

  @Bean
  public JavaMailSender getJavaMailSender() {
      JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
      mailSender.setHost("smtp.gmail.com");
      mailSender.setPort(587);

      mailSender.setUsername("recordpaw2020@gmail.com");
      mailSender.setPassword("twytgaptuqsrpurs");

      Properties props = mailSender.getJavaMailProperties();
      props.put("mail.transport.protocol", "smtp");
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.debug", "true");

         return mailSender;
        }


}
