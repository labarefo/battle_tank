package sopra.immobilier.games.mci;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import sopra.immobilier.games.mci.beans.*;
import sopra.immobilier.games.mci.enumeration.DeplacementEnum;
import sopra.immobilier.games.mci.enumeration.DirectionEnum;
import sopra.immobilier.games.mci.enumeration.TirEnum;
import sopra.immobilier.games.mci.enumeration.TypeBonusEnum;
import sopra.immobilier.games.mci.utils.Const;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BattleField {

  List<Joueur> listeJoueurs;
  private Joueur moi;
  List<Joueur> adversaires;
  private int maxLigne = 16;
  private int maxColonne = 26;
  private int taille = maxLigne * maxColonne;

  private String gamerName;
  private String secret;

  private static final int _NOMBRE_PROPRIETE = 5;

  public BattleField() {
//    buildCarte(commande);
  }

  int[][] carte;

  private final String pattern = "\\d";

  public String process(String request) {
    // compute commande
    String[] data = request.split("\\:");
    String commande = data[0].trim();
    String response = "";
    switch (commande) {
      case Const.KO:
        // todo ko
        break;
      case Const.FIN:
        throw new EndGameException("Fin de la partie");
      case Const.NOM:
        response = gamerName;
        break;
      case Const.CLE:
        if (data.length > 1) {
          // reception de clé
          String arg = getArgument(data);
          this.secret = arg;
        } else {
          // envoi de cle
          response = this.secret;
        }
        break;
      case Const.CARTE:
        String arg = getArgument(data);
        buildCarte(arg);
        break;
      case Const.CONNECTE:

        break;
      case Const.WARZONE:
        int offset = Integer.parseInt(data[1].trim());
        warzone(offset);
        break;
      case Const.ACTION:
        arg = getArgument(data);
        response = jouer(arg);
        break;


    }

    return response;
  }

  /**
   * @param joueurs
   * @return
   */
  private String jouer(String joueurs) {
    String response = StringUtils.EMPTY;
    listeJoueurs = buildJoueurs(joueurs);
    // recuper le mien
    this.moi = leMien(listeJoueurs);
    if(moi == null) {
      // TODO erreur server ???
      return response;
    }
    // recuper les adversaires
    adversaires = listeJoueurs.stream().filter(j -> !j.getNom().equalsIgnoreCase(moi.getNom())).collect(Collectors.toList());
    if(adversaires.isEmpty()){
      // je ne fait rien ouis que je n'ai pas d'adversaires
      return response;
    }

    List<IAction> actions = new ArrayList<>();
    DeplacementEnum direction = findDirection(moi.getPosition());
    rechercheActions(actions, moi.getPosition(), direction == null ? direction : DeplacementEnum.DROITE);
    // recherche des actions à faires
    if(actions.isEmpty()){
      return response;
    }
    response = action(actions);

    return response;
  }





//  private void actionDroite(Position position, List<IAction> actions) {
//    if(actions.size() > 1){
//      return;
//    }
//    List<Joueur> aDroite = ciblesDroite(position);
//    if(aDroite.size()> 0){
//      actions.add(TirEnum.DROITE);
//      // deplacement
//      DeplacementEnum action = deplacer(position);
//      if (action != null){
//        actions.add(action);
//        return;
//      }
//    }
//  }
  private DeplacementEnum safeDirection(Position position, List<Joueur> adverses){
    List<Joueur> cible = ciblesDroite(position, adverses);
    if(cible.isEmpty()){
      return DeplacementEnum.DROITE;
    }
    cible = ciblesGauche(position, adverses);
    if(cible.isEmpty()){
      return DeplacementEnum.GAUCHE;
    }
    cible = ciblesHaut(position, adverses);
    if(cible.isEmpty()){
      return DeplacementEnum.HAUT;
    }
    cible = ciblesBas(position, adverses);
    if(cible.isEmpty()){
      return DeplacementEnum.BAS;
    }
    return null;
  }

  private void tirer(Position position, List<IAction> actions) {
    List<Joueur> aDroite = ciblesDroite(position, adversaires);
    List<Joueur> aGauche = ciblesGauche(position, adversaires);

    List<Joueur> enHaut = ciblesHaut(position, adversaires);
    List<Joueur> enBas = ciblesBas(position, adversaires);
    TirEnum tir = null;
    if(aDroite.size() > 0 && !hasTir(actions, TirEnum.DROITE)){
      tir = TirEnum.DROITE;
      Joueur cible = aDroite.get(0);
      cible.touche();
    }else if(aGauche.size() > 0 && !hasTir(actions, TirEnum.GAUCHE)){
      tir = TirEnum.GAUCHE;
      Joueur cible = aGauche.get(0);
      cible.touche();
    }else if(enHaut.size() > 0 && !hasTir(actions, TirEnum.HAUT)){
      tir = TirEnum.HAUT;
      Joueur cible = enHaut.get(0);
      cible.touche();
    }else if(enBas.size() > 0 && !hasTir(actions, TirEnum.BAS)){
      tir = TirEnum.BAS;
      Joueur cible = enBas.get(0);
      cible.touche();
    }
    if(tir != null){
      actions.add(tir);
    }
  }


  /**
   * @param position
   * @param actions
   * @param direction
   */
  private void rechercheActions(List<IAction> actions, Position position, DeplacementEnum direction) {
    if(actions.size() > 1){
      return;
    }
    tirer(position, actions);
    if(actions.size() > 1){
      return;
    }
    //Je me deplace vers le plus proche
    switch (direction){
      case DROITE:
        if(directionDroite(actions, position)){
          actions.add(DeplacementEnum.DROITE);
        }else {
          explorer(actions, position);
        }
        break;
      case GAUCHE:
        if(directionGauche(actions, position)){
          actions.add(DeplacementEnum.GAUCHE);
        }else {
          explorer(actions, position);
        }
        break;
      case HAUT:
        if(directionHaut(actions, position)){
          actions.add(DeplacementEnum.HAUT);
        }else {
          explorer(actions, position);
        }
        break;
      case BAS:
        if(directionBas(actions, position)){
          actions.add(DeplacementEnum.BAS);
        }else {
          explorer(actions, position);
        }
        break;
    }


  }


  private void explorer(List<IAction> actions, Position position) {
    // je cherche la direction dans laquelle je suis sensé allé
    DeplacementEnum deplacement = findDirection(position);
    if(deplacement == null){
      // je ne peux aller dans aucune direction
      if (moi.getBonus() != null){// possibilité d'utiliser un bonus ?
        // deployer le bonus

      }else {
        if(actions.stream().allMatch(a -> DeplacementEnum.fromCode(a.getCode()) == null)){
          // je fait un double deplacement
        }else {
          // cela est improbable ==> mais je tire à l'aveugle
          actions.add(TirEnum.DROITE);
        }
      }
    }else {
      rechercheActions(actions, position, deplacement);
    }
  }
  /**
   * J'essaye de me deplacer à droite
   *
   * @param actions
   * @param position
   * @return
   */
  private boolean directionDroite(List<IAction> actions, Position position) {
    // à droite
    if(position.isDroitePossible(maxLigne)){
      position.deplacementDroite();
      boolean result = controleDeplacement(position, DeplacementEnum.DROITE);
      if(!result){
        // je retourne à la position initiale
        position.deplacementGauche();
      }
    }
    return false;
  }

  /**
   * @param actions
   * @param position
   * @return
   */
  private boolean directionGauche(List<IAction> actions, Position position) {
    // à gauche
    if(position.isGauchePossible()){
      position.deplacementGauche();
      boolean result = controleDeplacement(position, DeplacementEnum.GAUCHE);
      if(!result){
        // je retourne à la position initiale
        position.deplacementDroite();
      }
    }
    return false;
  }

  /**
   * @param actions
   * @param position
   * @return
   */
  private boolean directionHaut(List<IAction> actions, Position position) {
    // à gauche
    if(position.isHautPossible()){
      position.deplacementHaut();
      boolean result = controleDeplacement(position, DeplacementEnum.HAUT);
      if(!result){
        // je retourne à la position initiale
        position.deplacementBas();
      }
    }
    return false;
  }

  /**
   * @param actions
   * @param position
   * @return
   */
  private boolean directionBas(List<IAction> actions, Position position) {
    // à gauche
    if(position.isBasPossible(maxColonne)){
      position.deplacementBas();
      boolean result = controleDeplacement(position, DeplacementEnum.BAS);
      if(!result){
        // je retourne à la position initiale
        position.deplacementHaut();
      }
    }
    return false;
  }

  /**
   * @param position
   * @param deplacement
   * @return
   */
  private boolean controleDeplacement(Position position, DeplacementEnum deplacement) {

    boolean result = false;
    if(accessible(position)){
      // je regarde si un adversaire pourrait se deplacer et ensuite m'attager
      Set<DirectionEnum> directionsAttaque = attaqueAdverses(position);
      if(directionsAttaque.isEmpty()){
        result = true;
      }else {
        if(directionsAttaque.size() > 1){
          // attaque possible dans les 2 directions
          result = false;
        }else{
          // attaque possible selement dans une direction
          DirectionEnum dir = directionsAttaque.iterator().next();
          result = deplacement.getDirection() != dir;
        }
      }
    }
    return result;
  }

  /**
   * Dans quelles direction viendraient les attaques ?
   * @param position
   * @return
   */
  private Set<DirectionEnum> attaqueAdverses(Position position) {
    Set<DirectionEnum> directions = new HashSet<>();
    for (Joueur adverse: adversaires){
      directions.addAll(directionAttaque(position, adverse.getPosition()));
      if(directions.size() > 1){
        break;
      }
    }
    return directions;
  }

  private Set<DirectionEnum> directionAttaque(Position position, Position adverse){
    int dinstanceX = position.getX() - adverse.getX();
    int dinstanceY = position.getY() - adverse.getY();
    Set<DirectionEnum> directions = new HashSet<>();
    if (dinstanceX == 0){ // meme axe vertical
      // attaque par le haut ou par le bas si aucun obstacle
      if(isAccessible(position.getX(), adverse.getY(), adverse.getY(), false)){
        directions.add(DirectionEnum.VERTICALE);
      }
    }else if(Math.abs(dinstanceX) == 1){
      // attaque par le haut ou par le bas si y = 1 OU aucun obstacle
      if(dinstanceY == 1 || isAccessible(position.getX(), adverse.getY(), adverse.getY(), false)){
        directions.add(DirectionEnum.VERTICALE);
      }
    }
    if (dinstanceY == 0){ // meme axe horizontal
      // attaque par la gache ou la droite OU si aucun obstacle
      if(isAccessible(position.getY(), adverse.getX(), adverse.getX(), true)){
        directions.add(DirectionEnum.HORIZONTALE);
      }
    }else if(Math.abs(dinstanceY) == 1){
      // attaque par la droite ou par la gauche si x= 1 OU aucun obstacle
      if(dinstanceY == 1 || isAccessible(position.getY(), adverse.getX(), adverse.getX(), true)){
        directions.add(DirectionEnum.HORIZONTALE);
      }
    }
    return directions;
  }

  /**
   * @param position
   * @return la direction dans laquelle je prends pas de risque
   */
  private DeplacementEnum findDirection(Position position) {
    Map<DeplacementEnum, List<Joueur>> directionsOuvertes = new HashMap<>();
    for (Joueur adverse: adversaires){
      List<Joueur> list = new ArrayList<>();
      moi.setPosition(position);
      list.add(moi);
      DeplacementEnum direction = safeDirection(adverse.getPosition(), list);
      if(direction != null){
        List<Joueur> liste;
        if(directionsOuvertes.containsKey(direction)){
          liste = directionsOuvertes.get(direction);
        }else{
          list = new ArrayList<>();
        }
        list.add(adverse);
        directionsOuvertes.put(direction, list);
      }
    }
    final int size = adversaires.size();
    Optional<DeplacementEnum> opt = directionsOuvertes.keySet().stream().filter(dir -> directionsOuvertes.get(dir).size() == size).findAny();
    if (opt.isPresent()){
      return opt.get();
    }
    return null;
  }

  /**
   * @param actions
   * @param tir
   * @return
   */
  private boolean hasTir(List<IAction> actions, TirEnum tir) {
    return actions.stream().anyMatch(a -> a.getCode().equals(tir.getCode()));
  }

  /**
   * @param position
   * @return
   */
  private boolean accessible(Position position) {
    return carte[position.getX()][position.getY()] == 0;
  }

  /**
   * @param i
   * @param j
   * @return
   */
  private int[] trier(int i, int j){
    int[] arr = {i, j};
    Arrays.sort(arr);
    return arr;
  }

  /**
   * @param axe
   * @param p1
   * @param p2
   * @param isLigne
   * @return
   */
  private boolean isAccessible(int axe, int p1, int p2, boolean isLigne) {
    int distance = 0;
    int[] indexes = trier(p1, p2);
    if(isLigne){
      for (int i = indexes[0] + 1; i < indexes[1]; i++) {
        carte[axe][i] += distance;
        if (distance > 0) {
          return false;
        }
      }
    }else {
      for (int i = indexes[0] + 1; i < indexes[1]; i++) {
        carte[i][axe] += distance;
        if (distance > 0) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * @param listeJoueurs
   * @return
   */
  private Joueur leMien(List<Joueur> listeJoueurs) {
    Optional<Joueur> opt = listeJoueurs.stream().filter(j -> j.getNom().equalsIgnoreCase(this.gamerName)).findAny();
    if(opt.isPresent()){
      return opt.get();
    }
    // TODO est il possible ???
    return null;
  }

  /**
   * @param joueurs
   * @return
   */
  private List<Joueur> buildJoueurs(String joueurs) {
    String[] data = joueurs.split(";");
    if (data.length < _NOMBRE_PROPRIETE) {
      // TODO erreur server
      return Collections.emptyList();
    }
    List<Joueur> gamers = new ArrayList<>();
    int i = 0;
    while (i < data.length) {
      Joueur joueur = new Joueur();
      joueur.setNom(data[i++]);
      joueur.setPosition(new Position(Integer.parseInt(data[i++]) - 1, Integer.parseInt(data[i++]) - 1));
      joueur.setVies(Integer.parseInt(data[i++]));
      String codeBonus = data[i++];
      if (StringUtils.isNoneBlank(codeBonus)) {
        i++;
        Bonus bonus = new Bonus(TypeBonusEnum.fromCode(codeBonus), new Position(Integer.parseInt(data[i++]), Integer.parseInt(data[i++])));
        joueur.setBonus(bonus);
        i += 2;
      }
      gamers.add(joueur);
    }
    return gamers;
  }

  // TODO
  public static void main(String[] args) {
    BattleField battleField = new BattleField();
    List<Joueur> list = battleField.buildJoueurs("Clement;21;3;10;;Samuel;12;13;10;a;bonus;10;10;0;0;Moussa;11;2;15;;Alice;4;7;10;t;bonus;1;2;0;0");

    System.out.println(list);
  }

  /**
   * @param data
   * @return
   */
  private String getArgument(String[] data) {
    return StringUtils.join(ArrayUtils.subarray(data, 1, data.length));
  }

  /**
   * @param comandeCarte
   * @return
   */
  private void buildCarte(String comandeCarte) {
    Integer[] tableau = Pattern.compile(pattern)
            .matcher(comandeCarte)
            .results()
            .map(MatchResult::group)
            .map(Integer::valueOf)
            .toArray(Integer[]::new);
    if (tableau.length == 0) {
      // TODO
    }
    if (tableau.length != taille) {
      // TODO;
    }
    int[][] champs = new int[maxLigne][maxColonne];
    int k = 0;
    for (int i = 0; i < maxLigne; i++) {
      for (int j = 0; j < maxColonne; j++) {
        champs[i][j] = tableau[k++];
      }
    }
    carte = champs;
  }

  /**
   * @param warzone
   */
  private void reduireCarte(int warzone) {
    int ligne = Math.abs(maxLigne - warzone);
    int colonne = Math.abs(maxColonne - warzone);
    int[][] champs = new int[ligne][colonne];

    for (int i = 0; i < ligne; i++) {
      for (int j = 0; j < colonne; j++) {
        champs[i][j] = carte[i][j];
      }
    }
    carte = champs;
  }

  /**
   * @param actions
   * @return
   */
  private String action(List<IAction> actions) {
    StringBuilder sb = new StringBuilder(actions.get(0).getCode()).append(";");
    if (actions.size() > 1) {
      sb.append(actions.get(1).getCode());
    }
    return sb.toString();
  }

  /**
   * @param position
   * @return
   */
  private List<Joueur> ciblesHaut(Position maPosition, List<Joueur> adverses) {
    return adverses.stream()
            .filter(a -> a.getPosition().getX() == maPosition.getX() && a.getPosition().getY() < maPosition.getY())
            .filter(a -> isAccessible(maPosition.getX(), a.getPosition().getY(), maPosition.getY(), false))
            .sorted(Comparator.comparingInt(Joueur::getVies))
            .collect(Collectors.toList());
  }

  private List<Joueur> ciblesBas(Position maPosition, List<Joueur> adverses) {
    return adverses.stream()
            .filter(a -> a.getPosition().getX() == maPosition.getX() && a.getPosition().getY() > maPosition.getY())
            .filter(a -> isAccessible(maPosition.getX(), a.getPosition().getY(), maPosition.getY(), false))
            .sorted(Comparator.comparingInt(Joueur::getVies))
            .collect(Collectors.toList());
  }

  /**
   * @param position
   * @return
   */
  private List<Joueur> ciblesDroite(Position maPosition, List<Joueur> adverses) {
    return adverses.stream()
            .filter(a -> a.getPosition().getY() == maPosition.getY() && a.getPosition().getX() > maPosition.getX())
            .filter(a -> isAccessible(maPosition.getY(), a.getPosition().getX(), maPosition.getX(), true))
            .sorted(Comparator.comparingInt(Joueur::getVies))
            .collect(Collectors.toList());
  }
  private List<Joueur> ciblesGauche(Position maPosition, List<Joueur> adverses) {
    return adverses.stream()
            .filter(a -> a.getPosition().getY() == maPosition.getY() && a.getPosition().getX() < maPosition.getX())
            .filter(a -> isAccessible(maPosition.getY(), a.getPosition().getX(), maPosition.getX(), true))
            .sorted(Comparator.comparingInt(Joueur::getVies))
            .collect(Collectors.toList());
  }

  /**
   * @param nom
   * @return
   */
  private Joueur getJoueur(String nom) {
    if (StringUtils.isBlank(nom)) {
      throw new IllegalArgumentException("Le non du joueur est obligatoire");
    }
    if (listeJoueurs == null || listeJoueurs.isEmpty()) {
      return null;
    }
    Optional<Joueur> op = listeJoueurs.stream().filter(j -> j.getNom().equals(nom)).findAny();
    if (op.isPresent()) {
      return op.get();
    }
    return null;
  }

  /**
   * @param joueur
   */
  private void addJoueur(Joueur joueur) {
    if (joueur == null) {
      return;
    }
    if (listeJoueurs == null) {
      listeJoueurs = new ArrayList<>();
    }
    Optional<Joueur> op = listeJoueurs.stream().filter(j -> j.getNom().equals(joueur.getNom())).findAny();
    if (op
            .isPresent()) {
      Joueur j = op.get();
      j.setPosition(joueur.getPosition());
      j.setVies(joueur.getVies());
      j.setBonus(joueur.getBonus());
    } else {
      listeJoueurs.add(joueur);
    }
  }

  private List<Joueur> getListeJoueurs() {
    return listeJoueurs;
  }

  private void setListeJoueurs(List<Joueur> listeJoueurs) {
    this.listeJoueurs = listeJoueurs;
  }

  private int[][] getCarte() {
    return carte;
  }

  private void warzone(int w) {
    this.maxLigne = Math.abs(maxLigne - w);
    this.maxColonne = Math.abs(maxColonne - w);
    // TODO recalculer la carte ???
    reduireCarte(w);
  }


  public void setGamerName(String gamerName) {
    this.gamerName = gamerName;
  }


}
