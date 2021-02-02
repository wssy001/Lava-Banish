package cyou.wssy001.banish.entity;

import cn.hutool.core.date.DateUtil;
import lombok.Getter;
import lombok.Setter;
import moe.ofs.backend.domain.dcs.BaseEntity;

import java.util.Date;

/**
 * @projectName: lava-banish
 * @className: BanTemp
 * @description:
 * @author: alexpetertyler
 * @date: 2021/1/25
 * @version: v1.0
 */
@Getter
@Setter
public class BanTemp extends BaseEntity {
    private String killerUCID;
    private String victimPlayerUCID;
    private Date time;
    private Date decline;

    public void setTime(Date time) {
        setTime(time, 1);
    }

    public void setTime(Date time, int minute) {
        this.time = time;
        this.decline = DateUtil.offsetMinute(time, minute);
    }
}
