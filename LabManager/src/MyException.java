
public class MyException extends Exception {

    private String someString;

    public MyException (String string) {
        this.someString = string;
        System.out.println("Exception ExcClass");
    }

    public void myOwnExceptionMsg() {
        System.err.println("This is exception message for string: " + someString);
    }
}