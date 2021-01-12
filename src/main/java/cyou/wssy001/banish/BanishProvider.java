package cyou.wssy001.banish;

import cyou.wssy001.banish.service.UUIDInit;
import lombok.RequiredArgsConstructor;
import moe.ofs.backend.Plugin;

/**
 * @projectName: lava-banish
 * @className: BanishProvider
 * @description:
 * @author: alexpetertyler
 * @date: 2021/1/11
 * @version: v1.0
 */
@RequiredArgsConstructor
public class BanishProvider implements Plugin {
    private final UUIDInit uuidInit;

    @Override
    public void register() {
        uuidInit.generateUUID();
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
