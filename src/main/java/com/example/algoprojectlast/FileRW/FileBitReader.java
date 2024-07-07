
package com.example.algoprojectlast.FileRW;
import java.io.*;

public class FileBitReader {

   	private String fileName;
	
	private File inputFile;
	private FileInputStream fin;
	private BufferedInputStream in;
	private String currentByte;
	

	
	public FileBitReader(String txt) throws Exception{
		fileName = txt;
		inputFile = new File(fileName);
		fin = new FileInputStream(inputFile);
		in = new BufferedInputStream(fin);
		currentByte = "";
	}

	private String leftPad8(String txt){
		 StringBuilder txtBuilder = new StringBuilder(txt);
		 while(txtBuilder.length() < 8 )
			txtBuilder.insert(0, "0");
		 txt = txtBuilder.toString();
		 return txt;
	}
	private String rightPad8(String txt){
		 StringBuilder txtBuilder = new StringBuilder(txt);
		 while(txtBuilder.length() < 8 )
			txtBuilder.append("0");
		 txt = txtBuilder.toString();
		 return txt;
	}
		
	public String getBit() throws Exception{
		if(currentByte.length() == 0 && in.available() >= 1){
			int k = in.read();

			currentByte = Integer.toString(k,2);
			currentByte = leftPad8(currentByte);

		}
		if(currentByte.length() > 0){
			String ret = currentByte.substring(0,1);
			currentByte = currentByte.substring(1);
			return ret;
		}
		return "";
	}
	
	public String getBits(int n) throws Exception{

		StringBuilder ret = new StringBuilder();
		for(int i=0;i<n;i++){
		 ret.append(getBit());
		}
		return ret.toString();
	}
	
	public String getBytes(int n) throws Exception{
		StringBuilder ret = new StringBuilder();
		String temp;
		for(int i=0;i<n;i++){
			temp = getBits(8);
			char k = (char)Integer.parseInt(temp,2);
			ret.append(k);
		}
		return ret.toString();
	}


    
}
