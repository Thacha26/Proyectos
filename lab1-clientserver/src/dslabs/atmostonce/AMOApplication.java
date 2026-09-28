/*Finally, you need to modify your solution to Part 2 to guarantee exactly-once delivery. The simplest way to do this is to modify the client and server code directly. The test code for Lab 1 only exercises the system through the client interface, and so this will allow you to pass the tests. */

package dslabs.atmostonce;

import dslabs.framework.Application;
import dslabs.framework.Command;
import dslabs.framework.Result;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import java.util.Map;
import java.util.HashMap;
import dslabs.framework.Address;

@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public final class AMOApplication<T extends Application> implements Application {
  @Getter @NonNull private final T application;

  private final Map<Address, AMOResult> clientResults = new HashMap<>();

  @Override
  public AMOResult execute(Command command) {
    if (!(command instanceof AMOCommand)) {
      throw new IllegalArgumentException();
    }

    AMOCommand amoCommand = (AMOCommand) command;

    if (alreadyExecuted(amoCommand)) {
      return clientResults.get(amoCommand.clientAddress());
    }

    //si es un nuevo comando se va a KVStore
    Result result = application.execute(amoCommand.command());
        
    //empAQUETS el resultado
    AMOResult amoResult = new AMOResult(result, amoCommand.sequenceValue());
        
    //se sobreescribe el resultado, 
    clientResults.put(amoCommand.clientAddress(), amoResult);

    return amoResult;

    }

  public Result executeReadOnly(Command command) {
    if (!command.readOnly()) {
      throw new IllegalArgumentException();
    }

    if (command instanceof AMOCommand) {
      return execute(command);
    }

    return application.execute(command);
  }

  public boolean alreadyExecuted(AMOCommand amoCommand) {
    AMOResult lastResult = clientResults.get(amoCommand.clientAddress());
    return lastResult != null && lastResult.sequenceValue() >= amoCommand.sequenceValue();
  }
}
