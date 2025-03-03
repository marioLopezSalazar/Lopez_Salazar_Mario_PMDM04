package dam.pmdm.spyrothedragon.guide;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

import dam.pmdm.spyrothedragon.MainActivity;
import dam.pmdm.spyrothedragon.R;


/**
 * Performs a fragment of the app's guide which explains a ButtonNavigationView option.
 *
 * @author Mario López Salazar
 */
public class GuideTabs extends Guide {

    private Bundle bundle;
    private int numTab;
    private float tabX;
    private float tabY;
    private int buttonSizeX;
    private int buttonSizeY;
    private TextView bubble;


    /**
     * Performs a stage of the app's guide which explains one of the ButtonNavigationView option.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The performed view.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Getting fragment arguments:
        bundle = getArguments();
        if (bundle == null)
            return binding.getRoot();

        // Getting the tab number (0:Characters - 1:Worlds - 2:Collectibles):
        numTab = bundle.getInt("numTab");

        // Setting up the guide:
        binding.infoTabsPanel.getRoot().setVisibility(View.VISIBLE);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Objects.requireNonNull(binding.panelNavigation).setOrientation(LinearLayout.HORIZONTAL);
            Objects.requireNonNull(binding.guideNavigation.navigationBar).setOrientation(LinearLayout.HORIZONTAL);
            Objects.requireNonNull(binding.guideFlying.flying).setVisibility(View.GONE);
        }
        gradientBackground();
        switch (numTab) {
            case 0:
                binding.guideNavigation.radioCharacters.setChecked(true);
                break;
            case 1:
                binding.guideNavigation.radioWorlds.setChecked(true);
                break;
            case 2:
                binding.guideNavigation.radioCollectibles.setChecked(true);
                break;
        }
        bubble = binding.infoTabsPanel.bubble;
        switch (numTab) {
            case 0:
                bubble.setText(R.string.guide_characters);
                break;
            case 1:
                bubble.setText(R.string.guide_worlds);
                break;
            case 2:
                bubble.setText(R.string.guide_collectibles);
                break;
        }

        return binding.getRoot();
    }


    /**
     * Performs the animations and sound of the stages of the guide relatives to the BottomNavigationView.
     *
     * @param view               The View returned by onCreateView().
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Waiting up to the MainActivity layout is completely drawn:
        ((MainActivity) requireActivity()).layoutReady(() -> {

            // Refreshing coordinates when fragment re-created:
            if (savedInstanceState != null)
                bundle = ((MainActivity) requireActivity()).getBottomNavigationViewCoordinates();

            // Performing the animations:
            FrameLayout panel = binding.infoTabsPanel.getRoot();
            panel.post(() -> {
                computeCoordinates();
                infoAnimation(panel, binding.infoTabsPanel.ring, bubble,
                        tabX, tabY, buttonSizeX, buttonSizeY,
                        true);
            });

            // Playing sound when fragment is not being recreated:
            if (savedInstanceState == null)
                playSound(R.raw.guide_changestage);
        });
    }


    /**
     * Calculates the coordinates to locate the ring and the bubble, depending on the position of the info panel in the screen.
     */
    private void computeCoordinates() {

        // Coordinates of the info panel:
        FrameLayout infoTabsPanel = binding.infoTabsPanel.getRoot();
        int[] panelCoordinates = new int[2];
        infoTabsPanel.getLocationOnScreen(panelCoordinates);

        // Coordinates of the BottomNavigationView button:
        float buttonCoordinateX = 0;
        switch (numTab) {
            case 0:
                buttonCoordinateX = bundle.getFloat("charactersX");
                break;
            case 1:
                buttonCoordinateX = bundle.getFloat("worldsX");
                break;
            case 2:
                buttonCoordinateX = bundle.getFloat("collectiblesX");
                break;
        }

        // Coordinates of the BottomNavigationView button saw from the info panel:
        tabX = buttonCoordinateX - panelCoordinates[0];
        tabY = bundle.getFloat("bottomNavigationButtonsY") - panelCoordinates[1];

        // Size of the BottomNavigationView button:
        buttonSizeX = bundle.getInt("tabSizeX");
        buttonSizeY = bundle.getInt("tabSizeY");
    }

}