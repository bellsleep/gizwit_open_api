package web;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    public void test() {
        final Logger mLogger = LoggerFactory.getLogger(Main.class);
        
    }
    public static void main(String[] args) {
        final Logger mLogger = LoggerFactory.getLogger(Main.class);

        String str_val_productKey;
        String str_val_appid;
        String str_val_usertoken;
        String str_val_productSecret;
        String str_val_mac;
        String str_val_did;

        UserInfo userinfo = new UserInfo("string", "String123456");
        DeviceInfo devinfo = new DeviceInfo(str_val_mac, str_val_did);

        mLogger.debug("start...");
        GiziwitOpenApi giziw = new GiziwitOpenApi(str_val_productKey, str_val_appid, str_val_productSecret);
        giziw.login(userinfo);
        giziw.bind_mac(devinfo);

        Calendar start, end;
        start=Calendar.getInstance();
        start.set(2017, 12 - 1, 27, 0, 0, 0);
        end = Calendar.getInstance();
        
        end.set(2017, 12 - 1, 28, 0, 0, 0);
        //Long time = 1514379446000L;
        //end.setTimeInMillis(time);

        mLogger.debug("start:{},end:{}", start.getTime(), end.getTime());
        String devlog = giziw.getDevLog(devinfo, "cmd", start, end);
        DevLogData devlogdata = new DevLogData();
        devlogdata.parseJsonFromDevLog(devlog);

    }

}
