package com.example.home_zhang.cpms.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.home_zhang.cpms.MainActivity;
import com.example.home_zhang.cpms.R;
import com.example.home_zhang.cpms.activity.ProblemDescriptionActivity;
import com.example.home_zhang.cpms.model.Problem;

import java.util.List;

/**
 * Created by czhang on 3/5/2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private List<Problem> dataSet;
    Boolean check = false;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView number, difficulty, title, tags, question_id;
        RelativeLayout expandable;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.expandable = (RelativeLayout) itemView.findViewById(R.id.expandableLayout);
            this.number = (TextView) itemView.findViewById(R.id.number);
            this.difficulty = (TextView) itemView.findViewById(R.id.difficulty);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.tags = (TextView) itemView.findViewById(R.id.tags);
            this.question_id = (TextView) itemView.findViewById(R.id.question_id);
        }
    }

    public CustomAdapter(List<Problem> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_row, parent, false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailView = new Intent(v.getContext(), ProblemDescriptionActivity.class);
                TextView tv = (TextView)v.findViewById(R.id.question_id);
                Bundle b = new Bundle();
                b.putString("prob_Id", tv.getText().toString());
                detailView.putExtras(b);
                v.getContext().startActivity(detailView);
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView numberView = holder.number;
        TextView difficultyView = holder.difficulty;
        TextView titleView = holder.title;
        TextView tagsView = holder.tags;
        TextView questionIdView = holder.question_id;

        numberView.setText(dataSet.get(listPosition).getNo());
        difficultyView.setText(dataSet.get(listPosition).getDifficultyLevel());
        titleView.setText(dataSet.get(listPosition).getTitle());
        tagsView.setText(dataSet.get(listPosition).getTags());
        questionIdView.setText(dataSet.get(listPosition).getId());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
