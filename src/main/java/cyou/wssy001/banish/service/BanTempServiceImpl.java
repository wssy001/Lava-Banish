package cyou.wssy001.banish.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cyou.wssy001.banish.dao.BanDao;
import cyou.wssy001.banish.dao.BanLogDao;
import cyou.wssy001.banish.dao.BanTempService;
import cyou.wssy001.banish.dto.BanTemp;
import cyou.wssy001.banish.entity.Ban;
import cyou.wssy001.banish.entity.BanLog;
import lombok.RequiredArgsConstructor;
import moe.ofs.backend.common.AbstractMapService;
import moe.ofs.backend.discipline.service.PlayerConnectionValidationService;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
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
    private final BanDao banDao;
    private final PlayerConnectionValidationService playerConnectionValidationService;

    @Override
    public Set<BanTemp> findAll() {
        if (getNextId() == 1) throw new RuntimeException("数据为空");
        return new HashSet<>(this.map.values());
    }

    @Override
    public void add(BanTemp banTemp) {
        save(banTemp);
    }

    @Override
    public void add(List<BanTemp> banTempList) {
        banTempList.forEach(this::save);
    }

    @Override
    public long forgive(String victimPlayerUCID) {
        DateTime preMinute = DateUtil.offsetMinute(new Date(), -1);
        return forgive(victimPlayerUCID, preMinute);
    }

    @Override
    public long forgive(String victimPlayerUCID, Date preTime) {
        return forgive(victimPlayerUCID, preTime, false);
    }

    @Override
    public long forgiveAll(String victimPlayerUCID) {
        DateTime preMinute = DateUtil.offsetMinute(new Date(), -1);
        return forgive(victimPlayerUCID, preMinute, true);
    }

    private long forgive(String victimPlayerUCID, @NonNull Date preTime, boolean isAll) {

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

        stream = stream.filter(banTemp -> DateUtil.isIn(banTemp.getDecline(), preTime, new Date()))
                .sorted(Comparator.comparing(BanTemp::getDecline).reversed());
        size = stream.count();

        if (size == 0) return 0;

        stream.findFirst().ifPresent(v -> forgive(v.getId()));
        return 1;
    }

    private void forgive(Long id) {
        BanTemp banTemp = findById(id).get();
        BanLog banLog = banLogDao.selectById(banTemp.getDbId());
        banLog.setNote("玩家原谅");
        banLogDao.updateById(banLog);
        delete(banTemp);
    }

    @Override
    public long punish(String victimPlayerUCID) {
        DateTime preMinute = DateUtil.offsetMinute(new Date(), -1);
        return punish(victimPlayerUCID, preMinute);
    }

    @Override
    public long punish(String victimPlayerUCID, Date preTime) {
        return punish(victimPlayerUCID, preTime, false);
    }

    @Override
    public long punishAll(String victimPlayerUCID) {
        DateTime preMinute = DateUtil.offsetMinute(new Date(), -1);
        return punish(victimPlayerUCID, preMinute, true);
    }

    public long punish(String victimPlayerUCID, @NonNull Date preTime, boolean isAll) {

        Stream<BanTemp> stream = findAll().stream().filter(banTemp -> banTemp.getVictimPlayerUCID().equals(victimPlayerUCID));
        long size = stream.count();

        if (size == 0) return 0;

        Set<Long> ids = new HashSet<>();
        if (isAll) {
            stream.forEach(banTemp -> {
                ids.add(banTemp.getDbId());
                delete(banTemp);
            });
            banLogDao.update(null, Wrappers.<BanLog>lambdaUpdate().set(BanLog::getNote, "惩罚").in(BanLog::getId, ids));
            punish(ids);
            return size;
        }

        stream = stream.filter(banTemp -> DateUtil.isIn(banTemp.getDecline(), preTime, new Date()))
                .sorted(Comparator.comparing(BanTemp::getDecline).reversed());
        size = stream.count();

        if (size == 0) return 0;

        stream.findFirst().ifPresent(v -> punish(v.getId()));
        return 1;
    }

    private void punish(Set<Long> idSet) {
        Map<Object, String> map;
        Stream<BanTemp> banTempStream = findAll().stream().filter(v -> idSet.contains(v.getId()));
        if (banTempStream.count() == 0) return;
        map = banTempStream.collect(Collectors.toMap(BanTemp::getKillerUCID, v -> "您被ban了，如有异议，请联系管理员并附带有效证据"));
        playerConnectionValidationService.blockPlayerUcid(map);

        banLogDao.selectBatchIds(idSet).forEach(v -> {
            Ban ban = new Ban(v.getUcid(), v.getIpaddr(), v.getName(), new Date());
            ban.setReason("您被ban了，如有异议，请联系管理员并附带有效证据");
            banDao.insert(ban);
        });

    }

    private void punish(Long id) {
        BanTemp banTemp = findById(id).get();
        BanLog banLog = banLogDao.selectById(banTemp.getDbId());
        banLog.setNote("惩罚");
        banLogDao.updateById(banLog);
        delete(banTemp);

        Ban ban = new Ban(banLog.getUcid(), banLog.getIpaddr(), banLog.getName(), new Date());
        ban.setReason("您被ban了，如有异议，请联系管理员并附带有效证据");
        banDao.insert(ban);
        playerConnectionValidationService.blockPlayerUcid(ban.getUcid(), ban.getReason());
    }

    @Override
    @Scheduled(fixedDelay = 1000L)
    public void dispose() {
        if (getNextId() == 1) return;

        Date now = new Date();
        DateTime preMinute = DateUtil.offsetMinute(now, -1);
        Stream<BanTemp> banTempStream = findAll()
                .stream()
                .filter(banTemp -> !DateUtil.isIn(banTemp.getDecline(), preMinute, now));

        if (banTempStream.count() == 0) return;

        banTempStream.forEach(record -> punish(record.getId()));
    }
}