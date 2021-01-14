import java.util.Vector;

public final class ConfigContainer {
    public final String Input;
    public final MyPair ReaderConf;
    public final MyPair WriterConf;
    public final String Output;
    public final Vector<MyPair> Exec;
    // Executors class name in correct order
    public final Vector<String> Order;

    public ConfigContainer(String input, MyPair readerConf, MyPair writerConf, String output, Vector<MyPair> exec, Vector<String> order) {
        Input = input;
        ReaderConf = readerConf;
        WriterConf = writerConf;
        Output = output;
        Exec = exec;
        Order = order;
    }

    public ConfigContainer(String input, MyPair readerConf, MyPair writerConf,
                           String output) {
        Input = input;
        ReaderConf = readerConf;
        WriterConf = writerConf;
        Output = output;
        Exec = new Vector<>();
        Order =  new Vector<>();
    }
}
