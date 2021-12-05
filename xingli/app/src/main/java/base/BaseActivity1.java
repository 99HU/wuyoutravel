package base;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity1 extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ((UniteApp)getApplication()).getActivities().add(this);
        }
        @Override
        protected void onDestroy() {
            super.onDestroy();
            ((UniteApp)getApplication()).getActivities().remove(this);
        }
}
