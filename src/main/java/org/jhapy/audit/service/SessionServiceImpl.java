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

import org.jhapy.audit.domain.Session;
import org.jhapy.audit.repository.SessionRepository;
import org.jhapy.commons.utils.HasLogger;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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
  public MongoRepository<Session, UUID> getRepository() {
    return sessionRepository;
  }
}
