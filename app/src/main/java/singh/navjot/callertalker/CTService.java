package singh.navjot.callertalker;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.support.annotation.IntDef;
//import android.support.v7.app.NotificationCompat;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import java.util.Locale;


public class CTService extends Service implements TextToSpeech.OnInitListener
{
    private TextToSpeech tts;

    @Override
    public void onCreate()
    {
        super.onCreate();

        Log.i("testing", "onServiceCreated");
    }

    void telephonyaction()
    {
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateListener callStateListener = new PhoneStateListener()
        {
            public void onCallStateChanged(int state, String incomingNumber)
            {


                if(state==TelephonyManager.CALL_STATE_RINGING)
                {
                    String Contact_name = getContactName(CTService.this,incomingNumber);

                    for (int i=1; i<=5; i++ )
                    {
                        tts.speak(Contact_name+" calling", TextToSpeech.QUEUE_ADD, null);
                    }


                    Toast.makeText(getApplicationContext(),"Phone is Ringing : "+incomingNumber,
                            Toast.LENGTH_LONG).show();


                }
                if(state==TelephonyManager.CALL_STATE_OFFHOOK)
                {}
                if(state==TelephonyManager.CALL_STATE_IDLE){
                    //phone is neither ringing nor in a call
                    Log.i("testing", "phone is free now");

                }
            }
        };
        telephonyManager.listen(callStateListener,PhoneStateListener.LISTEN_CALL_STATE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.i("testing", "Service Started");

        tts = new TextToSpeech(this, this);
        tts.setSpeechRate(-1000000000);  // +ve = quicker

        telephonyaction();

//        return START_REDELIVER_INTENT;
        return START_STICKY;
//        return super.onStartCommand(intent, flags, startId);
    }



    public static String getContactName(Context context, String phoneNumber)
    {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if(cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.i("testing", "Service Destroyed");
        if (tts != null)
        {
            tts.stop();
            tts.shutdown();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onInit(int status)
    {
        if (status == TextToSpeech.SUCCESS)
        {
            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
            {
                Toast.makeText(this, "This Language is not supported", Toast.LENGTH_SHORT).show();
            }
            else
            {

            }

        }
        else
        {
            Toast.makeText(this, "Initilization Failed!", Toast.LENGTH_SHORT).show();
        }
    }
}
