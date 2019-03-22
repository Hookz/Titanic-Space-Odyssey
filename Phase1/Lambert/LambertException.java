package Lambert;

public class LambertException extends Exception {

    public String message;

    public LambertException(String message) {
        this.message = "Lambert "+message;
    }

}
