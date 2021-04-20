package edu.stevens.cs548.clinic.service.dto.util;

import edu.stevens.cs548.clinic.domain.DrugTreatment;
import edu.stevens.cs548.clinic.domain.RadiologyTreatment;
import edu.stevens.cs548.clinic.domain.SurgeryTreatment;
import edu.stevens.cs548.clinic.service.dto.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.dto.ObjectFactory;
import edu.stevens.cs548.clinic.service.dto.RadiologyTreatmentType;
import edu.stevens.cs548.clinic.service.dto.SurgeryTreatmentType;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;

public class TreatmentDtoFactory {
	
	ObjectFactory factory;
	
	public TreatmentDtoFactory() {
		factory = new ObjectFactory();
	}
	
	public TreatmentDto createDrugTreatmentDto () {
		TreatmentDto t = factory.createTreatmentDto();
		DrugTreatmentType dt = factory.createDrugTreatmentType();
		t.setDrugTreatment(dt);
		return t;
	}
	
	public TreatmentDto createTreatmentDto (DrugTreatment t) {
		TreatmentDto dto = factory.createTreatmentDto();
		dto.setId(t.getId());
		dto.setPatient(t.getPatient().getId());
		dto.setProvider(t.getProvider().getId());
		dto.setDiagnosis(t.getDiagnosis());
		
		DrugTreatmentType dt = factory.createDrugTreatmentType();
		dt.setDosage(Float.toString(t.getDosage()));
		dt.setDrug(t.getDrug());
		dto.setDrugTreatment(dt);
		return dto;
	}
	
	public TreatmentDto createRadiologyTreatmentDto () {
		TreatmentDto t = factory.createTreatmentDto();
		RadiologyTreatmentType dt = factory.createRadiologyTreatmentType();
		t.setRadiologyTreatment(dt);
		return t;
	}
	
	public TreatmentDto createTreatmentDto (RadiologyTreatment t) {
		TreatmentDto dto = factory.createTreatmentDto();
		dto.setId(t.getId());
		dto.setPatient(t.getPatient().getId());
		dto.setProvider(t.getProvider().getId());
		dto.setDiagnosis(t.getDiagnosis());
		
		RadiologyTreatmentType dt = factory.createRadiologyTreatmentType();
		dt.getDate().addAll(t.getTreatmentDates());
		dto.setRadiologyTreatment(dt);
		return dto;
	}

	public TreatmentDto createSurgeryTreatmentDto () {
		TreatmentDto t = factory.createTreatmentDto();
		SurgeryTreatmentType dt = factory.createSurgeryTreatmentType();
		t.setSurgeryTreatment(dt);
		return t;
	}
	
	public TreatmentDto createTreatmentDto (SurgeryTreatment t) {
		TreatmentDto dto = factory.createTreatmentDto();
		dto.setId(t.getId());
		dto.setPatient(t.getPatient().getId());
		dto.setProvider(t.getProvider().getId());
		dto.setDiagnosis(t.getDiagnosis());
		
		SurgeryTreatmentType dt = factory.createSurgeryTreatmentType();
		dt.setDate(t.getSurgeryDate());
		dto.setSurgeryTreatment(dt);
		return dto;
	}
}
