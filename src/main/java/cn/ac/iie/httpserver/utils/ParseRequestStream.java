package cn.ac.iie.httpserver.utils;

import java.io.*;

/**
 * @author Fighter Created on 2018/10/14.
 */
public class ParseRequestStream {

    public static String parseRequestStreamAsString(InputStream pServletInputStream) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] b = new byte[4096];
        int i = 0;
        while ((i = pServletInputStream.read(b, 0, 4096)) > 0) {
            out.write(b, 0, i);
        }
        return new String(out.toByteArray(), "UTF-8");
    }

    public static File parseRequestStreamAsFile(String pFilePath, InputStream pServletInputStream) {

        File f = null;
        FileOutputStream os = null;
        try {
            f = new File(pFilePath);
            os = new FileOutputStream(f);
            int temp = 0;
            byte[] b = new byte[10240];
            while ((temp = pServletInputStream.read(b, 0, 10240)) != -1) {
                os.write(b, 0, temp);
            }
        } catch (Exception e) {
            return null;
        } finally {
            try {
                os.close();
            } catch (Exception e) {
            }
            try {
                pServletInputStream.close();
            } catch (Exception e) {
            }
        }
        return f;
    }

    public static byte[] parseRequestStreamAsByteArray(InputStream pServletInputStream) {
        byte[] req = null;
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            byte[] b = new byte[4096];
            int i = 0;
            while ((i = pServletInputStream.read(b, 0, 4096)) > 0) {
                out.write(b, 0, i);
            }
            req = out.toByteArray();
        } catch (Exception ex) {
            return null;
        } finally {
            try {
                out.close();
            } catch (Exception ex) {
                return null;
            }
            try {
                pServletInputStream.close();
            } catch (Exception ex) {
                return null;
            }
        }
        return req;
    }
}
