package rip;
import java.net.*;
import java.io.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Daniel
 */

class Client extends Thread {
   private String SERVER_PORT;
   private Socket clientSocket;
   private PrintWriter out;
   private final Roteador node;
   
   Client(Roteador node) {
      this.node = node;
      System.out.println("Criando o nó   " +  node.getName() );
   }
   
   @Override
   public void run() {
       
       for(String vizinho : node.getVizinhos()){
            this.SERVER_PORT = "666" + vizinho;
            System.out.println("Conectando-se na porta: " + SERVER_PORT);
            
            try {
                clientSocket = new Socket("localhost", Integer.parseInt(SERVER_PORT));
                //out envia mensagens.
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

                //o método .println envia coisas pelo buffer de saída
                out.writeObject(node);
                out.flush();
                out.reset();

            } catch (Exception e) {
                System.out.println(e.toString());
            }
       }
   }
}

class Server extends Thread {
   private int port;
   private String SERVER_PORT;
   private ObjectInputStream in;
   private ObjectOutputStream out;
   private ServerSocket serverSocket;
   private Socket clientSocket;
   private final Roteador node;
   
   Server(Roteador node) {
      this.node = node;
      this.SERVER_PORT = "666"+node.getName();
      System.out.println("Minha porta é: "+ SERVER_PORT);
   }
   
   @Override
   public void run() {
       try{
           serverSocket = new ServerSocket(Integer.parseInt(SERVER_PORT));
           clientSocket = serverSocket.accept();
           
           Roteador inputLine;
           while(true) {
               //in recebe mensagens
               in = new ObjectInputStream(clientSocket.getInputStream());  
               if((inputLine = (Roteador) in.readObject()) != null){
                   //Se foi atualizado, envia pra todos os vizinhos                   
                    System.out.println("\nOi, alguem abriu uma conexão comigo, roteador: "+this.node.getName()+"\nMinha tabela atual é: "+this.node.getTabela().toString());
                    if(this.node.update(this.node, inputLine)){
                        for(String vizinho : this.node.getVizinhos()){
                            this.SERVER_PORT = "666" + vizinho;
                            System.out.println("Enviando atualização de tabela na porta: " + SERVER_PORT);

                            try {
                                clientSocket = new Socket("localhost", Integer.parseInt(SERVER_PORT));
                                //out envia mensagens.
                                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

                                //o método .println envia coisas pelo buffer de saída
                                out.writeObject(this.node);
                                out.flush();
                                out.reset();

                            } catch (IOException | NumberFormatException e) {
                                System.out.println("Deu algum erro: "+e.toString());
                            }
                       }
                    } else {
                        System.out.println("Não foi atualizado esse LIXO");
                    }
               }
               clientSocket.close();
               clientSocket = serverSocket.accept();
           }
       } catch (Exception e) {
           System.out.println("Deu algum erro: "+e.toString());
       }
   }
}

public class RIP {
    static private ArrayList<String> vizinhos; 
    static private Roteador t;
    private static HashMap <String, Entry<Integer, String>> tabela = new HashMap<>();
    
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));        
        int i = 0;
        try {
            i = Integer.parseInt(br.readLine());
        } catch (IOException ex) {
            Logger.getLogger(RIP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        init(i);
        
        System.out.println("Vizinhos do nó "+i+": "+vizinhos.toString());
        System.out.println("Tabela do nó "+i+": "+t.getTabela().toString());
        
        Server s1 = new Server(t);
        s1.start();

        br = new BufferedReader(new InputStreamReader(System.in));
        try {
            br.readLine();
        } catch (IOException ex) {
            Logger.getLogger(RIP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Client c1 = new Client(t);
        c1.start();
    }
    
    static public void init(int i) {
        switch (i){
           case 0:
               vizinhos = new ArrayList<>(Arrays.asList("1", "2", "3"));
               tabela.put("0", new SimpleEntry(0, null));
               tabela.put("1", new SimpleEntry(1, null));
               tabela.put("2", new SimpleEntry(3, null));
               tabela.put("3", new SimpleEntry(7, null));
               t = new Roteador("0", tabela, vizinhos);
               break;
           case 1:
               vizinhos = new ArrayList<>(Arrays.asList("0", "2"));
               tabela.put("0", new SimpleEntry(1, null));
               tabela.put("1", new SimpleEntry(0, null));
               tabela.put("2", new SimpleEntry(1, null));
               tabela.put("3", new SimpleEntry(999, ""));
               t = new Roteador("1", tabela, vizinhos);

               break;
           case 2:
               vizinhos = new ArrayList<>(Arrays.asList("0", "1", "3"));
               tabela.put("0", new SimpleEntry(3, null));
               tabela.put("1", new SimpleEntry(1, null));
               tabela.put("2", new SimpleEntry(0, null));
               tabela.put("3", new SimpleEntry(2, null));
               t = new Roteador("2", tabela, vizinhos);
               break;
           case 3:
               vizinhos = new ArrayList<>(Arrays.asList("0", "2"));
               tabela.put("0", new SimpleEntry(7, null));
               tabela.put("1", new SimpleEntry(999, "-1"));
               tabela.put("2", new SimpleEntry(2, null));
               tabela.put("3", new SimpleEntry(0, null));
               t = new Roteador("3", tabela, vizinhos);
               break;
       }
    }
}