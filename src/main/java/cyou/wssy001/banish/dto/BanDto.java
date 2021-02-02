package cyou.wssy001.banish.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @projectName: lava-banish
 * @className: BanDto
 * @description:
 * @author: alexpetertyler
 * @date: 2021/2/2
 * @version: v1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class BanDto extends BaseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "UCID")
    private String ucid;

    @ApiModelProperty(value = "IP地址")
    private String ipaddr;

    @ApiModelProperty(value = "玩家姓名")
    private String name;

    public BanDto(String uuid, String ucid, String ipaddr, String name) {
        super.setClientUUID(uuid);
        this.ucid = ucid;
        this.ipaddr = ipaddr;
        this.name = name;
    }
}
