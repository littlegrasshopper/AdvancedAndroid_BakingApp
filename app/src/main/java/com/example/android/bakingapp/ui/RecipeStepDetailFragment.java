package com.example.android.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.RecipeStep;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment for details of a recipe step (video, instructions)
 */
public class RecipeStepDetailFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String TAG = RecipeStepDetailFragment.class.getSimpleName();
    public static final String INSTANCE_RECIPE = "instanceRecipe";
    public static final String INSTANCE_RECIPE_STEP = "instanceRecipeStep";
    public static final String INSTANCE_RECIPE_STEP_INDEX = "instanceRecipeStepIndex";
    public static final String INSTANCE_PLAYER_POSITION = "instancePlayerPosition";
    public static final String INSTANCE_PLAYER_STATE = "instancePlayerState";

    private static final int NEXT_STEP_DELAY_MILLIS = 1000;


    @BindView(R.id.exoMediaPlayer) SimpleExoPlayerView mPlayerView;
   // @BindView(R.id.tvStepInstruction) TextView mStepInstruction;
    //@BindView(R.id.btnNextStep) Button mNextBtn;


    private Recipe mRecipe;
    private int mCurrentRecipeStepIndex;
    private int mCurrentWindow;

    private RecipeStep mRecipeStep;
    private long mPlayerPosition = 0;
    private boolean mPlayerState = true;

    private SimpleExoPlayer mExoPlayer;

    private TextView mStepInstruction;

    // Mandatory empty constructor for fragments
    public RecipeStepDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the step details fragment layout
        View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        ButterKnife.bind(this, rootView);

        // Load the saved state if there is one
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(INSTANCE_RECIPE)) {
                mRecipe = Parcels.unwrap(savedInstanceState.getParcelable(INSTANCE_RECIPE));
            }
            if (savedInstanceState.containsKey(INSTANCE_RECIPE_STEP)) {
                mRecipeStep = Parcels.unwrap(savedInstanceState.getParcelable(INSTANCE_RECIPE_STEP));
            }
            if (savedInstanceState.containsKey(INSTANCE_RECIPE_STEP_INDEX)) {
                mCurrentRecipeStepIndex = savedInstanceState.getInt(INSTANCE_RECIPE_STEP_INDEX);
            }
            if (savedInstanceState.containsKey(INSTANCE_PLAYER_POSITION)) {
                mPlayerPosition = savedInstanceState.getLong(INSTANCE_PLAYER_POSITION);
            }
            if (savedInstanceState.containsKey(INSTANCE_PLAYER_STATE)) {
                mPlayerState = savedInstanceState.getBoolean(INSTANCE_PLAYER_STATE);;
            }
        }

        // Return the rootView
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        mRecipe = Parcels.unwrap(args.getParcelable(RecipeDetailActivity.EXTRA_RECIPE));
        mCurrentRecipeStepIndex = args.getInt(RecipeDetailActivity.EXTRA_RECIPE_CURRENT_STEP);

        // Populate the details of the recipe step
        mRecipeStep = mRecipe.getSteps().get(mCurrentRecipeStepIndex);

        if (getActivity().findViewById(R.id.tvStepInstruction) != null) {
            mStepInstruction = (TextView) getActivity().findViewById(R.id.tvStepInstruction);
            mStepInstruction.setText(mRecipeStep.getShortDescription());
        }

        setupMediaPlayer(mRecipeStep, mPlayerPosition, mPlayerState);
    }

    public void setupMediaPlayer(RecipeStep step, long playerPosition, boolean playerState) {
        Log.i(TAG, "About to initialize media player");
        if (step != null) {
            String url = step.getVideoURL();
            String thumb = step.getThumbnailURL();

            if (mPlayerView != null && !TextUtils.isEmpty(thumb)) {
                mPlayerView.setDefaultArtwork(BitmapFactory.decodeFile(thumb));
            }
            if (!TextUtils.isEmpty(url)) {
                initializePlayer(Uri.parse(url), playerPosition, playerState);
            }
        }
    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri, long pos, boolean state) {

        if (mExoPlayer == null) {
            Log.i(TAG, "Exoplayer is null");
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(/*getContext()*/getActivity(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.seekTo(pos);

            mExoPlayer.addListener(this); //TODO: Is this needed?

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(state);
        }
    }

    /**
     * Release ExoPlayer.
     */
    public void releasePlayer() {
        if (mExoPlayer != null) {
            mPlayerPosition = mExoPlayer.getCurrentPosition();
            mCurrentWindow = mExoPlayer.getCurrentWindowIndex();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    public void stopPlayer() {
        // Wait some time so the user can see the correct answer, then go to the next question.
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mExoPlayer != null) {
                    mExoPlayer.stop();
                }
                //getActivity().finish();
            }
        }, NEXT_STEP_DELAY_MILLIS);
    }

    /**
     * Release the player when the activity is destroyed.
     */

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    @Override
    public void onPause() {
        if (mExoPlayer != null) {
            mPlayerState = mExoPlayer.getPlayWhenReady();
        }
        super.onPause();
    }

    // ExoPlayer Event Listeners


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        Log.i(TAG, "onTimelineChanged");
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        Log.i(TAG, "onTracksChanged");
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        Log.i(TAG, "onLoadingChanged");
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        Log.i(TAG, "onPlayerStateChanged");
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.i(TAG, "onPlayerError");
    }

    @Override
    public void onPositionDiscontinuity() {
        Log.i(TAG, "onPositionDiscontinuity");
    }


    /**
     * Set the Recipe object for this fragment
     * @param recipe Recipe object to guide the next step
     */
    public void XsetRecipe(Recipe recipe) {
        mRecipe = recipe;
    }

    /**
     * Set the RecipeStep object for this fragment
     * @param recipeStep RecipeStep object to render in this fragment
     */
    public void XsetRecipeStep(RecipeStep recipeStep) {
        mRecipeStep = recipeStep;
    }

    /**
     * Set the current step in the recipe
     * @param pos Current step
     */
    public void XsetCurrentRecipeStepIndex(int pos) {
        mCurrentRecipeStepIndex = pos;
    }

    /**
     * Save the current state of the fragment
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(INSTANCE_RECIPE, Parcels.wrap(mRecipe));
        outState.putParcelable(INSTANCE_RECIPE_STEP, Parcels.wrap(mRecipeStep));
        outState.putInt(INSTANCE_RECIPE_STEP_INDEX, mCurrentRecipeStepIndex);
        outState.putLong(INSTANCE_PLAYER_POSITION, mExoPlayer.getCurrentPosition());
        outState.putBoolean(INSTANCE_PLAYER_STATE, mPlayerState);
        super.onSaveInstanceState(outState);
    }
}
