package ga.rugal.pt.core.entity;

import static config.SystemDefaultProperty.SCHEMA;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
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

  /**
   * {@code Ciphered} password with BCrypt algorithm.<BR>
   * This will be used for user login via API and client.<BR>
   * Plain text will be sent in network transmission and be verified in controller.<BR>
   * The reason is, compromise in network transmission will expose single user password for sure,
   * but compromise service database would breaching all users.
   */
  @Size(max = 100)
  @Column(length = 100)
  private String password;

  @Size(max = 50)
  @Column(length = 50)
  private String email;

  /**
   * Secret for announce authentication in {@code plain text}.<BR>
   * This will only be used for client announcement.<BR>
   * Ciphered secret with BCrypt algorithm will be created while user downloading torrent.<BR>
   * Secret will be automatically and periodically generated, hence compromise does not affect too
   * much.
   */
  @Size(max = 50)
  @Column(length = 50)
  private String secret;

  @Column
  private Long download = 0L;

  @Column
  private Long upload = 0L;

  @Column
  private Integer credit = 0;

  /**
   * Default as 0.
   */
  @Column
  private Integer status;

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

  public String getStatus() {
    return Status.valueOfCode(this.status).toString();
  }

  public boolean isValid() {
    return this.status == Status.VALID.code;
  }

  public enum Status {

    VALID(0),
    BAN(1);

    public final int code;

    Status(final int code) {
      this.code = code;
    }

    private static final Map<Integer, Status> BY_CODE = new HashMap<>(2);

    static {
      for (Status e : Status.values()) {
        BY_CODE.put(e.code, e);
      }
    }

    public static Status valueOfCode(final int code) {
      return BY_CODE.get(code);
    }
  }
}
