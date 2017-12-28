package test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import web.GiziwitOpenApi;
import web.UserInfo;

public class test {
    private static final Logger mLogger = LoggerFactory.getLogger(test.class);

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        UserInfo userinfo = new UserInfo("string", "String123456");
        String para = userinfo.toJson(userinfo);
        
        String json = "{\"token\": \"0ba91df58cef42e28d8cdca41e8c3a2c\", \"uid\": \"a4236dde3cf64edebf2d4da4e394fb05\", \"expire_at\": 1514864356}";
        userinfo.parseJsonFromLogin(json);
    }

}
