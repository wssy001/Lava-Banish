package cyou.wssy001.banish.dto;

import lombok.Data;

/**
 * @projectName: lava-banish
 * @className: DataSourceDto
 * @description:
 * @author: alexpetertyler
 * @date: 2021/2/6
 * @version: v1.0
 */
@Data
public class DataSourceDto {
    private String username;
    private String password;
    private String databaseName;
    private String jdbcUrl;
    private Long maxLifetime;
    private Long idleTimeout;
    private Long connectionTimeout;
}
