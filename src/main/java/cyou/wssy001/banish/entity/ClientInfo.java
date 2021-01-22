package cyou.wssy001.banish.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 * @author Tyler
 * @since 2021-01-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ClientInfo对象", description = "")
public class ClientInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "UUID")
    @TableId(value = "uuid", type = IdType.ASSIGN_ID)
    private String uuid;

    @ApiModelProperty(value = "客户端名称")
    @TableField("client_Name")
    private String clientName;

    @ApiModelProperty(value = "客户端私钥")
    private String clientPrivateKey;

    @ApiModelProperty(value = "客户端公钥")
    private String clientPublicKey;

    @ApiModelProperty(value = "服务器公钥")
    private String serverPublicKey;

    public ClientInfo(String uuid) {
        this.uuid = uuid;
        this.clientPrivateKey = "";
        this.serverPublicKey = "";
        this.clientPublicKey = "";
    }
}
