package com.kidney.breznitsasuri2;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.ArrayList;

public class MyMediaPlayer {
    static MediaPlayer instance;

    public static MediaPlayer getInstance(){
        if(instance == null){
            instance = new MediaPlayer();
        }
        return instance;
    }

    public static int currentIndex = -1;

}
