package com.openchat.secureim.dom.smil;

import java.util.ArrayList;

import org.w3c.dom.smil.Time;
import org.w3c.dom.smil.TimeList;

public class TimeListImpl implements TimeList {
    private final ArrayList<Time> mTimes;

    
    TimeListImpl(ArrayList<Time> times) {
        mTimes = times;
    }

    

    public int getLength() {
        return mTimes.size();
    }

    public Time item(int index) {
        Time time = null;
        try {
            time = mTimes.get(index);
        } catch (IndexOutOfBoundsException e) {
        }
        return time;
    }

}
