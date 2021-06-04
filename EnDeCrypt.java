import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Arrays;

/**This class handles the encryption and decryption of files, and 
 * input and prepares output files and all console output.
 *
 * @author Leika Yamada
 * @date 6/01/2021
 * */
public class EnDeCrypt {
	
	public static final int KINT = 64; // 512 possible bits in byte[64] for KECCAK[512]
	public static final char[] HEXIDECIMAL = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	
	/**
	 * Extra Credit:
	 * Compute an authentication tag t of a byte array m under passphrase pw:
	 * t = KMACXOF256(pw, m, 512, “T”)
	 * @param myText, the text we want to hash
	 * @throws IOException 
	 */
	public static void hash(String password, String fileName) throws IOException {
		byte[] fileInput = Files.readAllBytes(Paths.get(fileName));
		
		byte[] myhashedBytes = KMACXOF256.theKMACXOF256(password.getBytes(), fileInput, 512, "T".getBytes());
	    StringBuilder hashedInput = new StringBuilder();
        for (byte msg : myhashedBytes) {
     	   int hex = msg & 0xFF;
            hashedInput.append(HEXIDECIMAL[hex >>> 4]); 
            hashedInput.append(HEXIDECIMAL[hex & 0x0F]); 
        }
        System.out.println("The authentication tag of your file is: " + hashedInput.toString());
	}
	
	/**
	 * Extra Credit:
	 * Computing a cryptographic hash h of a byte array m:
	 * Using text input from the user
	 * h = KMACXOF256(“”, m, 512, “D”)
	 * @param myText, the text we want to hash
	 */
	public static void hash(String myText) {
		byte[] myhashedBytes = KMACXOF256.theKMACXOF256("".getBytes(), myText.getBytes(), 512, "D".getBytes());
	    StringBuilder hashedInput = new StringBuilder();
        for (byte msg : myhashedBytes) {
     	   int hex = msg & 0xFF;
            hashedInput.append(HEXIDECIMAL[hex >>> 4]); 
            hashedInput.append(HEXIDECIMAL[hex & 0x0F]); 
        }
        System.out.println("The cryptographic hash of your input is: " + hashedInput.toString());
	}
	/**
	 * Computing a cryptographic hash h of a byte array m:
	 * Using a file input from the user
	 * h = KMACXOF256(“”, m, 512, “D”)
	 * @param myText, the text we want to hash
	 * @throws IOException 
	 */
	public static void hashFromFile(String fileName) throws IOException {
		byte[] fileInput = Files.readAllBytes(Paths.get(fileName));
		
		byte[] myhashedBytes = KMACXOF256.theKMACXOF256("".getBytes(), fileInput, 512, "D".getBytes());
	    StringBuilder hashedInput = new StringBuilder();
        for (byte msg : myhashedBytes) {
     	   int hex = msg & 0xFF;
            hashedInput.append(HEXIDECIMAL[hex >>> 4]); 
            hashedInput.append(HEXIDECIMAL[hex & 0x0F]); 
        }
        System.out.println("The cryptographic hash of your file is: " + hashedInput.toString());        
	}
	
