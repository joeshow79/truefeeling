package com.tf.truefeeling.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tf.truefeeling.R;

/**
 * Created by Guest on 2016/4/30.
 */
public class CusSetMonitorDistanceBar extends LinearLayout {

    public final static int LEFT_BUTTON = 0;
    public final static int MIDDLE_BUTTON = 1;
    public final static int RIGHT_BUTTON = 2;

    private OnItemClickListener myOnItemClickListener;
    private String mLeftText, mRightText;
    private int mLeftColor, mRightColor;
    private String myStyle;

    private Button leftBtn;
    private Button rightBtn;

    /*
     * 定义接口，以便可以像普通button一样在用到的地方可以添加点击事件
     */
    public interface OnItemClickListener {
        public void onItemClickListener(int item);
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        myOnItemClickListener = l;
    }

    /*
     * 开始构造
     */
    public CusSetMonitorDistanceBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        /*
         * 在这里，我们让刚才定义的属性开始起作用哦。
		 */
        //	从values/attrs中“获得”刚才定义的东东CusSetMonitorDistanceBar，它里面包含了我们定义的属性。
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CusSetMonitorDistanceBar, 0, 0);
        //	从东东a中取出leftText属性，它返回一个String类型的数据，即左边按钮的文字。
        mLeftText = a.getString(R.styleable.CusSetMonitorDistanceBar_leftText);
        mRightText = a.getString(R.styleable.CusSetMonitorDistanceBar_rightText);
        mLeftColor = a.getColor(R.styleable.CusSetMonitorDistanceBar_leftTextColor, 0xff000000);
        mRightColor = a.getColor(R.styleable.CusSetMonitorDistanceBar_rightTextColor, 0xff000000);

        //	在values/dimens.xml中定义了button文字的大小，我们将它获取到，
        // 	当然，也可以不去dimens中定义，直接在这里定义，例如size＝20.0；
        //	当然，也可以在定义特有属性时将文字大小定义为一个特有属性，在布局时传入（就像button文字颜色那样）
        float size = getResources().getDimension(R.dimen.click_button_text_size);
        // LEFT，定义左边button
        leftBtn = new Button(context);
        leftBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);//	设置button文字大小
//    	设置左边button的样式，也就是它的形状，monitor_left_button_normal是在drawable文件中定义的xml文件，非常简单的哦，一会儿上代码
        leftBtn.setBackgroundResource(R.drawable.monitor_left_button_normal);
/*
 *   	设置左边button文字的颜色，这个颜色值是通过自定义属性传进来的，也就是说我们在布局时用custom定义的颜色值通过attrs传到上面a中，再从a中取出放在mLeftColor，最后在这里进行最后的设置。
 *   所有自定义的特有属性都是这个原理起作用的。
 */
        leftBtn.setTextColor(mLeftColor);
        leftBtn.setText(mLeftText);//同理mLeftColor
//    	我们这个view是继承LinearLayout的，设置Button在这个LinearLayout中如何分布，LinearLayout的大小是多少呢？就是布局时非custom属性中的android:layout_width和android:layout_height决定
        LinearLayout.LayoutParams lp_left = new LayoutParams(0, LayoutParams.MATCH_PARENT);//填满整个LinearLayout，LinearLayout默认为horizon布局
        lp_left.weight = 1;//	比重，我们后面还有一个右边Button，它也是比重为1，所以两个平分了整个LinearLayout
        addView(leftBtn, lp_left); //	将Button（leftBtn）按照lp_left定义的布局方式添加到LinearLayout中。
        //给左边button添加点击事件
        leftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myOnItemClickListener != null) {
                    myOnItemClickListener.onItemClickListener(LEFT_BUTTON);
                }
            }
        });
        /*
         * 右边同理，不再赘述
         */
        //RIGHT
        rightBtn.setBackgroundResource(R.drawable.monitor_right_button_normal);

        rightBtn.setTextColor(mRightColor);
        rightBtn.setText(mRightText);
        LinearLayout.LayoutParams lp_right = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        lp_right.leftMargin = -1;
        lp_right.weight = 1;
        addView(rightBtn, lp_right);
        rightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myOnItemClickListener != null) {
                    myOnItemClickListener.onItemClickListener(RIGHT_BUTTON);
                }
            }
        });
        a.recycle();
    }

    public void setLeftBtnColor(int color) {
        leftBtn.setTextColor(color);
    }

    public void setRightBtnColor(int color) {
        rightBtn.setTextColor(color);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (int i = 0; i < this.getChildCount(); i++) {
            View v = this.getChildAt(i);
            v.setEnabled(enabled);
        }
    }
}
