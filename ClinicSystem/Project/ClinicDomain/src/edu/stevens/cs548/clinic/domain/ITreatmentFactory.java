package edu.stevens.cs548.clinic.domain;

import java.util.Date;
import java.util.List;

public interface ITreatmentFactory {
	
	public Treatment createDrugTreatment (String diagnosis, String drug, float dosage);
	
	public Treatment createSurgeryTreatment (String diagnosis, Date surgeryDate);
	
	public Treatment createRadiologyTreatment (String diagnosis, List<Date> dates);

}
