package de.elnarion.sample.cdi.dao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.elnarion.sample.cdi.domain.Gift;

/**
 * The Class GiftDao.
 */
@Named
public class GiftDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(GiftDao.class);
	
	/** The entity manager. */
	@Inject
	EntityManager entityManager;

	/**
	 * All.
	 *
	 * @return the list
	 */
	public List<Gift> all() {
		LOGGER.info("Transaction during all {}",entityManager.getTransaction());
        return this.entityManager.createNamedQuery("all", Gift.class).getResultList();
    }
	
	/**
	 * Save.
	 *
	 * @param paramGift the param gift
	 */
	@Transactional
	public void save(Gift paramGift) {
		LOGGER.info("Transaction during save {}",entityManager.getTransaction());
		this.entityManager.persist(paramGift);
	}
	
	/**
	 * Gets the by id.
	 *
	 * @param paramId the param id
	 * @return the by id
	 */
	public Gift getById(Long paramId) {
		LOGGER.info("Transaction during find {}",entityManager.getTransaction());
		return this.entityManager.find(Gift.class, paramId);
	}
}
