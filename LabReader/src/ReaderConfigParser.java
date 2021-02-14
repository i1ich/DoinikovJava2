import ru.spbstu.pipeline.RC;
import sun.rmi.runtime.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Logger;

public final class ReaderConfigParser {
    final Logger log;
    final String[] Words = {"Size:"};
    public ReaderConfigParser(Logger log) {
        this.log = log;
    }
    public ReaderConfig MakeConfig(String file)
    {
        ReaderConfig rc = null;
        int size;
        try {
            FileInputStream F1 = new FileInputStream(file);
            Scanner input = new Scanner(F1);
            input.useDelimiter(" |\\n");
            if(input.hasNext()) {
                String s = input.next();
                if(!s.equals(Words[0])) {
                    this.log.info("Bad reader config file ");
                }
            }
            else
                this.log.info("Bad reader config file ");
            if(input.hasNext()) {
                String s = input.next();
                size = Integer.valueOf(s);

                rc = new ReaderConfig(size);
            }
        } catch (FileNotFoundException e) {
            this.log.info("Bad reader config file, file not found");
            return null;
        }

        return rc;
    }
}
