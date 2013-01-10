package dll;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class GetIO {

    /**
     * 获取数据 数组格式
     * 
     * @param bos
     * @param is
     * @return
     */
    public byte[] getIO(ByteArrayOutputStream bos, InputStream is) {
        byte[] readByte = new byte[1024];
        try {
            int b = 0;
            while ((b = is.read(readByte, 0, readByte.length)) != -1) {
                bos.write(readByte, 0, b);
            }
            readByte = bos.toByteArray();
            // System.out.println(new String(readByte));
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return readByte;
    }

    /**
     * 
     * 
     * 
     * @param bos
     * @param is
     * @return
     */
    public String getIOString(ByteArrayOutputStream bos, InputStream is) {
        byte[] readByte = new byte[1024];
        try {
            int b = 0;
            while ((b = is.read(readByte, 0, readByte.length)) != -1) {
                bos.write(readByte, 0, b);
            }
            readByte = bos.toByteArray();
        } catch (Exception localException) {
        }
        return new String(readByte);
    }

}