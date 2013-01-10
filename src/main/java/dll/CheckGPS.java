package dll;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Gps 纠偏测试程序
 * 
 * @author huwg
 * 
 */
public class CheckGPS {

    // private static String callurl = "http://119.254.82.227/gps/Gps";
    private static Logger log = Logger.getLogger(CheckGPS.class);

    private static String callurl = "http://192.168.1.108:8081/Gps/Gps";

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub

        List<Long> lonList = new ArrayList<Long>();
        List<Long> latList = new ArrayList<Long>();
        // lonList.add((long) 419227416);
        // latList.add((long) 143663884);

        Double _lon = 3600000 * 116.35697;
        Double _lat = 3600000 * 39.8855;
        lonList.add(_lon.longValue());
        latList.add(_lat.longValue());

        Document document = DocumentHelper.createDocument();
        Element line = document.addElement("line");

        for (int i = 0; i < lonList.size(); i++) {
            Element point = line.addElement("point");
            Element id = point.addElement("id");
            Element lon = point.addElement("lon");
            Element lat = point.addElement("lat");
            id.setText(i + "");
            lon.setText(lonList.get(i) + "");
            lat.setText(latList.get(i) + "");
        }
        String xml = document.asXML();
        log.info("start String xml:==" + xml);

        String result = "";
        URL u0 = new URL(callurl);
        HttpURLConnection conn = (HttpURLConnection) u0.openConnection();
        conn.setRequestMethod("POST");
        byte[] contentbyte = xml.getBytes();
        conn.setRequestProperty("Content-Type", "text/plain");
        conn.setRequestProperty("Content-Length", "" + contentbyte.length);
        conn.setRequestProperty("Content-Language", "en-US");
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setReadTimeout(1000 * 1000);
        conn.setConnectTimeout(1000 * 1000);
        OutputStream out = conn.getOutputStream();
        out.write(contentbyte);
        out.flush();
        out.close();
        InputStream in = conn.getInputStream();
        StringBuffer buffer = new StringBuffer();
        int i = 0;
        while (i != -1) {
            i = in.read();
            if (i != -1) {
                buffer.append((char) i);
            }
        }
        in.close();

        result = new String(buffer.toString().getBytes("iso-8859-1"), "UTF-8");
        log.info("return String xml:==" + result);
    }
}
