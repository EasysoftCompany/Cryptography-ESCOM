package mx.cryptography;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.imageio.ImageIO;

public class DES_Cipher {
	
	private static Cipher encCBC;
	private static Cipher decCBC;
	private static final byte[] initializationVector = { 11, 22, 33, 44, 99, 88, 77, 66 };
	private static final int headerSize = 54;
	
	public static void main(String[] args) {
		String originalImageRepo ="Images/Original/prueba2.bmp";
		String encryptImageCBC = "Images/Encrypt/cbc.bmp";
		String decryptImageCBC = "Images/Decrypt/vuelta.bmp";

		System.out.println("try");
		try {
			//Creating a simmetric key for DES cipher
			SecretKey simmetricKey = KeyGenerator.getInstance("DES").generateKey();
			
			//Creating an special parameter (iv) for CBC mode
			AlgorithmParameterSpec algorithmParameterSpec = new IvParameterSpec(initializationVector);
			
			//Creating a Java cipher CBC
			//encCBC = Cipher.getInstance("DES/CBC/NoPadding");
			//encCBC = Cipher.getInstance("DES/CBC/PKCS5Padding");
			//encCBC.init(Cipher.ENCRYPT_MODE, simmetricKey, algorithmParameterSpec);
			
			encCBC = Cipher.getInstance("DES/ECB/NoPadding");
			encCBC.init(Cipher.ENCRYPT_MODE, simmetricKey);
			
			//decCBC = Cipher.getInstance("DES/CBC/NoPadding");
			//decCBC = Cipher.getInstance("DES/CBC/PKCS5Padding");
			//decCBC.init(Cipher.DECRYPT_MODE, simmetricKey, algorithmParameterSpec);
			decCBC = Cipher.getInstance("DES/ECB/NoPadding");
			decCBC.init(Cipher.DECRYPT_MODE, simmetricKey);
			
			BufferedImage img = ImageIO.read(new File(originalImageRepo));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write( img, "bmp", baos );
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();

				
			byte[] finalBytes = new byte[(int)imageInByte.length];
			byte[] encryptedBytes = new byte[(int)imageInByte.length-headerSize];
			
			for(int i = 0, j=0 ; i < imageInByte.length; i++) {
				if(i < headerSize) {
					finalBytes[i] = imageInByte[i];
				}else {
					encryptedBytes[j++] = imageInByte[i];
				}
			}	
			System.out.println("encrypted: "+encryptedBytes.length);
			byte[] imageModified = encCBC.doFinal(encryptedBytes);
			System.out.println("image modified: "+imageModified.length);
			
			for(int i = headerSize, j = 0; i < imageInByte.length; i++, j++) {
				finalBytes[i] = imageModified[j];
			}
			
			InputStream in = new ByteArrayInputStream(finalBytes);
			BufferedImage bImageFromConvert = ImageIO.read(in);
			ImageIO.write(bImageFromConvert, "bmp", new File(encryptImageCBC));

			//Dec
			BufferedImage img2 = ImageIO.read(new File(encryptImageCBC));
			ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
			ImageIO.write( img2, "bmp", baos2 );
			baos2.flush();
			byte[] imageInByte2 = baos2.toByteArray();
			baos2.close();

				
			byte[] finalBytes2 = new byte[(int)imageInByte2.length];
			byte[] encryptedBytes2 = new byte[(int)imageInByte2.length-headerSize];
			
			for(int i = 0, j=0 ; i < imageInByte2.length; i++) {
				if(i < headerSize) {
					finalBytes2[i] = imageInByte2[i];
				}else {
					encryptedBytes2[j++] = imageInByte2[i];
				}
			}	
			System.out.println("encrypted: "+encryptedBytes2.length);
			//byte[] imageModified2 = decCBC.doFinal(encryptedBytes2);
			byte[] imageModified2 = decCBC.update(imageModified);
			//byte[] imageModified2 = decCBC.update(encryptedBytes2);
			System.out.println("image modified: "+imageModified2.length);
			
			//for(int i = headerSize, j = 0; i < imageInByte2.length; i++, j++) {
			for(int i = headerSize, j = 0; i < imageModified2.length; i++, j++) {
				finalBytes2[i] = imageModified2[j];
			}
			
			InputStream in2 = new ByteArrayInputStream(finalBytes2);
			BufferedImage bImageFromConvert2 = ImageIO.read(in2);
			ImageIO.write(bImageFromConvert2, "bmp", new File(decryptImageCBC));
			
		} catch (Exception e) {
			System.out.println("error");
			e.printStackTrace();
		}
		System.out.println("Fin");
	}
}