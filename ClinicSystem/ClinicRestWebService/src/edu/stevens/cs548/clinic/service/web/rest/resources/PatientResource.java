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

import edu.stevens.cs548.clinic.service.dto.PatientDto;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.ejb.IPatientService;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.representations.PatientRepresentation;
import edu.stevens.cs548.clinic.service.representations.RepresentationFactory;
import edu.stevens.cs548.clinic.service.representations.TreatmentRepresentation;

@Path("/patient")
@RequestScoped
public class PatientResource {
	
	final static Logger logger = Logger.getLogger(PatientResource.class.getCanonicalName());
	
    @Context
    private UriInfo uriInfo;
    
    private RepresentationFactory factory = new RepresentationFactory();
    
    /**
     * Default constructor. 
     */
    public PatientResource() {
    }
    
    @Inject
    private IPatientService patientService;


    @POST
    @Consumes("application/xml")
    @Transactional
    public Response addPatient(PatientRepresentation patientRep) {
    	try {
    		PatientDto dto = patientRep.getPatientDto();
    		long id = patientService.addPatient(dto);
    		UriBuilder ub = uriInfo.getAbsolutePathBuilder().path("{id}");
    		URI url = ub.build(Long.toString(id));
    		return Response.created(url).build();
    	} catch (PatientServiceExn e) {    		
    		logger.log(Level.SEVERE, "Patient Service failed: add patient", e);
		
			WebApplicationException ex = new WebApplicationException(e.getMessage(), Response.Status.BAD_REQUEST);
			ex.initCause(e);
			throw ex;
    	}
    }
    
	/**
	 * Query methods for patient resources.
	 */
	@GET
	@Path("{id}")
	@Produces("application/xml")
	public PatientRepresentation getPatient(@PathParam("id") String id) {
		try {
			long key = Long.parseLong(id);
			PatientDto patientDTO = patientService.getPatient(key);
			PatientRepresentation patientRep = factory.createPatientRepresentation(patientDTO, uriInfo);
			return patientRep;
		} catch (PatientServiceExn e) {			
			logger.log(Level.SEVERE, "Patient Service failed: get patient by primary key", e);
		
			WebApplicationException ex = new WebApplicationException(e.getMessage(), Response.Status.NOT_FOUND);
			ex.initCause(e);
			throw ex;
		}
	}
    
	@GET
	@Path("byPatientId")
	@Produces("application/xml")
	public PatientRepresentation getPatientByPatientId(@QueryParam("id") String patientId) {
		try {
			long pid = Long.parseLong(patientId);
			PatientDto patientDTO = patientService.getPatientByPatId(pid);
			PatientRepresentation patientRep = factory.createPatientRepresentation(patientDTO, uriInfo);
			return patientRep;
		} catch (PatientServiceExn e) {
			logger.log(Level.SEVERE, "Patient Service failed: get patient by patient id", e);
			
			WebApplicationException ex = new WebApplicationException(e.getMessage(), Response.Status.NOT_FOUND);
			ex.initCause(e);
			throw ex;
		}
	}
    
    @GET
    @Path("{id}/treatments/{tid}")
    @Produces("application/xml")
    public TreatmentRepresentation getPatientTreatment(@PathParam("id") String id, @PathParam("tid") String tid) {
    	try {
    		TreatmentDto treatment = patientService.getTreatment(Long.parseLong(id), Long.parseLong(tid)); 
    		TreatmentRepresentation treatmentRep = factory.createTreatmentRepresentation(treatment, uriInfo);
    		return treatmentRep;
    	} catch (PatientServiceExn e) {
			logger.log(Level.SEVERE, "Patient Service failed: get treatment", e);
			
			WebApplicationException ex = new WebApplicationException(e.getMessage(), Response.Status.NOT_FOUND);
			ex.initCause(e);
			throw ex;
    	}
    }

}