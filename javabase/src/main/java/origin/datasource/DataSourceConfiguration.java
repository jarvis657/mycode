package origin.datasource;

import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Author:lmq
 * @Date: 2020/9/11
 * @Desc:
 **/
@Configuration
public class DataSourceConfiguration {

    @Resource(name = "ddd")
    private DataSource sdbDataSource;


    @Bean(name = "sharding")
    public DataSource shardingDataSource() throws SQLException {
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("sdb_sharding", sdbDataSource);
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.setDefaultDataSourceName("sdb_sharding");
        shardingRuleConfig.getTableRuleConfigs().add(useOpsLogsShardingRule());
        shardingRuleConfig.getTableRuleConfigs().add(ipOpsLogsShardingRule());
        shardingRuleConfig.getTableRuleConfigs().add(deviceOpsLogsShardingRule());
        Properties prop = new Properties();
        prop.setProperty("sql.show", "false");
        prop.setProperty("executor.size", "20");
        return ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, prop);
    }
    //<!--事务管理器-->
    //    <bean id="springTransactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    //        <property name="dataSource" ref="dataSource" />
    //    </bean>
    //
    //    <!--数据源-->
    //    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
    //        <property name="driverClassName" value="com.mysql.jdbc.Driver" />
    //        <property name="url" value="jdbc:mysql://127.0.0.1:3306/test?characterEncoding=utf8" />
    //        <property name="username" value="root" />
    //        <property name="password" value="123456" />
    //    </bean>
    //
    //    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    //        <property name="dataSource" ref="dataSource" />
    //        <!-- 指定sqlMapConfig总配置文件，订制的environment在spring容器中不在生效-->
    //        <!--指定实体类映射文件，可以指定同时指定某一包以及子包下面的所有配置文件，mapperLocations和configLocation有一个即可，当需要为实体类指定别名时，可指定configLocation属性，再在mybatis总配置文件中采用mapper引入实体类映射文件 -->
    //        <!--<property name="configLocation" value="classpath:fwportal/beans/dbconfig/mybatis.xml" />-->
    //        <property name="mapperLocations" value="classpath:mapper/*.xml" />
    //    </bean>
    //
    //    <!--将DAO接口注册为BEAN-->
    //    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    //        <property name="basePackage" value="TRANSACTION.DAO" />
    //    </bean>

    /**
     * 业务代码遇上异常处理事务
     */
    public void testTransaction() {
        TransactionStatus transactionStatus = TransactionAspectSupport.currentTransactionStatus();
        transactionStatus.setRollbackOnly();
    }


    private TableRuleConfiguration useOpsLogsShardingRule() {
        TableRuleConfiguration orderLogShardingRuleConfig = new TableRuleConfiguration("health_risk_user_op_log_info_sharding", "db_sharding.health_risk_user_op_log_info_sharding_${0..9}${0..9}}");
        orderLogShardingRuleConfig.setTableShardingStrategyConfig(
                new InlineShardingStrategyConfiguration("user_id", "health_risk_user_op_log_info_sharding_${String.format(\"%02d\", Math.abs(user_id) % 64)}"));
        return orderLogShardingRuleConfig;
    }

    private TableRuleConfiguration ipOpsLogsShardingRule() {
        TableRuleConfiguration orderLogShardingRuleConfig = new TableRuleConfiguration("health_risk_ip_op_log_info_sharding", "db_sharding.health_risk_ip_op_log_info_sharding_${0..9}${0..9}}");
        orderLogShardingRuleConfig.setTableShardingStrategyConfig(
                new InlineShardingStrategyConfiguration("ip", "health_risk_ip_op_log_info_sharding_${String.format(\"%02d\", (ip.hashCode() & 0x7FFFFFFF)  % 64)}"));
        return orderLogShardingRuleConfig;
    }

    private TableRuleConfiguration deviceOpsLogsShardingRule() {
        TableRuleConfiguration orderLogShardingRuleConfig = new TableRuleConfiguration("health_risk_device_op_log_info_sharding", "db_sharding.health_risk_device_op_log_info_sharding_${0..9}${0..9}}");
        orderLogShardingRuleConfig.setTableShardingStrategyConfig(
                new InlineShardingStrategyConfiguration("device", "health_risk_device_op_log_info_sharding_${String.format(\"%02d\", (device.hashCode() & 0x7FFFFFFF)  % 64)}"));
        return orderLogShardingRuleConfig;
    }
}

