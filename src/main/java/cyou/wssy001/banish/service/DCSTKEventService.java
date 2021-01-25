package cyou.wssy001.banish.service;

import moe.ofs.backend.annotations.ListenLavaEvent;
import moe.ofs.backend.domain.events.EventType;
import moe.ofs.backend.domain.events.LavaEvent;
import org.springframework.stereotype.Service;

/**
 * @projectName: lava-banish
 * @className: DCSTKEventService
 * @description:
 * @author: alexpetertyler
 * @date: 2021/1/25
 * @version: v1.0
 */
@Service
public class DCSTKEventService {

    @ListenLavaEvent(EventType.KILL)
    public void tkEvent(LavaEvent event) {

    }
}
