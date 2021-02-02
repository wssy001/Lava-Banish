package cyou.wssy001.banish.service;

import cyou.wssy001.banish.entity.BanishConfig;
import moe.ofs.backend.common.AbstractMapService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BanishConfigService extends AbstractMapService<BanishConfig> {
    @Override
    public BanishConfig save(BanishConfig object) {
        if (object == null) {
            throw new NullPointerException("Unable to save null value into MapService");
        } else {
            map.put(1L, object);
            return object;
        }
    }

    public BanishConfig update(BanishConfig object) {
        Optional<BanishConfig> banishConfig = findById(1L);
        if (!banishConfig.isPresent()) return save(object);

        BanishConfig config = banishConfig.get();
        if (object.getUuid() != null) config.setUuid(object.getUuid());
        if (object.getServerPublicKey() != null) config.setServerPublicKey(object.getServerPublicKey());
        if (object.getClientPrivateKey() != null) config.setClientPrivateKey(object.getClientPrivateKey());
        if (object.getClientPublicKey() != null) config.setClientPublicKey(object.getClientPublicKey());
        if (object.getPunishTime() != null) config.setPunishTime(object.getPunishTime());
        if (object.getThinkingTime() != null) config.setThinkingTime(object.getThinkingTime());
        return save(object);
    }

    public BanishConfig find() {
        Optional<BanishConfig> config = findById(1L);
        boolean present = config.isPresent();
        if (present) return config.get();
        throw new RuntimeException("没有配置文件，请检查！");
    }
}