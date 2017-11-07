package com.arm.tourist.NewsFeed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arm.tourist.Models.PostComment;
import com.arm.tourist.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by rht on 11/4/17.
 */

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.ViewHolder> {

    private List<PostComment> listItems;
    private Context context;

    public MyAdapter2(List<PostComment> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item2,parent,false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final PostComment listItem = listItems.get(position);

        /*holder.text_name.setText(listItem.getUserName());
        holder.text_comment.setText(listItem.getTourPlace());

        Picasso.with(context).load(listItem.getUserImage()).centerCrop().fit().into(holder.circleImageView);*/
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView circleImageView;
        public TextView text_name,text_comment;
        public ImageView image_send;

        public ViewHolder(View itemView) {
            super(itemView);

            circleImageView = (CircleImageView)itemView.findViewById(R.id.user_image);
            text_name = (TextView)itemView.findViewById(R.id.user_name);
            text_comment = (TextView)itemView.findViewById(R.id.text_comment);
        }
    }
}