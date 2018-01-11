package vn.manroid.backupcontact.custom.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import vn.manroid.backupcontact.R;

/**
 * Created by manro on 27/12/2017.
 */

public class VTEditText extends android.support.v7.widget.AppCompatEditText{

    private Typeface typeface = null;

    public VTEditText(Context context) {
        super(context);
    }

    public VTEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context,attrs);
    }

    public VTEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context,attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomEditText);
        String customFont = a.getString(R.styleable.CustomEditText_customText);
        typeface = Typeface.createFromAsset(ctx.getAssets(),customFont);
        setTypeface(typeface);
        a.recycle();
    }

}
