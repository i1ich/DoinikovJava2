import ru.spbstu.pipeline.IExecutable;
import ru.spbstu.pipeline.IReader;
import ru.spbstu.pipeline.RC;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class Reader implements IReader {
    IExecutable Consumer;
    FileInputStream F;
    ReaderConfigParser parser;
    ReaderConfig readerConfig;
    private final Logger logger;

    public Reader(Logger logger) {
        this.logger = logger;
        this.parser = new ReaderConfigParser(logger);
        readerConfig = null;
    }

    @Override
    public RC setInputStream(FileInputStream fis) {
        F = fis;
        return RC.CODE_SUCCESS;
    }
    // Only buffer size in config(Size: 100 for example)
    @Override
    public RC setConfig(String cfg) {
        readerConfig = parser.MakeConfig(cfg);
            if (readerConfig == null)
                return RC.CODE_CONFIG_GRAMMAR_ERROR;
            return RC.CODE_SUCCESS;
    }

    @Override
    public RC setConsumer(IExecutable c) {
        Consumer = c;
        return RC.CODE_SUCCESS;
    }

    @Override
    public RC setProducer(IExecutable p) {
        // do nothing, it is reader
        return RC.CODE_SUCCESS;
    }

    @Override
    public RC execute(byte[] data) {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(F, readerConfig.size);
        byte[] buff =new byte[readerConfig.size];
        try {
            int bytes = bufferedInputStream.read(buff, 0, readerConfig.size);
        } catch (IOException e) {
            return RC.READ_ERROR;
        }
        RC RC;
        RC = Consumer.execute(buff);
        return RC;
    }
}
