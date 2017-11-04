package com.unixtrong.tablelayout;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Author(s): danyun
 * Date: 2017/11/3
 */
public class SimpleTableLayout extends RelativeLayout {

    @IdRes
    private static final int ID_TITLE_BLOCK = 1;
    @IdRes
    private static final int ID_TITLE_ROW = 2;
    @IdRes
    private static final int ID_TITLE_COLUMN = 3;
    @IdRes
    private static final int ID_CONTENT = 4;

    private final LinearLayout mFirstCellLayout;
    private final LinearLayout mTitleRowLayout;
    private final LinearLayout mTitleColumnLayout;
    private final TableLayout mContentLayout;
    private final HorScrollView mTitleRowScroll;
    private final VerScrollView mTitleColumnScroll;
    private final HorScrollView mContentInnerHorScroll;
    private final VerScrollView mContentScroll;

    private int mTitleColumnWidth;
    private int[] mColumnWidth;

    private String mFirstCellText;
    private String[] mRowTitles;
    private String[] mColumnTitles;
    private LinkedHashMap<String, LinkedHashMap<String, ?>> mDataMap;

    public SimpleTableLayout(@NonNull Context context) {
        super(context);
    }

    public SimpleTableLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleTableLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        // init layout
        mFirstCellLayout = new LinearLayout(getContext());
        mTitleRowLayout = new LinearLayout(getContext());
        mTitleColumnLayout = new LinearLayout(getContext());
        mContentLayout = new TableLayout(getContext());

        // init res id
        mFirstCellLayout.setId(ID_TITLE_BLOCK);
        mTitleRowLayout.setId(ID_TITLE_ROW);
        mTitleColumnLayout.setId(ID_TITLE_COLUMN);
        mContentLayout.setId(ID_CONTENT);
        mTitleColumnLayout.setOrientation(LinearLayout.VERTICAL);

        // init scroll
        mTitleRowScroll = new HorScrollView(getContext());
        mTitleColumnScroll = new VerScrollView(getContext());
        mContentInnerHorScroll = new HorScrollView(getContext());
        mContentScroll = new VerScrollView(getContext());

        // add layout to scroll
        mTitleRowScroll.addView(mTitleRowLayout);
        mTitleColumnScroll.addView(mTitleColumnLayout);
        mContentScroll.addView(mContentInnerHorScroll);
        mContentInnerHorScroll.addView(mContentLayout);

        // init layout params
        // add layout to root
        int wrap = RelativeLayout.LayoutParams.WRAP_CONTENT;
        this.addView(this.mFirstCellLayout);

        RelativeLayout.LayoutParams titleRowParams = new RelativeLayout.LayoutParams(wrap, wrap);
        titleRowParams.addRule(RelativeLayout.END_OF, this.mFirstCellLayout.getId());
        this.addView(this.mTitleRowScroll, titleRowParams);

        RelativeLayout.LayoutParams titleColumnParams = new RelativeLayout.LayoutParams(wrap, wrap);
        titleColumnParams.addRule(RelativeLayout.BELOW, this.mFirstCellLayout.getId());
        this.addView(this.mTitleColumnScroll, titleColumnParams);

        RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams(wrap, wrap);
        contentParams.addRule(RelativeLayout.END_OF, this.mFirstCellLayout.getId());
        contentParams.addRule(RelativeLayout.BELOW, this.mFirstCellLayout.getId());
        this.addView(this.mContentScroll, contentParams);
    }

    public void setDataList(LinkedHashMap<String, LinkedHashMap<String, ?>> dataMap) {
        if (dataMap.isEmpty()) {
            return;
        }

        Set<LinkedHashMap.Entry<String, LinkedHashMap<String, ?>>> entrySet = dataMap.entrySet();
        mRowTitles = new String[entrySet.size()];
        int i = 0;
        for (LinkedHashMap.Entry<String, LinkedHashMap<String, ?>> entry : entrySet) {
            mRowTitles[i++] = entry.getKey();
            Set<? extends Map.Entry<String, ?>> columnEntrySet = entry.getValue().entrySet();
            mColumnTitles = new String[columnEntrySet.size()];
            mColumnWidth = new int[columnEntrySet.size()];
            int j = 0;
            for (Map.Entry<String, ?> columnEntry : columnEntrySet) {
                mColumnTitles[j++] = columnEntry.getKey();
            }
        }
        this.mDataMap = dataMap;
        notifyDataChanged();
    }

    public void setFirstCell(String firstCellText) {
        mFirstCellText = firstCellText;
    }

    private void notifyDataChanged() {
        fillFirstCellView();
        fillTitleRow();
        resizeTitleRowHeight();
        resizeColumnWidth();
        fillContentRow();
        resizeRowHeight();
    }

    private void fillContentRow() {
        for (String rowKey : mRowTitles) {
            this.fillTitleColumnChild(rowKey);
            this.fillContentRow(mDataMap.get(rowKey));
        }
    }

    private void fillTitleColumnChild(String titleColumnText) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mTitleColumnWidth, LayoutParams.MATCH_PARENT);
        params.setMargins(10, 10, 10, 10);
        mTitleColumnLayout.addView(this.bodyTextView(titleColumnText), params);
    }

    private void fillContentRow(Map<String, ?> info) {
        TableRow contentTableRow = new TableRow(getContext());
        for (int i = 0; i < mColumnTitles.length; i++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(mColumnWidth[i], LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(2, 2, 0, 0);
            TextView textView = this.bodyTextView(info.get(mColumnTitles[i]).toString());
            contentTableRow.addView(textView, params);
        }
        this.mContentLayout.addView(contentTableRow);
    }

    private TextView bodyTextView(String label) {
        TextView bodyTextView = new TextView(getContext());
        bodyTextView.setText(label);
        bodyTextView.setGravity(Gravity.CENTER);
        bodyTextView.setPadding(5, 5, 5, 5);
        return bodyTextView;
    }

    private void resizeTitleRowHeight() {
        int firstCellHeight = measureHeight(mFirstCellLayout);
        int titleRowHeight = measureHeight(mTitleRowLayout);

        LinearLayout layout = firstCellHeight < titleRowHeight ? mFirstCellLayout : mTitleRowLayout;
        int finalHeight = Math.max(firstCellHeight, titleRowHeight);
        resizeChildHeight(layout, finalHeight);
    }

    private void resizeColumnWidth() {
        int columnCount = mTitleRowLayout.getChildCount();
        this.mTitleColumnWidth = measureWidth(mFirstCellLayout);
        for (int i = 0; i < columnCount; i++) {
            this.mColumnWidth[i] = measureWidth(mTitleRowLayout.getChildAt(i));
        }
    }

    private void resizeRowHeight() {
        int titleColumnChildCount = mTitleColumnLayout.getChildCount();
        for (int i = 0; i < titleColumnChildCount; i++) {
            View titleColumnChildView = mTitleColumnLayout.getChildAt(i);
            TableRow contentRowView = (TableRow) mContentLayout.getChildAt(i);

            int titleColumnChildHeight = measureHeight(titleColumnChildView);
            int contentRowHeight = measureHeight(contentRowView);

            View childView = titleColumnChildHeight < contentRowHeight ? titleColumnChildView : contentRowView;
            int finalHeight = titleColumnChildHeight > contentRowHeight ? titleColumnChildHeight : contentRowHeight;

            this.resizeChildHeight(childView, finalHeight);
        }

    }

    private void resizeChildHeight(View view, int height) {
        if (!(view instanceof LinearLayout)) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            params.height = height - (params.bottomMargin + params.topMargin);
            return;
        }

        LinearLayout viewGroup = (LinearLayout) view;
        int childCount = viewGroup.getChildCount();
        int tallestViewPosition = getTallestViewPosition(viewGroup);
        for (int i = 0; i < childCount; i++) {
            View childView = viewGroup.getChildAt(i);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) childView.getLayoutParams();
            if (tallestViewPosition != i) {
                params.height = height - (params.bottomMargin + params.topMargin);
                return;
            }
        }
    }

    private int getTallestViewPosition(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        int maxHeightViewPosition = -1;
        int maxHeight = 0;

        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            int height = measureHeight(view);

            if (maxHeight < height) {
                maxHeightViewPosition = i;
                maxHeight = height;
            }
        }
        return maxHeightViewPosition;
    }

    private void fillTitleRow() {
        for (String columnKey : mColumnTitles) {
            TextView textView = new TextView(getContext());
            textView.setText(columnKey);
            textView.setBackgroundColor(Color.LTGRAY);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(50, 50, 50, 50);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            params.setMargins(2, 0, 0, 0);
            mTitleRowLayout.addView(textView, params);
        }
    }

    private void fillFirstCellView() {
        TextView textView = new TextView(getContext());
        textView.setBackgroundColor(Color.LTGRAY);
        textView.setText(mFirstCellText);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(50, 50, 50, 50);
        mFirstCellLayout.addView(textView);
    }

    class HorScrollView extends HorizontalScrollView {

        public HorScrollView(Context context) {
            super(context);
            setHorizontalScrollBarEnabled(false);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldL, int oldT) {
            if (this == mTitleRowScroll) {
                mContentInnerHorScroll.scrollTo(l, 0);
            } else {
                mTitleRowScroll.scrollTo(l, 0);
            }
        }

    }

    class VerScrollView extends ScrollView {

        public VerScrollView(Context context) {
            super(context);
            setVerticalScrollBarEnabled(false);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldL, int oldT) {
            if (this == mTitleColumnScroll) {
                mContentScroll.scrollTo(0, t);
            } else {
                mTitleColumnScroll.scrollTo(0, t);
            }
        }
    }

    private static int measureHeight(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredHeight();
    }

    private static int measureWidth(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredWidth();
    }
}
