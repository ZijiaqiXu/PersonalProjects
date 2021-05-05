package edu.stevens.cs548.clinic.domain;

public interface IProviderFactory {
	
	public Provider createProvider (long npi, String name, Provider.ProviderType type) throws IProviderDAO.ProviderExn;

}
