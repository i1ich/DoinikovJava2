import ru.spbstu.pipeline.IExecutable;
import ru.spbstu.pipeline.IWriter;
import ru.spbstu.pipeline.RC;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;

public class Writer implements IWriter {
    IExecutable Producer;
    FileOutputStream F;
    WriterConfigParser parser;
    WriterConfig writerConfig;
    private final Logger logger;

    public Writer(Logger logger) {
        this.logger = logger;
    }

    @Override
    public RC setOutputStream(FileOutputStream fos) {
        F = fos;
        return RC.CODE_SUCCESS;
    }

    @Override
    public RC setConfig(String cfg) {
        writerConfig = parser.MakeConfig(cfg);
        if (writerConfig == null)
            return RC.CODE_CONFIG_GRAMMAR_ERROR;
        return RC.CODE_SUCCESS;

    }

    @Override
    public RC setConsumer(IExecutable c) {
        // do nothing, it`s writer
        return RC.CODE_SUCCESS;
    }

    @Override
    public RC setProducer(IExecutable p) {
        Producer = p;
        return RC.CODE_SUCCESS;
    }

    @Override
    public RC execute(byte[] data) {
        try {
            if (data.length != writerConfig.size)
                return RC.BUFFER_SIZE_ERROR;
            F.write(data);
        } catch (IOException e) {
            return RC.WRITE_ERROR;
        }
        return RC.CODE_SUCCESS;
    }
}
