package edu.stevens.cs548.clinic.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import edu.stevens.cs548.clinic.service.dto.PatientDto;
import edu.stevens.cs548.clinic.service.dto.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.ProviderSpecType;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.util.PatientDtoFactory;
import edu.stevens.cs548.clinic.service.dto.util.ProviderDtoFactory;
import edu.stevens.cs548.clinic.service.dto.util.TreatmentDtoFactory;
import edu.stevens.cs548.clinic.service.ejb.IPatientService;
import edu.stevens.cs548.clinic.service.ejb.IProviderService;

/**
 * Session Bean implementation class TestBean
 */
@Singleton
@LocalBean
@Startup
public class InitBean {

	private static Logger logger = Logger.getLogger(InitBean.class.getCanonicalName());

	/**
	 * Default constructor.
	 */
	public InitBean() {
	}
	
	/*
	 * Do NOT use @PersistenceContext in this bean!
	 */
	
	@Inject
	private IPatientService patientService;
	
	@Inject
	private IProviderService providerService;

	@PostConstruct
	private void init() {
		/*
		 * Put your testing logic here. Use the logger to display testing output in the server logs.
		 */
		logger.info("Your name here: Zijiaqi Xu");

		try {
			
			PatientDtoFactory patientFactory = new PatientDtoFactory();
			ProviderDtoFactory providerFactory = new ProviderDtoFactory();
			TreatmentDtoFactory treatmentFactory = new TreatmentDtoFactory();
			
			/*
			 * Clear the database and populate with fresh data.
			 * 
			 * If we ensure that deletion of patients cascades deletes of treatments,
			 * then we only need to delete patients.
			 */

			// test adding patients
			PatientDto john = patientFactory.createPatientDto();
			john.setPatientId(12345678L);
			john.setName("John Doe");
			long johnId = patientService.addPatient(john);
			
			logger.info("Added patient " + john.getName() + " with id "+johnId);
			
			PatientDto lisa = patientFactory.createPatientDto();
			lisa.setPatientId(12342348L);
			lisa.setName("Lisa Elizabeth");
			
			long lisaId = patientService.addPatient(lisa);
			
			logger.info("Added patient " + lisa.getName() + " with id " + lisaId);
			
			// test retrieving patients
			// 1. by primary key
			PatientDto johnRe = patientService.getPatient(johnId);
			
			logger.info("Retrieved patient by Primary key: " + johnRe.getName() + " with id " + johnRe.getId());
			
			// 2. by patient Id
			long lisaPid = lisa.getPatientId();
			PatientDto lisaRe = patientService.getPatientByPatId(lisaPid);
			
			logger.info("Retrieved patient by Patient Id: " + lisaRe.getName() + " with id " + lisaRe.getId());
			
			// test adding providers
			ProviderDto dominic = providerFactory.createProviderDto();
			dominic.setNpi(231214412L);
			dominic.setName("Dominic Duggan");
			dominic.setProviderSpec(ProviderSpecType.INTERNAL);
			long dominicId = providerService.addProvider(dominic);
			
			logger.info("Added provider " + dominic.getName() + " with id " + dominicId);
			
			ProviderDto zijiaqi = providerFactory.createProviderDto();
			zijiaqi.setNpi(232344412L);
			zijiaqi.setName("Zijiaqi Xu");
			zijiaqi.setProviderSpec(ProviderSpecType.SURGERY);
			long zijiaqiId = providerService.addProvider(zijiaqi);
			
			logger.info("Added provider " + zijiaqi.getName() + " with id " + zijiaqiId);
			
			ProviderDto folge = providerFactory.createProviderDto();
			folge.setNpi(232344532L);
			folge.setName("Michael Folge");
			folge.setProviderSpec(ProviderSpecType.RADIOLOGY);
			long folgeId = providerService.addProvider(folge);
			
			logger.info("Added provider " + folge.getName() + " with id " + folgeId);
			
			/*
			 * tests
			// test retrieving provider
			// 1. by primary key
			ProviderDto dominicRe = providerService.getProvider(dominicId);
			
			logger.info("Retrieved provider by Primary key " + dominicRe.getName() + " with id " + dominicRe.getId());
			
			// 2. by NPI
			long zijiaqiNpi = zijiaqi.getNpi();
			ProviderDto zijiaqiRe = providerService.getProviderByNPI(zijiaqiNpi);
			
			logger.info("Retrieved provider by NPI " + zijiaqiRe.getName() + " with id " + zijiaqiRe.getId());
			
			// test adding treatments
			// drug treatment
			TreatmentDto drugTreatmentDto = treatmentFactory.createDrugTreatmentDto();
			drugTreatmentDto.setProvider(dominicId);
			drugTreatmentDto.setPatient(lisaId);
			drugTreatmentDto.setDiagnosis("diagnosis1");
			drugTreatmentDto.getDrugTreatment().setDrug("alcohol");
			drugTreatmentDto.getDrugTreatment().setDosage("25.2");
			long tid = providerService.addTreatment(drugTreatmentDto);
			
			logger.info("Added drug treatment with id " + tid + " to provider #" + dominicId + " and patient #" + lisaId);
			
			drugTreatmentDto = treatmentFactory.createDrugTreatmentDto();
			drugTreatmentDto.setProvider(dominicId);
			drugTreatmentDto.setPatient(lisaId);			
			drugTreatmentDto.setDiagnosis("diagnosis2");
			drugTreatmentDto.getDrugTreatment().setDrug("alcohol");
			drugTreatmentDto.getDrugTreatment().setDosage("100.25");
			tid = providerService.addTreatment(drugTreatmentDto);		
			
			logger.info("Added drug treatment with id " + tid + " to provider #" + dominicId + " and patient #" + lisaId);
			
			// surgery treatment
			GregorianCalendar calendar = new GregorianCalendar(2019, 12, 28, 14, 25);
			Date date = calendar.getGregorianChange();
			TreatmentDto surgeryTreatmentDto = treatmentFactory.createSurgeryTreatmentDto();
			surgeryTreatmentDto.setProvider(zijiaqiId);
			surgeryTreatmentDto.setPatient(johnId);
			surgeryTreatmentDto.setDiagnosis("diagnosis3");
			surgeryTreatmentDto.getSurgeryTreatment().setDate(date);
			tid = providerService.addTreatment(surgeryTreatmentDto);
			
			logger.info("Added surgery treatment with id " + tid + " to provider #" + zijiaqiId + " and patient #" + johnId);
			
			// radiology treatment
			List<Date> dates = new ArrayList<>();
			calendar = new GregorianCalendar(2019, 12, 22, 12, 20);
			dates.add(calendar.getGregorianChange());
			calendar = new GregorianCalendar(2020, 1, 14, 15, 30);
			dates.add(calendar.getGregorianChange());
			calendar = new GregorianCalendar(2020, 2, 8, 10, 8);
			dates.add(calendar.getGregorianChange());
			TreatmentDto radiologyTreatmentDto = treatmentFactory.createRadiologyTreatmentDto();
			radiologyTreatmentDto.setProvider(folgeId);
			radiologyTreatmentDto.setPatient(johnId);
			radiologyTreatmentDto.setDiagnosis("diagnosis4");
			radiologyTreatmentDto.getRadiologyTreatment().getDate().addAll(dates);
			tid = providerService.addTreatment(radiologyTreatmentDto);
			
			logger.info("Added radiology treatment with id " + tid + " to provider #" + folgeId + " and patient #" + johnId);
			
			// test retrieving treatments
			// 1. from patient and provider
			// from provider
			dominicRe = providerService.getProvider(dominicId);
			List<Long> tids = dominicRe.getTreatments();
			logger.info("Retrieving " + tids.size() + " treatments from provider " + dominicRe.getName() + ": ");
			for (long treatmentId: tids) {
				logger.info("Retrieving treatment with id " + treatmentId);
				TreatmentDto tDto = providerService.getTreatment(dominicId, treatmentId);
				logger.info("Retrieved treatment with id " + tDto.getId());
			}
			
			// from patient
			lisaRe = patientService.getPatientByPatId(lisaPid);
			tids = lisaRe.getTreatments();
			logger.info("Retrieving " + tids.size() + " treatments from patient " + lisaRe.getName() + ": ");
			for (long treatmentId: tids) {
				logger.info("Retrieving treatment with id " + treatmentId);
				TreatmentDto tDto = patientService.getTreatment(lisaId, treatmentId);
				logger.info("Retrieved treatment with id " + tDto.getId());
			}
			
			// 2. for single treatment with primary key
			TreatmentDto tDto = patientService.getTreatment(johnId, tid);
			logger.info("Retrieved single treatment with id " + tDto.getId());
			* manual test end
			*/
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Service failed: ", e);
			
			IllegalStateException ex = new IllegalStateException("Failed to add patient record.");
			ex.initCause(e);
			throw ex;
		}
			
	}
	

}
