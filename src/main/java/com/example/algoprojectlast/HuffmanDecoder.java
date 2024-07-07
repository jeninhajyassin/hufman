
package com.example.algoprojectlast;

import java.io.*;
import java.util.Objects;

import com.example.algoprojectlast.FileRW.*;


public class HuffmanDecoder implements huffmanSignature{

	
	private final String fileName;
	private String outputFilename;

	private final String[] hCodes = new String[MAXCHARS];

	private long timeTaken;




	public HuffmanDecoder(File f){
		fileName = f.getAbsolutePath();
		outputFilename = fileName.substring(0, fileName.lastIndexOf('.'));
	}


	   /* ========= Header Format ========
		* 1) 4 bytes for the file Signature
		* 2) 2 bytes for the file extension length
		* 3)  bytes for the file extension
		* 4) 4 bytes for the file length
		* 5) 2 bytes for the tree size
		* 6)  bytes for the tree
		* 7)  bytes for the encoded file
		* ================================
		* */


		
	public boolean decodeFile() throws Exception{ //Main decoding function

		Long startTime = System.currentTimeMillis();

		if(fileName.length() == 0)
			return false;
		
		for(int i=0;i<MAXCHARS;i++)
			hCodes[i] = "";

		long i;
		FileBitReader fin = new FileBitReader(fileName);
//		long fileLen = fin.available(); //gets the length of the passed file in bytes,to know how many bits to read

		String buf;
		buf = fin.getBytes(hSignature.length());
		if(!buf.equals(hSignature))
			return false;


		//read extension length
		int extLen = Integer.parseInt(fin.getBits(16),2);
		//read extension
		String ext = fin.getBytes(extLen);
		outputFilename += "." + ext;


		//length of file
		long outputFileLen = Long.parseLong(fin.getBits(32), 2);


		//read huffman tree length
		int treeLen = Integer.parseInt(fin.getBits(16), 2);


		//read huffman tree
		String tree = fin.getBits(treeLen);

		//create huffman tree from the string
		HuffmanNode root = recreateTree(tree);


		if (root.left == null && root.right == null) {
			// Special case: tree contains only one character
			System.out.println(outputFilename);
			BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(outputFilename));
			for (i = 0; i < outputFileLen; i++) {
				fout.write(root.ch);
			}
			fout.close();
			return true;
		}


		buildHuffmanCodes(root, "");   //build the huffman codes from the tree
		getHuffmanCodes(root);                   //fill the hCodes array with the codes



		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(outputFilename));
		i = 0;
		int k;
		int ch;
		System.out.println(outputFileLen);

		System.out.println("Decoding...");

		while(i < outputFileLen){	//main decoder, uses a findCode function for each coding in the file
				StringBuilder code = new StringBuilder();
				for(k=0;k<=16;k++){             //k<=16 is the maximum length of a code (was k <32 before)
					code.append(fin.getBit());
					ch  = findCode(code.toString());
					if(ch > -1){
						bufferedOutputStream.write((char)ch);
						i++;
						break;
					}
				}
		}

		bufferedOutputStream.close();


		long endTime = System.currentTimeMillis();
		timeTaken = endTime - startTime;

		return true;


	}

	private HuffmanNode recreateTree(String tree) {
		return recreateTree(tree, new int[]{0});
	}

	private HuffmanNode recreateTree(String tree, int[] index) {
		if (tree.charAt(index[0]) == '1') {
			char ch = (char)Integer.parseInt(tree.substring(index[0] + 1, index[0] + 9), 2);
			HuffmanNode node = new HuffmanNode(ch);
			index[0] += 9;
			return node;
		}
		else {
			index[0]++;
			HuffmanNode left = recreateTree(tree, index);
			HuffmanNode right = recreateTree(tree, index);
			return new HuffmanNode(0, '\n', left, right);
		}
	}



	int findCode(String c){ //main decoder of the character from the 1s and 0s
		int ret = -1;
		for(int i=0;i<MAXCHARS;i++){
			if(!Objects.equals(hCodes[i], "") && c.equals(hCodes[i])){
				ret = i;
				break;
			}
		}
		return ret;
	}



	private void getHuffmanCodes(HuffmanNode parentNode){ //getting the huffman codes of a symbol

		if(parentNode == null) return;

		int asciiCode = parentNode.ch;

		if(parentNode.left == null || parentNode.right == null){
			hCodes[asciiCode] = parentNode.huffCode;
		}

		if(parentNode.left != null ) getHuffmanCodes(parentNode.left);
		if(parentNode.right != null ) getHuffmanCodes(parentNode.right);
	}
	private void buildHuffmanCodes(HuffmanNode parentNode,String parentCode){ //building the huffman code for the symbols
		parentNode.huffCode = parentCode;
		if(parentNode.left != null)
			buildHuffmanCodes(parentNode.left,parentCode + "0"); //if we go left, 0

		if(parentNode.right != null)
			buildHuffmanCodes(parentNode.right,parentCode + "1"); //if we go right, 1
	}



	//getters
	public String getFileName(){
		return fileName;
	}
	public String getOutputFileName(){
		return outputFilename;
	}
	public Long getTimeTaken(){
		return timeTaken;
	}

		



}



