package com.fenglei.service.workFlow.util;

import java.util.UUID;

/**
 * @author pc
 */
public class UUIDUTIL {
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
