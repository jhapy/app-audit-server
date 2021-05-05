/*
 * Copyright 2020-2020 the original author or authors from the JHapy project.
 *
 * This file is part of the JHapy project, see https://www.jhapy.org/ for more information.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jhapy.audit.service;

import java.time.Instant;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.jhapy.audit.domain.Session;
import org.jhapy.audit.exception.ServiceException;
import org.jhapy.audit.repository.SessionRepository;
import org.jhapy.commons.utils.HasLogger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jHapy Lead Dev.
 * @version 1.0
 * @since 2019-05-24
 */
@Transactional(readOnly = true)
@Service
public class SessionServiceImpl implements SessionService, HasLogger {

  private final SessionRepository sessionRepository;

  public SessionServiceImpl(SessionRepository sessionRepository) {
    this.sessionRepository = sessionRepository;
  }

  @Override
  public Page<Session> findAnyMatching(String filter, Pageable pageable) {
    var loggerString = getLoggerPrefix("findAnyMatching");
    logger().debug(loggerString + "In = " + filter);
    Page<Session> result;

    if (StringUtils.isNotBlank(filter)) {
      result = sessionRepository.findByCriteria(filter, pageable);
    } else {
      result = sessionRepository.findAll(pageable);
    }

    logger().debug(loggerString + "Out = " + result);

    return result;
  }


  @Override
  public long countAnyMatching(String filter) {
    var loggerString = getLoggerPrefix("countAnyMatching");
    logger().debug(loggerString + "In = " + filter);
    long result;
    if (StringUtils.isNotBlank(filter)) {
      result = sessionRepository.countByCriteria(filter);
    } else {
      result = sessionRepository.count();
    }

    logger().debug(loggerString + "Out = " + result);
    return result;
  }

  @Transactional
  @Override
  public Session login(String jsessionId, String sourceIp, String username, Boolean isSuccess,
      String error) throws ServiceException {
    var loggerPrefix = getLoggerPrefix("login");
    Optional<Session> _session = sessionRepository.getByJsessionId(jsessionId);
    Session session;
    if (_session.isPresent()) {
      session = _session.get();
      if (!session.getUsername().equals(username)) {
        logger().error(loggerPrefix + "SESSION ID '" + jsessionId
            + "' is already associated with another user '" + session.getUsername()
            + "', expecting '" + username + "'");
        throw new ServiceException("SESSION ID is already associated with another user");
      }
      return session;
    } else {
      session = new Session();
      session.setJsessionId(jsessionId);
      session.setSourceIp(sourceIp);
      session.setUsername(username);
      session.setIsSuccess(isSuccess);
      session.setError(error);
      session.setSessionStart(Instant.now());
      return sessionRepository.save(session);
    }
  }

  @Transactional
  @Override
  public void logout(String jsessionId) {
    var loggerPrefix = getLoggerPrefix("logout");
    Optional<Session> _session = sessionRepository.getByJsessionId(jsessionId);
    if (_session.isPresent()) {
      Session session = _session.get();
      session.setSessionEnd(Instant.now());
      session.setSessionDuration(
          session.getSessionEnd().toEpochMilli() - session.getSessionStart().toEpochMilli());
      sessionRepository.save(session);
    } else {
      logger().error(
          String.format("%sSession with jsessionId '%s' not found", loggerPrefix, jsessionId));
    }
  }

  @Override
  public MongoRepository<Session, String> getRepository() {
    return sessionRepository;
  }
}
