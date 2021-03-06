/**
	@file	ParkingCam.java
	@date	2015/08/10
	@author	JawHwanM
	@brief	ParkingCam Main
*/

package android.parkingcam;

import android.content.Context;
import android.content.Intent;
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
import android.parkingcam.manual.MainManual;

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
    		try
        	{
    	    	Intent itMainManual = new Intent(getContext(), MainManual.class);
    			startActivityForResult(itMainManual, 0);
        	}
        	catch(Exception ex)
        	{
        		ex.printStackTrace();
        	}
    	}
    	else
    	{
    		AppContext.setLatitude(0);
    		AppContext.setLongitude(0);
    		
    		boolean stateOfGPS = mClsLocationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
    		if(stateOfGPS)
    		{
    			getCurLocation();
    		}
    		
    		Intent itCameraCapture = new Intent(getContext(), CameraCapture.class);
			startActivityForResult(itCameraCapture, 0);
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
	
	@Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
    
	/**
	 * View관련 컨트롤을 초기화한다.
	 */ 
    private void initViewControl()
    {
    	//
    }
    
    /**
     * 위치관련 컨트롤을 초기화한다.
     */
    private void initLocationControl()
    {
    	AppContext.setLatitude(0);
    	AppContext.setLongitude(0);
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
    }
    
    /**
     * 현재 위치를 받아온다.
     */
    public void getCurLocation()
    {
    	if(mClsLocationMgr != null)
    	{
    		String locProv = mClsLocationMgr.getBestProvider(getCriteria(), true);
            mClsLocationMgr.requestLocationUpdates(locProv, 2000, 10, mClsLocationListener);
    	}
    }
    
    /**
     * 위치 데이터 환경 설정
     * @return
     */
    public static Criteria getCriteria() 
    {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);		// 정확도
        criteria.setAltitudeRequired(false);				// 고도, 높이 값을 얻어 올지를 결정
        criteria.setSpeedRequired(false);					// 속도
        criteria.setBearingRequired(false);					// 방향
        criteria.setCostAllowed(true);						// 위치 정보를 얻어 오는데 들어가는 금전적 비용
        criteria.setPowerRequirement(Criteria.POWER_LOW);	// 전원 소비량		
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