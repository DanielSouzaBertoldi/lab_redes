/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rip;

import java.io.Serializable;
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
    private HashMap<String, Map.Entry<String, String>> tabela;
    
    public Roteador(String name, HashMap<String, Entry<String, String>> tabela, ArrayList<String> vizinhos){
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
}
