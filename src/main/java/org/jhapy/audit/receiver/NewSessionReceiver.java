package org.jhapy.audit.receiver;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.jhapy.audit.exception.ServiceException;
import org.jhapy.audit.service.SessionService;
import org.jhapy.commons.utils.HasLogger;
import org.jhapy.dto.messageQueue.NewSession;

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

  @JmsListener(destination = "newSession")
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
