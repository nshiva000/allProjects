package com.gmail.hyd.laexcellence;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.hyd.laexcellence.Models.QuestionsList.QuestionsList;
import com.gmail.hyd.laexcellence.Storage.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Preview extends AppCompatActivity implements View.OnClickListener{


    String exam_id,sid,my_answers,correct_answer,marks_s,rank_s;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<QuestionsList>  arrayList= new ArrayList();
    private int current_id;
    private List<QuestionsList> aquestionsLists;

    ArrayList ans_array = new ArrayList();

    String foo = my_answers;

    TextView prev_qid,prev_question,prev_a,prev_b,prev_c,prev_d,prev_answer,prev_explanation,correct_ans,marks,rank;
    Button p_btn,n_btn;


    private List<QuestionsList> q_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        prev_qid = findViewById(R.id.preview_qid);
        prev_question = findViewById(R.id.preview_question);
        prev_a = findViewById(R.id.preview_a);
        prev_b = findViewById(R.id.preview_b);
        prev_c = findViewById(R.id.preview_c);
        prev_d = findViewById(R.id.preview_d);
        prev_answer = findViewById(R.id.preview_ans);
        prev_explanation = findViewById(R.id.preview_explanation);
        correct_ans = findViewById(R.id.correct_ans);
        marks = findViewById(R.id.preview_marks);
        rank = findViewById(R.id.correct_rank);


        p_btn  = findViewById(R.id.btn_previous);
        n_btn  = findViewById(R.id.btn_next);

        p_btn.setOnClickListener(this);
        n_btn.setOnClickListener(this);







        exam_id = getIntent().getStringExtra("examId");
        sid = SharedPrefManager.get_mInstance(this).getSid();
        my_answers = getIntent().getStringExtra("my_answers");
        marks_s = getIntent().getStringExtra("marks");
        rank_s = getIntent().getStringExtra("rank");

//        if (marks_s.isEmpty()){
//            String str = marks_s;
//
//            if(str.length() > 0){
//                String substr = ".";
//                String before = str.substring(0, str.indexOf(substr));
//                marks.setText("Marks : "+before);
//            }
//
//        }


        marks.setText("Marks : "+marks_s);


        if (!rank_s.isEmpty()){
            String str1 = rank_s;


            if(str1.length() > 0){
                String substr1 = ".";
                String before1 = str1.substring(0, str1.indexOf(substr1));
                rank.setText("Rank : "+before1);
            }



        }




        Log.e("details",exam_id+my_answers+sid);


        recyclerView = findViewById(R.id.p_qid);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
      setSupportActionBar(toolbar);
       // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }




        getAnsList(sid);
        
        boolean q = false;


        String foo = my_answers;
        String[] split = foo.split(",");
        for (int i = 0; i < split.length; i++) {

            ans_array.add(split[i]);

        }


        Log.e("explode",ans_array.toString());


        //Log.e("q", q+"");


    }



    private void getAnsList(String s) {

        final ProgressDialog progressDialog=new ProgressDialog(Preview.this);
        progressDialog.setMessage("Loading,Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Call<List<QuestionsList>> call = RetrofitClient.getmInstance().getApi().get_all_questions(exam_id);

        call.enqueue(new Callback<List<QuestionsList>>() {
            @Override
            public void onResponse(Call<List<QuestionsList>> call, Response<List<QuestionsList>> response) {
               final List<QuestionsList> questionsLists = response.body();

                int id = 1;
                for (int i=0; i<questionsLists.size();i++){
                    questionsLists.get(i).setId(id+"");
                    id++;
                }

                aquestionsLists = questionsLists;
                progressDialog.dismiss();

                if (ans_array.size() > 1){
                    questionsLists.get(0).setMy_ans(ans_array);
                }else {

                    //ArrayList arrayList_new_u = new ArrayList();

                    for (int i=0; i<questionsLists.size();i++){
                        ans_array.add(i,"U");
                    }

                    questionsLists.get(0).setMy_ans(ans_array);
                }



                Log.e("my",ans_array.size()+""+questionsLists.size()+ans_array.toString());

                Log.d("recycler", "clicked position:" + questionsLists.get(0).getQuestion());

                correct_answer = "Correct ans :"+questionsLists.get(0).getAns().toUpperCase();

                prev_qid.setText(questionsLists.get(0).getId());
                prev_question.setText(questionsLists.get(0).getQuestion());

                prev_a.setText("A) "+questionsLists.get(0).getA());
                prev_b.setText("B) "+questionsLists.get(0).getB());
                prev_c.setText("C) "+questionsLists.get(0).getC());
                prev_d.setText("D) "+questionsLists.get(0).getD());
                prev_explanation.setText(questionsLists.get(0).getExplanation());
                correct_ans.setText(correct_answer);
                initial(questionsLists,ans_array);






                adapter = new PreviewAdapter(getApplicationContext(),questionsLists, new CustomItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        Log.d("recycler", "clicked position:" + position);
                        current_id = position;


                        ArrayList ans_aa = questionsLists.get(0).getMy_ans();


                        prev_qid.setText(questionsLists.get(current_id).getId());
                        prev_question.setText(questionsLists.get(current_id).getQuestion());
                        correct_answer = "Correct ans :"+questionsLists.get(current_id).getAns().toUpperCase();

                        prev_a.setText("A) "+questionsLists.get(current_id).getA());
                        prev_b.setText("B) "+questionsLists.get(current_id).getB());
                        prev_c.setText("C) "+questionsLists.get(current_id).getC());
                        prev_d.setText("D) "+questionsLists.get(current_id).getD());
                        prev_explanation.setText(questionsLists.get(current_id).getExplanation());
                        correct_ans.setText(correct_answer);


                        initial(questionsLists,ans_aa);

                    }
                });

                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<QuestionsList>> call, Throwable t) {
               Toasta("No Internet");
               progressDialog.dismiss();
            }
        });



    }





   public void initial(List<QuestionsList> questionsLists,ArrayList ans_aa){

       correct_answer = "Correct ans :"+questionsLists.get(current_id).getAns().toUpperCase();

       correct_ans.setText(correct_answer);

       switch (questionsLists.get(current_id).getAns().toUpperCase()){
           case "A":
               prev_a.setTextColor(getResources().getColor(R.color.green));
               prev_b.setTextColor(Color.BLACK);
               prev_c.setTextColor(Color.BLACK);
               prev_d.setTextColor(Color.BLACK);
               break;
           case "B":
               prev_b.setTextColor(getResources().getColor(R.color.green));
               prev_a.setTextColor(Color.BLACK);
               prev_c.setTextColor(Color.BLACK);
               prev_d.setTextColor(Color.BLACK);
               break;
           case "C":
               prev_c.setTextColor(getResources().getColor(R.color.green));
               prev_b.setTextColor(Color.BLACK);
               prev_a.setTextColor(Color.BLACK);
               prev_d.setTextColor(Color.BLACK);
               break;
           case "D":
               prev_d.setTextColor(getResources().getColor(R.color.green));
               prev_b.setTextColor(Color.BLACK);
               prev_a.setTextColor(Color.BLACK);
               prev_c.setTextColor(Color.BLACK);
               break;
       }

       String your_ans = ans_aa.get(current_id).toString().toUpperCase();
       String your_ans_output;


       if (your_ans.equals("U")){
           your_ans_output = "Not Attempted";
       }
       else {
           your_ans_output = "Your ans is : "+your_ans;
       }



       prev_answer.setText(your_ans_output);

   }


    public static long compareTo( java.util.Date date1, java.util.Date date2 )


    {
      //returns negative value if date1 is before date2
      //returns 0 if dates are even
      //returns positive value if date1 is after date2
        return date1.getTime() - date2.getTime();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_previous:
                previous();
                break;
            case R.id.btn_next:
                next();
                break;
        }
    }

    private void next() {


        int size_questions =  aquestionsLists.size();


        //Toast(size_questions+"");

        Log.e("position",current_id+"");

        if (current_id != size_questions-1){
            current_id++;
            initial(aquestionsLists,ans_array);

            Log.e("size",size_questions+"");
            Log.e("Currenid",current_id+"");
        }




        if (current_id < size_questions) {
            prev_qid.setText(aquestionsLists.get(current_id).getId());
            prev_question.setText(aquestionsLists.get(current_id).getQuestion());

            prev_a.setText("A) "+aquestionsLists.get(current_id).getA());
            prev_b.setText("B) "+aquestionsLists.get(current_id).getB());
            prev_c.setText("C) "+aquestionsLists.get(current_id).getC());
            prev_d.setText("D) "+aquestionsLists.get(current_id).getD());
            prev_explanation.setText(aquestionsLists.get(current_id).getExplanation());

            Log.e("position+", current_id + "");

        }
        if(current_id == aquestionsLists.size()-1) {
            Toasta("limit completed");
        }

    }

    private void Toasta(String s) {

        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }

    private void previous() {




        Log.e("position",current_id+"");
        if (current_id != 0){
            Log.e("position-", current_id + "");
            current_id--;
        }




        if (current_id >= 0) {
            initial(aquestionsLists,ans_array);
            prev_qid.setText(aquestionsLists.get(current_id).getId());
            prev_question.setText(aquestionsLists.get(current_id).getQuestion());


            prev_a.setText("A) "+aquestionsLists.get(current_id).getA());
            prev_b.setText("B) "+aquestionsLists.get(current_id).getB());
            prev_c.setText("C) "+aquestionsLists.get(current_id).getC());
            prev_d.setText("D) "+aquestionsLists.get(current_id).getD());
            prev_explanation.setText(aquestionsLists.get(current_id).getExplanation());
           // correct_ans.setText(correct_answer);
            Log.e("position+", current_id + "");


        }else {
            Toasta("limit completed");
        }
    }
}
