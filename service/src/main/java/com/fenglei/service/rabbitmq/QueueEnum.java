package com.fenglei.service.rabbitmq;

import lombok.Getter;

@Getter
public enum QueueEnum {
    QUEUE_ORDER_CANCEL("eos.saleOutStock.cancel", "eos.saleOutStock.cancel", "eos.saleOutStock.cancel"), // 取消订单

    QUEUE_ORDER_PROMOTION_START("eos.orderPromotion.start", "eos.orderPromotion.start", "eos.orderPromotion.start"), // 开始订单促销
    QUEUE_ORDER_PROMOTION_CANCEL("eos.orderPromotion.cancel", "eos.orderPromotion.cancel", "eos.orderPromotion.cancel"), // 取消订单促销
    QUEUE_PRODUCT_PROMOTION_START("eos.productPromotion.start", "eos.productPromotion.start", "eos.productPromotion.start"), // 开始商品促销
    QUEUE_PRODUCT_PROMOTION_CANCEL("eos.productPromotion.cancel", "eos.productPromotion.cancel", "eos.productPromotion.cancel"), // 取消商品促销
    QUEUE_COUPON_INVALID("eos.coupon.invalid", "eos.coupon.invalid", "eos.coupon.invalid"), // 作废优惠券
    QUEUE_PICKED_COUPON_INVALID("eos.picked.coupon.invalid", "eos.picked.coupon.invalid", "eos.coupon.invalid"), // 作废领取的优惠券
    QUEUE_START_PRE_SALE("eos.preSaleProduct.start","eos.preSaleProduct.start","eos.preSaleProduct.start"),//开始预售
    QUEUE_END_PRE_SALE("eos.preSaleProduct.end","eos.preSaleProduct.end","eos.preSaleProduct.end"),//结束预售

    QUEUE_AUTO_RECEIPT("eos.outStockBill.receipt","eos.outStockBill.receipt","eos.outStockBill.receipt"),
    ;


    /**
     * 交换名称
     */
    private String exchange;
    /**
     * 队列名称
     */
    private String name;
    /**
     * 路由键
     */
    private String routeKey;

    QueueEnum(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }
}
