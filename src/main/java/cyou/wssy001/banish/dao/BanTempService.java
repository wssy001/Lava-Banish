package cyou.wssy001.banish.dao;

import cyou.wssy001.banish.dto.BanTemp;

import java.util.Date;
import java.util.List;

/**
 * @projectName: lava-banish
 * @className: BanTempService
 * @description:
 * @author: alexpetertyler
 * @date: 2021/1/25
 * @version: v1.0
 */
public interface BanTempService {

    void add(BanTemp banTemp);

    void add(List<BanTemp> banTempList);

    // 原谅最近一次TK的凶手
    long forgive(String victimPlayerUCID);

    // 原谅XX时间之后最近一次TK的凶手
    long forgive(String victimPlayerUCID, Date time);

    // 原谅当前所有TK的凶手
    long forgiveAll(String victimPlayerUCID);

    // 惩罚最近一次TK的凶手
    long punish(String victimPlayerUCID);

    // 惩罚XX时间之后最近一次TK的凶手
    long punish(String victimPlayerUCID, Date time);

    // 惩罚当前所有TK的凶手
    long punishAll(String victimPlayerUCID);
}