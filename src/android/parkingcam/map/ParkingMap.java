/**
	@file	ParkingMap.java
	@date	2015/08/10
	@author	JawHwanM
	@brief	Parking Map
*/
package android.parkingcam.map;

import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.parkingcam.R;
import android.parkingcam.activity.GMapTemplate;
import android.parkingcam.common.Constants;

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
	protected float	mFlMapLevel	= 0;	/**< 기본 레벨 */
	private boolean	mBoolSatellite;		/**< 위성지도, 일반지도 여부  */
	private Marker	mClsMarker;			/**< 현재위치마커 */
	
	private String mStrPhotoName = "";	/**< 사진명	*/
	private String mStrPhotoMemo = "";	/**< 사진메모	*/
	private double mDblLatitude = 0;	/**< Lat좌표	*/
	private double mDblLongitude = 0;	/**< Lng좌표	*/
	
	/**
     * Activity 생성 이벤트
     * @param savedInstanceState 상태정보
     */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		super.initTemplate(this, R.layout.parking_map);
		
        initViewControl();
        initDataControl();
	}

	/**
     * Activity 시작 이벤트
     */    
    @Override
	public void onStart()
    {
    	super.onStart();
		super.setSatellite(mBoolSatellite);
		doDrawParkingMap();
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
    	mStrPhotoName = null;
    	mStrPhotoMemo = null;
    	
    	if(mClsMarker != null)
    	{
    		mClsMarker.remove();
        	mClsMarker = null;
    	}
    	
    	super.releaseTemplate();
    	super.onDestroy();
    }
    
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
    
    /**
     * View 컨트롤을 초기화한다.
     */    
    private void initViewControl()
    {
    	//	
    }
    
    /**
	 * Data관련 컨트롤 초기화한다.
	 */
	private void initDataControl() 
	{
		mFlMapLevel 	= MAP_LEVEL_DEFAULT_VIEW;
		mStrPhotoName	= "";
		mStrPhotoMemo	= "";
		mDblLatitude 	= Constants.MAP_DEFAULT_LAT;
    	mDblLongitude 	= Constants.MAP_DEFAULT_LNG;
		
		Intent itCurIntent = getIntent();
    	Bundle bundle 	= itCurIntent.getExtras();    	
   		if(bundle != null && "".equals(bundle) == false)
   		{
   			mStrPhotoName = itCurIntent.getExtras().getString("photoName");
   			//TODO::Select 구문			
   		}
	}
    
    /**
	 * 데이터를 조회한다.
	 */
    private void doDrawParkingMap()
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
			mClsMarker = mGmMap.addMarker(new MarkerOptions()
				     .position(new LatLng(mDblLatitude, mDblLongitude))
				     .title(getLocationName())
				     .snippet(mStrPhotoMemo)
				     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)) //BitmapDescriptorFactory.fromResource(R.drawable.icn_leaf)
				     .draggable(false));
			super.setZoomCenter(MAP_LEVEL_DETAIL_VIEW, mDblLatitude, mDblLongitude);
						
			System.out.println("mStrPhotoName="+mStrPhotoName);
   			System.out.println("mStrPhotoMemo="+mStrPhotoMemo);
   			System.out.println("mDblLatitude="+mDblLatitude);
   			System.out.println("mDblLongitude="+mDblLongitude); 		
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/**
	 * 좌표를 주소로 변환
	 * @return
	 */
	private String getLocationName()
	{
		List<Address> lstAddr;
		StringBuffer strbfAddr = new StringBuffer();
		try
		{
			Geocoder geocoder = new Geocoder(this, Locale.getDefault());
			lstAddr = geocoder.getFromLocation(mDblLatitude, mDblLongitude, 1);
			if(lstAddr != null && lstAddr.size() > 0)
			{
				String strAddr = lstAddr.get(0).getAddressLine(0).toString();				
				strAddr = strAddr.replace(lstAddr.get(0).getCountryName(),"");
				strbfAddr.append(strAddr.trim());
			}
		}
		catch(Exception ex)
		{
			showToastOnThread("주소 취득 실패");
			ex.printStackTrace();
		}
    	return strbfAddr.toString();
	}
}