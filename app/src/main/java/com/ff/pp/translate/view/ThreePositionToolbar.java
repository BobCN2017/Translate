package com.ff.pp.translate.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ff.pp.translate.R;


/**
 * Created by PP on 2017/3/21.
 */

public class ThreePositionToolbar extends Toolbar {

    private Context mContext;
    private LayoutInflater mInflater;
    private View mView;
    private ImageView mCenterIcon;
    private TextView mTitle;
    private ImageButton mRightButton;
    private ImageButton mLeftButton;

    public ThreePositionToolbar(Context context) {
        this(context, null);
    }

    public ThreePositionToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ThreePositionToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        initView(context);
        setUserDefineAttribute(attrs, defStyleAttr);
    }

    private void initView(Context context) {
        if (mView != null) return;
        mInflater = LayoutInflater.from(context);
        mView = mInflater.inflate(R.layout.title_bar, null);

        mCenterIcon = (ImageView) mView.findViewById(R.id.image_center);
        mTitle = (TextView) mView.findViewById(R.id.textView_title_bar);

        mRightButton = (ImageButton) mView.findViewById(R.id.imageButton_title_bar);
        mLeftButton = (ImageButton) mView.findViewById(R.id.imageButton_left_title_bar);

        buttonSetDefaultIcon();
        addView(mView);
        setLeftButtonDefaultListener();
    }

    private void setLeftButtonDefaultListener() {
        setOnLeftButtonClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mContext instanceof Activity){
                   ((Activity) mContext).finish();
                }
            }
        });
    }

    private void buttonSetDefaultIcon() {
        setLeftButtonIcon(R.drawable.ic_menu_selectall_holo_dark);
        setRightButtonIcon(R.drawable.actionbar_more_icon);
    }

    private void setUserDefineAttribute(@Nullable AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {

            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.ThreePositionToolbar, defStyleAttr, 0);
            Drawable centerIcon = a.getDrawable(R.styleable.ThreePositionToolbar_centerIcon);
            if (centerIcon != null) {
                setCenterIcon(centerIcon);
            }
            String title=a.getString(R.styleable.ThreePositionToolbar_centerTitle);
            if (!TextUtils.isEmpty(title)){
                setTitle(title);
            }
            Drawable icon = a.getDrawable(R.styleable.ThreePositionToolbar_rightButtonIcon);
            if (icon != null) {
                setRightButtonIcon(icon);
            }
            Drawable iconLeft = a.getDrawable(R.styleable.ThreePositionToolbar_leftButtonIcon);
            if (iconLeft != null) {
                setLeftButtonIcon(iconLeft);
            }

            a.recycle();
        }
    }

    private void setLeftButtonIcon(Drawable iconLeft) {
        if (mLeftButton != null) {
            mLeftButton.setVisibility(INVISIBLE);
            mLeftButton.setImageDrawable(iconLeft);
        }
    }

    private void setRightButtonIcon(Drawable icon) {
        if (mRightButton != null) {
            mRightButton.setVisibility(INVISIBLE);
            mRightButton.setImageDrawable(icon);
        }
    }


    private void setCenterIcon(Drawable drawable) {
        if (mCenterIcon != null) {
            mCenterIcon.setImageDrawable(drawable);
            mCenterIcon.setVisibility(VISIBLE);
        }
    }

    public void setCenterIcon(int imageId) {
        setCenterIcon(ContextCompat.getDrawable(mContext, imageId));
    }

    @Override
    public void setTitle(@StringRes int resId) {
        this.setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        initView(getContext());
        if (mTitle != null) {
            mTitle.setText(title);
            mTitle.setVisibility(VISIBLE);
        }
    }


    public void setLeftButtonIcon(int resId) {
        setLeftButtonIcon(ContextCompat.getDrawable(mContext, resId));
    }

    public void setRightButtonIcon(int resId) {
        setRightButtonIcon(ContextCompat.getDrawable(mContext, resId));
    }

    public void setOnRightButtonClickListener(OnClickListener listener) {
        mRightButton.setVisibility(VISIBLE);
        mRightButton.setOnClickListener(listener);
    }

    public void setOnLeftButtonClickListener(OnClickListener listener) {
        mLeftButton.setVisibility(VISIBLE);
        mLeftButton.setOnClickListener(listener);
    }
}
