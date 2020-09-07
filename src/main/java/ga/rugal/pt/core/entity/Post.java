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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

/**
 * Post entity.
 *
 * @author Rugal Bernstein
 */
@Data
@Entity
@Table(name = "post", schema = SCHEMA)
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class Post {

  private static final String SEQUENCE_NAME = "post_pid_seq";

  @Basic(optional = false)
  @Column
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
  @Id
  @SequenceGenerator(name = SEQUENCE_NAME, allocationSize = 1,
                     sequenceName = SCHEMA + "." + SEQUENCE_NAME)
  private Integer pid;

  @Size(max = 100)
  @Column(length = 100)
  private String title;

  @Size(max = 2147483647)
  @Column(length = 2147483647)
  private String content;

  /**
   * Disable post by default.<BR>
   * Only enable post after 1. torrent file uploaded, verified 2. post published.
   */
  @Column
  private Boolean enable = false;

  @Size(max = 50)
  @Column(length = 50)
  private String hash;

  @Column
  private byte[] torrent;

  @Column
  private Long size;

  @JoinColumn(name = "author", referencedColumnName = "uid")
  @ManyToOne
  private User author;

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
