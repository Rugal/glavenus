package config;

/**
 * For all constant string.
 *
 * @author Rugal Bernstein
 */
public interface Constant {

  String UID = "uid";

  String EMAIL = "email";

  String SECRET = "secret";

  String PASSWORD = "password";

  /**
   * For shorter name.
   */
  String P = PASSWORD;

  String HOST = "host";

  String PORT = "port";

  String PID = "pid";

  String TID = "tid";

  String TOKEN = "token";

  String AUTHORIZATION = "Authorization";

  String FILE = "file";

  String ANNOUNCE_TEMPLATE_URL = "http://%s:%d/announce?uid=%d&secret=%s";

  String BITTORRENT_MIME = "application/x-bittorrent";

  String TORRENT_SUFFIX = ".torrent";

  String STAR = "*";

  String SUBJECT = "authentication";

  String ISSUER = "pt";

  String SIZE = "size";

  String INDEX = "index";

  String PEER_ID = "peer_id";
}
