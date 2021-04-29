package edu.stevens.cs548.clinic.service.representations;

import javax.ws.rs.core.UriInfo;

import edu.stevens.cs548.clinic.service.dto.PatientDto;
import edu.stevens.cs548.clinic.service.dto.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;

public class RepresentationFactory {
	
	public PatientRepresentation createPatientRepresentation(PatientDto dto, UriInfo uriInfo) {
		return new PatientRepresentation(dto, uriInfo);
	}

	public ProviderRepresentation createProviderRepresentation(ProviderDto dto, UriInfo uriInfo) {
		return new ProviderRepresentation(dto, uriInfo);
	}
	
	public TreatmentRepresentation createTreatmentRepresentation(TreatmentDto dto, UriInfo uriInfo) {
		return new TreatmentRepresentation(dto, uriInfo);
	}

}
