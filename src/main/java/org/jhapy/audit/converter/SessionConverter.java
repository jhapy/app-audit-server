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

package org.jhapy.audit.converter;

import org.jhapy.commons.utils.OrikaBeanMapper;
import org.jhapy.dto.domain.audit.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

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