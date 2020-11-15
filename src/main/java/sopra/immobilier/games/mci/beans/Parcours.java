package sopra.immobilier.games.mci.beans;

public class Parcours {
  private Joueur joueur;
  private int distanceLigne;
  private int distanceColonne;

  public Joueur getJoueur() {
    return joueur;
  }

  public void setJoueur(Joueur joueur) {
    this.joueur = joueur;
  }

  public int getDistanceLigne() {
    return distanceLigne;
  }

  public void setDistanceLigne(int distanceLigne) {
    this.distanceLigne = distanceLigne;
  }

  public int getDistanceColonne() {
    return distanceColonne;
  }

  public void setDistanceColonne(int distanceColonne) {
    this.distanceColonne = distanceColonne;
  }
}
