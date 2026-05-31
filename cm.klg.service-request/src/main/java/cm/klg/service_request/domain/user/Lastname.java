package cm.klg.service_request.domain.user;


public record Lastname(String value) {
  public static Lastname from(String value) {
      return new Lastname(value);
  }
}
