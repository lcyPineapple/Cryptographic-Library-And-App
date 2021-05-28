/** This File contains the methods bytepad and encodeString.
*
* @author 
* @date 5/2/2021
*
**/

import java.util.Arrays;

public class bytepadES {

    public static byte[] bytepad(byte[] str, int w) {
        
        byte[] leftEncode = LREncode.left_Encode(w);

        int total = leftEncode.length + str.length;
        int len = total + (w - (total) % w);

        byte[] output = Arrays.copyOf(leftEncode, len);
        System.arraycopy(str, 0, output, leftEncode.length, str.length);

        return output;
    }

    public static byte[] encode_String(byte[] str) {
        int strLen = 0;
        byte[] leftEncode = LREncode.left_Encode(str.length * 8);

        strLen = str.length;

        byte[] output = Arrays.copyOf(leftEncode, leftEncode.length + strLen);
        System.arraycopy(str, 0, output, leftEncode.length, strLen);
        
        return output;
    }
    
}
