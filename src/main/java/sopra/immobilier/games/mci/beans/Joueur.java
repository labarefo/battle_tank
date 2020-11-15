package sopra.immobilier.games.mci.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Joueur implements Serializable {

  private String nom;
  private Position position;
  private int vies;
  private Bonus bonus;
  private boolean mort = false;

  public void touche(){
    this.vies--;
    if(vies <= 0){
      vies = 0;
      mort = true;
    }
  }

  public boolean isMort() {
    return mort;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public int getVies() {
    return vies;
  }

  public void setVies(int vies) {
    this.vies = vies;
  }

  public Bonus getBonus() {
    return bonus;
  }

  public void setBonus(Bonus bonus) {
    this.bonus = bonus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Joueur joueur = (Joueur) o;
    return Objects.equals(nom, joueur.nom) ;
  }

  @Override
  public int hashCode() {
    return Objects.hash(nom);
  }

}
