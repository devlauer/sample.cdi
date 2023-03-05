package de.elnarion.sample.cdi.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@ApplicationScoped
public class GiftEntityManagerProducer {

	@PersistenceUnit(unitName = "cdi-test-unit")
	private EntityManagerFactory entityManagerFactory;

	@Produces
	@GiftDatabaseManager
	public EntityManager produceEntityManager() {
		if (entityManagerFactory != null)
			return this.entityManagerFactory.createEntityManager();
		return null;
	}

	public void dispose(@Disposes @GiftDatabaseManager EntityManager entityManager) {
		if (entityManager.isOpen()) {
			entityManager.close();
		}
	}

}
