package com.luong.myfavoritewebsite;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebInfo {

    String domainName;
    Bitmap icon;

    public WebInfo(String domainName){
        this.domainName = domainName;
        icon = null;
    }

    public Bitmap getIcon() {


        DownloadTask task = new DownloadTask();
        String result = null;
        String urlIcon = "";
        try {
            result = task.execute(domainName).get();
            Log.i("html", result);
            String[] splitResult = result.split("</head>");
            Log.i("step check", "ok");
            Pattern p = Pattern.compile("<link href=\"(.*?)\" rel=\"icon\" type=\"iamge/png\"");
            Matcher m = p.matcher(splitResult[0]);
            Log.i("step three", "ok");
            if (m.find()) {

                urlIcon = m.group(1);

            }
            Log.i("step four", "ok");
            Log.i("icon url", urlIcon);
        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();

        }
		
		ImageDownloader imageTask = new ImageDownloader();
		Bitmap iconImage = null;
		try {

            iconImage = imageTask.execute(urlIcon).get();
			
		} catch (Exception e) {
            e.printStackTrace();
        }
		
        return iconImage;
    }



    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... urls) {

            try {

                URL url = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream inputStream = connection.getInputStream();

                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);

                return myBitmap;


            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }

            return null;
        }
    }


    public class DownloadTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection)url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();
                }

                return result;

            }
            catch (Exception e) {

                e.printStackTrace();

            }

            return null;
        }
    }
}