	/**
	 * Symmetric encryption under a password according to document spec: 
	 * z = Random(512)
	 * (ke || ka) = KMACXOF256(z || pw, “”, 1024, “S”)
	 * c = KMACXOF256(ke, “”, |m|, “SKE”) xor m
	 * t = KMACXOF256(ka, m, 512, “SKA”)
	 * symmetric cryptogram: (z, c, t)
	 * @param fileName the file that contains data to encrypt
	 * @param password, the password to encrypt the data under
	 */
	public static void encryptFileSymetric(String fileName, String password) throws IOException{
		SecureRandom randNum = new SecureRandom();
		byte[] myRandBytes = new byte[KINT];
		randNum.nextBytes(myRandBytes);
		
		byte[] fileInput = Files.readAllBytes(Paths.get(fileName));
		byte[] myPassBytes = password.getBytes();
		
		byte[] pswd2 = new byte[myRandBytes.length + myPassBytes.length];
		System.arraycopy(myRandBytes, 0, pswd2, 0, myRandBytes.length);
		System.arraycopy(myPassBytes, 0, pswd2, myRandBytes.length, myPassBytes.length);
		
		byte[] kmax = Arrays.copyOfRange(KMACXOF256.theKMACXOF256(pswd2, "".getBytes(), 1024, "S".getBytes()), 0, KINT);
		byte[] kmax2 = KMACXOF256.theKMACXOF256(kmax, "".getBytes(), fileInput.length*8, "SKE".getBytes());
	    byte[] mixMessage = new byte[fileInput.length];
	    for (int i = 0; i < fileInput.length; i++) {
	    	mixMessage[i] = (byte) (kmax2[i] ^ fileInput[i]);
	    }
	    byte[] kmax3 = Arrays.copyOfRange(KMACXOF256.theKMACXOF256(pswd2, "".getBytes(), 1024, "S".getBytes()), KINT, 128);
	    byte[] kmax4 = KMACXOF256.theKMACXOF256(kmax3, fileInput, 512, "SKA".getBytes());

	    byte[] myOutput;
	    ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
	    streamOut.write(myRandBytes);
	    streamOut.write(mixMessage);
	    streamOut.write(kmax4);
	    myOutput = streamOut.toByteArray();
	    
		try {
		FileOutputStream writer = new FileOutputStream(fileName + ".txt");
		writer.write(myOutput);
		} catch (IOException outputException) {
			outputException.printStackTrace();
		}
        System.out.println("Successfully Encrypted.");
	}
	
	/**
	 *  Decrypting a symmetric cryptogram (z, c, t) under passphrase pw:
	 * (ke || ka) = KMACXOF256(z || pw, “”, 1024, “S”)
	 * m = KMACXOF256(ke, “”, |c|, “SKE”) xor c
	 * t’ = KMACXOF256(ka, m, 512, “SKA”)
	 * accept if, and only if, t’ = t
	 * @param fileName the file that contains data to encrypt
	 * @param password, the password to encrypt the data under
	 */
	public static void decryptFileSymetric(String fileName, String password) throws IOException{
	        byte[] encryptedFile = Files.readAllBytes(Paths.get(fileName));
	        byte[] myPassBytes = password.getBytes();
	        
	        byte[] zValue = Arrays.copyOfRange(encryptedFile, 0, KINT);
	        byte[] cValue = Arrays.copyOfRange(encryptedFile, KINT, encryptedFile.length - KINT);
	        byte[] tValue = Arrays.copyOfRange(encryptedFile, encryptedFile.length - KINT, encryptedFile.length);

	        byte[] pswd2 = new byte[zValue.length + myPassBytes.length];
			System.arraycopy(zValue, 0, pswd2, 0, zValue.length);
			System.arraycopy(myPassBytes, 0, pswd2, zValue.length, myPassBytes.length);
		
	        byte[] kellka = KMACXOF256.theKMACXOF256(pswd2, "".getBytes(), 1024, "S".getBytes());
	        byte[] mValue = KMACXOF256.theKMACXOF256(Arrays.copyOfRange(kellka, 0, KINT), "".getBytes(), cValue.length * 8, "SKE".getBytes());
	        byte[] mValuexorC = new byte[cValue.length];
	        for (int i = 0; i < cValue.length; i++) {
	            mValuexorC[i] = (byte) (cValue[i] ^ mValue[i]);
	        }
	        byte[] tValuePrime = KMACXOF256.theKMACXOF256(Arrays.copyOfRange(kellka, KINT, 128), mValuexorC, 512, "SKA".getBytes());
	        
	        if (Arrays.equals(tValuePrime, tValue)) {
	           StringBuilder decryptedFile = new StringBuilder();
	           for (byte msg : mValuexorC) {
	        	   int hex = msg & 0xFF;
	               decryptedFile.append(HEXIDECIMAL[hex >>> 4]);
	               decryptedFile.append(HEXIDECIMAL[hex & 0x0F]);
	           }
	           String myDecryption = decryptedFile.toString();
	           System.out.println("File is decrypted" + myDecryption);
	        } else {
	           System.out.println("Password is incorrect");
	        }
	    
	}
	
}
