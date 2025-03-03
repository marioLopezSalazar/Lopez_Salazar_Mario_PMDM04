package dam.pmdm.spyrothedragon;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.ImageView;
import android.widget.MediaController;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import java.util.Objects;

import dam.pmdm.spyrothedragon.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    NavController navController;
    NavController guideNavController;
    Bundle guideBundle;
    boolean showingGuide;
    boolean menuReady = false;
    boolean playingVideo;
    int videoPosition;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));

        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        if (navHostFragment != null) {
            navController = NavHostFragment.findNavController(navHostFragment);
            NavigationUI.setupWithNavController(binding.navView, navController);
            NavigationUI.setupActionBarWithNavController(this, navController);
        }

        binding.navView.setOnItemSelectedListener(this::selectedBottomMenu);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null)
                // Si se navega a una pantalla donde se desea mostrar la flecha de atrás, habilítala
                // Para las pantallas de los tabs, no queremos que aparezca la flecha de atrás
                actionBar.setDisplayHomeAsUpEnabled(
                        destination.getId() != R.id.navigation_characters &&
                                destination.getId() != R.id.navigation_worlds &&
                                destination.getId() != R.id.navigation_collectibles);
        });

        // Getting the Guide NavController:
        Fragment guideNavHostFragment = getSupportFragmentManager().findFragmentById(R.id.guideNavHostFragment);
        if (guideNavHostFragment == null)
            return;
        guideNavController = NavHostFragment.findNavController(guideNavHostFragment);

        // Managing back button:
        configureBackButton();
    }



    /**
     * - Launches the APP GUIDE if this is the first app running (using SharedPreferences).
     * - Recovers the Easter Egg video if it was being played when the activity went to background.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Checking if the guide has been shown some time:
        SharedPreferences sharedPreferences = getSharedPreferences("spyroPreferences", Context.MODE_PRIVATE);
        boolean guideShown = sharedPreferences.getBoolean("guideShown", false);

        // First app running:
        if (!guideShown) {
            // Showing the guide:
            startGuide();

            // Indicating that the guide has been already shown:
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("guideShown", true);
            editor.apply();
        }

        // Recovering if the video was being played when the activity went to background:
        if (playingVideo)
            playVideo(videoPosition);
    }



    /**
     * - Saves the state if the Easter Egg video is being played when the activity goes to background.
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Saving if the video is being played when the activity goes to background:
        if (playingVideo) {
            binding.videoView.pause();
            videoPosition = binding.videoView.getCurrentPosition();
        }
    }



    /**
     * - Saves the state if the device orientation changes, eiter while:
     * ---- The guide is visible, or
     * ---- the Easter Egg video is being played.
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Saving if the guide is visible:
        outState.putBoolean("showingGuideWhenDestroyed", showingGuide);

        // Saving if the video is being played:
        if (playingVideo)
            outState.putInt("videoPosition", videoPosition);
    }



    /**
     * - Recovers the state if the device orientation changes, eiter while:
     * ---- The guide is visible, or
     * ---- the Easter Egg video is being played.
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Recovering if the guide was visible:
        if (savedInstanceState.getBoolean("showingGuideWhenDestroyed", false)) {
            showingGuide = true;
            binding.guideNavHostFragment.setVisibility(View.VISIBLE);
        }

        // Recovering if the video was being played:
        videoPosition = savedInstanceState.getInt("videoPosition", -1);
        playingVideo = (videoPosition != -1);
    }



    private boolean selectedBottomMenu(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.navigation_characters)
            navController.navigate(R.id.navigation_characters);
        else if (menuItem.getItemId() == R.id.navigation_worlds)
            navController.navigate(R.id.navigation_worlds);
        else
            navController.navigate(R.id.navigation_collectibles);
        return true;

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menú
        getMenuInflater().inflate(R.menu.about_menu, menu);
        menuReady = true;
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Gestiona el clic en el ítem de información
        if (item.getItemId() == R.id.action_info) {
            showInfoDialog();  // Muestra el diálogo
            return true;
        }
        // Gestiona el clic en el ítem de mostrar de nuevo la guía de bienvenida
        if (item.getItemId() == R.id.show_guide) {
            startGuide();  // Inicia la guía
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    private void showInfoDialog() {
        // Crear un diálogo de información
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_about)
                .setMessage(R.string.text_about)
                .setPositiveButton(R.string.accept, null)
                .show();
    }



    /**
     * Allows to start the app guide.
     * - Launches the initialization of its configuration, and
     * - Navigates to the first stage of the guide.
     */
    void startGuide() {
        guideBundle = new Bundle();
        binding.guideNavHostFragment.setVisibility(View.VISIBLE);
        showingGuide = true;
        guideNavController.navigate(R.id.guideWelcome, guideBundle);
    }



    /**
     * Allows to navigate to to the next stage of the guide, depending on the current stage.
     * -- Also changes the app fragment shown on background, depending on the stage.
     * -- Also launches the calculation of the tabs and buttons coordinates, depending on the moment.
     */
    public void nextGuide(View view) {
        int current = Objects.requireNonNull(guideNavController.getCurrentDestination()).getId();

        if (current == R.id.guideWelcome) {
            guideBundle = getBottomNavigationViewCoordinates();
            navController.navigate(R.id.navigation_characters);
            guideBundle.putInt("numTab", 0);
            guideNavController.navigate(R.id.action_guideWelcome_to_guideCharacters, guideBundle);
        } else if (current == R.id.guideCharacters) {
            navController.navigate(R.id.navigation_worlds);
            guideBundle.putInt("numTab", 1);
            guideNavController.navigate(R.id.action_guideCharacters_to_guideWorlds, guideBundle);
        } else if (current == R.id.guideWorlds) {
            navController.navigate(R.id.navigation_collectibles);
            guideBundle.putInt("numTab", 2);
            guideNavController.navigate(R.id.action_guideWorlds_to_guideCollectibles, guideBundle);
        } else if (current == R.id.guideCollectibles) {
            guideBundle = getToolBarMenuCoordinates();
            navController.navigate(R.id.navigation_characters);
            guideBundle.putInt("numButton", 0);
            guideNavController.navigate(R.id.action_guideCollectibles_to_guideInfo, guideBundle);
        } else if (current == R.id.guideInfo) {
            navController.navigate(R.id.navigation_characters);
            guideBundle.putInt("numButton", 1);
            guideNavController.navigate(R.id.action_guideInfo_to_guideGuide, guideBundle);
        } else if (current == R.id.guideGuide)
            guideNavController.navigate(R.id.action_guideGuide_to_guideSummary);
        else if (current == R.id.guideSummary)
            endGuide();
    }



    /**
     * Allows to navigate to to the previous stage of the guide, depending on the current stage.
     */
    void backGuide() {
        int current = Objects.requireNonNull(guideNavController.getCurrentDestination()).getId();

        // If the current stage is the first one, the back button closes the guide:
        if (current == R.id.guideWelcome) {
            endGuide();
            return;
        }

        // Otherwise, use the navController to go back, adjust the coordinates and show the accurate background fragment:
        guideNavController.navigateUp();
        if (current == R.id.guideWorlds)
            navController.navigate(R.id.navigation_characters);
        else if (current == R.id.guideCollectibles)
            navController.navigate(R.id.navigation_worlds);
        else if (current == R.id.guideInfo) {
            getBottomNavigationViewCoordinates();
            navController.navigate(R.id.navigation_collectibles);
        }else if(current == R.id.guideSummary)
            getToolBarMenuCoordinates();
    }



    /**
     * Allows to close the app guide.
     */
    public void endGuide() {
        showingGuide = false;
        binding.guideNavHostFragment.setVisibility(View.GONE);
    }



    /**
     * Calculates the coordinates of the BottomNavigationView menu elements, in order to perform
     * the non-fixed elements in the guide.
     * @return A bundle containing necessary layout values to perform the guide.
     */
    public Bundle getBottomNavigationViewCoordinates() {

        if (guideBundle == null)
            guideBundle = new Bundle();

        // Coordinates of the first button in BottomNavigationView:
        int[] firstButtonLocation = new int[2];
        findViewById(R.id.navigation_characters).getLocationOnScreen(firstButtonLocation);

        // Computing the size of a BottomNavigationView button:
        int[] secondButtonLocation = new int[2];
        findViewById(R.id.navigation_worlds).getLocationOnScreen(secondButtonLocation);
        int buttonSizeX = secondButtonLocation[0] - firstButtonLocation[0];
        int buttonSizeY = findViewById(R.id.navigation_characters).getHeight();

        // Getting the center for each BottomNavigationView button:
        guideBundle.putInt("tabSizeX", buttonSizeX);
        guideBundle.putInt("tabSizeY", buttonSizeY);
        guideBundle.putFloat("charactersX", firstButtonLocation[0] + buttonSizeX * 0.5f);
        guideBundle.putFloat("worldsX", firstButtonLocation[0] + buttonSizeX * 1.5f);
        guideBundle.putFloat("collectiblesX", firstButtonLocation[0] + buttonSizeX * 2.5f);
        guideBundle.putFloat("bottomNavigationButtonsY", firstButtonLocation[1] + buttonSizeY * 0.5f);

        return guideBundle;
    }




    /**
     * Calculates the coordinates of the ToolBar menu elements, in order to perform
     * the non-fixed elements in the guide.
     * @return A bundle containing necessary layout values to perform the guide.
     */
    public Bundle getToolBarMenuCoordinates() {

        if (guideBundle == null)
            guideBundle = new Bundle();

        // Coordinates of the first button in ToolBar Menu:
        int[] firstButtonLocation = new int[2];
        findViewById(R.id.show_guide).getLocationOnScreen(firstButtonLocation);

        // Computing the size of a ToolBar Menu button:
        int[] secondButtonLocation = new int[2];
        findViewById(R.id.action_info).getLocationOnScreen(secondButtonLocation);
        int buttonSizeX = secondButtonLocation[0] - firstButtonLocation[0];
        int buttonSizeY = findViewById(R.id.action_info).getHeight();

        // Getting the center for each ToolBar Menu button:
        guideBundle.putInt("menuSizeX", buttonSizeX);
        guideBundle.putInt("menuSizeY", buttonSizeY);
        guideBundle.putFloat("showGuideX", firstButtonLocation[0] + buttonSizeX * 0.5f);
        guideBundle.putFloat("showInfoX", firstButtonLocation[0] + buttonSizeX * 1.5f);
        guideBundle.putFloat("toolBarMenuButtonsY", firstButtonLocation[1] + buttonSizeY * 0.5f);

        return guideBundle;
    }



    /**
     * Allows to know if the ToolBar menu is completely inflated.
     * This method is called in order to assure that some app guide are displayed in the correct place.
     * @param callback Indicates if the ToolBar menu is completely inflated.
     */
    public void layoutReady(Runnable callback) {
        binding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (menuReady) {
                    binding.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    callback.run();
                }
            }
        });
    }


    /**
     * Plays the Easter Egg video.
     * @param startTime The moment of the video (milliseconds) in which it should start.
     */
    public void playVideo(int startTime) {

        //Loading video:
        binding.videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.video);

        // Setting up a Media controller to the VideoView.
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(binding.videoPanel);
        binding.videoView.setMediaController(mediaController);

        // Setting up the video as visible and full-screen (hiding system bar):
        binding.videoPanel.setVisibility(View.VISIBLE);
        WindowInsetsController insetsController = getWindow().getInsetsController();
        if (insetsController != null) {
            insetsController.hide(WindowInsets.Type.statusBars());
            insetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        }

        // Playing video:
        if (startTime > 0)
            binding.videoView.seekTo(startTime);
        playingVideo = true;
        binding.videoView.start();

        // Setting up an end-video listener:
        binding.videoView.setOnCompletionListener(this::stopVideo);
    }


    /**
     * Hides the Video player and navigates to Collectibles tab.
     */
    private void stopVideo(MediaPlayer mediaPlayer) {

        // Setting up the video as hide:
        playingVideo = false;
        binding.videoPanel.setVisibility(View.GONE);

        // Showing again the System bar:
        WindowInsetsController insetsController = getWindow().getInsetsController();
        if (insetsController != null) {
            insetsController.show(WindowInsets.Type.statusBars());
            insetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_DEFAULT);
        }

        // Navigating to Collectibles tab:
        navController.navigate(R.id.navigation_collectibles);
    }


    /**
     * Launches a "flame" animation on an Spyro picture.
     * @param image The Spyro picture.
     */
    public void showFire(ImageView image) {

        // Getting picture location (system bars space needed to be subtracted):
        int[] spyroCoordinates = new int[2];
        image.getLocationOnScreen(spyroCoordinates);
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int[] pictureInfo =  {
                spyroCoordinates[0] - rect.left,
                spyroCoordinates[1] - rect.top,
                image.getWidth(),
                image.getHeight()};

        // Creating the Easter Egg and adding it to the layout:
        EasterEggFire fireView = new EasterEggFire(this, pictureInfo);
        binding.getRoot().addView(fireView);

        // Starting easter egg, and hiding it when finished:
        fireView.start(new AnimatorListenerAdapter(){
            @Override
            public void onAnimationEnd(Animator animation) {
                binding.getRoot().removeView(fireView);
            }
        });

        // Playing an additional sound:
        SoundPool soundPool = new SoundPool.Builder().setMaxStreams(1).build();
        int soundId = soundPool.load(this, R.raw.fire, 1);
        soundPool.setOnLoadCompleteListener((soundPool1, sampleId, status) -> {
            if (status == 0)
                soundPool1.play(soundId, 1f, 1f, 0, 0, 1f);
        });
    }


    /**
     * Customizes the action of the Device Back Button in some cases.
     */
    private void configureBackButton() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                // Back navigating in the app guide:
                if (showingGuide)
                    backGuide();

                // Closing Easter Egg video:
                else if (playingVideo) {
                    binding.videoView.stopPlayback();
                    stopVideo(null);

                }

                // Closing app when pushed on the Home app fragment:
                else if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == navController.getGraph().getStartDestinationId())
                    finish();

                // Otherwise, use the NavController to navigate up:
                else
                    navController.navigateUp();
            }
        });
    }
}