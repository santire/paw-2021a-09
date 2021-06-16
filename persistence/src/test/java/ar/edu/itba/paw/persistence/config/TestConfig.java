package ar.edu.itba.paw.persistence.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hsqldb.jdbc.JDBCDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@ComponentScan({ "ar.edu.itba.paw.persistence" })
@Configuration
public class TestConfig {

  @Value("classpath:hsqldb.sql")
  private Resource hsqldbSql;
  @Value("classpath:schema.sql")
  private Resource schemaSql;
  @Bean
  public DataSource dataSource() {
    final SimpleDriverDataSource ds = new SimpleDriverDataSource();
    ds.setDriverClass(JDBCDriver.class);
    ds.setUrl("jdbc:hsqldb:mem:paw");
    ds.setUsername("ha");
    ds.setPassword("");
    
    return ds;
  }

  @Bean
  public DataSourceInitializer dataSourceInitializer(final DataSource ds) {
    final DataSourceInitializer dsi = new DataSourceInitializer();
    dsi.setDataSource(ds);
    dsi.setDatabasePopulator(databasePopulator());
    return dsi;
  }

  private DatabasePopulator databasePopulator() {
    final ResourceDatabasePopulator dbp = new ResourceDatabasePopulator();
    dbp.addScript(hsqldbSql);
    dbp.addScript(schemaSql);
    return dbp;
  }

  @Bean
  public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
    return new JpaTransactionManager(emf);
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
    jpaProperties.setProperty("spring.jpa.hibernate.ddl-auto", "none");
    jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");

    // Local Machine only, don't deploy!
    jpaProperties.setProperty("hibernate.show_sql", "false");
    jpaProperties.setProperty("format_sql", "false");

    entityFactory.setJpaProperties(jpaProperties);
    return entityFactory;
  }

  
}
