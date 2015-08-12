/**
	@file	MainManual.java
	@date	2015/08/10
	@author	JawHwanM
	@brief	사용설명서 메인
*/

package android.parkingcam.manual;

import android.os.Bundle;
import android.parkingcam.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * 사용설명서 메인
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
public class MainManual extends FragmentActivity
{
	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;
	
	@Override
	protected void onCreate(Bundle saveInstanceState)
	{
		super.onCreate(saveInstanceState);
		setContentView(R.layout.main_manual);
		mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
		
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
    	super.onDestroy();
    }
    
    /**
     * View관련 컨트롤 초기화
     */
    private void initViewControl()
    {
    	mViewPager = (ViewPager) findViewById(R.id.vPager);
    	mViewPager.setAdapter(mPagerAdapter);
    }
    
    
	/**
	 * ViewPager 어댑터
	 * @author JaeHwanM
	 *
	 */
	private class PagerAdapter extends FragmentStatePagerAdapter
	{
		/**
		 * 생성자
		 * @param fm
		 */
		public PagerAdapter(FragmentManager fragmentMgr)
		{
			super(fragmentMgr);
		}

		/**
		 * Fragment 페이지 생성
		 */
		@Override
		public Fragment getItem(int position) 
		{
			// TODO Auto-generated method stub
			return FragManual.create(position);
		}

		/**
		 * 페이지 총 개수
		 */
		@Override
		public int getCount() 
		{
			// TODO Auto-generated method stub
			return 5;
		}
	}
}
