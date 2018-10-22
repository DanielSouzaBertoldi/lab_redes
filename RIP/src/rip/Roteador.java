
package rip;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
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

public class Roteador implements Serializable {
    private ArrayList<String> vizinhos;
    private String nome;
    private HashMap<String, Map.Entry<Integer, String>> tabelaRoteador;
    
    public Roteador(String name, HashMap<String, Entry<Integer, String>> tabela, ArrayList<String> vizinhos){
        this.nome = name;
        this.tabelaRoteador = tabela;
        this.vizinhos = vizinhos;
    }
        
    public HashMap getTabela(){
        return this.tabelaRoteador;
    }
    
    
    public String getName(){
        return this.nome;
    }
    
    public ArrayList<String> getVizinhos(){
        return this.vizinhos;
    }
    
    public boolean update(Roteador atual, Roteador remetente) {
        boolean update = false;
        Entry<Integer, String> tabelaRemetente = (Entry<Integer, String>) atual.getTabela().get(remetente.getName());
        int custo_destinatario_para_remetente = tabelaRemetente.getKey();
        HashMap<String, Entry<Integer, String>> atualModificado = new HashMap<>();
        
        Entry<Integer, String> novoAtual = null;
        
        for(int i = 0; i < remetente.getTabela().size(); i++) {
            Entry<Integer, String> rEntry = (Entry<Integer, String>) remetente.getTabela().get(String.valueOf(i));
            String posicao = String.valueOf(i);
            tabelaRemetente = (Entry<Integer, String>) atual.getTabela().get(posicao);
            int custoAtual = tabelaRemetente.getKey();
            
            if(rEntry.getKey() != 999) 
                novoAtual = new SimpleEntry(rEntry.getKey()+custo_destinatario_para_remetente, remetente.getName());
            else
                novoAtual = new SimpleEntry(rEntry.getKey(), remetente.getName());
            
            atualModificado.put(posicao, novoAtual);
            
            if(atualModificado.get(posicao).getKey() < custoAtual){
                atual.getTabela().replace(posicao, atualModificado.get(posicao));
                System.out.println("\nAtualizei a tabela na posicao: "+posicao+"\nTroquei o valor ["+custoAtual+"] por "+atualModificado.get(posicao).getKey());
                update = true;
            }
            
        }
        System.out.println("\nMinha tabela modificada com a tabela do roteador ["+remetente.getName()+"] é: ");
        
        Iterator it = atual.getTabela().entrySet().iterator();
        System.out.println("   Nó ---- Custo ----  Next");

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            AbstractMap.SimpleEntry ee = (AbstractMap.SimpleEntry) pair.getValue();
            System.out.println("   "+pair.getKey() + "   |     " + ee.getKey() + "   |   "+ee.getValue());
        }
        
        return update;
    }
}
