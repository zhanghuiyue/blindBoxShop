package cn.lili.modules.blindBox.serviceimpl;

import cn.lili.modules.blindBox.entity.dos.BlindBoxCategory;
import cn.lili.modules.blindBox.mapper.BlindBoxCategoryMapper;
import cn.lili.modules.blindBox.service.BlindBoxService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlindBoxServiceImpl extends ServiceImpl<BlindBoxCategoryMapper,BlindBoxCategory> implements BlindBoxService {
    @Override
    public List<BlindBoxCategory> queryBlindBoxCategoryList() {
        return this.queryBlindBoxCategoryList();
    }


}
