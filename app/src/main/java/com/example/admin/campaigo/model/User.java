package com.example.admin.campaigo.model;

public class User {
	private String id = "";
	private String usname = "";
	private String pwd = "";
	private String className = "";
	private String position = "";
	private boolean errorLogin = true;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsname() {
		return usname;
	}
	public void setUsname(String usname) {
		this.usname = usname;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public boolean isErrorLogin() {
		return errorLogin;
	}
	public void setErrorLogin(boolean errorLogin) {
		this.errorLogin = errorLogin;
	}
	
	public void init(){
		id = "";
		usname = "";
		pwd = "";
		className = "";
		position = "";
		errorLogin = true;
	}
	
}
