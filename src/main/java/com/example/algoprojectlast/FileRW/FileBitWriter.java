
package com.example.algoprojectlast.FileRW;

import java.io.*;

public class FileBitWriter {
    

   	private String fileName;
	
	private File outputFile;
	private FileOutputStream fout;
	private BufferedOutputStream outf;
	private String currentByte;





	public FileBitWriter(String txt) throws Exception{
		fileName = txt;

		outputFile = new File(fileName);
		fout = new FileOutputStream(outputFile);
		outf = new BufferedOutputStream(fout);


		currentByte = "";

	}

	
	public void putBit(int bit) throws Exception{

		bit = bit % 2;
		currentByte = currentByte + bit;

		if(currentByte.length() >= 8){

			int byteVal = Integer.parseInt(currentByte.substring(0,8),2);
			outf.write(byteVal);
			currentByte = ""; //reset
		}

	}
	
	public void putBits(String bits) throws Exception{

		while(bits.length() > 0){
			int bit = Integer.parseInt(bits.substring(0,1));
			putBit(bit);
			bits = bits.substring(1);
		}

	}

	public void putString(String txt) throws Exception{

		while(txt.length() > 0){
			String binString = Integer.toString(txt.charAt(0),2);
			binString = leftPad8(binString);

			putBits(binString);
			txt = txt.substring(1);
		}
	}
		
	private String leftPad8(String txt){
		 StringBuilder txtBuilder = new StringBuilder(txt);
		 while(txtBuilder.length() < 8 )
			txtBuilder.insert(0, "0");
		 txt = txtBuilder.toString();
		 return txt;
	}
		
	public void closeFile() throws Exception{
		//check if incomplete byte exists
		while(currentByte.length() > 0){
			putBit(1);
		}
		outf.close();
	}
		
    
}
