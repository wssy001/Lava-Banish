package cyou.wssy001.banish.controller;

import cyou.wssy001.banish.dao.BanNetworkDao;
import cyou.wssy001.banish.dto.BanishList;
import cyou.wssy001.banish.entity.BanNetwork;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @projectName: lava-banish
 * @className: ReceiveController
 * @description:
 * @author: alexpetertyler
 * @date: 2021/2/1
 * @version: v1.0
 */
@RestController
@RequiredArgsConstructor
public class ClientController {
    private final BanNetworkDao banNetworkDao;

    @PostMapping("/banNet/update")
    public void banNetUpdate(@RequestBody BanishList<BanNetwork> banNetworkList) {

        banNetworkDao.delete(null);
        banNetworkList.forEach(banNetworkDao::insert);
    }
}
