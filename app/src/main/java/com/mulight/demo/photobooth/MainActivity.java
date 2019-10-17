package com.mulight.demo.photobooth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.mulight.demo.photobooth.ui.main.MainFragment;
import com.mulight.demo.photobooth.ui.main.PhotoListFragment;
import com.mulight.demo.photobooth.ui.main.PhotoContent;
import com.mulight.demo.photobooth.ui.main.PhotoViewFragment;

public class MainActivity extends AppCompatActivity implements PhotoListFragment.OnListFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }

    @Override
    public void onListFragmentInteraction(PhotoContent.PhotoItem item) {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, PhotoViewFragment.newInstance(item.path), null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
