package sopra.immobilier.games.mci.beans;

import sopra.immobilier.games.mci.enumeration.TypeBonusEnum;

import java.io.Serializable;
import java.util.Objects;

public class Bonus implements IAction, Serializable {

  private TypeBonusEnum typeBonusEnum;
  private Position position;

  public Bonus(TypeBonusEnum typeBonusEnum, Position position) {
    this.typeBonusEnum = typeBonusEnum;
    this.position = position;
  }

  public TypeBonusEnum getTypeBonusEnum() {
    return typeBonusEnum;
  }

  public void setTypeBonusEnum(TypeBonusEnum typeBonusEnum) {
    this.typeBonusEnum = typeBonusEnum;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Bonus bonus = (Bonus) o;
    return typeBonusEnum == bonus.typeBonusEnum &&
            Objects.equals(position, bonus.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(typeBonusEnum, position);
  }

  @Override
  public String getCode() {
    return String.format("%s;%d;%d", typeBonusEnum.getCode(), position.getX(), position.getY());
  }
}
