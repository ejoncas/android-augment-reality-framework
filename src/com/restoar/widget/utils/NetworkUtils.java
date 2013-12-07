package com.restoar.widget.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class NetworkUtils {

	protected static final int MAX = 5;
	protected static final int READ_TIMEOUT = 10000;
	protected static final int CONNECT_TIMEOUT = 10000;

	static class Communicator {
		public String executeHttpGet(String URL) {
			BufferedReader in = null;
			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet();
				request.setHeader("Content-Type", "text/plain; charset=utf-8");
				request.setURI(new URI(URL));
				HttpResponse response = client.execute(request);
				in = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));

				StringBuffer sb = new StringBuffer("");
				String line = "";

				String NL = System.getProperty("line.separator");
				while ((line = in.readLine()) != null) {
					sb.append(line + NL);
				}
				in.close();
				String page = sb.toString();
				// System.out.println(page);
				return page;
			} catch (Exception e) {
				Log.e("Communicator", "Error retriveing url " + URL, e);
				return "";
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						Log.d("BBB", e.toString());
					}
				}
			}
		}
	}

	public static JSONObject parseUrlAsJson(String url) {
		String response = new Communicator().executeHttpGet(url);
		JSONObject json = null;
		try {
			json = new JSONObject(response);
		} catch (JSONException e) {
			Log.e("NetworkUtils", "Error converting response to json. Response is:" + response, e);
		}
		if (json == null)
			throw new NullPointerException();
		return json;
	}

	public static InputStream getHttpGETInputStream(String urlStr) {
		if (urlStr == null)
			throw new NullPointerException();

		InputStream is = null;
		URLConnection conn = null;

		try {
			if (urlStr.startsWith("file://"))
				return new FileInputStream(urlStr.replace("file://", ""));

			URL url = new URL(urlStr);
			conn = url.openConnection();
			conn.setReadTimeout(READ_TIMEOUT);
			conn.setConnectTimeout(CONNECT_TIMEOUT);

			is = conn.getInputStream();

			return is;
		} catch (Exception ex) {
			try {
				is.close();
			} catch (Exception e) {
				// Ignore
			}
			try {
				if (conn instanceof HttpURLConnection)
					((HttpURLConnection) conn).disconnect();
			} catch (Exception e) {
				// Ignore
			}
			ex.printStackTrace();
		}

		return null;
	}

	public static String getHttpInputString(InputStream is) {
		if (is == null)
			throw new NullPointerException();

		BufferedReader reader = new BufferedReader(new InputStreamReader(is),
				8 * 1024);
		StringBuilder sb = new StringBuilder();

		try {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
