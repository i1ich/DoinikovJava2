import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Logger;

public final class WriterConfigParser {
    final Logger log;
    final String[] Words = {"Size:"};
    public WriterConfigParser(Logger log) {
        this.log = log;
    }
    public WriterConfig MakeConfig(String file)
    {
        WriterConfig wc = null;
        int size;
        try {
            FileInputStream F1 = new FileInputStream(file);
            Scanner input = new Scanner(F1);
            input.useDelimiter(" |\\n");
            if(input.hasNext()) {
                String s = input.next();
                if(!s.equals(Words[0])) {
                    this.log.info("Bad Writer config file ");
                }
            }
            else
                this.log.info("Bad Writer config file ");
            if(input.hasNext()) {
                String s = input.next();
                size = Integer.valueOf(s);

                wc = new WriterConfig(size);
            }
        } catch (FileNotFoundException e) {
            this.log.info("Bad Writer config file, file not found");
            return null;
        }

        return wc;
    }
}
