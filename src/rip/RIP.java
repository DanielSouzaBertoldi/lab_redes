package rip;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;


/**
 *
 * @author Daniel
 */

class Client extends Thread {
   private final static int SERVER_PORT = 6879;
   private Socket clientSocket;
   private PrintWriter out;
   private BufferedReader in;
   private Thread t;
   private String threadName;
   
   Client( String name) {
      threadName = name;
      System.out.println("Criando... " +  threadName );
   }
   
   public void run() {
       try {
           clientSocket = new Socket("localhost", SERVER_PORT);
           //out envia mensagens.
           PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
           
           JSONObject json = new JSONObject();
           json.put("No_Origem", "1");
           json.put("Fila_origem", "ssss");
           System.out.println("Enviando o JSON: "+json.toString());

           String whatsthejson = json.toString();
           
           //o método .println envia coisas pelo buffer de saída
           out.println(whatsthejson);
           out.println(".");
           
       } catch (Exception e) {
           System.out.println(e.toString());
       }
   }
}

class Server extends Thread {
   private int port;
   private final static int SERVER_PORT = 6879;
   private BufferedReader in;
   private ServerSocket serverSocket;
   private Socket clientSocket;
   private String threadName;
   
   Server( String name) {
      threadName = name;
      System.out.println("Criando... " +  threadName );
   }
   
   public void run() {
       try{
           serverSocket = new ServerSocket(SERVER_PORT);
           clientSocket = serverSocket.accept();
           
           ArrayList<String> oi = new ArrayList<>();
           
           //in recebe mensagens
           in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                      
           String inputLine;
           while((inputLine = in.readLine()) != null) {
               if(".".equals(inputLine)){
                   System.out.println("Desligando servidor...");
                   break;
               }
               
               JSONParser parser = new JSONParser();
               JSONObject json = (JSONObject) parser.parse(inputLine); 
               System.out.println("[Servidor] Recebido a mensagem: \n"+json.toString());
           }
       } catch (Exception e) {
           System.out.println(e.toString());
       }
   }
}

public class RIP {
   
    public static void main(String[] args) {
        int i = Integer.parseInt(args[0]);
        
        init(i);
        
        Server s1 = new Server("Server");
        s1.start();

        Client c1 = new Client("Client");
        c1.start();
    }
    
    static public void init(int i) {
       ArrayList<Integer> vizinhos; 
       HashMap<String, Integer> tabela = new HashMap<String, Integer>();
       switch (i){
           case 0:
               vizinhos = new ArrayList<>(Arrays.asList(1, 2, 3));
               tabela.put("0", 0);
               tabela.put("1", 1);
               tabela.put("2", 3);
               tabela.put("3", 7);
               break;
           case 1:
               vizinhos = new ArrayList<>(Arrays.asList(0, 2));
               break;
           case 2:
               vizinhos = new ArrayList<>(Arrays.asList(0, 1, 3));
               break;
           case 3:
               vizinhos = new ArrayList<>(Arrays.asList(0, 2));
               break;
       }
    }
}