package origin.spring.jpas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;

/**
 * @Author:lmq
 * @Date: 2020/9/30
 * @Desc:
 **/

@Configuration
@EnableJpaRepositories(
        basePackages = "org.websparrow.repo",
        transactionManagerRef = "transcationManager",
        entityManagerFactoryRef = "entityManager")
@EnableTransactionManagement
public class DataSourceConfigJpa {
    @Autowired
    private BangkokDetails bangkokDetails;
    @Autowired
    private HongkongDetails hongkongDetails;

    @Bean
    @Primary
    @Autowired
    public DataSource dataSource() {
        DataSourceRouting routingDataSource = new DataSourceRouting();
        routingDataSource.initDatasource(bangkokDataSource(), hongkongDataSource());
        return routingDataSource;
    }

    public DataSource hongkongDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(hongkongDetails.getUrl());
        dataSource.setUsername(hongkongDetails.getUsername());
        dataSource.setPassword(hongkongDetails.getPassword());
        return dataSource;
    }

    public DataSource bangkokDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(bangkokDetails.getUrl());
        dataSource.setUsername(bangkokDetails.getUsername());
        dataSource.setPassword(bangkokDetails.getPassword());
        return dataSource;
    }

//    @Bean(name = "entityManager")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder) {
//        return builder.dataSource(dataSource()).packages(Employee.class)
//                .build();
//    }
//
//    @Bean(name = "transcationManager")
//    public JpaTransactionManager transactionManager(
//            @Autowired @Qualifier("entityManager") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
//        return new JpaTransactionManager(entityManagerFactoryBean.getObject());
//    }

    @Bean(name = "studentEntityManager")
    @Primary
    public LocalContainerEntityManagerFactoryBean studentEntityManager(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(hongkongDataSource()).packages(Employee.class).build();
    }

    //creates entity manager with scanned entity classes of school database
    @Bean(name = "schoolEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean schoolEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(bangkokDataSource()).packages(Employee.class).build();
    }

    // Transaction Manager for Student
    @Bean(name = "studentTransactionManager")
    @Primary
    public PlatformTransactionManager studentTransactionManager(
            @Qualifier("studentEntityManager") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactoryBean.getObject()));
    }

    // Transaction Manager for School
    @Bean(name = "schoolTransactionManager")
    public PlatformTransactionManager schoolTransactionManager(
            @Qualifier("schoolEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactoryBean.getObject()));
    }

    @Repository
    public interface SchoolRepository extends JpaRepository<Employee, Integer> {
    }

    @RestController
    public class MainController {

        @Autowired
        private SchoolRepository schoolRepository;
        @Autowired
        private SimpleJpaRepository<Employee, Integer> simpleJpaRepository;

        @GetMapping(value = "school")
        public ResponseEntity<List<Employee>> getSchool() {
            Specification<Employee> spec = Specification.where((root, query, criteriaBuilder) -> {
                return null;
            });
            simpleJpaRepository.findAll(spec, Pageable.unpaged());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(schoolRepository.findAll());
        }
    }

    private class DataSourceRouting extends AbstractRoutingDataSource {
        public void initDatasource(DataSource bangkokDataSource, DataSource hongkongDataSource) {
        }

        @Override
        protected Object determineCurrentLookupKey() {
            return null;
        }
    }

    private class BangkokDetails {
        public String getUrl() {
            return "";
        }

        public String getUsername() {
            return "";
        }

        public String getPassword() {
            return "";
        }
    }

    private class HongkongDetails {
        public String getUrl() {
            return "";
        }

        public String getUsername() {
            return "";
        }

        public String getPassword() {
            return "";
        }
    }

    @Entity
    @Table(name = "student", schema = "studentdb")
    private class Employee {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        private String name;
        private int age;
    }
}
