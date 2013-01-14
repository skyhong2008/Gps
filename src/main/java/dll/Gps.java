package dll;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 
 * @author huwg
 * 
 */
public class Gps extends HttpServlet {

    private static Logger log = Logger.getLogger(Gps.class);

    private static final long serialVersionUID = -5300807699299719857L;

    ByteArrayOutputStream baos = null;
    InputStream is = null;
    GetIO gIo = null;
    DataOutputStream dos = null;

    /**
	 * 
	 */
    static {

        //
        //
        // 数据格式

        // //
        // String path = Gps.class.getClassLoader().getResource("/").getPath();
        // // System.out.println(path + "GPSEncryption.dll");
        // System.load(path + "GPSEncryption.dll");

        // 原先方式
        String abc = System.getProperty("java.library.path");
        log.info("java.library.path:==" + abc);
        // System.out.println(abc);
        System.loadLibrary("GPSEncryption");

    }

    public native void encryption_gps(Position paramPosition1,
            Position paramPosition2);

    /**
	 * 
	 */
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        //
        this.baos = new ByteArrayOutputStream();
        this.is = request.getInputStream();
        this.gIo = new GetIO();

        //
        SAXReader saxReader = new SAXReader();
        Document document = null;
        Gps hw = new Gps();
        try {
            document = saxReader.read(new ByteArrayInputStream(this.gIo.getIO(
                    this.baos, this.is)));
        } catch (Exception localException1) {
            localException1.printStackTrace();
        } finally {
            this.is.close();
        }

        if (null != document) {

        }
        Element root = document.getRootElement();
        List<String> id = new ArrayList<String>();
        List<Integer> lon = new ArrayList<Integer>();
        List<Integer> lat = new ArrayList<Integer>();
        Iterator<Element> it = root.elementIterator("point");
        while (it.hasNext()) {
            Element foo = (Element) it.next();
            id.add(foo.elementText("id"));
            lon.add(Integer.valueOf(foo.elementText("lon")));
            lat.add(Integer.valueOf(foo.elementText("lat")));
        }

        List<Integer> lonnew = new ArrayList<Integer>();
        List<Integer> latnew = new ArrayList<Integer>();
        for (int i = 0; i < id.size(); i++) {
            try {

                Position ps = new Position(lon.get(i).intValue(), lat.get(i)
                        .intValue());
                log.info(" old:==lon:" + lon.get(i).intValue() + ",lat:"
                        + lat.get(i).intValue());
                Position reps = new Position(0, 0);
                hw.encryption_gps(ps, reps);
                lonnew.add(Integer.valueOf(reps.getLon()));
                latnew.add(Integer.valueOf(reps.getLat()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Document documentout = DocumentHelper.createDocument();
        Element line = documentout.addElement("line");
        for (int i = 0; i < id.size(); i++) {
            Element point = line.addElement("point");

            Element idout = point.addElement("id", id.get(i));
            point.addElement("lon", String.valueOf(lonnew.get(i)));
            point.addElement("lat", String.valueOf(latnew.get(i)));
            log.info(" new:==lon:" + lonnew.get(i) + ",lat:" + latnew.get(i));
            // Element idout = point.addElement("id");
            // Element lonout = point.addElement("lon");
            // Element latout = point.addElement("lat");
            //
            // idout.setText(String.valueOf(id.get(i)));
            // lonout.setText(String.valueOf(lonnew.get(i)));
            // latout.setText(String.valueOf(latnew.get(i)));
        }
        String xml = documentout.asXML();
        // log.info("xml:==" + xml);

        this.dos = new DataOutputStream(response.getOutputStream());
        try {
            this.dos.write(xml.getBytes());
        } catch (Exception localException2) {
        } finally {
            this.is.close();
            this.dos.flush();
            this.dos.close();
        }

    }

    // ===========================================================
    // ===========================================================
    // ===========================================================

    /**
     * 测试
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            Position ps = new Position(419227416, 143663884);
            Position reps = new Position(0, 0);
            Gps hw = new Gps();

            hw.encryption_gps(ps, reps);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}