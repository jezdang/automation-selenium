package org.openweathermap.helper;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CommonUtils {
    private static final Logger LOGGER = Logger.getLogger(CommonUtils.class);
    private static File classpathRoot = new File(System.getProperty("user.dir"));

    private CommonUtils(){
    }

    public static File getClassRootPath() {
        return classpathRoot;
    }

    public static String getCurrentDateTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd___HH_mm_ss");
        return sdf.format(cal.getTime());
    }

    public static void copyDirectory(File srcFolder, File destFolder) {
        createDirectory(destFolder);
        LOGGER.debug("Directory copied from " + srcFolder + " to " + destFolder);
        int length;
        if(srcFolder.isDirectory()) {
            if(!destFolder.exists()) {
                destFolder.mkdir();
            }
            String[] in = srcFolder.list();
            String[] out = in;
            int ex = in.length;

            for(length = 0; length < ex; ++length) {
                String file = out[length];
                File srcFile = new File(srcFolder, file);
                File destFile = new File(destFolder, file);
                copyDirectory(srcFile, destFile);
            }
        } else {
            FileInputStream var23 = null;
            FileOutputStream var24 = null;

            try {
                var23 = new FileInputStream(srcFolder);
                var24 = new FileOutputStream(destFolder);
                byte[] var25 = new byte[1024];

                while((length = var23.read(var25)) > 0) {
                    var24.write(var25, 0, length);
                }
                LOGGER.debug("File copied from " + srcFolder + " to " + destFolder);
            } catch (IOException var21) {
                LOGGER.error(var21);
            } finally {
                if(var24 != null) {
                    try {
                        var24.close();
                    } catch (IOException var20) {
                        LOGGER.error(var20);
                    }
                }
                if(var23 != null) {
                    try {
                        var23.close();
                    } catch (IOException var19) {
                        LOGGER.error(var19);
                    }
                }
            }
        }
    }

    public static void createDirectory(File destFolder) {
        if(destFolder.exists()) {
            deleteDirectory(destFolder);
            destFolder.mkdirs();
        } else if(destFolder.isDirectory()) {
            destFolder.mkdirs();
        }
    }

    public static void deleteDirectory(File file) {
        if(file.isDirectory()) {
            if(file.list().length == 0) {
                file.delete();
                LOGGER.debug("Directory is deleted : " + file.getAbsolutePath());
            } else {
                String[] files = file.list();
                String[] var2 = files;
                int var3 = files.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    String temp = var2[var4];
                    File fileDelete = new File(file, temp);
                    deleteDirectory(fileDelete);
                }

                if(file.list().length == 0) {
                    file.delete();
                    LOGGER.debug("Directory is deleted: " + file.getAbsolutePath());
                }
            }
        } else {
            file.delete();
            LOGGER.debug("File is deleted: " + file.getAbsolutePath());
        }
    }

    public static void wait(int intTimeWaitInSeconds) {
        LOGGER.info("Wait for: [" + intTimeWaitInSeconds + "] seconds");
        int intCount = 1;

        while (intTimeWaitInSeconds >= 2) {
            try {
                Thread.sleep(intCount * 2000L);
            } catch (InterruptedException ex) {
                LOGGER.error(ex);
                // clean up state...
                Thread.currentThread().interrupt();
            }
            intTimeWaitInSeconds -= 2;
            LOGGER.debug("Remaining time: " + intTimeWaitInSeconds);
        }
    }

    public static String getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return sdf.format(new Date());
    }

    public static String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }
}
