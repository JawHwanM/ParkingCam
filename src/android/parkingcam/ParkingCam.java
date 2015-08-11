/**
	@file	ParkingCam.java
	@date	2015/08/10
	@author	JawHwanM
	@brief	ParkingCam Main
*/

package android.parkingcam;

import android.content.Intent;
import android.os.Bundle;
import android.parkingcam.activity.BaseTemplate;
import android.parkingcam.camera.CameraCapture;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
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
	/**
	 * Activity 생성 이벤트
	 * @param savedInstanceState 상태정보
	 */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        super.initTemplate(this, R.layout.parking_cam);
        
        initViewControl();
    }
    
    /**
     * Activity 시작 이벤트
     */    
    @Override
	public void onStart()
    {
    	super.onStart();
    	
    	if(AppContext.getAppFirstLoading() == false)
    	{
    		showToastOnThread("First Loading!");
    		AppContext.setAppFirstLoading(true);
    	}
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
    	
    	setScreenOrientation();
    }

    /**
     * Activity 소멸 이벤트
     */        
	@Override
	public void onDestroy()
    {	
	   	super.releaseTemplate();
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
    	
    	final Button btnIcon2 = (Button)findViewById(R.id.btnIcon2);
    	btnIcon2.setOnDragListener(new OnDragListener()
    	{
			@Override
			public boolean onDrag(View arg0, DragEvent arg1) 
			{
				showToastOnThread("ICON_2 Drag!");
				return false;
			}
    		
    	});
    	
    	btnIcon2.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				showToastOnThread("ICON_2 Click!");
			}		
    	});
    	
    	final Button btnIcon3 = (Button)findViewById(R.id.btnIcon3);
    	btnIcon3.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				showToastOnThread("ICON_3 Click!");
			}		
    	});
    	
    	final Button btnIcon4 = (Button)findViewById(R.id.btnIcon4);
    	btnIcon4.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				showToastOnThread("ICON_4 Click!");
			}		
    	});
    	
    	final Button btnIcon5 = (Button)findViewById(R.id.btnIcon5);
    	btnIcon5.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				showToastOnThread("ICON_5 Click!");
			}		
    	});
    	
    	final Button btnIcon6 = (Button)findViewById(R.id.btnIcon6);
    	btnIcon6.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				showToastOnThread("ICON_6 Click!");
			}		
    	});
    	
    	final Button btnIcon7 = (Button)findViewById(R.id.btnIcon7);
    	btnIcon7.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				showToastOnThread("ICON_7 Click!");
			}		
    	});
    	
    	final Button btnIcon8 = (Button)findViewById(R.id.btnIcon8);
    	btnIcon8.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				showToastOnThread("ICON_8 Click!");
			}		
    	});
    }
}