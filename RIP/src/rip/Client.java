package rip;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;


/**
 *
 * @authores
 * Carla Simões Gama        613843
 * Daniel Souza Bertoldi    620548
 */

class Client extends Thread {
   private String porta;
   private Socket clientSocket;
   private final Roteador node;
   private ObjectOutputStream out;
   
   
   Client(Roteador node) {
      this.node = node;
      System.out.println("Criando o nó [" + node.getName() +"]");
   }
   
   @Override
   public void run() {
       
       for(String vizinho : node.getVizinhos()){
            this.porta = "666" + vizinho;
            System.out.println("\nConectando-se na porta: " + porta);
            
            try {
                clientSocket = new Socket("localhost", Integer.parseInt(porta));
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                out.writeObject(node);
                out.flush();
                out.reset();

            } catch (IOException | NumberFormatException e) {
                System.out.println("["+Client.class.getName()+"] Erro: "+e.getMessage());
            }
       }
   }
}
