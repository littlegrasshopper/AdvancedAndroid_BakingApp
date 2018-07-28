package com.example.android.bakingapp.ui;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.RecipeStep;
import com.example.android.bakingapp.utilities.RecipeUtils;
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
 * Fragment for showing details of a recipe step (video, instructions)
 */
public class RecipeStepDetailFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String TAG = RecipeStepDetailFragment.class.getSimpleName();
    private static final int NEXT_STEP_DELAY_MILLIS = 1000;

    @BindView(R.id.exoMediaPlayer) SimpleExoPlayerView mPlayerView;
    @BindView(R.id.media_unavailable) ImageView mMediaUnavailable;

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

        // Return the rootView
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Load the saved state if there is one
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(RecipeUtils.INSTANCE_RECIPE)) {
                mRecipe = Parcels.unwrap(savedInstanceState.getParcelable(RecipeUtils.INSTANCE_RECIPE));
            }
            if (savedInstanceState.containsKey(RecipeUtils.INSTANCE_RECIPE_STEP)) {
                mRecipeStep = Parcels.unwrap(savedInstanceState.getParcelable(RecipeUtils.INSTANCE_RECIPE_STEP));
            }
            if (savedInstanceState.containsKey(RecipeUtils.INSTANCE_RECIPE_STEP_INDEX)) {
                mCurrentRecipeStepIndex = savedInstanceState.getInt(RecipeUtils.INSTANCE_RECIPE_STEP_INDEX);
            }
            if (savedInstanceState.containsKey(RecipeUtils.INSTANCE_PLAYER_POSITION)) {
                mPlayerPosition = savedInstanceState.getLong(RecipeUtils.INSTANCE_PLAYER_POSITION);
            }
            if (savedInstanceState.containsKey(RecipeUtils.INSTANCE_PLAYER_STATE)) {
                mPlayerState = savedInstanceState.getBoolean(RecipeUtils.INSTANCE_PLAYER_STATE);;
            }
        }

        if (mRecipe == null) {
            Bundle args = getArguments();
            mRecipe = Parcels.unwrap(args.getParcelable(RecipeUtils.EXTRA_RECIPE));
            mCurrentRecipeStepIndex = args.getInt(RecipeUtils.EXTRA_RECIPE_STEP_INDEX);
        }

        // Populate the details of the recipe step
        mRecipeStep = mRecipe.getSteps().get(mCurrentRecipeStepIndex);

        if (getActivity().findViewById(R.id.tvStepInstruction) != null) {
            mStepInstruction = (TextView) getActivity().findViewById(R.id.tvStepInstruction);
            mStepInstruction.setText(mRecipeStep.getShortDescription());
        }

        setupMediaPlayer(mRecipeStep, mPlayerPosition, mPlayerState);
    }

    /**
     * Set up the default artwork and call to initialize the media player
     *
     * @param recipeStep RecipeStep object with the media URLs
     * @param playerPosition Where to start playing
     * @param playerState Whether to play or pause
     */
    public void setupMediaPlayer(RecipeStep recipeStep, long playerPosition, boolean playerState) {
        if (recipeStep != null) {
            String url = recipeStep.getVideoURL();
            String thumb = recipeStep.getThumbnailURL();

            if (mPlayerView != null && !TextUtils.isEmpty(thumb)) {
                mPlayerView.setDefaultArtwork(BitmapFactory.decodeFile(thumb));
            }
            if (!TextUtils.isEmpty(url)) {
                showMediaPlayer();
                initializePlayer(Uri.parse(url), playerPosition, playerState);
            } else {
                showDefaultImage();
            }
        }
    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri, long pos, boolean state) {

        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(/*getContext()*/getActivity(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.seekTo(pos);

            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(state);
        }
    }

    /**
     * Show the media player
     */
    private void showMediaPlayer() {
        // hide the default image
        mMediaUnavailable.setVisibility(View.GONE);
        // show the media player
        mPlayerView.setVisibility(View.VISIBLE);
    }

    /**
     * Show a placeholder image if media is unavailable
     */
    private void showDefaultImage() {
        // hide the media player
        mPlayerView.setVisibility(View.GONE);
        // show the error message
        mMediaUnavailable.setVisibility(View.VISIBLE);
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
     * Save the current state of the fragment
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RecipeUtils.INSTANCE_RECIPE, Parcels.wrap(mRecipe));
        outState.putParcelable(RecipeUtils.INSTANCE_RECIPE_STEP, Parcels.wrap(mRecipeStep));
        outState.putInt(RecipeUtils.INSTANCE_RECIPE_STEP_INDEX, mCurrentRecipeStepIndex);
        outState.putLong(RecipeUtils.INSTANCE_PLAYER_POSITION, mExoPlayer.getCurrentPosition());
        outState.putBoolean(RecipeUtils.INSTANCE_PLAYER_STATE, mPlayerState);
        super.onSaveInstanceState(outState);
    }
}
