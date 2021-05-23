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

package org.jhapy.audit.endpoint;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.jhapy.audit.converter.SessionConverterV2;
import org.jhapy.audit.domain.Session;
import org.jhapy.audit.service.SessionService;
import org.jhapy.commons.endpoint.BaseEndpoint;
import org.jhapy.dto.serviceQuery.ServiceResult;
import org.jhapy.dto.serviceQuery.generic.CountAnyMatchingQuery;
import org.jhapy.dto.serviceQuery.generic.FindAnyMatchingQuery;
import org.jhapy.dto.serviceQuery.generic.GetByStrIdQuery;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jHapy Lead Dev.
 * @version 1.0
 * @since 2019-06-05
 */
@RestController
@RequestMapping("/api/sessionService")
public class SessionServiceEndpoint extends BaseEndpoint {

  private final SessionService sessionService;

  public SessionServiceEndpoint(SessionService sessionService,
      SessionConverterV2 converter) {
    super(converter);
    this.sessionService = sessionService;
  }

  protected SessionConverterV2 getConverter() {
    return (SessionConverterV2) converter;
  }

  @Operation(
      security = @SecurityRequirement(name = "openId", scopes = {"ROLE_AUDIT_READ",
          "ROLE_AUDIT_WRITE"})
  )
  @PostMapping(value = "/findAnyMatching")
  public ResponseEntity<ServiceResult> findAnyMatching(@RequestBody FindAnyMatchingQuery query) {
    var loggerPrefix = getLoggerPrefix("findAnyMatching");

    Page<Session> result = sessionService
        .findAnyMatching(query.getFilter(),
            converter.convert(query.getPageable()));
    return handleResult(loggerPrefix,
        toDtoPage(result, getConverter().convertToDtoSessions(result.getContent())));
  }

  @Operation(
      security = @SecurityRequirement(name = "openId", scopes = {"ROLE_AUDIT_READ",
          "ROLE_AUDIT_WRITE"})
  )
  @PostMapping(value = "/countAnyMatching")
  public ResponseEntity<ServiceResult> countAnyMatching(@RequestBody CountAnyMatchingQuery query) {
    var loggerPrefix = getLoggerPrefix("countAnyMatching");

    return handleResult(loggerPrefix, sessionService.countAnyMatching(query.getFilter()));
  }

  @Operation(
      security = @SecurityRequirement(name = "openId", scopes = {"ROLE_AUDIT_READ",
          "ROLE_AUDIT_WRITE"})
  )
  @PostMapping(value = "/getById")
  public ResponseEntity<ServiceResult> getById(@RequestBody GetByStrIdQuery query) {
    var loggerPrefix = getLoggerPrefix("getById");

    return handleResult(loggerPrefix,
        getConverter().convertToDto(sessionService.load(query.getId())));
  }
}