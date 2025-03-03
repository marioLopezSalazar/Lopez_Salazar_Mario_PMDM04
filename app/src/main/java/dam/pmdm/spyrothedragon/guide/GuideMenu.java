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
 * Performs a fragment of the app's guide which explains a ToolBar Menu option.
 *
 * @author Mario López Salazar
 */
public class GuideMenu extends Guide {

    private Bundle bundle;
    private int numButton;
    private float buttonX;
    private float buttonY;
    private int buttonSizeX;
    private int buttonSizeY;
    private TextView bubble;

    /**
     * Performs a stage of the app's guide which explains one of the ToolBar Menu option.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The performed view.
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Getting fragment arguments:
        bundle = getArguments();
        if (bundle == null)
            return binding.getRoot();

        // Getting the tab number (0:Info - 1:Guide):
        numButton = bundle.getInt("numButton");

        // Setting up the guide:
        binding.infoMenuPanel.getRoot().setVisibility(View.VISIBLE);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Objects.requireNonNull(binding.panelNavigation).setOrientation(LinearLayout.HORIZONTAL);
            Objects.requireNonNull(binding.guideNavigation.navigationBar).setOrientation(LinearLayout.HORIZONTAL);
            Objects.requireNonNull(binding.guideFlying.flying).setVisibility(View.GONE);
        }
        gradientReverseBackground();
        switch (numButton) {
            case 0:
                binding.guideNavigation.radioInfo.setChecked(true);
                break;
            case 1:
                binding.guideNavigation.radioGuide.setChecked(true);
                break;
        }
        bubble = binding.infoMenuPanel.bubble;
        switch (numButton) {
            case 0:
                bubble.setText(R.string.guide_info_button);
                break;
            case 1:
                bubble.setText(R.string.guide_guide_button);
                break;
        }

        return binding.getRoot();
    }


    /**
     * Performs the animations and sound of the stages of the guide relatives to the ToolBar menu.
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
                bundle = ((MainActivity) requireActivity()).getToolBarMenuCoordinates();

            // Performing the animations:
            FrameLayout panel = binding.infoMenuPanel.getRoot();
            panel.post(() -> {
                computeCoordinates();
                infoAnimation(panel, binding.infoMenuPanel.ring, bubble,
                        buttonX, buttonY, buttonSizeX, buttonSizeY,
                        false);
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
        FrameLayout infoMenuPanel = binding.infoMenuPanel.getRoot();
        int[] panelCoordinates = new int[2];
        infoMenuPanel.getLocationOnScreen(panelCoordinates);

        // Coordinates of the ToolBar Menu button:
        float buttonCoordinateX = bundle.getFloat("showInfoX");
        switch (numButton) {
            case 0:
                buttonCoordinateX = bundle.getFloat("showInfoX");
                break;
            case 1:
                buttonCoordinateX = bundle.getFloat("showGuideX");
                break;
        }

        // Coordinates of the ToolBar Menu button saw from the info panel:
        buttonX = buttonCoordinateX - panelCoordinates[0];
        buttonY = bundle.getFloat("toolBarMenuButtonsY") - panelCoordinates[1];

        // Size of the ToolBar Menu button:
        buttonSizeX = bundle.getInt("menuSizeX");
        buttonSizeY = bundle.getInt("menuSizeY");
    }

}