package org.jhapy.audit.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.jhapy.audit.domain.ChangeLog;

/**
 * @author jHapy Lead Dev.
 * @version 1.0
 * @since 2019-05-15
 */
public interface ChangeLogRepository extends MongoRepository<ChangeLog, String> {

}
