package com.fenglei.common.util.im;


public class Imtest {

    public static void main(String[] args) {
        long EXPIRETIME = 604800;
        TLSSigAPIv2 tlsSigAPIv2 = new TLSSigAPIv2(1400410230, "684f7c558bb7e39fc12a3a6208a5601cca12dcd7337bbd1537fe811b4bad0449");
        String result = tlsSigAPIv2.genUserSig("18888888888", EXPIRETIME);
        System.out.println(result);
    }
}
