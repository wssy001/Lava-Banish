package cyou.wssy001.banish.controller;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.HikariDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.hikari.HikariCpConfig;
import cyou.wssy001.banish.dto.BanishConfig;
import cyou.wssy001.banish.dto.DataSourceDto;
import cyou.wssy001.banish.service.BanishConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @projectName: lava-banish
 * @className: ConfigController
 * @description:
 * @author: alexpetertyler
 * @date: 2021/1/29
 * @version: v1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/banish")
@Slf4j
public class ConfigController {
    private final HikariDataSourceCreator hikariDataSourceCreator;
    private final BanishConfigService banishConfigService;
    private final DynamicRoutingDataSource ds;

    @PostMapping("/config/update")
    public String updateConfig(
            @RequestBody BanishConfig banishConfig
    ) {
        banishConfigService.update(banishConfig);
        return "配置更新成功!";
    }

    @PostMapping("/datasource/update")
    public String updateDataSource(
            @RequestBody DataSourceDto dataSourceDto
    ) {
        DataSourceProperty property = new DataSourceProperty();

        property.setUsername(dataSourceDto.getUsername());
        property.setPassword(dataSourceDto.getPassword());
        property.setUrl(dataSourceDto.getJdbcUrl() + "?useUnicode=true&useSSL=false&autoReconnect=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&rewriteBatchedStatements=true");
        property.setDriverClassName("com.mysql.cj.jdbc.Driver");
        HikariCpConfig hikariConfig = new HikariCpConfig();
        hikariConfig.setIdleTimeout(dataSourceDto.getIdleTimeout());
        hikariConfig.setMaxLifetime(dataSourceDto.getMaxLifetime());
        hikariConfig.setMaxPoolSize(20);
        hikariConfig.setMinIdle(5);
        hikariConfig.setConnectionTimeout(dataSourceDto.getConnectionTimeout());
        property.setHikari(hikariConfig);
        ds.removeDataSource("banish");
        ds.addDataSource("banish", hikariDataSourceCreator.createDataSource(property));
        return "更新数据源成功！";
    }
}
