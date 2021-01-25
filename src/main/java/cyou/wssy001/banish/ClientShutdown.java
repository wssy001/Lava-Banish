package cyou.wssy001.banish;

import lombok.RequiredArgsConstructor;
import moe.ofs.backend.discipline.service.PlayerConnectionValidationService;
import org.springframework.stereotype.Service;

/**
 * @projectName: lava-banish
 * @className: ClientShutdown
 * @description: Banish注销类
 * @author: alexpetertyler
 * @date: 2021/1/15
 * @version: v1.0
 */
@Service
@RequiredArgsConstructor
public class ClientShutdown {
    private final PlayerConnectionValidationService playerConnectionValidationService;

    public void shutdown() {
        unblockPlayer();
    }

    private void unblockPlayer() {
        playerConnectionValidationService.unblockPlayerAll();
    }
}
