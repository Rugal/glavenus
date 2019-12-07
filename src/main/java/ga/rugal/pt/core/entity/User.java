package ga.rugal.pt.core.entity;

import static config.SystemDefaultProperty.SCHEMA;

import java.time.Instant;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * User entity.
 *
 * @author Rugal Bernstein
 */
@Data
@Entity
@Table(name = "user", schema = SCHEMA)
public class User {

  private static final String SEQUENCE_NAME = "user_uid_seq";

  @Basic(optional = false)
  @Column
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
  @Id
  @SequenceGenerator(name = SEQUENCE_NAME, allocationSize = 1,
                     sequenceName = SCHEMA + "." + SEQUENCE_NAME)
  private Integer uid;

  @Size(max = 50)
  @Column(length = 50)
  private String username;

  @Size(max = 100)
  @Column(length = 100)
  private String password;

  @Size(max = 50)
  @Column(length = 50)
  private String email;

  @Column
  private Long download;

  @Column
  private Long upload;

  @Column
  private Integer credit;

  @Column
  private Short status;

  @Column(name = "create_at")
  private Long createAt;

  @Column(name = "update_at")
  private Long updateAt;

  @PrePersist
  void onCreate() {
    this.createAt = Instant.now().getEpochSecond();
  }

  @PreUpdate
  void onUpdate() {
    this.updateAt = Instant.now().getEpochSecond();
  }
}
