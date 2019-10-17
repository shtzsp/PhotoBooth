package com.mulight.demo.photobooth.ui.main;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mulight.demo.photobooth.R;
import com.mulight.demo.photobooth.utils.DBUtil;
import com.mulight.demo.photobooth.utils.FileUtil;

import java.io.File;
import java.io.IOException;

/**
 * Main fragment
 */
public class MainFragment extends Fragment {

    private static final int REQUEST_CAMERA = 100;
    private static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 110;
    private static final int REQUEST_CAMERA_PERMISSIONS = 111;

    private File mTmpFile;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.main_fragment, container, false);

        v.findViewById(R.id.btn_takephoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCameraAction();
            }
        });
        v.findViewById(R.id.btn_viewphoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.container, new PhotoListFragment(), null)
                        .commit();
            }
        });
        return v;
    }

    /**
     * Check permission and open camera
     */
    private void showCameraAction() {
        FragmentActivity fragActivity = getActivity();

        if (ContextCompat.checkSelfPermission(fragActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    getString(R.string.permission_rationale_write_storage),
                    REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);

        } else if (ContextCompat.checkSelfPermission(fragActivity,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            requestPermission(Manifest.permission.CAMERA,
                    getString(R.string.permission_rationale_camera),
                    REQUEST_CAMERA_PERMISSIONS);

        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(fragActivity.getPackageManager()) != null) {
                try {
                    mTmpFile = FileUtil.createTmpFile(getActivity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (mTmpFile != null && mTmpFile.exists()) {

                    Uri uri = FileProvider.getUriForFile(
                            fragActivity,
                            fragActivity.getPackageName() + ".fileprovider",
                            mTmpFile);

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else {
                    Toast.makeText(fragActivity, R.string.error_file_create_fail, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(fragActivity, R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestPermission(final String permission, String rationale, final int requestCode) {
        if (shouldShowRequestPermissionRationale(permission)) {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.permission_dialog_title)
                    .setMessage(rationale)
                    .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{permission}, requestCode);
                        }
                    })
                    .setNegativeButton(R.string.text_cancel, null)
                    .create().show();
        } else {
            requestPermissions(new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE_WRITE_ACCESS_PERMISSION
                || requestCode == REQUEST_CAMERA_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showCameraAction();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (mTmpFile != null) {
                    onCameraShot(mTmpFile);
                }
            } else {
                // delete tmp file
                while (mTmpFile != null && mTmpFile.exists()) {
                    boolean success = mTmpFile.delete();
                    if (success) {
                        mTmpFile = null;
                    }
                }
            }
        }
    }

    private void onCameraShot(File imageFile) {
        if(imageFile != null) {
            InputDialogFragment.newInstance(imageFile).show(getActivity().getSupportFragmentManager(), "inputDialog");
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        DBUtil.getInstance(getContext()).release();
    }

    /**
     * Dialog for input photo name
     */
    public static class InputDialogFragment extends DialogFragment {
        private File imgFile;

        static InputDialogFragment newInstance(File imgFileTobeNamed) {
            InputDialogFragment frag = new InputDialogFragment();
            frag.imgFile = imgFileTobeNamed;
            return frag;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.name_input, container, false);

            final String filename = imgFile.getName();
            final int dotIdx = filename.indexOf(".");
            final String extension = filename.substring(dotIdx);

            ((TextView)v.findViewById(R.id.textview_ext)).setText(extension);

            final EditText editText_name = v.findViewById(R.id.edittext_name);
            if (imgFile != null) {
                editText_name.setText(filename.substring(0, dotIdx));
                editText_name.selectAll();
            }

            v.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String newName = editText_name.getText().toString();

                    if (TextUtils.isEmpty(newName)) {
                        Toast.makeText(getContext(), getString(R.string.warning_name_empty), Toast.LENGTH_LONG).show();
                    }
                    else {
                        String fpath = imgFile.getAbsolutePath().substring(0, imgFile.getAbsolutePath().lastIndexOf(File.separator));
                        String fname = newName + extension;

                        FileUtil.rename(imgFile, fname);
                        DBUtil.getInstance(getContext()).savePhoto(fname, fpath + File.separator + fname);
                        Toast.makeText(getActivity(), R.string.msg_save_success, Toast.LENGTH_SHORT).show();

                        dismissAllowingStateLoss();
                    }
                }
            });
            v.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBUtil.getInstance(getContext()).savePhoto(imgFile.getName(), imgFile.getAbsolutePath());
                    Toast.makeText(getActivity(), R.string.msg_save_success, Toast.LENGTH_SHORT).show();
                    dismissAllowingStateLoss();
                }
            });
            return v;
        }
    }
}