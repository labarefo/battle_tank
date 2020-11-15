package sopra.immobilier.games.mci.enumeration;

public enum TypeBonusEnum {
  BOUCLIER ("b", "Bouclier"),
  SUPPORT_AERIEN("a", "Support aérien"),
  DOUBLE_TIR("t", "Double tir");

  private final String code;
  private final String libelle;

  TypeBonusEnum(String code, String libelle) {
    this.code = code;
    this.libelle = libelle;
  }

  public static TypeBonusEnum fromCode(String code){
    for (TypeBonusEnum e: TypeBonusEnum.values()){
      if(e.code.equalsIgnoreCase(code)){
        return e;
      }
    }
    return null;
  }

  public String getCode() {
    return code;
  }

  public String getLibelle() {
    return libelle;
  }
}
