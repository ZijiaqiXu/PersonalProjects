package edu.stevens.cs548.clinic.domain;


public interface IPatientFactory {
	
	public Patient createPatient (long pid, String name) throws IPatientDAO.PatientExn;
	
}
