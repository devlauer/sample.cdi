package de.elnarion.sample.cdi.support.extensions;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;

import org.jboss.weld.junit5.WeldJunit5Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arjuna.ats.jta.utils.JNDIManager;

import de.elnarion.jndi.server.NamingBeanImpl;

public class WeldJunit5WithJTAExtension extends WeldJunit5Extension{

	private static final Logger LOGGER = LoggerFactory.getLogger(WeldInitAllExtension.class);

	
	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		Store junitStore = context.getStore(Namespace.create(WeldJunit5WithJTAExtension.class));
		NamingBeanImpl namingBean = new NamingBeanImpl();
        namingBean.start();
        junitStore.put(NamingBeanImpl.class.getName(), namingBean);
		LOGGER.info("Initialising JTA");
		JNDIManager.bindJTAImplementation();
		LOGGER.info("Checking for datasource method");
		Class<?> testclass = context.getTestClass().get();
		Method[] methods = testclass.getMethods();
		Optional<Method> initMethodOptional = Arrays.asList(methods).stream()
				.filter(m -> Modifier.isStatic(m.getModifiers()))
				.filter(m -> m.getAnnotationsByType(InitDatasource.class).length > 0).findFirst();
		if (initMethodOptional.isPresent()) {
			Method initMethod = initMethodOptional.get();
			LOGGER.info("Method found and now getting called");
			initMethod.setAccessible(true);
			initMethod.invoke(null);
		}
		LOGGER.info("Initialising Weld");		
		super.beforeAll(context);
	}
	
	@Override
	public void afterAll(ExtensionContext context) throws Exception {
		super.afterAll(context);
		Store junitStore = context.getStore(Namespace.create(WeldJunit5WithJTAExtension.class));
		NamingBeanImpl namingBean = (NamingBeanImpl) junitStore.get(NamingBeanImpl.class.getName());
		if(namingBean!=null)
			namingBean.stop();
	}
	
}
