package config;

/**
 * For all constant string.
 *
 * @author Rugal Bernstein
 */
public interface Constant {

  String UID = "uid";

  String SECRET = "secret";

  String PASSWORD = "password";

  /**
   * For shorter name.
   */
  String P = PASSWORD;

  String HOST = "host";

  String PORT = "port";

  String PID = "pid";

  String FILE = "file";

  String ANNOUNCE_TEMPLATE_URL = "http://%s:%d/announce?uid=%d&secret=%s";

  String BITTORRENT_MIME = "application/x-bittorrent";

  String TORRENT_SUFFIX = ".torrent";

  String STAR = "*";

  String SUBJECT = "authentication";

  String ISSUER = "pt";
}
