package cyou.wssy001.banish.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cyou.wssy001.banish.entity.ClientInfo;
@DS("banish")
public interface ClientInfoDao extends BaseMapper<ClientInfo> {

}
