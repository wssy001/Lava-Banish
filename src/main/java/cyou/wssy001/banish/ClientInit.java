package cyou.wssy001.banish;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.http.HttpUtil;
import cn.hutool.setting.Setting;
import com.alibaba.fastjson.JSON;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.HikariDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.hikari.HikariCpConfig;
import cyou.wssy001.banish.dao.BanDao;
import cyou.wssy001.banish.dao.BanNetworkDao;
import cyou.wssy001.banish.dao.ClientInfoDao;
import cyou.wssy001.banish.dto.BanishConfig;
import cyou.wssy001.banish.dto.ClientInfoDto;
import cyou.wssy001.banish.entity.Ban;
import cyou.wssy001.banish.entity.BanNetwork;
import cyou.wssy001.banish.entity.ClientInfo;
import cyou.wssy001.banish.service.BanishConfigService;
import cyou.wssy001.banish.service.BanishPlayerForgiveService;
import cyou.wssy001.banish.service.BanishPlayerHelpService;
import cyou.wssy001.banish.service.BanishPlayerPunishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.ofs.backend.chatcmdnew.model.ChatCommandDefinition;
import moe.ofs.backend.chatcmdnew.services.ChatCommandSetManageService;
import moe.ofs.backend.connector.DcsScriptConfigManager;
import moe.ofs.backend.discipline.service.PlayerConnectionValidationService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @projectName: lava-banish
 * @className: ClientInit
 * @description: Banish初始化类
 * @author: alexpetertyler
 * @date: 2021/1/15
 * @version: v1.0
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientInit {
    private final BanDao banDao;
    private final BanNetworkDao banNetworkDao;
    private final ClientInfoDao clientInfoDao;

    private final BanishPlayerForgiveService banishPlayerForgiveService;
    private final BanishPlayerPunishService banishPlayerPunishService;
    private final BanishPlayerHelpService banishPlayerHelpService;
    private final BanishConfigService banishConfigService;

    private final PlayerConnectionValidationService playerConnectionValidationService;
    private final ChatCommandSetManageService chatCommandSetManageService;

    private final HikariDataSourceCreator hikariDataSourceCreator;
    private final DynamicRoutingDataSource ds;
    private final Environment environment;

    public void init() {
        try {
            loadConfig();
            blockPlayer();
            addChatCMD();
        } catch (Exception e) {
            log.info("******Banish运行错误：" + e.getMessage());
        }
    }

    // 加载配置
    private void loadConfig() {
        String path = DcsScriptConfigManager.LAVA_DATA_PATH.resolve("config").resolve("banish.setting").toString();
        Setting setting = new Setting("banish.setting");
        if (!FileUtil.isFile(path)) {
            setting.set("url", "localhost:3306");
            setting.set("dbName", "banish_local");
            setting.set("userName", "root");
            setting.set("password", "root");
            setting.store(path);
        }

        DataSourceProperty property = new DataSourceProperty();
        property.setUsername(setting.get("userName"));
        property.setPassword(setting.get("password"));
        String url = setting.get("url");
        String dbName = setting.get("dbName");
        property.setUrl("jdbc:mysql://" + url + "/" + dbName + "?useUnicode=true&useSSL=false&autoReconnect=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&rewriteBatchedStatements=true");
        property.setDriverClassName("com.mysql.cj.jdbc.Driver");
        HikariCpConfig hikariConfig = new HikariCpConfig();
        hikariConfig.setIdleTimeout(43170000L);
        hikariConfig.setMaxLifetime(43170000L);
        hikariConfig.setMaxPoolSize(20);
        hikariConfig.setMinIdle(5);
        hikariConfig.setConnectionTimeout(43170000L);
        property.setHikari(hikariConfig);

        ds.addDataSource("banish", hikariDataSourceCreator.createDataSource(property));

        List<ClientInfo> clientInfos = clientInfoDao.selectList(null);
        if (clientInfos.size() != 1) {
            clientInfoDao.delete(null);
            throw new RuntimeException("请检查库表中的配置项");
        }

        ClientInfo clientInfo = clientInfos.get(0);
        if (!checkClientInfo(clientInfo)) throw new RuntimeException("请联系作者获取密钥！");

        BanishConfig banishConfig = new BanishConfig();
        banishConfig.setUuid(clientInfo.getUuid());
        banishConfig.setServerPublicKey(clientInfo.getServerPublicKey());
        banishConfig.setClientPrivateKey(clientInfo.getClientPrivateKey());
        banishConfig.setClientPublicKey(clientInfo.getClientPublicKey());
        banishConfig.setThinkingTime(1);
        banishConfig.setPunishTime(30);
        banishConfig.setRecordsSearchTime(1);
        banishConfig.setServerAddress(clientInfo.getServerAddress());
        clientRegister(banishConfig);
    }

    // 校验客户端信息
    private boolean checkClientInfo(ClientInfo clientInfo) {
        if (clientInfo == null) return false;
        if (StrUtil.hasBlank(clientInfo.getClientPrivateKey(), clientInfo.getClientPublicKey(), clientInfo.getServerPublicKey()))
            return false;
        if (StrUtil.isBlank(clientInfo.getUuid())) {
            clientInfo.setUuid(IdUtil.fastSimpleUUID());
            clientInfoDao.updateById(clientInfo);
        }
        return true;
    }

    // 客户端注册
    private void clientRegister(BanishConfig banishConfig) {
        ClientInfoDto clientInfoDto = new ClientInfoDto();
        clientInfoDto.setClientName(banishConfig.getClientName());
        clientInfoDto.setClientPublicKey(banishConfig.getClientPublicKey());
        clientInfoDto.setServerPublicKey(banishConfig.getServerPublicKey());
        clientInfoDto.setClientUUID(banishConfig.getUuid());
        String port = environment.getProperty("server.port");
        if (StrUtil.isBlank(port)) port = "8080";
        clientInfoDto.setClientPort(port);
        String jsonString = JSON.toJSONString(clientInfoDto);
        SymmetricCrypto sm4 = SmUtil.sm4(banishConfig.getServerPublicKey().substring(0, 16).getBytes());
        String encryptHex = sm4.encryptHex(jsonString);

        clientInfoDto.setCrypt(encryptHex);
        String s = JSON.toJSONString(clientInfoDto);
        String post = HttpUtil.post(banishConfig.getServerAddress() + "/client/verify", s, 3000);
        if (post.equals("失败")) throw new RuntimeException("客户端配置验证失败");

        banishConfigService.save(banishConfig);
    }

    // 导入封禁玩家
    private void blockPlayer() {
        List<Ban> banList = banDao.selectList(null);
        List<BanNetwork> banNetworkList = banNetworkDao.selectList(null);

        if (banList == null && banNetworkList == null) return;

        if (banList == null) banList = new ArrayList<>();
        if (banNetworkList == null) banNetworkList = new ArrayList<>();

        Map<Object, String> ban = banList.stream().collect(Collectors.toMap(Ban::getUcid, Ban::getReason));
        banNetworkList.forEach(v -> ban.put(v.getUcid(), v.getReason()));

        playerConnectionValidationService.blockPlayerUcid(ban);
    }

    // 添加聊天指令
    private void addChatCMD() {
        ChatCommandDefinition help = ChatCommandDefinition.builder()
                .name("help")
                .keyword("/banish help")
                .description("指令帮助")
                .consumer(banishPlayerHelpService::helpPlayer)
                .build();

        ChatCommandDefinition forgive = ChatCommandDefinition.builder()
                .name("forgive player")
                .keyword("/forgive")
                .description("当然是选择原谅Ta")
                .consumer(banishPlayerForgiveService::forgivePlayer)
                .build();

        ChatCommandDefinition punish = ChatCommandDefinition.builder()
                .name("punish bad guy")
                .keyword("/punish")
                .description("惩罚")
                .consumer(banishPlayerPunishService::punishPlayer)
                .build();

        chatCommandSetManageService.addCommandDefinition(help);
        chatCommandSetManageService.addCommandDefinition(forgive);
        chatCommandSetManageService.addCommandDefinition(punish);
    }

}