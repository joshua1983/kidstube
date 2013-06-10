package com.joshua.kidstube;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.joshua.kidstube.control.ConsultaAdapter;
import com.joshua.kidstube.data.ItemVideo;
import com.joshua.kidstube.utils.StreamUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Seleccion extends Activity {

	ArrayList<ItemVideo> items_videos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seleccion);

		Bundle extras = getIntent().getExtras();
		String ls_cadena = extras.getString("cadena");
		if (!ls_cadena.equals("")) {
			buscarVideos(ls_cadena);
		}

		ListView lv = (ListView) findViewById(R.id.listView1);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ItemVideo lp = (ItemVideo) arg0.getAdapter().getItem(arg2);
				if (lp != null) {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(lp
							.getUrl())));

				}
			}
		});

	}

	private void buscarVideos(String ls_cadena) {
		String cadenaBusqueda = ls_cadena;
		CargarJsonAsynTask tarea = new CargarJsonAsynTask(this);
		tarea.execute(cadenaBusqueda);

	}

	private class CargarJsonAsynTask extends
			AsyncTask<String, Void, ArrayList<ItemVideo>> {
		Activity actividad;

		public CargarJsonAsynTask(Activity act) {
			this.actividad = act;
		}

		ArrayList<ItemVideo> items;
		ProgressDialog dialog;

		protected void onPreExecute() {
			dialog = ProgressDialog.show(Seleccion.this, "Cargando",
					"Buscando videos...");

		}

		@Override
		protected ArrayList<ItemVideo> doInBackground(String... params) {
			items = getVideos(params[0]);
			return items;
		}

		protected void onPostExecute(ArrayList<ItemVideo> mylist) {
			items_videos = mylist;
			if (items_videos.size() > 0) {
				ConsultaAdapter consulta = new ConsultaAdapter(actividad,
						this.items);
				ListView lista = (ListView) findViewById(R.id.listView1);
				lista.setAdapter(consulta);
			}

			dialog.dismiss();

		}

	}

	private ArrayList<ItemVideo> getVideos(String url) {
		String ls_json = conectar(url);
		ArrayList<ItemVideo> items = new ArrayList<ItemVideo>();

		if (!ls_json.equals("")) {
			try {
				JSONObject jsonObject = new JSONObject(ls_json);
				Log.d("playlist",ls_json);
				JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("items");

				for (int i = 0; i < 10; i++) {
					JSONObject jsonObjeto = jsonArray.getJSONObject(i);
					// The title of the video
					JSONObject JSONvideo = jsonObjeto.getJSONObject("video");
					String title = JSONvideo.getString("title");
					
					try {
						url = JSONvideo.getJSONObject("player").getString(
								"mobile");
					} catch (JSONException ignore) {
						url = JSONvideo.getJSONObject("player").getString(
								"default");
					}
					// A url to the thumbnail image of the video
					// We will use this later to get an image using a Custom
					// ImageView
					// Found here
					// http://blog.blundell-apps.com/imageview-with-loading-spinner/
					String thumbUrl = JSONvideo.getJSONObject("thumbnail")
							.getString("sqDefault");
					ItemVideo video = new ItemVideo();
					video.setTitulo(title);
					video.setUrl(JSONvideo.getJSONObject("player").getString(
							"default"));
					video.setUrlPreview(thumbUrl);
					items.add(video);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return items;

	}

	private String conectar(String url) {
		HttpClient httpclient = new DefaultHttpClient();
		String resultado = "";

		// Prepare a request object
		HttpGet httpget = new HttpGet(url);

		// Execute the request
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			// Examine the response status

			// Get hold of the response entity
			HttpEntity entity = response.getEntity();
			// If the response does not enclose an entity, there is no need
			// to worry about connection release

			if (entity != null) {

				// A Simple JSON Response Read
				InputStream instream = entity.getContent();
				resultado = StreamUtils.convertToString(instream);

				instream.close();
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultado;
	}

}
