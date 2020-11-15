/**
 *
 */
package sopra.immobilier.games.mci;

import java.util.Properties;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sopra.immobilier.games.mci.http.client.GameHttpClient;
import sopra.immobilier.games.mci.utils.Const;
import sopra.immobilier.games.mci.utils.PropertiesUtility;

/**
 * Cette classe est le point d'entrée de l'interface Radio.<br>
 *
 * @author mcissoko
 *
 */
public class BattleTank implements Daemon {

  protected static Logger log = LoggerFactory.getLogger(BattleTank.class);


  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    Daemon daemon = new BattleTank();
    daemon.start();
  }

  private void startInterface() {
    try {
      String repertoirebin = GameHttpClient.class.getProtectionDomain().getCodeSource().getLocation().toURI().toString();
      Properties properties = PropertiesUtility.getInstance().read(repertoirebin, Const.PATH_FICHIER_PROPERTIES);
      if (properties == null) {
        log.error("Le fichier properties n'est pas trouvé : {}", Const.PATH_FICHIER_PROPERTIES);
        return;
      }
      startHttpClient(properties);


    } catch (Exception e) {
      log.error("{} - main() : ", BattleTank.class.getName(), e);
    }

  }

  private void startHttpClient(Properties properties) {
    GameHttpClient client = new GameHttpClient(properties);
    try {
      client.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  @Override
  public void init(DaemonContext context) throws DaemonInitException, Exception {
  }

  @Override
  public void start() throws Exception {
    startInterface();
  }

  @Override
  public void stop() throws Exception {
  }

  @Override
  public void destroy() {

  }
}
