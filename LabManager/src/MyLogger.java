
public abstract class MyLogger {
    protected final java.util.logging.Logger log;

    protected MyLogger(java.util.logging.Logger log) {
        this.log = log;
    }

    protected final void writeLoggerInfo(String info) {
        if (this.log != null && info != null) {
            this.log.info(info);
        }

    }

    protected final java.util.logging.Logger getLog() {
        return this.log;
    }
}
