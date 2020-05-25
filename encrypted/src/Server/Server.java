package Server;

import java.math.BigInteger;
import java.util.Scanner;

import CentralAuthority.MD5;
import CentralAuthority.PKG;

public class Server {
	
public static void main(String args[]){
	
		String ID;
		
		Scanner p = new Scanner (System.in);
		
		System.out.println("Enter your User ID (for eg: abc@gmail.com)");
		ID = p.next();
			
		
		System.out.println("Hi... I am Server,\nSearching for messages\n");
		
		
		PKG pkg = new PKG();
		BigInteger Private_key =pkg.get_private_key(ID);
		BigInteger n = pkg.getn();
		
		
		System.out.println("\nMy Private Key is :- " +   Private_key );
		
		
		Decrypt decryptMessage = new Decrypt(ID,n,Private_key);
		
		//System.out.println("\n --------------------Decrypted message is -------------------------\n");

	String decrypt = decryptMessage.decrypt();
	String[] split = decrypt.split(",");
	System.out.println(split[1]);
	if (split[1].equals(MD5.encrypByMd5(split[0]))) {
		System.out.println("\n --------------------Decrypted message is -------------------------\n");
		System.out.println(split[0]);
	}else {
		System.out.println("\n --------------------签名认证失败-------------------------\n");
	}

}
}
