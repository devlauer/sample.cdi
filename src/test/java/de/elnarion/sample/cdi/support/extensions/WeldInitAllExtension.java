package de.elnarion.sample.cdi.support.extensions;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arjuna.ats.jta.utils.JNDIManager;

import de.elnarion.jndi.server.NamingBeanImpl;

public class WeldInitAllExtension implements BeforeAllCallback, AfterAllCallback {

	private static final Logger LOGGER = LoggerFactory.getLogger(WeldInitAllExtension.class);
	
	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		LOGGER.info("beforeAll");
		Store junitStore = context.getStore(Namespace.create(WeldInitAllExtension.class));
		NamingBeanImpl namingBean = new NamingBeanImpl();
        namingBean.start();
        junitStore.put(NamingBeanImpl.class.getName(), namingBean);
		JNDIManager.bindJTAImplementation();
		Class<?> testclass = context.getTestClass().get();
		Method[] methods = testclass.getMethods();
		Optional<Method> initMethodOptional = Arrays.asList(methods).stream()
				.filter(m -> Modifier.isStatic(m.getModifiers()))
				.filter(m -> m.getAnnotationsByType(InitDatasource.class).length > 0).findFirst();
		if (initMethodOptional.isPresent()) {
			Method initMethod = initMethodOptional.get();
			initMethod.setAccessible(true);
			initMethod.invoke(null);
		}
		Weld weld = new Weld().enableDiscovery();
		WeldContainer weldcontainer = weld.initialize();
		junitStore.put(WeldContainer.class.getName(), weldcontainer);

	}

	@Override
	public void afterAll(ExtensionContext context) throws Exception {
		LOGGER.info("afterAll");
		Store junitStore = context.getStore(Namespace.create(WeldInitAllExtension.class));
		WeldContainer container = (WeldContainer) junitStore.get(WeldContainer.class.getName());
		if (container != null)
			container.shutdown();
        NamingBeanImpl namingBean = (NamingBeanImpl) junitStore.get(NamingBeanImpl.class.getName());
        if(namingBean!=null)
        	namingBean.stop();
		
	}

}
