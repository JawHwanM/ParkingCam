/**
	@file	ParkingMap.java
	@date	2015/08/10
	@author	JawHwanM
	@brief	Parking Map
*/
package android.parkingcam.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.parkingcam.AppContext;
import android.parkingcam.R;
import android.parkingcam.activity.GMapTemplate;
import android.parkingcam.common.Constants;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

/**
 * Parking Map
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
public class ParkingMap extends GMapTemplate
{
	private SharedPreferences 	mSpfPreferences = null;	/**< 프레퍼런스 */
	private boolean				mBoolSatellite;			/**< 위성지도, 일반지도 여부  */
	
	private Marker	mClsMarker;							/**< 현재위치마커 */
	
	private String mStrPhotoDate = "";
	private String mStrPhotoMemo = "";
	private double mDblLatitude = 0;
	private double mDblLongitude = 0;
	
	/**
     * Activity 생성 이벤트
     * @param savedInstanceState 상태정보
     */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		super.onCreate(savedInstanceState);
		super.initTemplate(this, R.layout.parking_map);
   		
		mSpfPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		
        initMapControl();
        initViewControl();
	}
	
	/**
     * Activity 시작 이벤트
     */    
    @Override
	public void onStart()
    {
    	super.onStart();
		super.setSatellite(mBoolSatellite);
    }
   
    /**
     * Activity 중단 이벤트
     */    
    @Override
	public void onStop()
    {
    	super.onStop();
    }
    
    /**
     * Activity 실행/재실행 이벤트
     */    
    @Override
	public void onResume()
    {
    	super.onResume();
       	// 가로보기
	    setScreenOrientation();
	    
	    DisplayMetrics displayMetrics = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);	    
    	WindowManager.LayoutParams lpParams = getWindow().getAttributes();
    	lpParams.width =  displayMetrics.widthPixels - 40;
    	lpParams.height = displayMetrics.heightPixels- 100;
    	this.getWindow().setAttributes(lpParams);
    }  
    
    /**
     * Activity 멈춤 이벤트
     */ 
    @Override
	public void onPause() 
    {
        super.onPause();
    }
    
    /**
     * Activity 소멸 이벤트
     */        
    @Override
	public void onDestroy()
    {  	
    	mClsMarker = null;
    	
    	super.releaseTemplate();
    	super.onDestroy();
    }
    
    /**
     * 지도 컨트롤을 초기화한다.
     */    
    private void initMapControl()
    {
    	DisplayMetrics displayMetrics = new DisplayMetrics();
    	getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    	int deviceDisplay = displayMetrics.widthPixels * displayMetrics.heightPixels;
    	int intTextSize = 0;
    	
    	if(deviceDisplay > Constants.DEVICE_DISPLAY_XLARGE) intTextSize = 28;
    	else if(deviceDisplay > Constants.DEVICE_DISPLAY_LARGE) intTextSize = 18;
    	else if(deviceDisplay > Constants.DEVICE_DISPLAY_MEDIUM) intTextSize = 16;
    	else intTextSize = 14;
    	
    	String strFontColor = "#9F2700";
        String strBackColor = "#A7DF00"; 
        
    	Bitmap bmIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.icn_leaf);
        /*mClsMarker = new Marker({	position: new LatLng(AppContext.getLatitude(), AppContext.getLongitude()), 
					    	   		map: super.getMap(),
					    	   		title: '마커' });*/
    }
    
    /**
     * View 컨트롤을 초기화한다.
     */    
    private void initViewControl()
    {
    	DisplayMetrics displayMetrics = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);	    
    	WindowManager.LayoutParams lpParams = getWindow().getAttributes();
    	lpParams.width =  displayMetrics.widthPixels - 40;
    	lpParams.height = displayMetrics.heightPixels- 100;
    	this.getWindow().setAttributes(lpParams);
    }
    
    /**
	 * 데이터를 조회한다.
	 */
    private void doDrawLandMap()
	{
    	runOnUiThread(new Runnable()
        { 
        	@Override
			public void run() 
        	{
        		getParkingMap(); 
        	}
        });
	}
    
    /**
     * 농지마커 정보를 출력한다.
     */
	public void getParkingMap()
	{	
		try
		{
			Intent itCurIntent = getIntent();
	    	Bundle bundle 	= itCurIntent.getExtras();
	   		if(bundle != null && "".equals(bundle) == false)
	   		{
	   			mStrPhotoDate = itCurIntent.getExtras().getString("photoDate");
	   			mStrPhotoMemo = itCurIntent.getExtras().getString("photoMemo");
	   			mDblLongitude = itCurIntent.getExtras().getDouble("cx");
	   			mDblLatitude  = itCurIntent.getExtras().getDouble("cy");
	   			
	   			if(mDblLongitude > 0.0 && mDblLatitude > 0.0)
	   			{
	   				//
	   			}
	   			super.setZoomCenter(MAP_LEVEL_DETAIL_VIEW, mDblLongitude, mDblLatitude);
	   		}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}