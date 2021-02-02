package cyou.wssy001.banish.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @projectName: banish
 * @className: BaseDto
 * @description:
 * @author: alexpetertyler
 * @date: 2021/2/2
 * @version: v1.0
 */
@Data
public class BaseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "客户端UUID")
    private String clientUUID;

    @ApiModelProperty(value = "SM2签名")
    private String sign;

    @ApiModelProperty(value = "SM4加密")
    private String crypt;
}
