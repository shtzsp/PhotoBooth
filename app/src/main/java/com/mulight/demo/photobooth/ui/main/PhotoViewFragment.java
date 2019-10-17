package com.mulight.demo.photobooth.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mulight.demo.photobooth.R;

import java.io.File;

/**
 * A {@link Fragment} for view a photo in full screen.
 * Use the {@link PhotoViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoViewFragment extends Fragment {
    private static final String ARG_IMAGE_PATH = "image_path";

    private String mImagePath;

    public PhotoViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param imagePath Image path.
     * @return A new instance of fragment PhotoViewFragment.
     */
    public static PhotoViewFragment newInstance(String imagePath) {
        PhotoViewFragment fragment = new PhotoViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_PATH, imagePath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImagePath = getArguments().getString(ARG_IMAGE_PATH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_photo_view, container, false);
        Glide.with(getActivity()).load(new File(mImagePath))
                .into((ImageView)v.findViewById(R.id.imageview_a_photo));
        return v;
    }
}
