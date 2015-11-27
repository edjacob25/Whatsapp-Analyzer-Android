package com.example.jacob.myapplication.DeepAnalysis;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jacob on 25/11/2015.
 */
public class WordAnalyzer {
    HashMap<String, Word> definedWords = new HashMap<>();

    public WordAnalyzer(){
        //new Word       (name,                              agre,fear,happ,love,sadn,emoji,swear)
        definedWords.put("hola",new Word("hola",             0.0f,0.0f,0.2f,0.1f,0.0f,false,false));
        definedWords.put("adios",new Word("adios",           0.1f,0.0f,0.0f,0.0f,0.5f,false,false));
        definedWords.put("amor",new Word("amor",             0.0f,0.2f,0.5f,1.0f,0.0f,false,false));
        definedWords.put("quiero",new Word("quiero",         0.2f,0.0f,0.0f,0.5f,0.2f,false,false));
        definedWords.put("puto",new Word("puto",             1.0f,0.0f,0.0f,0.0f,0.0f,false,true));
        definedWords.put("pinche",new Word("pinche",         1.0f,0.0f,0.0f,0.0f,0.0f,false,true));
        definedWords.put("wey",new Word("wey",               0.2f,0.0f,0.0f,0.0f,0.0f,false,true));
        definedWords.put("pendejo",new Word("pendejo",       1.0f,0.0f,0.0f,0.0f,0.0f,false,true));
        definedWords.put("estupido",new Word("estupido",     0.7f,0.0f,0.0f,0.0f,0.0f,false,true));
        definedWords.put("verga",new Word("verga",           0.3f,0.0f,0.0f,0.0f,0.5f,false,true));
        definedWords.put("mañana",new Word("mañana",         0.0f,0.0f,0.0f,0.0f,0.0f,false,false));
        definedWords.put("salir",new Word("salir",           0.0f,0.0f,0.0f,0.0f,0.0f,false,false));
        definedWords.put("noche",new Word("anoche",          0.0f,0.0f,0.0f,0.0f,0.0f,false,false));
       /* definedWords.put("",new Word("",0.0f,0.0f,0.0f,0.0f,0.0f,false,false));
        definedWords.put("",new Word("",0.0f,0.0f,0.0f,0.0f,0.0f,false,false));
        definedWords.put("",new Word("",0.0f,0.0f,0.0f,0.0f,0.0f,false,false));
        definedWords.put("",new Word("",0.0f,0.0f,0.0f,0.0f,0.0f,false,false));
        definedWords.put("",new Word("",0.0f,0.0f,0.0f,0.0f,0.0f,false,false));
        definedWords.put("",new Word("",0.0f,0.0f,0.0f,0.0f,0.0f,false,false));*/

    }

    public Person analyze(String name, ArrayList<String> messages) {
        Person p = new Person(name);
        int step = 0;
        if (messages.size() < 200){
            step = 1;
        }
        else{
            step = (int) Math.floor((messages.size() / 200));

        }

        for (int i = 0; i < messages.size(); i = i + step ) {
            String[] words = messages.get(i).split(" ");
            for (String word : words) {
                word = word.toLowerCase();
                if (definedWords.containsKey(word)){
                    p.addNewWord(definedWords.get(word));
                }
            }
        }
        return p;
    }

}
