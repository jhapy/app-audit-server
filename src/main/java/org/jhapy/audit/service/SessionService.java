package org.jhapy.audit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.jhapy.audit.domain.Session;
import org.jhapy.audit.exception.ServiceException;

/**
 * @author jHapy Lead Dev.
 * @version 1.0
 * @since 2019-05-24
 */
public interface SessionService extends BaseCrudService<Session> {

  Page<Session> findAnyMatching(String filter, Pageable pageable);

  long countAnyMatching(String filter);

  Session login(String jsessionId, String sourceIp, String username, Boolean isSuccess,
      String error) throws ServiceException;

  void logout(String jsessionId);
}
