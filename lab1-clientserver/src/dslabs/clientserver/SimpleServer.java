package dslabs.clientserver;

import dslabs.framework.Address;
import dslabs.framework.Application;
import dslabs.framework.Node;
import dslabs.framework.Result;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import dslabs.atmostonce.AMOResult;
import dslabs.atmostonce.AMOApplication;

/**
 * Simple server that receives requests and returns responses.
 *
 * <p>See the documentation of {@link Node} for important implementation notes.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
class SimpleServer extends Node {
  // run to completion without blocking, sleeping, or starting other threads
  private final Application app;

  /* -----------------------------------------------------------------------------------------------
   *  Construction and Initialization
   * ---------------------------------------------------------------------------------------------*/
  public SimpleServer(Address address, Application app) { //at least once, sin que dependa de application, se puede usar cualquier aplicacion que implemente la interfaz Application
    super(address);
    this.app = new AMOApplication<>(app);
  }

  @Override
  public void init() {
    // No initialization necessary
  }


//garantizar que la respuesta sea de la petición que se hizo
  /* -----------------------------------------------------------------------------------------------
   *  Message Handlers
   * ---------------------------------------------------------------------------------------------*/
  private void handleRequest(Request m, Address sender) { // llega el request, y el resultado se envía al sender "cliente"
    AMOResult resultado = (AMOResult) app.execute(m.command());
    /*
    reply = new Reply(resultado, m.sequenceValue());
    send(reply, sender);
    
    this.send(new Reply(resultado, m.sequenceValue()), sender);
    */
    this.send(new Reply(resultado), sender);    
  }
}