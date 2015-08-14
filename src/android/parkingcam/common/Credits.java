/**
	@file	Credits.java
	@date	2015/08/10
	@author	JawHwanM
	@brief	Credits
*/
package android.parkingcam.common;

import android.os.Bundle;
import android.parkingcam.R;
import android.parkingcam.activity.BaseTemplate;

/**
 * Credits
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
public class Credits extends BaseTemplate
{
	/**
	 * Activity 생성 이벤트
	 * @param savedInstanceState 상태정보
	 */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        super.initTemplate(this, R.layout.credits);
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
}
