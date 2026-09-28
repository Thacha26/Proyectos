package dslabs.kvstore;

import dslabs.framework.Application;
import dslabs.framework.Command;
import dslabs.framework.Result;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class KVStore implements Application {

  private Map<String, String> kvStore = new HashMap<>(); // o con un constructor 

  public interface KVStoreCommand extends Command {}

  public interface SingleKeyCommand extends KVStoreCommand {
    String key();
  }

  @Data
  public static final class Get implements SingleKeyCommand {
    @NonNull private final String key;

    @Override
    public boolean readOnly() {
      return true;
    }
  }

  @Data
  public static final class Put implements SingleKeyCommand {
    @NonNull private final String key, value;
  }

  @Data
  public static final class Append implements SingleKeyCommand { // a un string se le hace un append al final
    @NonNull private final String key, value;
  }

  public interface KVStoreResult extends Result {}

  @Data
  public static final class GetResult implements KVStoreResult {
    @NonNull private final String value;
  }

  @Data
  public static final class KeyNotFound implements KVStoreResult {} // No encontrada

  @Data
  public static final class PutOk implements KVStoreResult {} // se pudo crear el dato

  @Data
  public static final class AppendResult implements KVStoreResult {  // se crea el dat y se devuelve el valor final del string
    @NonNull private final String value;
  }

  // Your code here...

  @Override
  public KVStoreResult execute(Command command) {
    if (command instanceof Get) {
      Get g = (Get) command;
      // Si está almacenada
      if (kvStore.containsKey(g.key())) {
        return new GetResult(kvStore.get(g.key()));
      } else{ //si no
      return new KeyNotFound();
      }
    }

    if (command instanceof Put) {
      Put p = (Put) command;
      kvStore.put(p.key(), p.value()); // String key, value;
      return new PutOk();
    }

    if (command instanceof Append) {
      Append a = (Append) command;
      String currentvalue = kvStore.getOrDefault(a.key(), ""); // si existe devuelve el valor, si no devuelve ""
      String newValue = currentvalue + a.value(); // concat valor existente con el nuevo
      kvStore.put(a.key(), newValue); // actualiza el valor
      return new AppendResult(newValue); // el valor nuevo es el que se devuelve
    }

    throw new IllegalArgumentException();
  }
}
