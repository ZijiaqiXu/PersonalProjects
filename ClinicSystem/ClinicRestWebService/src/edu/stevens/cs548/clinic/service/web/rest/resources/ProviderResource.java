package edu.stevens.cs548.clinic.service.web.rest.resources;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import edu.stevens.cs548.clinic.service.dto.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.ejb.IProviderService;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.representations.ProviderRepresentation;
import edu.stevens.cs548.clinic.service.representations.RepresentationFactory;
import edu.stevens.cs548.clinic.service.representations.TreatmentRepresentation;

@Path("/provider")
@RequestScoped
public class ProviderResource {
	
	final static Logger logger = Logger.getLogger(ProviderResource.class.getCanonicalName());
	
    @Context
    private UriInfo uriInfo;
    
    private RepresentationFactory factory = new RepresentationFactory();
    
    /**
     * Default constructor. 
     */
    public ProviderResource() {
    }
    
    @Inject
    private IProviderService providerService;


	@POST
	@Consumes("application/xml")
	@Transactional
    public Response addProvider(ProviderRepresentation providerRep) {
    	try {
    		ProviderDto dto = providerRep.getProviderDto();
    		long id = providerService.addProvider(dto);
    		UriBuilder ub = uriInfo.getAbsolutePathBuilder().path("{id}");
    		URI url = ub.build(Long.toString(id));
    		return Response.created(url).build();
    	} catch (ProviderServiceExn e) {    		
    		logger.log(Level.SEVERE, "Provider Service failed: add provider", e);
		
			WebApplicationException ex = new WebApplicationException(e.getMessage(), Response.Status.BAD_REQUEST);
			ex.initCause(e);
			throw ex;
    	}
    }
    
	/**
	 * Query methods for provider resources.
	 */
	@GET
	@Path("{id}")
	@Produces("application/xml")
	public ProviderRepresentation getProvider(@PathParam("id") String id) {
		try {
			long key = Long.parseLong(id);
			ProviderDto dto = providerService.getProvider(key);
			ProviderRepresentation providerRep = factory.createProviderRepresentation(dto, uriInfo);
			return providerRep;
		} catch (ProviderServiceExn e) {			
			logger.log(Level.SEVERE, "Provider Service failed: get provider by primary key", e);
		
			WebApplicationException ex = new WebApplicationException(e.getMessage(), Response.Status.NOT_FOUND);
			ex.initCause(e);
			throw ex;
		}
	}
    
	@GET
	@Path("byNPI")
	@Produces("application/xml")
	public ProviderRepresentation getProviderByNpi(@QueryParam("npi") String npi) {
		try {
			long pid = Long.parseLong(npi);
			ProviderDto dto = providerService.getProviderByNPI(pid);
			ProviderRepresentation providerRep = factory.createProviderRepresentation(dto, uriInfo);
			return providerRep;
		} catch (ProviderServiceExn e) {
			logger.log(Level.SEVERE, "Provider Service failed: get provider by npi", e);
			
			WebApplicationException ex = new WebApplicationException(e.getMessage(), Response.Status.NOT_FOUND);
			ex.initCause(e);
			throw ex;
		}
	}
	
	@POST
	@Path("{id}/treatments")
	@Consumes("application/xml")
	@Transactional
	public Response addTreatment(@PathParam("id") String id, TreatmentRepresentation treatmentRep) {
    	try {
    		long tid = providerService.addTreatment(treatmentRep.getTreatment());
    		UriBuilder ub = uriInfo.getAbsolutePathBuilder().path("{id}/treatments/{tid}");
    		URI url = ub.build(id, Long.toString(tid));
    		return Response.created(url).build();
    	} catch (ProviderServiceExn e) {			
    		logger.log(Level.SEVERE, "Provider Service failed: add treatment", e);
		
			WebApplicationException ex = new WebApplicationException(e.getMessage(), Response.Status.BAD_REQUEST);
			ex.initCause(e);
			throw ex;
    	} catch (PatientServiceExn e) {
    		logger.log(Level.SEVERE, "Provider Service failed: add treatment", e);
    		
			WebApplicationException ex = new WebApplicationException(e.getMessage(), Response.Status.BAD_REQUEST);
			ex.initCause(e);
			throw ex;
    	}
	}
    
	@GET
	@Path("{id}/treatments/{tid}")
	@Produces("application/xml")
    public TreatmentRepresentation getProviderTreatment(@PathParam("id") String id, @PathParam("tid") String tid) {
    	try {
    		TreatmentDto treatment = providerService.getTreatment(Long.parseLong(id), Long.parseLong(tid)); 
    		TreatmentRepresentation treatmentRep = factory.createTreatmentRepresentation(treatment, uriInfo);
    		return treatmentRep;
    	} catch (ProviderServiceExn e) {
			logger.log(Level.SEVERE, "Provider Service failed: get treatment", e);
			
			WebApplicationException ex = new WebApplicationException(e.getMessage(), Response.Status.NOT_FOUND);
			ex.initCause(e);
			throw ex;
    	}
    }

}