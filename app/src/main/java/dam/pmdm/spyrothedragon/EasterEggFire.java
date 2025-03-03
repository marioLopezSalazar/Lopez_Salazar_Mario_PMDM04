package dam.pmdm.spyrothedragon;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.View;

import androidx.annotation.NonNull;

/**
 * Uses a Canvas object to perform a "flame" animation for an Spyro picture.
 *
 * @author Mario López Salazar
 */
@SuppressLint("ViewConstructor")
public class EasterEggFire extends View {

    int white, yellow, red, transparent;
    private Paint paint;
    private float x, y;
    private float size, maxSize;


    /**
     * Creates an EasterEggFire object.
     * Call method start() to launch the animation.
     *
     * @param context     The context in which the animation will be performed (e.g. MainActivity).
     * @param pictureInfo Indicates the top-left corner and the width-height size of the base Spyro picture.
     */
    public EasterEggFire(Context context, int[] pictureInfo) {
        super(context);
        calculateLimits(pictureInfo);
        createPaint();
    }


    /**
     * Calculates the point where the flame will start (e.g. Spyro mouth) and the size of the flame.
     *
     * @param pictureInfo Indicates the top-left corner and the width-height size of the base Spyro picture.
     */
    private void calculateLimits(int[] pictureInfo) {
        x = pictureInfo[0] + 0.53f * pictureInfo[2];
        y = pictureInfo[1] + 0.7f * pictureInfo[3];
        maxSize = pictureInfo[3] / 6f;
    }


    /**
     * Initializes the Paint object that will be used to paint the flame.
     */
    private void createPaint() {
        paint = new Paint();

        // Fill coloured:
        paint.setStyle(Paint.Style.FILL);

        // Soft borders:
        paint.setAntiAlias(true);

        // Getting colours:
        white = getContext().getColor(R.color.white);
        yellow = getContext().getColor(R.color.fireYellow);
        red = getContext().getColor(R.color.fireRed);
        transparent = getContext().getColor(R.color.transparent);
    }


    /**
     * Performs the flame animation, whose size increases up to the maximum size and finally fades out.
     *
     * @param listener It can be used to detect when the animation has finished.
     */
    public void start(AnimatorListenerAdapter listener) {

        // Animator to increase the size of the flame and redraw the Canvas:
        ValueAnimator animatorSize = ValueAnimator.ofFloat(1, maxSize);
        animatorSize.setDuration(700);
        animatorSize.addUpdateListener(animationSize -> {
            size = (float) animationSize.getAnimatedValue();
            invalidate();
        });

        // Launching the size-increasing animation:
        animatorSize.start();

        // When size-increasing ends, launch an Animator to fade out:
        animatorSize.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                // Animator to fade out:
                ValueAnimator animatorAlpha = ValueAnimator.ofFloat(1f, 0);
                animatorAlpha.setDuration(500);
                animatorAlpha.addUpdateListener(animationAlpha -> {
                    setAlpha((float) animationAlpha.getAnimatedValue());
                    invalidate();
                });

                // Launching the fade out animation:
                animatorAlpha.start();

                // Adding the global listener to detect the end of the animation:
                animatorAlpha.addListener(listener);
            }
        });
    }


    /**
     * Draws the flame into the Canvas.
     * Uses a Path object to define the flame form, and RadialGradient object to simulate fire colours.
     * Flame size is controlled by EasterEggFire size.
     * This method is automatically called when the EasterEggFire is created, and also when invalidate() method is called.
     *
     * @param canvas The Canvas where the flame is going to be drawn.
     */
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        // Creating flame contour using a Path:
        Path path = new Path();

        // Start on Spyro mouth:
        path.moveTo(x, y);

        // Descending left-hand side branch:
        path.cubicTo(
                x - size * 0.5f, y + size * 5,
                x - size * 1.1f, y + size * 6,
                x - size * 2.5f, y + size * 8);

        // Bottom arc (simulating a fire ball):
        path.arcTo(
                new RectF(
                        x - size * 3, y + size * 6.8f,
                        x + size * 3, y + size * 12),
                240,
                -280,
                false);

        // Ascending right-hand side branch:
        path.cubicTo(
                x + size * 1.1f, y + size * 6,
                x + size * 0.5f, y + size * 5,
                x, y);


        // Gradient to simulate fire colours:

        RadialGradient gradient = new RadialGradient(
                x, y + size * 9,
                size * 10,
                new int[]{white, yellow, red, transparent},
                new float[]{0.0f, 0.05f, 0.5f, 0.9f},
                Shader.TileMode.CLAMP);
        paint.setShader(gradient);

        // Drawing:
        canvas.drawPath(path, paint);
    }
}