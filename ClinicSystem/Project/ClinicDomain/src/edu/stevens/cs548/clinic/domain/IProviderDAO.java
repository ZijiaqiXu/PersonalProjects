package edu.stevens.cs548.clinic.domain;


public interface IProviderDAO {
	
	public static class ProviderExn extends Exception {
		private static final long serialVersionUID = 1L;
		public ProviderExn (String msg) {
			super(msg);
		}
	}

	public long addProvider (Provider provider) throws ProviderExn;
	
	public Provider getProviderByNPI (long pid) throws ProviderExn;
	
	public Provider getProvider (long id) throws ProviderExn;
	
	public void deleteProviders();

}
