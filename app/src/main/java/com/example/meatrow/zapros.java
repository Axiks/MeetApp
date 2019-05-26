package com.example.meatrow;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class zapros extends Thread {
    private static final String TAG = "Neko";

    String id;
    String name;
    String surname;
    String dataTime;
    String middlename;
    InputStream is = null;
    String result = null;
    String line = null;

    ArrayList nekoClubs = new ArrayList();

    public void run()
    {
        Log.d(TAG, "Query to PHP server run");
        // создаем лист для отправки запросов
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        // один параметр, если нужно два и более просто добоовляем также
        nameValuePairs.add(new BasicNameValuePair("ID", id));

        //  подключаемся к php запросу и отправляем в него id
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.137.1/get_fio.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.d(TAG, "connection success ");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        // получаем ответ от php запроса в формате json
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            Log.d(TAG, "Read data success:" + result);
        } catch (Exception e)
        {
            Log.e(TAG, e.toString());
        }

        // обрабатываем полученный json
        try
        {
            JSONArray jsonarray = new JSONArray(result);
            for(int i=0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                id       = jsonobject.getString("id");
                name    = jsonobject.getString("name");
                surname  = jsonobject.getString("description");
                nekoClubs.add(name);
            }
            Log.d(TAG, "Convert result to JSON success");

        }
        catch(Exception e)
        {
            Log.e(TAG, e.toString());
        }
    }

    // принемаем id при запуске потока
    public void start(String idp)
    {
        this.id = idp;
        this.start();
    }

    public String resname ()
    {
        return  name;
    }
    public String ressurname ()
    {
        return  surname;
    }

    public ArrayList getClubs() {

        return nekoClubs;
    }
}
