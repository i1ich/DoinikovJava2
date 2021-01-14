import Workers.IO.BurkovReader.ReaderManager;
import javafx.util.Pair;
import ru.spbstu.pipeline.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;
import java.util.logging.Logger;

public class Manager {
    private String ConfigName;
    private Vector<Object> pipeline;
    // Streams to close
    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;
    private IWriter writer;
    private IReader reader;
    private Vector<Pair<String, IExecutor>> workers;
    private Logger logger;

    public Manager(String Name, Logger logger) {
        this.ConfigName = Name;
        this.logger = logger;
        pipeline = new Vector<>();
        workers = new Vector<>();
    }

    public RC SyntaxParse() {
        ConfigParse c = new ConfigParse(ConfigName);
        RC RC = ru.spbstu.pipeline.RC.CODE_SUCCESS;
        try {
            ConfigContainer data = c.RefreshData();
            RC = CreateSession(data);

        } catch (MyException e) {
            e.myOwnExceptionMsg(); // to log
            if (RC == ru.spbstu.pipeline.RC.CODE_SUCCESS)
                RC = ru.spbstu.pipeline.RC.ALGORITHM_PARAM_ERROR;
        }
        return RC;
    }

    IExecutor FindWorker(String S) {
        for (Pair P : workers
        ) {
            if (P.getKey().equals(S)) {
                return (IExecutor) P.getValue();
            }

        }
        return null;
    }

    RC MakeConveyor(ConfigContainer conf) {
        pipeline.add(reader);
        for (int i = 1; i < conf.Order.size() - 1; i++) {
            IExecutor w = FindWorker(conf.Order.get(i));
            if (w == null)
                return RC.ALGORITHM_PARAM_ERROR;
            pipeline.add(w);
        }
        pipeline.add(writer);

        IReader reader = (IReader) pipeline.get(0);
        reader.setConsumer((IExecutable) pipeline.get(1));
        IWriter writer = (IWriter) pipeline.get(pipeline.size() - 1);

        writer.setProducer((IExecutable) pipeline.get(pipeline.size() - 2));
        for (int i = 1; i < pipeline.size() - 1; i++) {
            IExecutor w = (IExecutor) pipeline.get(i);
            RC rc = w.setConsumer((IExecutable) pipeline.get(i + 1));
            if(rc != ru.spbstu.pipeline.RC.CODE_SUCCESS)
                return rc;
            rc = w.setProducer((IExecutable) pipeline.get(i - 1));
            if(rc != ru.spbstu.pipeline.RC.CODE_SUCCESS)
                return rc;
        }
        return RC.CODE_SUCCESS;
    }

    // Parametrize conveyor/session
    RC CreateSession(ConfigContainer conf) {
        // Reader
        try {
            fileInputStream = new FileInputStream(conf.Input);
            Class clazz = Class.forName(conf.ReaderConf.getClassName());
            Class[] params = {Logger.class};
            reader = (IReader) clazz.getConstructor(params).newInstance(logger);
            RC rc = reader.setConfig(conf.ReaderConf.getConfig());
            if (rc != ru.spbstu.pipeline.RC.CODE_SUCCESS)
                return rc;
            reader.setInputStream(fileInputStream);
        } catch (FileNotFoundException | IllegalAccessException |
                InstantiationException | ClassNotFoundException |
                NoSuchMethodException | InvocationTargetException e) {
            return RC.INPUT_FILE_OPEN_ERROR; // to log
        }
        // Workers
        for (MyPair P : conf.Exec) {
            try {
                Class<?> clazz = Class.forName(P.getClassName());
                Class<?>[] params = {Logger.class};
                IExecutor worker = (IExecutor) clazz.getConstructor(params).newInstance(logger);
                RC rc = worker.setConfig(P.getConfig());
                if (rc != ru.spbstu.pipeline.RC.CODE_SUCCESS)
                    return rc;

                workers.add(new Pair(P.getClassName(), worker));
            } catch (InstantiationException | IllegalAccessException e) {
                return RC.ALGORITHM_PARAM_ERROR; // to log
            } catch (NoSuchMethodException | InvocationTargetException | ClassNotFoundException e) {
                logger.warning("Can`t create worker class");
            }


            // all are Workers
            //Class cls = Class.forName(P.getClassName());
            //if (!cls.isInstance(worker))
            //    return ReturningCode.ALGORITHM_PARAM_ERROR;

        }
        // Writer
        try {
            fileOutputStream = new FileOutputStream(conf.Output);
            Class clazz = Class.forName(conf.WriterConf.getClassName());
            Class[] params = {Logger.class};
            writer = (IWriter) clazz.getConstructor(params).newInstance(logger);
            RC rc = writer.setConfig(conf.WriterConf.getConfig());
            if (rc != ru.spbstu.pipeline.RC.CODE_SUCCESS)
                return rc;
            writer.setOutputStream(fileOutputStream);
        } catch (IOException | IllegalAccessException |
                InstantiationException | ClassNotFoundException e) {
            return RC.OUTPUT_FILE_OPEN_ERROR; // to log
        } catch (NoSuchMethodException e) {
            logger.warning("Can`t create class");
        } catch (InvocationTargetException e) {
            logger.warning("Can`t create class");
        }
        /*IReader reader = (IReader) pipeline.get(0);
        reader.setConsumer((IExecutable) pipeline.get(1));
        IWriter writer = (IWriter) pipeline.get(pipeline.size() - 1);

        writer.setConsumer((IExecutable) pipeline.get(pipeline.size() - 2));
        for (int i = 1; i < pipeline.size() - 1; i++) {
            IExecutor w = (IExecutor) pipeline.get(i);
            w.setConsumer((IExecutable) pipeline.get(i + 1));
            w.setProducer((IExecutable) pipeline.get(i - 1));
        }*/

        RC RC = MakeConveyor(conf);
        return RC;
    }

    RC Run() {
        IExecutable input = (IExecutable) pipeline.get(0);
        RC RC = input.execute(null);
        try {
            fileInputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            return ru.spbstu.pipeline.RC.ALGORITHM_PARAM_ERROR;
        }
        return RC;
    }
}
