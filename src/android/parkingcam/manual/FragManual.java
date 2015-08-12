/**
	@file	FarmManual.java
	@date	2015/08/10
	@author	JawHwanM
	@brief	사용설명서 세부
*/
package android.parkingcam.manual;

import android.os.Bundle;
import android.parkingcam.R;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
public class FragManual extends Fragment
{
	private int mPageNumber;
	
	public static FragManual create(int pageNumber) 
	{
		FragManual fragment = new FragManual();
		Bundle args = new Bundle();
		args.putInt("page", pageNumber);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		mPageNumber = getArguments().getInt("page");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.frag_manual, container, false);
		((TextView) rootView.findViewById(R.id.tvNumber)).setText(mPageNumber + "");
		return rootView;
	}
}