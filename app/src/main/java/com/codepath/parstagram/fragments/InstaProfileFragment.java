package com.codepath.parstagram.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.codepath.parstagram.R;
import com.codepath.parstagram.databinding.FragmentInstaProfileBinding;
import com.codepath.parstagram.models.Post;
import com.codepath.parstagram.models.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class InstaProfileFragment extends  Fragment{

    public static final String TAG = "InstaProfileFragment";
    FragmentInstaProfileBinding binding;
    ParseUser user;
    private boolean isCurrentUserProfile;

    Toolbar toolbar;

    ImageView ivProfileImage;
    TextView tvPostsNum;
    TextView tvPostsText;
    TextView tvFollowersNum;
    TextView tvFollowersText;
    TextView tvFollowingNum;
    TextView tvFollowingText;

    TextView tvProfileName;
    TextView tvProfileBody;

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    File photoFile;

    public InstaProfileFragment() {
        // Required empty public constructor
        user = ParseUser.getCurrentUser();
        isCurrentUserProfile = true;
    }

    public InstaProfileFragment(ParseUser user) {
        this.user = user;
        isCurrentUserProfile = false;
    }

    // constructor for another user?

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInstaProfileBinding.inflate(getLayoutInflater(), container, false);
        // layout of fragment is stored in a special property called root
        View view = binding.getRoot();
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivProfileImage = binding.ivProfileImageActual;
        tvPostsNum = binding.tvPostsNum;
        tvPostsText = binding.tvPostsText;
        tvFollowersNum = binding.tvFollowersNum;
        tvFollowersText = binding.tvFollowersText;
        tvFollowingNum = binding.tvFollowingNum;
        tvFollowingText = binding.tvFollowingText;

        tvProfileName = binding.tvProfileName;
        tvProfileBody = binding.tvProfileBody;

        toolbar = binding.toolbar;

        setUpToolbar();

        setUpProfile();

        Fragment childFragment = new ProfilePostsFragment(user);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, childFragment).commit();
    }

    private void showBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_settings);

        LinearLayout logout = bottomSheetDialog.findViewById(R.id.logoutLinearLayout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "User has logged out");
                bottomSheetDialog.dismiss();
                ParseUser.logOut();
                Toast.makeText(getContext(), "User logged out!", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });

        LinearLayout saved = bottomSheetDialog.findViewById(R.id.savedLinearLayout);
        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "User's saved posts!");
            }
        });

        LinearLayout profileImage = bottomSheetDialog.findViewById(R.id.profileImageLinearLayout);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "User is changing their profile picture");
                onLaunchCamera(v);
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }

    public void onLaunchCamera(View view) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                Glide.with(getContext()).load(takenImage).transform(new CircleCrop()).into(ivProfileImage);
                updateProfilePicture(photoFile);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateProfilePicture(File photoFile) {
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.getInBackground(user.getObjectId(), new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i(TAG, "Profile picture save was successful!!");
                Toast.makeText(getContext(), "Profile Picture changed successfully!", Toast.LENGTH_SHORT).show();
                object.put(User.KEY_PROFILE_IMAGE, new ParseFile(photoFile));
                object.saveInBackground();
            }
        });
    }

    private void setUpToolbar() {
        toolbar.setTitle(user.getUsername());
        if (isCurrentUserProfile) {
            toolbar.inflateMenu(R.menu.menu_current_user);
            toolbar.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        showBottomSheetDialog();
                        return true;
                    default:
                        return false;
                }
            });
        }
    }

    private void setUpProfile() {
        Drawable drawable;

        ParseFile image = user.getParseFile(User.KEY_PROFILE_IMAGE);
        if (image != null) {
            Glide.with(getContext()).load(image.getUrl()).transform(new CircleCrop()).into(ivProfileImage);
        } else {
            drawable = AppCompatResources.getDrawable(getContext(), R.drawable.ic_profile);
            ivProfileImage.setImageDrawable(drawable);
        }

        // dummy data
        tvPostsNum.setText("50");
        tvPostsNum.setTextColor(Color.BLACK);
        tvPostsText.setText("Posts");
        tvPostsText.setTextColor(Color.BLACK);

        tvFollowersNum.setText("100");
        tvFollowersNum.setTextColor(Color.BLACK);
        tvFollowersText.setText("Followers");
        tvFollowersText.setTextColor(Color.BLACK);

        tvFollowingNum.setText("150");
        tvFollowingNum.setTextColor(Color.BLACK);
        tvFollowingText.setText("Following");
        tvFollowingText.setTextColor(Color.BLACK);

        tvProfileName.setText(user.getUsername());
        tvProfileName.setTextColor(Color.BLACK);
        tvProfileBody.setText("This is my description!");
        tvProfileBody.setTextColor(Color.BLACK);
    }
}