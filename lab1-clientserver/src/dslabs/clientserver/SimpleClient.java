package dslabs.clientserver;

import dslabs.framework.Address;
import dslabs.framework.Client;
import dslabs.framework.Command;
import dslabs.framework.Node;
import dslabs.framework.Result;
import lombok.EqualsAndHashCode;
import lombok.ToString;

//añadir un flowid y un uuid para cada request, y el server tiene que guardar el ultimo request que le llego de cada cliente, y si llega un request con el mismo flowid y uuid, se devuelve el mismo resultado que se devolvio la primera vez
//meterle un sequence number

//at least once delivery
/**
 * Simple client that sends requests to a single server and returns responses.
 *
 * <p>See the documentation of {@link Client} and {@link Node} for important implementation notes.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
class SimpleClient extends Node implements Client {
  private final Address serverAddress;

  // Your code here...

  /* -----------------------------------------------------------------------------------------------
   *  Construction and Initialization
   * ---------------------------------------------------------------------------------------------*/
  public SimpleClient(Address address, Address serverAddress) {
    super(address);
    this.serverAddress = serverAddress;
  }

  @Override
  public synchronized void init() {
    // No initialization necessary
  }

  @Override
  public synchronized void sendCommand(Command command) {
    // Your code here...
  }

  @Override
  public synchronized boolean hasResult() {
    // Your code here...
    return false;
  }

  @Override
  public synchronized Result getResult() throws InterruptedException {
    while(this.lastResult == null) {
      this.wait();
    }
    return this.lastResult;
  }

  /* -----------------------------------------------------------------------------------------------
   *  Message Handlers
   * ---------------------------------------------------------------------------------------------*/
  private synchronized void handleReply(Reply m, Address sender) { //no se puede asumir si el reply es para el primer request o para el segundo, por eso se tiene que guardar el ultimo request enviado 
    // Your code here...


  }

  /* -----------------------------------------------------------------------------------------------
   *  Timer Handlers
   * ---------------------------------------------------------------------------------------------*/
  private synchronized void onClientTimer(ClientTimer t) { //preguntar si el request actual ya tiene respuesta
  // se ejecuta una vez cada cierto tirmpo, hasta que el request actual tenga respuesta, si no tiene respuesta, se vuelve a enviar el request actual
    
  }
}
