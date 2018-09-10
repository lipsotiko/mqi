package io.egia.mqi.chunk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Service
public class ChunkService {
    private Logger log = LoggerFactory.getLogger(ChunkService.class);
    private EntityManager entityManager;
    private ChunkRepository chunkRepository;

    public ChunkService(EntityManager entityManager, ChunkRepository chunkRepository) {
        this.entityManager = entityManager;
        this.chunkRepository = chunkRepository;
    }

    public void chunkData() {
        log.info("Executing chunking process");
        Query query = entityManager.createNamedQuery("ChunkData");

        if (query.getSingleResult().toString().equals("-1")) {
            log.info("Chunking process failed.");
        }
    }

}
