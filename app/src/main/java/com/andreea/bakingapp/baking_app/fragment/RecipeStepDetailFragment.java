package com.andreea.bakingapp.baking_app.fragment;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andreea.bakingapp.baking_app.R;
import com.andreea.bakingapp.baking_app.activity.RecipeStepDetailActivity;
import com.andreea.bakingapp.baking_app.activity.RecipeStepListActivity;
import com.andreea.bakingapp.baking_app.data.MemoryCache;
import com.andreea.bakingapp.baking_app.model.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.andreea.bakingapp.baking_app.Constants.Extra.PLAYER_CURRENT_POSITION;
import static com.andreea.bakingapp.baking_app.Constants.Extra.RECIPE_ID;
import static com.andreea.bakingapp.baking_app.Constants.Extra.RECIPE_STEP_ID;

/**
 * A fragment representing a single RecipeStep detail screen.
 * This fragment is either contained in a {@link RecipeStepListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeStepDetailActivity}
 * on handsets.
 */
public class RecipeStepDetailFragment extends Fragment {

    @BindView(R.id.recipe_step_video) PlayerView mStepVideoView;
    @BindView(R.id.recipe_step_detail) TextView mStepDetailView;

    private Unbinder mButterknifeUnbinder;
    private SimpleExoPlayer mPlayer;
    private long mPlayerCurrentPosition = 0;
    private Step mRecipeStep;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null
                && getArguments().containsKey(RECIPE_ID)
                && getArguments().containsKey(RECIPE_STEP_ID)) {
            mRecipeStep = MemoryCache.getInstance().getRecipeStep(
                    getArguments().getInt(RECIPE_ID),
                    getArguments().getInt(RECIPE_STEP_ID));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_step_detail, container, false);
        mButterknifeUnbinder = ButterKnife.bind(this, view);

        if (mRecipeStep != null) {
            // Setup the step description:
            boolean isPhoneLandscape = getResources().getBoolean(R.bool.is_phone_landscape);
            mStepDetailView.setText(mRecipeStep.getDescription());
            mStepDetailView.setVisibility(isPhoneLandscape && !TextUtils.isEmpty(mRecipeStep.getVideoURL())
                    ? View.GONE : View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mPlayer == null) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPlayer != null) {
            mPlayerCurrentPosition = mPlayer.getCurrentPosition();
            outState.putLong(PLAYER_CURRENT_POSITION, mPlayerCurrentPosition);

        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        // The ExoPlayer's initial seek position is 0 by default or the last saved position.
        if (savedInstanceState != null) {
            mPlayerCurrentPosition = savedInstanceState.getLong(PLAYER_CURRENT_POSITION);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unbind the views:
        mButterknifeUnbinder.unbind();
    }

    /**
     * Initialize the ExoPlayer.
     */
    private void initializePlayer() {
        String videoURLString = mRecipeStep.getVideoURL();
        if (!TextUtils.isEmpty(videoURLString)) {
            mPlayer = ExoPlayerFactory.newSimpleInstance(getContext());
            mStepVideoView.setPlayer(mPlayer);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                    Objects.requireNonNull(getContext()),
                    Util.getUserAgent(getContext(),getString(R.string.app_name)));
            MediaSource videoSource = new ExtractorMediaSource.Factory(
                    dataSourceFactory).createMediaSource(Uri.parse(mRecipeStep.getVideoURL()));
            mPlayer.prepare(videoSource);
            mPlayer.seekTo(mPlayerCurrentPosition);
        } else {
            mStepVideoView.setVisibility(View.GONE);
        }
    }

    /**
     * Stop and release the ExoPlayer.
     */
    private void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.stop(true);
            mPlayer.release();
        }
    }
}
