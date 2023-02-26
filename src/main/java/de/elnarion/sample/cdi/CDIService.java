package de.elnarion.sample.cdi;

import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import de.elnarion.sample.cdi.dao.GiftDao;
import de.elnarion.sample.cdi.domain.Gift;

/**
 * The Class CDIService.
 */
@Named
@ApplicationScoped
public class CDIService {

	/** The gift dao. */
	@Inject
	private GiftDao giftDao;

	/**
	 * Creates the random gift.
	 *
	 * @return the gift
	 */
	@Transactional(value = TxType.REQUIRED)
	public Gift createRandomGift() {
		Gift gift = new Gift();
		gift.setName("" + UUID.randomUUID().toString());
		giftDao.save(gift);
		return gift;
	}
	
	/**
	 * Gets the all gifts.
	 *
	 * @return the all gifts
	 */
	@Transactional(value = TxType.REQUIRED)
	public List<Gift> getAllGifts(){
		return giftDao.all();
	}
	
	/**
	 * Gets the gift by id.
	 *
	 * @param paramGiftId the param gift id
	 * @return the gift by id
	 */
	public Gift getGiftById(Long paramGiftId) {
		return giftDao.getById(paramGiftId);
	}
}
