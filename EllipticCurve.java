import java.math.BigInteger;

public class EllipticCurve {

	/** The x and y coordinates for the elliptic curve point */
	public final BigInteger myX, myY;
	
	/** The string representation that will be used to compute the R value */
	public static final String myRString = "337554763258501705789107630418782636071904961214051226618635150085779108655765";
	
	/** The value use in the curve equation to implement the E_521 curve */
	public static final BigInteger myDValue = BigInteger.valueOf(-376014);
	
	/** The number of points n = 4r which is use for E_521 and computing Schnorr signatures */
	public static final BigInteger myRValue = BigInteger.valueOf(2L).pow(519).subtract(new BigInteger(myRString));
	
	/** A Mersenne prime */
	public static final BigInteger myPValue = BigInteger.valueOf(2L).pow(521).subtract(BigInteger.ONE);
	
	/**
	 * The neutral element of addition
	 * Based on project specifications
	 */
	public EllipticCurve() {
		myX = BigInteger.ZERO;
		myY = BigInteger.ONE;
	}
	
	/**
	 * Initialize a point on the curve given x and y coordinates
	 * @param theX X coordinate to be initialize
	 * @param theY Y coordinate to be initialize
	 */
	public EllipticCurve(BigInteger theX, BigInteger theY) {
		myX = theX;
		myY = theY;
	}
	
	/**
	 * Initialize a point on the curve given X and the least
	 * significant bit value
	 * @param theX X coordinate to be initialize
	 * @param theLeastSignBit the least significant bit used to calculate the Y coordinates
	 */
	public EllipticCurve(BigInteger theX, boolean theLeastSignBit) {
		// numerator = 1 - x^2
		BigInteger numerator = BigInteger.ONE.subtract(theX.pow(2));
		// denominator = 1 - d * x^2
		BigInteger denominator = BigInteger.ONE.subtract(theX).pow(2);
		// result = sqrt(numerator / denominator) mod p
		BigInteger valueY = sqrt(numerator.multiply(denominator.modInverse(myPValue)), theLeastSignBit);
		valueY.mod(myPValue);
		
		myX = theX;
		myY = valueY;
	}
	
	/**
	 * Get the myX
	 * @return myX value
	 */
	public BigInteger getX() {
		return myX;
	}
	
	/**
	 * Get the myY
	 * @return myY value
	 */
	public BigInteger getY() {
		return myY;
	}
	
	/**
	 * Negate the elliptic curve point
	 * @return the elliptic curves that's been negated
	 */
	public EllipticCurve oppositePoint() {
		BigInteger oppositeX;
		if (myX == BigInteger.ZERO) {
			oppositeX = myX;
		} else {
			oppositeX = myX.negate();
		}
		
		EllipticCurve result = new EllipticCurve(oppositeX, myY);
		
		return result;
	}
	
	/**
	 * Multiplication by scalar that invoke the Edwards point addition formula
	 * Based off assignment specifications
	 * @param theScalar value used to multiple elliptic curve point
	 * @return the new elliptic curve point that's been scalar multiplied
	 */
	public EllipticCurve scalarMultiplication(BigInteger theScalar) {
		EllipticCurve V = new EllipticCurve(myX, myY);
		for (int i = theScalar.bitLength() - 1; i >= 0; i--) {
			V = V.computeSum(V);
			if (theScalar.testBit(i)) {
				V = V.computeSum(this);
			}
		}
		return V;
	}
	
	/**
	 * 
	 * @param theCoordinates
	 * @return
	 */
	public EllipticCurve computeSum(EllipticCurve theCoordinates) {
		// base = (x_1 * x_2 * y_1 * y_2) mod p
		BigInteger base = myX.multiply(theCoordinates.myX).multiply(myY.multiply(theCoordinates.myY)).mod(myPValue);
		
		// numeratorX = (x_1 * y_2 + y_1 * x_2) mod p
		BigInteger numeratorX = myX.multiply(theCoordinates.myY).add(myY.multiply(theCoordinates.myX)).mod(myPValue);
		// denominatorX = (1 + (d * base)) mod p
		BigInteger denominatorX = BigInteger.ONE.add(myDValue.multiply(base)).mod(myPValue);
		// multiplyX = (numeratorX / denominatorX) mod P
		BigInteger resultX = numeratorX.multiply(denominatorX.modInverse(myPValue)).mod(myPValue);
				
		// numeratorY = (y_1 * y_2 - x_1 * x_2) mod p
		BigInteger numeratorY = myY.multiply(theCoordinates.myY).add(myX.multiply(theCoordinates.myX)).mod(myPValue);
		// denominatorY = (1 - (d * base)) mod p
		BigInteger denominatorY = BigInteger.ONE.subtract(myDValue.multiply(base)).mod(myPValue);
		// multiplyY = (numeratorY / denominatorY) mod P
		BigInteger resultY = numeratorY.multiply(denominatorY.modInverse(myPValue)).mod(myPValue);

		EllipticCurve result = new EllipticCurve(resultX, resultY);
		return result;
	}
	
	/**
	 * Compute a square root of v mod p with a specified
	 * least significant bit, if such a root exists.
	 * Based off assignment specifications
	 * @param theV the radicand.
	 * @param theLeastSignBit desired least significant bit (true: 1, false: 0).
	 * @return square root if root exist, otherwise null
	 */
	public BigInteger sqrt(BigInteger theV, boolean theLeastSignBit) {
		assert(myPValue.testBit(0) && myPValue.testBit(1)); // p = 3 (mod 4)
				
		if (theV.signum() == 0) {
			return BigInteger.ZERO;
		}
		
		BigInteger result = theV.modPow(myPValue.shiftRight(2).add(BigInteger.ONE), myPValue);

		if (result.testBit(0) != theLeastSignBit) {
			result = myPValue.subtract(result); // correct the lsb
		}
		
		int squareResult = result.multiply(result).subtract(theV).mod(myPValue).signum();
		return (squareResult == 0) ? result : null;
	}
	
	/**
     * Compare points for equality 
     * @param o the object to compare
     * @return a boolean of the compared points
     */
	public boolean equals(Object o) {
		EllipticCurve other = (EllipticCurve) o;
		return (this.myX.equals(other.myX) && this.myY.equals(other.myY));
	}
	
	/**
     * Write the string representation of the elliptic curve points
     * @return a string representation: (X, Y)
     */
	public String toString() {
		return "(" + this.myX.toString() + ", " + this.myY.toString() + ")";
	}
	
}
