package dslabs.atmostonce;

import dslabs.framework.Command;
import lombok.Data;
import dslabs.framework.Address;

@Data
public final class AMOCommand implements Command {
  /* pensaar quien hizo la aplicación, address, y command, result en result poner el atributo de resuly y command aplication, en ese se debe de poner lo de application app, las peticiones que ya se prpcesaron, para eso se usa un map, con la address y aorresult, lastresults
  siempre que se procese algo, se tiene que rvisar quien manda la peticion y el sequence number, y si ya se procesó, problema de memorización
  */
  private final Command command;
  private final Address clientAddress;
  private final int sequenceValue;
}
