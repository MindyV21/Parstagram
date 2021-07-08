package com.codepath.parstagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.parstagram.R;
import com.codepath.parstagram.databinding.FragmentInstaProfileBinding;
import com.codepath.parstagram.databinding.FragmentPostsBinding;
import com.codepath.parstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class InstaProfileFragment extends  Fragment{

    public static final String TAG = "InstaProfileFragment";
    FragmentInstaProfileBinding binding;

    ImageView ivProfile;
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
        ivProfile = binding.ivProfileImageActual;
        tvPostsNum = binding.tvPostsNum;
        tvPostsText = binding.tvPostsText;
        tvFollowersNum = binding.tvFollowersNum;
        tvFollowersText = binding.tvFollowersText;
        tvFollowingNum = binding.tvFollowingNum;
        tvFollowingText = binding.tvFollowingText;

        tvProfileName = binding.tvProfileName;
        tvProfileBody = binding.tvProfileBody;

        setUpProfile();

        Fragment childFragment = new ProfileFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, childFragment).commit();
    }

    private void setUpProfile() {

    }
}