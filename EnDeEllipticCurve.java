import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Arrays;

public class EnDeEllipticCurve {
	
	public static final int KINT = 64; // 512 possible bits in byte[64] for KECCAK[512]

	public static void decryptFileElliptic(String fileName, String password) throws IOException {
		byte[] encryptedFile = Files.readAllBytes(Paths.get(fileName));
		byte[] myPassBytes = password.getBytes();
		
		
	}
}
