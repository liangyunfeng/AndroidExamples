package com.github.liangyunfeng.retrofit.example.demo2.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by yunfeng.l on 2018/5/31.
 */

public class GzipUtil {

    private static final String UTF_8 = "UTF-8";

    /**
     * @param data
     * @return
     */
    public static final byte[] compress(String data) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputtStream = new GZIPOutputStream(out);
        try {
            gzipOutputtStream.write(data.getBytes(UTF_8));
        } finally {
            closeQuietly(gzipOutputtStream);
        }
        return out.toByteArray();
    }

    /**
     * @param data
     * @return
     */
    public static final byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputtStream = new GZIPOutputStream(out);
        try {
            gzipOutputtStream.write(data);
        } finally {
            closeQuietly(gzipOutputtStream);
        }
        return out.toByteArray();
    }

    /**
     * @param data
     * @return
     */
    public static final byte[] decompress(byte[] data) throws IOException {
        ByteArrayOutputStream buffer = null;
        GZIPInputStream gizpInputStream = null;
        try {
            buffer = new ByteArrayOutputStream();
            gizpInputStream = new GZIPInputStream(new ByteArrayInputStream(data));
            int n = -1;
            byte[] _buffer = new byte[1024 * 12];
            while (-1 != (n = gizpInputStream.read(_buffer))) {
                buffer.write(_buffer, 0, n);
            }
            return buffer.toByteArray();
        } finally {
            closeQuietly(gizpInputStream);
            closeQuietly(buffer);
        }
    }

    /**
     * @param in
     * @return
     * @throws IOException
     */
    public static final byte[] decompress(InputStream in) throws IOException {
        ByteArrayOutputStream buffer = null;
        GZIPInputStream gizpInputStream = null;
        try {
            buffer = new ByteArrayOutputStream();
            gizpInputStream = new GZIPInputStream(in);
            int n = -1;
            byte[] _buffer = new byte[1024 * 12];
            while (-1 != (n = gizpInputStream.read(_buffer))) {
                buffer.write(_buffer, 0, n);
            }
            return buffer.toByteArray();
        } finally {
            closeQuietly(gizpInputStream);
            closeQuietly(buffer);
        }
    }

    private static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }
}










/*
public class GzipUtil {*/
    /**
     * 字符串进行Gzip压缩
     *
     * @param primStr
     * @return String
     * @Title: gzip
     */
/*    public static String gzip(String primStr) {
        if (primStr == null || primStr.length() == 0) {
            return primStr;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(primStr.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (gzip != null) {
                try {
                    gzip.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);
    }*/

    /**
     * <p>
     * Description:使用gzip进行解压缩
     * </p>
     *
     * @param compressedStr
     * @return
     */
/*    public static String gunzip(String compressedStr) {
        if (compressedStr == null) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = null;
        GZIPInputStream ginzip = null;
        byte[] compressed = null;
        String decompressed = null;
        try {
            compressed = Base64.decode(compressedStr, Base64.DEFAULT);
            in = new ByteArrayInputStream(compressed);
            ginzip = new GZIPInputStream(in);
            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = ginzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed = new String(out.toByteArray(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ginzip != null) {
                try {
                    ginzip.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        return decompressed;
    }
}*/




/*
//2.服务端（JAVA）
//        GzipUtil类：

public class GzipUtil {

    public static final String GZIP_ENCODE_UTF_8 = "UTF-8";

    public static String gzip(String primStr)
    {
        if (primStr == null || primStr.length() == 0)
        {
            return primStr;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        try
        {
            gzip = new GZIPOutputStream(out);
            gzip.write(primStr.getBytes(GZIP_ENCODE_UTF_8));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (gzip != null)
            {
                try
                {
                    gzip.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return  new BASE64Encoder().encode(out.toByteArray());
    }

    public static String gunzip(String compressedStr)
    {
        if (compressedStr == null)
        {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = null;
        GZIPInputStream ginzip = null;
        byte[] compressed = null;
        String decompressed = null;
        try
        {
            BASE64Decoder decoder = new BASE64Decoder();
            compressed = decoder.decodeBuffer(compressedStr);
            in = new ByteArrayInputStream(compressed);
            ginzip = new GZIPInputStream(in);
            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = ginzip.read(buffer)) != -1)
            {
                out.write(buffer, 0, offset);
            }
            decompressed = new String(out.toByteArray(), GZIP_ENCODE_UTF_8);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (ginzip != null)
            {
                try
                {
                    ginzip.close();
                }
                catch (IOException e)
                {
                }
            }
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                }
            }
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e)
                {
                }
            }
        }
        return decompressed;
    }
}
*/