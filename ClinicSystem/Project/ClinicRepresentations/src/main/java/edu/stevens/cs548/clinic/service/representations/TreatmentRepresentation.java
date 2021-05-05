package edu.stevens.cs548.clinic.service.representations;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;

import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.util.TreatmentDtoFactory;
import edu.stevens.cs548.clinic.service.web.rest.data.ObjectFactory;
import edu.stevens.cs548.clinic.service.web.rest.data.TreatmentType;
import edu.stevens.cs548.clinic.service.web.rest.data.dap.LinkType;

@XmlRootElement
public class TreatmentRepresentation extends TreatmentType {
	
	private ObjectFactory repFactory = new ObjectFactory();

	public LinkType getLinkPatient() {
		return this.getPatient();
	}
	
	public LinkType getLinkProvider() {
		return this.getProvider();
	}
	
	private static edu.stevens.cs548.clinic.service.web.rest.data.dap.ObjectFactory linkFactory = 
			new edu.stevens.cs548.clinic.service.web.rest.data.dap.ObjectFactory();
	
	public static LinkType getTreatmentLink(long tid, UriInfo uriInfo) {
		UriBuilder ub = uriInfo.getBaseUriBuilder();
		ub.path("treatment");
		UriBuilder ubTreatment = ub.clone().path("{tid}");
		String treatmentURI = ubTreatment.build(Long.toString(tid)).toString();
	
		LinkType link = linkFactory.createLinkType();
		link.setUrl(treatmentURI);
		link.setRelation(Representation.RELATION_TREATMENT);
		link.setMediaType(Representation.MEDIA_TYPE);
		return link;
	}
	
	private TreatmentDtoFactory treatmentDtoFactory;
	
	public TreatmentRepresentation() {
		super();
		treatmentDtoFactory = new TreatmentDtoFactory();
	}
	
	public TreatmentRepresentation (TreatmentDto dto, UriInfo uriInfo) {
		this();
		this.id = getTreatmentLink(dto.getId(), uriInfo);
		this.patient =  PatientRepresentation.getPatientLink(dto.getPatient(), uriInfo);
		this.provider = ProviderRepresentation.getProviderLink(dto.getProvider(), uriInfo);
		
		this.diagnosis = dto.getDiagnosis();
		
		if (dto.getDrugTreatment() != null) {
			this.drugTreatment = repFactory.createDrugTreatmentType();
			this.drugTreatment.setName(dto.getDrugTreatment().getDrug());
			this.drugTreatment.setDosage(Float.parseFloat(dto.getDrugTreatment().getDosage()));
		} else if (dto.getSurgeryTreatment() != null) {
			this.surgery = repFactory.createSurgeryType();
			this.surgery.setDate(dto.getSurgeryTreatment().getDate());
		} else {
			this.radiology = repFactory.createRadiologyType();
			this.radiology.getDate().addAll(dto.getRadiologyTreatment().getDate());
		}
	}

	public TreatmentDto getTreatment() {
		TreatmentDto m = null;
		if (this.getDrugTreatment() != null) {
			m = treatmentDtoFactory.createDrugTreatmentDto();
			if (id != null) {
				m.setId(Representation.getId(id));
			}
			m.setPatient(Representation.getId(patient));
			m.setProvider(Representation.getId(provider));
			m.setDiagnosis(diagnosis);
			m.getDrugTreatment().setDrug(drugTreatment.getName());
			m.getDrugTreatment().setDosage(Float.toString(drugTreatment.getDosage()));
		} else if (this.getSurgery() != null) {
			m = treatmentDtoFactory.createSurgeryTreatmentDto();
			if (id != null) {
				m.setId(Representation.getId(id));
			}
			m.setPatient(Representation.getId(patient));
			m.setProvider(Representation.getId(provider));
			m.setDiagnosis(this.diagnosis);
			m.getSurgeryTreatment().setDate(this.getSurgery().getDate());
		} else if (this.getRadiology() != null) {
			m = treatmentDtoFactory.createRadiologyTreatmentDto();
			if (id != null) {
				m.setId(Representation.getId(id));
			}
			m.setPatient(Representation.getId(patient));
			m.setProvider(Representation.getId(provider));
			m.setDiagnosis(this.diagnosis);
			m.getRadiologyTreatment().getDate().addAll(this.getRadiology().getDate());
		}
		return m;
	}

	
}
