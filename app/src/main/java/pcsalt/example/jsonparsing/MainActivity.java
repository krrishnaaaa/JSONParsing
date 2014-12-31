package pcsalt.example.jsonparsing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {
    String TAG = getClass().getSimpleName();
    JSONObject mJsonObject;
    ListView lvPcsPost;
    PostValue postValue;
    ArrayList<PostValue> postList = new ArrayList<PostValue>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvPcsPost = (ListView) findViewById(R.id.lvPcsPost);
        lvPcsPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (postList != null && postList.size() > 0) {
                    Intent intentShowPost = new Intent(Intent.ACTION_VIEW, Uri.parse(postList.get(position).getGuid()));
                    startActivity(intentShowPost);
                }
            }
        });
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
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(MainActivity.this, "PCSalt", "Loading posts for PCSalt.com ...", true, false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            parse();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            PostBaseAdapter postBaseAdapter = new PostBaseAdapter(MainActivity.this, postList);
            lvPcsPost.setAdapter(postBaseAdapter);
            pd.dismiss();
        }
    }

}
