package com.example.gatherlink.adapters;

import static com.example.gatherlink.activity.MyPostsActivity.REQUEST_CODE_VIEW_DETAILS;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gatherlink.R;
import com.example.gatherlink.activity.MyPostsActivity;
import com.example.gatherlink.activity.PostDetailsActivity;
import com.example.gatherlink.model.PostsModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostsViewHolder> {

    public static final String KEY_POST = "PostDetails";
    public static final String KEY_ID = "IdOfPost";
    public static final String KEY_INDEX = "Postindex";

    private final ArrayList<PostsModel> postsList;
    private final HashMap<PostsModel, String> postIdMap;
    private final Context context;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());


    public PostAdapter(Context context, ArrayList<PostsModel> pM, HashMap<PostsModel, String> map) {
        this.context = context;
        this.postsList = pM;
        this.postIdMap = map;
    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_post, parent, false);
        return new PostsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsViewHolder holder, int position) {
        PostsModel post = postsList.get(position);

        holder.postNumber.setText("Post #" + (position + 1));
        holder.postMessage.setText("Post message: " + post.getMessage());

        String email = post.getEmail();
        String profileText = "??";
        if (email != null && email.contains("@")) {
            profileText = email.substring(0, email.indexOf("@")).toUpperCase();
            if (profileText.length() > 2) {
                profileText = profileText.substring(0, 2);
            }
        }
        holder.profilePic.setText(profileText);

        if (post.getFirstName() != null || post.getLastName() != null) {
            holder.postAuthor.setText("Posted by: " + post.getFirstName() + " " + post.getLastName());
        }

        if (email != null) {
            holder.authorEmail.setText("Email: " + email);
        }

        if (post.getCreatedAt() != null) {
            holder.postDate.setText("Posted on: " + dateFormat.format(post.getCreatedAt()));
        } else {
            holder.postDate.setText("Posted on: (unknown)");
        }

        if (post.getGroupId() != null) {
            FirebaseFirestore.getInstance()
                    .collection("Groups")
                    .document(post.getGroupId())
                    .get()
                    .addOnSuccessListener(groupDoc -> {
                        String groupName = groupDoc.getString("groupName");
                        holder.groupName.setText(groupName != null ? "Group: " + groupName : "Group: Unknown");
                    })
                    .addOnFailureListener(e -> {
                        holder.groupName.setText("Group: Error loading");
                    });
        } else {
            holder.groupName.setText("Group: Unknown");
        }

        holder.likes.setText("Likes: " + post.getLikes());

        holder.viewPostButton.setOnClickListener(v -> {
            Intent viewDetailedPostIntent = new Intent(holder.itemView.getContext(), PostDetailsActivity.class);
            Bundle bundle = new Bundle();

            bundle.putSerializable(KEY_POST, post);
            bundle.putString(KEY_ID, postIdMap.get(post));
            bundle.putInt(KEY_INDEX, position);

            viewDetailedPostIntent.putExtras(bundle);
            context.startActivity(viewDetailedPostIntent);
        });

        holder.rowPost.setOnClickListener(v -> holder.viewPostButton.performClick());
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public PostsModel getItem(int index) {
        return postsList.get(index);
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder {
        TextView postNumber;
        TextView postMessage;
        TextView postAuthor;
        TextView authorEmail;
        TextView postDate;
        TextView groupName;
        TextView likes;
        LinearLayout rowPost;
        TextView profilePic;
        Button viewPostButton;

        public PostsViewHolder(View view) {
            super(view);
            profilePic = view.findViewById(R.id.profilePic);
            postNumber = view.findViewById(R.id.postNumber);
            groupName = view.findViewById(R.id.groupName);
            postMessage = view.findViewById(R.id.postMessage);
            postAuthor = view.findViewById(R.id.postAuthor);
            authorEmail = view.findViewById(R.id.authorEmail);
            postDate = view.findViewById(R.id.postDate);
            likes = view.findViewById(R.id.postLikes);
            rowPost = view.findViewById(R.id.rowPost);
            viewPostButton = view.findViewById(R.id.viewPostButton);
        }
    }
}
