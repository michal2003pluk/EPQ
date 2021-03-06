package com.thundercandy.epq.data;

import android.content.Context;

import com.thundercandy.epq.database.DbUtils;

import java.util.ArrayList;

public class ExpandableCategory extends Category {

    private boolean isExpanded = false;

    public ExpandableCategory(int id, String name, ArrayList<Card> cards) {
        super(id, name, cards);
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public void flipExpanded() {
        setExpanded(!isExpanded());
    }

}
