package vn.manroid.backupcontact.custom.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;

import vn.manroid.backupcontact.R;

/**
 * Created by manro on 27/12/2017.
 */

public class VTButton extends Button {

    public VTButton(Context context) {
        super(context);
    }

    public VTButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initStyleButton(attrs);
    }

    public VTButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initStyleButton(attrs);
    }

    private void initStyleButton(AttributeSet attrs){
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomButton);
        String Text1 = a.getString(R.styleable.CustomButton_myText_1);
        String Text2 = a.getString(R.styleable.CustomButton_myText_2);
        setText(Text1 + "\n" + Text2);
        a.recycle();
    }

}
