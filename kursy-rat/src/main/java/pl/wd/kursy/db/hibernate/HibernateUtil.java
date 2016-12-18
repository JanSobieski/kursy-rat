package pl.wd.kursy.db.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Startup Hibernate and provide access to the singleton SessionFactory
 */
public class HibernateUtil {
	static Logger logger = Logger.getLogger("pl.wd.kursy.db.hibernate.HibernateUtil");

	private static SessionFactory _sessionFactory;

	public static SessionFactory getSessionFactory() {
		if ( _sessionFactory == null ) {
			// A SessionFactory is set up once for an application!
			final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
					.configure() // configures settings from hibernate.cfg.xml
					.build();
			try {
				_sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
			}
			catch (Exception e) {
				// The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
				// so destroy it manually.
				StandardServiceRegistryBuilder.destroy( registry );
				logger.error("hibernate error", e);
				throw new ExceptionInInitializerError(e);
			}
			
			
		}

		// Alternatively, we could look up in JNDI here
		return _sessionFactory;
	}

	public static void shutdown() {
		// Close caches and connection pools
		getSessionFactory().close();
	}

}
