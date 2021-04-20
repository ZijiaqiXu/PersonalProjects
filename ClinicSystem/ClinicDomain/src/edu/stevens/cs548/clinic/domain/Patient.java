package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;

/**
 * Entity implementation class for Entity: Patient
 *
 */
@NamedQueries({
	@NamedQuery(
		name="SearchPatientByPatientID",
		query="select p from Patient p where p.patientId = :pid"),
	@NamedQuery(
		name="CountPatientByPatientID",
		query="select count(p) from Patient p where p.patientId = :pid"),
	@NamedQuery(
		name = "RemoveAllPatients", 
		query = "delete from Patient p")

})

@Entity
public class Patient implements Serializable {
	private static Logger logger = Logger.getLogger(Patient.class.getCanonicalName());

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private long id;
	
	private long patientId;
	
	private String name;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPatientId() {
		return patientId;
	}

	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "patient")
	@OrderBy
	private List<Treatment> treatments;

	protected List<Treatment> getTreatments() {
		return treatments;
	}

	protected void setTreatments(List<Treatment> treatments) {
		this.treatments = treatments;
	}
	
	@Transient
	private ITreatmentDAO treatmentDAO;
	
	public void setTreatmentDAO (ITreatmentDAO tdao) {
		this.treatmentDAO = tdao;
	}
	
	/**
	 * This should only be called from Provider.addTreatment()
	 */
	long addTreatment (Treatment t) {
		// Persist treatment and set forward and backward links
		long id = this.treatmentDAO.addTreatment(t);
		this.getTreatments().add(t);
		logger.info("Binded treatment " + t.getId() + " with patient " + this.id + ". Now the patient has " + treatments.size() + " treatments");
		if (t.getPatient() != this) {
			t.setPatient(this);
		}
		return id;
	}
	
	public List<Long> getTreatmentIds() {
		List<Long> treatmentIds = new ArrayList<>();
		for (Treatment t : this.getTreatments()) {
			treatmentIds.add(t.getId());
		}
		return treatmentIds;
	}
	
	public <T> T exportTreatment(long tid, ITreatmentExporter<T> visitor) throws TreatmentExn {
		// Export a treatment without violating Aggregate pattern
		// Check that the exported treatment is a treatment for this patient.
		Treatment t = treatmentDAO.getTreatment(tid);
		if (t.getPatient().id != this.id) {
			throw new TreatmentExn("Inappropriate treatment access: patient = " + id + ", treatment = " + tid);
		}
		return t.export(visitor);
	}
	
	public Patient() {
		super();
		treatments = new ArrayList<Treatment>();
	}
   
}
