package cyou.wssy001.banish;

import cn.hutool.core.util.StrUtil;
import cyou.wssy001.banish.dao.BanDao;
import cyou.wssy001.banish.dao.BanNetworkDao;
import cyou.wssy001.banish.dao.ClientInfoDao;
import cyou.wssy001.banish.entity.Ban;
import cyou.wssy001.banish.entity.BanNetwork;
import cyou.wssy001.banish.entity.ClientInfo;
import cyou.wssy001.banish.service.BanishPlayerForgiveService;
import cyou.wssy001.banish.service.BanishPlayerHelpService;
import cyou.wssy001.banish.service.BanishPlayerPunishService;
import lombok.RequiredArgsConstructor;
import moe.ofs.backend.chatcmdnew.model.ChatCommandDefinition;
import moe.ofs.backend.chatcmdnew.services.ChatCommandSetManageService;
import moe.ofs.backend.discipline.service.PlayerConnectionValidationService;
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
public class ClientInit {
    private final BanDao banDao;
    private final BanNetworkDao banNetworkDao;
    private final ClientInfoDao clientInfoDao;

    private final BanishPlayerForgiveService banishPlayerForgiveService;
    private final BanishPlayerPunishService banishPlayerPunishService;
    private final BanishPlayerHelpService banishPlayerHelpService;

    private final PlayerConnectionValidationService playerConnectionValidationService;
    private final ChatCommandSetManageService chatCommandSetManageService;

    public void init() {
        clientRegister();
        blockPlayer();
        addChatCMD();
    }

    // 客户端注册
    private void clientRegister() {
        ClientInfo clientInfo = clientInfoDao.selectOne(null);
        if (checkClientInfo(clientInfo)) throw new RuntimeException("请联系作者获取密钥！");

    }

    // 校验客户端信息
    private boolean checkClientInfo(ClientInfo clientInfo) {
        if (clientInfo == null) return false;
        return !StrUtil.hasBlank(clientInfo.getClientPrivateKey(), clientInfo.getClientPublicKey(), clientInfo.getServerPublicKey());
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
                .keyword("/help banish")
                .description("指令帮助")
                .consumer(banishPlayerHelpService::helpPlayer)
                .build();

        ChatCommandDefinition forgive = ChatCommandDefinition.builder()
                .name("forgive player")
                .keyword("/forgive")
                .description("当然是选择原谅Ta /斜眼笑")
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