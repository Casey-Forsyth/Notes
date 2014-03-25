package com.hcigroup1.notes.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.ListIterator;

import static java.lang.Math.max;

public class DrawingView extends View implements OnTouchListener
{
    private Canvas  m_Canvas;
    private Path    m_Path;
    private Paint   pen_paint;
    private Paint   eraser_paint;
    private Bitmap  m_Bitmap;
    private int     paintColour;
    private int     eraseColour;

    ArrayList<Pair<Path, Paint>> paths = new ArrayList<Pair<Path, Paint>>();
    ArrayList<Pair<Path, Paint>> undonePaths = new ArrayList<Pair<Path, Paint>>();

    private float mX, mY;
    private float maxX, maxY;

    private static final float TOUCH_TOLERANCE = 4;

    private boolean isEraserActive;

    public DrawingView(Context context)
    {
        super(context);
        isEraserActive = false;
        setFocusable(true);
        setFocusableInTouchMode(true);
        setBackgroundColor(Color.WHITE);
        this.setOnTouchListener(this);
        onCanvasInitialization();
    }

    public DrawingView(Context context, AttributeSet attr)
    {
        this(context);
    }

    public DrawingView(DrawingView oldView, Context context)
    {
        this(context);
        //        m_Bitmap = oldView.getBitmap();
        paintColour     = oldView.paintColour;
        isEraserActive  = oldView.isEraserActive;
        pen_paint = new Paint(oldView.pen_paint);
        paths           = oldView.paths;
        undonePaths     = oldView.undonePaths;
    }

    public void onCanvasInitialization()
    {
        paintColour = Color.BLACK;
        eraseColour = Color.WHITE;

        pen_paint = new Paint();
        pen_paint.setAntiAlias(true);
        pen_paint.setDither(true);
        pen_paint.setColor(paintColour);
        pen_paint.setStyle(Paint.Style.STROKE);
        pen_paint.setStrokeJoin(Paint.Join.ROUND);
        pen_paint.setStrokeCap(Paint.Cap.ROUND);
        pen_paint.setStrokeWidth(2);


        eraser_paint = new Paint(pen_paint);
        eraser_paint.setColor(eraseColour);
        eraser_paint.setStrokeWidth(32);

        m_Canvas = null;//new Canvas();

        m_Path = new Path();
        //		Paint newPaint = new Paint(pen_paint);
        //		paths.add(new Pair<Path, Paint>(m_Path, newPaint));

        maxX = maxY = 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        m_Bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        m_Canvas = new Canvas(m_Bitmap);

        invalidate();

    }

    public void toggleEraser()
    {
        isEraserActive = !isEraserActive;
    }

    public boolean isErasing()
    {
        return isEraserActive;
    }

    public boolean onTouch(View arg0, MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw (Canvas canvas)
    {
        ListIterator<Pair<Path, Paint>> iter = paths.listIterator();
        Pair<Path, Paint> p;

        try
        {
            while (iter.hasNext())
            {
                p = iter.next();
                canvas.drawPath(p.first, p.second);
                m_Canvas.drawPath(p.first, p.second);
            }
        }
        catch(ConcurrentModificationException e)
        {
            System.out.print("ConcurrentModificationException");
        }
    }

    private void touch_start(float x, float y)
    {
        Paint paint;

        if (isEraserActive)
        {
            paint = eraser_paint;
        }
        else
        {
            paint = pen_paint;
        }

        Paint newPaint = new Paint(paint);
        paths.add(new Pair<Path, Paint>(m_Path, newPaint));

        m_Path.reset();
        m_Path.moveTo(x, y);
        mX = x;
        mY = y;

        maxX = max(x, maxX);
        maxY = max(y, maxY);
    }

    private void touch_move(float x, float y)
    {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE)
        {
            m_Path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;

            maxX = max(x, maxX);
            maxY = max(y, maxY);
        }
    }

    private void touch_up()
    {
        m_Path.lineTo(mX, mY);

        m_Path = new Path();
    }

    public Bitmap getBitmap()
    {
        return m_Bitmap;
    }

    public void onClickUndo()
    {
        if (paths.size() > 0)
        {
            undonePaths.add(paths.remove(paths.size() - 1));
            m_Bitmap.eraseColor(Color.WHITE);
            invalidate();
        }
    }

    public void onClickRedo()
    {
        if (undonePaths.size() > 0)
        {
            paths.add(undonePaths.remove(undonePaths.size() - 1));
            invalidate();
        }
    }

    public void setPaintColour(int selectedColour)
    {
        paintColour = selectedColour;
        pen_paint.setColor(paintColour);
    }

}
