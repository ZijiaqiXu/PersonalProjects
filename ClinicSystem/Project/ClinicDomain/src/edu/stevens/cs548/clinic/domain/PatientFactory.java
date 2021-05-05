package edu.stevens.cs548.clinic.domain;

public class PatientFactory implements IPatientFactory {

	@Override
	public Patient createPatient(long pid, String name) throws IPatientDAO.PatientExn {
			Patient p = new Patient();
			p.setPatientId(pid);
			p.setName(name);
			return p;
	}
	
}
