/** This File contains the methods bytepad and encodeString.
*
* @author Melinda Tran
* @date 5/2/2021
*
**/

import java.lang.reflect.Array;

public class bytepadES {

    public static byte[] bytepad(byte[] str, int w) {
        if (w > 0) {
            bytes[] left = LREncode.leftEncode(w);
            while (left.length mod 8 != 0) {
                left = 0;
            }

            while ((left.length / 8) mod 8 != 0) {
                left = 00000000;
            }
        }
        return left;
    }

    public static byte[] encodeString(byte[] str) {
        if (s.length >= 0 or s.length < 22040) {
            return LREncode.rightEncode(s);
        }
        //return s;
    }
    
}
