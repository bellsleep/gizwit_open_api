package web;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flexjson.JSONDeserializer;

public class DevLog {

    private static final Logger mLogger = LoggerFactory.getLogger(DevLog.class);
    /*"{"meta": 
    {"sort": "desc",
        "limit": 20,
        "end_time": 1514379508,
        "did": "xxx",
        "skip": 0,
        "start_time": 1514217600,
        "total": 2,
        "type": "cmd"},
   "objects":
     [{"ip": "139.227.220.135",
       "payload_bin": "000000030700009114020018",
       "type": "dev2app",
       "timestamp": 1514379445.407},
      {"ip": "139.227.220.135",
       "payload_bin": "000000030700009114020019",
       "type": "dev2app",
       "timestamp": 1514379435.139}
     ]}"  */

    public class DevData {
        String ip;
        String payload_bin;
        String type;    // dirtype
        Long timestamp;
    }

    String did;
    String dataType;

    String str= "{\"meta\": {\"sort\": \"desc\", \"limit\": 20, \"end_time\": 1514379508, \"did\": \"xxxx\", \"skip\": 0, \"start_time\": 1514217600, \"total\": 2, \"type\": \"cmd\"}, \"objects\": [{\"ip\": \"139.227.220.135\", \"payload_bin\": \"000000030700009114020018\", \"type\": \"dev2app\", \"timestamp\": 1514379445.407}, {\"ip\": \"139.227.220.135\", \"payload_bin\": \"000000030700009114020019\", \"type\": \"dev2app\", \"timestamp\": 1514379435.139}]}";
    public void parseJsonFromDevLog(String strJson) {
        strJson = str;
        if (strJson == null) {
            mLogger.info("nothing to do!");
            return;
        }
        mLogger.debug("parseFrom:" + strJson);
        Map<String, Object> mapJson;
        mapJson = (Map<String, Object>) new JSONDeserializer<Map<String, Object>>().deserialize(strJson);
        mLogger.debug("mapJson=" + mapJson);

       // did = (String)mapJson.get("meta")
    }
}
