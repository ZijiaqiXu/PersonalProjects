package edu.stevens.cs548.clinic.service.representations;

import java.util.List;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;

import edu.stevens.cs548.clinic.service.dto.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.ProviderSpecType;
import edu.stevens.cs548.clinic.service.dto.util.ProviderDtoFactory;
import edu.stevens.cs548.clinic.service.web.rest.data.ProviderSpec;
import edu.stevens.cs548.clinic.service.web.rest.data.ProviderType;
import edu.stevens.cs548.clinic.service.web.rest.data.dap.LinkType;
import edu.stevens.cs548.clinic.service.web.rest.data.dap.ObjectFactory;

@XmlRootElement
public class ProviderRepresentation extends ProviderType {
	
	public List<LinkType> getLinksTreatments() {
		return this.getTreatments();
	}
	
	private static ObjectFactory linkFactory = new ObjectFactory();
	
	public static LinkType getProviderLink(long id, UriInfo uriInfo) {
		UriBuilder ub = uriInfo.getBaseUriBuilder();
		ub.path("provider/{id}");
		String providerURI = ub.build(id).toString();
		
		LinkType link = linkFactory.createLinkType();
		link.setUrl(providerURI);
		link.setRelation(Representation.RELATION_PROVIDER);
		link.setMediaType(Representation.MEDIA_TYPE);
		return link;
	}
	
	private ProviderDtoFactory providerDtoFactory;
	
	public ProviderRepresentation () {
		super();
		this.providerDtoFactory = new ProviderDtoFactory();
	}
	
	public ProviderRepresentation (ProviderDto dto, UriInfo uriInfo) {
		this();
		this.id = getProviderLink(dto.getId(), uriInfo);
		this.name = dto.getName();
		this.npi = dto.getNpi();
	
		List<LinkType> links = this.getLinksTreatments();
		for (long tid: dto.getTreatments()) {
			links.add(TreatmentRepresentation.getTreatmentLink(tid, uriInfo));
		}
	}
	
	public ProviderDto getProviderDto() {
		ProviderDto p = providerDtoFactory.createProviderDto();
		if (this.id != null) {
			p.setId(Representation.getId(this.id));
		}
		p.setNpi(this.npi);
		p.setName(this.name);
		if (this.spec != null) {
			p.setProviderSpec(toDtoSpecType(this.spec));
		}
		return p;
	}
	
	protected static ProviderSpec fromDtoSpecType(ProviderSpecType specType) {
		switch(specType) {
		case INTERNAL:
			return ProviderSpec.INTERNAL;
		case RADIOLOGY:
			return ProviderSpec.RADIOLOGY;
		case SURGERY:
			return ProviderSpec.SURGERY;
		default:
			throw new IllegalArgumentException("Unrecognized provider spec type: "+specType);
		}
	}

	protected static ProviderSpecType toDtoSpecType(ProviderSpec specType) {
		switch(specType) {
		case INTERNAL:
			return ProviderSpecType.INTERNAL;
		case RADIOLOGY:
			return ProviderSpecType.RADIOLOGY;
		case SURGERY:
			return ProviderSpecType.SURGERY;
		default:
			throw new IllegalArgumentException("Unrecognized provider spec: "+specType);
		}
	}

}
