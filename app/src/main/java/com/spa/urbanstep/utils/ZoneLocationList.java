package com.spa.urbanstep.utils;

import com.spa.urbanstep.model.response.ListItem;


import java.util.ArrayList;

public class ZoneLocationList {

    private ArrayList<ListItem> list;
    private static ZoneLocationList _instance;


    private ZoneLocationList() {
        list = new ArrayList<>();
        list.add(new ListItem("Shahadra South", 28.62803, 77.30225));
        list.add(new ListItem("Shahadra North", 28.69059, 77.2919));
        list.add(new ListItem("South", 28.56961, 77.23682));
        list.add(new ListItem("Central", 28.56618, 77.24523));
        list.add(new ListItem("Civil Line", 28.68337, 77.22036));
        list.add(new ListItem("Narela", 28.527393, 77.2656));
        list.add(new ListItem("Rohini", 25.66939, 94.10637));
        list.add(new ListItem("Nazafgarh", 28.61582, 76.97419));
        list.add(new ListItem("West", -43.574028, 172.62233));
        list.add(new ListItem("Sadar Paharganj", 28.65246, 77.21054));
        list.add(new ListItem("Karol Bagh", 28.64941, 77.19165));
    }

    public static ZoneLocationList getInstance() {
        if (_instance == null) {
            _instance = new ZoneLocationList();
        }
        return _instance;
    }

    public ArrayList<ListItem> mergeList(ArrayList<ListItem> zoneList) {
        ArrayList<ListItem> updatedList = new ArrayList<>();
        for (ListItem item : zoneList) {
            ListItem newItem = new ListItem();
            newItem.setId(item.getId());
            newItem.setName(item.getName());
            for (ListItem item1 : list) {
                if (item.getName().equalsIgnoreCase(item1.getName())) {
                    newItem.setLat(item1.getLat());
                    newItem.setLong(item1.getLong());
                    updatedList.add(newItem);
                    break;
                }
            }
        }
        return updatedList;
    }
}


