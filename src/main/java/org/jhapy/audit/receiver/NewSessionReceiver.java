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

package org.jhapy.audit.receiver;

import org.jhapy.audit.exception.ServiceException;
import org.jhapy.audit.service.SessionService;
import org.jhapy.commons.utils.HasLogger;
import org.jhapy.dto.messageQueue.NewSession;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

/**
 * @author jHapy Lead Dev.
 * @version 1.0
 * @since 2019-07-02
 */
@Component
public class NewSessionReceiver implements HasLogger {

  private final SessionService sessionService;

  public NewSessionReceiver(SessionService sessionService) {
    this.sessionService = sessionService;
  }

  @RabbitListener(queues = "#{newSessionQueue.name}")
  public void newSession(Message<NewSession> message) {
    String loggerPrefix = getLoggerPrefix("newSession");
    logger().info(loggerPrefix + "+++++++++++++++++++++++++++++++++++++++++++++++++++++");
    MessageHeaders headers = message.getHeaders();
    logger().info(loggerPrefix + "Headers received : " + headers);

    NewSession newSession = message.getPayload();

    try {
      sessionService
          .login(newSession.getJsessionId(), newSession.getSourceIp(), newSession.getUsername(),
              newSession.getIsSuccess(), newSession.getError());
    } catch (ServiceException e) {
      logger().error(loggerPrefix + "Something wrong happend : " + e.getLocalizedMessage(), e);
    }

    logger().info(loggerPrefix + "Message content received : " + newSession);
    logger().info(loggerPrefix + "+++++++++++++++++++++++++++++++++++++++++++++++++++++");
  }
}
