package com.github.liangyunfeng.retrofit.example.demo2;

import android.support.annotation.Nullable;
import android.util.Log;

import com.github.liangyunfeng.retrofit.example.demo2.encrypt.Contacts;
import com.github.liangyunfeng.retrofit.example.demo2.encrypt.TripleDES;
import com.github.liangyunfeng.retrofit.example.demo2.utils.GzipUtil;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by yunfeng.l on 2018/5/28.
 */

public final class DecodeConverterFactory extends Converter.Factory {

    public static DecodeConverterFactory create() {
        return create(new Gson());
    }

    public static DecodeConverterFactory create(Gson gson) {
        return new DecodeConverterFactory(gson);
    }

    private final Gson gson;

    private DecodeConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new DecodeRequestBodyConverter<>(gson, adapter);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new DecodeResponseBodyConverter<>(adapter);
    }
}


// 分别实现request,response转换器
class DecodeRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    DecodeRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        okio.Buffer buffer = new okio.Buffer();
        Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
        JsonWriter jsonWriter = gson.newJsonWriter(writer);
        adapter.write(jsonWriter, value);
        jsonWriter.flush();
        byte[] bytes = buffer.readByteArray();
        try {
            //先压缩 再加密
            bytes = GzipUtil.compress(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //bytes = new TripleDES(Contacts.PARAMETER_ENCRYPTION_KEY).encryptionByteData(bytes);
        bytes = TripleDES.tDesEncryptCBC(new String(bytes,"UTF-8"), Contacts.PARAMETER_ENCRYPTION_KEY).getBytes();
        return RequestBody.create(MEDIA_TYPE, bytes);
    }
}

class DecodeResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final TypeAdapter<T> adapter;

    DecodeResponseBodyConverter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {

        byte[] bytes = value.bytes();
        //先解密 在解压
        try {
            //bytes = GzipUtil.decompress(new TripleDES(Contacts.BODY_ENCRYPTION_KEY).decryptionByteData(bytes));
            bytes = GzipUtil.decompress(TripleDES.tDesDecryptCBC(new String(bytes,"UTF-8"), Contacts.BODY_ENCRYPTION_KEY).getBytes());
        } catch (Exception e) {
            e.printStackTrace();

        }
        Log.d("ResponseBodyConverter", new String(bytes, "UTF-8"));

        //解密字符串
        return bytes == null ? null : adapter.fromJson(new String(bytes, "UTF-8"));
    }
}


