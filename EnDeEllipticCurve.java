import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Arrays;

public class EnDeEllipticCurve {
	
	public static final int KINT = 64; // 512 possible bits in byte[64] for KECCAK[512]

	/**
	 *  Generating a (Schnorr/ECDHIES) key pair from passphrase pw:
	 *  s = KMACXOF256(pw, “”, 512, “K”);
	 *  s =  s xor 4s
	 *  V = s*G
	 *  key pair: (s, V)
	 * @param fileName bit string to be padded
	 * @param password integer used to pad string
	 */
	public static void generateKeyPairElliptic(String fileName, String password) throws IOException {
		byte[] encryptedFile = Files.readAllBytes(Paths.get(fileName));
		byte[] myPassBytes = password.getBytes();
		byte[] s = KMACXOF256.theKMACXOF256(password.getBytes(), "".getBytes(), 512, "K".getBytes());
		BigInteger bigS = new BigInteger(s);
		BigInteger sxor = BigInteger.valueOf(4).multiply(bigS);
		BigInteger mynum = BigInteger.valueOf(18);
		EllipticCurve g = new EllipticCurve(mynum, false);
		EllipticCurve v = g.scalarMultiplication(sxor);
		
		File myObj = new File("generateKeyPairElliptic.txt");
		FileWriter myWriter = new FileWriter("generateKeyPairElliptic.txt");
	    myWriter.write(v.toString());
	    myWriter.close();
	    System.out.println("Your key pair has been written to : generateKeyPairElliptic.txt");
	}
	
}
