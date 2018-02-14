package ～;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Created by greentea774 on 2018/02/12.
 */
public class OAuthActivity extends Activity {

    private static String callBackURL;
    private static Twitter twitter;
    private static RequestToken requestToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: CallBack用URLの設定
        callBackURL = getString(R.string.twitter_callback_url);
        //Twitterインスタンスの取得
        twitter = TwitterUtils.getTwitterInstance(this);

        /*
        * ボタンのリスナー
        */
        Button btn = (Button) findViewById(R.id.start_oauth);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //ボタンが押されたら認証開始
                startAuthorize();
            }
        });
    }

    // 認証処理
    private void startAuthorize() {
        //AsyncTaskによる非同期処理
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    //リクエストトークンの取得
                    requestToken = twitter.getOAuthRequestToken(callBackURL);
                    return requestToken.getAuthorizationURL();
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String url) {
                if (url != null) {
                    //渡されたurlへアクティビティを遷移する
                    showToast("ブラウザ起動！");
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } else {
                    // 失敗。。。
                }
            }
        };
        task.execute();
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (intent == null || intent.getData() == null || !intent.getData().toString().startsWith(callBackURL)) {
            return;
        }
        showToast("認証されて帰ってきた！");
        //URLによって実行されたアプリから引数を取得する
        String verifier = intent.getData().getQueryParameter("oauth_verifier");

        AsyncTask<String, Void, AccessToken> task = new AsyncTask<String, Void, AccessToken>() {
            @Override
            protected AccessToken doInBackground(String... params) {
                try {
                    //アクセストークンの取得
                    return twitter.getOAuthAccessToken(requestToken, params[0]);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(AccessToken accessToken) {
                //トークンの登録
                if (accessToken != null) {
                    // 認証成功！
                    showToast("認証成功！");
                    successOAuth(accessToken);
                } else {
                    // 認証失敗。。。
                    showToast("認証失敗。。。");
                }
            }
        };
        task.execute(verifier);
    }

    private void successOAuth(AccessToken accessToken) {

        //Utilクラスからトークン登録メソッドを呼び出し
        TwitterUtils.storeAccessToken(this, accessToken);

        //別Activityへ遷移
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        //このアクティビティを終了する
        finish();
    }

    //トーストを表示するメソッド
    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }




}
