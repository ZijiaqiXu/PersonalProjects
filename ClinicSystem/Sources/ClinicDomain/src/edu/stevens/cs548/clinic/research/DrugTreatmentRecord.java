package edu.stevens.cs548.clinic.research;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity implementation class for Entity: DrugTreatmentRecord
 *
 */
@Entity

@NamedQuery(
		name="SearchDrugTreatmentRecords",
		query="select t from DrugTreatmentRecord t")

public class DrugTreatmentRecord implements Serializable {

	
	private static final long serialVersionUID = 1L;

	public DrugTreatmentRecord() {
		super();
	}
	
	@Id @GeneratedValue
	private long id;
	
	private String drugName;
	
	private float dosage;
	
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@ManyToOne
	private Subject subject;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDrugName() {
		return drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	public float getDosage() {
		return dosage;
	}

	public void setDosage(float dosage) {
		this.dosage = dosage;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

}
