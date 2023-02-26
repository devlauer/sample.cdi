package de.elnarion.sample.cdi.support;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class EntityManagerProducer {

	@Inject
	private EntityManagerFactory entityManagerFactory;

	@Produces
	public EntityManager produceEntityManager() {
		return entityManagerFactory.createEntityManager();
	}

	public void close(@Disposes EntityManager entityManager) {
		entityManager.close();
	}
}