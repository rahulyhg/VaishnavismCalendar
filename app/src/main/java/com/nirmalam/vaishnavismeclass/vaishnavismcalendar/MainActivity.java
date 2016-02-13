package com.nirmalam.vaishnavismeclass.vaishnavismcalendar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private Firebase mRef;
    private ListView calListView;
    private ArrayList<String> calArrayList;
    private Boolean getToday = Boolean.FALSE;
    private Boolean getWeek = Boolean.FALSE;
    private Boolean getAgenda = Boolean.FALSE;


    private ArrayAdapter<String> calArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calListView = (ListView) findViewById(R.id.lv);
        calArrayList = new ArrayList();
        Firebase.setAndroidContext(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    <android.support.design.widget.FloatingActionButton
        android:id="@+id/fab"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|end"
        android:layout_margin="@dimen/fab_margin"
        android:src="@android:drawable/ic_dialog_email" />

*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        Firebase.getDefaultConfig().setPersistenceEnabled(true);

        getCalendar();
    }

    private void populateList(ArrayList<String> calArrayList) {
        calArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, calArrayList);
        calListView.setAdapter(new CustomAdapter(this, calArrayList));
    }

    public void getToday(View view){
        getWeek = Boolean.FALSE;
        getAgenda = Boolean.FALSE;
        getToday = Boolean.TRUE;
        Toast.makeText(getApplicationContext(),"Today",Toast.LENGTH_LONG).show();

        getCalendar();

    }
    public void getWeek(View view){
        getWeek = Boolean.TRUE;
        getAgenda = Boolean.FALSE;
        getToday = Boolean.FALSE;
        Toast.makeText(getApplicationContext(),"This Week",Toast.LENGTH_LONG).show();
        getCalendar();
    }
    public void getAgenda(View view){
        getWeek = Boolean.FALSE;
        getAgenda = Boolean.TRUE;
        getToday = Boolean.FALSE;
        Toast.makeText(getApplicationContext(),"Agenda",Toast.LENGTH_LONG).show();
        getCalendar();
    }

    private void getCalendar(){
        mRef = new Firebase("https://vaishnavism-calendar.firebaseio.com/vaishnavismcalendar");
        // Create a handler to handle the result of the authentication
        Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                // Authenticated successfully with payload authData
            }
            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // Authenticated failed with error firebaseError
            }
        };
        // Or via popular OAuth providers ("facebook", "github", "google", or "twitter")
        mRef.authWithOAuthToken("<provider>", "<oauth-token>", authResultHandler);

        mRef.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    Log.d("Vec","User login successful!");
                } else {
                    Log.d("Vec","user is not logged in");
                }
            }
        });
        Date date = new Date();
        String modifiedDate= new SimpleDateFormat("yyyyMMdd").format(date);
        Query queryRef = mRef.orderByKey().startAt(modifiedDate).limitToFirst(1);



        if (getToday){

            modifiedDate= new SimpleDateFormat("yyyyMMdd").format(date);
            Log.d("VeC","Start with "+modifiedDate);
            queryRef = mRef.orderByKey().startAt(modifiedDate).limitToFirst(1);

        }
        if (getWeek){
            Calendar cal=Calendar.getInstance();
            cal.add( Calendar.DAY_OF_WEEK, -(cal.get(Calendar.DAY_OF_WEEK)-1));
            Log.d("Vec", String.valueOf(cal.get(Calendar.DATE)));
            modifiedDate= new SimpleDateFormat("yyyyMMdd").format(cal.get(Calendar.DATE));
            Log.d("VeC", "Start with " + modifiedDate);
            queryRef = mRef.orderByKey().startAt(modifiedDate).limitToFirst(7);

        }
        if (getAgenda){

            modifiedDate= new SimpleDateFormat("yyyyMMdd").format(date);
            Log.d("VeC","Start with "+modifiedDate);

            queryRef = mRef.orderByKey().startAt(modifiedDate);
            Log.d("Vec","got queryRef");

        }

        queryRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Log.d("Vec", snapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
            // ....
        });

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (getToday) {
                    Log.d("Vec", "Pressed today");
                }
                Log.d("VeC", "There are " + snapshot.getChildrenCount() + " calendar entries");
                String calEntries;
                //
                //reset the data to reload
                //
                calArrayList.clear();
                Log.d("Vec","Cleared calArrayList");
                for (DataSnapshot calendarSnapshot : snapshot.getChildren()) {
                    VaishnavismCalendar vaishnavismCalendar = calendarSnapshot.getValue(VaishnavismCalendar.class);

                    calEntries = calendarSnapshot.getKey()
                            + ";" + vaishnavismCalendar.getHighlightTa()
                            + ";" + vaishnavismCalendar.getStarTa()
                            + ";" + vaishnavismCalendar.getTamilDayTa()
                            + ";" + vaishnavismCalendar.getTamilMonthTa()
                            + ";" + vaishnavismCalendar.getThithiTa();

                    Log.d("VeC", calendarSnapshot.getKey()
                            + "- " + vaishnavismCalendar.getHighlightTa()
                            + " - " + vaishnavismCalendar.getStarTa()
                            + " - " + vaishnavismCalendar.getTamilDayTa()
                            + " - " + vaishnavismCalendar.getTamilMonthTa()
                            + " - " + vaishnavismCalendar.getThithiTa());
                    calArrayList.add(calEntries);

                }
                populateList(calArrayList);


            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("VC", "The read failed: " + firebaseError.getMessage());

            }


        });
    }

}
