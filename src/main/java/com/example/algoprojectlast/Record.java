package com.example.algoprojectlast;

public class Record {
    private char ch;
    private long freq;
    private String code;

    public Record(char ch, long freq, String code) {
        this.ch = ch;
        this.freq = freq;
        this.code = code;
    }

    public char getCh() {
        return ch;
    }
    public void setCh(char ch) {
        this.ch = ch;
    }
    public long getFreq() {
        return freq;
    }
    public void setFreq(long freq) {
        this.freq = freq;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

}
