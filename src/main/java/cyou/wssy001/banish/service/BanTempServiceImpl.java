package cyou.wssy001.banish.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cyou.wssy001.banish.dao.BanDao;
import cyou.wssy001.banish.dao.BanLogDao;
import cyou.wssy001.banish.dto.BanDto;
import cyou.wssy001.banish.dto.BanishConfig;
import cyou.wssy001.banish.dto.BanishList;
import cyou.wssy001.banish.entity.Ban;
import cyou.wssy001.banish.entity.BanLog;
import cyou.wssy001.banish.entity.BanTemp;
import cyou.wssy001.banish.util.BanishCryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.ofs.backend.common.AbstractMapService;
import moe.ofs.backend.connector.DcsScriptConfigManager;
import moe.ofs.backend.discipline.service.PlayerConnectionValidationService;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

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
@Slf4j
public class BanTempServiceImpl extends AbstractMapService<BanTemp> implements BanTempService {
    private final BanLogDao banLogDao;
    private final BanDao banDao;
    private final PlayerConnectionValidationService playerConnectionValidationService;
    private final BanishConfigService banishConfigService;

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
        BanishConfig banishConfig = banishConfigService.find();
        DateTime preMinute = DateUtil.offsetMinute(new Date(), -banishConfig.getRecordsSearchTime());
        return forgive(victimPlayerUCID, preMinute);
    }

    @Override
    public long forgive(String victimPlayerUCID, Date preTime) {
        return forgive(victimPlayerUCID, preTime, false);
    }

    @Override
    public long forgiveAll(String victimPlayerUCID) {
        BanishConfig banishConfig = banishConfigService.find();
        DateTime preMinute = DateUtil.offsetMinute(new Date(), -banishConfig.getRecordsSearchTime());
        return forgive(victimPlayerUCID, preMinute, true);
    }

    private long forgive(String victimPlayerUCID, @NonNull Date preTime, boolean isAll) {

        List<BanTemp> collect = findAll()
                .stream()
                .filter(banTemp -> banTemp.getVictimPlayerUCID().equals(victimPlayerUCID))
                .collect(Collectors.toList());
        long size = collect.size();

        if (size == 0) return 0;

        Set<Long> ids = new HashSet<>();
        if (isAll) {
            collect.forEach(banTemp -> {
                ids.add(banTemp.getId());
                delete(banTemp);
            });
            banLogDao.update(null, Wrappers.<BanLog>lambdaUpdate().set(BanLog::getNote, "玩家原谅").in(BanLog::getId, ids));
            return size;
        }

        collect = collect
                .stream()
                .filter(banTemp -> DateUtil.isIn(banTemp.getDecline(), preTime, new Date()))
                .sorted(Comparator.comparing(BanTemp::getDecline).reversed())
                .collect(Collectors.toList());

        if (collect.size() == 0) return 0;

        forgive(collect.get(0).getId());
        return 1;
    }

    private void forgive(Long id) {
        BanLog banLog = banLogDao.selectById(id);
        banLog.setNote("玩家原谅");
        banLogDao.updateById(banLog);
        deleteById(id);
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
        BanishConfig banishConfig = banishConfigService.find();
        DateTime preMinute = DateUtil.offsetMinute(new Date(), -banishConfig.getRecordsSearchTime());
        return punish(victimPlayerUCID, preMinute, true);
    }

    public long punish(String victimPlayerUCID, @NonNull Date preTime, boolean isAll) {
        List<BanTemp> collect = findAll().stream().filter(banTemp -> banTemp.getVictimPlayerUCID().equals(victimPlayerUCID)).collect(Collectors.toList());
        long size = collect.size();
        if (size == 0) return 0;

        Set<Long> ids = new HashSet<>();
        if (isAll) {
            collect.forEach(banTemp -> {
                ids.add(banTemp.getId());
                delete(banTemp);
            });
            banLogDao.update(null, Wrappers.<BanLog>lambdaUpdate().set(BanLog::getNote, "惩罚").in(BanLog::getId, ids));
            punish(ids);
            return size;
        }

        collect = collect.stream().filter(banTemp -> DateUtil.isIn(banTemp.getTime(), preTime, new Date()))
                .sorted(Comparator.comparing(BanTemp::getDecline).reversed()).collect(Collectors.toList());

        if (collect.size() == 0) return 0;

        punish(collect.get(0).getId());
        return 1;
    }

    public void punish(Set<Long> idSet) {
        Map<Object, String> map;
        List<BanTemp> collect = findAll().stream().filter(v -> idSet.contains(v.getId())).collect(Collectors.toList());
        if (collect.size() == 0) return;

        map = collect.stream().collect(Collectors.toMap(BanTemp::getKillerUCID, v -> "您被ban了，如有异议，请联系管理员并附带有效证据"));
        playerConnectionValidationService.blockPlayerUcid(map);
        collect(idSet);
        idSet.forEach(this::deleteById);

        BanishConfig banishConfig = banishConfigService.find();
        DateTime day = DateUtil.offsetDay(new Date(), banishConfig.getPunishTime());
        banLogDao.selectBatchIds(idSet).forEach(v -> {
            Ban ban = new Ban(v.getUcid(), v.getIpaddr(), v.getName(), new Date(), day);
            ban.setReason("您被ban了，如有异议，请联系管理员并附带有效证据");
            banDao.insert(ban);
        });

    }

    public void punish(Long id) {
        BanLog banLog = banLogDao.selectById(id);
        banLog.setNote("惩罚");
        banLogDao.updateById(banLog);
        collect(id);
        try {
            saveProof(id);
        } catch (Exception e) {
            log.info("******证据保存失败");
        }
        deleteById(id);

        BanishConfig banishConfig = banishConfigService.find();
        DateTime day = DateUtil.offsetDay(new Date(), banishConfig.getPunishTime());
        Ban ban = new Ban(banLog.getUcid(), banLog.getIpaddr(), banLog.getName(), new Date(), day);
        ban.setReason("您被ban了，如有异议，请联系管理员并附带有效证据");
        banDao.insert(ban);
        playerConnectionValidationService.blockPlayerUcid(ban.getUcid(), ban.getReason());
    }

    @Override
    @Scheduled(fixedDelay = 5 * 60 * 1000L)
    public void dispose() {
        if (getNextId() == 1) return;

        Date now = new Date();
        BanishConfig banishConfig = banishConfigService.find();
        DateTime preMinute = DateUtil.offsetMinute(now, -banishConfig.getThinkingTime());
        List<BanTemp> collect = findAll()
                .stream()
                .filter(banTemp -> !DateUtil.isIn(banTemp.getDecline(), preMinute, now))
                .collect(Collectors.toList());

        if (collect.isEmpty()) return;

        collect.forEach(record -> punish(record.getId()));
    }

    private void collect(Long id) {
        Ban ban = banDao.selectById(id);
        BanishConfig banishConfig = banishConfigService.find();
        String uuid = banishConfig.getUuid();
        BanDto banDto = new BanDto(uuid, ban.getUcid(), ban.getIpaddr(), ban.getName());
        banDto.setClientUUID(uuid);
        banDto.setSign(BanishCryptUtil.sign(JSON.toJSONString(banDto)));
        HttpUtil.post(banishConfig.getServerAddress() + "/ban/add", JSON.toJSONString(banDto), 3000);
    }

    private void collect(Set<Long> ids) {
        List<Ban> bans = banDao.selectBatchIds(ids);
        BanishConfig banishConfig = banishConfigService.find();
        String uuid = banishConfig.getUuid();
        BanishList<BanDto> banDtoList = new BanishList<>();
        List<BanDto> banDtos = new ArrayList<>();
        bans.forEach(ban -> {
            BanDto banDto = new BanDto(uuid, ban.getUcid(), ban.getIpaddr(), ban.getName());
            banDtos.add(banDto);
        });
        banDtoList.setList(banDtos);
        banDtoList.setClientUUID(uuid);
        String sign = BanishCryptUtil.sign(JSON.toJSONString(banDtoList));
        banDtoList.setSign(sign);
        HttpUtil.post(banishConfig.getServerAddress() + "/ban/add/batch", JSON.toJSONString(banDtoList), 3000);
    }

    private void saveProof(Long id) throws Exception {
        Path path = DcsScriptConfigManager.LAVA_DATA_PATH
                .resolve("addons")
                .resolve("banish")
                .resolve("TK证据")
                .resolve(DateUtil.today());
        BanTemp banTemp = findById(id).get();
        String address = "http://localhost:8088/screenshots/";
        String victimPhotoId = banTemp.getVictimPhotoId();
        if (victimPhotoId != null) {
            BufferedImage read = ImageIO.read(new URL(address + victimPhotoId));
            File outputfile = path.resolve(victimPhotoId + ".jpg").toFile();
            ImageIO.write(read, "jpg", outputfile);
        }

        String killerPhotoId = banTemp.getKillerPhotoId();
        if (killerPhotoId != null) {
            BufferedImage read = ImageIO.read(new URL(address + killerPhotoId));
            File outputfile = path.resolve(killerPhotoId + ".jpg").toFile();
            ImageIO.write(read, "jpg", outputfile);
        }
    }
}