import java.io.File;
import java.io.PrintStream;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

public class Main {
		
	/**
	 * Main function for this program
	 * 
	 * @param theArgs
	 */
    public static void main(String[] theArgs) {

// --------------------------------- Test Section -------------------------------- //
    	
        /////////////////////////////////////
        //Test Section for LREncode:
        //byte[] bytes = LREncode.enc8(128);
        //System.out.println(bytes[0]);
       // BigInteger myInt = new BigInteger("127");
        int myInt = 127;
        byte[] bytes = LREncode.right_Encode(myInt);
        //for ()
        ////////////////////////////////////

// --------------------------------- Main Section -------------------------------- //
        
        Scanner input = new Scanner(System.in);
        PrintStream output = null;
        boolean exitProgram = false;
        
        while (!exitProgram) {
        	displayMenu();
        	
        	int choice = input.nextInt();
        	
        	File selectedFile = null;
        	
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
                			break;
                			
                		// Message for Cryptographic Hash
                		case 2:
                			System.out.println("Enter your message: ");
                			input.nextLine();
                			String userInput = input.nextLine();
                			byte[] message = userInput.getBytes();
                			System.out.println("Cryptographic Hash Result: " + message);
                			
                			break;
                			
                		// Encrypt a Data File
                		case 3:
                			System.out.println("Select file to encrypt: ");
                			JFileChooser encryptFile = new JFileChooser(FileSystemView.getFileSystemView());
                			
                			int encryptValue = encryptFile.showOpenDialog(null);
                			
                			if (encryptValue == JFileChooser.APPROVE_OPTION) {
                				selectedFile = encryptFile.getSelectedFile();
                				Path filePath = Paths.get(selectedFile.getAbsolutePath());
                				System.out.println("Encrypted file is saved to: " + filePath.toString());
                			}
                			
                			break;
                			
                		// Decrypt a Data File
                		case 4:
                			System.out.println("Select file to decrypt: ");
                			JFileChooser decryptFile = new JFileChooser(FileSystemView.getFileSystemView());
                			
                			int decryptValue = decryptFile.showOpenDialog(null);
                			
                			if (decryptValue == JFileChooser.APPROVE_OPTION) {
                				selectedFile = decryptFile.getSelectedFile();
                				Path filePath = Paths.get(selectedFile.getAbsolutePath());
                				System.out.println("Decrypted file is saved to: " + filePath.toString());
                			}
                			
                			break;
                		
                		// Compute an Authentication Tag (MAC)
                		case 5:
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
	            			break;
	            			
	            		// Encrypt Private Key
	            		case 2:
	            			break;
	            			
	            		// Encrypt a Data File
	            		case 3:
	            			System.out.println("Select file to encrypt: ");
                			JFileChooser encryptFile = new JFileChooser(FileSystemView.getFileSystemView());
                			
                			int encryptValue = encryptFile.showOpenDialog(null);
                			
                			if (encryptValue == JFileChooser.APPROVE_OPTION) {
                				selectedFile = encryptFile.getSelectedFile();
                				Path filePath = Paths.get(selectedFile.getAbsolutePath());
                				System.out.println("Encrypted file is saved to: " + filePath.toString());
                			}
                			
	            			break;
	            			
	            		// Decrypt an Elliptic-encrypted File
	            		case 4:
	            			System.out.println("Select file to decrypt: ");
                			JFileChooser decryptFile = new JFileChooser(FileSystemView.getFileSystemView());
                			
                			int decryptValue = decryptFile.showOpenDialog(null);
                			
                			if (decryptValue == JFileChooser.APPROVE_OPTION) {
                				selectedFile = decryptFile.getSelectedFile();
                				Path filePath = Paths.get(selectedFile.getAbsolutePath());
                				System.out.println("Decrypted file is saved to: " + filePath.toString());
                			}
                			
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
}