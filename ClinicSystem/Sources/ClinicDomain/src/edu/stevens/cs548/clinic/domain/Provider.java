package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
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
		name="SearchProviderByNPI",
		query="select p from Provider p where p.npi = :npi"),
	@NamedQuery(
		name="CountProviderByNPI",
		query="select count(p) from Provider p where p.npi = :npi"),
	@NamedQuery(
		name = "RemoveAllPatients", 
		query = "delete from Patient p")
})

@Entity
public class Provider implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static enum ProviderType {
		INTERNIST,
		SURGEON,
		RADIOLOGIST
	};
	
	@Id
	@GeneratedValue
	private long id;
	
	private long npi;
	
	@Enumerated
	private ProviderType providerType;

	private String name;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getNpi() {
		return npi;
	}

	public void setNpi(long npi) {
		this.npi = npi;
	}

	public ProviderType getProviderType() {
		return providerType;
	}

	public void setProviderType(ProviderType providerType) {
		this.providerType = providerType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "provider")
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
	
	public Provider() {
		super();
		treatments = new ArrayList<Treatment>();
	}
	
	/*
	 * Addition and deletion of treatments should be done here.
	 */
	
	public long addTreatment (Patient p, Treatment t) {
		// set forward and backward links and call patient's addTreatment
		getTreatments().add(t);
		t.setProvider(p, this);
		long id = p.addTreatment(t);
		
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
		if (t.getProvider().id != this.id) {
			throw new TreatmentExn("Inappropriate treatment access: provider = " + id + ", treatment = " + tid);
		}
		return t.export(visitor);
	}
	
}
