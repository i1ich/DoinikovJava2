public class MyPair {
    private String ClassName;
    private String Config;

    public MyPair(String className, String config) {
        ClassName = className;
        Config = config;
    }

    String getClassName()
    {
        return this.ClassName;

    }
    String getConfig()
    {
        return this.Config;
    }
}
