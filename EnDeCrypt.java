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
}
