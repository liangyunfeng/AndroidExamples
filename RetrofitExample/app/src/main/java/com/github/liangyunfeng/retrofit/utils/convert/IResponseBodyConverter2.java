package com.github.liangyunfeng.retrofit.utils.convert;

import com.github.liangyunfeng.retrofit.utils.AesEncryptionUtil;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by yunfeng.l on 2018/5/28.
 */

public class IResponseBodyConverter2 implements Converter<ResponseBody, String> {

    @Override
    public String convert(ResponseBody value) throws IOException {
        String string = value.string();
        System.out.println("#解密前@#" + string);
        string = AesEncryptionUtil.decrypt(string);
        System.out.println("#解密后@#" + string);
        return string;
    }
}

