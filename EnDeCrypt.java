import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;

/**This class handles the encryption and decryption of files, and 
 * input and prepares output files and all console output.
 *
 * @author Leika Yamada
 * @date 6/01/2021
 * */
public class EnDeCrypt {
	public static final int KINT = 64; // 512 possible bits in byte[64] for KECCAK[512]

	public static void encryptFileSymetric(String fileName, String password) throws IOException{
		SecureRandom randNum = new SecureRandom();
		byte[] myRandBytes = new byte[KINT];
		randNum.nextBytes(myRandBytes);
		byte[] myPassBytes = password.getBytes();
		
		byte[] pswd2 = new byte[myRandBytes.length + myPassBytes.length];
		System.arraycopy(myRandBytes, 0, pswd2, 0, myRandBytes.length);
		System.arraycopy(myPassBytes, 0, pswd2, myRandBytes.length, myPassBytes.length);
		
		//byte[] kmax = KMACXOF256.theKMACXOF256(pswd2, "".getBytes(), 1024, "S".getBytes());
		//
		
		byte[] fileInput;
		try {
			fileInput = Files.readAllBytes(Paths.get(fileName));
		} catch (IOException inputReadException) {
			inputReadException.printStackTrace();
		}
		byte[] passBytes = password.getBytes();
		byte[] myOutput;
		
		try {
		FileOutputStream writer = new FileOutputStream(fileName + ".txt");
		writer.write(myOutput);
		} catch (IOException outputException) {
			outputException.printStackTrace();
		}
        System.out.println("Successfully Encrypted.");
	}
	
 
}
