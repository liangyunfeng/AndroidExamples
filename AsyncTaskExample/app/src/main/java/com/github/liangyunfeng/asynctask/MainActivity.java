package com.github.liangyunfeng.asynctask;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *  1.HttpClient已经过时并且从Android上移除
 *
 *  2.ProgressDialog已经过时，可以使用ProgressBar替代，
 *  但是弹出进度条对话框还是ProgressDialog好用
 */
public class MainActivity extends AppCompatActivity {

    private Button btn1, btn2, btn3, btn4;
    private ImageView imageView;
    private ProgressDialog progressDialog1;
    private ProgressDialog progressDialog2;
    private final String IMAGE_PATH = "http://i.imgur.com/DvpvklR.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(null);

                /**
                 * <String, Integer, byte[]>
                 * No scale progress
                 *
                 * 在UI Thread当中实例化AsyncTask对象，并调用execute方法
                 */
                new MyAsyncTask1().execute(IMAGE_PATH);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(null);

                /**
                 * <String, Integer, byte[]>
                 * With scale progress
                 *
                 * 在UI Thread当中实例化AsyncTask对象，并调用execute方法
                 */
                new MyAsyncTask2().execute(IMAGE_PATH);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(null);

                /**
                 * <String, Void, Bitmap>
                 * No scale progress
                 *
                 * 在UI Thread当中实例化AsyncTask对象，并调用execute方法
                 */
                new MyAsyncTask3().execute(IMAGE_PATH);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(null);

