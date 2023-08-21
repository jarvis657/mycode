package origin.mysql.multiDataSource;

import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.github.pagehelper.PageInterceptor;
import origin.mysql.typeHandlers.LongToStringTypeHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Properties;

//@Configuration
//@AutoConfigureBefore(XXConfiguration.class)
//@MapperScan(basePackages = "xxx.dao", annotationClass = DataSourceScan.class, sqlSessionFactoryRef = DataSourceConfiguration.SQL_SESSION_FACTORY_NAME)
public class DataSourceConfiguration {
    public static final String DATA_SOURCE_NAME = "mysqlSqlDatasource";
    public static final String JDBC_TEMPLATE_NAME = "mysqlJdbcTemplate";
    public static final String SQL_SESSION_FACTORY_NAME = "mysqlSqlSessionFactory";
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfiguration.class);

    @Autowired
    private ResourceLoader resourceLoader;

    @Bean(name = DATA_SOURCE_NAME)
    @Primary// 必须有一个主数据源，而且只能有一个，否则启动会报错，springboot默认的主数据源
//    public DataSource mysqlDatasource(Environment env) {
    public DataSource datasource() {
        LOGGER.info("init dohko origin.mysql tddl datasource success, {}", "url");
        return DataSourceBuilder.create().driverClassName("drivername")
                .url("url").type(DataSource.class).build();
    }

    @Bean(name = SQL_SESSION_FACTORY_NAME)
    @Primary// 必须有一个主数据源，而且只能有一个，否则启动会报错，springboot默认的主数据源
    public SqlSessionFactory mysqlSqlSessionFactory(@Qualifier(DATA_SOURCE_NAME) DataSource mysqlDataSource)
            throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
        //通过设置pageSize=0或者RowBounds.limit = 0就会查询出全部的结果。
        p.setProperty("pageSizeZero", "true");
        p.setProperty("helperDialect", "origin/mysql");
        p.setProperty("supportMethodsArguments", "true");
        p.setProperty("params", "count=countSql");
        p.setProperty("autoRuntimeDialect", "false");
        pageInterceptor.setProperties(p);
        PageInterceptor[] plugins = {pageInterceptor};
        factory.setPlugins(plugins);
        factory.setDataSource(mysqlDataSource);
        factory.setTypeAliases(new Class[]{LongToStringTypeHandler.class});
        factory.setVfs(SpringBootVFS.class);
//        factory.setConfigLocation(this.resourceLoader.getResource(mysqlProperties.getMybatisConfigPath()));
        LOGGER.info("init dohko origin.mysql tddl mysqlSqlSessionFactory success, dataSource = {}, appName = {}", DATA_SOURCE_NAME, "url");
        return factory.getObject();
    }

    @Bean(name = JDBC_TEMPLATE_NAME)
    public JdbcTemplate mysqlJdbcTemplate(@Qualifier(DATA_SOURCE_NAME) DataSource mysqlDataSource) {
        return new JdbcTemplate(mysqlDataSource);
    }
}
