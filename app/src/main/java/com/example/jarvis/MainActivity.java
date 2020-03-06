package com.example.jarvis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.AlarmClock;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.format.DateUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final String USER_TOKEN = "name";
    private TextToSpeech mytts;
    private SpeechRecognizer mysr;
    private ImageView imgbtn, imgtool;
    private TextView txtcmnd,txtclock,txtstate;
    private WifiManager wifiManager;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        imgtool = findViewById(R.id.imagetool);
        imgtool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog();
                bottomSheetDialog.show(getSupportFragmentManager(), "bottomsheet");
            }
        });
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        initializeTextToSpeech();
        initializeSpeechRecognizer();

        imgbtn = findViewById(R.id.imagemic);
        txtcmnd = findViewById(R.id.txtcommand);
        txtclock = findViewById(R.id.txtclock);
        txtstate = findViewById(R.id.txtstate);


        final Handler ha=new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                //call function
                getTime();

                ha.postDelayed(this, 1000);
            }
        }, 1000);


        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                mysr.startListening(intent);
            }
        });
    }

    private void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            mysr = SpeechRecognizer.createSpeechRecognizer(this);
            mysr.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                    txtstate.setText("Listening...");

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {
                    txtstate.setText("");

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle bundle) {
                    List<String> results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    processResult(results.get(0));

                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }

    private void processResult(String command) {

        command = command.toLowerCase();
        txtstate.setText("");

        txtcmnd.setText(command);
        if (command.indexOf("what") != -1) {
            if (command.indexOf("your name") != -1) {
                speak("My name is jarvis");
            }
            if (command.indexOf("time") != -1) {
                Date now = new Date();
                String time = DateUtils.formatDateTime(this, now.getTime(), DateUtils.FORMAT_SHOW_TIME);
                speak("The time now is " + time + "  sir");
            }
        } else if (command.indexOf("time") != -1) {
            Date now = new Date();
            String time = DateUtils.formatDateTime(this, now.getTime(), DateUtils.FORMAT_SHOW_TIME);
            speak("The time now is " + time + "sir");
        } else if (command.indexOf("open") != -1) {
            if (command.indexOf("camera") != -1) {
                speak("opening camera");
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);
            }
            if (command.indexOf("instagram") != -1) {
                speak("opening instagram");
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.instagram.android");
                startActivity(intent);
            }
            if (command.indexOf("facebook") != -1) {
                speak("opening facebook");
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
                startActivity(intent);
            }
            if (command.indexOf("whatsapp") != -1) {
                speak("opening whatsapp");
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                startActivity(intent);
            }
            if (command.indexOf("youtube") != -1) {
                speak("opening youtube");
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
                startActivity(intent);
            }
        } else if (command.indexOf("wifi on") != -1) {
            speak("turning on wifi");
            wifiManager.setWifiEnabled(true);
        } else if (command.indexOf("wifi off") != -1) {
            speak("turning off wifi");
            wifiManager.setWifiEnabled(false);
        } else if (command.indexOf("make a call to home") != -1) {
            speak("okay sir");
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:01632147320"));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(intent);
        }
        else if (command.indexOf("who created you") != -1  || command.indexOf("who made you") != -1) {
        speak("I was created by Eevaan");
        }
        else if (command.indexOf("search for") != -1  || command.indexOf("do a search for") != -1) {
            String[] split = command.split("for");
            String substr = split[1];
            Intent intent = new Intent(getApplicationContext(),BrowserActivity.class);
            intent.putExtra(USER_TOKEN,substr);
            speak("showing results for "+substr);
            startActivity(intent);

        }
        else if (command.indexOf("thank you") != -1  || command.indexOf("good job") != -1) {
            speak("anytime sir");
        }
        else if (command.indexOf("go to") != -1  || command.indexOf("visit to") != -1) {
            String[] split = command.split("to");
            String substr = split[1];
            Intent intent = new Intent(getApplicationContext(),BrowserActivity.class);
            intent.putExtra(USER_TOKEN,substr);
            startActivity(intent);

        }
        else if (command.indexOf("remind me") != -1  || command.indexOf("set a reminder") != -1) {
            speak("Okay Sir, I will remind you.");
            initializeSpeechRecognizer();
        }
        else if (command.indexOf("goodbye") != -1) {
            finish();
            System.exit(0);

        }
        else if (command.indexOf("alarm") != -1) {

            String[] split = command.split("at");
            String substr = split[1];
            String[] split1 = substr.split(":");
            String hour = split[0];
            String minit = split[1];
            Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
            intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
            intent.putExtra(AlarmClock.EXTRA_MINUTES, minit);
            startActivity(intent);
        }
        else
        {
            speak("I can not talk about this right now.");
        }

}

    private void initializeTextToSpeech() {
        mytts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(mytts.getEngines().size()==0)
                {
                    Toast.makeText(MainActivity.this, "No engine found..!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    mytts.setLanguage(Locale.UK);
                    speak("");
                    txtcmnd.setText("welcome");
                }
            }
        });
    }

    private void speak(String message) {
        if (Build.VERSION.SDK_INT>=21)
        {
            mytts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private void getTime()
    {
        String currentTime = java.text.DateFormat.getTimeInstance().format(new Date());
        String currentDate = java.text.DateFormat.getDateInstance().format(new Date());
        txtclock.setText(currentTime+"\n\n"+currentDate);


    }

    @Override
    protected void onPause() {
        super.onPause();
        mytts.shutdown();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeTextToSpeech();
    }
}
