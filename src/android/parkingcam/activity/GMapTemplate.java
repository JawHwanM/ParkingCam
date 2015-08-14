/**
	@file	GMapTemplate.java
	@date	2015/08/10
	@author	JawHwanM
	@brief	구글맵 액티비티 템플릿
*/
package android.parkingcam.activity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.parkingcam.R;
import android.parkingcam.common.Constants;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 구글맵 액티비티를 구현하기 위한 템플릿클래스
 * @author JawHwanM
 * @version 1.0
 */
public class GMapTemplate extends FragmentActivity
{
	private long mLnBackKeyPressedTime = 0;
    private Toast mToast;
    
	protected static final float	MAP_LEVEL_DEFAULT_VIEW		= 8;		/**< 기본 레벨 */
	protected static final float	MAP_LEVEL_DETAIL_VIEW		= 17;		/**< 상세 레벨 */
	protected static final float	MAP_LEVEL_MAX_DETAIL_VIEW	= 19;		/**< 최대 레벨 */
	private Marker mClsLocMarker								= null;		/**< 마커 */
	protected LocationManager mLmLocationMgr;								/**< 로케이션관리자 */
	private GpsLocationListener mLsnrGpsListener				= null;		/**< GPS 리스너 */	

	protected static Context mStaticContext						= null;		/**< Static 컨텍스트 */
	protected Context mCtxContext								= null;		/**< 컨텍스트 */
	protected SharedPreferences mSpfPrefer						= null;		/**< 프레퍼런스 */
	protected GoogleMap mGmMap;												/**< 맵 */
	
	/**
	 * 템플릿 초기화
	 * @param ctxContext 컨텍스트
	 * @param intRayoutId 레이아웃ID
	 */	
	public void initTemplate(Context ctxContext, int intRayoutId)
	{
		mStaticContext = ctxContext;
		mCtxContext = ctxContext;	
		mLsnrGpsListener = null;
        setContentView(intRayoutId);
        mLmLocationMgr = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	
        try
        {
	        this.mGmMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.fmMap)).getMap();
	        
	        LatLng objDefaultCenter = getDefaultLocation();
	        this.setZoomCenter(MAP_LEVEL_DEFAULT_VIEW, objDefaultCenter, false);
	        
