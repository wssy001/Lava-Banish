package cyou.wssy001.banish.service;

import cn.hutool.core.util.StrUtil;
import cyou.wssy001.banish.dao.ClientInfoDao;
import cyou.wssy001.banish.entity.ClientInfo;
import lombok.RequiredArgsConstructor;
import moe.ofs.backend.discipline.service.PlayerConnectionValidationService;
import moe.ofs.backend.hookinterceptor.HookInterceptorDefinition;
import moe.ofs.backend.hookinterceptor.HookInterceptorProcessService;
import org.springframework.stereotype.Service;

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
    private final ClientInfoDao clientInfoDao;
    private final PlayerConnectionValidationService playerConnectionValidationService;

    public void init() {
        clientRegister();

    }

    // 客户端注册
    private void clientRegister() {
        ClientInfo clientInfo = clientInfoDao.selectOne(null);
        if (checkClientInfo(clientInfo)) throw new RuntimeException("请联系作者获取密钥！");

        registerHook();
    }

    private boolean checkClientInfo(ClientInfo clientInfo) {
        if (clientInfo == null) return false;
        return !StrUtil.hasBlank(clientInfo.getClientPrivateKey(), clientInfo.getClientPublicKey(), clientInfo.getServerPublicKey());
    }

    private void registerHook() {

        playerConnectionValidationService.blockPlayerUcid("interceptor");
    }
}