package appewtc.masterung.testinstallapk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private String urlString = "http://androidthai.in.th/apk/test.apk";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);




    }

    public void clickDownload(View view) {

        InstallTask installTask = new InstallTask(this, urlString);
        installTask.execute();

    }   // clickDownload

    class InstallTask extends AsyncTask<Void, Void, String> {
        ProgressDialog mProgressDialog;

        Context context;
        String url;

        public InstallTask(Context context, String url) {
            this.context = context;

            this.url = url;

        }

        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(context,
                    "Download", " Downloading in progress..");
        }

        private String downloadapk() {
            String result = "";
            try {
                URL url = new URL(this.url);
                HttpURLConnection urlConnection = (HttpURLConnection) url
                        .openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                File sdcard = Environment.getExternalStorageDirectory();
                File file = new File(sdcard, "filename.apk");

                FileOutputStream fileOutput = new FileOutputStream(file);
                InputStream inputStream = urlConnection.getInputStream();

                byte[] buffer = new byte[1024];
                int bufferLength = 0;

                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
                }
                fileOutput.close();
                result = "done";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        protected String doInBackground(Void... params) {
            String result = downloadapk();
            return result;
        }

        protected void onPostExecute(String result) {
            if (result.equals("done")) {
                mProgressDialog.dismiss();
                installApk();
            } else {
                Toast.makeText(context, "Error while downloading",
                        Toast.LENGTH_LONG).show();

            }
        }

        private void installApk() {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(new File("/sdcard/filename.apk"));
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            context.startActivity(intent);
        }

    }   // Install Task



}   // Main Class
