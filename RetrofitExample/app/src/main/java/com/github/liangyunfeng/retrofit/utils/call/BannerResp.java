package com.github.liangyunfeng.retrofit.utils.call;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yunfeng.l on 2018/5/28.
 */

public class BannerResp implements Serializable {
    private static final long serialVersionUID = 1L;
    public int code;
    public int num;
    public String msg;
    public List<Banner> data;

    public class Banner implements Serializable {
        private static final long serialVersionUID = 1L;
        public String id;
        public String name;

        @Override
        public String toString() {
            return "Banner [id=" + id + ", name=" + name + "]";
        }
    }

    @Override
    public String toString() {
        return "BannerResp [code=" + code + ", num=" + num + ", msg=" + msg
                + ", data=" + data + "]";
    }
}
