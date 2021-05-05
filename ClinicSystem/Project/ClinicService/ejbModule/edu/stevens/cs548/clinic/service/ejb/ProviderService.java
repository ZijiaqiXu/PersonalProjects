package edu.stevens.cs548.clinic.service.ejb;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;	
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.persistence.EntityManager;

import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.IProviderDAO;
import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;
import edu.stevens.cs548.clinic.domain.IProviderFactory;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDAO;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.Provider.ProviderType;
import edu.stevens.cs548.clinic.domain.ProviderDAO;
import edu.stevens.cs548.clinic.domain.ProviderFactory;
import edu.stevens.cs548.clinic.domain.Treatment;
import edu.stevens.cs548.clinic.domain.TreatmentFactory;
import edu.stevens.cs548.clinic.service.dto.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.ProviderSpecType;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.util.ProviderDtoFactory;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientServiceExn;

/**
 * Session Bean implementation class ProviderService
 */
@RequestScoped
public class ProviderService implements IProviderService {
	
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(ProviderService.class.getCanonicalName());

	private IProviderFactory providerFactory;
	
	private ProviderDtoFactory providerDtoFactory;

	private IProviderDAO providerDAO;
	
	private IPatientDAO patientDAO;
	
	private TreatmentFactory treatmentFactory;
	
	@Inject @ClinicDomain
	private EntityManager em;
	
	/**
	 * Default constructor.
	 */
	public ProviderService() {
		// Initialize factories
		providerFactory = new ProviderFactory();
		providerDtoFactory = new ProviderDtoFactory();
		treatmentFactory = new TreatmentFactory();
	}
	
	@PostConstruct
	void init() {
		providerDAO = new ProviderDAO(em);
		patientDAO = new PatientDAO(em);
	}

	/**
	 * @see IProviderService#addProvider(ProviderDto dto)
	 */
	@Override
	public long addProvider(ProviderDto dto) throws ProviderServiceExn {
		// Use factory to create Provider entity, and persist with DAO
		try {
			Provider provider = providerFactory.createProvider(dto.getNpi(), dto.getName(), getType(dto.getProviderSpec()));
			return providerDAO.addProvider(provider);
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}

	/**
	 * @see IProviderService#getProvider(long)
	 */
	@Override
	public ProviderDto getProvider(long id) throws ProviderServiceExn {
		try {
			return providerDtoFactory.createProviderDto(providerDAO.getProvider(id));
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn(e.toString());
		}
	}

	/**
	 * @see IProviderService#getProviderByNPI(long)
	 */
	@Override
	public ProviderDto getProviderByNPI(long npi) throws ProviderServiceExn {
		try {
			return providerDtoFactory.createProviderDto(providerDAO.getProviderByNPI(npi));
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}
		
	@Resource(mappedName = "jms/TreatmentPool")
	private ConnectionFactory treatmentConnFactory;
	
	@Resource(mappedName = "jms/Treatment")
	private Topic treatmentTopic;
	
	@Override
	public long addTreatment(TreatmentDto dto) throws PatientServiceExn, ProviderServiceExn {
		try {
			Provider provider = providerDAO.getProvider(dto.getProvider());
			Patient patient = patientDAO.getPatient(dto.getPatient());
			Treatment treatment;
			if (dto.getDrugTreatment() != null) {
				treatment = treatmentFactory.createDrugTreatment(dto.getDiagnosis(), dto.getDrugTreatment().getDrug(),
						Float.parseFloat(dto.getDrugTreatment().getDosage()));
			} else if (dto.getRadiologyTreatment() != null) {
				treatment = treatmentFactory.createRadiologyTreatment(dto.getDiagnosis(), dto.getRadiologyTreatment().getDate());
			} else {
				treatment = treatmentFactory.createSurgeryTreatment(dto.getDiagnosis(), dto.getSurgeryTreatment().getDate());
			}
			// provider object handles the associations
			long treatmentId = provider.addTreatment(patient, treatment);
			dto.setId(treatmentId);
			
			Connection conn = null;
			try {
				conn = treatmentConnFactory.createConnection();
				Session session = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);
				MessageProducer producer = session.createProducer(treatmentTopic);
				
				ObjectMessage msg = session.createObjectMessage(dto);
				if (dto.getDrugTreatment() != null) {
					msg.setStringProperty("treatmentType", "Drug");
				}
				
				producer.send(msg);
			} catch (JMSException e) {
				logger.severe("JMS error: " + e);
			} finally {
				try {
					conn.close();
				} catch (JMSException e) {
					logger.severe("Error closing JMS connection: " + e);
				}
			}
			
			return treatmentId;
		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString());
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn(e.toString());
		}
	}

	@Override
	public TreatmentDto getTreatment(long id, long tid)
			throws ProviderNotFoundExn, TreatmentNotFoundExn, ProviderServiceExn {
		// Export treatment DTO from Provider aggregate
		try {
			Provider provider = providerDAO.getProvider(id);
			TreatmentExporter visitor = new TreatmentExporter();
			return provider.exportTreatment(tid, visitor);
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn(e.toString());
		} catch (TreatmentExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}

	public static ProviderType getType(ProviderSpecType providerType) {
		switch (providerType) {
		case INTERNAL:
			return ProviderType.INTERNIST;
		case RADIOLOGY:
			return ProviderType.RADIOLOGIST;
		case SURGERY:
			return ProviderType.SURGEON;
		default:
			throw new IllegalStateException("Unrecognized provider type: "+providerType);
		}
	}

}
