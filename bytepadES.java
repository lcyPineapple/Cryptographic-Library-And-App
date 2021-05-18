/** This File contains the methods bytepad and encodeString.
*
* @author Melinda Tran
* @date 5/2/2021
*
**/

import java.lang.reflect.Array;

public class bytepadES {

    public static byte[] bytepad(byte[] str, int w) {
        
        byte leftEncode = LREncode.leftEncode(w);

        int total = leftEncode.length + str.length;
        int len = total + (w - (total) % w);

        byte[] output = Array.copyOf(leftEncode, len);
        System.arraycopy(str, 0, output, leftEncode.length, str.length);

        return output;
    }

    public static byte[] encodeString(byte[] str) {
        int strLen;
        byte[] leftEncode = LREncode.leftEncode(str.length * 8);

        if str != NULL {
            strLen = str.length;
        } else {
            strLen = 0;
        }

        byte output = Array.copyOf(leftEncode, leftEncode + strLen);
        System.arraycopy(str, 0, output, leftEncode.length, strLen);
    }
    
}
