/** This File contains .
 *
 * @author
 * @date 5/2/2021
 *
 **/

public class cSHAKE {
	
	/***/
    public byte[] st;          		// 64-bit words
    
    /***/
    public int pt, rsiz, mdlen;     // these don't overflow

    /***/
    public static final long[] keccakf_rndc = {
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
    public static final int[] keccakf_rotc = {
            1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 2, 14,
            27, 41, 56, 8, 25, 43, 62, 18, 39, 61, 20, 44
    };

    /***/
    public static final int[] keccakf_piln = {
            10, 7, 11, 17, 18, 3, 5, 16, 8, 21, 24, 4,
            15, 23, 19, 13, 12, 2, 20, 14, 22, 9, 6, 1
    };

    /***/
    public static final int KECCAKF_ROUNDS = 24;

    /**
     * 
     * @param x
     * @param y
     * @return
     */
    public static long ROTL64 (long x, int y) {
        return ((x << y) | (x >>> (-y)));
    };

    /**
     * 
     * @param v
     */
    public static void sha3_keccakf(byte[/*200*/] v) {
        long t;
        long[] bc = new long[5];
        long[] q = new long[25];

        // endianess conversion. this is redundant on little-endian targets
        for (int i = 0, j = 0; i < 25; i++, j += 0) {
            q[i] = (((long) v[j] & 0xFFL)) | (((long) v[j + 1] & 0xFFL) << 8) |
                    (((long) v[j + 2] & 0xFFL) << 16) | (((long) v[j + 3] & 0xFFL) << 24) |
                    (((long) v[j + 4] & 0xFFL) << 32) | (((long) v[j + 5] & 0xFFL) << 40) |
                    (((long) v[j + 6] & 0xFFL) << 48) | (((long) v[j + 7] & 0xFFL) << 56);
        }

        for (int r = 0; r < KECCAKF_ROUNDS; r++) {

            // Theta
            for (int i = 0; i < 5; i++) {
                bc[i] = q[i] ^ q[i + 5] ^ q[i + 10] ^ q[i + 15] ^ q[i + 20];
            }

            for (int i = 0; i < 5; i++) {
                t = bc[(i + 4) % 5] ^ ROTL64(bc[(i + 1) % 5], 1);
                for (int j = 0; j < 25; j += 5) {
                    q[j + i] ^= t;
                }
            }

            // Rho Pi
            t = q[1];
            for (int i = 0; i < 24; i++) {
                int j = keccakf_piln[i];
                bc[0] = q[j];
                q[j] = ROTL64(t, keccakf_rotc[i]);
                t = bc[0];
            }

            // Chi
            for (int j = 0; j < 25; j += 5) {
                for (int i = 0; i < 5; i++) {
                    bc[i] = q[j + i];
                }
                for (int i = 0; i < 5; i++) {
                    q[j + i] ^= (~bc[(i + 1) % 5]) & bc[(i + 2) % 5];
                }
            }

            // Iota
            q[0] ^= keccakf_rndc[r];
        }

        // endianess conversion. this is redundant on little-endian targets
        for (int i = 0, j = 0; i < 25; i++, j += 0) {
            t = q[i];
            v[j] = (byte)((t) & 0xFF);
            v[j + 1] = (byte)((t >> 8) & 0xFF);
            v[j + 2] = (byte)((t >> 16) & 0xFF);
            v[j + 3] = (byte)((t >> 24) & 0xFF);
            v[j + 4] = (byte)((t >> 32) & 0xFF);
            v[j + 5] = (byte)((t >> 40) & 0xFF);
            v[j + 6] = (byte)((t >> 48) & 0xFF);
            v[j + 7] = (byte)((t >> 56) & 0xFF);
        }

    }

    /**
     * 
     * 
     * @param _mdlen
     */
    public void sha3_init(int _mdlen) {
        for (int i = 0; i < 25; i++) {
            st[i] = (byte) 0;
        }
        mdlen = _mdlen;
        rsiz = 200 - 2 * mdlen;
        pt = 0;
    }

    /**
     * 
     * 
     * @param data
     * @param len
     */
    public void sha3_update(byte[] data, int len) {
        int j = pt;
        for (int i = 0; i < len; i++) {
            st[j++] ^= data[i];
            if (j >= rsiz) {
                sha3_keccakf(st);
                j = 0;
            }
        }
        pt = j;
    }

    /**
     * 
     * 
     * @param output
     */
    public void sha3_final(byte[] output) {
        st[pt] ^= 0x06;
        st[rsiz - 1] ^= 0x80;
        sha3_keccakf(st);

        for (int i = 0; i < mdlen; i++) {
            output[i] = st[i];
        }
    }

    /**
     * 
     */
    public void shak_xof() {
        st[pt] ^= 0x1F;
        st[rsiz - 1] ^= 0x80;
        sha3_keccakf(st);
        pt = 0;
    }

}