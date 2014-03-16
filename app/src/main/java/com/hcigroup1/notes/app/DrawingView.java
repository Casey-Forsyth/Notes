package com.hcigroup1.notes.app;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DrawingView extends View implements OnTouchListener {
	private Canvas  m_Canvas;
	private Path    m_Path;
	private Paint   m_Paint;
    private Bitmap  m_Bitmap;
    private Paint   m_BitmapPaint;

	ArrayList<Pair<Path, Paint>> paths = new ArrayList<Pair<Path, Paint>>();
	ArrayList<Pair<Path, Paint>> undonePaths = new ArrayList<Pair<Path, Paint>>();

	private float mX, mY;

	private static final float TOUCH_TOLERANCE = 4;

	public static boolean isEraserActive = false; 

	public DrawingView(Context context, AttributeSet attr) {
		super(context);
		setFocusable(true);
		setFocusableInTouchMode(true);
		
		setBackgroundColor(Color.WHITE);
		
		this.setOnTouchListener(this);

		onCanvasInitialization();
	}

	public void onCanvasInitialization() {
		m_Paint = new Paint();
		m_Paint.setAntiAlias(true);
		m_Paint.setDither(true);
		m_Paint.setColor(Color.parseColor("#000000")); 
		m_Paint.setStyle(Paint.Style.STROKE);
		m_Paint.setStrokeJoin(Paint.Join.ROUND);
		m_Paint.setStrokeCap(Paint.Cap.ROUND);
		m_Paint.setStrokeWidth(2);

		m_Canvas = new Canvas();

//        m_Bitmap = Bitmap.createBitmap(getWindowManager().getDefaultDisplay().getWidth(),
//                                       getWindowManager().getDefaultDisplay().getHeight(),
//                                       Bitmap.Config.ARGB_8888);

        m_BitmapPaint = new Paint(Paint.DITHER_FLAG);

		m_Path = new Path();
		Paint newPaint = new Paint(m_Paint);
		paths.add(new Pair<Path, Paint>(m_Path, newPaint));
 
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
        m_Bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        m_Canvas = new Canvas(m_Bitmap);
	}

	public boolean onTouch(View arg0, MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
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
	protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(m_Bitmap, 0, 0, m_BitmapPaint);

		for (Pair<Path, Paint> p : paths) {
			canvas.drawPath(p.first, p.second);
		}
	}

	private void touch_start(float x, float y) {
		
		if (isEraserActive) {
			m_Paint.setColor(Color.WHITE);
			m_Paint.setStrokeWidth(6);
			Paint newPaint = new Paint(m_Paint); // Clones the mPaint object
			paths.add(new Pair<Path, Paint>(m_Path, newPaint));
			 
		} else { 
			m_Paint.setColor(Color.BLACK);
			m_Paint.setStrokeWidth(2);
			Paint newPaint = new Paint(m_Paint); // Clones the mPaint object
			paths.add(new Pair<Path, Paint>(m_Path, newPaint));
			 
		}
		
		
		m_Path.reset();
		m_Path.moveTo(x, y);
		mX = x;
		mY = y;
	}

	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			m_Path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
		}
	}

	private void touch_up() {
		m_Path.lineTo(mX, mY);

		// commit the path to our offscreen
		m_Canvas.drawPath(m_Path, m_Paint);

		// kill this so we don't double draw
		m_Path = new Path();
		Paint newPaint = new Paint(m_Paint); // Clones the mPaint object
		paths.add(new Pair<Path, Paint>(m_Path, newPaint));
	}

    public void clear()
    {
        System.out.println("Test");
        this.onClickUndo();
//        this.onCanvasInitialization();
//
//        m_Path.reset();
//        m_Path = new Path();
//        paths = new ArrayList<Pair<Path, Paint>>();
//        undonePaths = new ArrayList<Pair<Path, Paint>>();
//        m_Canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//
//        m_Canvas = new Canvas();
//        //m_Canvas.
//        //m_Canvas.drawColor(Color.BLACK);
//        //m_Canvas.drawColor(Color.WHITE);
//
//        Paint clearPaint = new Paint();
//        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//        m_Canvas.drawRect(0, 0, m_Canvas.getWidth(), m_Canvas.getHeight(), clearPaint);
//
//        m_Bitmap.eraseColor(Color.WHITE);
//        m_Canvas.drawBitmap(m_Bitmap, 0, 0, m_BitmapPaint);

    }

    public Bitmap getBitmap()
    {
        return m_Bitmap;
    }

//    @Override
//    public void onClick(View v)
//    {
//        this.clear();
//        Button b = (Button) v;
//        b.setBackgroundColor(Color.RED);
//        b.invalidate();
//    }

//	public void onClickPenColorButton(int penColor) {
//
//		if (isEraserActive) {
//			m_Paint.setColor(Color.WHITE);
//			Paint newPaint = new Paint(m_Paint); // Clones the mPaint object
//			paths.add(new Pair<Path, Paint>(m_Path, newPaint));
//
//			isEraserActive = false;
//		} else {
//			m_Paint.setColor(Color.RED);
//			Paint newPaint = new Paint(m_Paint); // Clones the mPaint object
//			paths.add(new Pair<Path, Paint>(m_Path, newPaint));
//
//			isEraserActive = true;
//		}
//
//	}
//
	public void onClickUndo() {
		if (paths.size() > 0) {
			undonePaths.add(paths.remove(paths.size() - 1));
			invalidate();
		} else {

		}
	}
//
//	public void onClickRedo() {
//		if (undonePaths.size() > 0) {
//			paths.add(undonePaths.remove(undonePaths.size() - 1));
//			invalidate();
//		} else {
//
//		}
//	}

}