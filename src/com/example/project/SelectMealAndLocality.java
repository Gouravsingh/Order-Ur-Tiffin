package com.example.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
//import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SelectMealAndLocality extends Activity implements OnClickListener, OnItemSelectedListener {

	Spinner spMeal;
	AutoCompleteTextView actvLocality;
	String[] arrMeal = {"Select Meal", "Breakfast" , "Lunch", "Dinner"};
	List<String> arrLocality;
	Button btnProceed;
	ArrayAdapter<String> adapterMeal , adapterLocality;
	String meal, mid ;
	String locality, local,id,lid;
	int id1;
	ArrayList<HashMap<String, String>> vendor ;
	HashMap < String   , String >  map ;
	JSONParser jParser = new JSONParser();
	private static final String URL_FETCH_LOCALITY = "http://10.0.2.2/android/OrderurTiffinDatabase/fetchVendorLocality.php";
	JSONArray localitie;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_meal_and_locality);
		
		createReferences();
		
		arrLocality = new ArrayList<String>();
		vendor = new ArrayList<HashMap<String, String>>();
		//Fetch locality from vendor database and store in array arrLocality
		
		new AllLocalities().execute();
		
		adapterMeal = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,arrMeal);
		
		//adapterMeal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spMeal.setAdapter(adapterMeal);
		
	    spMeal.setOnItemSelectedListener(this);
		
		btnProceed.setOnClickListener(this);
		
	}

	private void createReferences() {
		map =  new HashMap < String , String> () ; 
		spMeal = (Spinner)findViewById(R.id.spMeal);
		actvLocality = (AutoCompleteTextView)findViewById(R.id.actvLocality);
		btnProceed = (Button)findViewById(R.id.btnProceed);
	}

	@Override
	public void onClick(View view) {
		
		
		locality = actvLocality.getText().toString();
		lid  = map.get(locality) ; 
	//	Toast.makeText(getApplicationContext() , ""+locality , Toast.LENGTH_SHORT).show() ; 
	//	Toast.makeText(getApplicationContext(), ""+lid, Toast.LENGTH_LONG).show();
		if(lid == null)
		{
			Toast.makeText(getApplicationContext(), "No such locality exist !!", Toast.LENGTH_LONG).show();
		}
		
		else
		{
		if((!meal.equals("Select Meal")) && (!locality.equals("")))
		{
			//Store the data selected in spinners in database.
			
			Intent intent = new Intent(SelectMealAndLocality.this,VendorSelect.class);
			intent.putExtra("id1", lid);
			intent.putExtra("id", mid);
			startActivity(intent);
		}
		else
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(SelectMealAndLocality.this);
			
			if(!meal.equals("Select Meal"))
				builder.setMessage("Select Locality first");
			else if(!locality.equals("Select Locality"))
				builder.setMessage("Select Meal first");
			else
				builder.setMessage("Select Meal and Locality first");
			
			builder.setCancelable(false)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					dialog.cancel();
				}
			});
			
			AlertDialog alert = builder.create();
			alert.setTitle("AlertForLocality");
			alert.show();
		}  
		}
	}
	
	class AllLocalities extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			JSONObject json = jParser.makeHttpRequest(URL_FETCH_LOCALITY, "GET", params);
			Log.d("response1:",json.toString());
			
			try
			{
				int success = json.getInt("success");
				if(success == 1)
				{
					localitie = json.getJSONArray("products"); 
					for(int i=0; i < localitie.length() ;i++)
					{
						JSONObject locality = localitie.getJSONObject(i);
						id = locality.getString("lid");
						local = locality.getString("localities");
						
					//	map.put("id", id);
					//	map.put("local", local);
						map.put(local, id );
						//Toast.makeText(getApplicationContext(), ""+id, Toast.LENGTH_LONG).show();
						//vendor.add(map);
						arrLocality.add(local);
					}
				
				}
			}
			catch(JSONException ex)
			{
				ex.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			adapterLocality = new ArrayAdapter<String>(SelectMealAndLocality.this, android.R.layout.simple_dropdown_item_1line,arrLocality);
			//adapterLocality.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			actvLocality.setThreshold(1);
			actvLocality.setAdapter(adapterLocality);
			actvLocality.setTextColor(Color.BLACK);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long arg3) {
		// TODO Auto-generated method stub
		meal = parent.getItemAtPosition(pos).toString();
	    mid = String.valueOf(pos);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
