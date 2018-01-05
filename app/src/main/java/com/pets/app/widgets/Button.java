package com.pets.app.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.pets.app.R;
import com.pets.app.app.MyApplication;

public class Button extends android.support.v7.widget.AppCompatButton {

    private TypedArray a = null;

    public Button(Context context) {
        super(context);
        init(null, 0);
    }

    public Button(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public Button(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        int fontStyle;
        try {
            this.a = getContext().obtainStyledAttributes(attrs, R.styleable.MyCustomWidget, defStyle, 0);
            fontStyle = a.getInteger(R.styleable.MyCustomWidget_fontStyle, 0);
        } finally {
            if (this.a != null)
                this.a.recycle();
        }

        if (!isInEditMode()) {
            switch (fontStyle) {
                case 1:
                    setTypeface(MyApplication.fontRegular);
                    break;
                case 2:
                    setTypeface(MyApplication.fontItalic);
                    break;
                case 3:
                    setTypeface(MyApplication.fontBold);
                    break;
                case 4:
                    setTypeface(MyApplication.fontBoldItalic);
                    break;
                default:
                    setTypeface(MyApplication.fontRegular);
                    break;
            }
        }
    }
}
