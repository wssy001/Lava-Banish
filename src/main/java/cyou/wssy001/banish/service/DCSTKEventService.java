package cyou.wssy001.banish.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cyou.wssy001.banish.dao.BanLogDao;
import cyou.wssy001.banish.dao.BanTempService;
import cyou.wssy001.banish.entity.BanLog;
import cyou.wssy001.banish.entity.BanTemp;
import lombok.RequiredArgsConstructor;
import moe.ofs.backend.annotations.ListenLavaEvent;
import moe.ofs.backend.discipline.service.PlayerDisciplineService;
import moe.ofs.backend.domain.dcs.poll.PlayerInfo;
import moe.ofs.backend.domain.events.EventType;
import moe.ofs.backend.domain.events.LavaEvent;
import moe.ofs.backend.function.triggermessage.services.NetMessageService;
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
    private final BanishConfigService banishConfigService;
    private final NetMessageService netMessageService;

    @ListenLavaEvent(EventType.KILL)
    public void tkEvent(LavaEvent event) {
        PlayerInfo killer = event.getInitiatorPlayer();
        PlayerInfo victim = event.getTargetPlayer();

        if (killer == null || victim == null) return;
        if (killer.getSide() != victim.getSide()) return;
        Integer minute = banishConfigService.find().getPunishTime();
        disciplineService.kick(killer, "本服严禁TK，你有" + minute + "分钟的时间取得受害者的谅解");
        netMessageService.sendNetMessageForPlayer("你有 " + minute + "分钟的时间去谅解tk者", victim);
        BanLog banLog = new BanLog();
        banLog.setIpaddr(killer.getIpaddr());
        banLog.setUcid(killer.getUcid());
        banLog.setName(killer.getName());
        banLogDao.insert(banLog);

        BanTemp banTemp = new BanTemp();
        DateTime time = DateUtil.parseTime(event.getTime() * 1000 + "");
        banTemp.setTime(time);
        banTemp.setId(banLog.getId());
        banTemp.setKillerUCID(killer.getUcid());
        banTemp.setVictimPlayerUCID(victim.getUcid());
        banTempService.add(banTemp);

    }
}
