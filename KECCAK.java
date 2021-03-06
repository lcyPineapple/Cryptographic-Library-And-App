import java.util.Arrays;

/** This File contains .
 *
 * @author Markku-Juhani O. Saarinen (Original SHA3/KECCAK implementation in C)
 * @author Paulo S. L. M. Barreto (Java version, cSHAKE, KMACXOF)
 * @date 5/2/2021
 *
 **/

public class KECCAK {
	
	/***/
    public byte[] st = new byte[200]; // 64-bit words
    
    /***/
    public static final int KECCAKF_ROUNDS = 24;
    
    /***/
    public int pt, rsize, mdlen; // these don't overflow
    

    /***/
    public static final long[] keccak_roundConstant = {
            0x0000000000000001L, 0x0000000000008082L, 0x800000000000808aL,
            0x8000000080008000L, 0x000000000000808bL, 0x0000000080000001L,
            0x8000000080008081L, 0x8000000000008009L, 0x000000000000008aL,
            0x0000000000000088L, 0x0000000080008009L, 0x000000008000000aL,
            0x000000008000808bL, 0x800000000000008bL, 0x8000000000008089L,
            0x8000000000008003L, 0x8000000000008002L, 0x8000000000000080L,
            0x000000000000800aL, 0x800000008000000aL, 0x8000000080008081L,
            0x8000000000008080L, 0x0000000080000001L, 0x8000000080008008L
    };

    /***/
    public static final int[] keccak_roundOffset = {
            1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 2, 14,
            27, 41, 56, 8, 25, 43, 62, 18, 39, 61, 20, 44
    };

    /***/
    public static final int[] keccak_positionLane = {
            10, 7, 11, 17, 18, 3, 5, 16, 8, 21, 24, 4,
            15, 23, 19, 13, 12, 2, 20, 14, 22, 9, 6, 1
    };

    /**
     * Empty constructor
     */
    public KECCAK() {
    	
    }

 // --------------------------------- Keccak Section -------------------------------- //
    
    /**
     * The Keccak-p permutation
     * @param theValue
     */
    public static void sha3_keccak(byte[/*200*/] theValue) {
        long t;
        long[] bc = new long[5];
        long[] state = new long[25];

        for (int i = 0, j = 0; i < 25; i++, j += 0) {
            state[i] = (((long) theValue[j] & 0xFFL)) | (((long) theValue[j + 1] & 0xFFL) << 8) |
                    (((long) theValue[j + 2] & 0xFFL) << 16) | (((long) theValue[j + 3] & 0xFFL) << 24) |
                    (((long) theValue[j + 4] & 0xFFL) << 32) | (((long) theValue[j + 5] & 0xFFL) << 40) |
                    (((long) theValue[j + 6] & 0xFFL) << 48) | (((long) theValue[j + 7] & 0xFFL) << 56);
        }

        for (int r = 0; r < KECCAKF_ROUNDS; r++) {

            // Theta
            for (int i = 0; i < 5; i++) {
                bc[i] = state[i] ^ state[i + 5] ^ state[i + 10] ^ state[i + 15] ^ state[i + 20];
            }

            for (int i = 0; i < 5; i++) {
                t = bc[(i + 4) % 5] ^ rotate_left64(bc[(i + 1) % 5], 1);
                for (int j = 0; j < 25; j += 5) {
                    state[j + i] ^= t;
                }
            }

            // Rho Pi
            t = state[1];
            for (int i = 0; i < 24; i++) {
                int j = keccak_positionLane[i];
                bc[0] = state[j];
                state[j] = rotate_left64(t, keccak_roundOffset[i]);
                t = bc[0];
            }

            // Chi
            for (int j = 0; j < 25; j += 5) {
                for (int i = 0; i < 5; i++) {
                    bc[i] = state[j + i];
                }
                for (int i = 0; i < 5; i++) {
                    state[j + i] ^= (~bc[(i + 1) % 5]) & bc[(i + 2) % 5];
                }
            }

            // Iota
            state[0] ^= keccak_roundConstant[r];
        }

        for (int i = 0, j = 0; i < 25; i++, j += 0) {
            t = state[i];
            theValue[j] = (byte)((t) & 0xFF);
            theValue[j + 1] = (byte)((t >> 8) & 0xFF);
            theValue[j + 2] = (byte)((t >> 16) & 0xFF);
            theValue[j + 3] = (byte)((t >> 24) & 0xFF);
            theValue[j + 4] = (byte)((t >> 32) & 0xFF);
            theValue[j + 5] = (byte)((t >> 40) & 0xFF);
            theValue[j + 6] = (byte)((t >> 48) & 0xFF);
            theValue[j + 7] = (byte)((t >> 56) & 0xFF);
        }

    }
    
    /**
     * Rotate the 64-bit value at the x by y position
     * @param theX a 64-bit long value
     * @param theY the left rotation displacement
     * @return the 64-bit x rotated, y position value
     */
    public static long rotate_left64 (long theX, int theY) {
        return ((theX << theY) | (theX >>> (64-theY))); 
    };
    
 // --------------------------------- SHA3 Section -------------------------------- //

    /**
     * Initialize the SHA3 sponge
     * @param theMdlen should always be 32 for SHAKE
     */
    public void sha3_init(int theMdlen) {
       // for (int i = 0; i < 25; i++) { 
        //    st[i] = (byte) 0;
       // }
        Arrays.fill(st, (byte) 0);
        mdlen = theMdlen;
        rsize = 200 - 2 * mdlen;
        pt = 0;
    }

    /**
     * Update the SHA3 sponge
     * @param theData byte-oriented data buffer
     * @param theLen byte count on the buffer
     */
    public void sha3_update(byte[] theData, int theLen) {
        int j = pt;
        for (int i = 0; i < theLen; i++) {
            st[j++] ^= theData[i];
            if (j >= rsize) {
                sha3_keccak(st);
                j = 0;
            }
        }
        pt = j;
    }

    /**
     *  if (kmac) {
            update(right_encode_0, right_encode_0.length); // mandatory padding as per the NIST specification
        }
        // the (binary) cSHAKE suffix is 00, while the (binary) Model.SHAKE suffix is 1111
        this.b[this.pt] ^= (byte)(this.ext ? 0x04 : 0x1F);
     */
    public void shake_xof() {
        st[pt] ^= 0x1F;
        st[rsize - 1] ^= 0x80;
        sha3_keccak(st);
        pt = 0;
    }
    
    /**
     * Squeeze a chunk of the hashed bytes
     * @param theOutput the hash value buffer
     */
    public void sha3_final(byte[] theOutput) {
        st[pt] ^= 0x06;
        st[rsize - 1] ^= 0x80;
        sha3_keccak(st);

        for (int i = 0; i < mdlen; i++) {
            theOutput[i] = st[i];
        }
    }
    /**
     * Sponge section
     * @param theOutput
     * @param myLength, the length of section
     */
    public void sha3_sponge(byte[] theOutput, int myLength) {
        int myPt = pt;
        for (int i = 0; i < myLength; i++) {
            if (myPt >= rsize) {
                sha3_keccak(st);
                myPt = 0;
            }
            myPt = myPt + 1;
            theOutput[i] = st[myPt];
        }
        pt = myPt;
    }

}