package sopra.immobilier.games.mci.beans;

public class EndGameException extends RuntimeException {



  private String detailMessage;

  /**
   * Construit une nouvelle exception ayant comme information textuelle le
   * message passé en paramètre.
   * @param message description textuelle de l'exception
   */
  public EndGameException(String message) {
    super(message);
    detailMessage = message;
  }

  /**
   * Construit un nouvelle exception chaînée à l'exception donnée en paramètre
   * @param message description textuelle de l'exception
   * @param nestedException exception chaînée
   */
  public EndGameException(String message, Throwable nestedException) {
    super(message, nestedException);
    detailMessage = message;
  }

  /**
   * Permmet d'ajouter un message qui prefixe le message original.
   * @param prefixMessage le chaine de caractère prefixant le message
   */
  public void addMessagePrefix(String prefixMessage) {
    detailMessage = prefixMessage + detailMessage;
  }

  @Override
  public String getMessage() {
    return detailMessage;
  }
}
