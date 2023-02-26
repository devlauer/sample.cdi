package de.elnarion.sample.cdi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import javax.enterprise.inject.spi.CDI;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.jnp.server.NamingBeanImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arjuna.ats.jta.utils.JNDIManager;

import de.elnarion.sample.cdi.domain.Gift;
import de.elnarion.sample.cdi.support.TransactionalConnectionProvider;

class CDIJPAServiceTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(CDIJPAServiceTest.class);
	
	private static WeldContainer weldcontainer;
	
	private static NamingBeanImpl NAMING_BEAN;
	
	@BeforeAll
	static void initContainer() throws Exception {
		NAMING_BEAN = new NamingBeanImpl();
        NAMING_BEAN.start();
		LOGGER.info("Registering JTA Manager");
		JNDIManager.bindJTAImplementation();
		LOGGER.info("Registering Datasource");
        TransactionalConnectionProvider.bindDataSource();
        Weld weld = new Weld().enableDiscovery();
        weldcontainer=weld.initialize();
	}
	
	@AfterAll
	static void shutdownContainer() {
		weldcontainer.shutdown();
		NAMING_BEAN.stop();
	}

	@BeforeEach
	void initDao() {
		cdiService = CDI.current().select(CDIService.class).get();
	}
	

	CDIService cdiService;

	@Test
	void testCDIService() throws IllegalStateException, SecurityException, SystemException, NotSupportedException {
		UserTransaction transaction = CDI.current().select(UserTransaction.class).get();
		transaction.begin();
		// ARRANGE/ACT
		Gift gift = cdiService.createRandomGift();
		Gift readGift = cdiService.getGiftById(gift.getId());

		// ASSERT
		assertNotNull(gift.getId());
		assertNotNull(readGift);
		assertEquals(gift.getId(), readGift.getId());

		// combined test
		
		// ARRANGE/ACT
		List<Gift> gifts = cdiService.getAllGifts();

		// ASSERT
		assertNotNull(gifts);
		assertFalse(gifts.isEmpty());
		transaction.rollback();
	}

}
