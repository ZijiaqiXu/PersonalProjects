package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Treatment
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Treatment implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private long id;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	private String diagnosis;
	
	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	@ManyToOne
	@JoinColumn(name = "patient_fk", referencedColumnName = "id")
	private Patient patient;

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
		if (!patient.getTreatments().contains(this))
			patient.addTreatment(this);
	}
	
	@ManyToOne
	@JoinColumn(name = "provider_fk", referencedColumnName = "id")
	private Provider provider;

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Patient patient, Provider provider) {
		this.provider = provider;
		if (!provider.getTreatments().contains(this))
			provider.addTreatment(patient, this);
	}
	
	public abstract <T> T export(ITreatmentExporter<T> visitor);

	public Treatment() {
		super();
	}
   
}
