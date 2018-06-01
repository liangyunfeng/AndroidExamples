package com.github.liangyunfeng.retrofit.example.demo2.encrypt;

import java.security.Provider;

/**
 * Created by yunfeng.l on 2018/5/31.
 */

public class BouncyCastleProvider extends Provider {

    public BouncyCastleProvider() {
        super("", 1, "");
    }

    public BouncyCastleProvider(String name, double version, String info) {
        super(name, version, info);
    }

}
