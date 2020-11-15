package sopra.immobilier.games.mci;

import sopra.immobilier.games.mci.utils.Const;

import java.util.Properties;

public class Game {


  private String commande;

  private BattleField battleField;
  private Game() {
    battleField = new BattleField();
  }
  private static Game instance;
  public static synchronized Game getInstance() {
    if(instance == null){
      instance = new Game();
    }
    return instance;
  }

  public String onCommande(String commande) {
    return battleField.process(commande);
  }

  public void init(Properties properties) {
    String name = properties.getProperty(Const.GAMER_NAME, "balamouss");
    battleField.setGamerName(name);
  }
}
