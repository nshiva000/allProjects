package com.gmail.hyd.laexcellence;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gmail.hyd.laexcellence.Models.QuestionsList.QuestionsList;

import java.util.ArrayList;
import java.util.List;

public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.PreviewViewHolder>{
    private Context context;
    private List<QuestionsList> list;
    CustomItemClickListener listener;

    public PreviewAdapter(Context context, List<QuestionsList> list, CustomItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PreviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.qid_item,viewGroup,false);

        final PreviewAdapter.PreviewViewHolder mViewHolder = new PreviewAdapter.PreviewViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, mViewHolder.getPosition());
            }
        });
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PreviewViewHolder previewViewHolder, int i) {
        QuestionsList questionsList  = list.get(i);




        previewViewHolder.qid.setText(questionsList.getId());

        ArrayList dv = list.get(0).getMy_ans();

        /*if (list.get(0).getMy_ans().size() == list.size()) {


            if (questionsList.getAns().toUpperCase().equals(list.get(0).getMy_ans().get(i).toString().toUpperCase())) {
                previewViewHolder.rid.setBackgroundColor(Color.GREEN);


            } else if (list.get(0).getMy_ans().get(i).toString().toUpperCase().equals("U")) {

                Log.e("ans", questionsList.getAns().toUpperCase());
                previewViewHolder.rid.setBackgroundColor(Color.WHITE);
            } else {
                previewViewHolder.rid.setBackgroundColor(Color.RED);
            }
            }
       */








    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PreviewViewHolder extends RecyclerView.ViewHolder{
        LinearLayout qid_card;
        TextView qid;
        CardView rid;
        public PreviewViewHolder(@NonNull View itemView) {
            super(itemView);




            qid = itemView.findViewById(R.id.qid);
            qid_card = itemView.findViewById(R.id.qid_card);
            rid = itemView.findViewById(R.id.rid);
        }



    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}