package pt.ulisboa.tecnico.cmov.htmlgoogledownloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends ActionBarActivity {

    TextView mTextView;
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.tv_download);
//        mTextView.setMovementMethod(new ScrollingMovementMethod());
        mImageView = (ImageView) findViewById(R.id.iv_download);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void downloadHTML(View view) {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if ((networkInfo != null) && networkInfo.isConnected()) {
            // download the page
            new DownloadWebpageTask().execute("http://upload.wikimedia.org/wikipedia/commons/2/20/Big_Ben_IJA.PNG");
            mTextView.setText("Download started...");

        } else {
            mTextView.setText("No network connection available.");
        }

    }

    /**
     * AsyncTask to fetch the data in the background away from the UI thread
     */

    private class DownloadWebpageTask extends AsyncTask<String, Void, Bitmap> {

        private HttpURLConnection mConnection;

        @Override
        protected Bitmap doInBackground(String... urls) {
//            try {
//
//                URL url = new URL(urls[0]);
//                mConnection = (HttpURLConnection) url.openConnection();
//                mConnection.setReadTimeout(10000 /* milliseconds */);
//                mConnection.setConnectTimeout(15000 /* milliseconds */);
//                mConnection.setRequestMethod("GET");
//                mConnection.setDoInput(true);
//
//                mConnection.connect();
//                int statusCode = mConnection.getResponseCode();
//                if (statusCode != HttpURLConnection.HTTP_OK) {
//                    return "Error: Failed getting update notes";
//                }
//
//                return readTextFromServer(mConnection);
//
//            } catch (IOException e) {
//                return "Error: " + e.getMessage();
//            }
            Bitmap image = null;
            try {

                URL imageUrl = new URL(urls[0]);
                Log.d("URL CREATED", "");
                image = BitmapFactory.decodeStream(imageUrl.openStream());
                if (image != null) {
                    Log.i("DL", "Successfully retrieved file!");
                    return image;
                } else {
                    Log.i("DL", "Failed decoding file from stream");
                }
            } catch (Exception e) {
                Log.i("DL", "Failed downloading file!");
                e.printStackTrace();
            }
            return image;
        }

        private String readTextFromServer(HttpURLConnection connection) throws IOException {
            InputStreamReader stream = null;
            try {
                stream = new InputStreamReader(connection.getInputStream());
                BufferedReader br = new BufferedReader(stream);
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                while (line != null) {
                    sb.append(line + "\n");
                    line = br.readLine();
                }
                return sb.toString();
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        }

        @Override
        protected void onPostExecute(Bitmap image) {
//            mTextView.setText("PAGE:" + result);
            mImageView.setImageBitmap(image);
        }
    }
}
