package config;

/**
 * Some system level properties.
 *
 * @author Rugal Bernstein
 */
public interface SystemDefaultProperty {

  String ENCODING = "UTF-8";

  String SCHEMA = "pt";

  String PEER_ID_PATTERN = "^-(\\w{2}\\d{4})-(\\w{12})$";
}
