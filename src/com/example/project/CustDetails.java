package com.example.project;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.*;

public class CustDetails extends Activity {
   EditText tcon ,tmail , tname ,tadd, tQty ;
   TextView total;
   int suc;
   String contact_no, email_id;
   Button btnPlace;
   String vid, mid, mprice, qty, lid;
   private ProgressDialog pDialog;
   JSONParser jParser = new JSONParser(); 
   private static final String URL= "http://10.0.2.2/android/OrderurTiffinDatabase/Addorder.php";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cust_details);
		tcon = (EditText)findViewById(R.id.tcon);
		tname = (EditText)findViewById(R.id.tname);
		tadd = (EditText)findViewById(R.id.tadd);
		tmail = (EditText)findViewById(R.id.tmail);
		tQty = (EditText)findViewById(R.id.etQty);
		total = (TextView)findViewById(R.id.tTotal);
		btnPlace = (Button) findViewById(R.id.btnPlace);
		
		vid = getIntent().getExtras().getString("vid1");
		mid = getIntent().getExtras().getString("mid1");
		mprice = getIntent().getExtras().getString("price");
		lid = getIntent().getExtras().getString("idl");
		
		btnPlace.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				contact_no = tcon.getText().toString();
				email_id = tmail.getText().toString();
				qty = tQty.getText().toString();
				
				if(tname.getText().toString().isEmpty()  || tadd.getText().toString().isEmpty() || tcon.getText().toString().isEmpty() || tmail.getText().toString().isEmpty() && tQty.getText().toString().isEmpty())
				{
					Toast.makeText(getApplicationContext(), "Fill all the credentials", Toast.LENGTH_LONG).show();
				}
				
				//else if(!email_id.equals("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$") )
				//{
					//Toast.makeText(getApplicationContext(), "Invalid Email Address", Toast.LENGTH_LONG).show();
			//	}
				
				else if(!isValidEmail(email_id))
				{
					Toast.makeText(getApplicationContext(), "Invalid Email Address", Toast.LENGTH_LONG).show();
				}
				
				else if( contact_no.length() != 10  || !TextUtils.isDigitsOnly(contact_no) )
				{
					Toast.makeText(getApplicationContext(), "Invalid Mobile no", Toast.LENGTH_LONG).show();
				}
				
				else if( !TextUtils.isDigitsOnly(qty) )
				{
					Toast.makeText(getApplicationContext(), "Invalid Quantity", Toast.LENGTH_LONG).show();
				}
				
				else if( qty.equals(0))
				{
					Toast.makeText(getApplicationContext(), "Enter positive quantity", Toast.LENGTH_LONG).show();
				}
				
			else{
                  AlertDialog.Builder builder = new AlertDialog.Builder(CustDetails.this);		
                  
                  builder
                       .setMessage("Do you want to confirm your order !!")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int id) {
								// TODO Auto-generated method stub
								new InsertOrder().execute();
								
							}
						})
						.setNegativeButton("No", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int id) {
								// TODO Auto-generated method stub
								dialog.cancel();
								finish();
								Intent i = new Intent(getApplicationContext(),VendorSelect.class);
								i.putExtra("id1", lid);
								startActivity(i);
							}
						});
                  
		          	AlertDialog alert = builder.create();
		          	alert.show();
			}
			}
		});
	}

	public final static boolean isValidEmail(String email) {
		// TODO Auto-generated method stub
		
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cust_details, menu);
		return true;
	}
class InsertOrder extends AsyncTask <Void , Void , Void >{
    	
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(CustDetails.this) ; 
			pDialog.setTitle("placing your order.....");
			pDialog.setCancelable(false);
			pDialog.show();  
		}   

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
    	@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
    		String name = tname.getText().toString();
    		String contact_no = tcon.getText().toString();
    		String email_id = tmail.getText().toString(); 
    		String  address  = tadd.getText().toString();
    		qty = tQty.getText().toString();
			List<NameValuePair>   params  =new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name1" , name));
			params.add(new  BasicNameValuePair("contact1" , contact_no));
			params.add(new BasicNameValuePair("mail1" , email_id));
			params.add(new BasicNameValuePair("add1" , address));
			params.add(new BasicNameValuePair("vendor1" , vid));
			params.add(new BasicNameValuePair("meal1", mid));
			params.add(new BasicNameValuePair("price1", mprice));
			params.add(new BasicNameValuePair("qty1", qty));
			
			try {
		        JSONObject json1 = jParser.makeHttpRequest(URL , "GET" , params) ; 
		        suc = json1.getInt("success") ; 
				Log.d("response" , json1.toString());
			}
		   catch (Exception e ){
			   e.printStackTrace();
		   }
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		    if(pDialog.isShowing())   
		    	pDialog.cancel();
		    
		    if(suc == 1)
		    {
		    	//Toast.makeText(CustDetails.this, qty +" "+mprice, Toast.LENGTH_SHORT).show();
		    	int a = 0 ,b = 0 ;
		    	a= Integer.valueOf(qty);
		    	b = Integer.valueOf(mprice);
		    	int c = a * b ;
		    	//String s1 = String.valueOf(c) ;
		    	Toast.makeText(CustDetails.this, "Total Bill payable : "+ c, Toast.LENGTH_LONG).show();
		    	Intent i = new Intent(getApplicationContext(), LastActivity.class);
				startActivity(i);
		    }
		    
		}
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		
    	
    }
      
}

