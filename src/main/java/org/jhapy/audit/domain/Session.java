package org.jhapy.audit.domain;

import java.time.Instant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author jHapy Lead Dev.
 * @version 1.0
 * @since 2019-05-24
 */
@Document(collection = "session")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Session extends BaseEntity {

  private String username;

  private String sourceIp;

  private Instant sessionStart;

  private Instant sessionEnd;

  private Long sessionDuration;

  private Boolean isSuccess;

  private String error;

  @Indexed(unique = true)
  private String jsessionId;

}
