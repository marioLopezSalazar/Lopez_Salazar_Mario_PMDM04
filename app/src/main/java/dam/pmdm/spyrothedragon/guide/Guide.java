package dam.pmdm.spyrothedragon.guide;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.Objects;
import java.util.Random;

import dam.pmdm.spyrothedragon.MainActivity;
import dam.pmdm.spyrothedragon.R;
import dam.pmdm.spyrothedragon.databinding.GuideBinding;


/**
 * Performs a generic fragment of the app's guide.
 * Provides several methods to animate several elements in the guide and to play sounds. It also receives buttons user events.
 *
 * @author Mario López Salazar
 */
public abstract class Guide extends Fragment {

    GuideBinding binding;

    /**
     * Creates the view, animates Spyro picture and sets up button listeners.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The performed view.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = GuideBinding.inflate(inflater, container, false);
        Objects.requireNonNull(binding.guideFlying.flying).post(this::animateSpyro);
        binding.guideNavigation.btnSkip.setOnClickListener(this::skip);
        Objects.requireNonNull(binding.guideNext.btnNext).setOnClickListener(this::next);
        animateNext();

        return binding.getRoot();
    }


    /**
     * Manages the "skip button" user event.
     * It delegates the action to the Main Activity.
     *
     * @param view The current view.
     */
    private void skip(View view) {
        ((MainActivity) requireActivity()).endGuide();
    }


    /**
     * Manages the "next button" user event.
     * It delegates the action to the Main Activity.
     *
     * @param view The current view.
     */
    private void next(View view) {
        ((MainActivity) requireActivity()).nextGuide(view);
    }


