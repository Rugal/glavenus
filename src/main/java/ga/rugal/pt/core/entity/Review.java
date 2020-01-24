package ga.rugal.pt.core.entity;

import static config.SystemDefaultProperty.SCHEMA;

import java.time.Instant;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * Review entity. Represents user comment and review against a post.
 *
 * @author Rugal Bernstein
 */
@Data
@Entity
@Table(name = "review", schema = SCHEMA)
public class Review {

  private static final String SEQUENCE_NAME = "review_rid_seq";

  @Basic(optional = false)
  @Column
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
  @Id
  @SequenceGenerator(name = SEQUENCE_NAME, allocationSize = 1,
                     sequenceName = SCHEMA + "." + SEQUENCE_NAME)
  private Integer rid;

  @Size(max = 2147483647)
  @Column(length = 2147483647)
  private String content;

  @JoinColumn(name = "pid", referencedColumnName = "pid")
  @ManyToOne
  private Post post;

  @JoinColumn(name = "uid", referencedColumnName = "uid")
  @ManyToOne
  private User author;

  /**
   * Need to set a default rate.
   */
  @Column
  private Integer rate;

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
