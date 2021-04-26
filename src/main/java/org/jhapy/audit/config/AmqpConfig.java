package org.jhapy.audit.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Alexandre Clavaud.
 * @version 1.0
 * @since 27/03/2021
 */
@Configuration
public class AmqpConfig {

  @Bean
  public Queue newSessionQueue() {
    return new Queue("audit.newSession", true);
  }

  @Bean
  public Queue endSessionQueue() {
    return new Queue("audit.endSession", true);
  }
}
