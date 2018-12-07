package com.andreea.bakingapp.baking_app.fragment;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
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

    /**
     * The dummy content this fragment is presenting.
     */
    private Step mRecipeStep;

    private SimpleExoPlayer mPlayer;
    private long mPlayerCurrentPosition = 0;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assert getArguments() != null;
        if (getArguments().containsKey(RECIPE_ID) && getArguments().containsKey(RECIPE_STEP_ID)) {
            mRecipeStep = MemoryCache.getInstance().getRecipeStep(
                    getArguments().getInt(RECIPE_ID),
                    getArguments().getInt(RECIPE_STEP_ID));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_step_detail, container, false);

        // The ExoPlayer's initial seek position is 0 by default or the last saved position.
        if (savedInstanceState != null) {
            mPlayerCurrentPosition = savedInstanceState.getLong(PLAYER_CURRENT_POSITION);
        }

        if (mRecipeStep != null) {
            PlayerView stepVideoView = rootView.findViewById(R.id.recipe_step_video);
            TextView stepDetailView = rootView.findViewById(R.id.recipe_step_detail);

            // Setup the video:
            String videoURLString = mRecipeStep.getVideoURL();
            if (!TextUtils.isEmpty(videoURLString)) {
                mPlayer = ExoPlayerFactory.newSimpleInstance(getContext());
                stepVideoView.setPlayer(mPlayer);
                setupVideoPlayer(mPlayer, Uri.parse(mRecipeStep.getVideoURL()));
            } else {
                stepVideoView.setVisibility(View.GONE);
            }

            // Setup the step description:
            boolean isPhoneLandscape = getResources().getBoolean(R.bool.is_phone_landscape);
            stepDetailView.setText(mRecipeStep.getDescription());
            stepDetailView.setVisibility(isPhoneLandscape && !TextUtils.isEmpty(videoURLString)
                    ? View.GONE : View.VISIBLE);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPlayer != null) {
            outState.putLong(PLAYER_CURRENT_POSITION, mPlayer.getCurrentPosition());
            mPlayer.stop(true);
        }
    }

    private void setupVideoPlayer(SimpleExoPlayer player, Uri mp4VideoUri) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                Objects.requireNonNull(getContext()),
                Util.getUserAgent(getContext(),getString(R.string.app_name)));
        MediaSource videoSource = new ExtractorMediaSource.Factory(
                dataSourceFactory).createMediaSource(mp4VideoUri);
        player.prepare(videoSource);
        player.seekTo(mPlayerCurrentPosition);
    }
}
