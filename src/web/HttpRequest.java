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
     * ��ָ��URL����GET����������
     * 
     * @param url
     *            ���������URL
     * @param param
     *            ����������������Ӧ���� name1=value1&name2=value2 ����ʽ��
     * @return URL ������Զ����Դ����Ӧ���
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
            // �򿪺�URL֮�������
            //URLConnection connection = realUrl.openConnection(proxy);
            URLConnection connection = realUrl.openConnection();
            // ����ͨ�õ���������
            connection.setRequestProperty("accept", "application/json");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            if (send_appid)
                connection.setRequestProperty(str_appid, str_val_appid);
            if (send_usertoken)
                connection.setRequestProperty(str_usertoken, str_val_usertoken);
            // ����ʵ�ʵ�����
            connection.connect();
            // ��ȡ������Ӧͷ�ֶ�
            Map<String, List<String>> map = connection.getHeaderFields();
            // �������е���Ӧͷ�ֶ�
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // ���� BufferedReader����������ȡURL����Ӧ
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("����GET��������쳣��" + e);
            e.printStackTrace();
        }
        // ʹ��finally�����ر�������
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
     * ��ָ�� URL ����POST����������
     * 
     * @param url
     *            ��������� URL
     * @param param
     *            ����������������Ӧ���� name1=value1&name2=value2 ����ʽ��
     * @return ������Զ����Դ����Ӧ���
     */
    public String sendPost(String url, String param, boolean send_appid, boolean send_usertoken) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // �򿪺�URL֮�������
            URLConnection conn = realUrl.openConnection();
            // ����ͨ�õ���������
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
            // ����POST�������������������
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // ��ȡURLConnection�����Ӧ�������
            out = new PrintWriter(conn.getOutputStream());
            // �����������
            out.print(param);
            // flush������Ļ���
            out.flush();
            // ����BufferedReader����������ȡURL����Ӧ
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("���� POST ��������쳣��"+e);
            e.printStackTrace();
        }
        //ʹ��finally�����ر��������������
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
            // �򿪺�URL֮�������
            URLConnection conn = realUrl.openConnection();
            // ����ͨ�õ���������
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
            // ����POST�������������������
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // ��ȡURLConnection�����Ӧ�������
            out = new PrintWriter(conn.getOutputStream());
            // �����������
            out.print(param);
            // flush������Ļ���
            out.flush();
            // ����BufferedReader����������ȡURL����Ӧ
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("���� POST ��������쳣��"+e);
            e.printStackTrace();
        }
        //ʹ��finally�����ر��������������
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