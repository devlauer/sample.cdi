package de.elnarion.sample.cdi;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import javax.enterprise.inject.spi.CDI;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arjuna.ats.jta.utils.JNDIManager;

import de.elnarion.jndi.server.NamingBeanImpl;
import de.elnarion.sample.cdi.dao.GiftDao;
import de.elnarion.sample.cdi.domain.Gift;
import de.elnarion.sample.cdi.support.TransactionalConnectionProvider;

class CDIJPADAOTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(CDIJPADAOTest.class);
	
	private static WeldContainer weldcontainer;
	private static NamingBeanImpl namingBean;
	
	@BeforeAll
	static void initContainer() throws Exception {
		namingBean = new NamingBeanImpl();
		namingBean.start();
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
		namingBean.stop();
	}

	@BeforeEach
	void initDao() {
		giftDAO = CDI.current().select(GiftDao.class).get();
	}
	

	GiftDao giftDAO;

	@Order(value = 1)
	@Test
	void testSaveGift() {
		// ARRANGE
		Gift gift = new Gift();
		gift.setName("Dummy");

		// ACT
		giftDAO.save(gift);

		// ASSERT
		assertNotNull(gift.getId());
	}

	@Order(value = 2)
	@Test
	void testGetAllGifts() {
		// ARRANGE/ACT
		List<Gift> gifts = giftDAO.all();

		// ASSERT
		assertNotNull(gifts);
		assertFalse(gifts.isEmpty());

	}

}
