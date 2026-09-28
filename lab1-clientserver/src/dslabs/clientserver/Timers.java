package dslabs.clientserver;

import dslabs.framework.Command;
import dslabs.framework.Timer;
import dslabs.atmostonce.AMOCommand;
import lombok.Data;

@Data
final class ClientTimer implements Timer {
  static final int CLIENT_RETRY_MILLIS = 100;
  private final AMOCommand command;
  //private final int sequenceValue;
}
