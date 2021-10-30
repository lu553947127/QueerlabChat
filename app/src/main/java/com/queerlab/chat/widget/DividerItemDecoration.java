package com.queerlab.chat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.widget
 * @ClassName: DividerItemDecoration
 * @Description: 自定义RecyclerView分割线
 * @Author: 鹿鸿祥
 * @CreateDate: 5/19/21 1:28 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/19/21 1:28 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private Drawable mDivider;

    private int mOrientation;

    private int mLeftPadding = 0;
    private int mRightPadding = 0;

    public DividerItemDecoration(Context context, int orientation) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
    }

    public DividerItemDecoration(Context context, int orientation, int paddingLeft, int paddingRight) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
        mLeftPadding = dp2px(context, paddingLeft);
        mRightPadding = dp2px(context, paddingRight);
    }

    public DividerItemDecoration(Context context, int orientation, @DrawableRes int drawableRes) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = context.getResources().getDrawable(drawableRes);
        a.recycle();
        setOrientation(orientation);
    }

    public DividerItemDecoration(Context context, int orientation, @DrawableRes int drawableRes, int paddingLeft, int paddingRight) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = context.getResources().getDrawable(drawableRes);
        a.recycle();
        setOrientation(orientation);
        mLeftPadding = dp2px(context, paddingLeft);
        mRightPadding = dp2px(context, paddingRight);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null || mDivider == null) {
            return;
        }

        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft() + mLeftPadding;
        final int right = parent.getWidth() - parent.getPaddingRight() - mRightPadding;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            //最后一行不画分割线
            if (i == childCount - 1){
                continue;
            }

            final View child = parent.getChildAt(i);
            RecyclerView v = new RecyclerView(parent.getContext());
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (mDivider == null) {
            outRect.set(0, 0, 0, 0);
            return;
        }

        int childAdapterPosition = parent.getChildAdapterPosition(view);
        int childLayoutPosition = parent.getChildLayoutPosition(view);
        int itemCount = parent.getAdapter().getItemCount();

        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else {
            //最后一列不加分割线
            if (childAdapterPosition != itemCount - 1){
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            }
        }
    }

    private int dp2px(Context context, float dip){
        return (int) (context.getResources().getDisplayMetrics().density * dip);
    }
}
