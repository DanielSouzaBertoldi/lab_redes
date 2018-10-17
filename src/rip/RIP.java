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
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import com.google.gson.*;


/**
 *
 * @author Daniel
 */

class Client extends Thread {
   private final static int SERVER_PORT = 6879;
   private Socket clientSocket;
   private PrintWriter out;
   private Roteador node;
   
   Client(Roteador node) {
      this.node = node;
      System.out.println("Criando o nó   " +  node.getName() );
   }
   
   public void run() {
       try {
           clientSocket = new Socket("localhost", SERVER_PORT);
           //out envia mensagens.
           ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
           
           JSONObject json = new JSONObject();
           json.put("No_Origem", node.getName());
           json.put("Tabela", this.node.getTabela().toString());
           System.out.println("Enviando o JSON: "+json.toString());

           String whatsthejson = json.toString();
           
           //o método .println envia coisas pelo buffer de saída
           out.writeObject(node);
           out.flush();
           out.reset();
           
       } catch (Exception e) {
           System.out.println(e.toString());
       }
   }
}

class Server extends Thread {
   private int port;
   private final static int SERVER_PORT = 6879;
   private ObjectInputStream in;
   private ServerSocket serverSocket;
   private Socket clientSocket;
   private String threadName;
   
   Server( String name) {
      threadName = name;
      System.out.println("Criando... " +  threadName );
   }
   
   @Override
   public void run() {
       try{
           serverSocket = new ServerSocket(SERVER_PORT);
           clientSocket = serverSocket.accept();
                      
           //in recebe mensagens
           in = new ObjectInputStream(clientSocket.getInputStream());  
           Roteador inputLine;
           while((inputLine = (Roteador) in.readObject()) != null) {
               if(".".equals(inputLine)){
                   System.out.println("Desligando servidor...");
                   break;
               }
               
               System.out.println("chegou aquui? \n"+ inputLine.getName());
               System.out.println("chegou aquui? \n"+ inputLine.getVizinhos().toString());
           }
       } catch (Exception e) {
           System.out.println(e.toString());
       }
   }
}

public class RIP {
    static private ArrayList<Integer> vizinhos; 
    static private Roteador t;
    private static HashMap <String, Entry<String, String>> tabela = new HashMap<>();
    
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
        
        Server s1 = new Server(Integer.toString(i));
        s1.start();

        Client c1 = new Client(t);
        c1.start();
    }
    
    static public void init(int i) {
        switch (i){
           case 0:
               vizinhos = new ArrayList<>(Arrays.asList(1, 2, 3));
               tabela.put("0", new SimpleEntry("0", null));
               tabela.put("1", new SimpleEntry("1", null));
               tabela.put("2", new SimpleEntry("3", null));
               tabela.put("3", new SimpleEntry("7", null));
               t = new Roteador("0", tabela, vizinhos);
               break;
           case 1:
               vizinhos = new ArrayList<>(Arrays.asList(0, 2));
               tabela.put("0", new SimpleEntry("1", null));
               tabela.put("1", new SimpleEntry("0", null));
               tabela.put("2", new SimpleEntry("1", null));
               tabela.put("3", new SimpleEntry("999", ""));
               t = new Roteador("1", tabela, vizinhos);

               break;
           case 2:
               vizinhos = new ArrayList<>(Arrays.asList(0, 1, 3));
               tabela.put("0", new SimpleEntry("3", null));
               tabela.put("1", new SimpleEntry("1", null));
               tabela.put("2", new SimpleEntry("0", null));
               tabela.put("3", new SimpleEntry("2", null));
               t = new Roteador("2", tabela, vizinhos);
               break;
           case 3:
               vizinhos = new ArrayList<>(Arrays.asList(0, 2));
               tabela.put("0", new SimpleEntry("7", null));
               tabela.put("1", new SimpleEntry("999", ""));
               tabela.put("2", new SimpleEntry("2", null));
               tabela.put("3", new SimpleEntry("0", null));
               t = new Roteador("3", tabela, vizinhos);
               break;
       }
    }
}