import java.util.Arrays;

/**This File the high level implementation of KMACXOF256
 * according to the NIST Specification.
 * KMACXOF256(K, X, L, S):
 *		Validity Conditions: len(K) <22040 and 0 ≤ L and len(S) < 22040
 *		1. newX = bytepad(encode_string(K), 136) || X || right_encode(0).
 *		2. return cSHAKE256(newX, L, “KMAC”, S). 
 *
 * @author Leika Yamada
 * @date 5/28/2021
 * */
public class KMACXOF256 {
	
	/* This method right encodes large integers
	   *  into a byte array.
	   *  @param k String k
	   *  @param m byte[] m
	   *  @param L int L
	   *  @param S String S
	   *  @return byte[] the byte string generated by the integer
	   * */
	public static byte[] theKMACXOF256(byte[] k, byte[] m, int L, byte[] S) {
		byte[] kmac = {(byte)0x4B, (byte)0x4D, (byte)0x41, (byte)0x43}; //"KMAC"
		byte[]  kmax256 = new byte[L >>> 3];
	    byte[] encK = bytepadES.bytepad(bytepadES.encode_String(k), 136);
	    KECCAK keccak_512 = new KECCAK();
	    keccak_512.sha3_init(32);
	    if (S.length != 0) {
	    	byte[] encodeN = bytepadES.encode_String(kmac);
			byte[] encodeS = bytepadES.encode_String(S);
			
			byte[] message = Arrays.copyOf(encodeN, encodeN.length + encodeS.length);
			System.arraycopy(encodeS, 0, message, encodeN.length, encodeS.length);
			
			byte[] mybytepad = bytepadES.bytepad(message, 136);
			keccak_512.sha3_update(mybytepad, mybytepad.length);
	    }
	    keccak_512.sha3_update(encK, encK.length);
	    keccak_512.sha3_update(m, m.length);
	    keccak_512.shake_xof();
	    keccak_512.sha3_sponge(kmax256, L >>> 3);
	    return kmax256;
    }
	
//	public static byte[] theKMACXOF256(byte[] k, byte[] m, int L, byte[] S) {
//		byte[] end = LREncode.right_Encode(0);
//		byte[] result = new byte[m.length + end.length];
//		System.arraycopy(m, 0, result, 0, m.length);
//		System.arraycopy(end, 0, result, m.length, end.length);
//		
//		byte[] mybytepad = bytepadES.bytepad(bytepadES.encode_String(k), 136);
//		byte[] newX = new byte[result.length + mybytepad.length];
//		System.arraycopy(mybytepad, 0, newX, 0, mybytepad.length);
//		System.arraycopy(result, 0, newX, mybytepad.length, result.length);
//		return cSHAKE256.thecSHAKE256(newX, L, "KMAC", S);
//		return end; 
//	}
	
}
