package edu.auctioneer.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.ResourceUtils;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@Component
public class DbPopulation {

    private final PlatformTransactionManager transactionManager;
    private final EntityManager entityManager;

    public DbPopulation(PlatformTransactionManager transactionManager, EntityManager entityManager) {
        this.transactionManager = transactionManager;
        this.entityManager = entityManager;
    }

    //Populate db here, since we are using hibernate ddl auto we cannot auto load sql scripts
    @EventListener(ApplicationReadyEvent.class)
    public void populateDb() throws IOException {
        String content = new String(Files.readAllBytes(ResourceUtils.getFile("classpath:schema.sql").toPath()));
        List<String> sqlQueries = Arrays.asList(content.replaceAll(System.lineSeparator(), " ").split(";"));
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.executeWithoutResult(status -> sqlQueries.forEach(query -> entityManager.createNativeQuery(query).executeUpdate()));
    }
}
