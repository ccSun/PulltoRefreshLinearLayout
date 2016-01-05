package com.nsnv.mlib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

/**
 * use function setState***() to change state.
 */

public class MProgressCircle extends View {

    private Paint paintDraw;

    private final int DEF_ARROW_HEIGHT = 100;
    private int arrowHeight;
    private int paintWidth;
    private int arrowLength;
    private RectF rectFArc;

    private float MAX_SWEEP_ANGLE = 360 / 1.5f;

    //
    public enum STATE{
        ARROW_DOWN(1),
        ARROW_UP(2),
        LOADING(3),
        SUCCESS(4),
        FAIL(5);

        private int VALUE;

         STATE(int i) {
             VALUE = i;
        }

    }
    private int state = STATE.ARROW_DOWN.VALUE;

    private float fractionArrowLength = 1;
    private float fractionArcAngle = 0;

    public MProgressCircle(Context context) {
        super(context);
        init(context);

    }

    public MProgressCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MProgressCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        initDefSetting();

    }

    private void initDefSetting(){

        paintWidth = arrowHeight / 12;
        arrowLength = arrowHeight / 3 + paintWidth /2;

        rectFArc = new RectF(paintWidth, paintWidth, arrowHeight - paintWidth, arrowHeight - paintWidth);

        paintDraw = new Paint();
        paintDraw.setAntiAlias(true);
        paintDraw.setColor(Color.BLUE);
        paintDraw.setStyle(Paint.Style.STROKE);
        paintDraw.setStrokeWidth(paintWidth > 20 ? 20 : paintWidth);
        paintDraw.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if(MeasureSpec.EXACTLY == widthMode || MeasureSpec.EXACTLY == heightMode){
            arrowHeight = widthSize > heightSize ? heightSize:widthSize;
        }else{
            arrowHeight = DEF_ARROW_HEIGHT;
        }
        widthSize = heightSize = arrowHeight;

        initDefSetting();
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float startX;
        float startY;
        float stopX;
        float stopY;

        if(STATE.ARROW_DOWN.VALUE == state) {

            // clear up
            startX = arrowHeight / 2 - arrowLength * (1 - fractionArrowLength);
            startY = paintWidth + arrowLength * (1 - fractionArrowLength);
            stopX = arrowHeight / 2;
            stopY = paintWidth;
            canvas.drawLine(startX, startY, stopX, stopY, paintDraw);
            startX = arrowHeight / 2 + arrowLength * (1 - fractionArrowLength);
            canvas.drawLine(startX, startY, stopX, stopY, paintDraw);

            // draw down
            startX = arrowHeight / 2;
            startY = arrowHeight - paintWidth;
            stopX = startX;
            stopY = paintWidth;
            canvas.drawLine(startX, startY, stopX, stopY, paintDraw);

            stopX = startX - arrowLength * fractionArrowLength;
            stopY = startY - arrowLength * fractionArrowLength;
            canvas.drawLine(startX, startY, stopX, stopY, paintDraw);

            stopX = startX + arrowLength * fractionArrowLength;
            canvas.drawLine(startX, startY, stopX, stopY, paintDraw);

        }else if(STATE.ARROW_UP.VALUE == state) {

            // clear down
            startX = arrowHeight / 2 - arrowLength * (1 - fractionArrowLength);
            startY = arrowHeight - paintWidth - arrowLength * (1 - fractionArrowLength);
            stopX = arrowHeight / 2;
            stopY = arrowHeight - paintWidth;
            canvas.drawLine(startX, startY, stopX, stopY, paintDraw);
            startX = arrowHeight / 2 + arrowLength * (1 - fractionArrowLength);
            canvas.drawLine(startX, startY, stopX, stopY, paintDraw);

            // draw up
            startX = arrowHeight / 2;
            startY = paintWidth;
            stopX = startX;
            stopY = arrowHeight - paintWidth;
            canvas.drawLine(startX, startY, stopX, stopY, paintDraw);

            stopX = startX - arrowLength * fractionArrowLength;
            stopY = startY + arrowLength * fractionArrowLength;
            canvas.drawLine(startX, startY, stopX, stopY, paintDraw);

            stopX = startX + arrowLength * fractionArrowLength;
            canvas.drawLine(startX, startY, stopX, stopY, paintDraw);

        }else if(STATE.LOADING.VALUE == state) {

            float ratio = arrowLength / (arrowLength + arrowHeight);
            if(fractionArrowLength < ratio) {

                // clear up
                startX = arrowLength / ratio * fractionArrowLength + (arrowHeight / 2 - arrowLength);
                startY = -arrowLength / ratio * fractionArrowLength + (paintWidth + arrowLength);
                stopX = arrowHeight / 2;
                stopY = paintWidth;
                canvas.drawLine(startX, startY, stopX, stopY, paintDraw);
                startX = -arrowLength / ratio * fractionArrowLength + (arrowHeight / 2 + arrowLength);
                canvas.drawLine(startX, startY, stopX, stopY, paintDraw);

                startX = stopX;
                startY = arrowHeight - paintWidth;
                canvas.drawLine(startX, startY, stopX, stopY, paintDraw);
            }else if(fractionArrowLength < 1){

                startX = arrowHeight / 2;
                startY = (arrowHeight - paintWidth - paintWidth) / (1 - ratio) * fractionArrowLength + (paintWidth - (arrowHeight - paintWidth) * ratio) / (1 - ratio);
                stopX = startX;
                stopY = arrowHeight - paintWidth;
                canvas.drawLine(startX, startY, stopX, stopY, paintDraw);

                paintDraw.setStyle(Paint.Style.STROKE);
//                float sweepAngle = (float) ((360/Math.PI)/(1-ratio)*fractionArrowLength - (360/Math.PI)/(1-ratio)*ratio);
                float sweepAngle = MAX_SWEEP_ANGLE/(1-ratio)*fractionArrowLength - MAX_SWEEP_ANGLE/(1-ratio)*ratio;
                canvas.drawArc(rectFArc, 90, sweepAngle, false, paintDraw);
                paintDraw.setStyle(Paint.Style.FILL);
            }else { //if(fractionArrowLength == 1)


                paintDraw.setStyle(Paint.Style.STROKE);
                float startAngle = 360 * fractionArcAngle + 90;
                canvas.drawArc(rectFArc, startAngle, MAX_SWEEP_ANGLE, false, paintDraw);
                paintDraw.setStyle(Paint.Style.FILL);
            }
        }else if(STATE.SUCCESS.VALUE == state){

            paintDraw.setStyle(Paint.Style.STROKE);
            canvas.drawArc(rectFArc, 0, 360, false, paintDraw);
            paintDraw.setStyle(Paint.Style.FILL);

            float ratio = 0.4f;
            if(fractionArrowLength < ratio){

                startX = arrowHeight / 2 - 2 * paintWidth;
                startY = arrowHeight / 2;
                stopX = arrowHeight / 2 ;
                stopY = (startY + 2 * paintWidth);
                stopX = (stopX - startX) / ratio * fractionArrowLength + startX;
                stopY = (stopY - startY) / ratio * fractionArrowLength + startY;

                canvas.drawLine(startX, startY, stopX, stopY, paintDraw);
            }else{

                startX = arrowHeight / 2 - 2 * paintWidth;
                startY = arrowHeight / 2;
                stopX = arrowHeight / 2;
                stopY = startY + 2 * paintWidth;
                canvas.drawLine(startX, startY, stopX, stopY, paintDraw);

                startX = stopX;
                startY = stopY;
                stopX = arrowHeight / 2 + 2 * paintWidth;
                stopY = arrowHeight /2 - 2* paintWidth;
                stopX = (stopX - startX) / (1 - ratio) * (fractionArrowLength - ratio) + startX;
                stopY = (stopY - startY) / (1 - ratio) * (fractionArrowLength - ratio) + startY;
                canvas.drawLine(startX, startY, stopX, stopY, paintDraw);
            }


        }else if(STATE.FAIL.VALUE == state){

            paintDraw.setStyle(Paint.Style.STROKE);
            canvas.drawArc(rectFArc, 0, 360, false, paintDraw);
            paintDraw.setStyle(Paint.Style.FILL);

            float ratio = 0.5f;
            if(fractionArrowLength < ratio) {

                startX = arrowHeight / 2;
                startY = arrowHeight / 2 - 2 * paintWidth;
                stopX = startX;
                stopY = arrowHeight /2 + paintWidth;

                stopY = (stopY - startY) / ratio * fractionArrowLength + startY;
                canvas.drawLine(startX, startY, stopX, stopY, paintDraw);

            }else{

                startX = arrowHeight / 2;
                startY = arrowHeight / 2 - 2 * paintWidth;
                stopX = startX;
                stopY = arrowHeight /2 + paintWidth;
                canvas.drawLine(startX, startY, stopX, stopY, paintDraw);

                canvas.drawPoint(stopX, stopY + paintWidth + paintWidth * 0.5f, paintDraw);
            }


        }
    }


    public void setStateArrowDown(){
        if(STATE.ARROW_DOWN.VALUE != state){

            state = STATE.ARROW_DOWN.VALUE;
            getAnimArrowLength().start();
        }
    }

    public void setStateArrowUp(){
        if(STATE.ARROW_UP.VALUE != state){

            state = STATE.ARROW_UP.VALUE;
            getAnimArrowLength().start();
        }
    }

    public void setStateLoading(){
        if(STATE.LOADING.VALUE != state){

            state = STATE.LOADING.VALUE;
            getAnimArrowLength().addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    animation.removeAllListeners();
                    getAnimArcAngle().setRepeatCount(Animation.INFINITE);
                    getAnimArcAngle().setRepeatMode(ValueAnimator.RESTART);
                    getAnimArcAngle().start();
                    super.onAnimationEnd(animation);
                }
            });
            getAnimArrowLength().start();
        }
    }

    public void setStateSuccess(){
        if( STATE.SUCCESS.VALUE != state){

            state = STATE.SUCCESS.VALUE;
            getAnimArcAngle().cancel();
            getAnimArrowLength().start();
        }
    }

    public void setStateFail(){
        if( STATE.FAIL.VALUE != state){

            state = STATE.FAIL.VALUE;
            getAnimArrowLength().start();;
        }
    }

    private ObjectAnimator animatorArrowLength;
    private ObjectAnimator getAnimArrowLength(){
        if(null == animatorArrowLength) {
            animatorArrowLength = ObjectAnimator.ofFloat(this, "fractionArrowLength", 0, 1);
            animatorArrowLength.setDuration(500);
            animatorArrowLength.setInterpolator(new AccelerateDecelerateInterpolator());
        }
        return animatorArrowLength;
    }

    public void setFractionArrowLength(float fractionArrowLength) {
        this.fractionArrowLength = fractionArrowLength;
        invalidate();
    }

    private ObjectAnimator animatorArcAngle;
    private ObjectAnimator getAnimArcAngle(){
        if(null == animatorArcAngle) {
            animatorArcAngle = ObjectAnimator.ofFloat(this, "fractionArcAngle", 0, 1);
            animatorArcAngle.setDuration(800);
            animatorArcAngle.setInterpolator(new LinearInterpolator());
        }
        return animatorArcAngle;
    }

    public void setFractionArcAngle(float fractionArcAngle) {
        this.fractionArcAngle = fractionArcAngle;
        invalidate();
    }
}
