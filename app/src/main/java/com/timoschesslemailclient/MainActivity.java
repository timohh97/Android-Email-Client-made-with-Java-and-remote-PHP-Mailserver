package com.timoschesslemailclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void buttonClicked(View view) throws IOException {
        EditText editTextFrom = findViewById(R.id.editText4);
        EditText editTextTo = findViewById(R.id.editText5);
        EditText editTextMessage = findViewById(R.id.editText7);

        String fromEmail = editTextFrom.getText().toString();
        String toEmail = editTextTo.getText().toString();
        String message = editTextMessage.getText().toString();

        if(validate(fromEmail))
        {
            if(validate(toEmail))
            {
                if(message.length()>4)
                {
                    Log.i("From:",fromEmail);
                    Log.i("To:",toEmail);
                    Log.i("Message:",message);
                    Toast toast = Toast.makeText(this,"Sending email......",Toast.LENGTH_SHORT);
                    toast.show();

                    SendMailTask task = new SendMailTask();

                    try {

                        task.execute("https://timoschessl.com/mailserverAndroid.php?to="+toEmail+"&from="+fromEmail+"&message="+message+"");

                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                }
                else
                {
                    Toast toast = Toast.makeText(this,"Please type in at least 5 characters for your message!",Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
            else
            {
                Toast toast = Toast.makeText(this,"The second email is not valid!",Toast.LENGTH_SHORT);
                toast.show();
            }

        }
        else
        {
            Toast toast = Toast.makeText(this,"The email is not valid!",Toast.LENGTH_SHORT);
            toast.show();
        }

    }


    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public class SendMailTask extends AsyncTask<String,Void,String> {


        @Override
        protected String doInBackground(String... urls) {

            URL url;
            HttpURLConnection httpURLConnection = null;

            String result="";

            try {

                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");

                BufferedReader b = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;

                while ((line = b.readLine()) != null) {
                    stringBuffer.append(line);
                }
                b.close();
                System.out.println(stringBuffer.toString());
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            return null;

        }
    }



}
