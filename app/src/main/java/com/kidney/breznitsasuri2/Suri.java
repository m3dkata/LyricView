package com.kidney.breznitsasuri2;

import java.io.Serializable;

public class Suri implements Serializable {

    String name;
    int currentIndex, suraimage;

    public Suri(String name, int currentIndex, int suraimage) {
        this.name = name;
        this.currentIndex = currentIndex;
        this.suraimage = suraimage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }


    public int getSuraimage() {
        return suraimage;
    }

    public void setSuraimage(int suraimage) {
        this.suraimage = suraimage;
    }

    public String toString(){
        return name;
    }
}
