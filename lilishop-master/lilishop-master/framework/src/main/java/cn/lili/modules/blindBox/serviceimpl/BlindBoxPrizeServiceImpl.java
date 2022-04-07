package cn.lili.modules.blindBox.serviceimpl;

import cn.lili.modules.blindBox.entity.dos.Prize;
import cn.lili.modules.blindBox.mapper.PrizeMapper;
import cn.lili.modules.blindBox.service.BlindBoxPrizeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlindBoxPrizeServiceImpl extends ServiceImpl<PrizeMapper, Prize> implements BlindBoxPrizeService {


    @Override
    public boolean batchAddPrize(List<Prize> prize) {
        return this.saveBatch(prize);
    }
}
