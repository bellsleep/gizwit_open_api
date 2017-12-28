package web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeviceInfo {
    private static final Logger mLogger = LoggerFactory.getLogger(DeviceInfo.class);
    
    String mac;
    String did;
    
    public DeviceInfo(String mac, String did) {
        // TODO Auto-generated constructor stub
        this.mac = mac;
        this.did = did;
    }
}
