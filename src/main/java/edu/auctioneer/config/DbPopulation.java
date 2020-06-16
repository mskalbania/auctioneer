package edu.auctioneer.config;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

@Component
public class DbPopulation {

    private static final Logger LOG = LoggerFactory.getLogger(DbPopulation.class);

    private final PlatformTransactionManager transactionManager;
    private final EntityManager entityManager;

    public DbPopulation(PlatformTransactionManager transactionManager, EntityManager entityManager) {
        this.transactionManager = transactionManager;
        this.entityManager = entityManager;
    }

    //Populate db here, since we are using hibernate ddl auto we cannot auto load sql scripts
    @EventListener(ApplicationReadyEvent.class)
    public void populateDb() throws IOException {
        try {
            String content = IOUtils.resourceToString("schema.sql", Charset.defaultCharset(), this.getClass().getClassLoader());
            List<String> sqlQueries = Arrays.asList(content.replaceAll(System.lineSeparator(), " ").split(";"));
            TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
            transactionTemplate.executeWithoutResult(status -> sqlQueries.forEach(query -> entityManager.createNativeQuery(query).executeUpdate()));
            LOG.info("Successfully populated database using schema.sql script");
        } catch (Exception e) {
            LOG.error("Unable to populate database, {}", e.getMessage());
        }
    }
}
