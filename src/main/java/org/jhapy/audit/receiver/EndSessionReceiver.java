package org.jhapy.audit.receiver;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.jhapy.audit.service.SessionService;
import org.jhapy.commons.utils.HasLogger;
import org.jhapy.dto.messageQueue.EndSession;

/**
 * @author jHapy Lead Dev.
 * @version 1.0
 * @since 2019-07-02
 */
@Component
public class EndSessionReceiver implements HasLogger {

  private final SessionService sessionService;

  public EndSessionReceiver(SessionService sessionService) {
    this.sessionService = sessionService;
  }

  @JmsListener(destination = "endSession")
  public void endSession(Message<EndSession> message) {
    String loggerPrefix = getLoggerPrefix("endSession");
    logger().info(loggerPrefix + "+++++++++++++++++++++++++++++++++++++++++++++++++++++");
    MessageHeaders headers = message.getHeaders();
    logger().info(loggerPrefix + "Headers received : " + headers);

    EndSession endSession = message.getPayload();

    sessionService.logout(endSession.getJsessionId());

    logger().info(loggerPrefix + "Message content received : " + endSession);
    logger().info(loggerPrefix + "+++++++++++++++++++++++++++++++++++++++++++++++++++++");
  }
}
