import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
		
	/**
	 * Main function for this program
	 * 
	 * @param theArgs
	 * @throws IOException 
	 */
    public static void main(String[] theArgs) throws IOException {
        
        Scanner input = new Scanner(System.in);
        boolean exitProgram = false;
        //boolean exitProgram = true;
        while (!exitProgram) {
        	displayMenu();
        	
        	int choice = input.nextInt();
        	        	
        	switch (choice) {
        	
        		// ************************************** //
        		// *		Symmetric Cryptography		* //
        		// ************************************** //
        	
        		// Symmetric Cryptography
        		case 1:
        			
        			input.nextLine();
        			
        			displaySymmetric();
                	int symmetricChoice = input.nextInt();
                	        			
                	switch (symmetricChoice) {
                		
                		// Cryptographic Hash
                		case 1:
                			
                			System.out.println("Enter the file path to compute plain cryptographic hash: ");
                			input.nextLine();
                			String hashFilePath = input.nextLine();
                			
                			boolean checkHashFile = isValidPath(hashFilePath);
                			while (checkHashFile == false) {
                				System.out.println("Please try again: ");
                				hashFilePath = input.nextLine();
                    			checkHashFile = isValidPath(hashFilePath);
                			}
                			
                			EnDeCrypt.hashFromFile(hashFilePath);
                			
                			break;
                			
                		// Message for Cryptographic Hash
                		case 2:
                			
                			System.out.println("Enter your message: ");
                			input.nextLine();
                			String userInput = input.nextLine();
                			EnDeCrypt.hash(userInput);
                			
                			break;
                			
                		// Encrypt a Data File
                		case 3:
                			
                			System.out.println("Enter the file path you want to encrypt: ");
                			input.nextLine();
                			String encryptFilePath = input.nextLine();
                			
                			boolean checkEncryptFile = isValidPath(encryptFilePath);
                			while (checkEncryptFile == false) {
                				System.out.println("Please try again: ");
                				encryptFilePath = input.nextLine();
                    			checkEncryptFile = isValidPath(encryptFilePath);
                			}
                			
                			System.out.println("Enter a passphrase: ");
                			String encryptPass = input.nextLine();
                			EnDeCrypt.encryptFileSymetric(encryptFilePath, encryptPass);

                			break;
                			
                		// Decrypt a Data File
                		case 4:
                			
                			System.out.println("Enter the file path you want to decrypt: ");
                			input.nextLine();
                			String decryptFilePath = input.nextLine();
                			
                			boolean checkDecryptFile = isValidPath(decryptFilePath);
                			while (checkDecryptFile == false) {
                				System.out.println("Please try again: ");
                				decryptFilePath = input.nextLine();
                				checkDecryptFile = isValidPath(decryptFilePath);
                			}
                			
                			System.out.println("Enter a passphrase: ");
                			String decryptPass = input.nextLine();
                			EnDeCrypt.decryptFileSymetric(decryptFilePath, decryptPass);

                			break;
                		
                		// Compute an Authentication Tag (MAC)
                		case 5:
                			
                			System.out.println("Enter the file path you want to compute an MAC (Must be exact): ");
                			input.nextLine();
                			String authFilePath = input.nextLine();
                			
                			boolean checkAuthFile = isValidPath(authFilePath);
                			while (checkAuthFile == false) {
                				System.out.println("Please try again: ");
                				authFilePath = input.nextLine();
                				checkAuthFile = isValidPath(authFilePath);
                			}
                			
                			System.out.println("Enter a passphrase: ");
                			String authPass = input.nextLine();
                			EnDeCrypt.hash(authPass, authFilePath);

                			break;
                			
                		// Exit Program
                		case 6:
                			exitProgram = true;
                			break;
                	}
                	
        			break;
        			
            	// ************************************** //
            	// *	  Elliptic Curve Arithmetic		* //
           		// ************************************** //
        			
        		// Generate Elliptic Curve Arithmetic
        		case 2:
        			
        			input.nextLine();
        			
        			displayElliptic();
                	int ellipticChoice = input.nextInt();
        			
                	switch (ellipticChoice) {
                	
                		// Elliptic Key Pair
	                	case 1:
	                		
	                		System.out.println("Enter a passphrase: ");
	                		input.nextLine();
	                		String keyPass = input.nextLine();
	                		EnDeEllipticCurve.generateKeyPairElliptic(keyPass);
	                		
	            			break;
	            			
	            		// Encrypt Private Key
	            		case 2:
	            			break;
	            			
	            		// Encrypt a Data File
	            		case 3:

	            			System.out.println("Enter the file path of your elliptic public key: ");
                			input.nextLine();
                			String encryptECFilePath = input.nextLine();
                			
                			boolean checkEncryptECFile = isValidPath(encryptECFilePath);
                			while (checkEncryptECFile == false) {
                				System.out.println("Please try again: ");
                				encryptECFilePath = input.nextLine();
                				checkEncryptECFile = isValidPath(encryptECFilePath);
                			}
                			
                			EnDeEllipticCurve.generateKeyPairElliptic(encryptECFilePath);
	            			
	            			break;
	            			
	            		// Decrypt an Elliptic-encrypted File
	            		case 4:
                			
	            			break;
	            			
	            		// Message to Encrypt/Decrypt
	            		case 5:
	            			
	            			input.nextLine();
	            			
	            			displayEncryptDecrypt();
	            			int encryptDecrypt  = input.nextInt();
	            			
	            			String userInput;
            				byte[] message;
            				
	            			switch (encryptDecrypt) {
	            			
	           	            	// Encrypt the message
		            			case 1:
		            				System.out.println("Enter your message: ");
		                			input.nextLine();
		                			userInput = input.nextLine();
		                			message = userInput.getBytes();
		                			System.out.println("Encrypted message: " + message);
		                			
		            				break;
		            				
		            			// Decrypt the message
		            			case 2:
		            				System.out.println("Enter your message: ");
		                			input.nextLine();
		                			userInput = input.nextLine();
		                			message = userInput.getBytes();
		                			System.out.println("Decrypted message: " + message);
		                			
		            				break;
		            				
		            			// Exit Program
		            			case 3:
		            				exitProgram = true;
		                			break;
	            			}
	            			
                			break;
                			
                		// Sign a Given File
	            		case 6:
                			break;
                			
                		// Verify a Data File
	            		case 7:
                			break;
                			
                		// Exit Program
                		case 8:
                			exitProgram = true;
                			break;
                	}
                	
        			break;
        			
        		// Exit Program
        		case 3:
        			exitProgram = true;
        			break;
        	}
        }
        input.close();
    }
    
	// ************************************** //
	// *			Console Menu			* //
	// ************************************** //
    
    /**
     * Display the main menu and what this application does
     */
    public static void displayMenu() {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append("\n---Main Menu Option---\n");
    	sb.append("\n");
    	sb.append("1) Symmetric Cryptography \n");
    	sb.append("2) Elliptic Curve Arithmetic \n");
    	sb.append("3) Exit Program \n");
    	sb.append("\n");
    	
    	System.out.print(sb.toString());
    	System.out.print("Enter an option: ");
    }
    
    /**
     * Display the Symmetric cryptography menu
     */
    public static void displaySymmetric() {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append("\n---Symmetric Menu Option---\n");
    	sb.append("\n");
    	sb.append("1) Cryptographic Hash File \n");
    	sb.append("2) Message for Cryptographic Hash \n");
    	sb.append("3) Encrypt a Data File \n");
    	sb.append("4) Decrypt a Data File \n");
    	sb.append("5) Compute an Authentication Tag (MAC) \n");
    	sb.append("6) Exit Program \n");
    	sb.append("\n");
    	
    	System.out.print(sb.toString());
    	System.out.print("Enter an option: ");
    }
    
    /**
     * Display the Elliptic curve arithmetic menu
     */
    public static void displayElliptic() {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append("\n---Elliptic Menu Option---\n");
    	sb.append("\n");
    	sb.append("1) Generate Elliptic Key Pair \n");
    	sb.append("2) Encrypt Private Key \n");
    	sb.append("3) Encrypt a Data File \n");
    	sb.append("4) Decrypt an Elliptic-encrypted File \n");
    	sb.append("5) Message to Encrypt/Decrypt \n");
    	sb.append("6) Sign a Given File \n");
    	sb.append("7) Verify a Data File \n");
    	sb.append("8) Exit Program \n");
    	sb.append("\n");
    	
    	System.out.print(sb.toString());
    	System.out.print("Enter an option: ");
    }
    
    /**
     * Display the option to encrypt/decrypt a message
     */
    public static void displayEncryptDecrypt() {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append("\n---Encrypt/Decrypt Menu Option---\n");
    	sb.append("\n");
    	sb.append("1) Encrypt the message \n");
    	sb.append("2) Decrypt the message \n");
    	sb.append("3) Exit Program \n");
    	sb.append("\n");
    	
    	System.out.print(sb.toString());
    	System.out.print("Enter an option: ");
    }
    
    /**
     * Checks for a valid file path
     * @param path the file path to be check
     * @return true if exist, false otherwise
     */
    public static boolean isValidPath(String thePath) {
    	try {
    		Files.readAllBytes(Paths.get(thePath));
    	} catch (Exception e) {
    		return false;
    	}
    	return true;
    }
}