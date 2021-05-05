package edu.stevens.cs548.clinic.service.dto.util;

import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.service.dto.PatientDto;
import edu.stevens.cs548.clinic.service.dto.ObjectFactory;

public class PatientDtoFactory {
	
	ObjectFactory factory;
	
	public PatientDtoFactory() {
		factory = new ObjectFactory();
	}
	
	public PatientDto createPatientDto () {
		return factory.createPatientDto();
	}
	
	public PatientDto createPatientDto (Patient p) {
		PatientDto d = factory.createPatientDto();
		/*
		 * Initialize the fields of the DTO.
		 */
		d.setId(p.getId());
		d.setName(p.getName());
		d.setPatientId(p.getPatientId());
		d.getTreatments().addAll(p.getTreatmentIds());
		return d;
	}

}
