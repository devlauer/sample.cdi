package de.elnarion.sample.cdi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.elnarion.sample.cdi.domain.Gift;
import de.elnarion.sample.cdi.support.TransactionalConnectionProvider;
import de.elnarion.sample.cdi.support.extensions.InitDatasource;
import de.elnarion.sample.cdi.support.extensions.WeldJunit5WithJTAExtension;

@ExtendWith(WeldJunit5WithJTAExtension.class)
@Named
@TestMethodOrder(OrderAnnotation.class)
class CDIJPAServiceWithWeldExtensionTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(CDIJPAServiceWithWeldExtensionTest.class);

	@WeldSetup
	WeldInitiator weldInitiator = WeldInitiator.performDefaultDiscovery();

	@InitDatasource
	public static void initDatasource() {
		LOGGER.info("initializing Datasource");
		TransactionalConnectionProvider.bindDataSource();
	}

	@Inject
	CDIService cdiService;
	
	@Test
	@Order(1)
	void testCreateRandomGift()
			throws IllegalStateException, SecurityException, SystemException, NotSupportedException {
		// ARRANGE/ACT
		Gift gift = cdiService.createRandomGift();
		Gift readGift = cdiService.getGiftById(gift.getId());

		// ASSERT
		assertNotNull(gift.getId());
		assertNotNull(readGift);
		assertEquals(gift.getId(), readGift.getId());
	}

	@Test
	@Order(2)
	void testGetAllGifts() throws IllegalStateException, SecurityException, SystemException, NotSupportedException {

		// ARRANGE/ACT
		List<Gift> gifts = cdiService.getAllGifts();

		// ASSERT
		assertNotNull(gifts);
		assertTrue(gifts.isEmpty());
	}

}
