package Server;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Scanner;

import CentralAuthority.Constant;
import CentralAuthority.DESUtil;
import CentralAuthority.PKG;
import Client.*;

public class Decrypt {

	
	BigInteger n , private_key;
	String ID;
	
	//java.net.URL url = getClass().getResource("C:\\Users\\Administrator.DESKTOP-56DDVRR\\Desktop\\毕业设计\\abcd\\src\\Server\\EncryptedMessage.txt");
    File file ;
	
    
	public Decrypt( String ID,BigInteger n , BigInteger private_Key){
		
		this.ID = ID;
		this.private_key = private_Key;
		this.n = n;
		file=new File(Decrypt.class.getClassLoader().getResource("EncryptedMessage.txt").getPath());
	}
	
	public String decrypt() {							// First Reading Message then crypting
		
		byte [] message = new byte[0];
		byte [] md5=new byte[0];
		byte [] desKey=new byte[0];
		int desLength;
		int md5Lenght;
		int k;
		boolean flag = false;
		
		try {
			
			Scanner p = new Scanner(file);
			while(p.hasNext()){
				
					if(p.next().compareTo(ID) == 0){			// Message Found for this server
						
						k = p.nextInt();
						message  = new byte[k];
						
						for(int i=0;i<k;i++){
							
							message[i] = p.nextByte();
							
						}
						
						flag = true;
						//System.out.println(new String(decryptMessage(message)) + "\n");
						
					}
					else{
						
						k = p.nextInt();
						
						for(int i=0;i<k;i++){
							
							p.nextByte();
							
						}
						
					}
					
			}
			Scanner p1 = new Scanner(file);
			while(p1.hasNext()){

				if(p1.next().compareTo(Constant.MD5_PREFIX+ID) == 0){			// Message Found for this server

					md5Lenght = p1.nextInt();
					md5  = new byte[md5Lenght];

					for(int i=0;i<md5Lenght;i++){

						md5[i] = p1.nextByte();

					}
					flag = true;
					//System.out.println("this is md5+"+new String(decryptMd5(md5) )+ "\n");

				}
				else{

					md5Lenght = p1.nextInt();

					for(int i=0;i<md5Lenght;i++){

						p1.nextByte();

					}

				}

			}
			Scanner p2 = new Scanner(file);
			while(p2.hasNext()){

				if(p2.next().compareTo(Constant.DES_PREFIX+ID) == 0){			// Message Found for this server

					desLength = p2.nextInt();
					desKey  = new byte[desLength];

					for(int i=0;i<desLength;i++){

						desKey[i] = p2.nextByte();

					}
					flag = true;
					//System.out.println("this is md5+"+new String(decryptMd5(md5) )+ "\n");

				}
				else{

					desLength = p2.nextInt();

					for(int i=0;i<desLength;i++){

						p2.nextByte();

					}

				}

			}
			/*else 	*/
			p.close();
			p1.close();
			p2.close();
			String result="";
			result+=new String(DESUtil.decrypt(new String(message),decryptMessage(decryptDES(desKey))));
			result+=",";
			result+=new String(decryptMd5(md5));
			if(flag)	return result;
			
		} 
		catch (FileNotFoundException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(new String("There is no message for you !!!!!!!"));
		return new String("There is no message for you !!!!!!!");   // No Message Found for this server
		
        
    } 
	
	private static String bytesToString(byte[] encrypted) { 
		
        String test = ""; 
        
        for (byte b : encrypted) { 
            test += Byte.toString(b); 
        } 
        
        return test; 
        
    } 
	
	public String decryptMessage(byte[] des) {			// Actual Message Decryption
		
        //return (new BigInteger(message)).modPow(private_key, n).toByteArray();

        return new String(des);
    }
    public byte[] decryptMd5(byte[] md5){
		PKG pkg = new PKG();
		BigInteger public_key = pkg.get_public_key(ID);
		return (new BigInteger(md5)).modPow(public_key,pkg.getn()).toByteArray();
	}

	public byte[] decryptDES(byte[] des){
		PKG pkg = new PKG();
		BigInteger public_key = pkg.get_public_key(ID);
		return (new BigInteger(des)).modPow(public_key,pkg.getn()).toByteArray();
	}
}
