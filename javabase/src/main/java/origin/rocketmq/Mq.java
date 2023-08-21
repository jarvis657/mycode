package origin.rocketmq;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author:lmq
 * @Date: 2020/3/25
 * @Desc:
 **/
public class Mq {
    public static void main(String[] args) throws Exception {
        TransactionMQProducer producer = new TransactionMQProducer();
        producer.setNamesrvAddr(RocketMQConstants.NAMESRV_ADDR);
        producer.setProducerGroup(RocketMQConstants.TRANSACTION_PRODUCER_GROUP);

        //自定义线程池,执行事务操作
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 50, 10L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(20), (Runnable r) -> new Thread("Order Transaction Massage Thread"));
        producer.setExecutorService(executor);

        //设置事务消息监听器
        producer.setTransactionListener(new OrderTransactionListener());

        producer.start();

        System.err.println("OrderService Start");

        for (int i = 0; i < 10; i++) {
            String orderId = UUID.randomUUID().toString();
            String payload = "下单,orderId: " + orderId;
            String tags = "Tag";
            Message message = new Message(RocketMQConstants.TRANSACTION_TOPIC_NAME, tags, orderId, payload.getBytes(RemotingHelper.DEFAULT_CHARSET));

            //发送事务消息
            TransactionSendResult result = producer.sendMessageInTransaction(message, orderId);
            System.err.println("发送事务消息,发送结果: " + result);
        }
    }

    public static class RocketMQConstants {
        public static final String TRANSACTION_TOPIC_NAME = "";
        public static final String NAMESRV_ADDR = "";
        public static final String TRANSACTION_PRODUCER_GROUP = "";
        public static final String TRANSACTION_CONSUMER_GROUP = "";
    }
}

class OrderTransactionListener implements TransactionListener {
    private static final Map<String, Boolean> results = new ConcurrentHashMap<>();

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        String orderId = (String) arg;

        //记录本地事务执行结果
        boolean success = persistTransactionResult(orderId);
        System.err.println("订单服务执行本地事务下单,orderId: " + orderId + ", result: " + success);
        return success ? LocalTransactionState.COMMIT_MESSAGE : LocalTransactionState.ROLLBACK_MESSAGE;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        String orderId = msg.getKeys();
        System.err.println("执行事务消息回查,orderId: " + orderId);
        return Boolean.TRUE.equals(results.get(orderId)) ? LocalTransactionState.COMMIT_MESSAGE : LocalTransactionState.ROLLBACK_MESSAGE;
    }

    private boolean persistTransactionResult(String orderId) {
        boolean success = Math.abs(Objects.hash(orderId)) % 2 == 0;
        results.put(orderId, success);
        return success;
    }
}

