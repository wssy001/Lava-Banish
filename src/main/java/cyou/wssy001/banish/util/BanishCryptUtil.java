package cyou.wssy001.banish.util;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.asymmetric.SM2;
import cyou.wssy001.banish.dto.BanishConfig;
import cyou.wssy001.banish.service.BanishConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @projectName: lava-banish
 * @className: BanishCryptUtil
 * @description:
 * @author: alexpetertyler
 * @date: 2021/2/1
 * @version: v1.0
 */
@Component
public class BanishCryptUtil {
    private static BanishConfigService banishConfigService;

    @Autowired
    public BanishCryptUtil(BanishConfigService banishConfigService) {
        BanishCryptUtil.banishConfigService = banishConfigService;
    }


    public static boolean verify(String str, String sign) {
        return verify(str, sign, false);
    }

    public static boolean verify(String str, String sign, boolean b) {
        return getSM2(b).verifyHex(HexUtil.encodeHexStr(str), sign);
    }

    public static String sign(String str) {
        return sign(str, false);
    }

    public static String sign(String str, boolean b) {
        return getSM2(b).signHex(HexUtil.encodeHexStr(str));

    }

    //    仅用于重新生成密钥对
    private static SM2 getSM2(boolean b) {
        BanishConfig banishConfig = banishConfigService.find();
        if (b) {
            String privateKey = banishConfig.getClientPrivateKey();
            String publicKey = banishConfig.getClientPublicKey();
            return new SM2(privateKey, publicKey);
        } else {
            String privateKey = banishConfig.getClientPrivateKey();
            String publicKey = banishConfig.getServerPublicKey();
            return new SM2(privateKey, publicKey);
        }
    }
}
