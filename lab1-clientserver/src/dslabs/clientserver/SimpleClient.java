package dslabs.clientserver;

import dslabs.framework.Address;
import dslabs.framework.Client;
import dslabs.framework.Command;
import dslabs.framework.Node;
import dslabs.framework.Result;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import dslabs.atmostonce.AMOCommand;
import dslabs.atmostonce.AMOResult;

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
  private Command lastCommand;
  private Result lastResult;
  private int sequenceValue = 0; // flowid / uuid (sequence number)


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
    this.lastCommand = command;
    this.lastResult = null; 
    this.sequenceValue++;  
    
    //send(new Request(this.lastCommand, this.sequenceValue), this.serverAddress);
    //set(new ClientTimer(this.sequenceValue), ClientTimer.CLIENT_RETRY_MILLIS);

    // Empaquetamos el comando original con la identidad del cliente y la secuencia
    AMOCommand amoCommand = new AMOCommand(this.lastCommand, this.address(), this.sequenceValue);
    
    // Enviamos el comando empaquetado en el Request
    send(new Request(amoCommand), this.serverAddress);
    
    // Configuramos el timer con el comando empaquetado
    set(new ClientTimer(amoCommand), ClientTimer.CLIENT_RETRY_MILLIS);
  }

  @Override
  public synchronized boolean hasResult() {
    return this.lastResult != null;
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
    if (m.result().sequenceValue() == this.sequenceValue) {
      this.lastResult = m.result().result();
      this.notify(); // Despierta al hilo que está pausado en getResult()
    }
  }

  /* -----------------------------------------------------------------------------------------------
   *  Timer Handlers
   * ---------------------------------------------------------------------------------------------*/
  private synchronized void onClientTimer(ClientTimer t) { //preguntar si el request actual ya tiene respuesta
  if (t.command().sequenceValue() == this.sequenceValue && this.lastResult == null) {
      send(new Request(t.command()), this.serverAddress); 
      // send(new Request(this.lastCommand, this.sequenceValue), this.serverAddress);
      set(t, ClientTimer.CLIENT_RETRY_MILLIS);
    }
  }
}
