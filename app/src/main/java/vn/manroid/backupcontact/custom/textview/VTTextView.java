package vn.manroid.backupcontact.custom.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;

import vn.manroid.backupcontact.R;

/**
 * Created by manro on 27/12/2017.
 */

public class VTTextView extends android.support.v7.widget.AppCompatTextView {

    private Typeface typeface = null;


    public VTTextView(Context context) {
        super(context);
//        Typeface face=Typeface.createFromAsset(context.getAssets(), "mylove.ttf");
//        this.setTypeface(face);
    }

    public VTTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public VTTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context, attrs);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        String customFont = a.getString(R.styleable.CustomTextView_customFont);
        typeface = Typeface.createFromAsset(ctx.getAssets(),customFont);
        setTypeface(typeface);
        a.recycle();
    }
}
