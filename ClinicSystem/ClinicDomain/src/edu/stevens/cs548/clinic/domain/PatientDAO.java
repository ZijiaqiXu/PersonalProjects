package edu.stevens.cs548.clinic.domain;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class PatientDAO implements IPatientDAO {

	private EntityManager em;
	
	private TreatmentDAO treatmentDAO;
	
	public PatientDAO(EntityManager em) {
		this.em = em;
		this.treatmentDAO = new TreatmentDAO(em);
	}

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(PatientDAO.class.getCanonicalName());

	@Override
	public long addPatient(Patient patient) throws PatientExn {
		long pid = patient.getPatientId();
		Query query = em.createNamedQuery("CountPatientByPatientID").setParameter("pid", pid);
		Long numExisting = (Long) query.getSingleResult();
		
		if (numExisting < 1) {
			
			// Add to database (and sync with database to generate primary key)
			// Don't forget to initialize the patient aggregate with a treatment DAO
			em.persist(patient);
			em.flush();
			patient.setTreatmentDAO(this.treatmentDAO);
			
			return patient.getId();
		} else {
			throw new PatientExn("Insertion: Patient with patient id (" + pid + ") already exists.");
		}
	}

	@Override
	public Patient getPatient(long id) throws PatientExn {
		/*
		 * Retrieve patient using primary key
		 */
		Patient p = em.find(Patient.class, id);
		if (p == null) {
			throw new PatientExn("Patient not found: primary key = " + id);
		} else {
			p.setTreatmentDAO(this.treatmentDAO);
			return p;
		}
	}

	@Override
	public Patient getPatientByPatientId(long pid) throws PatientExn {
		/*
		 * Retrieve patient using query on patient id (secondary key)
		 */
		TypedQuery<Patient> query = em.createNamedQuery("SearchPatientByPatientID", Patient.class).setParameter("pid",pid);
		List<Patient> patients = query.getResultList();
		
		if (patients.size() > 1) {
			throw new PatientExn("Duplicate patient records: patient id = " + pid);
		} else if (patients.size() < 1) {
			throw new PatientExn("Patient not found: patient id = " + pid);
		} else {
			Patient p = patients.get(0);
			p.setTreatmentDAO(this.treatmentDAO);
			return p;
		}
	}
	
	@Override
	public void deletePatients() {
		Query update = em.createNamedQuery("RemoveAllPatients");
		update.executeUpdate();
	}

}
