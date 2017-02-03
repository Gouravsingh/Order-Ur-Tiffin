package com.example.project;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class VendorMdetails extends Activity implements OnItemSelectedListener {
	
	Spinner spMeal;
	EditText eMeal1, ePrice, eMeal2, eMeal3, eMeal4, eMeal5, eMeal6;
	Button bSave, bLogout;
	JSONParser jParser;
	String mid, vid, m1, m2, m3, m4, m5, m6, total;
	int success, success1;
	//String[] day = {"---","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
	String[] type = {"---","Breakfast", "Lunch", "Dinner"};
	ArrayAdapter<String> adapter1;
	String URL = "http://10.0.2.2/android/OrderurTiffinDatabase/updatemeal.php";
	String URL_price = "http://10.0.2.2/android/OrderurTiffinDatabase/updatePrice.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vendor_mdetails);
		
		jParser = new JSONParser();
		spMeal = (Spinner)findViewById(R.id.spMeal);
		eMeal1 = (EditText)findViewById(R.id.etMeal1);
		ePrice = (EditText)findViewById(R.id.ePrice);
		bLogout = (Button)findViewById(R.id.bLogout);
		bSave = (Button)findViewById(R.id.bSave);
		
		vid = getIntent().getExtras().getString("idv");
		
		adapter1 = new ArrayAdapter<String>(VendorMdetails.this, android.R.layout.simple_spinner_item, type);
		spMeal.setAdapter(adapter1);
		spMeal.setOnItemSelectedListener(this);
		
		bSave.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				m1 = eMeal1.getText().toString();
				total = ePrice.getText().toString();
				
				if(m1.isEmpty() && total.isEmpty())
				{
					Toast.makeText(getApplicationContext(), "Required fields are missing to update menu !!", Toast.LENGTH_LONG).show();
				}
				
				else if(m1.isEmpty() && !total.isEmpty())
				{
					Toast.makeText(getApplicationContext(), "Item name is empty !!", Toast.LENGTH_LONG).show();
				}
				
				else if(!m1.isEmpty() && total.isEmpty())
				{
					Toast.makeText(getApplicationContext(), "Price is missing !!", Toast.LENGTH_LONG).show();
				}
				
				else{
				new updateMenu().execute();
				
				
				}
			}
			
		});
		
		bLogout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),SecondActivity.class);
				finish();
				startActivity(i);
			}
			
		});
	}
	
	class updateMenu extends AsyncTask<Void, Void, Void>
	{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("idm", mid ));
			params.add(new BasicNameValuePair("idv", vid));
			params.add(new BasicNameValuePair("c1", m1));
			params.add(new BasicNameValuePair("price", total));
			
			JSONObject json = jParser.makeHttpRequest(URL, "GET", params);
			Log.d("meal Respose: ", json.toString());
			
			JSONObject json1 = jParser.makeHttpRequest(URL_price, "GET", params);
			Log.d("price Response: ", json1.toString());
			
			try{
				success = json.getInt("success");
				success1 = json1.getInt("success");
			}catch(JSONException e){
				
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(success == 1 && success1 == 1){
				Toast.makeText(getApplicationContext(), "Menu and price updated successfully", Toast.LENGTH_LONG).show();
				
				eMeal1.setText("");
				ePrice.setText("");
			}
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int pos,
			long arg3) {
		// TODO Auto-generated method stub
		mid = String.valueOf(pos);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "Select the type of meal !!", Toast.LENGTH_LONG).show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.vendor_mdetails, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch(item.getItemId())
		{
		case R.id.pass: 
			Intent i = new Intent(this, PasswordChange.class);
			i.putExtra("idv", vid);
			startActivity(i);
			return true;
			
		case R.id.update:
			return true;
			
		case R.id.order:
			Intent in = new Intent(this, ViewOrder.class);
			in.putExtra("idv", vid) ; 
			startActivity(in);
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
