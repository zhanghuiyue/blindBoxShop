package cn.lili.timetask.handler.impl.blindbox;

import cn.lili.modules.blindBox.entity.dos.Tribe;
import cn.lili.modules.blindBox.enums.StatusEnum;
import cn.lili.modules.blindBox.service.TribeService;
import cn.lili.timetask.handler.EveryDayZeroExecute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CancelBlindBoxTribeTaskExcute implements EveryDayZeroExecute {

    @Autowired
    private TribeService tribeService;

    @Override
    public void execute() {
        List<Tribe>  tribeList = tribeService.queryTribelist();
        for (Tribe tribe:tribeList) {
            tribe.setStatus(StatusEnum.UNVALID.name());
        }
        tribeService.updateBatchById(tribeList);
    }
}
