import java.math.BigInteger;

/**
 * 
 * This class has methods to create a key pair, sign, and verify our schnorr signatures.
 * 
 * @author Jacob Sousie
 * 
 */

public class Schnorr
{
	/**
	 * Private key (mod q)
	 * 
	 */
	private BigInteger x;
	
	/**
	 * Public key (mod p)
	 * 
	 */
	private BigInteger y;

	
	// Parametres
	private BigInteger p;
	private BigInteger q;
	private BigInteger g;
	
	
	/**
	 * Sets parametres used in calculation
	 * 
	 * @param p
	 * @param q
	 * @param g
	 */
	public void Intconfig ( BigInteger p, BigInteger q, BigInteger g )
	{
		this.p = p;
		this.q = q;
		this.g = g;
		
	}
}