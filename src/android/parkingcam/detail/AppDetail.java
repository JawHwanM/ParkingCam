/**
	@file	AppDetail.java
	@date	2015/08/10
	@author	JawHwanM
	@brief	사용설명서
*/

package android.parkingcam.detail;

import android.os.Bundle;
import android.parkingcam.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * 사용설명서
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
public class AppDetail extends FragmentActivity
{
	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;
	
	@Override
	protected void onCreate(Bundle saveInstanceState)
	{
		super.onCreate(saveInstanceState);
		
		setContentView(R.layout.app_detail);
		mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
	}
	
	private class PagerAdapter extends FragmentStatePagerAdapter
	{
		public PagerAdapter(FragmentManager fm)
		{
			super(fm);
		}

		@Override
		public Fragment getItem(int position) 
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getCount() 
		{
			// TODO Auto-generated method stub
			return 0;
		}
	}
}
