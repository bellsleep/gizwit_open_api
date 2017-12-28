package web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class UserInfo {
    String username;
    String password;
    String uid;
    String token;
    Calendar expire_at;
    
    private static final Logger mLogger = LoggerFactory.getLogger(UserInfo.class);
    
    public UserInfo(String name, String pwd) {
        username = name;
        password = pwd;
    }

    public final String getusername() {
        return username;
    }
    public final String getpassword() {
        return password;
    }

    public String toJson(UserInfo ui) {
        String str = new JSONSerializer().include("username", "password").exclude("*.class").serialize(this);
        str = new JSONSerializer().exclude("*.class").serialize(this);
        //mLogger.info(str);
        return str;
    }
    public void parseJsonFromLogin(String strJson) {
        if (strJson == null) {
            mLogger.info("nothing to do!");
            return;
        }
        mLogger.debug("parseFrom:" + strJson);
        Map<String, Object> mapJson;
        mapJson = (Map<String, Object>) new JSONDeserializer<Map<String, Object>>().deserialize(strJson);
        mLogger.debug("mapJson=" + mapJson);
        token = (String)mapJson.get("token");
        uid = (String)mapJson.get("uid");
        expire_at = Calendar.getInstance();
        Integer time = (Integer)mapJson.get("expire_at");
        //mLogger.debug("time=" + time);
        Long timelong = (long)time * 1000;
        //mLogger.debug("timelong=" + timelong);
        expire_at.setTimeInMillis(timelong);
        mLogger.debug("token={},uid={},expire_at={}", token, uid, expire_at.getTime());
    }
}
