package pcsalt.example.jsonparsing;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {
    String TAG = getClass().getSimpleName();
    JSONObject mJsonObject;
    TextView tv;
    PostValue postValue;
    ArrayList<PostValue> postList = new ArrayList<PostValue>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);

        new JSONAsync().execute();
    }

    void parse() {
        JSONHelper mJsonHelper = new JSONHelper();

        mJsonObject = mJsonHelper.getJSONFromUrl();

        try {
            JSONArray postsArray = mJsonObject.getJSONArray("posts");

            for (int i = 0; i < postsArray.length(); i++) {
                JSONObject posts = postsArray.getJSONObject(i);
                JSONObject post = posts.getJSONObject("post");

                postValue = new PostValue();

                String title = post.getString("post_title");
                String guid = post.getString("guid");
                String date = post.getString("post_date");

                postValue.setTitle(title);
                postValue.setGuid(guid);
                postValue.setDate(date);
                postList.add(postValue);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class JSONAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            parse();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            tv.setText("");
            for (PostValue value : postList) {
                tv.append("\n\nTitle: " + value.getTitle());
                tv.append("\nGuid: " + value.getGuid());
                tv.append("\nDate: " + value.getDate());
            }
        }
    }

}
