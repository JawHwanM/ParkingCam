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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.parkingcam.R;
import android.parkingcam.activity.GMapTemplate;
import android.parkingcam.data.ParkDBResource;
import android.parkingcam.data.PhotoInfo;
import android.view.ContextThemeWrapper;

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
		mDblLatitude 	= 0.0;
    	mDblLongitude 	= 0.0;
		
		Intent itCurIntent = getIntent();
    	Bundle bundle 	= itCurIntent.getExtras();    	
   		if(bundle != null && "".equals(bundle) == false)
   		{
   			mStrPhotoName = itCurIntent.getExtras().getString("photoName");
   			selectLocation(mStrPhotoName);
   			
   			if(mDblLatitude <= 0.0 || mDblLongitude <= 0.0)
   			{
   				AlertDialog.Builder adBuilder = new AlertDialog.Builder(getDialogContext());
   	   			adBuilder.setIcon(R.drawable.icn_camera);
   	   			adBuilder.setTitle("PARKING MAP");
   	            adBuilder.setMessage("위치 데이터가 없습니다.");
   	            adBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() 
   	            {
   	                public void onClick(DialogInterface dialog, int which) 
   	                {
   	                    dialog.dismiss();
   	                    doFinish();
   	                }
   	            });
   	            adBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() 
   	            {	
   					@Override
   					public void onCancel(DialogInterface dialog) 
   					{
   						dialog.dismiss();
   	                    doFinish();
   					}
   				});
   	            adBuilder.show();
   			}
   			else
   			{
   				doDrawParkingMap();
   			}
   	
   		}
	}
    
	/**
	 * 카테고리 코드리스트를 받아온다.
	 */
	public void selectLocation(String strPhotoName)
	{
		ParkDBResource clsResource = new ParkDBResource(this);
		List<PhotoInfo> lstPhoto = clsResource.getPhotoInfo(strPhotoName);
		
		try
		{
			if(lstPhoto != null && lstPhoto.size() > 0)
			{
				mStrPhotoMemo = lstPhoto.get(0).getPhotoMemo();
				mDblLatitude = Double.valueOf(lstPhoto.get(0).getCoordY()).doubleValue();
				mDblLongitude = Double.valueOf(lstPhoto.get(0).getCoordX()).doubleValue();
			}
		}
		catch (Exception e)
		{
			showToastOnThread("데이터 검색중 에러가 발생하였습니다.");
			e.printStackTrace();
		}
		finally
		{
			if(clsResource != null)	{ clsResource = null; }
			if(lstPhoto != null) { lstPhoto = null; }
		}
	}
	
	/**
     * Dialog 버전별 테마
     * @return
     */
    private Context getDialogContext() 
    {
        final Context context;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) 
            context = new ContextThemeWrapper(this, android.R.style.Theme_Holo);
        else 
            context = new ContextThemeWrapper(this, android.R.style.Theme_Dialog);

        return context;
    }
    
    /**
     * Dialog 종료
     */
    private void doFinish()
    {
    	moveTaskToBack(true);
    	finish();
    	android.os.Process.killProcess(android.os.Process.myPid());
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