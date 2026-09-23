package dslabs.clientserver;

import dslabs.framework.Message;
import lombok.Data;

// client -> server
@Data
class Request implements Message {
  private final Command command;
}

// server -> client
@Data
class Reply implements Message {
  private final Result result;
}
