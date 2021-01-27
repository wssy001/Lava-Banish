package cyou.wssy001.banish.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cyou.wssy001.banish.dao.BanLogDao;
import cyou.wssy001.banish.dao.BanTempService;
import cyou.wssy001.banish.dto.BanTemp;
import cyou.wssy001.banish.entity.BanLog;
import lombok.RequiredArgsConstructor;
import moe.ofs.backend.annotations.ListenLavaEvent;
import moe.ofs.backend.discipline.service.PlayerDisciplineService;
import moe.ofs.backend.domain.dcs.poll.PlayerInfo;
import moe.ofs.backend.domain.events.EventType;
import moe.ofs.backend.domain.events.LavaEvent;
import org.springframework.stereotype.Service;

/**
 * @projectName: lava-banish
 * @className: DCSTKEventService
 * @description:
 * @author: alexpetertyler
 * @date: 2021/1/25
 * @version: v1.0
 */
@Service
@RequiredArgsConstructor
public class DCSTKEventService {
    private final BanLogDao banLogDao;
    private final BanTempService banTempService;
    private final PlayerDisciplineService disciplineService;

    @ListenLavaEvent(EventType.KILL)
    public void tkEvent(LavaEvent event) {
        PlayerInfo killer = event.getInitiatorPlayer();
        PlayerInfo victim = event.getTargetPlayer();

        if (killer == null || victim == null) return;
        if (killer.getSide() != victim.getSide()) return;

        disciplineService.kick(killer, "本服严禁TK，你有1分钟的时间取得受害者的谅解");
        BanLog banLog = new BanLog();
        banLog.setIpaddr(killer.getIpaddr());
        banLog.setUcid(killer.getUcid());
        banLog.setName(killer.getName());
        banLogDao.insert(banLog);

        BanTemp banTemp = new BanTemp();
        DateTime time = DateUtil.parseTime(event.getTime() * 1000 + "");
        banTemp.setTime(time);
        banTemp.setDbId(banLog.getId());
        banTemp.setKillerUCID(killer.getUcid());
        banTemp.setVictimPlayerUCID(victim.getUcid());
        banTempService.add(banTemp);

    }
}
