package org.jhapy.audit.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author jHapy Lead Dev.
 * @version 1.0
 * @since 2019-05-24
 */
@Document(collection = "changelog")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ChangeLog extends BaseEntity {

  private String entity;

  private Long recordId;

  private Session session;

  private String attribute;

  private String oldValue;

  private String newValue;
}
