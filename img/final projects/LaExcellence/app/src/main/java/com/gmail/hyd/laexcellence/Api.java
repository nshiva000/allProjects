package com.gmail.hyd.laexcellence;

import com.gmail.hyd.laexcellence.Models.Edit.Edit_response;
import com.gmail.hyd.laexcellence.Models.ExamList.ExamList;
import com.gmail.hyd.laexcellence.Models.Login.Login;
import com.gmail.hyd.laexcellence.Models.QuestionsList.QuestionsList;
import com.gmail.hyd.laexcellence.Models.Submit.Submit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface Api {

    @FormUrlEncoded
    @POST("studentlogin")
    Call<Login> user_login(
            @Field("username") String username,
            @Field("password") String password
    );



    @FormUrlEncoded
    @POST("prelims")
    Call<ExamList> exam_list(
            @Field("sid") String sid
    );


    @GET("prelims/{qid}")
    Call<List<QuestionsList>> get_all_questions(@Path("qid") String qid);



    @FormUrlEncoded
    @POST("submittest")
    Call<Submit> submit_answers(
            @Field("sid") String sid,
            @Field("tid") String tid,
            @Field("answers") String ans
    );

    @FormUrlEncoded
    @POST("editprofile")
    Call<Edit_response> edit_profile(
            @Field("sid") String sid,
            @Field("email") String email
    );

   /* @GET("exam_process.php")
    Call<List<TestList>> getExamList();


    @GET("exam_list.php")
    Call<List<Question>> getQuetions(@Query("exam_id") String id);

    */
}
