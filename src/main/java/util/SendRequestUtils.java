package util;

import okhttp3.*;

import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SendRequestUtils {

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType File = MediaType.parse("application/x-tar");
    private static final String METHOD_GET = "GET";


    public Map<String, String> send(String json, String urls, String method) {
        OkHttpClient client;

        client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();

        RequestBody body = null;

        if (json != null) {
            body = RequestBody.create(JSON, json);
        } else if (!METHOD_GET.equals(method)) {
            body = RequestBody.create(JSON, "{}");
        }
        if ("HEAD".equals(method)) {
            body = null;
        }

        Request request = new Request.Builder()
                .url(urls).method(method, body)
                .build();

        Response response;
        HashMap<String, String> map = new HashMap<>(4);

        try {
            response = client.newCall(request).execute();
            map.put("responseCode", String.valueOf(response.code()));
            map.put("responseData", response.body().string());
//            map.put("responseHeader",response.headers().toMultimap().toString());

        } catch (ConnectException e) {
            e.printStackTrace();
            map = new HashMap<>(0);
        } catch (Exception e) {
            e.printStackTrace();
            map = new HashMap<>(0);
        } finally {
            return map;
        }
    }

    public Map<String, String> sendUrl(String json, String urls, String method, String header) {
        OkHttpClient client;
        client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();

        RequestBody body = null;
        HashMap<String, String> map = new HashMap<>(4);
        if (json != null) {
            body = RequestBody.create(JSON, json);
        } else if (!METHOD_GET.equals(method)) {
            body = RequestBody.create(JSON, "{}");
        }
        Request request = new Request.Builder().addHeader("X-Registry-Auth", header)
                .url(urls).method(method, body).build();
        Response response;
        try {
            response = client.newCall(request).execute();
            map.put("responseCode", String.valueOf(response.code()));
            map.put("responseData", response.body().string());
            map.put("responseHeader", response.headers().toString());
        } catch (Exception e) {
            e.printStackTrace();
            map = new HashMap<>(0);
        } finally {
            return map;
        }
    }

    public Map<String, String> uploadImage(String urls, String method, java.io.File file) {
        OkHttpClient client;
        client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();

        RequestBody body = null;
        Response response;
        HashMap<String, String> map = new HashMap<>(4);
        if (file.exists()) {
            body = RequestBody.create(File, file);
        }
        Request request = new Request.Builder()
                .url(urls).method(method, body)
                .build();
        try {
            response = client.newCall(request).execute();
            map.put("responseCode", String.valueOf(response.code()));
            map.put("responseData", response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
            map = new HashMap<>(0);
        } catch (Exception e) {
            e.printStackTrace();
            map = new HashMap<>(0);
        } finally {
            return map;
        }
    }
}