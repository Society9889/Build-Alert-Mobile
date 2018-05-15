package alert.build.mobilebuildalert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.w3c.dom.Text;

import java.util.Map;
import java.util.Random;

import alert.build.mobilebuildalert.data.Build;

public class BuildAlertActivity extends AppCompatActivity {
   FirebaseAuth firebaseAuth;

    private TextView status;
    private TextView buildNumber;
    private ImageView statusIcon;
    private ProgressBar progressBar;

    private Toolbar alertToolbar;

    private DatabaseReference nRef;
    private DatabaseReference pastBuildsRef;

    private RecyclerView pastBuilds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_alert);

        firebaseAuth = firebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();
        Firebase.setAndroidContext(this);

        nRef = FirebaseDatabase.getInstance().getReference();
        pastBuildsRef = nRef.child("PastBuilds");

        alertToolbar = (Toolbar) findViewById(R.id.alertToolbar);

        setSupportActionBar(alertToolbar);
        getSupportActionBar().setTitle(user.getEmail());
        getSupportActionBar().setIcon(R.drawable.ic_user_icon);

        status = (TextView) findViewById(R.id.build_status);
        buildNumber = (TextView) findViewById(R.id.build_number);
        statusIcon = (ImageView) findViewById(R.id.build_status_icon);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        pastBuilds = (RecyclerView) findViewById(R.id.past_builds);
        pastBuilds.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        pastBuilds.setLayoutManager(mLayoutManager);

        setUpListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()){
            case R.id.logout:
                onLogOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onLogOut(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    private void setUpListeners(){
        nRef.child("RecentBuild").addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                Map<String, String> data = (Map) dataSnapshot.getValue();
                String s = data.get("status");
                String bn = String.valueOf(data.get("buildNumber"));
                if(s.equals("Build is fine")){
                    statusIcon.setBackgroundResource(R.drawable.happy);
                } else {
                    statusIcon.setBackgroundResource(R.drawable.sad);
                }
                buildNumber.setText("#" + bn);
                status.setText(s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter<Build, PastBuildsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Build, PastBuildsViewHolder>(
                        Build.class,
                        R.layout.past_build_card,
                        PastBuildsViewHolder.class,
                        pastBuildsRef
                ) {
                    @Override
                    protected void populateViewHolder(PastBuildsViewHolder pastBuildsViewHolder, Build s, int i) {
                        pastBuildsViewHolder.buildNumber.setText("#" + String.valueOf(s.getBuildNumber()));
                        pastBuildsViewHolder.result.setText(s.getStatus());
                        if(s.getStatus().equals("SUCCESS")){
                            pastBuildsViewHolder.icon.setBackgroundResource(R.drawable.happy_sm);
                        } else {
                            pastBuildsViewHolder.icon.setBackgroundResource(R.drawable.sad_sm);
                        }
                        if(pastBuilds.getVisibility() == View.INVISIBLE) {
                            pastBuilds.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                };
            pastBuilds.setAdapter(adapter);
    }
}
