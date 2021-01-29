package cyou.wssy001.banish.entity;

import lombok.Getter;
import lombok.Setter;
import moe.ofs.backend.domain.dcs.BaseEntity;

/**
 * @projectName: lava-banish
 * @className: BanishConfig
 * @description:
 * @author: alexpetertyler
 * @date: 2021/1/29
 * @version: v1.0
 */
@Getter
@Setter
public class BanishConfig extends BaseEntity {
    private String uuid;
    private String clientPrivateKey;
    private String clientPublicKey;
    private String serverPublicKey;

}