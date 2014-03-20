package com.hcigroup1.notes.app;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    private ImageView eraser;
    private Button save_button;
    private Button next_button;
    private static int filename_inc = 0;
    private LinearLayout drawingLayout;
    private DrawingView drawingView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawingLayout = (LinearLayout) findViewById(R.id.drawingLayout);

        drawingView = new DrawingView(drawingLayout.getContext());
        drawingLayout.addView(drawingView);

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
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap b = drawingView.getBitmap();
                FileOutputStream fos = null;

                try
                {
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
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
