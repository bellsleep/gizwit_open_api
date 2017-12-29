package test;

import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flexjson.JSONDeserializer;
import web.DevLogData;
import web.GiziwitOpenApi;
import web.UserInfo;

public class test {
    private static final Logger mLogger = LoggerFactory.getLogger(test.class);

    
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        /*UserInfo userinfo = new UserInfo("string", "String123456");
        String para = userinfo.toJson(userinfo);
        
        String json = "{\"token\": \"xxxx\", \"uid\": \"xxxx\", \"expire_at\": 1514864356}";
        userinfo.parseJsonFromLogin(json);*/
        //test instance = new test();
        String str = "\"{\\\"meta\\\": {\\\"sort\\\": \\\"desc\\\", \\\"limit\\\": 20, \\\"end_time\\\": 1514379446, \\\"did\\\": \\\"xx\\\", \\\"skip\\\": 0, \\\"start_time\\\": 1514304000, \\\"total\\\": 2, \\\"type\\\": \\\"cmd\\\"}, \\\"objects\\\": [{\\\"ip\\\": \\\"139.227.220.135\\\", \\\"payload_bin\\\": \\\"000000030700009114020018\\\", \\\"type\\\": \\\"dev2app\\\", \\\"timestamp\\\": 1514379445.407}, {\\\"ip\\\": \\\"139.227.220.135\\\", \\\"payload_bin\\\": \\\"000000030700009114020019\\\", \\\"type\\\": \\\"dev2app\\\", \\\"timestamp\\\": 1514379435.139}]}\"";
        if (str.charAt(0) == '"' && str.charAt(str.length() - 1) == '"') {
            str = str.substring(1, str.length() - 1);
            str = str.replaceAll("\\\\\"", "\"");
            mLogger.debug("new result = '{}'", str);
        }

        DevLogData devlog = new DevLogData();
        devlog.parseJsonFromDevLog(str);
    }

}
