package rip;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;


/**
 *
 * @authores
 * Carla Simões Gama        613843
 * Daniel Souza Bertoldi    620548
 */

class Server extends Thread {
   private String porta;
   private ObjectInputStream in;
   private ServerSocket serverSocket;
   private Socket clientSocket;
   private final Roteador node;
   private ObjectOutputStream out;
   
   Server(Roteador node) {
      this.node = node;
      this.porta = "666"+node.getName();
      System.out.println("\n\nMinha porta é: "+ porta);
   }
   
   @Override
   public void run() {
       try{
           serverSocket = new ServerSocket(Integer.parseInt(porta));
           clientSocket = serverSocket.accept();
           
           Roteador inputLine;
           while(true) {
               in = new ObjectInputStream(clientSocket.getInputStream());  
               if((inputLine = (Roteador) in.readObject()) != null){
                   /* Se foi atualizado, envia pra todos os vizinhos */
                    System.out.println("Roteador ["+this.node.getName()+"]\nTabela atual é: ");
                    Iterator it = this.node.getTabela().entrySet().iterator();
                    System.out.println("\n   Nó ---- Custo ----  Next");

                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        AbstractMap.SimpleEntry ee = (AbstractMap.SimpleEntry) pair.getValue();
                        System.out.println("   "+pair.getKey() + "   |     " + ee.getKey() + "   |   "+ee.getValue());
                    }
                    
                    if(this.node.update(this.node, inputLine)){
                        for(String vizinho : this.node.getVizinhos()){
                            this.porta = "666" + vizinho;
                            System.out.println("Enviando atualização para o vizinho ["+vizinho+"], na porta [" + porta + "]");

                            try {
                                clientSocket = new Socket("localhost", Integer.parseInt(porta));
                                out = new ObjectOutputStream(clientSocket.getOutputStream());

                                out.writeObject(this.node);
                                out.flush();
                                out.reset();

                            } catch (IOException | NumberFormatException e) {
                                System.out.println("["+Server.class.getName()+"] Erro: "+e.getMessage());
                            }
                       }
                    } else {
                        System.out.println("|---------- NÃO FOI ATUALIZADA -----------|\n\n");
                    }
               }
               clientSocket.close();
               clientSocket = serverSocket.accept();
           }
       } catch (IOException | ClassNotFoundException | NumberFormatException e) {
           System.out.println("["+Server.class.getName()+"] Erro: "+e.getMessage());
       }
   }
}
