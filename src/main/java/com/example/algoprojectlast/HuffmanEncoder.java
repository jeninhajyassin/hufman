
package com.example.algoprojectlast;

import java.io.*;
import java.util.PriorityQueue;

import com.example.algoprojectlast.FileRW.FileBitWriter;



public class HuffmanEncoder implements huffmanSignature{

	/***
	 * ========= Header Format ========
	 *  1) 4 bytes for the file Signature
	 * 	2) 2 bytes for the file extension length
	 * 	3)  bytes for the file extension
	 *  4) 4 bytes for the file length
	 * 	5) 2 bytes for the tree size
	 * 	6)  bytes for the tree
	 * 	7)  bytes for the encoded file
	 * 	================================
	 *
	 **/

	String inputFileName,outputFilename;

	private final long[] freq  = new long[MAXCHARS];
	private final String[] hCodes = new String[MAXCHARS];

	private int distinctChars = 0;
	private long inputFileSize;
	private long outputFileSize;

	private final String fileExtension;

	private String header;
	private int headerSize;

	private long timeTaken;





	public HuffmanEncoder(File f){
		inputFileName = f.getAbsolutePath();
		fileExtension = inputFileName.substring(inputFileName.lastIndexOf(".")+1);
		outputFilename = f.getAbsolutePath() ;
		outputFilename = outputFilename.substring(0,outputFilename.lastIndexOf('.')) + strExtension;

		resetFrequency();
	}



	void resetFrequency(){
		for(int i=0;i<MAXCHARS;i++)
			freq[i] = 0;
		distinctChars = 0;
		inputFileSize =0;
	}




	public boolean encodeFile() throws Exception{ //main encoding function

		Long startTime = System.currentTimeMillis();
		 
		if(inputFileName.length() == 0)
			return false;                 //if file has no content, return

		FileInputStream fin = new FileInputStream(inputFileName);
		//used for writing to the output file
		BufferedInputStream in = new BufferedInputStream(fin); //Readers to read the file contents

		//Frequency Analysis
		{
			inputFileSize = in.available();
			if(inputFileSize == 0)
				return false; //if file has no content, return


			long i=0;
			in.mark((int) inputFileSize);

			distinctChars = 0; //start counting distinct chars
			while (i < inputFileSize) //count distinct chars
			{
				int ch = in.read();
				i++;
				if(freq[ch] == 0)
					distinctChars++;
				freq[ch]++;
			}
			in.reset();
		}



		
		//priority queue to store the nodes
		PriorityQueue<HuffmanNode> pq = new PriorityQueue<>(distinctChars);
		for(int i=0;i<MAXCHARS;i++){
			if(freq[i] > 0){

				HuffmanNode np = new HuffmanNode(freq[i],(char)i,null,null); 
				// make a new huffman node, has no children on L and R
				pq.add(np);
			}
		}



		HuffmanNode low1,low2; //For extracting the two lowest nodes
		
		while(pq.size() > 1){ //until there's only 1 node left in pq
			low1 = pq.poll();       //extract the lowest node from pq, put it in low1
			low2 = pq.poll();       //extract the lowest node from pq, put it in low2

			if(low1 == null || low2 == null) { throw new Exception("PQueue Error!"); } //if for some reason there's no low1 or low2, throw error
			HuffmanNode intermediate = new HuffmanNode((low1.freq+low2.freq),(char)0,low1,low2); //make a new node, set its value to low1+low2, put low1 and low2 as children
			pq.add(intermediate);

		}



		//at this point there's only one node on pq left
		//starting node for the final tree
		HuffmanNode root = pq.poll();             //so we remove it
		assert root != null;

		// magic happens here :D
		if(root.left == null && root.right == null) //if it has no children, it means it's a leaf node
			root.huffCode = "0"; //so we set its huff code to 0
		else
			buildHuffmanCodes(root, ""); //and use it as the root for our tree



		for(int i=0;i<MAXCHARS;i++)
			hCodes[i] = "";           //reset the hCodes array just to be sure



		getHuffmanCodes(root); //we convert that huffman tree to 1s and 0s



		
		FileBitWriter hFile = new FileBitWriter(outputFilename); //Writer to file

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

		//We write the signature
		hFile.putString(hSignature);
		header = hSignature;
		headerSize += hSignature.length();

		//we write the extension length
		int extLength = fileExtension.length();
		String extLengthStr = leftPad(Integer.toString(extLength,2),16);
		hFile.putBits(extLengthStr);
		header += extLengthStr;
		headerSize += 2;

		//we write the extension
		hFile.putString(fileExtension);
		header += fileExtension;
		headerSize += fileExtension.length();

		//We write the file length
		String buf;
		buf = leftPad(Long.toString(inputFileSize,2),32); //fileLen
		hFile.putBits(buf);
		header += buf;
		headerSize += 4;


		//encode the tree
		String tree = encodeTree(root);

		//write the tree length
		buf = leftPad(Integer.toString(tree.length(),2),16); //Tree Length
		hFile.putBits(buf);
		header += buf;
		headerSize += 2;

		//write the tree
		hFile.putBits(tree);
		header += tree;
		headerSize += tree.length()/8 + (tree.length()%8 == 0 ? 0 : 1);



		long counter = 0;
		while(counter < inputFileSize){   //encode the file
			int ch = in.read();
			hFile.putBits(hCodes[ch]);
			counter++;
		}


		hFile.closeFile();

		//used for comparison at the end of compression
		outputFileSize = new File(outputFilename).length();

		Long endTime = System.currentTimeMillis();
		timeTaken = endTime - startTime;


		return true;


		
	}

	private String encodeTree(HuffmanNode root) {
		if(root == null) return "";
		if(root.left == null && root.right == null){
			return "1" + leftPad(Integer.toString(root.ch,2),8);
		}
		return "0" + encodeTree(root.left) + encodeTree(root.right);
	}

	private void buildHuffmanCodes(HuffmanNode parentNode,String parentCode){ //building the huffman code for the symbols
		parentNode.huffCode = parentCode;
		if(parentNode.left != null)
			buildHuffmanCodes(parentNode.left,parentCode + "0"); //if we go left, 0

		if(parentNode.right != null)
			buildHuffmanCodes(parentNode.right,parentCode + "1"); //if we go right, 1
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
	
	private String leftPad(String txt, int n){
		StringBuilder txtBuilder = new StringBuilder(txt);
		while(txtBuilder.length() < n )
			txtBuilder.insert(0, "0");
		txt = txtBuilder.toString();
		return txt;
	}


	public String getExtension() {
		return fileExtension;
	}

	//getters
	public long getInputFileSize() {
		return inputFileSize;
	}
	public long getOutputFileSize() {
		return outputFileSize;
	}
	public String getHeader() {
		return header;
	}
	public int getHeaderSize() {
		return headerSize;
	}
	public double getCompressionRatio() {
		//the percentage of the compression
		return (double) (inputFileSize - outputFileSize) / inputFileSize * 100;
	}
	public String getHuffmanSignature() {
		return hSignature;
	}
	public long[] getCharFrequencies() {
		return freq;
	}
	public String[] getHuffmanCodes() {
		return hCodes;
	}
	public long getTimeTaken() {
		return timeTaken;
	}


}

