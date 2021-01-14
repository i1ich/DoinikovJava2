import ru.spbstu.pipeline.IExecutable;
import ru.spbstu.pipeline.IExecutor;
import ru.spbstu.pipeline.RC;

import java.util.logging.Logger;

public class Executor implements IExecutor {
    IExecutable Consumer;
    IExecutable Producer;
    ExecutorConfigParser parser;
    ExecutorConfig executorConfig;
    private final Logger logger;

    public Executor(Logger logger) {
        this.logger = logger;
        this.parser = new ExecutorConfigParser(logger);
        executorConfig = null;
    }

    @Override
    public RC setConfig(String cfg) {
        executorConfig = parser.MakeConfig(cfg);
        if (executorConfig == null)
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
        Producer = p;
        return RC.CODE_SUCCESS;
    }

    @Override
    public RC execute(byte[] data) {
        RC RC;
        if(data.length == executorConfig.size){
            for (int i = 0; i < data.length; i++) {
                data[i] = '1';
            }
            RC = Consumer.execute(data);
        }
        else
            return ru.spbstu.pipeline.RC.BUFFER_SIZE_ERROR;
        return RC;
    }
}
