import java.math.BigInteger;
class Main {
    public static void main(String[] args) {
        /////////////////////////////////////
        //Test Section for LREncode:
        //byte[] bytes = LREncode.enc8(128);
        //System.out.println(bytes[0]);
        BigInteger myInt = new BigInteger("127");
        byte[] bytes = LREncode.rightEncode(myInt);
        ////////////////////////////////////

    }
}