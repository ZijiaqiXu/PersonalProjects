package edu.stevens.cs548.clinic.service.ejb;

import edu.stevens.cs548.clinic.service.dto.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientServiceExn;

// @Local if using EJB
public interface IProviderService {
	
	public class ProviderServiceExn extends Exception {
		private static final long serialVersionUID = 1L;
		public ProviderServiceExn (String m) {
			super(m);
		}
	}
	public class ProviderNotFoundExn extends ProviderServiceExn {
		private static final long serialVersionUID = 1L;
		public ProviderNotFoundExn (String m) {
			super(m);
		}
	}
	public class TreatmentNotFoundExn extends ProviderServiceExn {
		private static final long serialVersionUID = 1L;
		public TreatmentNotFoundExn (String m) {
			super(m);
		}
	}
	
	public long addProvider(ProviderDto dto) throws ProviderServiceExn;

	public ProviderDto getProvider(long id) throws ProviderServiceExn;
	
	public ProviderDto getProviderByNPI(long npi) throws ProviderServiceExn;
		
	public long addTreatment(TreatmentDto dto) throws PatientServiceExn, ProviderServiceExn;
	
	public TreatmentDto getTreatment(long id, long tid) throws ProviderNotFoundExn, TreatmentNotFoundExn, ProviderServiceExn;

}