                /**
                 * <String, Integer, Bitmap>
                 * With scale progress
                 *
                 * 在UI Thread当中实例化AsyncTask对象，并调用execute方法
                 */
                new MyAsyncTask4().execute(IMAGE_PATH);
            }
        });

    }

    /**
     * <String, Integer, byte[]>
     * No scale progress
     */
    class MyAsyncTask1 extends AsyncTask<String, Integer, byte[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 在onPreExecute()中我们让ProgressDialog显示出来
            progressDialog1.show();
        }

        @Override
        protected byte[] doInBackground(String... params) {
            byte[] image = new byte[]{};
            if (params != null) {
                HttpURLConnection connection = null;
                try {
                    // 1. 得到访问地址的URL
                    URL url = new URL(params[0]);
                    // 2. 得到网络访问对象java.net.HttpURLConnection
                    connection = (HttpURLConnection) url.openConnection();
                    /* 3. 设置请求参数（过期时间，输入、输出流、访问方式），以流的形式进行连接 */
                    // 设置是否向HttpURLConnection输出
                    connection.setDoOutput(false);
                    // 设置是否从httpUrlConnection读入
                    connection.setDoInput(true);
                    // 设置请求方式
                    connection.setRequestMethod("GET");
                    // 设置是否使用缓存
                    connection.setUseCaches(true);
                    // 设置此 HttpURLConnection 实例是否应该自动执行 HTTP 重定向
                    //connection.setInstanceFollowRedirects(true);
                    // 设置连接超时时间
                    connection.setConnectTimeout(3000);
                    // 设置读取超时时间
                    //connection.setReadTimeout(3000);

                    // 连接
                    connection.connect();

                    // 4. 得到响应状态码的返回值 responseCode
                    int code = connection.getResponseCode();

                    // 5. 如果返回值正常，数据在网络中是以流的形式得到服务端返回的数据
                    if (code == 200) { // 正常响应
                        // 得到文件的总长度
                        long file_length = connection.getContentLength();
                        // 每次读取后累加的长度
                        long total_length = 0;
                        // 从流中读取响应信息
                        InputStream is = connection.getInputStream();
                        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                        try {
                            // 每次最多读取1024个字节
                            byte[] buff = new byte[1024];
                            int len;
                            while ((len = is.read(buff)) != -1) {
                                arrayOutputStream.write(buff, 0, len);
                            }
                            image = arrayOutputStream.toByteArray();
                        } finally {
                            is.close();
                            arrayOutputStream.close();
                        }

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        // 6. 断开连接，释放资源
                        connection.disconnect();
                    }
                }
            }
            return image;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(byte[] result) {
            super.onPostExecute(result);
            // 将doInBackground方法返回的byte[]解码成要给Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);
            // 更新我们的ImageView控件
            imageView.setImageBitmap(bitmap);
            // 使ProgressDialog框消失
            progressDialog1.dismiss();
        }
    }

    /**
     * <String, Integer, byte[]>
     * With scale progress
     */
    class MyAsyncTask2 extends AsyncTask<String, Integer, byte[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 在onPreExecute()中我们让ProgressDialog显示出来
            progressDialog2.show();
            progressDialog2.setProgress(0);
        }

        @Override
        protected byte[] doInBackground(String... params) {
            byte[] image = new byte[]{};
            if (params != null) {
                HttpURLConnection connection = null;
                try {
                    // 1. 得到访问地址的URL
                    URL url = new URL(params[0]);
                    // 2. 得到网络访问对象java.net.HttpURLConnection
                    connection = (HttpURLConnection) url.openConnection();
                    /* 3. 设置请求参数（过期时间，输入、输出流、访问方式），以流的形式进行连接 */
                    // 设置是否向HttpURLConnection输出
                    connection.setDoOutput(false);
                    // 设置是否从httpUrlConnection读入
                    connection.setDoInput(true);
                    // 设置请求方式
                    connection.setRequestMethod("GET");
                    // 设置是否使用缓存
                    connection.setUseCaches(true);
                    // 设置此 HttpURLConnection 实例是否应该自动执行 HTTP 重定向
                    //connection.setInstanceFollowRedirects(true);
                    // 设置连接超时时间
                    connection.setConnectTimeout(3000);
                    // 设置读取超时时间
                    //connection.setReadTimeout(3000);

                    // 连接
                    connection.connect();

                    // 4. 得到响应状态码的返回值 responseCode
                    int code = connection.getResponseCode();

                    // 5. 如果返回值正常，数据在网络中是以流的形式得到服务端返回的数据
                    if (code == 200) { // 正常响应
                        // 得到文件的总长度
                        long file_length = connection.getContentLength();
                        // 每次读取后累加的长度
                        long total_length = 0;
                        // 从流中读取响应信息
                        InputStream is = connection.getInputStream();
                        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                        try {
                            // 每次最多读取1024个字节
                            byte[] buff = new byte[1024];
                            int len;
                            while ((len = is.read(buff)) != -1) {
                                arrayOutputStream.write(buff, 0, len);
                                total_length += len;
                                // 得到当前图片下载的进度
                                int progress = ((int) ((total_length / (float) file_length) * 100));
                                // 时刻将当前进度更新给onProgressUpdate方法
                                publishProgress(progress);
                            }
                            image = arrayOutputStream.toByteArray();
                        } finally {
                            is.close();
                            arrayOutputStream.close();
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        // 6. 断开连接，释放资源
                        connection.disconnect();
                    }
                }
            }
            return image;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            // 更新ProgressDialog的进度条
            progressDialog2.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(byte[] result) {
            super.onPostExecute(result);
            // 将doInBackground方法返回的byte[]解码成要给Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);
            // 更新我们的ImageView控件
            imageView.setImageBitmap(bitmap);
            // 使ProgressDialog框消失
            progressDialog2.dismiss();
        }
    }

    /**
     * <String, Void, Bitmap>
     * No scale progress
     */
    class MyAsyncTask3 extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog1.show();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            HttpURLConnection connection = null;
            InputStream is = null;
            if (params != null) {
                try {
                    URL url = new URL(params[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setDoOutput(false);
                    connection.setRequestMethod("GET");
                    connection.setUseCaches(true);
                    connection.setConnectTimeout(3000);

                    connection.connect();

                    int code = connection.getResponseCode();
                    if (code == 200) {
                        is = connection.getInputStream();
                        bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null)
                        connection.disconnect();
                }
            }
            return bitmap;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
            progressDialog1.dismiss();
        }
    }

    /**
     * <String, Integer, Bitmap>
     * With scale progress
     */
    class MyAsyncTask4 extends AsyncTask<String, Integer, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog2.show();
            progressDialog2.setProgress(0);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            HttpURLConnection connection = null;
            InputStream is = null;
            ByteArrayOutputStream byteArrayOutputStream = null;
            if (params != null) {
                try {
                    URL url = new URL(params[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setDoOutput(false);
                    connection.setRequestMethod("GET");
                    connection.setUseCaches(true);
                    connection.setConnectTimeout(3000);

                    connection.connect();

                    int code = connection.getResponseCode();
                    if (code == 200) {
                        int total = connection.getContentLength();
                        int curLen = 0;
                        int len;
                        is = connection.getInputStream();
                        byte[] buff = new byte[1024];
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        while ((len = is.read(buff)) != -1) {
                            curLen += len;
                            byteArrayOutputStream.write(buff, 0, len);
                            int progress = (int)((curLen / (float)total) * 100);
                            publishProgress(progress);
                        }
                        byte[] image = byteArrayOutputStream.toByteArray();
                        bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null)
                        connection.disconnect();
                }
            }
            return bitmap;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog2.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            progressDialog2.dismiss();
            imageView.setImageBitmap(bitmap);
        }
    }

    public byte[] getBytesInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try {
            byte[] buff = new byte[512];
            int len;
            while ((len = is.read(buff)) != -1) {
                arrayOutputStream.write(buff, 0, len);
            }
        } finally {
            is.close();
            arrayOutputStream.close();
        }
        return arrayOutputStream.toByteArray();
    }

    public void initView() {
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        imageView = (ImageView) findViewById(R.id.imageView);

        // 弹出要给ProgressDialog
        progressDialog1 = new ProgressDialog(MainActivity.this);
        progressDialog1.setTitle("提示信息");
        progressDialog1.setMessage("正在下载中，请稍后......");
        // 设置setCancelable(false); 表示我们不能取消这个弹出框，等下载完成之后再让弹出框消失
        progressDialog1.setCancelable(false);
        // 设置ProgressDialog样式为圆圈的形式
        progressDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // 弹出要给ProgressDialog
        progressDialog2 = new ProgressDialog(MainActivity.this);
        progressDialog2.setTitle("提示信息");
        progressDialog2.setMessage("正在下载中，请稍后......");
        // 设置setCancelable(false); 表示我们不能取消这个弹出框，等下载完成之后再让弹出框消失
        progressDialog2.setCancelable(false);
        // 设置ProgressDialog样式为水平的样式
        progressDialog2.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }
}
