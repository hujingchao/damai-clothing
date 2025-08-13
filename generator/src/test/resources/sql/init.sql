CREATE TABLE `sms_order_promotion` (
                                       `id` bigint(20) NOT NULL,
                                       `no` varchar(20) NOT NULL,
                                       `create_time` datetime NOT NULL COMMENT '创建时间',
                                       `creator` varchar(20) NOT NULL COMMENT '创建人',
                                       `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                       `updater` varchar(20) DEFAULT NULL COMMENT '更新人',
                                       `bill_status` int(1) NOT NULL COMMENT '状态：1-促销中，0-已过期，2-未开始',
                                       `content` varchar(255) NOT NULL COMMENT '促销内容',
                                       `begin_date` datetime NOT NULL COMMENT '促销开始时间',
                                       `end_date` datetime NOT NULL COMMENT '促销结束时间',
                                       `promotion_customer` varchar(255) NOT NULL COMMENT '促销对象：多个，以逗号隔开',
                                       `is_step` tinyint(1) NOT NULL COMMENT '是否启用阶梯促销：0-否，1-是',
                                       `promotion_type` int(1) NOT NULL COMMENT '促销规则：1-买赠，2-满减，3-打折（应付订单总额=订单金额x折扣）',
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
