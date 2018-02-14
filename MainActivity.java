package ～;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.RequestToken;


public class MainActivity extends AppCompatActivity {

    private static Twitter twitter;
    private static RequestToken requestToken;

    // 最初に実行される
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth);

        /*
        * ボタンのリスナー
        */
        Button btn = (Button) findViewById(R.id.remove_twitter_instance);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //ボタンが押されたらtwitterインスタンスを破棄してMainactivityに遷移

                //Twitterインスタンスの削除
                removetoken();
                finish();
            }
        });

    }

    private void removetoken(){
        TwitterUtils.removeAccessToken(this);
        Intent intent = new Intent(this, OAuthActivity.class);
        startActivity(intent);
    }
}

