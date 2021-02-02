package cyou.wssy001.banish.controller;

import cyou.wssy001.banish.entity.BanishConfig;
import cyou.wssy001.banish.service.BanishConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @projectName: lava-banish
 * @className: ConfigController
 * @description:
 * @author: alexpetertyler
 * @date: 2021/1/29
 * @version: v1.0
 */
@RestController
@RequiredArgsConstructor
public class ConfigController {
    private final BanishConfigService banishConfigService;

    @GetMapping("/update")
    public String updateConfig(@RequestBody BanishConfig banishConfig) {
        banishConfigService.update(banishConfig);
        return "成功!";
    }
}
