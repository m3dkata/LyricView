package com.kidney.breznitsasuri2;

import static com.kidney.breznitsasuri2.MyMediaPlayer.currentIndex;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.lauzy.freedom.library.Lrc;
import com.lauzy.freedom.library.LrcHelper;
import com.lauzy.freedom.library.LrcView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayerActivity extends AppCompatActivity {

    ImageView pausePlay, prev, next, repeat;
    LrcView suraText, suraTexttr, suraTextpr;
    SeekBar mSeekBarTime, mSeekBarVol;
    ArrayList<Suri> suriList;
    Suri currentSong;
    int nowPlay;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    private Context context;
    static boolean repeatBoolean = false;
    public AudioManager mAudioManager;
    private RecyclerView recyclerView;
    private boolean SongUrls;
    View swipeView;
    private String suraAudio;
    private int test;
    private String testAudio;
    private Uri audioSura;
    private MediaPlayer mMediaPlayer;
    private Uri testUri;
    private MediaPlayer loadAudio;
    private ArrayList<String> suriAudio;
    private int resId;
    private Spinner spinner;


    //ArrayList<Integer> songs = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);


        //mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        pausePlay = findViewById(R.id.play);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);

        suraText = findViewById(R.id.suraText);
        suraTexttr = findViewById(R.id.suraTexttr);
        suraTextpr = findViewById(R.id.suraTextpr);
        mSeekBarTime = findViewById(R.id.seekBarTime);
        mSeekBarVol = findViewById(R.id.seekBarVol);
        repeat = findViewById(R.id.repeat);

        Intent intent = getIntent();
        suriList = (ArrayList<Suri>) getIntent().getSerializableExtra("LIST");


        //suraAudio = getIntent().getParcelableExtra("suriAudio");


        //suraAudio = getIntent().getExtras().getString("suriAudio");
        spinner = findViewById(R.id.spinner);

        ArrayAdapter<Suri> adapter = new ArrayAdapter<Suri>(this, R.layout.spinner_item, suriList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setSelection(currentIndex);

        currentSong = suriList.get(MyMediaPlayer.currentIndex);

        setTitle(suriList.get(currentIndex).getName());
        pausePlay.setOnClickListener(v -> pausePlay());
        next.setOnClickListener(v -> playNextSong());
        prev.setOnClickListener(v -> playPreviousSong());
        repeat.setOnClickListener(v -> repeat());
        songNames();
        centerTitle();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MyMediaPlayer.getInstance().reset();
                MyMediaPlayer.currentIndex = spinner.getSelectedItemPosition();
                setResourcesWithMusic();
                songNames();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mSeekBarTime.setProgress(mediaPlayer.getCurrentPosition());
                    suraText.updateTime(mediaPlayer.getCurrentPosition());
                    suraTexttr.updateTime(mediaPlayer.getCurrentPosition());
                    suraTextpr.updateTime(mediaPlayer.getCurrentPosition());
                    pausePlay.setImageResource(R.drawable.play_btn);
                }
                if (mediaPlayer.isPlaying()) {
                    pausePlay.setImageResource(R.drawable.pause_btn);
                    suraText.updateTime(mediaPlayer.getCurrentPosition());
                    suraTexttr.updateTime(mediaPlayer.getCurrentPosition());
                    suraTextpr.updateTime(mediaPlayer.getCurrentPosition());

                } else {
                    pausePlay.setImageResource(R.drawable.play_btn);
                    suraText.updateTime(mediaPlayer.getCurrentPosition());
                    suraTexttr.updateTime(mediaPlayer.getCurrentPosition());
                    suraTextpr.updateTime(mediaPlayer.getCurrentPosition());

                }
                new Handler().postDelayed(this, 100);
            }
        });


        mSeekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    suraText.updateTime(mediaPlayer.getCurrentPosition());
                    suraTexttr.updateTime(mediaPlayer.getCurrentPosition());
                    suraTextpr.updateTime(mediaPlayer.getCurrentPosition());
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(!repeatBoolean){
                    if(MyMediaPlayer.currentIndex== suriList.size()-1)
                        return;
                    MyMediaPlayer.currentIndex +=1;
                    mediaPlayer.reset();
                    repeat.setImageResource(R.drawable.repeat_btn_off);
                    mediaPlayer.setLooping(false);
                    spinner.setSelection(currentIndex);
                    setResourcesWithMusic();
                    playSound();
                    songNames();
                }
            }
        });

        mSeekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    suraText.updateTime(mediaPlayer.getCurrentPosition());
                    suraTexttr.updateTime(mediaPlayer.getCurrentPosition());
                    suraTextpr.updateTime(mediaPlayer.getCurrentPosition());
                    mSeekBarTime.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekBarVol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                suraText.setLrcTextSize(Float.valueOf(i));
                suraTexttr.setLrcTextSize(Float.valueOf(i));
                suraTextpr.setLrcTextSize(Float.valueOf(i));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });

        suraText.setOnPlayIndicatorLineListener(new LrcView.OnPlayIndicatorLineListener() {
            @Override
            public void onPlay(long time, String content) {
                mediaPlayer.seekTo((int) time);
            }
        });
        suraTexttr.setOnPlayIndicatorLineListener(new LrcView.OnPlayIndicatorLineListener() {
            @Override
            public void onPlay(long time, String content) {
                mediaPlayer.seekTo((int) time);
            }
        });
        suraTextpr.setOnPlayIndicatorLineListener(new LrcView.OnPlayIndicatorLineListener() {
            @Override
            public void onPlay(long time, String content) {
                mediaPlayer.seekTo((int) time);
            }
        });


