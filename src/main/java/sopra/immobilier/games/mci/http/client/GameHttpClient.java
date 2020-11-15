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
import java.util.Scanner;

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
    try (var socket = new Socket(url, port)) {
      var in = new Scanner(socket.getInputStream(), StandardCharsets.US_ASCII);
      var out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII), true);

      Game.getInstance().init(properties);
      try {
        while (in.hasNextLine()) {
          String commande = in.nextLine();
          if (StringUtils.isNoneBlank(commande)) {
            StringBuilder response = new StringBuilder(Game.getInstance().onCommande(commande));
            if(StringUtils.isNoneBlank(response.toString())){
              response.append(Const.CRLF);
              out.println(response.toString());
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
