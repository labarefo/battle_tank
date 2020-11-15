package sopra.immobilier.games.mci.enumeration;

import sopra.immobilier.games.mci.beans.IAction;

public enum DeplacementEnum implements IAction {

  DROITE ("d", "déplacement à droite (Est)", DirectionEnum.HORIZONTALE),
  GAUCHE("g", "déplacement à gauche (Ouest)", DirectionEnum.HORIZONTALE),
  HAUT("h", "déplacement en haut (Nord)", DirectionEnum.VERTICALE),
  BAS("B", "déplacement en bas (Sud)", DirectionEnum.VERTICALE);

  private final String code;
  private final String libelle;
  private final DirectionEnum direction;

  DeplacementEnum(String code, String libelle, DirectionEnum direction) {
    this.code = code;
    this.libelle = libelle;
    this.direction = direction;
  }

  public static DeplacementEnum fromCode(String code){
    for (DeplacementEnum e: DeplacementEnum.values()){
      if(e.code.equalsIgnoreCase(code)){
        return e;
      }
    }
    return null;
  }

  @Override
  public String getCode() {
    return code;
  }

  public String getLibelle() {
    return libelle;
  }

  public DirectionEnum getDirection() {
    return direction;
  }

}
