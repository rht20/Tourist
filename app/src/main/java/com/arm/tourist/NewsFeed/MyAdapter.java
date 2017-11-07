package com.arm.tourist.NewsFeed;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arm.tourist.Models.TourEvent;
import com.arm.tourist.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by rht on 11/4/17.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<TourEvent> listItems;
    private Context context;

    public MyAdapter(List<TourEvent> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final TourEvent listItem = listItems.get(position);

        holder.text_name.setText(listItem.getUserName());
        holder.text_place.setText(listItem.getTourPlace());
        holder.text_title.setText(listItem.getTourTitle());
        holder.text_date.setText(listItem.getStartDate()+" - "+listItem.getEndDate());
        holder.text_time.setText(listItem.getTime());
        holder.like.setText(listItem.getLikeCount());

        Picasso.with(context).load(listItem.getUserImage()).centerCrop().fit().into(holder.circleImageView);
        Picasso.with(context).load(listItem.getCover()).fit().into(holder.image_photo);

        holder.like_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ref = listItem.getPostId();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                int x = Integer.parseInt(listItem.getLikeCount());
                x++;
                String ss = Integer.toString(x);
                listItem.setLikeCount(ss);
                databaseReference.child("Tours").child(ref).child("likeCount").setValue(ss);
            }
        });

        holder.comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,CommentSection.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView circleImageView;
        public TextView text_name,text_place,text_title,text_date,text_time,like;
        public ImageView image_photo,like_button,comment_button;

        public ViewHolder(View itemView) {
            super(itemView);

            circleImageView = (CircleImageView)itemView.findViewById(R.id.user_image);
            text_name = (TextView)itemView.findViewById(R.id.user_name);
            text_place = (TextView)itemView.findViewById(R.id.user_place);
            text_title = (TextView)itemView.findViewById(R.id.user_title);
            text_date = (TextView)itemView.findViewById(R.id.user_date);
            text_time = (TextView)itemView.findViewById(R.id.postTime);
            like = (TextView)itemView.findViewById(R.id.likeCount);
            image_photo = (ImageView)itemView.findViewById(R.id.user_photo);
            like_button = (ImageView)itemView.findViewById(R.id.likeButton);
            comment_button = (ImageView)itemView.findViewById(R.id.commentButton);
        }
    }
}