package cyou.wssy001.banish.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import cyou.wssy001.banish.entity.BanLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@DS("banish")
public interface BanLogDao extends BaseMapper<BanLog> {
    @Update("UPDATE ban_log ${ew.customSqlSegment}")
    long updateBatch(@Param(Constants.WRAPPER) Wrapper wrapper);
}
