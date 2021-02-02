package cyou.wssy001.banish.aspect;

import com.alibaba.fastjson.JSON;
import cyou.wssy001.banish.dto.BanishList;
import cyou.wssy001.banish.entity.BanNetwork;
import cyou.wssy001.banish.util.BanishCryptUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @projectName: lava-banish
 * @className: BanishRequestVerifyAspect
 * @description:
 * @author: alexpetertyler
 * @date: 2021/2/1
 * @version: v1.0
 */
@Aspect
@Component
public class BanishRequestVerifyAspect {

    @Before("execution(public * cyou.wssy001.banish.controller.ClientController.banNetUpdate(..))")
    public BanishList<BanNetwork> verify(JoinPoint point) {
        BanishList<BanNetwork> arg = (BanishList<BanNetwork>) point.getArgs()[0];
        String sign = arg.getSign();
        arg.setSign(null);
        boolean verify = BanishCryptUtil.verify(JSON.toJSONString(arg), sign);
        if (verify) return arg;
        throw new RuntimeException("验签失败，疑似非法请求");
    }

}
