package co.kogi.chronometerapp.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import co.kogi.chronometerapp.R;
import co.kogi.chronometerapp.model.Lap;
import co.kogi.chronometerapp.ui.adapter.LapAdapter;

/*
    This app was made by Miguel Ruiz as part of a developer test for kogiMobile
 */

public class MainActivity extends AppCompatActivity {

    private static final int TICK_WHAT = 2;
    private ImageView imageView_startPause;
    private ImageView imageView_lap;
    private ImageView imageView_restart;
    private TextView textView_hours;
    private TextView textView_minutes;
    private TextView textView_millis;
    private TextView textView_seconds;
    private TextView textView_days;
    private LinearLayout linearLayout_background;
    private ListView listView_laps;
    private LapAdapter lapAdapter;
    private boolean mRunning;
    private boolean mStarted;
    private long mBase;
    private long mElapsed = 0;
    private long lastLap = 0;
    private boolean stopwatchStarted = false;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            if (mRunning) {
                updateText(SystemClock.elapsedRealtime());
                sendMessageDelayed(Message.obtain(this, TICK_WHAT),
                        100);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView_laps = (ListView) findViewById(R.id.listView_recentLaps);
        linearLayout_background = (LinearLayout) findViewById(R.id.background);
        imageView_lap = (ImageView) findViewById(R.id.imageView_lap);
        imageView_startPause = (ImageView) findViewById(R.id.imageView_startPause);
        textView_hours = (TextView) findViewById(R.id.textView_hours);
        textView_minutes = (TextView) findViewById(R.id.textView_minutes);
        textView_millis = (TextView) findViewById(R.id.textView_millis);
        textView_seconds = (TextView) findViewById(R.id.textView_seconds);
        textView_days = (TextView) findViewById(R.id.textView_days);
        imageView_restart = (ImageView) findViewById(R.id.imageView_restart);
        lapAdapter = new LapAdapter(this);
        listView_laps.setAdapter(lapAdapter);

        imageView_startPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!stopwatchStarted) {
                    stopwatchStarted = true;
                    linearLayout_background.setBackgroundColor(Color.parseColor("#F9A825"));
                    imageView_startPause.setImageResource(R.drawable.selector_pause);
                    imageView_restart.setVisibility(View.VISIBLE);
                    imageView_lap.setVisibility(View.VISIBLE);
                    start();
                } else {
                    stopwatchStarted = false;
                    linearLayout_background.setBackgroundColor(Color.parseColor("#F44336"));
                    imageView_startPause.setImageResource(R.drawable.selector_play);
                    pause();
                }
            }
        });

        imageView_lap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stopwatchStarted) {
                    addLap();
                }
            }
        });

        imageView_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mElapsed = 0;
                stopwatchStarted = false;
                imageView_restart.setVisibility(View.INVISIBLE);
                imageView_lap.setVisibility(View.INVISIBLE);
                imageView_startPause.setImageResource(R.drawable.selector_play);
                linearLayout_background.setBackgroundColor(Color.parseColor("#f44336"));
                stop();
                textView_days.setText("00");
                textView_hours.setText("00");
                textView_minutes.setText("00");
                textView_seconds.setText("00");
                textView_millis.setText("000");
            }
        });

        if (savedInstanceState != null) {
            boolean stopwatchWasStarted = (Boolean) savedInstanceState.get("stopwatchWasStarted");
            mRunning = (Boolean) savedInstanceState.get("mRunning");
            Lap[] array = (Lap[]) savedInstanceState.get("laps");
            mElapsed = (Long) savedInstanceState.get("mElapsed");
            lapAdapter.setItems(new ArrayList<>(Arrays.asList(array)));
            lastLap = (Long) savedInstanceState.get("lastLap");
            if (stopwatchWasStarted) {
                stopwatchStarted = true;
                linearLayout_background.setBackgroundColor(Color.parseColor("#F9A825"));
                imageView_startPause.setImageResource(R.drawable.selector_pause);
                imageView_restart.setVisibility(View.VISIBLE);
                imageView_lap.setVisibility(View.VISIBLE);
                start();

            } else if (mElapsed>0) {
                mBase = SystemClock.elapsedRealtime() - mElapsed;
                updateText(SystemClock.elapsedRealtime());
                imageView_restart.setVisibility(View.VISIBLE);
                imageView_lap.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_clearLaps) {
           new AlertDialog.Builder(this)
                   .setTitle("Information")
                   .setMessage("Do you want to erase the lap list")
                   .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           lapAdapter.clearItems();
                       }
                   }).setNegativeButton("Cancel",null)
                   .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mStarted) {
            pause();
        }
        Lap[] array = new Lap[lapAdapter.getItems().size()];
        lapAdapter.getItems().toArray(array);
        outState.putBoolean("mRunning", mRunning);
        outState.putParcelableArray("laps", array);
        outState.putLong("mElapsed", mElapsed);
        outState.putBoolean("stopwatchWasStarted", stopwatchStarted);
        outState.putBoolean("screenChange", true);
        outState.putLong("lastLap",lastLap);
    }

    private void start() {
        if (mElapsed == 0) {
            mBase = SystemClock.elapsedRealtime();
        } else {
            mBase = SystemClock.elapsedRealtime() - mElapsed;
        }
        mStarted = true;
        updateRunning();
    }

    private void pause() {
        mStarted = false;
        mElapsed = SystemClock.elapsedRealtime() - mBase;
        updateRunning();

    }

    private void stop() {
        mElapsed = 0;
        mStarted = false;
        updateRunning();
    }


    private void addLap() {
        DecimalFormat df = new DecimalFormat("00");
        DecimalFormat dfm = new DecimalFormat("000");
        long timeElapsed;

        if (lastLap == 0) {
            timeElapsed = SystemClock.elapsedRealtime() - mBase;
        } else {
            timeElapsed = SystemClock.elapsedRealtime() - lastLap;
        }

        lastLap = SystemClock.elapsedRealtime();
        int days = (int) (timeElapsed / (3600 * 1000 * 24));
        int hours = (int) (timeElapsed / (3600 * 1000));
        int remaining = (int) (timeElapsed % (3600 * 1000));
        int minutes = (remaining / (60 * 1000));
        remaining = (remaining % (60 * 1000));
        int seconds = (remaining / 1000);
        int milliseconds = (((int) timeElapsed % 1000));

        lapAdapter.addItem(new Lap(df.format(days), df.format(hours), df.format(minutes),
                df.format(seconds), dfm.format(milliseconds)));
    }


    private synchronized void updateText(long now) {
        DecimalFormat df = new DecimalFormat("00");
        DecimalFormat dfm = new DecimalFormat("000");
        long timeElapsed;
        timeElapsed = now - mBase;

        int days = (int) (timeElapsed / (3600 * 1000 * 24));
        int hours = (int) (timeElapsed / (3600 * 1000));
        int remaining = (int) (timeElapsed % (3600 * 1000));
        int minutes = (remaining / (60 * 1000));
        remaining = (remaining % (60 * 1000));
        int seconds = (remaining / 1000);
        int milliseconds = (((int) timeElapsed % 1000));

        textView_days.setText(df.format(days));
        textView_hours.setText(df.format(hours));
        textView_minutes.setText(df.format(minutes));
        textView_seconds.setText(df.format(seconds));
        textView_millis.setText(dfm.format(milliseconds));

    }

    private void updateRunning() {
        boolean running = mStarted;
        if (running != mRunning) {
            if (running) {
                updateText(SystemClock.elapsedRealtime());
                mHandler.sendMessageDelayed(Message.obtain(mHandler,
                        TICK_WHAT), 100);
            } else {
                mHandler.removeMessages(TICK_WHAT);
            }
            mRunning = running;
        }
    }


}
