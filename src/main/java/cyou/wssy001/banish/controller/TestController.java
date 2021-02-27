package cyou.wssy001.banish.controller;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import cyou.wssy001.banish.entity.BanNetwork;
import cyou.wssy001.banish.entity.BanTemp;
import cyou.wssy001.banish.service.BanTempService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.ofs.backend.connector.lua.LuaQueryEnv;
import moe.ofs.backend.connector.util.LuaScripts;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/banish")
@RequiredArgsConstructor
@Slf4j
public class TestController {
    private final BanTempService banTempService;

    @GetMapping("/test")
    public String test() {
        return HttpUtil.get("https://www.baidu.com", StandardCharsets.UTF_8);
    }

    @GetMapping("/add/punish")
    public void addBantemp() {
        BanTemp banTemp = new BanTemp();
        banTemp.setId(111111L);
        banTemp.setKillerUCID("test-KillerUCID");
        banTemp.setVictimPlayerUCID("test-victimUCID");
        banTemp.setTime(new Date());
        banTempService.add(banTemp);
        banTempService.punish("test-victimUCID");
    }

    @GetMapping("/add/BanNet")
    public void addBanNet() {
        BanNetwork banNetwork = new BanNetwork();
        banNetwork.setBannedFrom(new Date());
        banNetwork.setBannedUntil(new Date());
        banNetwork.setUcid("test-UCID");
        List<BanNetwork> banNetworkList = new ArrayList<>();
        HttpUtil.post("localhost:8080/banNet/update", JSON.toJSONString(banNetworkList), 3000);
    }

    @GetMapping("/makeScreenShot")
    public String makeScreenShot() {
        String luaString = LuaScripts.load("makePlayerScreenshot.lua")
                .replace("%victimId%", "1")
                .replace("%killerId%", "2");
        return LuaScripts.request(LuaQueryEnv.SERVER_CONTROL, luaString).get();
    }
}