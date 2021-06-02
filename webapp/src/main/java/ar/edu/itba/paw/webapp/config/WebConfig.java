package ar.edu.itba.paw.webapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Properties;

@EnableTransactionManagement
@EnableScheduling
@EnableWebMvc
@ComponentScan({ "ar.edu.itba.paw.webapp.controller", "ar.edu.itba.paw.service", "ar.edu.itba.paw.persistence", })
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
  public DataSource dataSource() {

    final SimpleDriverDataSource ds = new SimpleDriverDataSource();
    ds.setDriverClass(org.postgresql.Driver.class);
     ds.setUrl("jdbc:postgresql://localhost/paw");
     ds.setUsername("postgres");
     ds.setPassword("postgres");

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
  public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
    return new JpaTransactionManager(emf);
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
    
    // Local Machine only, don't deploy!
    props.put("mail.debug", "false");

    return mailSender;
  }

  @Bean(name = "multipartResolver")
  public CommonsMultipartResolver multipartResolver() {
    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
    multipartResolver.setMaxUploadSize(-1);
    return multipartResolver;
  }



  @Bean
  public LocaleResolver localeResolver() {
    CookieLocaleResolver clr = new CookieLocaleResolver();
    return clr;
  }

  @Bean
  public WebMvcConfigurer configurer(){
    return new WebMvcConfigurerAdapter() {
      @Override
      public void addInterceptors (InterceptorRegistry registry) {
        LocaleChangeInterceptor l = new LocaleChangeInterceptor();
        l.setParamName("lang");
        registry.addInterceptor(l);
      }
    };
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    final  LocalContainerEntityManagerFactoryBean entityFactory = new LocalContainerEntityManagerFactoryBean();

    entityFactory.setPackagesToScan("ar.edu.itba.paw.model");
    entityFactory.setDataSource(dataSource());

    JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
    entityFactory.setJpaVendorAdapter(jpaVendorAdapter);

    final Properties jpaProperties = new Properties();
    jpaProperties.setProperty("hibernate.hbm2ddl.auto", "update");
    jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL92Dialect");

    // Local Machine only, don't deploy!
    jpaProperties.setProperty("hibernate.show_sql", "true");
    jpaProperties.setProperty("format_sql", "true");

    entityFactory.setJpaProperties(jpaProperties);
    return entityFactory;
  }

}
