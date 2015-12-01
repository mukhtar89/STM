package STM.Logger;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by Mukhtar on 11/29/2015.
 */
public class LoggerFwk {

    private FileHandler fh;

    public LoggerFwk() throws IOException {
        fh = new FileHandler("F:/Academics/UCIrvineGD/Qtr4/Multicore Prog/STM/MyLogFile.log");
    }

    public Logger logHandler(Logger logger) {
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
        return logger;
    }
}
