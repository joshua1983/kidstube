package com.joshua.kidstube.control;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.joshua.kidstube.R;
import com.joshua.kidstube.data.ItemVideo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ConsultaAdapter extends BaseAdapter {
	protected ArrayList<ItemVideo> itemsVideos;
	protected Activity actividad;

	public ConsultaAdapter(Activity actividad, ArrayList<ItemVideo> items) {
		this.actividad = actividad;
		this.itemsVideos = items;
	}

	@Override
	public int getCount() {
		return itemsVideos.size();
	}

	@Override
	public Object getItem(int arg0) {
		return itemsVideos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View vi = arg1;

		if (arg1 == null) {
			LayoutInflater li = (LayoutInflater) actividad
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			vi = li.inflate(com.joshua.kidstube.R.layout.item_video, null);

		}

		ItemVideo item = (ItemVideo) getItem(arg0);
		ImageView img = (ImageView) vi.findViewById(R.id.imagen);

		DownloadImagenTask task1 = new DownloadImagenTask(img);
		task1.execute(item.getUrlPreview());

		TextView txtAsunto = (TextView) vi.findViewById(R.id.titulo);
		txtAsunto.setText(item.getTitulo());

		return vi;
	}

	private class DownloadImagenTask extends AsyncTask<String, Void, Bitmap> {

		ImageView imgView;

		public DownloadImagenTask(ImageView imagenView) {
			imgView = imagenView;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bitmap = null;
			try {
				bitmap = BitmapFactory.decodeStream((InputStream) new URL(
						params[0]).getContent());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return bitmap;
		}

		protected void onPostExecute(Bitmap resultado) {
			imgView.setImageBitmap(resultado);
		}

	}

}
