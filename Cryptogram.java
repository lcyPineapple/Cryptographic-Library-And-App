import java.io.Serializable;
import java.util.Arrays;
// Learned about serializable from https://www.baeldung.com/java-serial-version-uid
class Cryptogram implements Serializable {

    private EllipticCurve z;
    private byte[] c;
    private byte[] t;
    private static final long serialVersionUID = 1234567L;


    Cryptogram(EllipticCurve z, byte[] c, byte[] t) {
        this.z = z;
        this.c = c;
        this.t = t;
    }

    EllipticCurve getz() {
    	return this.z;
    	}
    byte[] getc() {
    	return this.c;
    	}
    byte[] gett() {
    	return this.t;
    	}

    @Override
    public String toString() {
        return "z = " + z.getX() + " c = " + Arrays.toString(c) + "t = " + Arrays.toString(t);
    }
}
