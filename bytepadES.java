/** This File contains the methods bytepad and encodeString.
*
* @author 
* @date 5/2/2021
*
**/

import java.util.Arrays;

public class bytepadES {

    public static byte[] bytepad(byte[] str, int w) {
        
        byte[] leftEncode = LREncode.leftEncode(w); // Big integer vs int

        int total = leftEncode.length + str.length;
        int len = total + (w - (total) % w);

        byte[] output = Arrays.copyOf(leftEncode, len);
        System.arraycopy(str, 0, output, leftEncode.length, str.length);

        return output;
    }

    public static byte[] encodeString(byte[] str) {
        int strLen;
        byte[] leftEncode = LREncode.leftEncode(str.length * 8); // Big integer vs int

        if (str != null) {
            strLen = str.length;
        } else {
            strLen = 0;
        }

        byte[] output = Arrays.copyOf(leftEncode, leftEncode.length + strLen);
        System.arraycopy(str, 0, output, leftEncode.length, strLen);
        
        return output;
    }
    
}
