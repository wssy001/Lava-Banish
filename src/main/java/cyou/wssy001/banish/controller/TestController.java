package cyou.wssy001.banish.controller;

import cn.hutool.http.HttpUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
public class TestController {
    @GetMapping("/addon/test")
    public String test() {
        return HttpUtil.get("https://www.baidu.com", StandardCharsets.UTF_8);
    }
}