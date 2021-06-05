import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Arrays;


/**This class handles the encryption and decryption of files, and 
 * input and prepares output files and all console output. For Part 2
 *
 * @author Leika Yamada
 * @date 6/04/2021
 * */
public class EnDeEllipticCurve {
	
	public static final int KINT = 64; // 512 possible bits in byte[64] for KECCAK[512]
	public static final char[] HEXIDECIMAL = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	
	/**
	 *  verify signiture: ran out of time
	 */
	public static void verifySig(String password, String fileName) throws IOException {
		//NEED to do
	}
	/**
	 *  Generating a signature for a byte array m under passphrase pw:
		s = KMACXOF256(pw, “”, 512, “K”); s = 4s
		k = KMACXOF256(s, m, 512, “N”); k = 4k
		U = k*G;
		h = KMACXOF256(Ux, m, 512, “T”); z = (k – hs) mod r
		signature: (h, z)
	 */
	public static void generateSig(String password, String fileName) throws IOException {
		byte[] m = Files.readAllBytes(Paths.get(fileName));
		byte[] myPassBytes = password.getBytes();
		byte[] s = KMACXOF256.theKMACXOF256(password.getBytes(), "".getBytes(), 512, "K".getBytes());
		byte[] k = KMACXOF256.theKMACXOF256(s, m, 512, "N".getBytes());
		BigInteger bigS = new BigInteger(s);
		BigInteger s4 = BigInteger.valueOf(4).multiply(bigS);
		BigInteger bigk = new BigInteger(k);
		BigInteger k4 = BigInteger.valueOf(4).multiply(bigk);
		BigInteger mynum = BigInteger.valueOf(18);
		EllipticCurve g = new EllipticCurve(mynum, false);
		EllipticCurve u = g.scalarMultiplication(k4);
		byte[] h =  KMACXOF256.theKMACXOF256(u.getX().toByteArray(), m, 512, "T".getBytes());
		BigInteger hbig = new BigInteger(h);
		BigInteger hs = hbig.multiply(s4);
		BigInteger big = new BigInteger("1000000000000000000000000000000");
		BigInteger r = new BigInteger("2");
	    r = (r.pow(519)).subtract(big);
		BigInteger z = (k4.subtract(hs)).mod(r);
		Signiture sig = new Signiture(h, z);
		
		File myObj = new File("generateSig.txt");
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("generateSig.txt"));
        out.writeObject(sig);
	    out.close();
	    System.out.println("Successfully Encrypted.");
	    System.out.println("Your signature has been written to : generateSig.txt");
	    
	}
	
	/**
	 *  Generating a (Schnorr/ECDHIES) key pair from passphrase pw:
	 *  s = KMACXOF256(pw, “”, 512, “K”);
	 *  s =  s xor 4s
	 *  V = s*G
	 *  key pair: (s, V)
	 * @param fileName bit string to be padded
	 * @param password integer used to pad string
	 */
	public static void generateKeyPairElliptic(String password) throws IOException {
		byte[] myPassBytes = password.getBytes();
		byte[] s = KMACXOF256.theKMACXOF256(password.getBytes(), "".getBytes(), 512, "K".getBytes());
		BigInteger bigS = new BigInteger(s);
		BigInteger s4 = BigInteger.valueOf(4).multiply(bigS);
		BigInteger mynum = BigInteger.valueOf(18);
		EllipticCurve g = new EllipticCurve(mynum, false);
		EllipticCurve v = g.scalarMultiplication(s4);
		
		File myObj = new File("generateKeyPairElliptic.txt");
		FileWriter myWriter = new FileWriter("generateKeyPairElliptic.txt");
	    myWriter.write(v.toString());
	    myWriter.close();
	    System.out.println("Your key pair has been written to : generateKeyPairElliptic.txt");
	}
	
	/**
	 *Encrypting a byte array m under the (Schnorr/ECDHIES) public key V:
	  k = Random(512); k = 4k
	 W = k*V; Z = k*G
	 (ke || ka) = KMACXOF256(Wx, “”, 1024, “P”)
	 c = KMACXOF256(ke, “”, |m|, “PKE”) xor m
	 t = KMACXOF256(ka, m, 512, “PKA”)
	 cryptogram: (Z, c, t)
	 * @throws ClassNotFoundException 
	 */
	public static void encryptElliptic(String fileName) throws IOException, ClassNotFoundException {
		byte[] fileInput = Files.readAllBytes(Paths.get(fileName));
		SecureRandom randNum = new SecureRandom();
		byte[] k = new byte[KINT];
		randNum.nextBytes(k);
		BigInteger kbig = BigInteger.valueOf(4).multiply(new BigInteger(k));
		
		ObjectInputStream obStream = new ObjectInputStream(new FileInputStream(fileName));
		EllipticCurve v = (EllipticCurve) obStream.readObject();
		EllipticCurve w = v.scalarMultiplication(kbig);
		
		
		BigInteger mynum = BigInteger.valueOf(18);
		EllipticCurve g = new EllipticCurve(mynum, false);
        EllipticCurve Z = g.scalarMultiplication(kbig);
      
        byte[] kellka = KMACXOF256.theKMACXOF256(w.getX().toByteArray(), "".getBytes(), 1024, "p".getBytes());
        byte[] ke = Arrays.copyOfRange(kellka, 0, KINT);
        byte[] ka = Arrays.copyOfRange(kellka, KINT, 128);
        int myNum = fileInput.length;
        byte[] c = KMACXOF256.theKMACXOF256(ke, "".getBytes(), myNum * 8, "PKE".getBytes());
        byte[] cxor = new byte[myNum];
        for (int i = 0; i < myNum; i++) {
            cxor[i] = (byte) (fileInput[i] ^ c[i]);
        }
        byte[] t = KMACXOF256.theKMACXOF256(ka, fileInput, 512, "PKA".getBytes());
        
        File myObj = new File("encrypEllip.txt");
        Cryptogram myCrypt = new Cryptogram(Z, c, t);
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("encrypEllip.txt"));
        out.writeObject(myCrypt);
	    out.close();
	    System.out.println("Successfully Encrypted.");
	    System.out.println("Your cryptogram has been written to : encrypEllip.txt");
	}
	
	/**
	 * Decrypting a cryptogram (Z, c, t) under passphrase pw
	 s = KMACXOF256(pw, “”, 512, “K”); s = 4s
	 W = s*Z
	 (ke || ka) = KMACXOF256(Wx, “”, 1024, “P”)
	 m = KMACXOF256(ke, “”, |c|, “PKE”) xor c
	 t’ = KMACXOF256(ka, m, 512, “PKA”)
	 accept if, and only if, t’ = 
	 */
	public static void decryptElliptic(String filename, String pass) throws IOException, ClassNotFoundException {
	    ObjectInputStream ob = new ObjectInputStream(new FileInputStream(filename));
	    Cryptogram encryptogram = (Cryptogram) ob.readObject();
	    EllipticCurve z = encryptogram.getz();
	    byte[] c = encryptogram.getc();
	    byte[] t = encryptogram.gett();
	      
	    byte[] s = KMACXOF256.theKMACXOF256(pass.getBytes(), "".getBytes(), 512, "K".getBytes());
	    BigInteger sbig = BigInteger.valueOf(4).multiply(new BigInteger(s));

        EllipticCurve w = z.scalarMultiplication(sbig);
	    byte[] kellka = KMACXOF256.theKMACXOF256(w.getX().toByteArray(), "".getBytes(), 1024, "p".getBytes());
	    byte[] ke = Arrays.copyOfRange(kellka, 0, KINT);
	    byte[] ka = Arrays.copyOfRange(kellka, KINT, 128);
	        
	    byte[] m = KMACXOF256.theKMACXOF256(ke, "".getBytes(), c.length, "PKE".getBytes());
	    byte[] mxor = new byte[c.length];
	    for (int i = 0; i < m.length; i++) {
	        mxor[i] = (byte) (m[i] ^ c[i]);
	    }
	    byte[] t2 = KMACXOF256.theKMACXOF256(ka, m, 512, "PKA".getBytes());
	    if (Arrays.equals(t, t2)) {
	        System.out.println("File Successfully Decrypted");
	        StringBuilder hashedInput = new StringBuilder();
	        for (byte msg : mxor) {
	             int hex = msg & 0xFF;
	             hashedInput.append(HEXIDECIMAL[hex >>> 4]); 
	             hashedInput.append(HEXIDECIMAL[hex & 0x0F]); 
	        }
	           System.out.println("Cryptogram contains: " + hashedInput.toString());
	        } else {
	        	System.out.println("Incorrect Password");
	        }    
	}
}
