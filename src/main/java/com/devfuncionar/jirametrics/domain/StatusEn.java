package com.devfuncionar.jirametrics.domain;

import org.apache.commons.lang.StringUtils;

public enum StatusEn {

	TEAM_BACKLOG(0, "Team Backlog", "Backlog"),
	GROOMING_FUNC(1, "Grooming Func", "Refinamento Funcional"),
	GROOMING_TECH(2, "Grooming Tech", "Refinamento Tecnico"),
	READY_TO_DEVELOP(3, "Ready to Develop", "Pronto para Dev"),
	SPRINT_BACKLOG(4, "Sprint Backlog - 2", "Sprint Backlog"),
	DEVELOPING(5, "Developing", "Em Desenvolvimento"),
	READY_TO_TEST(6, "Ready to Test", "Pronto para Teste"),
	TESTING(7, "Testing", "Em Teste"),
	READY_TO_ACCEPT(8, "Ready to Accept", "Pronto para Aceite"),
	ACCEPTING(9, "Accepting", "Em Aceite"),
	READY_TO_DEPLOY(10, "Ready to Deploy", "Pronto para Implantar"),
	DEPLOYED(11, "Deployed", "Implantado"),
	CLOSED(12, "Closed", "Done");
	
	
	private Integer step;
	private String description;
	private String translation;
	
	private StatusEn(Integer step, String description, String translation) {
		this.step = step;
		this.description = description;
		this.translation = translation;
	}
	
	public static StatusEn getByDescription(String description) {
		
		for(StatusEn _status : StatusEn.values()) {
			if(StringUtils.equalsIgnoreCase(_status.getDescription(), description)) {
				return _status;
			}
		}
		
		return null;
	}

	public Integer getStep() {
		return step;
	}

	public String getDescription() {
		return description;
	}

	public String getTranslation() {
		return translation;
	}
	
}
