
import javax.xml.crypto.Data;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Vector;

// Work with config(parsing) file class
public class ConfigParse {
    private

    // Config filename
            String FileName;
    // Data from config
    private ConfigContainer data;
    // special words in config file
    public static final String[] ParamList = {"Input:", "Output:", "Reader:",
            "Writer:", "Executor:", "Order:"};

    // enum to unify parameters in config file
    enum Parameter {
        INPUT,
        OUTPUT,
        READER,
        WRITER,
        EXECUTOR,
        ORDER,
        ERROR
    }

    // Config class constructor
    //** Get ready to load(refresh) data
    //** In:
    //**** Config filename: String fileName
    //** Out: none
    public ConfigParse(String fileName) {
        this.FileName = fileName;
        MyPair mp = new MyPair("", "");
        this.data = new ConfigContainer("", mp, mp, "");
    }

    // Get Style from string
    //** 4 example "Input:" --> INPUT; "abracadabra"--> ERROR
    //** In: String from config
    //** Out: Parameter type
    public Parameter GetParameter(String S) {
        for (int i = 0; i < ParamList.length; i++) {
            if (ParamList[i].equals(S))
                return Parameter.values()[i];
        }
        return Parameter.ERROR;
    }

    // Refresh data function
    //** Use this.FileName to load config data and set this.data field
    //** In: none
    //** Out: none
    public ConfigContainer RefreshData() throws MyException {
        // Open file from this.FileName
        try {
            String Input = "", Output = "";
            MyPair ReaderConf = new MyPair("", ""), WriterConf = new MyPair("", "");
            Vector<MyPair> Exec = new Vector<>();
            Vector<String> Order = new Vector<>();
            //int i = 0;
            File file = new File(this.FileName);
            Scanner input = new Scanner(file);
            input.useDelimiter(" |\\n|\\r");
            String s = input.next();
            //!!
            while (true) { // we have break when we read all file, exception otherwise
               /* if (i == 0 && s.compareTo(style[0]) != 0 && s.compareTo(style[1]) != 0) // wrong 1st argument
                {
                    System.err.println("Incorrect file (1st string)");
                    break;
                } else if (i > 0 && s.compareTo(style[i + 1]) != 0) // wrong one of arguments
                {
                    System.err.println("Incorrect file (??? string)");
                    break;
                }*/
                while (s.equals("\n") || (input.hasNext() && s.length() == 0))
                    s = input.next();

                Parameter param = GetParameter(s);
                // only 3 steps
                // const exp required in switch
                if (param == Parameter.INPUT) {
                    s = input.next();
                    Input = s;
                } else if (param == Parameter.OUTPUT) {
                    s = input.next();
                    Output = s;
                } else if (param == Parameter.READER) {
                    s = input.next();
                    String s2 = input.next();
                    MyPair m = new MyPair(s, s2);
                    ReaderConf = m;
                } else if (param == Parameter.WRITER) {
                    s = input.next();
                    String s2 = input.next();
                    MyPair m = new MyPair(s, s2);
                    WriterConf = m;
                } else if (param == Parameter.EXECUTOR) {
                    s = input.next();
                    String s2 = input.next();
                    MyPair m = new MyPair(s, s2);
                    Exec.add(m);
                } else if (param == Parameter.ORDER) {
                    // Keyword break flag
                    boolean exist = false;
                    while (input.hasNext()) {
                        s = input.next();
                        for (String p : ParamList) {
                            if (p.equals(s))
                                exist = true;

                        }
                        if (!exist)
                            Order.add(s);
                        else
                            break;
                    }
                } else {
                    throw new MyException("Wrong algorithm"); // to log
                }

                if (input.hasNext())
                    s = input.next();
                else
                    break;
            }
            this.data = new ConfigContainer(Input, ReaderConf, WriterConf, Output, Exec, Order);
        } catch (FileNotFoundException|NoSuchElementException ex) {
            throw new MyException("File Incorrect in refresh data");
        }
        return this.data;
    }


}
