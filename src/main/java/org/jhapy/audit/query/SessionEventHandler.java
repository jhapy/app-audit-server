package org.jhapy.audit.query;

import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.jhapy.audit.converter.SessionConverter;
import org.jhapy.audit.domain.Session;
import org.jhapy.audit.repository.SessionRepository;
import org.jhapy.commons.utils.HasLogger;
import org.jhapy.cqrs.event.audit.LoginEvent;
import org.jhapy.cqrs.event.audit.LogoutEvent;
import org.jhapy.cqrs.event.audit.SessionDeletedEvent;
import org.jhapy.cqrs.query.audit.CountAnyMatchingSessionQuery;
import org.jhapy.cqrs.query.audit.GetSessionByIdQuery;
import org.jhapy.dto.serviceQuery.CountChangeResult;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@ProcessingGroup("session-group")
public class SessionEventHandler implements HasLogger {
  private final SessionRepository repository;
  private final SessionConverter converter;
  private final QueryUpdateEmitter queryUpdateEmitter;

  @ExceptionHandler
  public void handleException(Exception ex) throws Exception {
    String loggerPrefix = getLoggerPrefix("handleException");
    error(
        loggerPrefix,
        ex,
        "Exception in EventHandler (ExceptionHandler): {0}:{1}",
        ex.getClass().getName(),
        ex.getMessage());
    throw ex;
  }

  @EventHandler
  public void on(LoginEvent event) throws Exception {
    String loggerPrefix = getLoggerPrefix("onSessionCreatedEvent");
    debug(loggerPrefix, "In with : " + event.getId() + ", " + event.getJsessionId());

    Session entity = converter.toEntity(event);
    entity = repository.save(entity);
    queryUpdateEmitter.emit(
        GetSessionByIdQuery.class, query -> true, converter.asDTO(entity, null));

    queryUpdateEmitter.emit(
        CountAnyMatchingSessionQuery.class, query -> true, new CountChangeResult());

    debug(loggerPrefix, "Out with : " + event.getId() + ", " + event.getJsessionId());
  }

  @EventHandler
  public void on(LogoutEvent event) throws Exception {
    String loggerPrefix = getLoggerPrefix("onSessionUpdatedEvent");
    debug(loggerPrefix, "In with : " + event.getId());

    Session entity = converter.toEntity(event);
    entity.setSessionEnd(Instant.now());
    entity = repository.save(entity);
    queryUpdateEmitter.emit(
        GetSessionByIdQuery.class, query -> true, converter.asDTO(entity, null));

    debug(loggerPrefix, "Out with : " + event.getId());
  }

  @EventHandler
  public void on(SessionDeletedEvent event) throws Exception {
    repository.deleteById(event.getId());
  }
}
