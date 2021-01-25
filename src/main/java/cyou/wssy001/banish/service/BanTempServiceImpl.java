package cyou.wssy001.banish.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cyou.wssy001.banish.dao.BanLogDao;
import cyou.wssy001.banish.dao.BanTempService;
import cyou.wssy001.banish.dto.BanTemp;
import cyou.wssy001.banish.entity.BanLog;
import lombok.RequiredArgsConstructor;
import moe.ofs.backend.common.AbstractMapService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @projectName: lava-banish
 * @className: BanTempService
 * @description:
 * @author: alexpetertyler
 * @date: 2021/1/25
 * @version: v1.0
 */
@Service
@RequiredArgsConstructor
public class BanTempServiceImpl extends AbstractMapService<BanTemp> implements BanTempService {
    private final BanLogDao banLogDao;

    @Override
    public void save(List<BanTemp> banTempList) {
        banTempList.forEach(this::save);
    }

    @Override
    public Set<BanTemp> findAll() {
        if (getNextId() == 1) throw new RuntimeException("数据为空");
        return new HashSet<>(this.map.values());
    }

    @Override
    public long forgive(String victimPlayerUCID) {
        return forgive(victimPlayerUCID, null);
    }

    @Override
    public long forgive(String victimPlayerUCID, Date time) {
        return forgive(victimPlayerUCID, time, false);
    }

    @Override
    public long forgiveAll(String victimPlayerUCID) {
        DateTime minute = DateUtil.offsetMinute(new Date(), 1);
        return forgive(victimPlayerUCID, minute, true);
    }

    private long forgive(String victimPlayerUCID, Date time, boolean isAll) {
        final Date beginTime = time == null ? DateUtil.offsetMinute(new Date(), 1) : time;

        Stream<BanTemp> stream = findAll().stream().filter(banTemp -> banTemp.getVictimPlayerUCID().equals(victimPlayerUCID));
        long size = stream.count();

        if (size == 0) return 0;

        Set<Long> ids = new HashSet<>();
        if (isAll) {
            stream.forEach(banTemp -> {
                ids.add(banTemp.getId());
                delete(banTemp);
            });
            banLogDao.update(null, Wrappers.<BanLog>lambdaUpdate().set(BanLog::getNote, "玩家原谅").in(BanLog::getId, ids));
            return size;
        }

        stream = stream.filter(banTemp -> DateUtil.isIn(new Date(), beginTime, banTemp.getDecline()));
        size = stream.count();

        if (size == 0) return 0;

        stream.forEach(this::delete);
        return size;
    }

    @Override
    public long punish(String victimPlayerUCID) {
        return punish(victimPlayerUCID, null);
    }

    @Override
    public long punish(String victimPlayerUCID, Date time) {
        return punish(victimPlayerUCID, time, false);
    }

    @Override
    public long punishAll(String victimPlayerUCID) {
        return punish(victimPlayerUCID, null, true);
    }

    public long punish(String victimPlayerUCID, Date time, boolean isAll) {
        final Date beginTime = time == null ? DateUtil.offsetMinute(new Date(), 1) : time;

        Stream<BanTemp> stream = findAll().stream().filter(banTemp -> banTemp.getVictimPlayerUCID().equals(victimPlayerUCID));
        long size = stream.count();

        if (size == 0) return 0;

        Set<Long> ids = new HashSet<>();
        if (isAll) {
            stream.forEach(banTemp -> {
                ids.add(banTemp.getId());
                delete(banTemp);
            });
            banLogDao.update(null, Wrappers.<BanLog>lambdaUpdate().set(BanLog::getNote, "惩罚").in(BanLog::getId, ids));
            return size;
        }

        stream = stream.filter(banTemp -> DateUtil.isIn(new Date(), beginTime, banTemp.getDecline()));
        size = stream.count();

        if (size == 0) return 0;

        stream.forEach(this::delete);
        return size;
    }
}o