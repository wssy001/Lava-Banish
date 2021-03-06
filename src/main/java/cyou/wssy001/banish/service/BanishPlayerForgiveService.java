package cyou.wssy001.banish.service;

import cyou.wssy001.banish.util.BanishDateUtil;
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
                Date time;
                if (s1.equalsIgnoreCase("all")) {
                    long l1 = banTempService.forgiveAll(ucid);
                    if (l1 > 0) {
                        msg = "已原谅" + l1 + "人";
                    } else {
                        msg = "您还没有TK记录";
                    }
                    break;
                }

                time = BanishDateUtil.convertToDate(s1);
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
