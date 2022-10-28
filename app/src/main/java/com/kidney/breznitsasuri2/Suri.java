package com.kidney.breznitsasuri2;

import java.io.Serializable;

public class Suri implements Serializable {

    String name;
    int currentIndex, suraimage;
    String opisanie;

    public Suri(String name, int currentIndex, int suraimage, String opisanie) {
        this.name = name;
        this.currentIndex = currentIndex;
        this.suraimage = suraimage;
        this.opisanie = opisanie;
    }

    public String getName() {
        return name;
    }
    public String getOpisanie() {
        return opisanie;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setOpisanie(String opisanie) {this.opisanie = opisanie; }
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
