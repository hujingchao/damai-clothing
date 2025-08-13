package com.fenglei.service.rabbitmq.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class MqDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
}
