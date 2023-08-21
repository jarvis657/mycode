package origin.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @Author:lmq
 * @Date: 2020/3/25
 * @Desc:
 **/
public class Service {
    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();
        consumer.setNamesrvAddr(Mq.RocketMQConstants.NAMESRV_ADDR);
        consumer.setConsumerGroup(Mq.RocketMQConstants.TRANSACTION_CONSUMER_GROUP);
        consumer.subscribe(Mq.RocketMQConstants.TRANSACTION_TOPIC_NAME, "*");
        consumer.registerMessageListener(new ProductListener());
        consumer.start();
        System.err.println("ProductService Start");
    }
}

class ProductListener implements MessageListenerConcurrently {
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        Optional.ofNullable(msgs).orElse(Collections.emptyList()).forEach(m -> {
            String orderId = m.getKeys();
            System.err.println("监听到下单消息,orderId: " + orderId + ", 商品服务减库存");
        });
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}