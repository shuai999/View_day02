package com.jack.view_day02;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Email 2185134304@qq.com
 * Created by JackChen on 2018/1/20.
 * Version 1.0
 * Description:  自定义View
 */
public class TextView extends View {
//View  LinearLayout

    //如果让TextView继承 LinearLayout，为了让自定义的TextView的文字显示出来 ， 可以用如下3种方法实现


    private String mText ;
    private int mTextSize = 15 ; //默认是15sp
    private int mTextColor = Color.BLACK ; // 默认给文字设置颜色为黑色

    private Paint mPaint ; //初始化画笔 用画笔去画文字


    //和系统一样，将此构造方法的super(context)改为this(context , null)，默认会调用第二个构造方法
    //此方法会在代码中new的时候调用
    //TextView tv = new TextView(this)
    public TextView(Context context) {
        this(context , null);
    }


    //和系统一样，将此构造方法的super(context,attrs)改为this(context , attrs , 0)，默认会调用第三个构造方法
    //这样的话，无论你写的是哪个构造方法，都会默认去调用第三个构造发方法
    //此方法在布局文件Layout中使用
    /*<com.jack.view.MyTextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="我爱王子文"
            />*/
    public TextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    //此方法在布局文件Layout中使用  但是会有style
    public TextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextView);
        mText = typedArray.getString(R.styleable.TextView_jackText) ;
        mTextColor = typedArray.getColor(R.styleable.TextView_jackTextColor, mTextColor);
        //15sp
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.TextView_jackTextSize , sp2px(mTextSize)) ; //mTextSize

        typedArray.recycle();


        mPaint = new Paint() ;
        mPaint.setAntiAlias(true); //设置抗锯齿，可以让文字比较清晰，同时文字也会比较圆滑的
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);


//        // 方法二：默认给一个背景 --> 前提是人家在布局文件中没有设置background时你才可以在这里设置background背景
//        setBackgroundColor(Color.TRANSPARENT);
        //方法三：默认让它画
//        setWillNotDraw(false);


    }


    /**
     *  sp转为px
     * @param sp
     * @return
     */
    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getResources().getDisplayMetrics());
    }



    /**
     *  自定义View的测量方法
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //获取宽高的模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec) ;
        int heightMode = MeasureSpec.getMode(heightMeasureSpec) ;


        //1.如果在布局中你设置文字的宽高是固定值[如100dp、200dp]，就不需要计算, 直接获取宽和高就可以
        int width = MeasureSpec.getSize(widthMeasureSpec);

        //1.如果在布局中你设置文字的宽高是wrap_content[对应MeasureSpec.AT_MOST] , 则需要使用模式来计算
        if (widthMode == MeasureSpec.AT_MOST){
            //计算的宽度 与字体的大小和长度有关 用画笔来测量
            Rect bounds = new Rect() ;
            //获取文本的Rect [区域]
            //参数一：要测量的文字、参数二：从位置0开始、参数三：到文字的长度、参数四：
            mPaint.getTextBounds(mText , 0 , mText.length() , bounds);

            //文字的宽度  getPaddingLeft表示宽度 getPaddingRight表示高度 --> 写这两个作用就是为了你在布局文件中设置padding可以起作用
            width = bounds.width() + getPaddingLeft() + getPaddingRight() ;
        }



        int height = MeasureSpec.getSize(heightMeasureSpec);
        //1.如果在布局中你设置文字的宽高是wrap_content[对应MeasureSpec.AT_MOST] , 则需要使用模式来计算
        if (heightMode == MeasureSpec.AT_MOST){
            //计算的宽度 与字体的大小和长度有关 用画笔来测量
            Rect bounds = new Rect() ;
            //获取文本的Rect [区域]
            //参数一：要测量的文字、参数二：从位置0开始、参数三：到文字的长度、参数四：
            mPaint.getTextBounds(mText , 0 , mText.length() , bounds);

            //文字的高度  getPaddingTop表示宽度 getPaddingBottom表示高度 --> 写这两个作用就是为了你在布局文件中设置padding可以起作用
            height = bounds.width()  + getPaddingTop() + getPaddingBottom() ;
        }


        //设置文字控件的宽和高
        setMeasuredDimension(width , height);


    }


    /**
     * 用于绘制
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*//绘制文字
        canvas.drawText();
        //绘制弧
        canvas.drawArc();
        //绘制圆
        canvas.drawCircle();*/

        //画文字 text
        // 参数一：要画的文字
        // 参数二：x就是开始的位置 从0开始
        // 参数三：y基线baseLine
        // 参数四：画笔mPaint

        //dy: 代表的是：高度的一半到baseLine的距离
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt() ;
        //top是负值 bottom是正值   bottom代表的是baseLine到文字底部的距离
        int dy = (fontMetrics.bottom - fontMetrics.top) /2 - fontMetrics.bottom ;

        int baseLine = getHeight() /2 + dy ;

        int x = getPaddingLeft() ;

        canvas.drawText(mText, x, baseLine, mPaint);
    }


    /**
     * 在布局文件中不设置 background，我也想让自定义的TextView显示出来，该如何实现 ？
     *     方法1 -->  直接将onDraw()方法替换成 下边的dispatchDraw()即可
     *     方法2 -->  在第三个构造方法中的 直接设置背景即可
     */
//    /*
//     *   方法一
//         用于绘制
//     * @param canvas
//     */
//    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        super.dispatchDraw(canvas);
//        /*//绘制文字
//        canvas.drawText();
//        //绘制弧
//        canvas.drawArc();
//        //绘制圆
//        canvas.drawCircle();*/
//
//        //画文字 text
//        // 参数一：要画的文字
//        // 参数二：x就是开始的位置 从0开始
//        // 参数三：y基线baseLine
//        // 参数四：画笔mPaint
//
//        //dy: 代表的是：高度的一半到baseLine的距离
//        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt() ;
//        //top是负值 bottom是正值   bottom代表的是baseLine到文字底部的距离
//        int dy = (fontMetrics.bottom - fontMetrics.top) /2 - fontMetrics.bottom ;
//
//        int baseLine = getHeight() /2 + dy ;
//
//        int x = getPaddingLeft() ;
//
//        canvas.drawText(mText , x,baseLine , mPaint);
//    }


    /**
     * 处理跟用户交互的、手指触摸等等
     * @param event  事件分发、事件拦截
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN :
                 //手指按下
                 Log.e("TAG", "手指按下") ;
                 break;
            case MotionEvent.ACTION_MOVE :
                 //手指移动
                 Log.e("TAG", "手指移动") ;
                 break;
            case MotionEvent.ACTION_UP :
                 //手指抬起
                 Log.e("TAG", "手指抬起") ;
                 break;
        }

        invalidate();
        return super.onTouchEvent(event);
    }
}
