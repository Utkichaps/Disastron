package com.example.disastron;


import android.util.Log;
import android.os.AsyncTask;
import java.io.IOException;

//Network Managers
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

//JSOUP
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class AlertsReceiver extends AsyncTask<Object,String,String>{

    double lat;
    double lon;
    ArrayList<String> DailyAlerts;

    public AlertsReceiver(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
        _getSSLCertificate();
    }

    @Override
    protected String doInBackground(Object... params) {

        StringBuilder GeocodeUrl = new StringBuilder("https://maps.googleapis.com/maps/api/geocode/json?");
        GeocodeUrl.append("latlng=" + lat + "," + lon);
        GeocodeUrl.append("&key=" + "AIzaSyCGebJhS00UuJTvtAGt-xIkPGVUy8nHY5k");

        String GeocodeData = "";
        try {
            Log.d("GeocodeEnter", "doInBackground entered");
            String url = GeocodeUrl.toString();
            DownloadUrl downloadUrl = new DownloadUrl();
            GeocodeData = downloadUrl.readUrl(url);
            Log.d("GeocodeRead", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GeocodeAsyncError", e.toString());
        }

        DataParser dataParser = new DataParser();
        String state = dataParser.getStateFromJson(GeocodeData);
        Log.d("CurrentState",state);
        ArrayList<String> Alerts = new ArrayList<String>();
        try {
            Alerts = getAlerts(state);
            Log.d("GeocodeScrape","Alerts received");
        } catch (Exception e) {
            Log.d("GeocodeExecuteError","Didn't work fam");
            e.printStackTrace();
        }


        this.DailyAlerts = Alerts;
        return "";
    }

    @Override
    protected void onPreExecute() {
        Log.d("Pre","Execution start");
    }

    @Override
    protected void onPostExecute(String str) {
        Log.d("Post","Execution Complete");
    }

    private ArrayList<String> getAlerts(String currstate) throws IOException {


        //web scraping
        Document doc = null;
        String title = "title";
        String test = "test";

        doc = Jsoup.connect("https://ndma.gov.in/en/alerts").get();

        Elements news = doc.select("ul.latestnews");
        Elements linkbro = news.select("a");
        String linkHref = linkbro.attr("href").toString();
        //System.out.println(linkHref + '\n');

        doc = Jsoup.connect("https://ndma.gov.in/en/alerts" + linkHref).get();
        doc.outputSettings().prettyPrint(false);

        Elements realshit = doc.select("ul.latestnews");
        Elements datatime = realshit.select("a");
        String textdata = datatime.text();

        //Extracting alerts
        textdata = textdata.split("WEATHER WARNING")[1];
        String[] dates = textdata.split(":");

        ArrayList<String> alerts = new ArrayList<String>();

        for(int i=2;i<=4;i++)
        {
            String[] sentences = dates[i].toString().split("\\.");
            String state = currstate;
            String tdate = null;

            for (String str : sentences) {
                if(str.contains(state))
                {
                    if(i==2)
                        tdate = "today: ";
                    else if(i==3)
                        tdate = "tomorrow: ";
                    else if(i==4)
                        tdate = "day after: ";
                    alerts.add(tdate + str);
                }
            }
        }

        return alerts;
    }


    private void _getSSLCertificate() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }
        } };
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
