
package com.example.algoprojectlast;


public class HuffmanNode implements Comparable<HuffmanNode>{
	
	HuffmanNode left,right;
	public long freq;
	public char ch;
	public String huffCode;
	
	
	public HuffmanNode(){
		this.freq = 0;
		this.ch = 0;
		this.huffCode = "";
		this.left = null;
		this.right = null;
	}

	public HuffmanNode(long freq,char ch,HuffmanNode left,HuffmanNode right){
		this.freq = freq;
		this.ch = ch;
		this.left = left;
		this.right = right;
		this.huffCode = "";
	}

	public HuffmanNode(char ch) {
		this.ch = ch;
		this.freq = 1;
		this.left = null;
		this.right = null;
		this.huffCode = "";
	}

	@Override
	public int compareTo(HuffmanNode o) {
		return Long.compare(this.freq, o.freq);
	}


	
}
