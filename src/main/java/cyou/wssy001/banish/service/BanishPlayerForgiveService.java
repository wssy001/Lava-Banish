package cyou.wssy001.banish.service;

import lombok.RequiredArgsConstructor;
import moe.ofs.backend.chatcmdnew.model.ChatCommandProcessEntity;
import moe.ofs.backend.function.triggermessage.services.NetMessageService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BanishPlayerForgiveService {
    private final NetMessageService netMessageService;


    public void forgivePlayer(ChatCommandProcessEntity chatCommandProcessEntity) {
        String keyword = chatCommandProcessEntity.getKeyword();

    }
}
