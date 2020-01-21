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

import lombok.Data;

/**
 * PostTag entity.<BR>
 * Represent assignment of tags on post.
 *
 * @author Rugal Bernstein
 */
@Data
@Entity
@Table(name = "post_tag", schema = SCHEMA)
public class PostTag {

  private static final String SEQUENCE_NAME = "post_tag_ptid_seq";

  @Basic(optional = false)
  @Column
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
  @Id
  @SequenceGenerator(name = SEQUENCE_NAME, allocationSize = 1,
                     sequenceName = SCHEMA + "." + SEQUENCE_NAME)
  private Integer ptid;

  @JoinColumn(name = "pid", referencedColumnName = "pid")
  @ManyToOne
  private Post post;

  @JoinColumn(name = "tid", referencedColumnName = "tid")
  @ManyToOne
  private Tag tag;

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
