import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * @Authors Tyler, Matt, Daniel
 * @Date_Updated 11/29/17
 * @Model_Used Strategy
 * 
 * This is used for Asymmetric Key. The plan is to have the user be able to choose what type of encryption / decryption
 * they want to use and that one is implemented without having to have all the different forms.
 */
public class AsymmetricKey implements FileCryptoInterface
{
	//Initializing the cipher
    private Cipher cipher;

    /**
     * Constructor that sets the instance of the cipher to be RSA
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    public AsymmetricKey() throws NoSuchAlgorithmException, NoSuchPaddingException
    {
        cipher = Cipher.getInstance("RSA");
    }

    /**
     * This method encrypts the file given to it and stores the file in a set location
     * @param file (File to encrypt)
     * @param algorithm (Not currently used)
     * @param keys (The object that contains the keys)
     * @return Encrypted File (In case we want to display it to the user as well)
     */
    @Override
    public File fileEncryptor(File file, String algorithm, StoredKeys keys) 
    {
        //This is to get the input file so we can save the output file in the same directory with a different name
        String fileName = file.getPath();
        int ext = fileName.lastIndexOf(".");

        //Creates the file where the encrepted message will be stored
        File encryptFile = new File("Messages/Encrypted-RSA" + fileName.substring(ext));

        //This makes the directory so the file can be stored
        encryptFile.getParentFile().mkdirs();
        
        //Initializing the byte array
        byte[] fileBytes;
		try
		{
			//Makes the file an array of bytes
			fileBytes = getFileInBytes(file);
			
			//Encrypts the file
			encryptFile(fileBytes, encryptFile, keys.getPublicKey());
		} catch (IOException | GeneralSecurityException e)
		{
			e.printStackTrace();
		}
		
		//Returns the encrypted file
        return encryptFile;
    }

    /**
     * This method decrypts the file given to it and stores the file in a set location
     * @param file (File to decrypt)
     * @param algorithm (Not currently used)
     * @param keys (The object that contains the keys)
     * @return Decrypted File (In case we want to display it to the user as well)
     */
    @Override
    public File fileDecryptor(File file, String algorithm, StoredKeys keys) 
    {
        //This is to get the input file so we can save the output file in the same directory with a different name
        String fileName = file.getPath();
        int ext = fileName.lastIndexOf(".");

        //Creates the file where the decrypted message will be stored
        File decryptFile = new File("Messages/Decrypoted-RSA" + fileName.substring(ext));

        //This makes the directory so the file can be stored
        decryptFile.getParentFile().mkdirs();

        //Initializing the byte array
        byte[] fileBytes;
		try
		{
			//Make the file an array of bytes
			fileBytes = getFileInBytes(file);
			
			//Decrypts the file
			decryptFile(fileBytes, decryptFile, keys.getPrivateKey());
		} catch (IOException | GeneralSecurityException e)
		{
			e.printStackTrace();
		}

		//Returns the decrypted file
        return decryptFile;
    }

    /**
     * Function that gets the file bytes then returns them back
     * @param file (Input file)
     * @return byte[] of the file
     * @throws IOException
     */
    public byte[] getFileInBytes(File file) throws IOException
    {
        FileInputStream fis = new FileInputStream(file);
        byte[] fbytes = new byte[(int) file.length()];
        fis.read(fbytes);
        fis.close();
        return fbytes;
    }

    /**
     * Function that Encrypts the file
     * @param input byte[] of the file to encrypt
     * @param output File output (Where to put the encrypted file
     * @param key Public Key to encrypt file
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public void encryptFile(byte[] input, File output, PublicKey key)
            throws IOException, GeneralSecurityException
    {
        this.cipher.init(Cipher.ENCRYPT_MODE, key);
        writeToFile(output, this.cipher.doFinal(input));
    }

    /**
     * Function that Decrypts the file
     * @param input byte[] of the file to decrypt
     * @param output File output (Where to put the decrpted file)
     * @param key Private Key to decrypt file
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public void decryptFile(byte[] input, File output, PrivateKey key)
            throws IOException, GeneralSecurityException
    {
        this.cipher.init(Cipher.DECRYPT_MODE, key);
        writeToFile(output, this.cipher.doFinal(input));
    }

    /**
     * Function that writes the bytes to the file specified then returns the file
     * @param output The file to write to
     * @param toWrite The data to write
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws IOException
     */
    private void writeToFile(File output, byte[] toWrite) throws IllegalBlockSizeException, BadPaddingException, IOException
    {
        FileOutputStream fos = new FileOutputStream(output);
        fos.write(toWrite);
        fos.flush();
        fos.close();
    }
}
