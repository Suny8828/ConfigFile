package suny.configfile;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ConfigFile.initConfig(this)) {
            ConfigFile.display();
        }
        if (ConfigFileSD.initConfig(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator)) {
            ConfigFileSD.display();
        }
    }
}
