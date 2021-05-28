/** This File contains the methods Left and Right encode.
 * The methods take an integer value from 0 up to 2^2040 - 1
 * and encodes it as a byte array using the following pseudocode from
 * the NIST Specification.
 * 1. Let n be the smallest positive integer for which 2^8n > x.
 * 2. Let x1, x2,…, xn be the base-256 encoding of x satisfying:
 * x = ∑ 28(n-i)
 * xi, for i = 1 to n.
 * 3. Let Oi = enc8(xi), for i = 1 to n.
 * 4. Let On+1 = enc8(n).
 * 5. Return O = O1 || O2 || … || On || On+1
 *
 * @author Leika Yamada
 * @date 4/27/2021
 *
 * */
import java.math.BigInteger;
import java.lang.Math;
public class LREncode {
    /* This method right encodes infinetly large integers
    *  into a byte array.
    *  @param x BigInteger x, the integer to encode
    *  @return byte[] the byte string generated by the integer
    * */
    public static byte[] right_Encode(BigInteger x){
        int n = 0;
        if (x.bitLength() >= 2){
            n = x.bitLength()/8 + 1;

        }
        System.out.println(n);
        byte[] myArr = new byte[]{0};
        return myArr;
    }
    /* This method right encodes infinitly large integers
     *  into a byte array.
     *  @param x BigInteger x, the integer to encode
     *  @return byte[] the byte string generated by the integer
     * */
    public static byte[] left_Encode(BigInteger x){
        byte[] myArr = new byte[]{0};
        return myArr;
    }
    /* This method encodes an integer from 0 - 255 to a byte
     *  @param i int i, the integer to encode
     *  @return byte[] the byte representation of the integer
     * */
    public static byte[] enc8(int i){
        byte[] myArr = new byte[]{(byte)i};
        return myArr;
    }
}

