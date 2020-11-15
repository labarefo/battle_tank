package sopra.immobilier.games.mci.enumeration;

import sopra.immobilier.games.mci.beans.IAction;

public enum TirEnum implements IAction {

  DROITE ("6", "tir à droite"),
  GAUCHE("4", "tir à gauche"),
  HAUT("8", "tir en haut"),
  BAS("2", "tir en bas");

  private final String code;
  private final String libelle;

  TirEnum(String code, String libelle) {
    this.code = code;
    this.libelle = libelle;
  }

  public static TirEnum fromCode(String code){
    for (TirEnum e: TirEnum.values()){
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
}
