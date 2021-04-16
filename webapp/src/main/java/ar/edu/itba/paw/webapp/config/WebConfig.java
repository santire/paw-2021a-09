package ar.edu.itba.paw.webapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.sql.DataSource;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

@EnableWebMvc
@ComponentScan({
        "ar.edu.itba.paw.webapp.controller",
        "ar.edu.itba.paw.service",
        "ar.edu.itba.paw.persistence",
})



@Configuration
public class WebConfig {

  @Value("classpath:schema.sql")
  private Resource schemaSql;

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


    // paw server
      // ds.setUrl("jdbc:postgresql://10.16.1.110/paw-2021a-09");
      // ds.setUsername("paw-2021a-09");
      // ds.setPassword("6jnqLFj1g");

    // remote testing database (very slow)

    // ds.setUrl("jdbc:postgresql://santire.heliohost.us/santire_paw");
    // ds.setUsername("santire_root");
    // ds.setPassword("santire_root");

    return ds;
  }

  @Bean
  public MessageSource messageSource() {
    final ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();

    ms.setBasename("classpath:i18n/messages");
    ms.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
    ms.setCacheSeconds(5);

    return ms;
  }

  @Bean
  public DataSourceInitializer DataSourceInitializer(final DataSource ds) {

    final DataSourceInitializer dsi = new DataSourceInitializer();
    dsi.setDataSource(ds);
    dsi.setDatabasePopulator(databasePopulator());
    return dsi;
  }

  private DatabasePopulator databasePopulator() {
    final ResourceDatabasePopulator dbp = new ResourceDatabasePopulator();
    dbp.addScript(schemaSql);
    return dbp;
  }

  @Bean
  public JavaMailSender getJavaMailSender() {
      JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
      mailSender.setHost("smtp.gmail.com");
      mailSender.setPort(587);

      mailSender.setUsername("gourmetablewebapp@gmail.com");
      mailSender.setPassword("evjotkxuvnmjknuu");

      Properties props = mailSender.getJavaMailProperties();
      props.put("mail.transport.protocol", "smtp");
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.debug", "true");

         return mailSender;
        }


}
