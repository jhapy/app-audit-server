package org.jhapy.audit.converter;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.jhapy.commons.utils.OrikaBeanMapper;
import org.jhapy.dto.domain.audit.Session;

/**
 * @author jHapy Lead Dev.
 * @version 1.0
 * @since 2019-06-05
 */
@Component
public class SessionConverter {

  private final OrikaBeanMapper orikaBeanMapper;

  public SessionConverter(OrikaBeanMapper orikaBeanMapper) {
    this.orikaBeanMapper = orikaBeanMapper;
  }

  @Bean
  public void utilsConverters() {
    orikaBeanMapper.addMapper(org.jhapy.audit.domain.Session.class, Session.class);
    orikaBeanMapper.addMapper(Session.class, org.jhapy.audit.domain.Session.class);
  }
}