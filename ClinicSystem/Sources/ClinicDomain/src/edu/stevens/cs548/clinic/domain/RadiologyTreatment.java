package edu.stevens.cs548.clinic.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class RadiologyTreatment extends Treatment {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3656673416179492428L;

	@ElementCollection(fetch = FetchType.EAGER)
	@Temporal(value = TemporalType.DATE)
	protected List<Date> treatmentDates;

	public List<Date> getTreatmentDates() {
		return treatmentDates;
	}

	public void setTreatmentDates(List<Date> treatmentDates) {
		this.treatmentDates = treatmentDates;
	}

	@Override
	public <T> T export(ITreatmentExporter<T> visitor) {
		return visitor.exportRadiology(this.getId(), 
				   this.getPatient().getId(),
				   this.getProvider().getId(),
		   		   this.getDiagnosis(),
		   		   this.getTreatmentDates());
	}
	
}