//        suraText.setOnTouchListener(new OnSwipeTouchListener(context) {
//            @Override
//            public void onSwipeLeft() {
//                // Whatever
//                playNextSong();

//            }
//            public void onSwipeRight() {
                // Whatever
//                playPreviousSong();

//            }
//        });


    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    void setResourcesWithMusic() {

        currentSong = suriList.get(MyMediaPlayer.currentIndex);

        setTitle(suriList.get(currentIndex).getOpisanie());
        pausePlay.setOnClickListener(v -> pausePlay());
        next.setOnClickListener(v -> playNextSong());
        prev.setOnClickListener(v -> playPreviousSong());
        repeat.setOnClickListener(v -> repeat());
        playSound();

    }


    private void playNextSong() {
        if(MyMediaPlayer.currentIndex== suriList.size()-1)
            return;
        MyMediaPlayer.currentIndex +=1;
        mediaPlayer.reset();
        repeat.setImageResource(R.drawable.repeat_btn_off);
        mediaPlayer.setLooping(false);
        spinner.setSelection(currentIndex);
        setResourcesWithMusic();
        playSound();
        songNames();

    }


    private void playPreviousSong() {
        if(MyMediaPlayer.currentIndex== 0)
            return;
        MyMediaPlayer.currentIndex -=1;
        mediaPlayer.reset();
        repeat.setImageResource(R.drawable.repeat_btn_off);
        mediaPlayer.setLooping(false);
        spinner.setSelection(currentIndex);
        setResourcesWithMusic();
        playSound();
        songNames();

    }

    private void pausePlay() {

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();

        } else {
            mediaPlayer.start();

        }

    }

    private void repeat() {
        if (repeatBoolean){
            repeatBoolean = false;
            repeat.setImageResource(R.drawable.repeat_btn_off);
            mediaPlayer.setLooping(false);
        }
        else{
            repeatBoolean = true;
            repeat.setImageResource(R.drawable.repeat_btn_on);
            mediaPlayer.setLooping(true);

        }
    }


    private void songNames() {
        if (currentSong.currentIndex == 0) {
            List<Lrc> lrcs = LrcHelper.parseLrcFromAssets(this, "002001.lrc");suraText.setLrcData(lrcs);
            List<Lrc> lrcsTR = LrcHelper.parseLrcFromAssets(this, "002001tr.lrc");suraTexttr.setLrcData(lrcsTR);
            List<Lrc> lrcsPR = LrcHelper.parseLrcFromAssets(this, "002001pr.lrc");suraTextpr.setLrcData(lrcsPR);
        }

        if (currentSong.currentIndex == 1) {
            List<Lrc> lrcs = LrcHelper.parseLrcFromAssets(this, "002001.lrc");suraText.setLrcData(lrcs);
            List<Lrc> lrcsTR = LrcHelper.parseLrcFromAssets(this, "002001tr.lrc");suraTexttr.setLrcData(lrcsTR);
            List<Lrc> lrcsPR = LrcHelper.parseLrcFromAssets(this, "002001pr.lrc");suraTextpr.setLrcData(lrcsPR);
        }

        if (currentSong.currentIndex == 2) {
            List<Lrc> lrcs = LrcHelper.parseLrcFromAssets(this, "002001.lrc");suraText.setLrcData(lrcs);
            List<Lrc> lrcsTR = LrcHelper.parseLrcFromAssets(this, "002001tr.lrc");suraTexttr.setLrcData(lrcsTR);
            List<Lrc> lrcsPR = LrcHelper.parseLrcFromAssets(this, "002001pr.lrc");suraTextpr.setLrcData(lrcsPR);
        }

        if (currentSong.currentIndex == 3) {
            List<Lrc> lrcs = LrcHelper.parseLrcFromAssets(this, "002001.lrc");suraText.setLrcData(lrcs);
            List<Lrc> lrcsTR = LrcHelper.parseLrcFromAssets(this, "002001tr.lrc");suraTexttr.setLrcData(lrcsTR);
            List<Lrc> lrcsPR = LrcHelper.parseLrcFromAssets(this, "002001pr.lrc");suraTextpr.setLrcData(lrcsPR);
        }

        if (currentSong.currentIndex == 4) {
            List<Lrc> lrcs = LrcHelper.parseLrcFromAssets(this, "002001.lrc");suraText.setLrcData(lrcs);
            List<Lrc> lrcsTR = LrcHelper.parseLrcFromAssets(this, "002001tr.lrc");suraTexttr.setLrcData(lrcsTR);
            List<Lrc> lrcsPR = LrcHelper.parseLrcFromAssets(this, "002001pr.lrc");suraTextpr.setLrcData(lrcsPR);
        }

        if (currentSong.currentIndex == 5) {
            List<Lrc> lrcs = LrcHelper.parseLrcFromAssets(this, "002001.lrc");suraText.setLrcData(lrcs);
            List<Lrc> lrcsTR = LrcHelper.parseLrcFromAssets(this, "002001tr.lrc");suraTexttr.setLrcData(lrcsTR);
            List<Lrc> lrcsPR = LrcHelper.parseLrcFromAssets(this, "002001pr.lrc");suraTextpr.setLrcData(lrcsPR);
        }

        if (currentSong.currentIndex == 6) {
            List<Lrc> lrcs = LrcHelper.parseLrcFromAssets(this, "002001.lrc");suraText.setLrcData(lrcs);
            List<Lrc> lrcsTR = LrcHelper.parseLrcFromAssets(this, "002001tr.lrc");suraTexttr.setLrcData(lrcsTR);
            List<Lrc> lrcsPR = LrcHelper.parseLrcFromAssets(this, "002001pr.lrc");suraTextpr.setLrcData(lrcsPR);
        }

        if (currentSong.currentIndex == 7) {
            List<Lrc> lrcs = LrcHelper.parseLrcFromAssets(this, "002001.lrc");suraText.setLrcData(lrcs);
            List<Lrc> lrcsTR = LrcHelper.parseLrcFromAssets(this, "002001tr.lrc");suraTexttr.setLrcData(lrcsTR);
            List<Lrc> lrcsPR = LrcHelper.parseLrcFromAssets(this, "002001pr.lrc");suraTextpr.setLrcData(lrcsPR);
        }

        if (currentSong.currentIndex == 8) {
            List<Lrc> lrcs = LrcHelper.parseLrcFromAssets(this, "002001.lrc");suraText.setLrcData(lrcs);
            List<Lrc> lrcsTR = LrcHelper.parseLrcFromAssets(this, "002001tr.lrc");suraTexttr.setLrcData(lrcsTR);
            List<Lrc> lrcsPR = LrcHelper.parseLrcFromAssets(this, "002001pr.lrc");suraTextpr.setLrcData(lrcsPR);
        }

        if (currentSong.currentIndex == 9) {
            List<Lrc> lrcs = LrcHelper.parseLrcFromAssets(this, "002001.lrc");suraText.setLrcData(lrcs);
            List<Lrc> lrcsTR = LrcHelper.parseLrcFromAssets(this, "002001tr.lrc");suraTexttr.setLrcData(lrcsTR);
            List<Lrc> lrcsPR = LrcHelper.parseLrcFromAssets(this, "002001pr.lrc");suraTextpr.setLrcData(lrcsPR);
        }

        if (currentSong.currentIndex == 10) {
            List<Lrc> lrcs = LrcHelper.parseLrcFromAssets(this, "002001.lrc");suraText.setLrcData(lrcs);
            List<Lrc> lrcsTR = LrcHelper.parseLrcFromAssets(this, "002001tr.lrc");suraTexttr.setLrcData(lrcsTR);
            List<Lrc> lrcsPR = LrcHelper.parseLrcFromAssets(this, "002001pr.lrc");suraTextpr.setLrcData(lrcsPR);
        }

        if (currentSong.currentIndex == 11) {
            List<Lrc> lrcs = LrcHelper.parseLrcFromAssets(this, "002001.lrc");suraText.setLrcData(lrcs);
            List<Lrc> lrcsTR = LrcHelper.parseLrcFromAssets(this, "002001tr.lrc");suraTexttr.setLrcData(lrcsTR);
            List<Lrc> lrcsPR = LrcHelper.parseLrcFromAssets(this, "002001pr.lrc");suraTextpr.setLrcData(lrcsPR);
        }

        if (currentSong.currentIndex == 12) {
            List<Lrc> lrcs = LrcHelper.parseLrcFromAssets(this, "002001.lrc");suraText.setLrcData(lrcs);
            List<Lrc> lrcsTR = LrcHelper.parseLrcFromAssets(this, "002001tr.lrc");suraTexttr.setLrcData(lrcsTR);
            List<Lrc> lrcsPR = LrcHelper.parseLrcFromAssets(this, "002001pr.lrc");suraTextpr.setLrcData(lrcsPR);
        }

        if (currentSong.currentIndex == 13) {
            List<Lrc> lrcs = LrcHelper.parseLrcFromAssets(this, "002001.lrc");suraText.setLrcData(lrcs);
            List<Lrc> lrcsTR = LrcHelper.parseLrcFromAssets(this, "002001tr.lrc");suraTexttr.setLrcData(lrcsTR);
            List<Lrc> lrcsPR = LrcHelper.parseLrcFromAssets(this, "002001pr.lrc");suraTextpr.setLrcData(lrcsPR);
        }

    }

    private void playMusic(int index) {
        mediaPlayer.reset();
        try {
            AssetManager assetManager = getAssets();
            String[] audios = assetManager.list(SOUNDFILE_PATH);
            if (audios == null || index >= audios.length) {
                return;
            }
            String soundFilePath = new File(SOUNDFILE_PATH, audios[index]).getPath();
            AssetFileDescriptor afd = getAssets().openFd(soundFilePath);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mediaPlayer.setDataSource(afd);
            } else {
                FileDescriptor fd = afd.getFileDescriptor();
                Log.d("MusicPlayerActivity", String.format("<<<< %s %d %d", soundFilePath, afd.getStartOffset(), afd.getLength()));
                mediaPlayer.setDataSource(fd, afd.getStartOffset(), afd.getLength());
            }
            afd.close();
            mediaPlayer.prepare();
            mediaPlayer.start();
            mSeekBarTime.setProgress(0);
            mSeekBarTime.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void playSound() {

        if (currentSong.currentIndex == 0){ playMusic(0);}
        if (currentSong.currentIndex == 1){ playMusic(1);}
        if (currentSong.currentIndex == 2){ playMusic(2);}
        if (currentSong.currentIndex == 3){ playMusic(3);}
        if (currentSong.currentIndex == 4){ playMusic(4);}
        if (currentSong.currentIndex == 5){ playMusic(5);}
        if (currentSong.currentIndex == 6){ playMusic(6);}
        if (currentSong.currentIndex == 7){ playMusic(7);}
        if (currentSong.currentIndex == 8){ playMusic(8);}
        if (currentSong.currentIndex == 9){ playMusic(9);}
        if (currentSong.currentIndex == 10){ playMusic(10);}
        if (currentSong.currentIndex == 11){ playMusic(11);}
        if (currentSong.currentIndex == 12){ playMusic(12);}
        if (currentSong.currentIndex == 13){ playMusic(13);}
    }
    private static final String SOUNDFILE_PATH = "sounds";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    private void centerTitle() {
        ArrayList<View> textViews = new ArrayList<>();

        getWindow().getDecorView().findViewsWithText(textViews, getTitle(), View.FIND_VIEWS_WITH_TEXT);

        if(textViews.size() > 0) {
            AppCompatTextView appCompatTextView = null;
            if(textViews.size() == 1) {
                appCompatTextView = (AppCompatTextView) textViews.get(0);
            } else {
                for(View v : textViews) {
                    if(v.getParent() instanceof Toolbar) {
                        appCompatTextView = (AppCompatTextView) v;
                        break;
                    }
                }
            }

            if(appCompatTextView != null) {
                ViewGroup.LayoutParams params = appCompatTextView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                appCompatTextView.setLayoutParams(params);
                appCompatTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info_btn:
                Intent intent = new Intent(this,InfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(intent);
                return true;
            case R.id.namazTime:
                intent = new Intent(this,Namaz.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}