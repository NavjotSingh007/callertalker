package singh.navjot.callertalker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    Switch switchref;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    void switchaction()
    {
        switchref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchref.isChecked())
                {
                    Toast.makeText(MainActivity.this, "ON", Toast.LENGTH_SHORT).show();
                    write("on", true);
                    Intent intent = new Intent(MainActivity.this, CTService.class);
                    startService(intent);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "OFF", Toast.LENGTH_SHORT).show();
                    write("on", false);
                    Intent intent = new Intent(MainActivity.this,CTService.class);
                    stopService(intent);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("testing", "onCreate");
        switchref = (Switch) findViewById(R.id.switch1);

        sharedPreferences = getSharedPreferences("sharedfile", MODE_PRIVATE);

        boolean value = read("on");

        if(value == true)
        {
            switchref.setChecked(true);
            write("on", true);
            Intent intent = new Intent(MainActivity.this, CTService.class);
            startService(intent);
        }
        else
        {
            switchref.setChecked(false);
            write("on", false);
            Intent intent = new Intent(MainActivity.this,CTService.class);
            stopService(intent);
        }

//        switchaction();
        checkedchange();
    }

    void checkedchange()
    {
        switchref.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(switchref.isChecked())
                {  Log.i("testing", "checked");
                    Toast.makeText(MainActivity.this, "ON", Toast.LENGTH_SHORT).show();
                    write("on", true);
                    Intent intent = new Intent(MainActivity.this, CTService.class);
                    startService(intent);
                }
                else{
                    Toast.makeText(MainActivity.this, "OFF", Toast.LENGTH_SHORT).show();
                    write("on", false);
                    Intent intent = new Intent(MainActivity.this,CTService.class);
                    stopService(intent);
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("testing", "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("testing", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("testing", "onPause");
    }

    boolean read(String key)
    {
        boolean booleanvalue = sharedPreferences.getBoolean(key, false);
        return booleanvalue;
    }

    void write(String key, boolean value )
    {
        editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.i("testing", "onDestroy");
    }


}
