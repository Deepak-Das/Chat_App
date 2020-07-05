package com.example.chat_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.example.chat_app.Adapters.ViewPagerAdapter;
import com.example.chat_app.Fragments.ChatFragment;
import com.example.chat_app.Fragments.ProfileFragment;
import com.example.chat_app.Fragments.UsersFragment;
import com.example.chat_app.Models.User;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    CircleImageView profile_img;
    MaterialTextView toolbar_username;

    Toolbar toolbar;

    FirebaseUser currentUser;
    FirebaseDatabase database;
    DatabaseReference userRef;

    TabLayout tabLayout;
    TabItem tabChat,tabUsers;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.main_menu_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        profile_img=findViewById(R.id.profile_image);
        toolbar_username=findViewById(R.id.main_menu_toolbar_username);

        database=FirebaseDatabase.getInstance();
        currentUser=FirebaseAuth.getInstance().getCurrentUser();

        String user_id=currentUser.getUid();

        userRef=database.getReference("Users").child(user_id);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                toolbar_username.setText(user.getUser_name());
                if(user.getImage_URL().equals("default")){
                    profile_img.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Glide.with(MainActivity.this).load(user.getImage_URL()).into(profile_img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.view_pager);


        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragmentAndTitle(new ChatFragment(),"Chat");
        viewPagerAdapter.addFragmentAndTitle(new UsersFragment(),"Users");
        viewPagerAdapter.addFragmentAndTitle(new ProfileFragment(),"Profile");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                Intent intent =new Intent(MainActivity.this,StartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}