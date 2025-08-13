//package com.fenglei.service.rabbitmq;
//
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.CustomExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * rabbitmq配置类，
// * 为了简单，我们这里只配置了Queue
// * 至于exchanges、brokers等用的默认配置
// *
// */
//@Configuration
//public class RabbitConfig {
//
//
//    /**
//     * 取消订单
//     * 延时队列交换机
//     * 注意这里的交换机类型：CustomExchange
//     * @return
//     */
//    @Bean
//    public CustomExchange orderCancelDelayExchange(){
//        Map<String, Object> args = new HashMap<>();
//        args.put("x-delayed-type", "direct");
//        return new CustomExchange(QueueEnum.QUEUE_ORDER_CANCEL.getExchange(),"x-delayed-message",true, false,args);
//    }
//
//    /**
//     * 取消订单
//     * 延时队列
//     * @return
//     */
//    @Bean
//    public Queue orderCancelDelayQueue(){
//        return new Queue(QueueEnum.QUEUE_ORDER_CANCEL.getName(),true);
//    }
//
//    /**
//     * 取消订单
//     * 给延时队列绑定交换机
//     * @return
//     */
//    @Bean
//    public Binding orderCancelDelayBinding(Queue orderCancelDelayQueue,CustomExchange orderCancelDelayExchange){
//        return BindingBuilder.bind(orderCancelDelayQueue).to(orderCancelDelayExchange).with(QueueEnum.QUEUE_ORDER_CANCEL.getRouteKey()).noargs();
//    }
//
//
//    /**
//     * 开始订单促销
//     * 延时队列交换机
//     * 注意这里的交换机类型：CustomExchange
//     * @return
//     */
//    @Bean
//    public CustomExchange orderPromotionStartDelayExchange(){
//        Map<String, Object> args = new HashMap<>();
//        args.put("x-delayed-type", "direct");
//        return new CustomExchange(QueueEnum.QUEUE_ORDER_PROMOTION_START.getExchange(),"x-delayed-message",true, false,args);
//    }
//
//    /**
//     * 开始订单促销
//     * 延时队列
//     * @return
//     */
//    @Bean
//    public Queue orderPromotionStartDelayQueue(){
//        return new Queue(QueueEnum.QUEUE_ORDER_PROMOTION_START.getName(),true);
//    }
//
//    /**
//     * 开始订单促销
//     * 给延时队列绑定交换机
//     * @return
//     */
//    @Bean
//    public Binding orderPromotionStartDelayBinding(Queue orderPromotionStartDelayQueue,CustomExchange orderPromotionStartDelayExchange){
//        return BindingBuilder.bind(orderPromotionStartDelayQueue).to(orderPromotionStartDelayExchange).with(QueueEnum.QUEUE_ORDER_PROMOTION_START.getRouteKey()).noargs();
//    }
//
//
//
//    /**
//     * 取消订单促销
//     * 延时队列交换机
//     * 注意这里的交换机类型：CustomExchange
//     * @return
//     */
//    @Bean
//    public CustomExchange orderPromotionCancelDelayExchange(){
//        Map<String, Object> args = new HashMap<>();
//        args.put("x-delayed-type", "direct");
//        return new CustomExchange(QueueEnum.QUEUE_ORDER_PROMOTION_CANCEL.getExchange(),"x-delayed-message",true, false,args);
//    }
//
//    /**
//     * 取消订单促销
//     * 延时队列
//     * @return
//     */
//    @Bean
//    public Queue orderPromotionCancelDelayQueue(){
//        return new Queue(QueueEnum.QUEUE_ORDER_PROMOTION_CANCEL.getName(),true);
//    }
//
//    /**
//     * 取消订单促销
//     * 给延时队列绑定交换机
//     * @return
//     */
//    @Bean
//    public Binding orderPromotionCancelDelayBinding(Queue orderPromotionCancelDelayQueue,CustomExchange orderPromotionCancelDelayExchange){
//        return BindingBuilder.bind(orderPromotionCancelDelayQueue).to(orderPromotionCancelDelayExchange).with(QueueEnum.QUEUE_ORDER_PROMOTION_CANCEL.getRouteKey()).noargs();
//    }
//
//
//    /**
//     * 开始商品促销
//     * 延时队列交换机
//     * 注意这里的交换机类型：CustomExchange
//     * @return
//     */
//    @Bean
//    public CustomExchange productPromotionStartDelayExchange(){
//        Map<String, Object> args = new HashMap<>();
//        args.put("x-delayed-type", "direct");
//        return new CustomExchange(QueueEnum.QUEUE_PRODUCT_PROMOTION_START.getExchange(),"x-delayed-message",true, false,args);
//    }
//
//    /**
//     * 取消商品促销
//     * 延时队列
//     * @return
//     */
//    @Bean
//    public Queue productPromotionStartDelayQueue(){
//        return new Queue(QueueEnum.QUEUE_PRODUCT_PROMOTION_START.getName(),true);
//    }
//
//    /**
//     * 取消商品促销
//     * 给延时队列绑定交换机
//     * @return
//     */
//    @Bean
//    public Binding productPromotionStartDelayBinding(Queue productPromotionStartDelayQueue,CustomExchange productPromotionStartDelayExchange){
//        return BindingBuilder.bind(productPromotionStartDelayQueue).to(productPromotionStartDelayExchange).with(QueueEnum.QUEUE_PRODUCT_PROMOTION_START.getRouteKey()).noargs();
//    }
//
//
//
//    /**
//     * 取消商品促销
//     * 延时队列交换机
//     * 注意这里的交换机类型：CustomExchange
//     * @return
//     */
//    @Bean
//    public CustomExchange productPromotionCancelDelayExchange(){
//        Map<String, Object> args = new HashMap<>();
//        args.put("x-delayed-type", "direct");
//        return new CustomExchange(QueueEnum.QUEUE_PRODUCT_PROMOTION_CANCEL.getExchange(),"x-delayed-message",true, false,args);
//    }
//
//    /**
//     * 取消商品促销
//     * 延时队列
//     * @return
//     */
//    @Bean
//    public Queue productPromotionCancelDelayQueue(){
//        return new Queue(QueueEnum.QUEUE_PRODUCT_PROMOTION_CANCEL.getName(),true);
//    }
//
//    /**
//     * 取消商品促销
//     * 给延时队列绑定交换机
//     * @return
//     */
//    @Bean
//    public Binding productPromotionCancelDelayBinding(Queue productPromotionCancelDelayQueue,CustomExchange productPromotionCancelDelayExchange){
//        return BindingBuilder.bind(productPromotionCancelDelayQueue).to(productPromotionCancelDelayExchange).with(QueueEnum.QUEUE_PRODUCT_PROMOTION_CANCEL.getRouteKey()).noargs();
//    }
//
//
//    /**
//     * 作废优惠券
//     * 延时队列交换机
//     * 注意这里的交换机类型：CustomExchange
//     * @return
//     */
//    @Bean
//    public CustomExchange couponInvalidDelayExchange(){
//        Map<String, Object> args = new HashMap<>();
//        args.put("x-delayed-type", "direct");
//        return new CustomExchange(QueueEnum.QUEUE_COUPON_INVALID.getExchange(),"x-delayed-message",true, false,args);
//    }
//    /**
//     * 作废优惠券
//     * 延时队列
//     * @return
//     */
//    @Bean
//    public Queue couponInvalidDelayQueue(){
//        return new Queue(QueueEnum.QUEUE_COUPON_INVALID.getName(),true);
//    }
//    /**
//     * 作废优惠券
//     * 给延时队列绑定交换机
//     * 参数命名必须是上面两个方法名
//     * @return
//     */
//    @Bean
//    public Binding couponInvalidDelayBinding(Queue couponInvalidDelayQueue,CustomExchange couponInvalidDelayExchange){
//        return BindingBuilder.bind(couponInvalidDelayQueue).to(couponInvalidDelayExchange).with(QueueEnum.QUEUE_COUPON_INVALID.getRouteKey()).noargs();
//    }
//
//
//    /**
//     * 作废领取的优惠券
//     * 延时队列交换机
//     * 注意这里的交换机类型：CustomExchange
//     * @return
//     */
//    @Bean
//    public CustomExchange pickedCouponInvalidDelayExchange(){
//        Map<String, Object> args = new HashMap<>();
//        args.put("x-delayed-type", "direct");
//        return new CustomExchange(QueueEnum.QUEUE_PICKED_COUPON_INVALID.getExchange(),"x-delayed-message",true, false,args);
//    }
//    /**
//     * 作废领取的优惠券
//     * 延时队列
//     * @return
//     */
//    @Bean
//    public Queue pickedCouponInvalidDelayQueue(){
//        return new Queue(QueueEnum.QUEUE_PICKED_COUPON_INVALID.getName(),true);
//    }
//    /**
//     * 作废领取的优惠券
//     * 给延时队列绑定交换机
//     * 参数命名必须是上面两个方法名
//     * @return
//     */
//    @Bean
//    public Binding pickedCouponInvalidDelayBinding(Queue pickedCouponInvalidDelayQueue,CustomExchange pickedCouponInvalidDelayExchange){
//        return BindingBuilder.bind(pickedCouponInvalidDelayQueue).to(pickedCouponInvalidDelayExchange).with(QueueEnum.QUEUE_PICKED_COUPON_INVALID.getRouteKey()).noargs();
//    }
//
//
//    /**
//     * 开始预售
//     * 延时队列交换机
//     * 注意这里的交换机类型：CustomExchange
//     * @return
//     */
//    @Bean
//    public CustomExchange startPreSaleDelayExchange(){
//        Map<String, Object> args = new HashMap<>();
//        args.put("x-delayed-type", "direct");
//        return new CustomExchange(QueueEnum.QUEUE_START_PRE_SALE.getExchange(),"x-delayed-message",true, false,args);
//    }
//    /**
//     * 开始预售
//     * 延时队列
//     * @return
//     */
//    @Bean
//    public Queue startPreSaleDelayQueue(){
//        return new Queue(QueueEnum.QUEUE_START_PRE_SALE.getName(),true);
//    }
//    /**
//     * 结束预售
//     * 给延时队列绑定交换机
//     * 参数命名必须是上面两个方法名
//     * @return
//     */
//    @Bean
//    public Binding startPreSaleDelayBinding(Queue startPreSaleDelayQueue,CustomExchange startPreSaleDelayExchange){
//        return BindingBuilder.bind(startPreSaleDelayQueue).to(startPreSaleDelayExchange).with(QueueEnum.QUEUE_START_PRE_SALE.getRouteKey()).noargs();
//    }
//
//
//    /**
//     * 开始预售
//     * 延时队列交换机
//     * 注意这里的交换机类型：CustomExchange
//     * @return
//     */
//    @Bean
//    public CustomExchange endPreSaleDelayExchange(){
//        Map<String, Object> args = new HashMap<>();
//        args.put("x-delayed-type", "direct");
//        return new CustomExchange(QueueEnum.QUEUE_END_PRE_SALE.getExchange(),"x-delayed-message",true, false,args);
//    }
//    /**
//     * 结束预售
//     * 延时队列
//     * @return
//     */
//    @Bean
//    public Queue endPreSaleDelayQueue(){
//        return new Queue(QueueEnum.QUEUE_END_PRE_SALE.getName(),true);
//    }
//    /**
//     * 结束预售
//     * 给延时队列绑定交换机
//     * 参数命名必须是上面两个方法名
//     * @return
//     */
//    @Bean
//    public Binding endPreSaleDelayBinding(Queue endPreSaleDelayQueue,CustomExchange endPreSaleDelayExchange){
//        return BindingBuilder.bind(endPreSaleDelayQueue).to(endPreSaleDelayExchange).with(QueueEnum.QUEUE_END_PRE_SALE.getRouteKey()).noargs();
//    }
//
//
//
//
//    @Bean
//    public CustomExchange autoReceiptDelayExchange(){
//        Map<String, Object> args = new HashMap<>();
//        args.put("x-delayed-type", "direct");
//        return new CustomExchange(QueueEnum.QUEUE_AUTO_RECEIPT.getExchange(),"x-delayed-message",true, false,args);
//    }
//
//    @Bean
//    public Queue autoReceiptDelayQueue(){
//        return new Queue(QueueEnum.QUEUE_AUTO_RECEIPT.getName(),true);
//    }
//
//
//    @Bean
//    public Binding autoReceiptDelayBinding(Queue autoReceiptDelayQueue,CustomExchange autoReceiptDelayExchange){
//        return BindingBuilder.bind(autoReceiptDelayQueue).to(autoReceiptDelayExchange).with(QueueEnum.QUEUE_AUTO_RECEIPT.getRouteKey()).noargs();
//    }
//
//}
