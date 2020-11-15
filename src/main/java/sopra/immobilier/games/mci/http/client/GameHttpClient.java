package sopra.immobilier.games.mci.http.client;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sopra.immobilier.games.mci.Game;
import sopra.immobilier.games.mci.beans.EndGameException;
import sopra.immobilier.games.mci.utils.Const;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class GameHttpClient {

  public static Logger log = LoggerFactory.getLogger(GameHttpClient.class);
  Properties properties;

  public GameHttpClient(Properties properties) {
    this.properties = properties;
  }


  public void start() throws Exception {

    log.info("Le fichier properties est lu avec succès");
    log.info("{}", properties);
    String url = properties.getProperty(Const.SERVER_URL, "http://duffbattletank.ddns.net");
    int port = Integer.parseInt(properties.getProperty(Const.SERVER_PORT, "5000"));
    try (Socket socket = new Socket(url, port)) {

      InputStream input = socket.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.US_ASCII));

      OutputStream output = socket.getOutputStream();
      PrintWriter responseWwriter = new PrintWriter(output, true, StandardCharsets.US_ASCII);
      Game.getInstance().init(properties);
      try {
        while (true) {
          String commande = reader.readLine();
          if (StringUtils.isNoneBlank(commande)) {
            StringBuilder response = new StringBuilder(Game.getInstance().onCommande(commande));
            if(StringUtils.isNoneBlank(response.toString())){
              response.append(Const.CRLF);
              responseWwriter.println(response.toString());
            }
          }
        }
      } catch (EndGameException e) {
        socket.close();
      }

    } catch (UnknownHostException ex) {


    } catch (IOException ex) {

    }
  }


}
