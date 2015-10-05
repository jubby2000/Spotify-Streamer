package com.example.jacob.spotifystreamer;

import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jacob on 7/30/15.
 */
public class PlayerDialogFragment extends DialogFragment {

    private final String LOG_TAG = PlayerDialogFragment.class.getSimpleName();

    boolean mIsLargeLayout;
    boolean isPaused;
    String artistName;
    String albumName;
    String albumArtLarge;
    String trackName;
    String previewUrl;
    String artistImageLarge;
    static MediaPlayer mediaPlayer;
    ImageButton playPause;
    ImageButton previousTrack;
    ImageButton nextTrack;
    SeekBar seekBar;
    Handler handler = new Handler();
    ArrayList<ListOfTracks> trackList = new ArrayList<>();
    int currentPosition;

//    static PlayerDialogFragment newInstance(String artistName, String albumName,
//                                            String albumArtLarge, String trackName,
//                                            String previewUrl) {
//        PlayerDialogFragment f = new PlayerDialogFragment();
//
//        // Supply string input as an argument.
//        Bundle args = new Bundle();
//        args.putString("artistName", artistName);
//        args.putString("albumName", albumName);
//        args.putString("albumArtLarge", albumArtLarge);
//        args.putString("trackName", trackName);
//        args.putString("previewUrl", previewUrl);
//        f.setArguments(args);
//
//        return f;
//        }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.player_layout);

        setRetainInstance(true);

        if (savedInstanceState == null || !savedInstanceState.containsKey("tracks")) {
            trackList = getArguments().getParcelableArrayList("trackList");

            if (trackList != null && currentPosition >= 0) {
                artistName = getArguments().getString("artistName");
                albumName = trackList.get(currentPosition).albumName;
                albumArtLarge = trackList.get(currentPosition).largeImage;
                trackName = trackList.get(currentPosition).trackName;
                previewUrl = trackList.get(currentPosition).previewUrl;
                artistImageLarge = trackList.get(currentPosition).image;
            }
//        } else {
//            trackList = savedInstanceState.getParcelableArrayList("tracks");
//            currentPosition = savedInstanceState.getInt("currentTrack");
//            playPause.setTag(savedInstanceState.getString("playPauseState"));
//            if (trackList != null && currentPosition >= 0) {
//                artistName = trackList.get(getCurrentPosition()).artistName;
//                albumName = trackList.get(getCurrentPosition()).albumName;
//                albumArtLarge = trackList.get(getCurrentPosition()).largeImage;
//                trackName = trackList.get(getCurrentPosition()).trackName;
//                previewUrl = trackList.get(getCurrentPosition()).previewUrl;
//                artistImageLarge = trackList.get(getCurrentPosition()).image;
//                if (mediaPlayer.isPlaying()) {
//                    getPlayPauseImage().setImageResource(android.R.drawable.ic_media_pause);
//                } else if (getPlayPauseImage().getTag() == "ic_media_play") {
//                    getPlayPauseImage().setImageResource(android.R.drawable.ic_media_play);
//                } else {
//                    getPlayPauseImage().setImageResource(android.R.drawable.ic_menu_revert);
//                }
//            }

        }

        mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);

        currentPosition = getArguments().getInt("currentPosition");

    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        Log.d(LOG_TAG, "onActivityCreated");
        Log.d(LOG_TAG, "state != null: " + (state != null ? "true" : "false"));
        //Log.v(LOG_TAG, artistName);

        if (state != null) {
            artistName = state.getString("artistName");
            trackList = state.getParcelableArrayList("tracks");
            setCurrentPosition(state.getInt("currentTrack"));
            seekBar.setMax(((mediaPlayer.getDuration()) / 1000) % 60 );
            seekBar.setProgress(state.getInt("seekBarProgress"));
            mediaPlayer.seekTo(state.getInt("mediaPlayerProgress"));
            isPaused = state.getBoolean("isPaused");
            Log.d(LOG_TAG, state.getString("playPauseState"));
            if (state.getString("playPauseState") == "ic_media_play") {
                getPlayPauseImage().setImageResource(android.R.drawable.ic_media_play);
            }
            if (state.getString("playPauseState") == "ic_media_pause") {
                getPlayPauseImage().setImageResource(android.R.drawable.ic_media_pause);
            }
            if (state.getString("playPauseState") == "ic_menu_revert") {
                getPlayPauseImage().setImageResource(android.R.drawable.ic_menu_revert);
            }
            Log.d(LOG_TAG, state.getString("playPauseState"));
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(LOG_TAG, "onSaveInstanceState");
        if (trackList != null) {
            //mediaPlayer.pause();

            outState.putString("artistName", artistName);
            outState.putParcelableArrayList("tracks", trackList);
            outState.putInt("currentTrack", getCurrentPosition());
            outState.putInt("seekBarProgress", seekBar.getProgress());
            outState.putInt("mediaPlayerProgress", ((mediaPlayer.getCurrentPosition())));
            outState.putString("playPauseState", getPlayPauseImage().getTag().toString());
            outState.putBoolean("isPaused", isPaused);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup player,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        final View rootView = inflater.inflate(R.layout.player_layout, player, false);
        final TextView textElapsed = (TextView) rootView.findViewById(R.id.player_elapsed_time);
        final TextView textTotal = (TextView) rootView.findViewById(R.id.player_total_time);

        //setRetainInstance(true);
        //int currentTrack = currentPosition;

        final String url = trackList.get(getCurrentPosition()).previewUrl;

        if (mediaPlayer == null && savedInstanceState == null){
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    play();
                    getPlayPauseImage().setImageResource(android.R.drawable.ic_media_pause);
                    textTotal.setText("0:" + String.valueOf(((mediaPlayer.getDuration()) / 1000) % 60));
                    seekBar.setMax(Integer.valueOf(((mediaPlayer.getDuration()) / 1000) % 60));
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());

                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //mediaPlayer.stop();
                    getPlayPauseImage().setImageResource(android.R.drawable.ic_menu_revert);
                    getPlayPauseImage().setTag("ic_menu_revert");
                }
            });
        } else if (savedInstanceState != null){
            mediaPlayer.seekTo((savedInstanceState.getInt("mediaPlayerProgress")));
            if (savedInstanceState.getString("playPauseState") == "ic_media_play") {
                playPause.setImageResource(android.R.drawable.ic_media_play);
                pause();
            } else if(savedInstanceState.getString("playPauseState") == "ic_media_pause"){
                playPause.setImageResource(android.R.drawable.ic_media_pause);
                play();
            } else {
                playPause.setImageResource(android.R.drawable.ic_menu_revert);
            }
            seekBar.setProgress(savedInstanceState.getInt("seekBarProgress"));
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //mediaPlayer.stop();
                    getPlayPauseImage().setImageResource(android.R.drawable.ic_menu_revert);
                    getPlayPauseImage().setTag("ic_menu_revert");
                }
            });

        } else {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();

        }catch(IllegalStateException e) {
            Log.v(LOG_TAG, "something went wrong here");

        }catch(IOException e) {
            Snackbar.make(getActivity().findViewById(R.id.player),
                    "Weird... I can't seem to find that track.",
                    Snackbar.LENGTH_LONG).show();
            Log.v(LOG_TAG, "something went wrong here");

        } catch (IllegalArgumentException e) {
            Snackbar.make(getActivity().findViewById(R.id.player),
                    "Weird. I can't seem to find that track.",
                    Snackbar.LENGTH_LONG).show();
            Log.v(LOG_TAG, "something went wrong here");
        }


        setPlayPauseImage((ImageButton) rootView.findViewById(R.id.play_pause));
        previousTrack = (ImageButton) rootView.findViewById(R.id.previous_track);
        nextTrack = (ImageButton) rootView.findViewById(R.id.next_track);

        getPlayPauseImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    pause();

                } else if (getPlayPauseImage().getTag().toString() == "ic_menu_revert") {

                    mediaPlayer.reset();
                    try {
                        mediaPlayer.setDataSource(url);
                        mediaPlayer.prepareAsync();
                    } catch (IllegalStateException e) {
                        Log.v(LOG_TAG, e.toString());
                    } catch (IOException e) {
                        Log.v(LOG_TAG, e.toString());
                    } catch (IllegalArgumentException e) {
                        Log.v(LOG_TAG, e.toString());
                    }
                } else {
                    play();
                }

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        play();
                    }
                });
                }
        });

        previousTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getCurrentPosition() > 0) {
                    setCurrentPosition(getCurrentPosition() - 1);

                    TextView artistTextView = (TextView) rootView.findViewById(R.id.player_artist_name);
                    artistTextView.setText(artistName);

                    TextView albumTextView = (TextView) rootView.findViewById(R.id.player_album_name);
                    albumTextView.setText(trackList.get(getCurrentPosition()).albumName);

                    ImageView albumArtView = (ImageView) rootView.findViewById(R.id.player_album_art);
                    try {
                        Picasso.with(getActivity())
                                .load(trackList.get(getCurrentPosition()).largeImage)
                                .placeholder(R.drawable.solid_gray)
                                .into(albumArtView);
                    } catch (IllegalArgumentException error) {
                        Picasso.with(getActivity())
                                .load("http://lorempixel.com/640/640/cats/")
                                .placeholder(R.drawable.solid_gray)
                                .into(albumArtView);
                    }

                    TextView trackTextView = (TextView) rootView.findViewById(R.id.player_track_name);
                    trackTextView.setText(trackList.get(getCurrentPosition()).trackName);

                    mediaPlayer.reset();

                    try{
                        mediaPlayer.setDataSource(trackList.get(getCurrentPosition()).previewUrl);
                        mediaPlayer.prepareAsync();

                    }catch(IllegalStateException e) {
                        Log.v(LOG_TAG, e.toString());

                    }catch(IOException e) {
                        Log.v(LOG_TAG, e.toString());

                    }catch(IllegalArgumentException e) {
                        Log.v(LOG_TAG, e.toString());
                    }

                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            play();
                        }
                    });
                }else {
                    mediaPlayer.reset();

                    Snackbar.make(getActivity().findViewById(R.id.player),
                            "This is the first track in the list, bro.",
                            Snackbar.LENGTH_LONG).show();

                    try{
                        mediaPlayer.setDataSource(trackList.get(getCurrentPosition()).previewUrl);
                        mediaPlayer.prepareAsync();

                    }catch(IllegalStateException e) {
                        Log.v(LOG_TAG, e.toString());

                    }catch(IOException e) {
                        Log.v(LOG_TAG, e.toString());

                    }catch(IllegalArgumentException e) {
                        Log.v(LOG_TAG, e.toString());
                    }

                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            play();
                        }
                    });
                }



            }
        });

        nextTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getCurrentPosition() < 8) {
                    setCurrentPosition(getCurrentPosition() + 1);

                    TextView artistTextView = (TextView) rootView.findViewById(R.id.player_artist_name);
                    artistTextView.setText(artistName);

                    TextView albumTextView = (TextView) rootView.findViewById(R.id.player_album_name);
                    albumTextView.setText(trackList.get(getCurrentPosition()).albumName);

                    ImageView albumArtView = (ImageView) rootView.findViewById(R.id.player_album_art);
                    try {
                        Picasso.with(getActivity())
                                .load(trackList.get(getCurrentPosition()).largeImage)
                                .placeholder(R.drawable.solid_gray)
                                .into(albumArtView);
                    } catch (IllegalArgumentException error) {
                        Picasso.with(getActivity())
                                .load("http://lorempixel.com/640/640/cats/")
                                .placeholder(R.drawable.solid_gray)
                                .into(albumArtView);
                    }

                    TextView trackTextView = (TextView) rootView.findViewById(R.id.player_track_name);
                    trackTextView.setText(trackList.get(getCurrentPosition()).trackName);

                    mediaPlayer.reset();

                    try {
                        mediaPlayer.setDataSource(trackList.get(getCurrentPosition()).previewUrl);
                        mediaPlayer.prepareAsync();

                    } catch (IllegalStateException e) {
                        Log.v(LOG_TAG, e.toString());

                    } catch (IOException e) {
                        Log.v(LOG_TAG, e.toString());

                    } catch (IllegalArgumentException e) {
                        Log.v(LOG_TAG, e.toString());
                    }

                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            play();
                        }
                    });

                } else {
                    Snackbar.make(getActivity().findViewById(R.id.player),
                            "This is the last track in the list, bro.",
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });



        seekBar = (SeekBar) rootView.findViewById(R.id.player_seek_bar);
        if (savedInstanceState != null) {
            seekBar.setProgress(savedInstanceState.getInt("seekBarProgress"));
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int prog = 0;

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

                if (prog < 30) {
                    mediaPlayer.seekTo((seekBar.getProgress()) * 1000);
                } else {
                    releaseMediaPlayer();
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub

                prog = progress;

                if (prog < 10) {
                    textElapsed.setText("0:0" + String.valueOf(progress));
                } else {
                    textElapsed.setText("0:" + String.valueOf(progress));
                }
            }
        });

        TextView artistTextView = (TextView) rootView.findViewById(R.id.player_artist_name);
        artistTextView.setText(artistName);


        TextView albumTextView = (TextView) rootView.findViewById(R.id.player_album_name);
        albumTextView.setText(trackList.get(getCurrentPosition()).albumName);

        ImageView albumArtView = (ImageView) rootView.findViewById(R.id.player_album_art);
        try {
            Picasso.with(getActivity())
                    .load(trackList.get(getCurrentPosition()).largeImage)
                    .placeholder(R.drawable.solid_gray)
                    .into(albumArtView);
        } catch (IllegalArgumentException error) {
            Picasso.with(getActivity())
                    .load("http://lorempixel.com/640/640/cats/")
                    .placeholder(R.drawable.solid_gray)
                    .into(albumArtView);
        }

        TextView trackTextView = (TextView) rootView.findViewById(R.id.player_track_name);
        trackTextView.setText(trackList.get(getCurrentPosition()).trackName);



        return rootView;
    }

    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            if(!isPaused && mediaPlayer != null){
                seekBar.setProgress(((mediaPlayer.getCurrentPosition()) / 1000) % 60);

                handler.postDelayed(updateSeekBarTime, 1000);
            }
        }
    };

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            if(mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void pause() {
        mediaPlayer.pause();
        isPaused = true;
        handler.removeCallbacks(updateSeekBarTime);
        getPlayPauseImage().setImageResource(android.R.drawable.ic_media_play);
        getPlayPauseImage().setTag("ic_media_play");
    }

    private int getCurrentPosition() {
        return currentPosition;
    }

    private void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    private ImageButton getPlayPauseImage() {
        return playPause;
    }

    private void setPlayPauseImage(ImageButton playPause) {
        this.playPause = playPause;
    }

    private void play() {
        mediaPlayer.start();
        isPaused = false;
        getPlayPauseImage().setImageResource(android.R.drawable.ic_media_pause);
        getPlayPauseImage().setTag("ic_media_pause");
        handler.postDelayed(updateSeekBarTime, 1000);
    }

//    @Override
//    public void onResume() {
//        handler.postDelayed(updateSeekBarTime, 1000);
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }

//    public void showDialog() {
//        FragmentManager fragmentManager = getFragmentManager();
//        PlayerDialogFragment newFragment = new PlayerDialogFragment();
//
//        if (mIsLargeLayout) {
//            // The device is using a large layout, so show the fragment as a dialog
//            newFragment.show(fragmentManager, "dialog");
//        } else {
//            // The device is smaller, so show the fragment fullscreen
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            // For a little polish, specify a transition animation
//            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//            // To make it fullscreen, use the 'content' root view as the container
//            // for the fragment, which is always the root view for the activity
//            transaction.add(android.R.id.content, newFragment)
//                    .addToBackStack(null).commit();
//        }
//    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }
}
