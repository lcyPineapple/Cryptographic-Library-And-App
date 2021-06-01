/**
 * This File the high level implementation of cSHAKE256
 * according to the NIST Specification.
 * cSHAKE256(X, L, N, S):
 * 				Validity Conditions: len(N) < 2^2040 and len(S) < 2^2040
 * 				1. If N = "" and S = "":
 * 					return SHAKE256(X, L);
 * 				2. Else:
 * 					return KECCAK[512] (bytepad(encode_string(N) || encode_string(S), 136) || X || 00, L)
 * 
 * @author Melinda Tran
 * @date 5/30/2021
 *
 */

import java.util.Arrays;


public class cSHAKE256 {

	/**
	 * Implementation of the cSHAKE256
	 * @param X the main input bit string
	 * @param L the integer representing requested output length in bits
	 * @param N the function name in string
	 * @param S the customization string
	 * @return call SHAKE or return KECCAK[512]
	 */
	public static byte[] thecSHAKE256(byte[] X, int L, String N, String S) {
		
		if (N.equals("") && S.equals("")) {
			return theSHAKE256(X, L);
			
		} else {
			
			byte[] encodeN = bytepadES.encode_String(N.getBytes());
			byte[] encodeS = bytepadES.encode_String(S.getBytes());
			
			byte[] message = Arrays.copyOf(encodeN, encodeN.length + encodeS.length);
			System.arraycopy(encodeS, 0, message, encodeN.length, encodeS.length);
			
			byte[] mybytepad = bytepadES.bytepad(message, 136);
			message = Arrays.copyOf(mybytepad, mybytepad.length + X.length);
			System.arraycopy(X, 0, message, mybytepad.length, X.length);
			
			byte[] newbyte = new byte[] {0x04};
			message = Arrays.copyOf(message, message.length + newbyte.length);
			System.arraycopy(newbyte, 0, message, message.length, newbyte.length);
			
<<<<<<< Updated upstream
			KECCAK keccak_512 = new KECCAK();
			keccak_512.sha3_init(32);
			keccak_512.sha3_update(message, L);
			keccak_512.shake_xof();
			keccak_512.sha3_final(message);
			
			return message;
			//return KECCAK(message, L, 512);
=======
>>>>>>> Stashed changes
		}
	}
	
	/**
	 * Implementation of the SHAKE256
	 * @param X the main input bit string
	 * @param L the integer representing requested output length in bits
	 * @return return KECCAK[512]
	 */
	public static byte[] theSHAKE256(byte[] X, int L) {
		byte[] message = Arrays.copyOf(X, X.length + 1);
		int bytetopad = 136 - X.length % (136);
		
		if (bytetopad == 1) {
			message[X.length] = (byte) 0x9f;
		} else {
			message[X.length] = (byte) 0x1f;
		}
		
		KECCAK keccak_512 = new KECCAK();
		keccak_512.sha3_init(32);
		keccak_512.sha3_update(message, L);
		keccak_512.shake_xof();
		keccak_512.sha3_final(message);
		
		//return KECCAK(message, L, 512);
		return message;
	}
}
