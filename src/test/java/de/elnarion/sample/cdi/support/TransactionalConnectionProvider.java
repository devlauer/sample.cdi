package de.elnarion.sample.cdi.support;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.h2.jdbcx.JdbcDataSource;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arjuna.ats.jdbc.TransactionalDriver;

public class TransactionalConnectionProvider implements ConnectionProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionalConnectionProvider.class);
	private static final long serialVersionUID = 1L;
	public static final String DATASOURCE_JNDI = "jdbc/MyDS";
	public static final String USERNAME = "sa";
	public static final String PASSWORD = "";

	private final TransactionalDriver transactionalDriver;

	public TransactionalConnectionProvider() {
		transactionalDriver = new TransactionalDriver();
	}

	public static void bindDataSource() {
		LOGGER.info("Binding Datasource");
		JdbcDataSource dataSource = new JdbcDataSource();
		dataSource.setURL("jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1");
		dataSource.setUser(USERNAME);
		dataSource.setPassword(PASSWORD);

		try {
			InitialContext initialContext = new InitialContext();
			initialContext.createSubcontext("jdbc");
			initialContext.bind(DATASOURCE_JNDI, dataSource);
			LOGGER.info("Datasource bound");

		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Connection getConnection() throws SQLException {
		LOGGER.info("Getting Connection");

		Properties properties = new Properties();
		properties.setProperty(TransactionalDriver.userName, USERNAME);
		properties.setProperty(TransactionalDriver.password, PASSWORD);
		return transactionalDriver.connect("jdbc:arjuna:" + DATASOURCE_JNDI, properties);
	}

	@Override
	public void closeConnection(Connection connection) throws SQLException {
		if (!connection.isClosed()) {
			connection.close();
		}
	}

	@Override
	public boolean supportsAggressiveRelease() {
		return false;
	}

	@Override
	public boolean isUnwrappableAs(@SuppressWarnings("rawtypes") Class aClass) {
		return getClass().isAssignableFrom(aClass);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T unwrap(Class<T> aClass) {
		if (isUnwrappableAs(aClass)) {
			return (T) this;
		}

		throw new UnknownUnwrapTypeException(aClass);
	}
}
