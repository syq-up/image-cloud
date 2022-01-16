package com.shiyq.cloudsystem.util;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;

/**
 * 媒体类型工具类
 */
public class MimeTypeUtil {
    public static final String IMAGE_PNG = "image/png";

    public static final String IMAGE_JPG = "image/jpg";

    public static final String IMAGE_JPEG = "image/jpeg";

    public static final String IMAGE_BMP = "image/bmp";

    public static final String IMAGE_GIF = "image/gif";

    public static String getExtension(String prefix) {
        switch (prefix) {
            case IMAGE_PNG:
                return "png";
            case IMAGE_JPG:
                return "jpg";
            case IMAGE_JPEG:
                return "jpeg";
            case IMAGE_BMP:
                return "bmp";
            case IMAGE_GIF:
                return "gif";
            default:
                return "";
        }
    }

    /**
     * 缓存文件头信息-文件头信息
     */
    public static final HashMap<String, String> mimeTypes = new HashMap<String, String>();

    static {
        // images
        mimeTypes.put("FFD8FF", "jpg");
        mimeTypes.put("89504E47", "png");
        mimeTypes.put("47494638", "gif");
        mimeTypes.put("49492A00", "tif");
        mimeTypes.put("424D", "bmp");
    }

    /**
     * <p>Title:getFileTypeByFileInputStream </p>
     * <p>Description: 根据文件流获取文件头信息</p>
     *
     * @param is 文件流
     * @return 文件头信息
     */
    public static String getFileTypeByFileInputStream(InputStream is) {
        return mimeTypes.get(getFileHeaderByFileInputStream(is));
    }

    /**
     * <p>Title:getFileHeader </p>
     * <p>Description: 根据网络文件路径获取文件类型信息 </p>
     */
    public static String getFileTypeByNetPath(String netFilePath) throws Exception {
        trustAllHosts();
        URLConnection url = new URL(netFilePath).openConnection();
        url.connect();
        return getFileTypeByFileInputStream(url.getInputStream());
    }

    /**
     * <p>Title:getFileHeaderByFileInputStream </p>
     * <p>Description: 根据文件流获取文件头信息</p>
     *
     * @param is 文件流
     * @return 十六进制文件头信息
     */
    private static String getFileHeaderByFileInputStream(InputStream is) {
        String value = null;
        try {
            byte[] b = new byte[4];
            /*
             * int read() 从此输入流中读取一个数据字节。int read(byte[] b) 从此输入流中将最多 b.length
             * 个字节的数据读入一个 byte 数组中。 int read(byte[] b, int off, int len)
             * 从此输入流中将最多 len 个字节的数据读入一个 byte 数组中。
             */
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {

        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {

                }
            }
        }
        return value;
    }

    /**
     * <p>Title:bytesToHexString </p>
     * <p>Description: 将要读取文件头信息的文件的byte数组转换成string类型表示 </p>
     *
     * @param src 要读取文件头信息的文件的byte数组
     * @return 文件头信息
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (byte b : src) {
            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
            hv = Integer.toHexString(b & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }

    /**
     * 解决网络证书问题
     * 如下异常： unable to find valid certification path to requested target
     */
    public static void trustAllHosts() throws Exception {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }
        }
        };

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
