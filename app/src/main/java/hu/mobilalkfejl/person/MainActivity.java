package hu.mobilalkfejl.person;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void listPersons(View view) {
        Intent intent = new Intent(this, PersonListActivity.class);
        startActivity(intent);
    }

    public void openAbout(View view) {
        //TODO névjegy lap megvalósítása
    }
}