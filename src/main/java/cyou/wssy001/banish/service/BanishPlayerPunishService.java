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
public class BanishPlayerPunishService {
    private final NetMessageService netMessageService;

    public void punishPlayer(ChatCommandProcessEntity chatCommandProcessEntity) {

    }
}
