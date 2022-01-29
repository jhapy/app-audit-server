package org.jhapy.audit.query;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.axonframework.queryhandling.QueryHandler;
import org.jhapy.audit.converter.GenericMapper;
import org.jhapy.audit.converter.SessionConverter;
import org.jhapy.audit.domain.Session;
import org.jhapy.audit.repository.SessionRepository;
import org.jhapy.cqrs.query.audit.*;
import org.jhapy.dto.domain.audit.SessionDTO;
import org.jhapy.dto.domain.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
@RequiredArgsConstructor
public class SessionQueryHandler implements BaseQueryHandler<Session, SessionDTO> {
  private final SessionRepository repository;
  private final SessionConverter converter;
  private final MongoTemplate mongoTemplate;

  @QueryHandler
  public GetSessionByIdResponse getById(GetSessionByIdQuery query) {
    return new GetSessionByIdResponse(
        converter.asDTO(
            repository.findById(query.getId()).orElseThrow(EntityNotFoundException::new), null));
  }

  @QueryHandler
  public GetAllSessionsResponse getAll(GetAllSessionsQuery query) {
    return new GetAllSessionsResponse(converter.asDTOList(repository.findAll(), null));
  }

  @QueryHandler
  public GetSessionByJSessionIdResponse getSessionByJSessionId(GetSessionByJSessionIdQuery query) {
    return new GetSessionByJSessionIdResponse(
        converter.asDTO(
            repository
                .getByJsessionId(query.getJsessionId())
                .orElseThrow(EntityNotFoundException::new),
            null));
  }

  @QueryHandler
  public FindAnyMatchingSessionResponse findAnyMatchingSession(FindAnyMatchingSessionQuery query) {
    Page<Session> result =
        BaseQueryHandler.super.findAnyMatching(
            query.getFilter(), query.getShowInactive(), converter.convert(query.getPageable()));
    return new FindAnyMatchingSessionResponse(converter.asDTOList(result.getContent(), null));
  }

  @QueryHandler
  public CountAnyMatchingSessionResponse countAnyMatchingSession(
      CountAnyMatchingSessionQuery query) {
    return new CountAnyMatchingSessionResponse(
        BaseQueryHandler.super.countAnyMatching(query.getFilter(), query.getShowInactive()));
  }

  public void buildSearchQuery(Criteria rootCriteria, String filter, Boolean showInactive) {
    String loggerPrefix = getLoggerPrefix("buildSearchQuery");
    List<Criteria> andPredicated = new ArrayList<>();

    if (StringUtils.isNoneBlank(filter)) {
      andPredicated.add(
          (new Criteria())
              .orOperator(
                  where("username").regex(filter),
                  where("sourceIp").regex(filter),
                  where("error").regex(filter),
                  where("jsessionId").regex(filter)));
    }

    if (showInactive == null || !showInactive) {
      andPredicated.add(Criteria.where("isActive").is(Boolean.TRUE));
    }

    if (!andPredicated.isEmpty()) rootCriteria.andOperator(andPredicated.toArray(new Criteria[0]));
  }

  @Override
  public MongoRepository<Session, UUID> getRepository() {
    return repository;
  }

  @Override
  public MongoTemplate getMongoTemplate() {
    return mongoTemplate;
  }

  @Override
  public Class<Session> getEntityClass() {
    return Session.class;
  }

  @Override
  public GenericMapper<Session, SessionDTO> getConverter() {
    return converter;
  }
}
