/** This File contains the methods bytepad and encodeString
 * according to the NIST Specification.
 * bytepad(X, w):
 * 		Validity Conditions: w > 0
 * 			1. z = left_encode(w) || X
 * 			while len(z) mod 8 != 0:
 * 				z = z || 0
 * 			while (len(z) / 8) mode w != 0:
 * 				z = z || 00000000
 * 			return z
 * 
 * encode_string(S):
 * 		Validity Conditions: 0 <= len(S) || S
 * 			1. Return left_encode(len(S) || S)
 *
 * @author Melinda Tran
 * @date 5/2/2021
 *
 **/

import java.util.Arrays;

public class bytepadES {

	/**
	 * Prepends an encoding of integer to a string, then pads with zero
	 * @param str bit string to be padded
	 * @param w integer used to pad string
	 * @return the byte padded bit string
	 */
    public static byte[] bytepad(byte[] str, int w) {
        
        byte[] leftEncode = LREncode.left_Encode(w);

        int total = leftEncode.length + str.length;
        int len = total + (w - (total) % w);

        byte[] output = Arrays.copyOf(leftEncode, len);
        System.arraycopy(str, 0, output, leftEncode.length, str.length);

        return output;
    }

    /**
     * Encode bit string to parsed unambiguously
     * @param str bit string to be encoded
     * @return the encoded bit string
     */
    public static byte[] encode_String(byte[] str) {
        int strLen = 0;
        byte[] leftEncode = LREncode.left_Encode(str.length * 8);

        strLen = str.length;

        byte[] output = Arrays.copyOf(leftEncode, leftEncode.length + strLen);
        System.arraycopy(str, 0, output, leftEncode.length, strLen);
        
        return output;
    }
    
}
