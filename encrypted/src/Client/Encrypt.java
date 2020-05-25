package Client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;


import CentralAuthority.Constant;
import CentralAuthority.DESUtil;
import CentralAuthority.MD5;
import CentralAuthority.PKG;
import Server.Decrypt;

public class Encrypt {
	
	private String Message,ID,desKey;
	BigInteger n,Public_key;
	
    //java.net.URL url = getClass().getResource("C:\\Users\\Administrator.DESKTOP-56DDVRR\\Desktop\\毕业设计\\abcd\\src\\Server\\EncryptedMessage.txt");
    File file;
	
    
	public Encrypt(String ID,String Message , BigInteger n , BigInteger Public_key){
		
		this.ID = ID;
		this.Message = Message;
		this.n = n;
		this.Public_key = Public_key;
		this.desKey=Constant.DES_KEY;
		file=new File(Encrypt.class.getClassLoader().getResource("EncryptedMessage.txt").getPath());
	}

	
     
 //Encrypt message
     public byte[] encrypt() {   						// Encrypting message
    	 
    	 //byte[] message = Message.getBytes();
    	 
    	 //System.out.println(bytesToString((new BigInteger(message)).modPow(Public_key, n).toByteArray()));
		 System.out.println(bytesToString(messageEncrypt()));

		 byte[] encryptedMessage = messageEncrypt();

		 try(
    			 
				 FileWriter fileWriter = new FileWriter(file,true);
				 BufferedWriter bufferFileWriter  = new BufferedWriter(fileWriter);
    			 
			) {
			
    		 fileWriter.append(ID);													// Sending Message to server
    		 fileWriter.append(" ");
    		 fileWriter.append(Integer.toString(encryptedMessage.length));
    		 fileWriter.append(" ");
    		 
    		 for(byte e_message : encryptedMessage){ 
    			 fileWriter.append(Byte.toString(e_message));
    			 fileWriter.append(" ");
    		 }
			 String md5 = MD5.encrypByMd5(Message);
			 byte[] md5Encrypt = md5Encrypt(md5.getBytes());
	        fileWriter.append("\n");
    		 fileWriter.append(Constant.MD5_PREFIX+ID);
			 fileWriter.append(" ");
			 fileWriter.append(Integer.toString(md5Encrypt.length));
			 fileWriter.append(" ");
			 for(byte e_md5 : md5Encrypt){
				 fileWriter.append(Byte.toString(e_md5));
				 fileWriter.append(" ");
			 }
			 byte[] desKey = new BigInteger(this.desKey.getBytes()).toByteArray();
			 byte[] desEncrypt = desEncrypt(desKey);
			 fileWriter.append("\n");
			 fileWriter.append(Constant.DES_PREFIX+ID);
			 fileWriter.append(" ");
			 fileWriter.append(Integer.toString(desEncrypt.length));
			 fileWriter.append(" ");
			 for (byte b : desEncrypt) {
				 fileWriter.append(Byte.toString(b));
				 fileWriter.append(" ");
			 }
			 fileWriter.append("\n");
	        bufferFileWriter.close();
	        fileWriter.close();
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		
		}
    	 	 
    	 return encryptedMessage;
        //return (new BigInteger(message)).modPow(Public_key, n).toByteArray();
        
        
    } 
    
     private byte [] messageEncrypt(){				// Actual message Encryption
		 String encrypt=null;
    	 //return (new BigInteger(message)).modPow(Public_key, n).toByteArray();
		 try {
			 encrypt = DESUtil.encrypt(Message,desKey);
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		 return (new BigInteger(encrypt.getBytes())).toByteArray();
	 }
	private byte [] md5Encrypt(byte [] md5){				// Actual message Encryption
		PKG pkg = new PKG();
		BigInteger private_key = pkg.get_private_key(ID);

		return (new BigInteger(md5)).modPow(private_key, pkg.getn()).toByteArray();

	}
	private byte [] desEncrypt(byte [] des){				// Actual message Encryption
		PKG pkg = new PKG();
		BigInteger private_key = pkg.get_private_key(ID);

		return (new BigInteger(des)).modPow(private_key, pkg.getn()).toByteArray();

	}

     
     private static String bytesToString(byte[] encrypted) { 
 		
         String test = ""; 
         
         for (byte b : encrypted) { 
             test += Byte.toString(b); 
             //test +=" ";
         } 
         
         return test; 
         
     }

	public static void main(String[] args) {
		byte[] bytes={2,3,4,5,6,7,8,9};
		System.out.println(Arrays.toString(new BigInteger(bytes).toByteArray()));
	}
}
