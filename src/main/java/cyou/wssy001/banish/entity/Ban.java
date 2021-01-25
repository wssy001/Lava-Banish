package cyou.wssy001.banish.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Ban对象")
public class Ban implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "UCID")
    private String ucid;

    @ApiModelProperty(value = "IP地址")
    private String ipaddr;

    @ApiModelProperty(value = "玩家姓名")
    private String name;

    @ApiModelProperty(value = "封禁理由")
    private String reason;

    @ApiModelProperty(value = "封禁开始时间")
    private Date bannedFrom;

    @ApiModelProperty(value = "封禁结束时间")
    private Date bannedUntil;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "是否启用")
    @TableLogic
    private Boolean enable;

    @ApiModelProperty(value = "乐观锁")
    @Version
    private Integer version;


}
