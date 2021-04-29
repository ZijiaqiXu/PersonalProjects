package edu.stevens.cs548.clinic.service.ejb;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.IPatientFactory;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDAO;
import edu.stevens.cs548.clinic.domain.PatientFactory;
import edu.stevens.cs548.clinic.service.dto.PatientDto;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.util.PatientDtoFactory;

/**
 * Session Bean implementation class PatientService
 */
@RequestScoped
public class PatientService implements IPatientService {
	
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(PatientService.class.getCanonicalName());

	private IPatientFactory patientFactory;
	
	private PatientDtoFactory patientDtoFactory;

	private IPatientDAO patientDAO;
	
	@Inject @ClinicDomain
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public PatientService() {
		// Initialize factories
		patientFactory = new PatientFactory();
		patientDtoFactory = new PatientDtoFactory();
	}
	
	
	@PostConstruct
	void init() {
		patientDAO = new PatientDAO(em);
	}
	
	
	@Override
	public long addPatient(PatientDto dto) throws PatientServiceExn {
		// Use factory to create patient entity, and persist with DAO
		try {
			Patient patient = patientFactory.createPatient(dto.getPatientId(), dto.getName());
			return patientDAO.addPatient(patient);
		} catch (PatientExn e) {
			throw new PatientServiceExn(e.toString());
		}
	}

	
	@Override
	public PatientDto getPatient(long id) throws PatientServiceExn {
		try {
			return patientDtoFactory.createPatientDto(patientDAO.getPatient(id));
		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString());
		}
	}

	
	@Override
	public PatientDto getPatientByPatId(long pid) throws PatientServiceExn {
		try {
			return patientDtoFactory.createPatientDto(patientDAO.getPatientByPatientId(pid));
		} catch (PatientExn e) {
			throw new PatientServiceExn(e.toString());
		}
	}

	
	@Override
	public TreatmentDto getTreatment(long id, long tid)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		// Export treatment DTO from patient aggregate
		try {
			Patient patient = patientDAO.getPatient(id);
			TreatmentExporter visitor = new TreatmentExporter();
			return patient.exportTreatment(tid, visitor);
		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString());
		} catch (TreatmentExn e) {
			throw new PatientServiceExn(e.toString());
		}
	}
	


}
