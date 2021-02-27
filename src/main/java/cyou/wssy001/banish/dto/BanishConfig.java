package cyou.wssy001.banish.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import moe.ofs.backend.domain.dcs.BaseEntity;

/**
 * @projectName: lava-banish
 * @className: BanishConfig
 * @description:
 * @author: alexpetertyler
 * @date: 2021/1/29
 * @version: v1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BanishConfig extends BaseEntity {
    private String uuid;
    private String clientName;
    private String clientPrivateKey;
    private String clientPublicKey;
    private String serverPublicKey;
    //    天
    private Integer punishTime = 30;
    //    分钟
    private Integer thinkingTime = 1;
    //    分钟
    private Integer recordsSearchTime = 1;

    private String serverAddress;

    private String databaseUrl;
    private String databaseUsername;
    private String databasePassword;
    private Long idleTimeout;
    private Long connectionTimeout;
    private Long maxLifetime;
    private Integer maxPoolSize;
    private Integer minIdle;

}