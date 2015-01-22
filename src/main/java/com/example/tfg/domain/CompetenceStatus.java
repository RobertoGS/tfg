package com.example.tfg.domain;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;



@Embeddable
public class CompetenceStatus {
	
	@ManyToOne
	private Competence competence;
	

	@Basic
	@Column(name = "percentage", nullable = false, columnDefinition = "double default 0.0")
	private double percentage;
 
	public Competence getCompetence() {
		return competence;
	}
	public void setCompetence(Competence competence) {
		this.competence = competence;
	}
	public double getPercentage() {
		return percentage;
	}
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

}
