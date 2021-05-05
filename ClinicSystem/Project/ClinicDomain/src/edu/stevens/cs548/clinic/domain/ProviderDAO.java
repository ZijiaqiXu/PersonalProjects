package edu.stevens.cs548.clinic.domain;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class ProviderDAO implements IProviderDAO {

	private EntityManager em;
	private TreatmentDAO treatmentDAO;
	
	public ProviderDAO(EntityManager em) {
		this.em = em;
		this.treatmentDAO = new TreatmentDAO(em);
	}

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(ProviderDAO.class.getCanonicalName());

	@Override
	public long addProvider(Provider provider) throws ProviderExn {
		long npi = provider.getNpi();
		Query query = em.createNamedQuery("CountProviderByNPI").setParameter("npi", npi);
		Long numExisting = (Long) query.getSingleResult();
		
		if (numExisting < 1) {
			em.persist(provider);
			em.flush();
			provider.setTreatmentDAO(treatmentDAO);
			
			return provider.getId();
		} else {
			throw new ProviderExn("Insertion: Provider with npi (" + npi + ") already exists.");
		}
	}

	@Override
	public Provider getProvider(long id) throws ProviderExn {
		Provider p = em.find(Provider.class, id);
		if (p == null) {
			throw new ProviderExn("Provider not found: primary key = " + id);
		} else {
			p.setTreatmentDAO(treatmentDAO);
			return p;
		}
	}

	@Override
	public Provider getProviderByNPI(long npi) throws ProviderExn {
		TypedQuery<Provider> query = em.createNamedQuery("SearchProviderByNPI", Provider.class).setParameter("npi", npi);
		List<Provider> providers = query.getResultList();
		
		if (providers.size() > 1) {
			throw new ProviderExn("Duplicate provider records: npi = " + npi);
		} else if (providers.size() < 1) {
			throw new ProviderExn("Provider not found: npi = " + npi);
		} else {
			Provider p = providers.get(0);
			p.setTreatmentDAO(treatmentDAO);
			return p;
		}
	}
	
	@Override
	public void deleteProviders() {
		Query update = em.createNamedQuery("RemoveAllProviders");
		update.executeUpdate();
	}

}
