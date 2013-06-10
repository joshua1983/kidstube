package com.joshua.kidstube;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {

	String gs_cadena_busqueda = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ImageButton btn_mickey = (ImageButton) findViewById(R.id.imageButton1);
		ImageButton btn_people = (ImageButton) findViewById(R.id.imageButton2);

		btn_mickey.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(), Seleccion.class);
				i.putExtra(
						"cadena",
						"https://gdata.youtube.com/feeds/api/playlists/PL2OEwGe1CASkQZpVU6l7s0UT_m2jfHi64?v=2&alt=jsonc");
				startActivity(i);

			}
		});

		btn_people.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(), Seleccion.class);
				i.putExtra(
						"cadena",
						"https://gdata.youtube.com/feeds/api/playlists/PL2OEwGe1CASkMbj6dHQfcuIU111sOwCm7?v=2&alt=jsonc");
				startActivity(i);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
