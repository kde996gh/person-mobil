package hu.mobilalkfejl.person;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        this.overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
        setTitle("Névjegy");

    }

    public void backToMain(View view) {
        finish();
        this.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }
}