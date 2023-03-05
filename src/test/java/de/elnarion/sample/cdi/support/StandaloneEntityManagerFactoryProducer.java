package de.elnarion.sample.cdi.support;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.cfg.Environment;

@Singleton
public class StandaloneEntityManagerFactoryProducer {

	private EntityManagerFactory entityManagerFactory;
	
	@PostConstruct
    public void postConstruct() {
		Map<String, Object> props = new HashMap<>();
		props.put("javax.persistence.bean.manager", beanManager);
		props.put(Environment.CONNECTION_PROVIDER, TransactionalConnectionProvider.class);
        entityManagerFactory = Persistence.createEntityManagerFactory("cdi-test-unit",props);
    }
	
	@Inject
	private BeanManager beanManager;

	@Produces
	public EntityManagerFactory produceEntityManagerFactory() {
		return entityManagerFactory;
	}

	public void close(@Disposes EntityManagerFactory entityManagerFactory) {
		entityManagerFactory.close();
	}
}