	        this.mClsLocMarker = this.mGmMap.addMarker(new MarkerOptions().position(objDefaultCenter));
	        this.mClsLocMarker.setVisible(false);
	        this.mGmMap.setMyLocationEnabled(true);	// 내 위치 보기는 안보이도록 한다.
	        this.mGmMap.getUiSettings().setCompassEnabled(true);
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}	        
		mSpfPrefer = PreferenceManager.getDefaultSharedPreferences(getContext());
		setScreenOrientation();
	}
	
	/**
	 * 템플릿 제거
	 */		
	public void releaseTemplate()
	{
		mSpfPrefer		= null;	
		
		try
		{
			if(mClsLocMarker != null)
			{
				mClsLocMarker.remove();
				mClsLocMarker = null;
			}
			
			this.mGmMap.clear();
			this.mGmMap	= null;
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}			
		mCtxContext	= null;
	}	
	
	/**
	 * 지도 확대레벨을 얻는다.
	 * @return 지도 확대 레벨
	 */
	protected float getZoom()
	{
		return this.mGmMap.getCameraPosition().zoom;
	}
	
    /**
     * 현재위치 활성화 여부를 설정한다.
     * @param boolEnabled 활성화 여부
     */
	protected void setMyLocationEnabled(boolean boolEnabled)
	{
		this.mGmMap.setMyLocationEnabled(boolEnabled);
	}

	/**
	 * 현재위치 버튼 활성화 여부를 설정한다.
	 * @param boolEnabled 활성화 여부
	 */
	protected void setMyLocationButtonEnabled(boolean boolEnabled)
	{
		this.mGmMap.getUiSettings().setMyLocationButtonEnabled(boolEnabled);
	}
	
	/**
	 * 디폴트 위치를 리턴한다.
	 * @return 디폴트 위치
	 */
    protected LatLng getDefaultLocation()
    {
    	return new LatLng(Constants.MAP_DEFAULT_LAT, Constants.MAP_DEFAULT_LNG); // 사당역
    }
    
    
	/**
	 * Map 객체를 리턴한다.
	 * @return Map객체
	 */
    protected GoogleMap getMap()
    {
    	return this.mGmMap;
    }		

    /**
     * 지도 종류를 설정한다.
     * @param boolSatellite 지도 종류
     */
    protected void setSatellite(boolean boolSatellite)
    {
    	try
    	{
			if(boolSatellite)
				this.mGmMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			else
				this.mGmMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}				
    }    
    
	/**
	 * 컨텍스트를 얻는다.
	 */		
	public Context getContext()
	{
		return mCtxContext;
	}	
	
	/**
	 * 프레퍼런스를 얻는다.
	 */	
	public SharedPreferences getPreferences()
	{
		return mSpfPrefer;
	}	

	/**
	 * 화면 가로/세로보기 설정
	 */	
	public void setScreenOrientation()
	{
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}	
	/**
	 * 지도상에서 현재 위치로 이동한다.
	 */	
	public void goCurrentLocation()
	{
		mLsnrGpsListener = new GpsLocationListener();
		goCurrentLocation(mLsnrGpsListener);
	}

	/**
	 * 지도상에서 현재 위치로 이동한다.
	 * @param lsnrGpsListener GPS 리스너
	 */	
	public void goCurrentLocation(LocationListener lsnrGpsListener)
	{
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);		// 정확도
		criteria.setAltitudeRequired(false);				// 고도, 높이 값을 얻어 올지를 결정
		criteria.setSpeedRequired(false);					// 속도
		criteria.setBearingRequired(false);					// 방향
		criteria.setCostAllowed(true);						// 위치 정보를 얻어 오는데 들어가는 금전적 비용
		criteria.setPowerRequirement(Criteria.POWER_LOW);	// 전원 소비량
		
		String strGpsProvider = mLmLocationMgr.getBestProvider(criteria, true);
		if(strGpsProvider == null)
		{
			showToastText("GPS 장치가 없거나  설정이 꺼져있습니다.");
			return;
		}
		mLmLocationMgr.requestLocationUpdates(strGpsProvider, 2000, 10, lsnrGpsListener);
		Location locLastLocation = mLmLocationMgr.getLastKnownLocation(strGpsProvider);
		drawMapCurrentLocation(locLastLocation);
	}
	
	/**
	 * 현재위치로 이동하여 지도상에 Draw한다.
	 * @param locLastLocation 위치객체
	 */	
	public void drawMapCurrentLocation(Location locLastLocation)
	{
		double dblLon = 0;
		double dblLat = 0;

		if(locLastLocation != null)
		{
			dblLon = locLastLocation.getLongitude();
			dblLat = locLastLocation.getLatitude();
			if(dblLon > 0 && dblLat > 0)
			{
				// 주기적으로 보고 할것이 아니므로 Gps 리스너 해제
				if(mLsnrGpsListener != null) mLmLocationMgr.removeUpdates(mLsnrGpsListener);
				Geocoder gcCoder = null;
				StringBuilder sbAddr = null;
				try
				{
					gcCoder = new Geocoder(this, Locale.getDefault());
					sbAddr = new StringBuilder();
					List<Address> lstAddresses = gcCoder.getFromLocation(dblLat, dblLon, 1);
					if(lstAddresses.size() > 0)
					{
						Address adrGeoAddress = lstAddresses.get(0);
						for(int intCnt = 0; intCnt <= adrGeoAddress.getMaxAddressLineIndex(); intCnt++)
						{
							sbAddr.append(adrGeoAddress.getAddressLine(intCnt));
						}
					}

					LatLng objCenter = new LatLng(dblLat, dblLon);
		            mClsLocMarker.setPosition(objCenter);
		            mClsLocMarker.setTitle(sbAddr.toString());
		            mClsLocMarker.setVisible(true);
		            mClsLocMarker.showInfoWindow();
		            this.setZoomCenter(MAP_LEVEL_DETAIL_VIEW, objCenter, true);
				}
				catch(IOException e)
				{
					showToastText(e.getMessage());
				}
				finally
				{
					gcCoder = null;
					sbAddr = null;
				}
				
			}
		}
	}
		
	/**
     * Toast메시지를 보여준다.
     * @param strMessage 메시지
     */    
    protected void showToastText(String strMessage)
    {
    	if(strMessage != null)
    	{
    		Toast tosMessage = Toast.makeText(this, strMessage, Toast.LENGTH_SHORT);
    		tosMessage.show();
    	}
    }

	/**
	 * 옵션메뉴 생성이벤트
	 * @param menu 메뉴 
	 */    
    @Override
	public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.parking_cam, menu);
        return true;
	}

    /**
     * 옵션메뉴 선택이벤트
	 * @param item 메뉴아이템
     */     
    @Override
	public boolean onOptionsItemSelected(MenuItem item)
    {
    	//int intItemId = item.getItemId();
        
        return super.onOptionsItemSelected(item);
    }

    /**
     * 중심좌표로 이동한다.
     * @param fltZoomLevel 확대레벨
     * @param fltBearing 방향
     * @param dblLng X좌표
     * @param dblLat Y좌표
     */
    protected void setCenter(float fltZoomLevel, float fltBearing, double dblLat, double dblLng)
    {
    	try
    	{
	    	if(dblLat > 0 && dblLng > 0)
	    	{
		     	CameraPosition cameraPosition = new CameraPosition.Builder().
		                target(new LatLng(dblLat, dblLng)).
		                //tilt(degrees).
		                zoom(fltZoomLevel).
		                bearing(fltBearing).
		                build();
		    	mGmMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	    	}
       	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}	    	
    }
	    
    /**
     * 중심좌표로 이동한다.
     * @param fltZoomLevel 확대레벨
     * @param fltBearing 방향
     * @param dblLng X좌표
     * @param dblLat Y좌표
     * @param boolAnimate 애니메이션여부
     */
    protected void setCenter(float fltZoomLevel, float fltBearing, double dblLat, double dblLng, boolean boolAnimate)
    {
    	try
    	{
	    	if(dblLat > 0 && dblLng > 0)
	    	{
		     	CameraPosition cameraPosition = new CameraPosition.Builder().
		                target(new LatLng(dblLat, dblLng)).
		                //tilt(degrees).
		                zoom(fltZoomLevel).
		                bearing(fltBearing).
		                build();
		     	if(boolAnimate)
		     	{
		     		mGmMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		     	}
		     	else
		     	{
		     		mGmMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		     	}
	    	}
       	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}    	
    }    
	    
    /**
     * 중심좌표로 이동한다.
     * @param fltBearing 방향
     * @param dblLng X좌표
     * @param dblLat Y좌표
     */
    protected void setCenter(float fltBearing, double dblLat, double dblLng)
    {
    	try
    	{
	    	if(dblLat > 0 && dblLng > 0)
	    	{
		    	float fltZoom = this.getZoom();
		     	CameraPosition cameraPosition = new CameraPosition.Builder().
		                target(new LatLng(dblLat, dblLng)).
		                zoom(fltZoom).
		                bearing(fltBearing).
		                build();
		    	mGmMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	    	}
       	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}	    	
    }
    
    /**
     * 중심좌표로 이동한다.
     * @param dblLng X좌표
     * @param dblLat Y좌표
     */      
    protected void setCenter(double dblLat, double dblLng)
    {
    	try
    	{
    		if(dblLat > 0 && dblLng > 0) mGmMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(dblLat, dblLng)));
       	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}    	
    }

    /**
     * 중심좌표로 이동한다.
     * @param objCenter 중심좌표
     * @param boolAnimate 애니메이션 이동 여부
     */
    protected void setCenter(LatLng objCenter, boolean boolAnimate)
    {
    	try
    	{
	     	if(boolAnimate)
	     	{
	     		mGmMap.animateCamera(CameraUpdateFactory.newLatLng(objCenter));
	     	}
	     	else
	     	{
	     		mGmMap.moveCamera(CameraUpdateFactory.newLatLng(objCenter));
	     	}
       	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}	     	
    }    
    
    /**
     * 중심좌표를 얻는다.
     * @return
     */
    protected LatLng getCenter()
    {
    	return mGmMap.getCameraPosition().target;
    }
    
    /**
     * 중심좌표로 이동한다.
     * @param fltZoomLevel 확대레벨
     * @param dblLng X좌표
     * @param dblLat Y좌표
     */    
    protected void setZoomCenter(float fltZoomLevel, double dblLat, double dblLng)
    {
    	try
    	{
    		if(dblLat > 0 && dblLng > 0) mGmMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dblLat, dblLng), fltZoomLevel));
       	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}    		
    }
	    
    /**
     * 중심좌표로 이동한다.
     * @param fltZoomLevel 확대레벨
     * @param objCenter 중심좌표
     * @param boolAnimate 애니메이션이동여부
     */
    protected void setZoomCenter(float fltZoomLevel, LatLng objCenter, boolean boolAnimate)
    {
    	try
    	{
	     	if(boolAnimate)
	     	{
	     		mGmMap.animateCamera(CameraUpdateFactory.newLatLngZoom(objCenter, fltZoomLevel));
	
	     	}
	     	else
	     	{
	     		mGmMap.moveCamera(CameraUpdateFactory.newLatLngZoom(objCenter, fltZoomLevel));
	     	}
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    }
	    
    /**
     * 중심좌표로 이동한다.
     * @param dblLng X좌표 
     * @param dblLat Y좌표
     */
    protected void setDetailZoomCenter(double dblLat, double dblLng)
    {
    	if(dblLat > 0 && dblLng > 0) this.setZoomCenter(MAP_LEVEL_DETAIL_VIEW, new LatLng(dblLat, dblLng), true);
    }
        
    /**
     * Toast 메시지를 보여준다.
     * @param strMessage 메시지
     */
    protected void showToastOnThread(final String strMessage)
    {
    	runOnUiThread(new Runnable()
    	{     
    		@Override
			public void run()
    		{
    			if(mCtxContext != null && strMessage != null) Toast.makeText(mCtxContext, strMessage, Toast.LENGTH_SHORT).show();
    		} 
    	});
    }    

    /**
     * Dialog 메시지를 보여준다.
     * @param strTitle 다이얼로그 제목 
     * @param strMessage 메시지
     */     
    protected void showDialogOnThread(final String strTitle, final String strMessage)
    {
    	showDialogOnThread(strTitle, strMessage, -1);
    }    
	    
    /**
     * Dialog 메시지를 보여준다.
     * @param strTitle 다이얼로그 제목 
     * @param strMessage 메시지
     * @param intTimeout 타임아웃(밀리세컨)
     */       
    protected void showDialogOnThread(final String strTitle, final String strMessage, final int intTimeout)
    {
    	runOnUiThread(new Runnable()
    	{     
    		@Override
			public void run()
    		{
    			LayoutInflater layoutInflater = (LayoutInflater) mCtxContext.getSystemService(LAYOUT_INFLATER_SERVICE);
    			View viDialog = layoutInflater.inflate(R.layout.html_alert_dialog, (ViewGroup) findViewById(R.id.content_scroll_view));		    		
    			TextView contentTextView = (TextView) viDialog.findViewById(R.id.content_text_view);
    			contentTextView.setText(strMessage);
    			contentTextView.setMovementMethod(LinkMovementMethod.getInstance());
    			AlertDialog.Builder adBuilder = new AlertDialog.Builder(mCtxContext);
    			adBuilder.setView(viDialog);
				adBuilder.setTitle(strTitle);
				if(intTimeout > -1)
				{
					adBuilder.setCancelable(true);
				}
				else
				{
					adBuilder.setPositiveButton("확인",  null);
				}
				final AlertDialog adDialog = adBuilder.create();
				adDialog.show();   
				if(intTimeout > -1)
				{
					final Timer tmTimer = new Timer();
			        tmTimer.schedule(new TimerTask()
			        {
			            @Override
						public void run()
			            {
			            	adDialog.dismiss(); 
			                tmTimer.cancel();
			            }
			        }, intTimeout);
				}
    		} 
    	});    	
    }
	
	/**
	 * GPS 위치 리스너 클래스
	 * 
	 * @author 이용민
	 * @version 1.0
	 */	
    public class GpsLocationListener implements LocationListener
    {
    	/**
    	 * 위치변경 이벤트
    	 * @param location 위치객체 
    	 */    	
    	@Override
		public void onLocationChanged(Location location)
    	{
    		drawMapCurrentLocation(location);
    	}
    	
    	/**
    	 * 위치제공자 상태변경시 발생하는 이벤트
    	 * @param provider 위치제공자 
    	 * @param status 상태
    	 * @param extras 현상태
    	 */     	
    	@Override
		public void onStatusChanged(String provider, int status, Bundle extras) { }

    	/**
    	 * 위치제공자 사용불가시 발생하는 이벤트
    	 * @param provider 위치제공자 
    	 */     	
    	@Override
		public void onProviderDisabled(String provider) { }
    	
    	/**
    	 * 위치제공자 사용가능시 발생하는 이벤트
    	 * @param provider 위치제공자 
    	 */       	
    	@Override
		public void onProviderEnabled(String provider) { }
    }
    
    @Override
    public void onBackPressed()
    {
        if (System.currentTimeMillis() > mLnBackKeyPressedTime + 2000)
        {
        	mLnBackKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= mLnBackKeyPressedTime + 2000)
        {
        	mToast.cancel();
        	moveTaskToBack(true);
        	finish();
        	android.os.Process.killProcess(android.os.Process.myPid());            
        }
    }

    public void showGuide()
    {
    	mToast = Toast.makeText(getBaseContext(), "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
    	mToast.show();
    }
}