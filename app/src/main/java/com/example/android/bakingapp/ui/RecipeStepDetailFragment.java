package com.example.android.bakingapp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
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
    public static final String INSTANCE_RECIPE = "recipe";
    public static final String INSTANCE_RECIPE_STEP = "recipeStep";
    public static final String INSTANCE_RECIPE_INDEX = "recipeIndex";
    public static final String INSTANCE_PLAYER_POSITION = "playerPosition";
    public static final String INSTANCE_PLAYER_STATE = "playerState";

    private static final int NEXT_STEP_DELAY_MILLIS = 1000;


    @BindView(R.id.exoMediaPlayer) SimpleExoPlayerView mPlayerView;
   // @BindView(R.id.tvStepInstruction) TextView mStepInstruction;
    //@BindView(R.id.btnNextStep) Button mNextBtn;


    private Recipe mRecipe;
    private int mCurrentRecipeStep;

    private RecipeStep mRecipeStep;
    private long mPlayerPosition = -1;
    private boolean mPlayerState = true;

    private SimpleExoPlayer mExoPlayer;
    //private SimpleExoPlayerView mPlayerView;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

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
            if (savedInstanceState.containsKey(INSTANCE_RECIPE_INDEX)) {
                mCurrentRecipeStep = savedInstanceState.getInt(INSTANCE_RECIPE_INDEX);
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


        // Populate the details of the recipe step
        mRecipeStep = mRecipe.getSteps().get(mCurrentRecipeStep);

        if (getActivity().findViewById(R.id.tvStepInstruction) != null) {
            mStepInstruction = (TextView) getActivity().findViewById(R.id.tvStepInstruction);
            mStepInstruction.setText(mRecipeStep.getShortDescription());
        }

        /*
        if (mNextBtn.getVisibility() == View.VISIBLE) {
            mNextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO

                    // Wait some time so the user can see the correct answer, then go to the next question.
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mExoPlayer.stop();
                            //finish();
                        }
                    }, NEXT_STEP_DELAY_MILLIS);
                }
            });
        }*/

        // Initialize the Media Session.
        initializeMediaSession();
        //Sample answerSample = Sample.getSampleByID(this, mAnswerSampleID);

        // Initialize the player.
        //String videoUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/590129a5_10-mix-in-melted-chocolate-for-frosting-yellow-cake/10-mix-in-melted-chocolate-for-frosting-yellow-cake.mp4";
        setupMediaPlayer(mRecipeStep, mPlayerPosition, mPlayerState);
        /*String videoUrl = mRecipeStep.getVideoURL();
        if (videoUrl != null && !videoUrl.isEmpty()) {
            initializePlayer(Uri.parse(videoUrl), null, mPlayerPosition, mPlayerState);
            Log.i(TAG, "initializing player again: " + mExoPlayer);
        }
        if (mRecipeStep.getThumbnailURL() != null) {
            mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                    (getResources(), R.drawable.question_mark));
        }*/
    }

    public void setupMediaPlayer(RecipeStep step, long playerPosition, boolean playerState) {
        Log.i(TAG, "About to initialize media session and media player");
        if (step != null) {
            //initializeMediaSession();
            String url = step.getVideoURL();
            String thumb = step.getThumbnailURL();

            if (mPlayerView != null && thumb != null) {
                mPlayerView.setDefaultArtwork(BitmapFactory.decodeFile(thumb));
            }
            if (url != null && !url.isEmpty()) {
                initializePlayer(Uri.parse(url), Uri.parse(thumb), playerPosition, playerState);
            }
        }
    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getActivity(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_FAST_FORWARD |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        //TODO mMediaSession.setCallback(new RecipeStepDetailFragment().MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri, Uri thumbnailUri, long pos, boolean state) {

        if (mExoPlayer == null) {
            Log.i(TAG, "Exoplayer is null");
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            if (pos > 0) {
                mExoPlayer.seekTo(pos);
            }

            // Set the ExoPlayer.EventListener to this activity.
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
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        //mNotificationManager.cancelAll();
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    /**
     * Set the RecipeStep object for this fragment
     * @param recipeStep RecipeStep object to render in this fragment
     */
    public void setRecipeStep(RecipeStep recipeStep) {
        mRecipeStep = recipeStep;
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
                //finish();
            }
        }, NEXT_STEP_DELAY_MILLIS);
    }
    /**
     * Release the player when the activity is destroyed.
     */
    /*
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
        mMediaSession.setActive(false);
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //super.onDestroy();
        releasePlayer();
       //mMediaSession.setActive(false);
    }
    // ExoPlayer Event Listeners

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPause() {
//        mPlayerState = mExoPlayer.getPlayWhenReady();
        super.onPause();
    }

    /**
     * Method that is called when the ExoPlayer state changes. Used to update the MediaSession
     * PlayBackState to keep in sync, and post the media notification.
     * @param playWhenReady true if ExoPlayer is playing, false if it's paused.
     * @param playbackState int describing the state of ExoPlayer. Can be STATE_READY, STATE_IDLE,
     *                      STATE_BUFFERING, or STATE_ENDED.
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        /*
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
       // mMediaSession.setPlaybackState(mStateBuilder.build());
        //showNotification(mStateBuilder.build());*/
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity() {
    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    // TODO done (1): Create a static inner class that extends Broadcast Receiver and implement the onReceive() method.
    public static class MediaReceiver extends BroadcastReceiver {
        public MediaReceiver() {

        }
        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }
    //TODO done (2): Call MediaButtonReceiver.handleIntent and pass in the incoming intent
    // as well as the MediaSession object to forward the intent to the MediaSession.Callbacks.

    /**
     * Gets portrait of the composer for a sample by the sample ID.

     * @return The portrait Bitmap.
     */
    /*
    static Bitmap getComposerArtBySampleID(Context context, int sampleID){
        Sample sample = Sample.getSampleByID(context, sampleID);
        int albumArtID = context.getResources().getIdentifier(
                sample != null ? sample.getAlbumArtID() : null, "drawable",
                context.getPackageName());
        return BitmapFactory.decodeResource(context.getResources(), albumArtID);
    }
    */

    /*
    public void showNextButton(boolean show) {
        if (mNextBtn != null) {
            if (show) {
                mNextBtn.setVisibility(View.VISIBLE);
            } else {
                mNextBtn.setVisibility(View.GONE);
            }
        }
    }*/


    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
    }

    public void setCurrentRecipeStep(int pos) {
        mCurrentRecipeStep = pos;
    }

    /**
     * Save the current state of the fragment
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(INSTANCE_RECIPE, Parcels.wrap(mRecipe));
        outState.putParcelable(INSTANCE_RECIPE_STEP, Parcels.wrap(mRecipeStep));
        outState.putInt(INSTANCE_RECIPE_INDEX, mCurrentRecipeStep);
        outState.putLong(INSTANCE_PLAYER_POSITION, mExoPlayer.getCurrentPosition());
        outState.putBoolean(INSTANCE_PLAYER_STATE, mPlayerState);
    }
}
