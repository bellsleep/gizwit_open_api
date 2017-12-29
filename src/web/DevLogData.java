package web;

import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flexjson.JSONDeserializer;


public class DevLogData {

    private static final Logger mLogger = LoggerFactory.getLogger(DevLogData.class);
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

        @Override
        public String toString() {
            // TODO Auto-generated method stub
            return "[timestamp=" + timestamp + ",ip=" + ip + ",type=" + type + ",payload_bin=" + payload_bin + "]";
        }
    }

    String did;
    String dataType;
    ArrayList<DevData> datalist;
    @Override
    public String toString() {
        String tostring = "{did=" + did;
        tostring += ",dataType=" + dataType;
        tostring += ",";
        for (DevData dev : datalist) {
            tostring += dev.toString();
        }
        tostring += "}";
        return tostring;
    };


    public void parseJsonFromDevLog(String strJson) {
        if (strJson == null) {
            mLogger.info("parseJsonFromDevLog nothing to do!");
            return;
        }

        mLogger.debug("parseFrom:{}\n",  strJson);

        try {
            Map<String, Object> mapJson;

            mapJson = (Map<String, Object>) new JSONDeserializer<Map<String, Object>>().deserialize(strJson);
            mLogger.trace("mapJson=" + mapJson);

            Map<String, Object> meta = (Map<String, Object>) mapJson.get("meta");
            mLogger.trace("meta=" + meta);
            if (meta == null) {
                mLogger.warn("no meta data\n");
                return;
            }

            this.did = (String)meta.get("did");
            this.dataType = (String)meta.get("type");
            ArrayList<?> objects = (ArrayList<?>) mapJson.get("objects");
            mLogger.trace("objects=" + objects);
           // did = (String)mapJson.get("meta")
            this.datalist = new ArrayList<DevData>();
            for (Object mem : objects) {
                Map<String, Object> para = (Map<String, Object>)mem;
                DevData devdata = new DevData();
                devdata.ip = (String)para.get("ip");
                devdata.payload_bin = (String)para.get("payload_bin");
                devdata.type = (String)para.get("type");
                Double time = (Double)para.get("timestamp");
                devdata.timestamp = (new Double(time * 1000)).longValue();
                this.datalist.add(devdata);
            }
            mLogger.debug("devlog=" + this);
        } catch (Exception e) {
            // TODO: handle exception
            mLogger.error("parseJsonFromDevLog '{}' error", strJson);
            e.printStackTrace();
        }
        
    }
}
