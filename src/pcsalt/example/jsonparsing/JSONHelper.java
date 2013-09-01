package pcsalt.example.jsonparsing;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONHelper {
	String URL_MAIN = "http://pcsalt.com/postservice/?format=json";
	static InputStream is = null;
	static JSONObject mJsonObject = null;
	static String json = "";
	String TAG = getClass().getSimpleName();

	public JSONObject getJSONFromUrl() {
		try {
			// Create default http client to open URL
			DefaultHttpClient mDefaultHttpClient = new DefaultHttpClient();
			HttpPost mHttpPost = new HttpPost(URL_MAIN);

			// Execute the Post request and get the response from server
			HttpResponse mHttpResponse = mDefaultHttpClient.execute(mHttpPost);
			HttpEntity mHttpEntity = mHttpResponse.getEntity();

			// Get the response into the InputStream
			is = mHttpEntity.getContent();
		} catch (Exception e) {
			Log.e(TAG, "Exception: " + e.getMessage());
			e.printStackTrace();
		}

		try {
			// Read the response in InputStream and build local JSON in a String
			BufferedReader mBufferedReader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = mBufferedReader.readLine()) != null) {
				builder.append(line + "\n");
			}
			is.close();
			json = builder.toString();
		} catch (Exception e) {
			Log.e(TAG, "Exception: " + e.getMessage());
		}

		try {
			// Convert the JSON String from InputStream to a JSONObject
			mJsonObject = new JSONObject(json);
		} catch (JSONException e) {
			Log.e(TAG, "Exception: " + e.getMessage());
		}

		return mJsonObject;
	}
}
