package com.example.a5120app;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * The RestClient program implements an class that
 * use url to connect to the rest api
 */

public class RestClient {
    private static final String BASE_URL = "https://test-api-on-your-toes.herokuapp.com/";
    // private static final String MAP_URL = "https://maps.googleapis.com/maps/api/geocode/json?address=";

    /**
     * get all suburbs
     */
//    public static String getSuburb() {
//        // http://localhost:8080/getSuburb
//        String methodPath = "getSuburb";
//        return getData(methodPath).replaceAll("[^\\d.]", "");
//    }

    /**
     * get postcode by suburb name
     */
    static String getSuburbByAddress(String suburb) {
        // http://localhost:8080/getSuburb
        String methodPath = "getSuburbByName/\"" + suburb + ",_____\"";
        String result = "";
        String connReturn = getData(methodPath);
        result = connReturn.replaceAll("[^\\d.]", "");
        if (!result.equals("")) {
            result = result.substring(0, 4);
        }
        return result;
    }

    /**
     * get suburb by postcode
     */
    static String getSuburbByPostcode(String postcode) throws JSONException {
        // http://localhost:8080/getSuburb
        String methodPath = "getSuburbByPostcode/\"%25" + postcode + "\"";
        String result = "";
        String connReturn = getData(methodPath);
        if (!connReturn.equals("[]")) {
            JSONArray jsonArray = new JSONArray(connReturn);
            result = jsonArray.getJSONObject(0).getString("SuburbPostcode");
        }
        return result;
    }

    /**
     * get exercise by name
     */
    static String getExerciseByName(String name) {
        // http://localhost:8080/getSuburb
        String methodPath = "getExerciseByName/\"" + name + "\"";
        String result = "";
        String connReturn = getData(methodPath);
        if (!connReturn.equals((""))) {
            result = connReturn;
        }
        return result;
    }

    /**
     * get suburb risk score
     */
    static String getSuburbScore(String suburbAndPostcode) {
        // http://localhost:8080/getSuburbScore/"Aubrey, 3393"
        String methodPath = "getSuburbScore/\"" + suburbAndPostcode + "\"";
        String result = getData(methodPath).replaceAll("[^\\d.]", "");
        return result;
    }

    /**
     * get suburb risk indicator
     */
    static String getSuburbIndicator(String suburbAndPostcode) {
        // http://localhost:8080/getSuburbScore/"Aubrey, 3393"
        String methodPath = "getSuburbIndicator/\"" + suburbAndPostcode + "\"";
        String result = getData(methodPath).replaceAll("[^a-zA-Z ]", "").substring(9);
        return result;
    }

//    public static String getLocationPercentage(String suburbAndPostcode) {
//        // http://localhost:8080/getLocationPercentage/"Aubrey, 3393"
//        String methodPath = "getLocationPercentage/\"" + suburbAndPostcode + "\"";
//        String dataReturn = getData(methodPath);
//        String result = dataReturn;
//        return result;
//    }

    /**
     * get password hash by user name
     */
    static String getUserPasswordByName(String username) {
        String methodPath = "getUserPasswordByName/\"" + username + "\"";
        String dataReturn = getData(methodPath);
        if (dataReturn.equals("[]")) {
            return "";
        }
        return dataReturn;
    }

    /**
     * post user in the database
     */
    static void postUser(String userName, String passwordHash) throws JSONException {
        int userId = getMaxId() + 1;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("USER_ID", userId);
        jsonObject.put("USER_NAME", userName);
        jsonObject.put("PASSWORD_HASH", passwordHash);
        postData(jsonObject.toString());
    }

    /**
     * post data to database by rest api
     */
    private static void postData(String data) {
        URL url;
        HttpURLConnection conn = null;
        try {
            url = new URL(BASE_URL + "postUser");
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(data.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/json");
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(data);
            out.close();
            int responseCode = conn.getResponseCode();
            Log.i("error", Integer.valueOf(responseCode).toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
    }

//    public static void updateData(String urlStr, String id, String data) {
//        URL url = null;
//        HttpURLConnection conn = null;
//        try {
//            url = new URL(BASE_URL + urlStr + "/" + id);
//            conn = (HttpURLConnection) url.openConnection();
//            conn.setReadTimeout(10000);
//            conn.setConnectTimeout(15000);
//            conn.setRequestMethod("PUT");
//            conn.setDoOutput(true);
//            conn.setFixedLengthStreamingMode(data.getBytes().length);
//            conn.setRequestProperty("Content-Type", "application/json");
//            PrintWriter out = new PrintWriter(conn.getOutputStream());
//            out.print(data);
//            out.close();
//            int responseCode = conn.getResponseCode();
//            Log.i("error", new Integer(responseCode).toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            conn.disconnect();
//        }
//    }


    /**
     * count user amount
     */
    private static int getMaxId() {
        int id = 0;
        URL url;
        String idStr;
        String idUrlStr;

        idUrlStr = "CredentialCount";

        HttpURLConnection conn = null;

        String urlStr = BASE_URL + idUrlStr;
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestProperty("Accept", "text/plain");
            Scanner inStream = new Scanner(conn.getInputStream());
            while (inStream.hasNextLine()) {
                idStr = inStream.nextLine();
                id = Integer.parseInt(idStr.replaceAll("[^\\d.]", ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
//        [{ "COUNT(*)": 5 }]
        return id;
    }

    /**
     * get data from database by rest api
     */
    private static String getData(String urlStr) {
        URL url;
        HttpURLConnection conn = null;
        StringBuilder result = new StringBuilder();
        try {
            url = new URL(BASE_URL + urlStr);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("GET");
            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Scanner inStream = new Scanner(conn.getInputStream());
            //read the input stream and store it as string
            while (inStream.hasNextLine()) {
                result.append(inStream.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return result.toString();
    }


}

