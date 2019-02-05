package com.devfuncionar.jirametrics.domain;

import java.util.Date;

public class Status {

	private Date movedIn;
	private StatusEn status;
	
	public Status() {
		// TODO Auto-generated constructor stub
	}
	
	public Status(Date movedIn, StatusEn staus) {
		this.movedIn = movedIn;
		this.status = staus;
	}
	
	public Date getMovedIn() {
		return movedIn;
	}
	public void setMovedIn(Date movedIn) {
		this.movedIn = movedIn;
	}
	public StatusEn getStatus() {
		return status;
	}
	public void setStatus(StatusEn status) {
		this.status = status;
	}
	
}
