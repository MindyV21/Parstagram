package com.codepath.parstagram.fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.codepath.parstagram.R;
import com.codepath.parstagram.databinding.FragmentInstaProfileBinding;
import com.codepath.parstagram.models.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class InstaProfileFragment extends  Fragment{

    public static final String TAG = "InstaProfileFragment";
    FragmentInstaProfileBinding binding;

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

    public InstaProfileFragment() {
        // Required empty public constructor
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

        Fragment childFragment = new ProfilePostsFragment();
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

        bottomSheetDialog.show();
    }

    private void setUpToolbar() {
        toolbar.setTitle(ParseUser.getCurrentUser().getUsername());
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

    private void setUpProfile() {
        ParseUser user = ParseUser.getCurrentUser();

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