    /**
     * Establishes an opaque purple background (used in the first and last stages of the guide).
     */
    void opaqueBackground() {
        binding.getRoot().setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.purple));
    }


    /**
     * Establishes a gradient half-transparent purple background (used in the BottomNavigationView stages of the guide).
     */
    void gradientBackground() {
        binding.getRoot().setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.gradient_background));
    }


    /**
     * Establishes a gradient half-transparent purple background (used in the ToolBar Menu stages of the guide).
     */
    void gradientReverseBackground() {
        binding.getRoot().setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.gradient_background_reverse));
    }


    /**
     * Performs a left-right reverse of the Spyro picture.
     */
    void reverseSpyro() {
        Objects.requireNonNull(binding.guideFlying.flying).setScaleX(-1);
    }


    /**
     * Animates the picture of Spyro.
     * Performs an infinite random translation.
     */
    private void animateSpyro() {
        ImageView spyro = Objects.requireNonNull(binding.guideFlying.flying);

        // Maximum translation limits:
        int MAX_X = spyro.getWidth() / 4;
        int MAX_Y = spyro.getHeight() / 3;

        // If not visible:
        if (MAX_X == 0 || MAX_Y == 0)
            return;

        // Random translation distance:
        Random random = new Random();
        float newX = random.nextInt(MAX_X);
        float newY = random.nextInt(MAX_Y);

        // Performing the animation:
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(spyro, View.X, newX);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(spyro, View.Y, newY);
        animatorX.setDuration(1000);
        animatorY.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorX, animatorY);
        animatorSet.start();

        // New random translation:
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animateSpyro();
            }
        });
    }


    /**
     * Animates the pictures of a gem and a dragon egg.
     * Simulates a rotation of these elements.
     */
    void animateGemAndEgg(ImageView imageView) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, -1f);
        animator.setDuration(500);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.setRepeatMode(ObjectAnimator.REVERSE);
        animator.start();
    }


    /**
     * Animates the "next" button.
     * Performs a pulse and a rotation.
     */
    void animateNext() {
        Button next = binding.guideNext.btnNext;

        // Performing button scale:
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(next, "scaleX", 1f, 0.9f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(next, "scaleY", 1f, 0.9f);
        scaleX.setRepeatMode(ValueAnimator.REVERSE);
        scaleY.setRepeatMode(ValueAnimator.REVERSE);
        scaleX.setRepeatCount(ValueAnimator.INFINITE);
        scaleY.setRepeatCount(ValueAnimator.INFINITE);
        AnimatorSet animatorSetNext = new AnimatorSet();
        animatorSetNext.playTogether(scaleX, scaleY);
        animatorSetNext.setDuration(500);
        animatorSetNext.setStartDelay(500);
        animatorSetNext.start();

        // Performing button rotation:
        ObjectAnimator rotate = ObjectAnimator.ofFloat(next, "rotation", -5f, 5f);
        rotate.setRepeatMode(ValueAnimator.REVERSE);
        rotate.setRepeatCount(ValueAnimator.INFINITE);
        rotate.setDuration(1000);
        rotate.start();
    }


    /**
     * Animates the guide information elements (ring and bubble) in the intermediate stages of the guide.
     *
     * @param panel       Information panel (i.e. info_tabs_panel or info_menu_panel).
     * @param ring        Drawable which indicates the app option.
     * @param bubble      TextView which explains the app option.
     * @param coordX      Coordinate X where the app option is displayed.
     * @param coordY      Coordinate Y where the app option is displayed.
     * @param sizeX       Width of the app option.
     * @param sizeY       Height of the app option.
     * @param bubbleOnTop Indicates if the bubble must be placed on the top/bottom of the ring.
     */
    void infoAnimation(View panel, ImageView ring, TextView bubble, float coordX, float coordY, int sizeX, int sizeY, boolean bubbleOnTop) {

        // Performing ring size (according to the app option size):
        ViewGroup.LayoutParams params = ring.getLayoutParams();
        params.width = sizeX;
        params.height = sizeX;
        ring.setLayoutParams(params);

        // Performing ring place (according to the app option place):
        float ringMovementX = coordX - sizeX / 2f;
        float ringMovementY = coordY - sizeX / 2f;
        ObjectAnimator animRingX = ObjectAnimator.ofFloat(ring, "translationX", ringMovementX);
        ObjectAnimator animRingY = ObjectAnimator.ofFloat(ring, "translationY", ringMovementY);
        animRingX.setDuration(1000);
        animRingY.setDuration(1000);
        AccelerateDecelerateInterpolator interpolatorPlaceRing = new AccelerateDecelerateInterpolator();
        animRingX.setInterpolator(interpolatorPlaceRing);
        animRingY.setInterpolator(interpolatorPlaceRing);

        // Performing bubble place (according to the ring place but into the screen limits):
        float bubbleMovementX =
                Math.max(0f,
                        Math.min(panel.getWidth() - bubble.getWidth(),
                                bubbleOnTop ?
                                        ringMovementX + sizeX / 2f - bubble.getWidth() / 2f :
                                        ringMovementX + sizeX / 2f - bubble.getWidth()));
        float bubbleMovementY =
                bubbleOnTop ? ringMovementY - bubble.getHeight() : ringMovementY + sizeY;
        ObjectAnimator animBubbleX = ObjectAnimator.ofFloat(bubble, "translationX", bubbleMovementX);
        ObjectAnimator animBubbleY = ObjectAnimator.ofFloat(bubble, "translationY", bubbleMovementY);
        animBubbleX.setDuration(1000);
        animBubbleY.setDuration(1000);

        // Performing bubble appearing:
        bubble.setAlpha(0f);
        ObjectAnimator bubbleAppearing = ObjectAnimator.ofFloat(bubble, "alpha", 1f);
        bubbleAppearing.setDuration(500);
        bubbleAppearing.setInterpolator(new LinearInterpolator());

        // Performing bubble scale:
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(bubble, "scaleX", 1f, 0.9f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(bubble, "scaleY", 1f, 0.9f);
        scaleX.setRepeatMode(ValueAnimator.REVERSE);
        scaleY.setRepeatMode(ValueAnimator.REVERSE);
        scaleX.setRepeatCount(ValueAnimator.INFINITE);
        scaleY.setRepeatCount(ValueAnimator.INFINITE);
        AnimatorSet animatorSetBubbleScale = new AnimatorSet();
        animatorSetBubbleScale.playTogether(scaleX, scaleY);
        animatorSetBubbleScale.setDuration(500);

        // Launching animations:
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animRingX, animRingY, animBubbleX, animBubbleY);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                bubbleAppearing.start();
                animatorSetBubbleScale.start();
            }
        });
        animatorSet.start();
    }


    /**
     * Plays a sound stored in res/raw.
     *
     * @param sound Sound resource Id to play.
     */
    void playSound(int sound) {
        SoundPool soundPool = new SoundPool.Builder().setMaxStreams(1).build();
        int soundId = soundPool.load(requireContext(), sound, 1);

        soundPool.setOnLoadCompleteListener((soundPool1, sampleId, status) -> {
            if (status == 0)
                soundPool1.play(soundId, 1f, 1f, 0, 0, 1f);
        });
    }

}
