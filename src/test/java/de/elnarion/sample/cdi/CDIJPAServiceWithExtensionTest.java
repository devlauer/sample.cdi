package de.elnarion.sample.cdi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import javax.enterprise.inject.spi.CDI;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.elnarion.sample.cdi.domain.Gift;
import de.elnarion.sample.cdi.support.TransactionalConnectionProvider;
import de.elnarion.sample.cdi.support.extensions.InitDatasource;
import de.elnarion.sample.cdi.support.extensions.WeldInitAllExtension;

@ExtendWith(WeldInitAllExtension.class)
class CDIJPAServiceWithExtensionTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(CDIJPAServiceWithExtensionTest.class);
	

	@InitDatasource
	public static void initDatasource() {
		LOGGER.info("initializing Datasource");
		TransactionalConnectionProvider.bindDataSource();
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
