package rip;

import java.io.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


/**
 *
 * @authores
 * Carla Simões Gama        613843
 * Daniel Souza Bertoldi    620548
 */

public class RIP {
    static private ArrayList<String> vizinhos; 
    static private Roteador no;
    private static HashMap <String, Entry<Integer, String>> tabelaRoteador = new HashMap<>();
    
    public static void main(String[] args) {
        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(System.in));        
        int i = 0;
        try {
            i = Integer.parseInt(bufferReader.readLine());
        } catch (IOException ex) {
            System.out.println("["+RIP.class.getName() + "] Erro: " + ex.getMessage());
        }
        
        init(i);
        
        System.out.println("Vizinhos do nó ["+i+"]: "+vizinhos.toString());

        Iterator it = no.getTabela().entrySet().iterator();
        System.out.println("\n   Nó ---- Custo ----  Next");
        
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            SimpleEntry ee = (SimpleEntry) pair.getValue();
            System.out.println("   "+pair.getKey() + "   |     " + ee.getKey() + "   |   "+ee.getValue());
        }
        
        Server server = new Server(no);
        server.start();

        bufferReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            bufferReader.readLine();
        } catch (IOException ex) {
            System.out.println("["+RIP.class.getName()+"] Erro: "+ex.getMessage());
        }
        
        Client c1 = new Client(no);
        c1.start();
    }
    
    static public void init(int i) {
        switch (i){
           case 0:
               vizinhos = new ArrayList<>(Arrays.asList("1", "2", "3"));
               tabelaRoteador.put("0", new SimpleEntry(0, null));
               tabelaRoteador.put("1", new SimpleEntry(1, null));
               tabelaRoteador.put("2", new SimpleEntry(3, null));
               tabelaRoteador.put("3", new SimpleEntry(7, null));
               no = new Roteador("0", tabelaRoteador, vizinhos);
               break;
           case 1:
               vizinhos = new ArrayList<>(Arrays.asList("0", "2"));
               tabelaRoteador.put("0", new SimpleEntry(1, null));
               tabelaRoteador.put("1", new SimpleEntry(0, null));
               tabelaRoteador.put("2", new SimpleEntry(1, null));
               tabelaRoteador.put("3", new SimpleEntry(999, "-1"));
               no = new Roteador("1", tabelaRoteador, vizinhos);

               break;
           case 2:
               vizinhos = new ArrayList<>(Arrays.asList("0", "1", "3"));
               tabelaRoteador.put("0", new SimpleEntry(3, null));
               tabelaRoteador.put("1", new SimpleEntry(1, null));
               tabelaRoteador.put("2", new SimpleEntry(0, null));
               tabelaRoteador.put("3", new SimpleEntry(2, null));
               no = new Roteador("2", tabelaRoteador, vizinhos);
               break;
           case 3:
               vizinhos = new ArrayList<>(Arrays.asList("0", "2"));
               tabelaRoteador.put("0", new SimpleEntry(7, null));
               tabelaRoteador.put("1", new SimpleEntry(999, "-1"));
               tabelaRoteador.put("2", new SimpleEntry(2, null));
               tabelaRoteador.put("3", new SimpleEntry(0, null));
               no = new Roteador("3", tabelaRoteador, vizinhos);
               break;
       }
    }
}