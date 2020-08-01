package com.gmail.hyd.laexcellence;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gmail.hyd.laexcellence.Models.ExamList.Datum;
import com.gmail.hyd.laexcellence.Models.ExamList.ExamList;
import com.gmail.hyd.laexcellence.Storage.SharedPrefManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private String sid,image,name;
    private DrawerLayout drawerLayout;

    private TextView nav_textView;
    private ImageView nav_imageView;

    private Button rec_btn;

    SwipeRefreshLayout pullToRefresh;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Datum>  arrayList= new ArrayList();
    List<Datum> exam_list_data;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_list);


        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        pullToRefresh = findViewById(R.id.pullToRefresh);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setIcon(R.drawable.ic_launcher_background);

        getSupportActionBar().setDisplayShowTitleEnabled(false);




        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);


        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);


        nav_textView = header.findViewById(R.id.nav_header_textView);
        nav_imageView = header.findViewById(R.id.nav_header_imageView);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();



        recyclerView = findViewById(R.id.test_recycleView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        sid = SharedPrefManager.get_mInstance(this).getSid();
        image = SharedPrefManager.get_mInstance(this).getImage();
        name = SharedPrefManager.get_mInstance(this).getName();

        nav_textView.setText(name);

        Glide.with(getApplicationContext()).load(image).into(nav_imageView);



        getExamList(sid);
        Log.e("studenid",sid);




        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getExamList(sid);
                pullToRefresh.setRefreshing(false);

            }
        });

    }



    private void sortArrayList(){

        Collections.sort(exam_list_data, new Comparator<Datum>() {
            public int compare(Datum o1, Datum o2) {
                if (o1.getDate() == null || o2.getDate() == null)
                    return 0;
                return o2.getDate().compareTo(o1.getDate());
            }
        });



        adapter.notifyDataSetChanged();

    }





    private void getExamList(String s) {

        final ProgressDialog progressDialog=new ProgressDialog(TestList.this);
        progressDialog.setMessage("Loading,Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<ExamList> call = RetrofitClient.getmInstance().getApi().exam_list(sid);

        call.enqueue(new Callback<ExamList>() {
            @Override
            public void onResponse(Call<ExamList> call, Response<ExamList> response) {
                ExamList result = response.body();


                progressDialog.dismiss();

                assert result != null;
                if (result.getSuccess()){

                    exam_list_data = result.getData();



                    adapter = new TestListAdapter(getApplicationContext(),exam_list_data);
                    recyclerView.setAdapter(adapter);

                    sortArrayList();

                }



            }

            @Override
            public void onFailure(Call<ExamList> call, Throwable t) {
                Log.e("err",t.getMessage()+"");
                Toast("No Internet");
                progressDialog.dismiss();
            }
        });

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            logout();
            Toast("logout");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if (id == R.id.nav_item_one) {
            Toast(" Profile");
            Intent intent = new Intent(TestList.this,Profile.class);
            intent.putExtra("user_name",name);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_item_two) {

            Intent i = new Intent(android.content.Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.gmail.hyd.laexcellence"));
            startActivity(i);
            //Toast("share link");

        } else if (id == R.id.nav_item_three) {
            Intent i = new Intent(android.content.Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://laex.in/aboutus.php"));
            startActivity(i);
           // Toast("about us link ");
        }

//        } else if (id == R.id.nav_item_four) {
//            Toast("four link");
//
//        } else if (id == R.id.nav_item_five) {
//            Toast("five link");
//
//        } else if (id == R.id.nav_item_six) {
//            Toast("six link");
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }


    private void Toast(String s){
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }



    public void logout(){

        SharedPrefManager.get_mInstance(getApplicationContext()).clear();
        Intent intent = new Intent(TestList.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
