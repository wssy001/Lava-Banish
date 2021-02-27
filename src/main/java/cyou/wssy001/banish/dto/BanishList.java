package cyou.wssy001.banish.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @projectName: lava-banish
 * @className: BanishList
 * @description:
 * @author: alexpetertyler
 * @date: 2021/2/2
 * @version: v1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BanishList<T> extends BaseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "客户端UUID")
    private List<T> list;

}