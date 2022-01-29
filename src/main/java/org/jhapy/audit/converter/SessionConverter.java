package org.jhapy.audit.converter;

import org.jhapy.audit.command.SessionAggregate;
import org.jhapy.audit.domain.Session;
import org.jhapy.cqrs.command.audit.LoginCommand;
import org.jhapy.cqrs.command.audit.LogoutCommand;
import org.jhapy.cqrs.event.audit.LoginEvent;
import org.jhapy.cqrs.event.audit.LogoutEvent;
import org.jhapy.dto.domain.audit.SessionDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper(config = BaseNosqlDbConverterConfig.class, componentModel = "spring")
public abstract class SessionConverter extends GenericMapper<Session, SessionDTO> {
  public static SessionConverter INSTANCE = Mappers.getMapper(SessionConverter.class);

  public abstract LoginEvent toLoginEvent(LoginCommand dto);

  public abstract LogoutEvent toLogoutEvent(LogoutCommand dto);

  public abstract void updateAggregateFromLoginEvent(
      LoginEvent event, @MappingTarget SessionAggregate aggregate);

  public abstract void updateAggregateFromLogoutEvent(
      LogoutEvent event, @MappingTarget SessionAggregate aggregate);

  @Mapping(target = "created", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "modified", ignore = true)
  @Mapping(target = "modifiedBy", ignore = true)
  public abstract Session toEntity(LoginEvent event);

  @Mapping(target = "created", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "modified", ignore = true)
  @Mapping(target = "modifiedBy", ignore = true)
  public abstract Session toEntity(LogoutEvent event);

  public abstract Session asEntity(SessionDTO dto, @Context Map<String, Object> context);

  public abstract SessionDTO asDTO(Session domain, @Context Map<String, Object> context);

  @AfterMapping
  protected void afterConvert(
      SessionDTO dto, @MappingTarget Session domain, @Context Map<String, Object> context) {}

  @AfterMapping
  protected void afterConvert(
      Session domain, @MappingTarget SessionDTO dto, @Context Map<String, Object> context) {}
}
