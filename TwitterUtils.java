package ～;

/**
 * Created by greentea774 on 2018/02/11.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Map;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterUtils {

    //SharedPrerence用のキー
    private static final String TOKEN = "token";
    private static final String TOKEN_SECRET = "token_secret";
    private static final String PREF_NAME = "twitter_access_token";



    //Twitterインスタンスの生成
    public static Twitter getTwitterInstance(Context context){
        // consumerKeyとconsumerSecretの設定
        String CONSUMER_KEY = context.getString(R.string.twitter_consumer_key);
        String CONSUMER_SECRET = context.getString(R.string.twitter_consumer_secret);

        TwitterFactory tf = new TwitterFactory();
        Twitter twitter = tf.getInstance();
        twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);

        //トークンの設定
        if(hasAccessToken(context)){
            twitter.setOAuthAccessToken(loadAccessToken(context));
        }
        return twitter;
    }

    //トークンの格納
    public static void storeAccessToken(Context context, AccessToken accessToken) {

        //トークンの設定
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TOKEN, accessToken.getToken());
        editor.putString(TOKEN_SECRET, accessToken.getTokenSecret());

        //トークンの保存
        editor.commit();
    }

    //トークンの読み込み
    public static AccessToken loadAccessToken(Context context) {

        //preferenceからトークンの呼び出し
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String token = preferences.getString(TOKEN, null);
        String tokenSecret = preferences.getString(TOKEN_SECRET, null);
        if(token != null && tokenSecret != null){
            return new AccessToken(token, tokenSecret);
        }
        else{
            return null;
        }
    }

    //トークンの削除
    public static void removeAccessToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();

    }

    //トークンの有無判定
    public static boolean hasAccessToken(Context context) {
        return  loadAccessToken(context) != null;
    }
}
