package cyou.wssy001.banish.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @projectName: lava-banish
 * @className: ClientInfoDto
 * @description:
 * @author: alexpetertyler
 * @date: 2021/2/1
 * @version: v1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ClientInfoDto extends BaseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "客户端端口")
    private String clientPort;

    @ApiModelProperty(value = "客户端名称")
    private String clientName;

    @ApiModelProperty(value = "客户端私钥")
    private String clientPrivateKey;

    @ApiModelProperty(value = "客户端公钥")
    private String clientPublicKey;

    @ApiModelProperty(value = "服务器公钥")
    private String serverPublicKey;

}
