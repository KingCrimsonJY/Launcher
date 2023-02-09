package launcher.kcjy.xyz;


import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.*;
import launcher.kcjy.xyz.launcher;


public class MainActivity extends AppCompatActivity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this,launcher.class);
        startActivity(intent);
        finish();
        
    }
}
