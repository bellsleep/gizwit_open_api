package web;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flexjson.JSONDeserializer;

public class GiziwitOpenApi {
    private static final Logger mLogger = LoggerFactory.getLogger(GiziwitOpenApi.class);

    String str_val_productKey;
    String str_val_appid;
    String str_val_usertoken;
    String str_val_productSecret;
    String str_val_mac;
    String str_val_did;

    UserInfo userinfo;
    DeviceInfo devinfo;
    HttpRequest gizi;

    public GiziwitOpenApi(String productKey, String appid, String productSecret) {
        // TODO Auto-generated constructor stub
        str_val_productKey = productKey;
        str_val_appid = appid;
        str_val_productSecret = productSecret;
        gizi = new HttpRequest(str_val_appid);
    }

    /**
     * get user token,you shoud login first.
     * @return String user token, or null;
     */
    public String getUserToken() {
        if (userinfo == null)
            return null;
        return userinfo.token;
    }

    public boolean setUserToken(String userToken) {
        if (userinfo == null)
            return false;
        userinfo.token = userToken;
        gizi.setUserToken(userToken);
        return true;
    }

    public boolean setUserInfo(UserInfo ui) {
        if (ui == null || ui.username == null || ui.password == null) {
            return false;
        }
        userinfo = ui;
        gizi.setUserToken(userinfo.token);
        return true;
    }

    public UserInfo getUserInfo() {
        return userinfo;
    }

    public boolean setDeviceInfo(DeviceInfo di) {
        if (di == null || di.mac == null || di.mac == null) {
            return false;
        }
        devinfo = di;
        return true;
    }

    public String login(UserInfo ui) {
        String result;
        if (ui != null && ui.username != null && ui.password != null) {
            mLogger.debug("update userinfo");
            userinfo = ui;
        }
        mLogger.debug("login with userinfo name:{} password:{}", userinfo.username, userinfo.password);

        //String para = "username=" + userinfo.username + "&password=" + userinfo.password;
        String para ; //= "{\"username\": \"" + userinfo.username + "\",\"password\": \"" + userinfo.password + "\"}";
        para = userinfo.toJson(userinfo);
        //mLogger.debug("post para=" + para);
        result = gizi.sendPost("http://api.gizwits.com/app/login", para, true, false);

        mLogger.trace("login result=" + result);
        userinfo.parseJsonFromLogin(result);

        if (userinfo.token != null)
            gizi.setUserToken(userinfo.token);
        return result;
    }

    public String bind_mac(DeviceInfo di) {
        if (di != null && di.mac != null) {
            devinfo = di;
            mLogger.debug("update di info!");
        }
        mLogger.debug("bind_mac mac=" + devinfo.mac);
        str_val_mac = devinfo.mac;

        Map<String, String> mapHead = new HashMap<String, String>();
        String str_timestamp = Long.toString(Calendar.getInstance().getTimeInMillis()/1000);
        String str = (str_val_productSecret + str_timestamp).toLowerCase();
        String md5 = Utils.getMD5(str).toLowerCase();
        mapHead.put(HttpRequest.str_timestamp, str_timestamp);
        mapHead.put(HttpRequest.str_signature, md5);

        // body is json str
        String str_body = "{\"product_key\":\"" + str_val_productKey;
        str_body += "\",\"mac\":\"" + devinfo.mac + "\"}";

        String result = gizi.sendPost("http://api.gizwits.com/app/bind_mac", str_body, true, true, mapHead);
        mLogger.trace("bind_mac result=" + result);
        /*
         * {"remark": "", "ws_port": 8080, "did": "xxxx",
         * "port_s": 8883, "is_disabled": false, "host": "sandbox.gizwits.com",
         * "product_key": "xxxx", "wss_port": 8880,
         * "mac": "xxxx", "role": "special", "dev_alias": "",
         * "is_online": false, "passcode": "xxxx", "dev_label": [],
         * "type": "normal", "port": 1883}
         */
        return result;
    }

    public String getDevLog(DeviceInfo devinfo, String type, Calendar start, Calendar end) {
        String result;
        if (!"cmd".equalsIgnoreCase(type) && !"online".equalsIgnoreCase(type)) {
            mLogger.warn("getLog type must be cmd or online, but real:" + type);
            return null;
        }
        if ((start == null && end == null) || (start != null && end != null)) {
        } else {
            mLogger.warn("time is invalid! start={},end={}", start, end);
        }
        if (str_val_did == null && (devinfo == null || devinfo.did == null)) {
            mLogger.error("getDevLog did is null!");
            return null;
        }
        if (devinfo != null && devinfo.did != null)
            str_val_did = devinfo.did;

        mLogger.debug("getLog with type:{} time:{}-{}", type, start.getTime(), end.getTime());

        //https://api.gizwits.com/app/devices/{did}/raw_data?type={type}&start_time={start_time}&end_time={end_time}
        String url="https://api.gizwits.com/app/devices/" + str_val_did + "/raw_data";
        String para = "type=" + type.toLowerCase() + "&start_time=" + start.getTimeInMillis()/1000 + "&end_time=" + end.getTimeInMillis()/1000;
        mLogger.trace("send get=>" + url + para);
        result = gizi.sendGet(url, para, true, true);
        mLogger.debug("getDevLog result=" + result);
        if (result.charAt(0) == '"' && result.charAt(result.length() - 1) == '"') {
            result = result.substring(1, result.length() - 1);
            mLogger.debug("remove head and end \" result = '{}'", result);
        }
        result = result.replaceAll("\\\\\"", "\"");
        mLogger.debug("new result = '{}'", result);

        
        // "{\"meta\": {\"sort\": \"desc\", \"limit\": 20, \"end_time\": 1514379508, \"did\": \"xxxx\", \"skip\": 0, \"start_time\": 1514217600, \"total\": 2, \"type\": \"cmd\"}, \"objects\": [{\"ip\": \"139.227.220.135\", \"payload_bin\": \"000000030700009114020018\", \"type\": \"dev2app\", \"timestamp\": 1514379445.407}, {\"ip\": \"139.227.220.135\", \"payload_bin\": \"000000030700009114020019\", \"type\": \"dev2app\", \"timestamp\": 1514379435.139}]}"
        //parseJsonFromDevLog(result);
        return result;
    }


}
