package dslabs.clientserver;

import dslabs.framework.Command;
import dslabs.framework.Message;
import dslabs.framework.Result;
import dslabs.atmostonce.AMOCommand;
import dslabs.atmostonce.AMOResult;
import lombok.Data;

// client -> server
@Data
class Request implements Message {
  private final AMOCommand command;
  // private final int sequenceValue; // UUID / flowid
}

// server -> client
@Data
class Reply implements Message {
  private final AMOResult result;
  // private final int sequenceValue;
}
