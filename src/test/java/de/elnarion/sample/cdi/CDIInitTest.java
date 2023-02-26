package de.elnarion.sample.cdi;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;

import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(WeldJunit5Extension.class)
class CDIInitTest {

	@WeldSetup
	WeldInitiator weld = WeldInitiator.performDefaultDiscovery();

	@Test
	void testCDICreation() {
		Instance<CDIService> cdiserviceInstance = CDI.current().select(CDIService.class);
		assertAll(() -> assertNotNull(cdiserviceInstance), () -> assertTrue(cdiserviceInstance.isResolvable()),
				() -> assertNotNull(cdiserviceInstance.get()));
	}

}
