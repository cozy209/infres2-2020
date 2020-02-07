import java.io.Serializable;

public class Message implements Serializable {

    private byte[] IV;
    private byte[] message;

    public Message(byte[] IV, byte[] message) {
        this.IV = IV;
        this.message = message;
    }

    public byte[] getIV() {
        return IV;
    }

    public byte[] getMessage() {
        return message;
    }
}
