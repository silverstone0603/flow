package module;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class modEncryption {
	private modCoreWindow clsParent;
	private modSysData clsSysData;

	private static final String algorithm = "AES";
	private static final String transformation = algorithm+"/ECB/PKCS5Padding";

	private Key key;
	
	private static final byte[] keyValue = new byte[16]; 
	// 총 16개의 문자열로 구성된 KEY
	
	
	public modEncryption(modSysData clsSysData){
		this.clsSysData = clsSysData;
	}

	private void generateKey() throws Exception {
		if(this.clsSysData.getLoginValue()==true) {
			Key tmpKey;
			byte[] tmpValue = new byte[16]; // { 'T', 'h', 'i', 's', 'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y' };
			
			String tmpString = "FlowTeam";
			String tmpUserID = clsSysData.getUserKey();
			
			for(int i=0; i<16; i++) {
				if(i<tmpString.getBytes().length) {
					// 앞자리는 FlowTeam 글자로 먼저 채움
					tmpValue[i] = tmpString.getBytes()[i];
				}else{
					// 뒷자리는 아이디로 채움
					if( (i-tmpString.length()) < tmpUserID.length() ){
						tmpValue[i] = (byte) tmpUserID.charAt( i-tmpString.length() );
					}
				}
			}
	
			tmpKey = new SecretKeySpec(tmpValue, algorithm); 
			this.key = tmpKey; 
		}else {
			System.out.println("[Flow _ Encryption Module] 로그인이 되어 있지 않아서 UserKey를 만들 수 없습니다.");
		}
	}

	/**
	 * 원본 파일을 암호화해서 대상 파일을 만든다.
	 * @param source 원본 파일
	 * @param dest 대상 파일
	 * @throws Exception
	 */
	public boolean encrypt(File source, File dest) throws Exception{
		boolean tmpVal = crypt(Cipher.ENCRYPT_MODE, source, dest);
		return tmpVal;
	}

	/**
	 * 원본 파일을 복호화해서 대상 파일을 만든다.
	 * @param source 원본 파일
	 * @param dest 대상 파일
	 * @throws Exception
	 */
	public boolean decrypt(File source, File dest) throws Exception {
		boolean tmpVal = crypt(Cipher.DECRYPT_MODE, source, dest);
		return tmpVal;
	}

	/**
	 * 원본 파일을 읽어와 암호화하여 대상 파일을 만든다.
	 * @param source 원본 파일
	 * @param dest 대상 파일
	 * @throws Exception
	 */
	public void encrypt(InputStream is, OutputStream os) throws Exception{
		crypt(Cipher.ENCRYPT_MODE, is, os);
	}

	/**
	 * 원본 파일을 읽어와 복호화하여 대상 파일을 만든다.
	 * @param source 원본 파일
	 * @param dest 대상 파일
	 * @throws Exception
	 */
	public void decrypt(InputStream is, OutputStream os) throws Exception{
		crypt(Cipher.DECRYPT_MODE, is, os);
	}

	private void crypt(int mode, InputStream is, OutputStream os) throws Exception{
		if(this.clsSysData.getLoginValue()==true) {
			this.generateKey();
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(mode, key);
	
			InputStream input = is;
			OutputStream output = os;
	
			byte[] buffer = new byte[1024];
			int read = -1;
			while((read = input.read(buffer)) != -1){
				output.write(cipher.update(buffer, 0, read));
			}
			output.write(cipher.doFinal());
		}
	}

	private boolean crypt(int mode, File source, File dest) throws Exception{
		if(this.clsSysData.getLoginValue()==true) {
			this.generateKey();
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(mode, key);
			InputStream input = null;
			OutputStream output = null;
	
			try{
				input = new BufferedInputStream(new FileInputStream(source));
				output = new BufferedOutputStream(new FileOutputStream(dest));
				byte[] buffer = new byte[1024];
				int read = -1;
				while((read = input.read(buffer)) != -1){
					output.write(cipher.update(buffer, 0, read));
				}
				output.write(cipher.doFinal());
				return true;
			}catch (Exception e) {
				output.close();
	            if(dest.exists())
	               if(dest.delete())
	            	   clsParent.clsTrayIcon.showMessage("작업할 수 없는 파일", clsSysData.getUserID()+"님의 파일이 아닌 것 같습니다.\n해당 파일은 작업을 수행할 수 없습니다.", 2);
				return false;
			}finally{
				if(output != null){
					try{output.close();}catch(IOException e){}
				}
				if(input != null){
					try{input.close();}catch(IOException e){}
				}
			}
		}
		return false;
	}
}