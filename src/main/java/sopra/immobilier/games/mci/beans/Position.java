package sopra.immobilier.games.mci.beans;

import java.io.Serializable;
import java.util.Objects;

public class Position implements Serializable {
  private int x;
  private int y;

  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public boolean isDroitePossible(int maxLigne){
    if(x < maxLigne){
      return true;
    }
    return false;
  }
  public void deplacementDroite(){
     x++;
  }
  public boolean isGauchePossible(){
    if(x > 0){
      return true;
    }
    return false;
  }
  public void deplacementGauche(){
    x--;
  }

  public boolean isBasPossible(int maxColonne){
    if(y < maxColonne){
      return true;
    }
    return false;
  }
  public void deplacementBas(){
    y++;
  }
  public boolean isHautPossible(){
    if(y > 0){
      return true;
    }
    return false;
  }
  public void deplacementHaut(){
    y--;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Position position = (Position) o;
    return x == position.x &&
            y == position.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }
}
