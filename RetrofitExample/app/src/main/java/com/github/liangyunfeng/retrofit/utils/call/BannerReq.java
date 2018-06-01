package com.github.liangyunfeng.retrofit.utils.call;

import java.io.Serializable;

/**
 * Created by yunfeng.l on 2018/5/28.
 */

public class BannerReq implements Serializable {
    private static final long serialVersionUID = 1L;
    public int num;
    public String version = "v1.0.0";
    public String system = "360";

    public BannerReq(int num) {
        super();
        this.num = num;
    }

    @Override
    public String toString() {
        return "BannerReq [num=" + num + ", version=" + version + ", system="
                + system + "]";
    }
}

