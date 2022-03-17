package cn.lili.modules.promotion.listener;

import cn.lili.common.properties.RocketmqCustomProperties;
import cn.lili.modules.promotion.event.UpdateEsGoodsIndexPromotionsEvent;
import cn.lili.rocketmq.RocketmqSendCallbackBuilder;
import cn.lili.rocketmq.tags.GoodsTagsEnum;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author paulG
 * @since 2022/1/19
 **/
@Component
public class UpdateEsGoodsIndexPromotionsListener {

    /**
     * rocketMq
     */
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    /**
     * rocketMq配置
     */
    @Autowired
    private RocketmqCustomProperties rocketmqCustomProperties;


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void updateEsGoodsIndexPromotions(UpdateEsGoodsIndexPromotionsEvent event) {
        //更新商品促销消息
        String destination = rocketmqCustomProperties.getGoodsTopic() + ":" + GoodsTagsEnum.UPDATE_GOODS_INDEX_PROMOTIONS.name();
        //发送mq消息
        rocketMQTemplate.asyncSend(destination, event.getPromotionsJsonStr(), RocketmqSendCallbackBuilder.commonCallback());
    }


}
