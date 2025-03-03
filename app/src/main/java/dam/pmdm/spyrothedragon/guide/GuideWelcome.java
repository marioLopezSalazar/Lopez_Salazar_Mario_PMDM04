package dam.pmdm.spyrothedragon.guide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

import dam.pmdm.spyrothedragon.R;


/**
 * Performs the welcome fragment of the app's guide.
 *
 * @author Mario López Salazar
 */
public class GuideWelcome extends Guide {

    /**
     * Performs the welcome stage of the guide.
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The performed view.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Setting up the guide:
        binding.welcomePanel.getRoot().setVisibility(View.VISIBLE);
        binding.guideNavigation.radioWelcome.setChecked(true);
        Objects.requireNonNull(binding.guideNext.btnNext).setText(R.string.start);
        opaqueBackground();

        return binding.getRoot();
    }


    /**
     * Performs the animations and sound of the welcome stage of the guide.
     *
     * @param view               The View returned by onCreateView().
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Performing the animations:
        reverseSpyro();
        animateGemAndEgg(binding.welcomePanel.decorationPanel.spyroGem);
        animateGemAndEgg(binding.welcomePanel.decorationPanel.spyroEgg);

        // Playing sound when fragment is not being recreated and the guide is just starting:
        if (savedInstanceState == null && getArguments() != null)
            playSound(R.raw.guide_start);
    }
}