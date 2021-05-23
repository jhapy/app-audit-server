package org.jhapy.audit.converter;

import java.util.Collection;
import java.util.List;
import org.jhapy.commons.converter.CommonsConverterV2;
import org.jhapy.dto.domain.audit.Session;
import org.mapstruct.Mapper;

/**
 * @author Alexandre Clavaud.
 * @version 1.0
 * @since 18/05/2021
 */
@Mapper(componentModel = "spring")
public abstract class SessionConverterV2 extends CommonsConverterV2 {

  public abstract Session convertToDto(org.jhapy.audit.domain.Session domain);

  public abstract org.jhapy.audit.domain.Session convertToDomain(Session dto);

  public abstract List<Session> convertToDtoSessions(
      Collection<org.jhapy.audit.domain.Session> domains);

  public abstract List<org.jhapy.audit.domain.Session> convertToDomainSessions(
      Collection<Session> dtos);
}
