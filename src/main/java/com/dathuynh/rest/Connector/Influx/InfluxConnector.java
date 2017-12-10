package com.dathuynh.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.dathuynh.rest.InfluxConfig;

public class InfluxConnector {

  public static void createDatabase(String dbname) throws Exception {
    try {
      URL url = new URL(InfluxConfig.API_ENDPOINT + "/query?q=DATABASE+CREATE+\""+ dbname +"\"");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Accept", "application/json");
      if (conn.getResponseCode() != 200) {
        throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
      }
      BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String output;
      System.out.println("Output from Server .... \n");
      while ((output = br.readLine()) != null) {
        System.out.println(output);
      }
      conn.disconnect();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void writeData(String db, String data) throws Exception{
    try {
      URL url = new URL(InfluxConfig.API_ENDPOINT + "/write");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setDoOutput(true);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");

      String input = "";

      OutputStream os = conn.getOutputStream();
      os.write(input.getBytes());
      os.flush();

      if (conn.getResponseCode() != 200) {
        throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
      }
      BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String output;
      System.out.println("Output from Server .... \n");
      while ((output = br.readLine()) != null) {
        System.out.println(output);
      }
      conn.disconnect();
    } catch (MalformedURLException e){
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
