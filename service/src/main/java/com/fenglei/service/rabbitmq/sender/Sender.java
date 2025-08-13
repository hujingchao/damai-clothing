//package com.fenglei.service.rabbitmq.sender;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fenglei.service.rabbitmq.QueueEnum;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.core.MessageBuilder;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class Sender {
//
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    public void sendOrderCancelMessage(String orderId, final long delayTimes) {
//        try {
//            Message msg = MessageBuilder.withBody(objectMapper.writeValueAsBytes(orderId)).build();
//            this.rabbitTemplate.convertAndSend(
//                    QueueEnum.QUEUE_ORDER_CANCEL.getExchange(),
//                    QueueEnum.QUEUE_ORDER_CANCEL.getRouteKey(),
//                    msg,
//                    message -> {
//                        //注意这里时间可以使long，而且是设置header
//                        message.getMessageProperties().setHeader("x-delay", String.valueOf((delayTimes * 60 * 1000) + 3000));
//                        return message;
//                    }
//            );
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void sendMessage(QueueEnum queueEnum, String id, final long delayTimes) {
//        try {
//            Message msg = MessageBuilder.withBody(objectMapper.writeValueAsBytes(id)).build();
//            this.rabbitTemplate.convertAndSend(
//                    queueEnum.getExchange(),
//                    queueEnum.getRouteKey(),
//                    msg,
//                    message -> {
//                        //注意这里时间可以使long，而且是设置header
//                        message.getMessageProperties().setHeader("x-delay", String.valueOf(delayTimes));
//                        return message;
//                    }
//            );
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void endPreSale(QueueEnum queueEnum, String id,final long delayTimes) {
//        try {
//            Message msg = MessageBuilder.withBody(objectMapper.writeValueAsBytes(id)).build();
//            this.rabbitTemplate.convertAndSend(
//                    queueEnum.getExchange(),
//                    queueEnum.getRouteKey(),
//                    msg,
//                    message -> {
//                        //注意这里时间可以使long，而且是设置header
//                        message.getMessageProperties().setHeader("x-delay", String.valueOf(delayTimes));
//                        return message;
//                    }
//            );
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void startPreSale(QueueEnum queueEnum, String id, long delayTimes) {
//        try {
//            Message msg = MessageBuilder.withBody(objectMapper.writeValueAsBytes(id)).build();
//            this.rabbitTemplate.convertAndSend(
//                    queueEnum.getExchange(),
//                    queueEnum.getRouteKey(),
//                    msg,
//                    message -> {
//                        //注意这里时间可以使long，而且是设置header
//                        message.getMessageProperties().setHeader("x-delay", String.valueOf(delayTimes));
//                        return message;
//                    }
//            );
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public void sendAutoReceiptMessage(String id, Integer delayTime) {
//        try {
//            Message msg = MessageBuilder.withBody(objectMapper.writeValueAsBytes(id)).build();
//            this.rabbitTemplate.convertAndSend(
//                    QueueEnum.QUEUE_AUTO_RECEIPT.getExchange(),
//                    QueueEnum.QUEUE_AUTO_RECEIPT.getRouteKey(),
//                    msg,
//                    message -> {
//                        //注意这里时间可以使long，而且是设置header
//                        message.getMessageProperties().setHeader("x-delay", String.valueOf((delayTime * 60 * 1000) + 3000));
//                        return message;
//                    }
//            );
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
