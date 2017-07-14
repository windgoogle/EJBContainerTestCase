package com.tongweb.client;



import javax.management.Attribute;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by huangfeng on 2017/7/6.
 */
public class ConfigAndMonitorClient {
    public final static String ip="168.1.2.6";
    public final static String port="7200";
    public final static String jmxurl="service:jmx:rmi:///jndi/rmi://"+ip+":"+port+"/jmxrmi";
    public final static String CONFIG_EVENT_OBJECTNAME = "config:name=eventtransaction";
    public final static String CONFIG_STATELESS_CONTAINER="config:name=Stateless,parent=/Tongweb/Server/EjbContainer";
    public final static String CONFIG_MDB_CONTAINER="config:name=Mdb,parent=/Tongweb/Server/EjbContainer";

    public static MBeanServerConnection conn;

    static {
        connect();
    }

    public static void connect()  {
        try {
            Map env=new HashMap();
            String[] credentials = new String[] { "twns", "twns123.com" };
            env.put("jmx.remote.credentials", credentials);
            JMXServiceURL url = new JMXServiceURL(jmxurl);
            JMXConnector jmxc = JMXConnectorFactory.connect(url, env);
            conn = jmxc.getMBeanServerConnection();

        }catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public static void setAttribute(String objectName, Attribute attributeName)
            throws Exception {
        setAttribute(new ObjectName(objectName), attributeName);
    }

    public static  void setAttribute(ObjectName name , Attribute attribute) throws  Exception {
        try {
            conn.setAttribute(name, attribute);
        }catch (Exception e) {
            System.out.println("set attribute error ! name :"+attribute.getName() +",value : "+attribute.getValue());
            throw new Exception(e);
        }
    }

    public static Object getAttribute(String objectName, String attributeName)
            throws Exception {
        return getAttribute(new ObjectName(objectName), attributeName);
    }

    public static Object getAttribute(ObjectName objectName,
                                      String attributeName) throws Exception {
        return conn.getAttribute(objectName, attributeName);
    }

    public static Object invoke(String objectName, String operationName)
            throws Exception {
        return invoke(objectName, operationName, new Object[]{},
                new String[]{});
    }

    public static Object invoke(String objectName, String operationName,
                                Object[] params, String[] signature) throws Exception {
        Object result = invoke(new ObjectName(objectName), operationName,
                params, signature);
        return result;
    }

    public static Object invoke(ObjectName objectName, String operationName,
                                 Object[] params, String[] signature) throws Exception {
        Object result = conn.invoke(objectName, operationName,
                params, signature);
        return result;
    }

    public static void begin() throws Exception {
        invoke(CONFIG_EVENT_OBJECTNAME, "begin");
    }

    public static void commit() throws Exception {
        invoke(CONFIG_EVENT_OBJECTNAME, "commit");
    }

    public static void setAttributes(String oName,
                                     Map<String, Object> attributes) throws Exception {
        Iterator<Map.Entry<String, Object>> iter = attributes.entrySet().iterator();
        setAttributes(new ObjectName(oName), attributes);
    }

    public static void setAttributes(ObjectName oName,
                                     Map<String, Object> attributes) throws Exception {
        Iterator<Map.Entry<String, Object>> iter = attributes.entrySet().iterator();
        begin();
        while (iter.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iter
                    .next();
            String key = entry.getKey();
            Object val = entry.getValue();
            Attribute attribute = new Attribute(key, val);
            setAttribute(oName, attribute);
        }
        commit();
    }

    public static void configStatelessContainer()throws Exception {
        Map<String,Object> attributes=new HashMap<String,Object>();
        attributes.put("accessTimeout","30");
        attributes.put("callbackThreads","5");
        attributes.put("closeTimeout","5");
        attributes.put("garbageCollection","true");
        attributes.put("idleTimeout","5");   //分钟
        attributes.put("maxAge","1");    //小时
        attributes.put("maxAgeOffset","0");
        attributes.put("maxSize","10");
        attributes.put("minSize","1");
        attributes.put("replaceAged","true");
        attributes.put("replaceFlushed","false");
        attributes.put("strictPooling","false");
        attributes.put("sweepInterval","5");  //分钟

       setAttributes(CONFIG_STATELESS_CONTAINER,attributes);
       System.out.println("Config statelesss container completed !");
    }

    public static void configMdbContainer()throws Exception {
        Map<String,Object> attributes=new HashMap<String,Object>();
        attributes.put("accessTimeout","30");
        attributes.put("callbackThreads","5");
        attributes.put("closeTimeout","5");
        attributes.put("garbageCollection","true");
        attributes.put("idleTimeout","5");   //分钟
        attributes.put("maxAge","1");    //小时
        attributes.put("maxAgeOffset","0");
        attributes.put("maxSize","10");
        attributes.put("minSize","1");
        attributes.put("replaceAged","true");
        attributes.put("replaceFlushed","false");
        attributes.put("strictPooling","false");
        attributes.put("sweepInterval","5");  //分钟

        setAttributes(CONFIG_MDB_CONTAINER,attributes);
        System.out.println("Config MDB container completed !");
    }




    public static void main(String[] args) throws Exception{
        //configStatelessContainer();
       configMdbContainer();
    }
}
