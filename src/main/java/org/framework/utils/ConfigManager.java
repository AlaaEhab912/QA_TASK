package org.framework.utils;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
    private static ConfigManager manager;

    //select environment to be used(NewPreProdEnv/PreProdEnv/TestingEnv/StagingEnv/GeneralEnv/WebGateEnv/ProductionCloneEnv/WebLogicEnv/TPLWebLogicEnv/LoadBalancerEnv/PreProd31Env/CitrixEnv)
    //static String env = "PreProdEnv";

    public static String getDefaultRealm()
    {
        return defaultRealm;
    }

    static String defaultRealm = "reqres";
    static String realmId;

    public static String getRealmId() {
        return realmId;
    }

    public static void setRealmId(String realmId) {
        ConfigManager.realmId = realmId;
    }

    private static final Properties properties = new Properties();

    public ConfigManager() throws IOException {
        properties.load(new FileInputStream("resources/"  + "/config-" + realmId + ".properties"));
    }

    public static ConfigManager getInstance() {
        if (manager == null) {
            synchronized (ConfigManager.class) {
                try {
                    manager = new ConfigManager();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return manager;
    }

    public String getString(String key) {
        return System.getProperty(key, properties.getProperty(key));
    }


}
