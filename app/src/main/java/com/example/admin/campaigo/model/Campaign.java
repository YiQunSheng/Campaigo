package com.example.admin.campaigo.model;

import java.sql.Timestamp;

public class Campaign {
	private int camid;
	private String caname;
	private Timestamp endeadline;
	private Timestamp startline;
	private Timestamp endline;
	private String describe;
	
	public int getCamid() {
		return camid;
	}
	public void setCamid(int camid) {
		this.camid = camid;
	}
	public String getCaname() {
		return caname;
	}
	public void setCaname(String caname) {
		this.caname = caname;
	}
	public Timestamp getEndeadline() {
		return endeadline;
	}
	public void setEndeadline(Timestamp endeadline) {
		this.endeadline = endeadline;
	}
	public Timestamp getStartline() {
		return startline;
	}
	public void setStartline(Timestamp startline) {
		this.startline = startline;
	}
	public Timestamp getEndline() {
		return endline;
	}
	public void setEndline(Timestamp endline) {
		this.endline = endline;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	
	

}
