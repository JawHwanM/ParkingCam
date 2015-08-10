/**
	@file	ParkingCam.java
	@date	2015/08/10
	@author	JawHwanM
	@brief	ParkingCam Main
*/

package android.parkingcam;

import android.os.Bundle;
import android.parkingcam.activity.BaseTemplate;
import android.view.View;
import android.view.View.OnClickListener;
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
    	final Button btnIcon1 = (Button)findViewById(R.id.btnIcon1);
    	btnIcon1.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				showToastOnThread("ICON1 Click!");
			}		
    	});
    }
}