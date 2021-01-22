package cyou.wssy001.banish;

import cyou.wssy001.banish.service.ClientInit;
import cyou.wssy001.banish.service.ClientShutdown;
import lombok.RequiredArgsConstructor;
import moe.ofs.backend.Plugin;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BanishProvider implements Plugin {
    private final ClientInit init;
    private final ClientShutdown shutdown;

    @Override
    public void register() {
        init.init();
    }

    @Override
    public void unregister() {
        shutdown.shutdown();
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
