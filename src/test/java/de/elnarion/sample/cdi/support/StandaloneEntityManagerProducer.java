package de.elnarion.sample.cdi.support;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import de.elnarion.sample.cdi.dao.GiftDatabaseManager;
import de.elnarion.sample.cdi.dao.GiftEntityManagerProducer;

public class StandaloneEntityManagerProducer extends GiftEntityManagerProducer {

	@Inject
	private EntityManagerFactory entityManagerFactory;

	@GiftDatabaseManager
	@Produces
	@Specializes
	public EntityManager produceEntityManager() {
		return entityManagerFactory.createEntityManager();
	}

	public void dispose(@GiftDatabaseManager @Disposes EntityManager entityManager) {
		entityManager.close();
	}
}