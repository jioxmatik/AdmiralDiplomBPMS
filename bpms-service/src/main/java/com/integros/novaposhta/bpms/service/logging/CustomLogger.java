package com.integros.novaposhta.bpms.service.logging;

import java.io.File;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

public class CustomLogger extends Logger {
    // Own console log
    private static final Logger consoleLog = Logger.getLogger(CustomLogger.class.getName());

    protected static final String SYSTEM_LINEBREAK = System.getProperty("line.separator");
    
    private static final long MAX_FILE_SIZE = 1024L*1024L*1024L;    // 1 GB
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
    private static final String FILENAME_TEMPLATE = "bpms_service_%s.log";
    private static final String OLD_FILENAME_POSTFIX = "_old";
    
    private static final String serviceInstanceId = String.valueOf(System.currentTimeMillis());
    
    private final File currentLogFile;
    
    protected CustomLogger(String name) {
        super(name, null);
        currentLogFile = getFile();
    }

    public static CustomLogger getLogger(String name) {
        return new CustomLogger(name);
    }
    
    @Override
    public void log(LogRecord record) {
        super.log(record);
        consoleLog.log(record);
        try {
            if(currentLogFile.exists() && FileUtils.sizeOf(currentLogFile) > MAX_FILE_SIZE) {
                // Rotate file
                File oldFile = getFile(true);
                if(oldFile.exists())
                {
                    oldFile.delete();
                }
                FileUtils.moveFile(currentLogFile, oldFile);
            }
            FileUtils.writeStringToFile(currentLogFile, logRecordToString(record), true);
        } catch(Exception e) {
            super.log(
                new LogRecord(
                    Level.WARNING, 
                    String.format(
                        "Failed to write log record to file: %s",
                        e.getMessage()
                    )
                )
            );
        }
    }
    
    protected static String logRecordToString(LogRecord record)
    {
        return String.format(
            "%s [%s] (%s) %s" + SYSTEM_LINEBREAK,
            currentDateAsString(),
            record.getLevel(),
            record.getLoggerName(),
            new MessageFormat(record.getMessage()).format(record.getParameters())
        );
    }
    
    protected static String currentDateAsString()
    {
        return dateFormat.format(new Date());
    }
    
    protected static File getFile()
    {
        return getFile(false);
    }
    
    protected static File getFile(boolean old)
    {
        return new File(
            System.getenv("JBOSS_HOME") + "/"
            + String.format(
                FILENAME_TEMPLATE + (old ? OLD_FILENAME_POSTFIX : ""),
                serviceInstanceId
            )
        );
    }
}
