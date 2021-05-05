package edu.stevens.cs548.clinic.service.ejb;

import java.util.Date;
import java.util.List;

import edu.stevens.cs548.clinic.domain.ITreatmentExporter;
import edu.stevens.cs548.clinic.service.dto.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.dto.ObjectFactory;
import edu.stevens.cs548.clinic.service.dto.RadiologyTreatmentType;
import edu.stevens.cs548.clinic.service.dto.SurgeryTreatmentType;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;

public class TreatmentExporter implements ITreatmentExporter<TreatmentDto> {
	
	private ObjectFactory factory = new ObjectFactory();
	
	@Override
	public TreatmentDto exportDrugTreatment(long tid, long patientId, long providerId, String diagnosis,
			String drug, float dosage) {
		TreatmentDto dto = factory.createTreatmentDto();
		dto.setId(tid);
		dto.setPatient(patientId);
		dto.setProvider(providerId);
		dto.setDiagnosis(diagnosis);
		
		DrugTreatmentType drugInfo = factory.createDrugTreatmentType();
		drugInfo.setDosage(Float.toString(dosage));
		drugInfo.setDrug(drug);
		dto.setDrugTreatment(drugInfo);
		return dto;
	}

	@Override
	public TreatmentDto exportRadiology(long tid, long patientId, long providerId, String diagnosis, List<Date> dates) {
		TreatmentDto dto = factory.createTreatmentDto();
		dto.setId(tid);
		dto.setPatient(patientId);
		dto.setProvider(providerId);
		dto.setDiagnosis(diagnosis);
		
		RadiologyTreatmentType radioInfo = factory.createRadiologyTreatmentType();
		radioInfo.getDate().addAll(dates);
		dto.setRadiologyTreatment(radioInfo);
		return dto;
	}

	@Override
	public TreatmentDto exportSurgery(long tid, long patientId, long providerId, String diagnosis, Date date) {
		TreatmentDto dto = factory.createTreatmentDto();
		dto.setId(tid);
		dto.setPatient(patientId);
		dto.setProvider(providerId);
		dto.setDiagnosis(diagnosis);
		
		SurgeryTreatmentType surgeryInfo = factory.createSurgeryTreatmentType();
		surgeryInfo.setDate(date);
		dto.setSurgeryTreatment(surgeryInfo);
		return dto;
	}
	
}