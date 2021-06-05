import java.io.Serializable;
import java.math.BigInteger;
// Learned about serializable from https://www.baeldung.com/java-serial-version-uid
class Signiture implements Serializable {

	private byte[] h;
	private BigInteger z;
	private static final long serialVersionUID = 5678910L;


	Signiture(byte[] h, BigInteger z) {
        this.h = h;
        this.z = z;
    }
//Getters 
    byte[] geth() {
    	return this.h;
    	}
    BigInteger getz() {
    	return this.z;
    	}
}