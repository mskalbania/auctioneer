package edu.auctioneer.repository;

import edu.auctioneer.entity.BidEntity;
import edu.auctioneer.entity.ItemEntity;
import edu.auctioneer.entity.UserEntity;
import edu.auctioneer.exception.UserNotFoundException;
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
public class UserRepository {

    private static final Logger LOG = LoggerFactory.getLogger(UserRepository.class);

    @PersistenceContext
    private final EntityManager entityManager;

    public UserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public UUID saveUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        entityManager.flush();
        return userEntity.getId();
    }

    public UserEntity getById(UUID id) {
        try {
            return entityManager.createQuery("select u from UserEntity u where u.id=:id", UserEntity.class)
                                .setParameter("id", id)
                                .getSingleResult();
        } catch (NoResultException ex) {
            LOG.error("Item with id {} not found in database", id.toString());
            throw new UserNotFoundException(id.toString());
        }
    }

    public UserEntity getWithDetailsById(UUID id) {
        UserEntity user = getById(id);
        user.setBids(fetchUserBids(id));
        user.setItems(fetchUserItems(id));
        return user;
    }

    private List<ItemEntity> fetchUserItems(UUID userId) {
        return entityManager.createQuery("select distinct i from ItemEntity i " +
                                                 "left join fetch i.bids " +
                                                 "where i.owner.id=:id", ItemEntity.class)
                            .setParameter("id", userId)
                            .getResultList();
    }

    private List<BidEntity> fetchUserBids(UUID userId) {
        return entityManager.createQuery("select b from BidEntity b where b.owner.id=:id", BidEntity.class)
                            .setParameter("id", userId)
                            .getResultList();
    }
}
