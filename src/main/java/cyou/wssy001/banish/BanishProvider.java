package cyou.wssy001.banish;

import cyou.wssy001.banish.service.ClientInit;
import lombok.RequiredArgsConstructor;
import moe.ofs.backend.Plugin;
import org.springframework.stereotype.Component;

/**
 * @projectName: lava-banish
 * @className: BanishProvider
 * @description:
 * @author: alexpetertyler
 * @date: 2021/1/11
 * @version: v1.0
 */
@RequiredArgsConstructor
@Component
public class BanishProvider implements Plugin {
    private final ClientInit clientInit;

    @Override
    public void register() {
        clientInit.init();
    }

    @Override
    public void unregister() {

    }

    @Override
    public String getName() {
        return "banish";
    }

    @Override
    public String getDescription() {
        return "一款基于Lava的联ban插件";
    }
}
