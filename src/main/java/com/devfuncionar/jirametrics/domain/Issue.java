package com.devfuncionar.jirametrics.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Issue {

	private String id;
	private Date created;
	private String description;
	private String type;
	private String team;
	private List<Status> status;

	public Issue() {
		this.status = new ArrayList<>();
	}

	public void addStatus(Status status) {
		this.status.add(status);
	}

	public void addStatus(List<Status> status) {
		for (Status _status : status) {
			this.status.add(_status);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public List<Status> getStatus() {
		return status;
	}

	public void setStatus(List<Status> status) {
		this.status = status;
	}

}
