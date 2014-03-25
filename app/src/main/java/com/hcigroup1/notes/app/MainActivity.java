package com.hcigroup1.notes.app;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity implements ColourPickerDialogFragment.ColourPickerDialogListener
{
    private ImageView eraser;
    private ImageView undo;
    private ImageView redo;
    private ImageView picker;
    private Button save_button;
    private Button next_button;
    private static int filename_inc = 0;
    private LinearLayout drawingLayout;
    private static DrawingView drawingView;
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawingLayout = (LinearLayout) findViewById(R.id.drawingLayout);

        if( drawingView == null )
        {
            drawingView = new DrawingView(drawingLayout.getContext());
        }
        else
        {
            drawingView = new DrawingView(drawingView, drawingLayout.getContext());
        }
        drawingLayout.addView(drawingView);

        eraser = (ImageView) findViewById(R.id.eraser);
        if (drawingView.isErasing())
            eraser.setImageResource(R.drawable.pencil);
        else
            eraser.setImageResource(R.drawable.eraser);

        eraser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (drawingView.isErasing())
                    eraser.setImageResource(R.drawable.eraser);
                else
                    eraser.setImageResource(R.drawable.pencil);

                drawingView.toggleEraser();
            }
        });

        undo = (ImageView) findViewById(R.id.undo);
        undo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                drawingView.onClickUndo();
            }
        });

        redo = (ImageView) findViewById(R.id.redo);
        redo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                drawingView.onClickRedo();
            }
        });

        picker = (ImageView) findViewById(R.id.picker);
        picker.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentManager fragmentManager = getFragmentManager();
                ColourPickerDialogFragment colourPicker = new ColourPickerDialogFragment();

                // Show the fragment as a dialog
                colourPicker.show(fragmentManager, "dialog");

            }
        });

        next_button = (Button) findViewById(R.id.next_button);
        next_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                drawingLayout.removeView(drawingView);
                drawingView = new DrawingView(drawingLayout.getContext());
                drawingLayout.addView(drawingView);
                filename_inc ++;
            }
        });

        save_button = (Button) findViewById(R.id.save_button);
        save_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                saveImage();
            }
        });

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                saveImage();
            }
        }, 1500, 1500);
    }

    @Override
    public void onDialogClick(DialogFragment dialog)
    {
        drawingView.setPaintColour(((ColourPickerDialogFragment)dialog).getSelectedColour());

        if(drawingView.isErasing())
        {
            drawingView.toggleEraser();
            eraser.setImageResource(R.drawable.eraser);
        }
    }

    private void saveImage()
    {
        try
        {
            Bitmap b = drawingView.getBitmap();
            //            drawingView.buildDrawingCache();
            //            Bitmap b = drawingView.getDrawingCache();
            FileOutputStream fos; // = null;
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File file = new File(dir, "drawing"+filename_inc+".png");
            fos = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.PNG, 95, fos);
            fos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
