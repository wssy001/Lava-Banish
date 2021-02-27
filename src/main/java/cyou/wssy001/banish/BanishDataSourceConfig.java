//package cyou.wssy001.banish;
//
//import com.baomidou.dynamic.datasource.creator.HikariDataSourceCreator;
//import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
//import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.hikari.HikariCpConfig;
//import lombok.RequiredArgsConstructor;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//
///**
// * @projectName: lava-banish
// * @className: BanishDataSourceConfig
// * @description:
// * @author: alexpetertyler
// * @date: 2021/1/31
// * @version: v1.0
// */
//@Configuration
//@MapperScan("cyou.wssy001.banish.dao")
//@RequiredArgsConstructor
//public class BanishDataSourceConfig {
//    private final HikariDataSourceCreator hikariDataSourceCreator;
//
//    @Bean
//    public DataSource banishDataSource() {
//        DataSourceProperty property = new DataSourceProperty();
//        property.setUsername("root");
//        property.setPassword("wssy001");
//        property.setUrl("jdbc:mysql://localhost:3306/banish_local?useUnicode=true&useSSL=false&autoReconnect=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&rewriteBatchedStatements=true");
//        property.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        HikariCpConfig hikariConfig = new HikariCpConfig();
//        hikariConfig.setIdleTimeout(43170000L);
//        hikariConfig.setMaxLifetime(43170000L);
//        hikariConfig.setMaxPoolSize(20);
//        hikariConfig.setMinIdle(5);
//        hikariConfig.setConnectionTimeout(43170000L);
//        property.setHikari(hikariConfig);
//
//        return hikariDataSourceCreator.createDataSource(property);
//    }
//}