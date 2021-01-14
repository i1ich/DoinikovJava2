import ru.spbstu.pipeline.RC;

import java.io.*;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        RC rc = ru.spbstu.pipeline.RC.CODE_SUCCESS;
        FileInputStream In = null;
        try {
            if (args.length != 1) {
                String E = "Bad cmd line format";
                throw new MyException(E);
            }
            Logger log = Logger.getLogger(Main.class.getName());

            Manager M = new Manager(args[0], log);
            rc = M.SyntaxParse();
            if (rc == ru.spbstu.pipeline.RC.CODE_SUCCESS)
                rc = M.Run();
            BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt"));
            switch (rc) {
                case ALGORITHM_PARAM_ERROR:
                    writer.write("ERROR in algorithm\n" + "\nConfig: " + args[0]);
                    break;
                case READ_ERROR:
                    writer.write("ERROR in read module" + "\nConfig: " + args[0]);
                    break;
                case WRITE_ERROR:
                    writer.write("ERROR in write module" + "\nConfig: " + args[0]);
                    break;
                case BUFFER_SIZE_ERROR:
                    writer.write("ERROR size between executors is different" + "\nConfig: " + args[0]);
                    break;
                case CODE_CONFIG_GRAMMAR_ERROR:
                    writer.write("ERROR in config files" + "\nConfig: " + args[0]);
                    break;
                case INPUT_FILE_OPEN_ERROR:
                    writer.write("ERROR: input file not open" + "\nConfig: " + args[0]);
                    break;
                case OUTPUT_FILE_OPEN_ERROR:
                    writer.write("ERROR: output file not open" + "\nConfig: " + args[0]);
                    break;
                case CODE_INVALID_ARGUMENT:
                    System.err.println("ERROR: output file not open" + "\nConfig: " + args[0]);
                    break;
                case CODE_SUCCESS:
                    writer.write("SUCCESS" + "\nConfig: " + args[0]);
                    break;
            }
            writer.close();
        } catch (MyException ex) {
            ex.myOwnExceptionMsg();
        } catch (IOException e) {
            System.err.println("ERROR: log file not open");
        }

    }
}
