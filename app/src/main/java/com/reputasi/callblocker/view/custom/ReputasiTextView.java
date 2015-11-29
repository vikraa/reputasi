package com.reputasi.callblocker.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.reputasi.callblocker.R;
import com.reputasi.callblocker.view.utilities.Font;

/**
 * Created by vikraa on 4/18/2015.
 */
public class ReputasiTextView extends TextView {

    int mTypeFace;

    public ReputasiTextView(Context context) {
        super(context);
        if (!isInEditMode()){
            setTypeface(Font.getFont(getContext(), Font.FontType.RobotoRegular));
        }
    }

    public ReputasiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()){
            applyTypeFace(context, attrs);
        }
    }

    public ReputasiTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()){
            applyTypeFace(context, attrs);
        }
    }

    private void applyTypeFace(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ReputasiFonts, 0, 0);

        try {
            mTypeFace = a.getInteger(R.styleable.ReputasiFonts_fontType, 0);

            switch (mTypeFace) {
                case 0:
                    setTypeface(Font.getFont(getContext(), Font.FontType.RobotoLight));
                    break;
                case 1:
                    setTypeface(Font.getFont(getContext(), Font.FontType.RobotoBlack));
                    break;
                case 2:
                    setTypeface(Font.getFont(getContext(), Font.FontType.RobotoBold));
                    break;
                case 3:
                    setTypeface(Font.getFont(getContext(), Font.FontType.RobotoItalic));
                    break;
                case 4:
                    setTypeface(Font.getFont(getContext(), Font.FontType.RobotoMedium));
                    break;
                case 5:
                    setTypeface(Font.getFont(getContext(), Font.FontType.RobotoRegular));
                    break;
                case 6:
                    setTypeface(Font.getFont(getContext(), Font.FontType.BebasNeue));
                    break;
                default:
                    setTypeface(Font.getFont(getContext(), Font.FontType.RobotoRegular));
            }
        } finally {
            a.recycle();
        }
    }
}
