package web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequest {
    private static final Logger mLogger = LoggerFactory.getLogger(HttpRequest.class);

    final static String str_appid = "X-Gizwits-Application-Id";
    final static String str_usertoken = "X-Gizwits-User-token";
    final static String str_timestamp = "X-Gizwits-Timestamp";
    final static String str_signature = "X-Gizwits-Signature";
    
    String str_val_appid;
    String str_val_usertoken;
    
    /**
     * http request
     * @param appid appid
     * @param token user token
     */
    public HttpRequest(String appid, String token) {
        // TODO Auto-generated constructor stub
        if (appid != null)
            str_val_appid = appid;
        if (token != null)
            str_val_usertoken = token;
    }

    public HttpRequest(String appid) {
        if (appid != null)
            str_val_appid = appid;
    }
    
    public void setUserToken(String userToken) {
        str_val_usertoken = userToken;
    }
    //String proxyHost = "127.0.0.1";
    //int proxyPort = 9999;
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public String sendGet(String url, String param, boolean send_appid, boolean send_usertoken) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString;
            if (param == null)
                urlNameString = url;
            else
                urlNameString = url + "?" + param;
            
            //InetSocketAddress netSocketAddress = new InetSocketAddress(proxyHost, proxyPort);
            //Proxy proxy = new Proxy(Proxy.Type.SOCKS, netSocketAddress);
            mLogger.debug("sendGet " + urlNameString);
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            //URLConnection connection = realUrl.openConnection(proxy);
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "application/json");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            if (send_appid)
                connection.setRequestProperty(str_appid, str_val_appid);
            if (send_usertoken)
                connection.setRequestProperty(str_usertoken, str_val_usertoken);
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public String sendPost(String url, String param, boolean send_appid, boolean send_usertoken) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            if (send_appid)
                conn.setRequestProperty(str_appid, str_val_appid);
            if (send_usertoken)
                conn.setRequestProperty(str_usertoken, str_val_usertoken);
            //conn.setRequestProperty("", arg1);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
    
    /**
     * send post
     * @param url String url
     * @param param String body
     * @param send_appid true:send appid, false:not send
     * @param send_usertoken true:send usertoken, false:not send
     * @param maphead Map<String, String> other head info to be send
     * @return
     */
    public String sendPost(String url, String param, boolean send_appid, boolean send_usertoken, Map<String, String> maphead) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            if (send_appid)
                conn.setRequestProperty(str_appid, str_val_appid);
            if (send_usertoken)
                conn.setRequestProperty(str_usertoken, str_val_usertoken);
            
            if (maphead != null) {
                for (Map.Entry<String, String> entry : maphead.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            //conn.setRequestProperty("", arg1);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
}