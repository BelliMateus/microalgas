package com.mateus.passartelas.collect;

import java.util.ArrayList;

public class CollectData {

    public static ArrayList<Collect> collect_list = new ArrayList<>();

    public static Collect getLastCollect(){
        if(!collect_list.isEmpty()) {
            return collect_list.get(collect_list.size() - 1);
        }else{
            return null;
        }
    }

}
