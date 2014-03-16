package com.hcigroup1.notes.app;

import com.hcigroup1.notes.app.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {

    private ImageView eraser;
    private Button save_button;
    private Button next_button;
    private static int filename_inc = 0;

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    //private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_fullscreen);

        final DrawingView drawingView = (DrawingView) findViewById(R.id.drawingView);

        eraser = (ImageView) findViewById(R.id.eraser);
        eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (drawingView.isEraserActive) {
                    drawingView.isEraserActive = false;

                    eraser.setImageResource(R.drawable.eraser);

                } else {
                    drawingView.isEraserActive = true;

                    eraser.setImageResource(R.drawable.pencil);
                }

            }
        });

        next_button = (Button) findViewById(R.id.next_button);
//        next_button.setOnClickListener(drawingView);

        final FullscreenActivity self = this;
        next_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                filename_inc ++;
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        save_button = (Button) findViewById(R.id.save_button);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View rootview = getWindow().getDecorView().getRootView();
                rootview.setDrawingCacheEnabled(true);
                Bitmap b = rootview.getDrawingCache();

                //Bitmap b = drawingView.getBitmap();

                //Bitmap b = Bitmap.createBitmap(getWindowManager().getDefaultDisplay().getWidth(),
                //        getWindowManager().getDefaultDisplay().getHeight(),
                //        Bitmap.Config.ARGB_8888);

                FileOutputStream fos = null;
                try {
                    //fos = new FileOutputStream("drawing.png");
                    //fos = openFileOutput("drawing.png", Context.MODE_WORLD_READABLE);
                    File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    File file = new File(dir, "drawing"+filename_inc+".png");
                    fos = new FileOutputStream(file);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                b.compress(Bitmap.CompressFormat.PNG, 95, fos);



            }
        });

//        final View controlsView = findViewById(R.id.fullscreen_content_controls);
//        final View contentView = findViewById(R.id.fullscreen_content);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
//        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
//        mSystemUiHider.setup();
//        mSystemUiHider
//                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
//                    // Cached values.
//                    int mControlsHeight;
//                    int mShortAnimTime;
//
//                    @Override
//                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//                    public void onVisibilityChange(boolean visible) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//                            // If the ViewPropertyAnimator API is available
//                            // (Honeycomb MR2 and later), use it to animate the
//                            // in-layout UI controls at the bottom of the
//                            // screen.
//                            if (mControlsHeight == 0) {
//                                mControlsHeight = controlsView.getHeight();
//                            }
//                            if (mShortAnimTime == 0) {
//                                mShortAnimTime = getResources().getInteger(
//                                        android.R.integer.config_shortAnimTime);
//                            }
//                            controlsView.animate()
//                                    .translationY(visible ? 0 : mControlsHeight)
//                                    .setDuration(mShortAnimTime);
//                        } else {
//                            // If the ViewPropertyAnimator APIs aren't
//                            // available, simply show or hide the in-layout UI
//                            // controls.
//                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
//                        }
//
//                        if (visible && AUTO_HIDE) {
//                            // Schedule a hide().
//                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
//                        }
//                    }
//                });

        // Set up the user interaction to manually show or hide the system UI.
//        contentView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (TOGGLE_ON_CLICK) {
//                    mSystemUiHider.toggle();
//                } else {
//                    mSystemUiHider.show();
//                }
//            }
//        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }

//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//
//        // Trigger the initial hide() shortly after the activity has been
//        // created, to briefly hint to the user that UI controls
//        // are available.
//        delayedHide(100);
//    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
//    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            if (AUTO_HIDE) {
//                delayedHide(AUTO_HIDE_DELAY_MILLIS);
//            }
//            return false;
//        }
//    };

    //Handler mHideHandler = new Handler();
    //Runnable mHideRunnable = new Runnable() {
//        @Override
//        public void run() {
//            mSystemUiHider.hide();
//        }
//    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
//    private void delayedHide(int delayMillis) {
//        mHideHandler.removeCallbacks(mHideRunnable);
//        mHideHandler.postDelayed(mHideRunnable, delayMillis);
//    }
}
