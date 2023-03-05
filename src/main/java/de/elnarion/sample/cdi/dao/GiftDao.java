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
	@GiftDatabaseManager
	private EntityManager giftEntityManager;

	/**
	 * All.
	 *
	 * @return the list
	 */
	public List<Gift> all() {
		LOGGER.info("Transaction during all {}", giftEntityManager.getTransaction());
		return this.giftEntityManager.createNamedQuery("all", Gift.class).getResultList();
	}

	/**
	 * Save.
	 *
	 * @param paramGift the param gift
	 */
	@Transactional
	public void save(Gift paramGift) {
		LOGGER.info("Transaction during save {}", giftEntityManager.getTransaction());
		this.giftEntityManager.persist(paramGift);
	}

	/**
	 * Gets the by id.
	 *
	 * @param paramId the param id
	 * @return the by id
	 */
	public Gift getById(Long paramId) {
		LOGGER.info("Transaction during find {}", giftEntityManager.getTransaction());
		return this.giftEntityManager.find(Gift.class, paramId);
	}
}
