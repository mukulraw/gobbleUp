package com.gobble.gobble_up;

import android.content.Context;


public class Util {
	Context context;
	public Util(Context context){
		this.context = context;
	}
	
	public static boolean emailValidater(String email){
		boolean isValid;
		String emailStr = email.trim();

		String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

		isValid = emailStr.matches(emailPattern);
		return isValid;
	}

	/*public void showDialog(String msg){
		
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle("Alert!");
		alertDialog.setMessage(msg);
		
		 alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                	
                }
        });
 
        alertDialog.show();
	}*/
	
/*
	public void showDialog(String string){
		Button cancelBtn,contBtn;
		TextView textView,header;
			
			final Dialog alertDialog = new Dialog(context);
			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			alertDialog.setContentView(R.layout.alart_dialog);
			cancelBtn = (Button) alertDialog.findViewById(R.id.conCancel_btn);
			contBtn = (Button) alertDialog.findViewById(R.id.keepRes_btn);
			textView = (TextView) alertDialog.findViewById(R.id.confirmcancel_txt);
			header = (TextView) alertDialog.findViewById(R.id.textView11);
			textView.setText(string);
			
			alertDialog.setTitle("Alert!");
		//	alertDialog.setMessage(msg);
			
			android.graphics.Typeface face1 = android.graphics.Typeface
					.createFromAsset(context.getAssets(), "font/RobotoRegular.ttf");
			
			cancelBtn.setTypeface(face1);
			contBtn.setTypeface(face1);
			textView.setTypeface(face1);
			header.setTypeface(face1);
			
			cancelBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					*//*savePref.saveCurrentRideId("0");
					startActivity(new Intent(FindingNearest.this,PickupLocation.class));
					PickupLocation.isVehicalSelected = false;
					Constants.dropoffLocation = "";
					finish();
					*//*
					//new CancelRide().execute();
				}
			});
			
			contBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
				}
			});
	 try{
	        alertDialog.show();
	 }catch(Exception e){
		 e.printStackTrace();
	 }
		}
	*/
	
	
/*
public String RegisterWithGCM(Context ctx)
{      
	String regId;
	SavePref savePref = new SavePref(context);
	try{
    GCMRegistrar.checkDevice(context);
    GCMRegistrar.checkManifest(context);
    regId = GCMRegistrar.getRegistrationId(context);
    if (regId.equals("")) {
      GCMRegistrar.register(context, "744279721335"); // Note: get the sender id from configuration.
    } else {
         
      Log.v("Registration", "Already registered, regId: " + regId);
    }
    savePref.saveRegId(regId);
    return regId;
	}
	catch(Exception e)
	{
		e.printStackTrace();
		return null;
	}
}*/


}
