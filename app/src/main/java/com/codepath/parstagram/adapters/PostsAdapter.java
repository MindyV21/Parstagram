package com.codepath.parstagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.codepath.parstagram.fragments.InstaProfileFragment;
import com.codepath.parstagram.models.Post;
import com.codepath.parstagram.R;
import com.codepath.parstagram.models.User;
import com.parse.ParseFile;

import org.w3c.dom.Text;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    public static final String TAG = "PostsAdapter";

    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivProfileImage;
        private TextView tvUsername;
        private ImageButton ibMore;

        private ImageView ivImage;

        private ImageButton ibHeart;
        private ImageButton ibComment;
        private ImageButton ibDirect;
        private ImageButton ibSave;

        private RelativeLayout relLayoutLikes;
        private RelativeLayout relLayoutLikesProfiles;
        private ImageView ivLikesOne;
        private ImageView ivLikesTwo;
        private ImageView ivLikesThree;
        private TextView tvLikedBy;

        private TextView tvDescription;
        private TextView tvComments;
        private TextView tvTimestamp;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ibMore = itemView.findViewById(R.id.ibMore);

            ivImage = itemView.findViewById(R.id.ivImage);

            ibHeart = itemView.findViewById(R.id.ibHeart);
            ibComment = itemView.findViewById(R.id.ibComment);
            ibDirect = itemView.findViewById(R.id.ibDirect);
            ibSave = itemView.findViewById(R.id.ibSave);

            relLayoutLikes = itemView.findViewById(R.id.relLayoutLikes);
            relLayoutLikesProfiles = itemView.findViewById(R.id.relLayoutLikesProfiles);
            ivLikesOne = itemView.findViewById(R.id.ivLikesOne);
            ivLikesTwo = itemView.findViewById(R.id.ivLikesTwo);
            ivLikesThree = itemView.findViewById(R.id.ivLikesThree);
            tvLikedBy = itemView.findViewById(R.id.tvLikedBy);

            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvComments = itemView.findViewById(R.id.tvViewComments);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);

            //itemView.setOnClickListener(this);
        }

        public void bind(Post post) {
            // bind the post data into view elements
            bindHeader(post);

            // image
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }

            bindActions(post);
            bindLikes(post);

            tvDescription.setText(post.getDescription());
            // TODO: view comments btn
            tvComments.setText("View all 87 comments");
            tvComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "view comments button pressed");
                }
            });
            tvTimestamp.setText(Post.calculateTimeAgo(post.getCreatedAt()));
        }

        private void bindLikes(Post post) {
            // TODO: logic for likes showing up
            // make certain views gone depending on data
            Drawable drawable = AppCompatResources.getDrawable(context, R.drawable.ic_profile);
            ivLikesOne.setImageDrawable(drawable);
            ivLikesTwo.setImageDrawable(drawable);
            ivLikesThree.setImageDrawable(drawable);
            tvLikedBy.setText("Liked by 387 users");
        }

        // binds the actions buttons on a post - like, comment, direct, save
        private void bindActions(Post post) {
            // TODO: action buttons of a post
            Drawable drawable = AppCompatResources.getDrawable(context, R.drawable.ic_heart);
            ibHeart.setBackground(drawable);
            // if post already liked by user
            ibHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ibHeart.isSelected()) {
                        Log.i(TAG, "heart: user unliked this post !");
                        ibHeart.setSelected(false);
                    } else {
                        Log.i(TAG, "heart: user liked this post !");
                        ibHeart.setSelected(true);
                    }
                }
            });

            drawable = AppCompatResources.getDrawable(context, R.drawable.ufi_comment);
            ibComment.setBackground(drawable);
            ibComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "comment: go to comment section !");
                }
            });

            drawable = AppCompatResources.getDrawable(context, R.drawable.direct);
            ibDirect.setBackground(drawable);
            ibDirect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "direct: send post to another user !");
                }
            });

            drawable = AppCompatResources.getDrawable(context, R.drawable.ic_save);
            ibSave.setBackground(drawable);
            // if post already saved by user
            ibSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ibSave.isSelected()) {
                        Log.i(TAG, "save: user unsaved this post !");
                        ibSave.setSelected(false);
                    } else {
                        Log.i(TAG, "save: user saved this post !");
                        ibSave.setSelected(true);
                    }
                }
            });
        }

        // binds header of a post
        private void bindHeader(Post post) {
            ParseFile image = post.getUser().getParseFile(User.KEY_PROFILE_IMAGE);
            if (image != null) {
                Glide.with(context).load(image.getUrl()).transform(new CircleCrop()).into(ivProfileImage);
            } else {
                Drawable drawable = AppCompatResources.getDrawable(context, R.drawable.ic_profile);
                ivProfileImage.setImageDrawable(drawable);
            }
            // TODO: click to go to user's profile
            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "pfp: go to user's profile !");
                    AppCompatActivity activity = (AppCompatActivity) context;
                    Fragment myFragment = new InstaProfileFragment(post.getUser());
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, myFragment).addToBackStack(null).commit();
                }
            });


            tvUsername.setText(post.getUser().getUsername());
            // TODO: click to go to user's profile
            tvUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "username: go to user's profile !");
                }
            });

            Drawable drawable = AppCompatResources.getDrawable(context, R.drawable.ufi_more);
            ibMore.setBackground(drawable);
            // TODO: set up more options for a post
            ibMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "more button clicked !");
                }
            });
        }

        @Override
        public void onClick(View v) {
//            // get position
//            int position = getAdapterPosition();
//            // make sure valid position
//            if (position != RecyclerView.NO_POSITION) {
//                // get post at position
//                Post post = posts.get(position);
//                // intent to display Post
//                Intent i = new Intent(context, PostDetailsActivity.class);
//                // pass post as an extra serialized
//                i.putExtra(Post.class.getSimpleName(), post);
//                // show activity
//                context.startActivity(i);
//            }
        }
    }
}
