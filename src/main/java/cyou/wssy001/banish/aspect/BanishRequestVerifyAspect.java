//package cyou.wssy001.banish.aspect;
//
//import cn.hutool.http.HttpUtil;
//import com.alibaba.fastjson.JSON;
//import cyou.wssy001.banish.dao.BanDao;
//import cyou.wssy001.banish.dto.BanDto;
//import cyou.wssy001.banish.dto.BanishList;
//import cyou.wssy001.banish.entity.Ban;
//import cyou.wssy001.banish.dto.BanishConfig;
//import cyou.wssy001.banish.service.BanishConfigService;
//import cyou.wssy001.banish.util.BanishCryptUtil;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Set;
//
///**
// * @projectName: lava-banish
// * @className: BanishBanColletctAspect
// * @description:
// * @author: alexpetertyler
// * @date: 2021/2/2
// * @version: v1.0
// */
//@Aspect
//@RequiredArgsConstructor
//@Component
//@Slf4j
//public class BanishBanCollectAspect {
//    private final BanDao banDao;
//    private final BanishConfigService banishConfigService;
//
//    @Before("execution(public void cyou.wssy001.banish.service.BanTempServiceImpl.punish(java.lang.Long))")
//    public void banCollect(JoinPoint point) {
//        log.info("******进入了banCollect切面");
//        log.info("******" + JSON.toJSONString(point.getArgs()));
//
//        Long id = (Long) point.getArgs()[0];
//        Ban ban = banDao.selectById(id);
//        BanishConfig banishConfig = banishConfigService.find();
//        String uuid = banishConfig.getUuid();
//        BanDto banDto = new BanDto(uuid, ban.getUcid(), ban.getIpaddr(), ban.getName());
//        banDto.setClientUUID(uuid);
//        banDto.setSign(BanishCryptUtil.sign(JSON.toJSONString(banDto)));
//        HttpUtil.post(banishConfig.getServerAddress() + "/ban/add", JSON.toJSONString(banDto), 3000);
//    }
//
//    @Before("execution(public void cyou.wssy001.banish.service.BanTempServiceImpl.punish(java.util.Set<java.lang.Long>))")
//    public void banBatchCollect(JoinPoint point) {
//        log.info("******进入了banBatchCollect切面");
//        log.info("******" + JSON.toJSONString(point.getArgs()));
//
//        Set<Long> arg = (Set<Long>) point.getArgs()[0];
//        List<Ban> bans = banDao.selectBatchIds(arg);
//        BanishConfig banishConfig = banishConfigService.find();
//        String uuid = banishConfig.getUuid();
//        BanishList<BanDto> banDtoList = new BanishList<>();
//        bans.forEach(ban -> {
//            BanDto banDto = new BanDto(uuid, ban.getUcid(), ban.getIpaddr(), ban.getName());
//            banDtoList.add(banDto);
//        });
//        banDtoList.setClientUUID(uuid);
//        String sign = BanishCryptUtil.sign(JSON.toJSONString(banDtoList));
//        banDtoList.setSign(sign);
//        HttpUtil.post(banishConfig.getServerAddress() + "/ban/add/batch", JSON.toJSONString(banDtoList), 3000);
//    }
//
//}
