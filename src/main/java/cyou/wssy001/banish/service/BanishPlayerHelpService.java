package cyou.wssy001.banish.service;

import lombok.RequiredArgsConstructor;
import moe.ofs.backend.chatcmdnew.model.ChatCommandProcessEntity;
import moe.ofs.backend.function.triggermessage.services.NetMessageService;
import org.springframework.stereotype.Service;

/**
 * @projectName: lava-banish
 * @className: BanishForgiveChatCMDService
 * @description:
 * @author: alexpetertyler
 * @date: 2021/1/27
 * @version: v1.0
 */
@Service
@RequiredArgsConstructor
public class BanishPlayerHelpService {
    private final NetMessageService netMessageService;

    public void helpPlayer(ChatCommandProcessEntity chatCommandProcessEntity) {

        String help = "这是帮助内容：\n" +
                "/forgive 快速原谅一分钟内产生的TK" +
                "/forgive 1【h/小时/min/分钟/mon/月/min/分钟】 原谅过去时间内产生的一次TK*" +
                "/forgive all 原谅所有TK" +
                "/punish 快速惩罚一分钟内产生的TK" +
                "/punish 1【h/小时/min/分钟/mon/月/min/分钟】 惩罚过去时间内产生的一次TK*" +
                "/punish all 原谅所有TK";
        netMessageService.sendNetMessageForPlayer(help, chatCommandProcessEntity.getPlayer());
    }
}
