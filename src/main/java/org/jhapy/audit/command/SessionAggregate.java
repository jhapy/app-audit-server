package org.jhapy.audit.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.jhapy.audit.converter.SessionConverter;
import org.jhapy.cqrs.command.AbstractBaseAggregate;
import org.jhapy.cqrs.command.audit.DeleteSessionCommand;
import org.jhapy.cqrs.command.audit.LoginCommand;
import org.jhapy.cqrs.command.audit.LogoutCommand;
import org.jhapy.cqrs.event.audit.LoginEvent;
import org.jhapy.cqrs.event.audit.LogoutEvent;
import org.jhapy.cqrs.event.audit.SessionDeletedEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

import static org.axonframework.modelling.command.AggregateLifecycle.markDeleted;

@Aggregate
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SessionAggregate extends AbstractBaseAggregate {
  private String username;
  private String sourceIp;
  private Instant sessionStart;
  private Instant sessionEnd;
  private Long sessionDuration;
  private Boolean isSuccess;
  private String error;
  private String jsessionId;

  private transient SessionConverter converter;

  @CommandHandler
  public SessionAggregate(
      @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") LoginCommand command,
      @Autowired SessionConverter mailConverter) {
    this.converter = mailConverter;

    if (StringUtils.isBlank(command.getJsessionId())) {
      throw new IllegalArgumentException("JsessionId action can not be empty");
    }

    LoginEvent event = converter.toLoginEvent(command);
    event.setId(command.getId());
    AggregateLifecycle.apply(event);
  }

  @Autowired
  public void setConverter(SessionConverter converter) {
    this.converter = converter;
  }

  @CommandHandler
  public void handle(LogoutCommand command) {
    LogoutEvent event = converter.toLogoutEvent(command);
    AggregateLifecycle.apply(event);
  }

  @CommandHandler
  public void handle(DeleteSessionCommand command) {
    SessionDeletedEvent event = new SessionDeletedEvent(command.getId());
    AggregateLifecycle.apply(event);
  }

  @EventSourcingHandler
  public void on(LoginEvent event) {
    converter.updateAggregateFromLoginEvent(event, this);
  }

  @EventSourcingHandler
  public void on(LogoutEvent event) {
    converter.updateAggregateFromLogoutEvent(event, this);
  }

  @EventSourcingHandler
  public void on(SessionDeletedEvent event) {
    markDeleted();
  }
}
