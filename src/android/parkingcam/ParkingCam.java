/**
	@file	ParkingCam.java
	@date	2015/08/10
	@author	JawHwanM
	@brief	ParkingCam Main
*/

package android.parkingcam;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.parkingcam.activity.BaseTemplate;
import android.parkingcam.camera.CameraCapture;
import android.parkingcam.common.Constants;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

/**
 * ParkingCam Main
 * @author JawHwanM
 * @since 2015.08.10
 * @version 1.0
 * @see <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2015.08.10  JawHwanM          최초 생성
 * 
 * </pre>
 */
public class ParkingCam extends BaseTemplate
{	
	private LocationManager 	mClsLocationMgr;			/**< 위치기반 매니져 */
	private LocationListener 	mClsLocationListener;		/**< 위치기반 리스너 */
	private boolean mBoolAppFirstLoading = false;
	
	/**
	 * Activity 생성 이벤트
	 * @param savedInstanceState 상태정보
	 */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);        
        super.initTemplate(this, R.layout.parking_cam);
        
        initViewControl();
        initLocationControl();
    }
    
    /**
     * Activity 시작 이벤트
     */    
    @Override
	public void onStart()
    {
    	super.onStart();
    	
    	mBoolAppFirstLoading = mSpfPrefer.getBoolean(Constants.APP_FIRST_LOADING, true);  	
    	if(mBoolAppFirstLoading == true)
    	{
    		//TODO:: 사용 설명서 삽입
    		showToastOnThread("First Loading!");
    		try
        	{
        		SharedPreferences.Editor edit = mSpfPrefer.edit();
    	    	edit.putBoolean(Constants.APP_FIRST_LOADING,  false);
    	    	edit.commit();
        	}
        	catch(Exception ex)
        	{
        		ex.printStackTrace();
        	}
    	}
    	else
    	{
			boolean stateOfGPS = mClsLocationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
    		if(stateOfGPS)
    		{
    			Intent itCameraCapture = new Intent(getContext(), CameraCapture.class);
    			startActivityForResult(itCameraCapture, 0);
    		}
    		else
    		{
    			AlertDialog.Builder alert = new AlertDialog.Builder(ParkingCam.this);
		    	alert.setTitle("GPS 경고");
				alert.setMessage("GPS가 꺼져있습니다. 활성화 시켜주세요.");
				alert.setPositiveButton("확인",  new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						finish();
					}
				});
				alert.show();
    		}
    	}
	}
	
	/**
	 * Activity 중단 이벤트
	 */    
	@Override
	public void onStop()
	{
		mClsLocationMgr.removeUpdates(mClsLocationListener);
		super.onStop();
	}
	
	/**
     * Activity 실행/재실행 이벤트
     */	
    @Override
	public void onResume()
    {
    	super.onResume();    	
    	setScreenOrientation();
    }
    
    /**
	 * Activity 소멸 이벤트
	 */
	@Override  
	public void onDestroy() 
	{		
		mClsLocationMgr = null;
		mClsLocationListener = null;
		super.onDestroy();
	}
    
	/**
	 * View관련 컨트롤을 초기화한다.
	 */ 
    private void initViewControl()
    {
    	final Button btnTake = (Button)findViewById(R.id.btnTake);
    	btnTake.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Intent itCameraCapture = new Intent(v.getContext(), CameraCapture.class);
				startActivityForResult(itCameraCapture, 0);
			}		
    	});
    }
    
    /**
     * 위치관련 컨트롤을 초기화한다.
     */
    private void initLocationControl()
    {
    	mClsLocationMgr = (LocationManager)getSystemService(Context.LOCATION_SERVICE);    	
    	mClsLocationListener = new LocationListener()
    	{
    		/**
    		 * 위치 변경 이벤트
    		 */
    		@Override
			public void onLocationChanged(Location location) 
			{
				AppContext.setLatitude(location.getLatitude());
				AppContext.setLongitude(location.getLongitude());
				mClsLocationMgr.removeUpdates(mClsLocationListener);
			}
    		
    		/**
    		 * 상태 변경 이벤트
    		 */
    		@Override
			public void onStatusChanged(String provider, int status, Bundle bundleExtras) 
    		{  
    			switch (status) 
    			{
                case LocationProvider.OUT_OF_SERVICE:
                    // Provider Out of Service
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    // Provider Temporarily Unavailable
                    break;
                case LocationProvider.AVAILABLE:
                    // Provider Available
                    break;
                }
    		}
    		
    		@Override
			public void onProviderEnabled(String strData) {   }
			
			@Override
			public void onProviderDisabled(String strData) {  }
    	};
    	
    	if(mClsLocationMgr != null)
    	{
    		String locProv = mClsLocationMgr.getBestProvider(getCriteria(), true);
            mClsLocationMgr.requestLocationUpdates(locProv, 1000, 1, mClsLocationListener);
    	}
    }
    
    public static Criteria getCriteria() 
    {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(true);		// 방향
        criteria.setSpeedRequired(true);		// 속도
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        return criteria;
    }
    
    /**
	 * 시스템 설정값이 변경되면 발생하는 이벤트
	 * @param conNewConfig 설정객체 
	 */	
	@Override
	public void onConfigurationChanged(Configuration conNewConfig)
	{
		super.onConfigurationChanged(conNewConfig);
	}
}