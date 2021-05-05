package edu.stevens.cs548.clinic.domain;

import java.util.Date;
import java.util.List;

public class TreatmentFactory implements ITreatmentFactory {
	
	/*
	 * Patient and provider fields are set when the treatment is added (see Provider).
	 * Id field is set when the treatment entity is synced with the database (see TreatmentDAO).
	 */

	@Override
	public Treatment createDrugTreatment(String diagnosis, String drug, float dosage) {
		DrugTreatment treatment = new DrugTreatment();
		treatment.setDiagnosis(diagnosis);
		treatment.setDrug(drug);
		treatment.setDosage(dosage);
		return treatment;
	}
	
	@Override
	public Treatment createSurgeryTreatment(String diagnosis, Date surgeryDate) {
		SurgeryTreatment treatment = new SurgeryTreatment();
		treatment.setDiagnosis(diagnosis);
		treatment.setSurgeryDate(surgeryDate);
		return treatment;
	}
	
	@Override
	public Treatment createRadiologyTreatment(String diagnosis, List<Date> dates) {
		RadiologyTreatment treatment = new RadiologyTreatment();
		treatment.setDiagnosis(diagnosis);
		treatment.setTreatmentDates(dates);
		return treatment;
	}
}
