//package com.fenglei.service.rabbitmq.receiver;
//
//import com.fenglei.service.rabbitmq.model.MqDto;
//import com.rabbitmq.client.Channel;
//import org.springframework.amqp.core.Message;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.io.IOException;
//
///**
// * 通过@RabbitListener对forecastPush队列进行监听
// */
//@Component
//public class Receiver {
//    @Resource
//    RedisTemplate redisTemplate;
//
//
//    private MqDto getMsgMap(Message msg, Channel channel) throws Exception {
//        // 获取订单
//        MqDto mqDto = new MqDto();
//        try {
//            String message = new String(msg.getBody(), "UTF-8");
//            if (message.indexOf("\"") >= 0) {
//                message = message.substring(message.indexOf("\"") + 1);
//            }
//            if (message.lastIndexOf("\"") >= 0) {
//                message = message.substring(0, message.lastIndexOf("\""));
//            }
//            mqDto.setId(message);
//        } catch (Exception e) {
//            System.out.println("监听消费消息 发生异常： " + e.fillInStackTrace());
//        }
//        return mqDto;
//
//    }
//
//    void errorAction(String key, Channel channel, long deliveryTag) throws IOException {
//        if (!redisTemplate.hasKey(key)) {
//            redisTemplate.opsForValue().set(key, 1);
//            channel.basicNack(deliveryTag, false, true);
//        } else {
//            Integer times = (Integer) redisTemplate.opsForValue().get(key);
//            if (times == 3) {
//                redisTemplate.delete(key);
//                channel.basicNack(deliveryTag, false, false);
//            } else {
//                redisTemplate.opsForValue().set(key, ++times);
//                channel.basicNack(deliveryTag, false, true);
//            }
//        }
//    }
//
//}
