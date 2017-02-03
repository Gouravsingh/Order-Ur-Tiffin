package com.example.project; 

	import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

	import android.os.AsyncTask;
import android.os.Bundle;
	//import android.app.Activity;
	import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;


public class OrderDetails extends Activity {

		String id,vid;
		JSONParser jParser;
		int success;
		JSONArray order ;
		TextView t1, t2, t3, t4, t5, t6;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_order_details);
			
			jParser = new JSONParser();
			id = getIntent().getExtras().getString("oid");
			vid = getIntent().getExtras().getString("vid");
			t1 = (TextView)findViewById(R.id.ta);
			t2 = (TextView)findViewById(R.id.tb);
			t3 = (TextView)findViewById(R.id.tc);
			t4 = (TextView)findViewById(R.id.td);
			t5 = (TextView)findViewById(R.id.te);
			t6 = (TextView)findViewById(R.id.tf);
			new viewOrder().execute();
		}

		class viewOrder extends AsyncTask<Void, Void, Void>
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
				params.add(new BasicNameValuePair("oid",id));
				
				JSONObject json = jParser.makeHttpRequest("http://10.0.2.2/android/OrderurTiffinDatabase/orderDetails.php", "GET", params);
				Log.d("Order response: ", json.toString());
				
				try{
					
					success = json.getInt("success");
					if(success == 1){
						order = json.getJSONArray("orders");
						
						for(int i=0;i<order.length();i++){
							JSONObject product = order.getJSONObject(i);
							String name = product.getString("name");
							String add = product.getString("add");
							String contact = product.getString("contact");
							String qty = product.getString("qty");
							String bill = product.getString("total_bill");
							String mid = product.getString("mid");
							
							t1.setText("Customer Name: "+name);
							t2.setText("Delivery Address: "+add);
							t3.setText("Contact No: "+contact);
							t4.setText("Quantity: "+qty);
							t5.setText("Meal Id: "+mid);
							t6.setText("Total Bill: "+bill);
							
				    }
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
				
				
		}
		}
		
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.view_order, menu);
			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// TODO Auto-generated method stub
			
			switch(item.getItemId())
			{
			case R.id.pass: 
				Intent i = new Intent(OrderDetails.this, PasswordChange.class);
				//i.putExtra("idv", vid);
				startActivity(i);
				return true;
				
			case R.id.update:
				Intent in = new Intent(OrderDetails.this, VendorMdetails.class);
				in.putExtra("idv", vid);
				startActivity(in);
				return true;
				
			case R.id.order:
				return true;
			}
			
			return super.onOptionsItemSelected(item);
		}
		
	}

	


