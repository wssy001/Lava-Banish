package cyou.wssy001.banish.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cyou.wssy001.banish.dao.BanTempService;
import lombok.RequiredArgsConstructor;
import moe.ofs.backend.chatcmdnew.model.ChatCommandProcessEntity;
import moe.ofs.backend.domain.dcs.poll.PlayerInfo;
import moe.ofs.backend.function.triggermessage.services.NetMessageService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class BanishPlayerForgiveService {
    private final NetMessageService netMessageService;
    private final BanTempService banTempService;


    public void forgivePlayer(ChatCommandProcessEntity chatCommandProcessEntity) {
        String[] s = chatCommandProcessEntity.getMessage().split(" ");
        PlayerInfo player = chatCommandProcessEntity.getPlayer();
        String ucid = player.getUcid();
        String msg;
        switch (s.length) {
            case 1:
                long l = banTempService.forgive(ucid);
                if (l == 1) {
                    msg = "已原谅";
                } else {
                    msg = "未找到最近1分钟内被TK的记录";
                }
                break;
            case 2:
                String s1 = s[1];
                DateTime time;
                if (s1.equalsIgnoreCase("all")) {
                    long l1 = banTempService.forgiveAll(ucid);
                    if (l1 > 0) {
                        msg = "已原谅" + l1 + "人";
                    } else {
                        msg = "您还没有TK记录";
                    }
                    break;
                } else if (s1.contains("分钟")) {
                    Integer minute = Integer.getInteger(s1.replace("分钟", ""));
                    time = DateUtil.offsetMinute(new Date(), minute);
                } else if (s1.contains("小时")) {
                    Integer hour = Integer.getInteger(s1.replace("小时", ""));
                    time = DateUtil.offsetMinute(new Date(), hour);
                } else {
                    msg = "您输入的参数有误，请查证";
                    break;
                }
                long l1 = banTempService.forgive(ucid, time);

                if (l1 > 0) {
                    msg = "已原谅";
                } else {
                    msg = "您还没有TK记录";
                }

                break;
            default:
                msg = "您输入的指令有误，请查证";
        }
        netMessageService.sendNetMessageForPlayer(msg, player);

    }
}
