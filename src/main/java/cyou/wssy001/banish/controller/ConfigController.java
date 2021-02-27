package cyou.wssy001.banish.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.HikariDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.hikari.HikariCpConfig;
import cyou.wssy001.banish.dto.BanishConfig;
import cyou.wssy001.banish.dto.DataSourceDto;
import cyou.wssy001.banish.service.BanishConfigService;
import lombok.RequiredArgsConstructor;
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
public class ConfigController {
    private final HikariDataSourceCreator hikariDataSourceCreator;
    private final BanishConfigService banishConfigService;
    private final DynamicRoutingDataSource ds;

    @PostMapping("/config/update")
    public String updateConfig(
            @RequestBody BanishConfig banishConfig
    ) {
        banishConfigService.update(banishConfig);
        return "成功!";
    }

    @PostMapping("/datasource/update")
    public String updateDataSource(
            @RequestBody DataSourceDto dataSourceDto
    ) {
        DataSourceProperty property = new DataSourceProperty();
        String url = environment.getProperty("banish.db.url");
        String name = environment.getProperty("banish.db.name");
        String username = environment.getProperty("banish.db.username");
        String password = environment.getProperty("banish.db.password");
        property.setUsername(username);
        property.setPassword(password);
        property.setUrl("jdbc:mysql://" + url + "/" + name + "?useUnicode=true&useSSL=false&autoReconnect=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&rewriteBatchedStatements=true");
        property.setDriverClassName("com.mysql.cj.jdbc.Driver");
        log.info("******Banish数据源：" + JSON.toJSONString(property));
        HikariCpConfig hikariConfig = new HikariCpConfig();
        hikariConfig.setIdleTimeout(43170000L);
        hikariConfig.setMaxLifetime(43170000L);
        hikariConfig.setMaxPoolSize(20);
        hikariConfig.setMinIdle(5);
        hikariConfig.setConnectionTimeout(43170000L);
        property.setHikari(hikariConfig);
        ds.addDataSource("banish", hikariDataSourceCreator.createDataSource(property));
        return "";
    }
}
