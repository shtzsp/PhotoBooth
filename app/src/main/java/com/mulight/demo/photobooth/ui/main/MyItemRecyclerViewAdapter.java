package com.mulight.demo.photobooth.ui.main;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mulight.demo.photobooth.R;
import com.mulight.demo.photobooth.ui.main.PhotoListFragment.OnListFragmentInteractionListener;
import com.mulight.demo.photobooth.ui.main.PhotoContent.PhotoItem;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PhotoItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "ItemRecyclerViewAdapter";

    private WeakReference<FragmentActivity> mActivity;
    private final List<PhotoItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapter(FragmentActivity activity, List<PhotoItem> items, OnListFragmentInteractionListener listener) {
        mActivity = new WeakReference<>(activity);
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_photo_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).name);
        holder.mTimeView.setText(mValues.get(position).createTime);

        Log.d(TAG, "path: " + mValues.get(position).path);

        Glide.with(mActivity.get())
                .load(new File(mValues.get(position).path))
                .into(holder.mThumbView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView mThumbView;
        final TextView mNameView;
        final TextView mTimeView;
        PhotoItem mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mThumbView = view.findViewById(R.id.item_thumbnail);
            mNameView = view.findViewById(R.id.item_name);
            mTimeView = view.findViewById(R.id.item_time);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
