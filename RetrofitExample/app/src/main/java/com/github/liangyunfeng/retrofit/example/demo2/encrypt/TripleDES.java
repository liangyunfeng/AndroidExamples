package com.github.liangyunfeng.retrofit.example.demo2.encrypt;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * 3-DES加密工具类
 * Created by yunfeng.l on 2018/5/31
 */
public class TripleDES {
    /**
     * 算法名称
     */
    public static final String KEY_ALGORITHM = "desede";
    /**
     * 算法名称/加密模式/填充方式
     *
     String source = "abcdefgh"; // 无填充情况下，长度必须为8的倍数
     String key = "6C4E60E55552386C759569836DC0F83869836DC0F838C0F7";// 长度必须大于等于48
     System.out.println(key.length());
     String encryptData = TripleDES.tDesEncryptCBC(source, key);
     System.out.println("加密后: " + encryptData);

     String decryptData = TripleDES.tDesDecryptCBC(encryptData, key);
     System.out.println("解密后: " + decryptData);
     */
    public static final String CIPHER_ALGORITHM = "desede/CBC/NoPadding";
    /**
     * IvParameterSpec参数
     */
    public static final byte[] keyiv = { 1, 2, 3, 4, 5, 6, 7, 8 };

    /**
     * CBC加密
     * @param data 明文
     * @param key 密钥
     * @return Base64编码的密文
     */
    public static String tDesEncryptCBC(String data, String key){
        try {
            // 添加一个安全提供者
            Security.addProvider(new BouncyCastleProvider());
            // 获得密钥
            Key desKey = keyGenerator(key);
            // 获取密码实例
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec ips = new IvParameterSpec(keyiv);
            // 初始化密码
            cipher.init(Cipher.ENCRYPT_MODE, desKey, ips);
            // 执行加密
            byte[] bytes = cipher.doFinal(data.getBytes("UTF-8"));
            //return Base64..encodeBase64String(bytes);
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (InvalidKeyException e) {
            return "无效KEY"; // 无效KEY
        } catch (NoSuchAlgorithmException e) {
            return "无效算法名称"; // 无效算法名称
        } catch (InvalidKeySpecException e) {
            return "无效KeySpec"; // 无效KeySpec
        } catch (NoSuchPaddingException e) {
            return "无效算法名称"; // 无效算法名称
        } catch (IllegalBlockSizeException e) {
            return "无效字节"; // 无效字节
        } catch (BadPaddingException e) {
            return "解析异常"; // 解析异常
        } catch (UnsupportedEncodingException e) {
            return "编码异常"; // 编码异常
        } catch (InvalidAlgorithmParameterException e) {
            return "摘要参数异常"; // 摘要参数异常
        }
    }

    /**
     * CBC解密
     * @param data Base64编码的密文
     * @param key 密钥
     * @return
     */
    public static String tDesDecryptCBC(String data, String key) {
        try {
            Key desKey = keyGenerator(key);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec ips = new IvParameterSpec(keyiv);
            cipher.init(Cipher.DECRYPT_MODE, desKey, ips);
            //byte[] bytes = cipher.doFinal(Base64.decodeBase64(data));
            byte[] bytes = cipher.doFinal(Base64.decode(data, Base64.DEFAULT));
            return new String(bytes, "UTF-8");
        } catch (InvalidKeyException e) {
            return "无效KEY"; // 无效KEY
        } catch (NoSuchAlgorithmException e) {
            return "无效算法名称"; // 无效算法名称
        } catch (InvalidKeySpecException e) {
            return "无效KeySpec"; // 无效KeySpec
        } catch (NoSuchPaddingException e) {
            return "无效算法名称"; // 无效算法名称
        } catch (IllegalBlockSizeException e) {
            return "无效字节"; // 无效字节
        } catch (BadPaddingException e) {
            return "解析异常"; // 解析异常
        } catch (UnsupportedEncodingException e) {
            return "编码异常"; // 编码异常
        } catch (InvalidAlgorithmParameterException e) {
            return "摘要参数异常"; // 摘要参数异常
        }
    }


    /**
     * 生成密钥key对象
     * @param key 密钥字符串
     * @return 密钥对象
     * @throws InvalidKeyException 无效的key
     * @throws NoSuchAlgorithmException 算法名称未发现
     * @throws InvalidKeySpecException 无效的KeySpec
     */
    private static Key keyGenerator(String key) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] input = hexString2Bytes(key);
        DESedeKeySpec desKey = new DESedeKeySpec(input);
        // 创建一个密钥工厂，然后用它把DESKeySpec转化
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        // 获得一个密钥
        SecretKey secretKey = keyFactory.generateSecret(desKey);
        return secretKey;
    }

    /**
     * 从十六进制字符串到字节数组转化
     * @param key 密钥
     */
    private static byte[] hexString2Bytes(String key) {
        byte[] b = new byte[key.length()/2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = key.charAt(j++);
            char c1 = key.charAt(j++);
            // c0做b[i]的高字节，c1做低字节
            b[i] = (byte) ((parse(c0)<<4)|parse(c1));
        }
        return b;
    }

    /**
     * 将字符转换为int值
     * @param c 要转化的字符
     * @return ASCII码值
     */
    private static int parse(char c) {
        if (c >= 'a') {
            return (c - 'a' + 10) & 0x0f;
        }
        if (c >= 'A') {
            return (c - 'A' + 10) & 0x0f;
        }
        return (c - '0') & 0x0f;
    }

}
