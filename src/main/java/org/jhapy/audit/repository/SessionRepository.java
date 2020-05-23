package org.jhapy.audit.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.jhapy.audit.domain.Session;

/**
 * @author jHapy Lead Dev.
 * @version 1.0
 * @since 2019-05-15
 */
public interface SessionRepository extends MongoRepository<Session, String> {

  @Query("{'$or' : [{'username' : {$regex : ?0, $options: 'i'}}, {'sourceIp' : {$regex : ?0, $options: 'i'}}, {'jsessionId' : {$regex : ?0, $options: 'i'}}]}")
  Page<Session> findByCriteria(String criteria, Pageable pageable);

  @Query(value = "{'$or' : [{'username' : {$regex : ?0, $options: 'i'}}, {'sourceIp' : {$regex : ?0, $options: 'i'}}, {'jsessionId' : {$regex : ?0, $options: 'i'}}]}", count = true)
  Long countByCriteria(String criteria);

  Optional<Session> getByJsessionId(String jsessionId);
}