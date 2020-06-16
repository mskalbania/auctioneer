package edu.auctioneer.repository;

import edu.auctioneer.entity.ItemEntity;
import edu.auctioneer.exception.ItemNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public class ItemRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ItemRepository.class);

    @PersistenceContext
    private final EntityManager entityManager;

    public ItemRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<ItemEntity> findAllBy(String like) {
        return entityManager.createQuery("select distinct i from ItemEntity i " +
                                                 "left join fetch i.bids " +
                                                 "where lower(i.name) like lower(concat('%',:l,'%'))", ItemEntity.class)
                            .setParameter("l", like)
                            .getResultList();
    }

    public ItemEntity findById(UUID id) {
        try {
            return entityManager.createQuery("select i from ItemEntity i left join fetch i.bids where i.id=:id", ItemEntity.class)
                                .setParameter("id", id)
                                .getSingleResult();
        } catch (NoResultException ex) {
            LOG.error("Item with id {} not found in database", id.toString());
            throw new ItemNotFoundException(id.toString());
        }
    }

    public UUID save(ItemEntity itemEntity) {
        entityManager.persist(itemEntity);
        entityManager.flush();
        LOG.debug("Successfully persisted item {}", itemEntity.toString());
        return itemEntity.getId();
    }
}
