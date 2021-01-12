package cyou.wssy001.banish.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import moe.ofs.backend.BackendApplication;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @projectName: lava-banish
 * @className: UUIDInit
 * @description:
 * @author: alexpetertyler
 * @date: 2021/1/11
 * @version: v1.0
 */
@Service
@RequiredArgsConstructor
public class UUIDInit {
    private final Environment environment;

    public void generateUUID() {
        String uuid = environment.getProperty("UUID");
        if (StrUtil.isBlank(uuid)) {
            ApplicationHome ah = new ApplicationHome(BackendApplication.class);
            String docStorePath = ah.getSource().getParentFile().toString();
            boolean check = FileUtil.isFile(new File(docStorePath + "/application.properties"));
            if (!check) {
                uuid = IdUtil.fastSimpleUUID();

                FileWriter fileWriter = new FileWriter(docStorePath + "/application.properties");
                fileWriter.write("UUID=" + uuid);
                fileWriter.append("\r\nspring.profiles.active=" + environment.getProperty("spring.profiles.active"));
            }
        }
    }
}
