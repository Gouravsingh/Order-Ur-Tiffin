package com.example.project;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
//import android.widget.Spinner;
import android.widget.TextView;

public class VendorInfo extends Activity {
	
	String id, mid, lid;
	String name, desc, contact, mname, price1, menu;
	JSONParser jParser;
	StringBuilder builder ;
	int success, success1, success2;
	List<String> lMeal;
	TextView spMeal;
	Button bOrder;
	ArrayAdapter<String> adapter;
	TextView tName, tDesc, tPrice, tContact;
	private static final String url_vdetails = "http://10.0.2.2/android/OrderurTiffinDatabase/vendorDetails.php";
	private static final String url_meal = "http://10.0.2.2/android/OrderurTiffinDatabase/vendorMeal.php";
	private static final String url_price = "http://10.0.2.2/android/OrderurTiffinDatabase/mealPrice.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vendor_info);
		
		id = getIntent().getExtras().getString("vid");
		mid = getIntent().getExtras().getString("id");
		lid = getIntent().getExtras().getString("idl");
		tName = (TextView)findViewById(R.id.tname);
		tDesc = (TextView)findViewById(R.id.tdesc);
		tPrice = (TextView)findViewById(R.id.tvPrice);
		tContact = (TextView)findViewById(R.id.tvContact);
		spMeal = (TextView)findViewById(R.id.spMeal);
		bOrder = (Button)findViewById(R.id.bOrder);
		lMeal = new ArrayList<String>();
		jParser = new JSONParser();
		builder= new StringBuilder();
		
		new fetchDetails().execute();
		
		//adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, lMeal);
		//spMeal.setAdapter(adapter);
		//Toast.makeText(getApplicationContext(), ""+id+mid , Toast.LENGTH_LONG).show();
		bOrder.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), CustDetails.class);
				i.putExtra("vid1", id);
				i.putExtra("mid1", mid);
				i.putExtra("price", price1);
				i.putExtra("idl", lid);
				startActivity(i);
			}
			
		});
	}

	class fetchDetails extends AsyncTask<Void, Void, Void>{
		
		JSONArray meal;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("vid", id));
			
			JSONObject json = jParser.makeHttpRequest(url_vdetails, "GET", params);
			Log.d("Response:", json.toString());
			
			params.add(new BasicNameValuePair("mid", mid));
			
			
			JSONObject json1 = jParser.makeHttpRequest(url_meal, "GET", params);
			Log.d("Response1:", json1.toString());
			
			JSONObject json2 = jParser.makeHttpRequest(url_price, "GET", params);
			Log.d("Response2:", json2.toString());
			
			try{
				success = json.getInt("success");
				JSONArray details = json.getJSONArray("vendors");
				for(int i=0;i<details.length();i++)
				{
					JSONObject det = details.getJSONObject(i);
					name = det.getString("vendor_name");
					desc = det.getString("vendor_desc");
					contact = det.getString("vendor_contact");
				}
				
				success1 = json1.getInt("success");
			//	menu = json1.getString("menu_desc");
				meal = json1.getJSONArray("items");
				
					for(int i=0;i<meal.length();i++)
					{
						JSONObject me;
						
							me = meal.getJSONObject(i);
							mname = me.getString("name");
						    lMeal.add(mname);
							
							//spMeal.setText(Arrays.toString(lMeal));
							//builder.append(mname);
						}
						
					//	if(!mname.isEmpty())
						  
							
						//	else
						//		lMeal.add(menu);				
				
				success2 = json2.getInt("success");
				JSONArray price = json2.getJSONArray("prices");
				for(int i=0;i<price.length();i++)
				{
					JSONObject pr = price.getJSONObject(i);
					price1 = pr.getString("mprice");
				}
				
			}catch(JSONException e){
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(success==1 || success1==1 || success2 == 1){
				
				tName.setText(""+name);
				tDesc.setText(""+desc);
				tContact.setText("Call us on: "+contact);
				tPrice.setText("Price per tiffin is : "+price1);
				//spMeal.setText(builder.toString());
			//	if(lMeal.isEmpty())
				//	spMeal.setText(menu);
					
				for(int p=0;p<lMeal.size();p++)
				{
					spMeal.append(lMeal.get(p));
					spMeal.append("\n");
					
				}
				
				if(spMeal.getText().toString().isEmpty())
				{
					
					spMeal.setText("Menu not available yet !!");
					bOrder.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(), "Order can't be placed !!", Toast.LENGTH_LONG).show();
						}
						
					});
				}
			}
	}
	}	
}
