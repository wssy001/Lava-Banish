package cyou.wssy001.banish.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * @projectName: lava-banish
 * @className: BanishList
 * @description:
 * @author: alexpetertyler
 * @date: 2021/2/2
 * @version: v1.0
 */
@Setter
@Getter
public class BanishList<E> extends ArrayList<E> {
    @ApiModelProperty(value = "客户端UUID")
    private String clientUUID;

    @ApiModelProperty(value = "SM2签名")
    private String sign;

    @ApiModelProperty(value = "SM4加密")
    private String crypt;
}
