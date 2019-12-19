package config;

/**
 * For all constant string.
 *
 * @author Rugal Bernstein
 */
public interface Constant {

  String UID = "uid";

  String SECRET = "secret";

  String HOST = "host";

  String PORT = "port";

  String PID = "pid";

  String FILE = "file";

  String ANNOUNCE_TEMPLATE_URL = "http://%s:%d/announce?uid=%d&secret=%s";

  String BITTORRENT_MIME = "application/x-bittorrent";

  String TORRENT_SUFFIX = ".torrent";
}
