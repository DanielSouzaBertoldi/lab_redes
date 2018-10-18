/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rip;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Daniel
 */
public class Roteador implements Serializable {
    private ArrayList<String> vizinhos;
    private String whoIam;
    private HashMap<String, Map.Entry<Integer, String>> tabela;
    
    public Roteador(String name, HashMap<String, Entry<Integer, String>> tabela, ArrayList<String> vizinhos){
        this.whoIam = name;
        this.tabela = tabela;
        this.vizinhos = vizinhos;
    }
        
    public HashMap getTabela(){
        return this.tabela;
    }
    
    
    public String getName(){
        return this.whoIam;
    }
    
    public ArrayList<String> getVizinhos(){
        return this.vizinhos;
    }
    
    public boolean update(Roteador destinatario, Roteador remetente) {
        boolean update = false;
        Entry<Integer, String> tabelaAux = (Entry<Integer, String>) destinatario.getTabela().get(remetente.getName());
        int custo_destinatario_para_remetente = tabelaAux.getKey();
        HashMap<String, Entry<Integer, String>> destinatarioModificado = new HashMap<>();
        
        /*for(int i = 0; i < remetente.getTabela().size(); i++){
            if(remetente.getTabela().get(i))
        }*/
        Entry<Integer, String> dModificado = null;
        for(int i = 0; i < remetente.getTabela().size(); i++) {
            Entry<Integer, String> rEntry = (Entry<Integer, String>) remetente.getTabela().get(String.valueOf(i));
            
            if(rEntry.getKey() != 999) 
                dModificado = new SimpleEntry(rEntry.getKey()+custo_destinatario_para_remetente, remetente.getName());
            else
                dModificado = new SimpleEntry(rEntry.getKey(), remetente.getName());
            
            destinatarioModificado.put(String.valueOf(i), dModificado);
            
        }
        System.out.println("\nOi, eu sou o roteador: "+destinatario.getName()+"\nA minha tabela modificada com a tabela do roteador "+remetente.getName()+" é:"+destinatarioModificado.toString());
        
        for(int j = 0; j < remetente.getTabela().size(); j ++){
            String posicao = String.valueOf(j);
            tabelaAux = (Entry<Integer, String>) destinatario.getTabela().get(posicao);
            int custoAtual = tabelaAux.getKey();
                    
            if(destinatarioModificado.get(posicao).getKey() < custoAtual){
                System.out.println("\n\ndestinatarioModificado.get(posicao).getKey(): "+destinatarioModificado.get(posicao).getKey());
                System.out.println("custoAtual: "+custoAtual+"\n");
                destinatario.getTabela().replace(posicao, destinatarioModificado.get(posicao));
                System.out.println("\nAtualizei a tabela na posicao: "+posicao+"\nTroquei o valor '"+custoAtual+"' por "+destinatarioModificado.get(posicao).getKey());
                update = true;
            }
        }
        
        return update;
    }
}
