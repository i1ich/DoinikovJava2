import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Logger;

public final class ExecutorConfigParser {
    final Logger log;
    final String[] Words = {"Size:"};
    public ExecutorConfigParser(Logger log) {
        this.log = log;
    }
    public ExecutorConfig MakeConfig(String file)
    {
        ExecutorConfig wc = null;
        int size;
        try {
            FileInputStream F1 = new FileInputStream(file);
            Scanner input = new Scanner(F1);
            input.useDelimiter(" |\\n");
            if(input.hasNext()) {
                String s = input.next();
                if(!s.equals("Size:")) {
                    this.log.info("Bad Executor config file ");
                }
            }
            else
                this.log.info("Bad Executor config file ");
            if(input.hasNext()) {
                String s = input.next();
                size = Integer.valueOf(s);

                wc = new ExecutorConfig(size);
            }
        } catch (FileNotFoundException e) {
            this.log.info("Bad Executor config file, file not found");
            return null;
        }

        return wc;
    }
}
