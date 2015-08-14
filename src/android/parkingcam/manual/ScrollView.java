/**
	@file	ScrollView.java
	@date	2015/08/10
	@author	JawHwanM
	@brief	제스쳐로 화면 전환
*/
package android.parkingcam.manual;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.parkingcam.R;
import android.parkingcam.common.Constants;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.Toast;

/**
 * 제스쳐로 화면 전환
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
public class ScrollView extends ViewGroup
{
	//private boolean mBoolFirstMove = true;
	
	private VelocityTracker mVelocityTracker = null;	/**< 드래그 속도와 방향 판단	*/
	
	private int mTouchSlop = 10; 		/**< px단위 화면전환 터치 인식	*/
	
	private Bitmap mWallpaper = null;	/**< 배경화면	*/
	private Paint mPaint = null;
	
	private Scroller mScroller = null;	/**< 자동스크롤	*/
	private PointF mLastPoint = null;	/**< 마지막 터치지점	*/
	private int mCurPage = 0;			/**< 현재페이지	*/
	private int mCurTouchState;			/**< 현재 터치 상태	*/
	
	private Toast mToast;
	
	/**
	 * 생성자
	 * @param context
	 */
	public ScrollView(Context context)
	{
		super(context);
		initScrollView();
	}
	
	public ScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initScrollView();
	}
	
	public ScrollView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initScrollView();
	}
	
	/**
	 * ScrollView 초기화한다.
	 */
	private void initScrollView()
	{
		mWallpaper = BitmapFactory.decodeResource(getResources(), R.drawable.icn_gps);
		mPaint = new Paint();
		mScroller = new Scroller(getContext());
		mLastPoint = new PointF();
	}
	
	/**
	 * ChildView 크기 지정
	 */
	@Override
	protected void onMeasure(int intWidth, int intHeight)
	{
		super.onMeasure(intWidth, intHeight);
		for(int intIndex = 0; intIndex < getChildCount(); intIndex++)
		{
			getChildAt(intIndex).measure(intWidth, intHeight);
		}
	}
	
	/**
	 * ChildView 위치 지정
	 */
	@Override
	protected void onLayout(boolean changed, int intLeft, int intTop, int intRight, int intBottom)
	{
		for(int intIndex = 0; intIndex < getChildCount(); intIndex++)
		{
			int intChildLeft = getChildAt(intIndex).getMeasuredWidth() * intIndex;
			getChildAt(intIndex).layout(intChildLeft,  
										intTop, 
										intChildLeft + getChildAt(intIndex).getMeasuredWidth(), 
										getChildAt(intIndex).getMeasuredHeight());
		}
	}

	/**
	 * onDraw() 메서드와 동일
	 */
	@Override
	protected void dispatchDraw(Canvas canvas)
	{
		canvas.drawBitmap(mWallpaper, 0, 0, mPaint);
		for(int intIndex = 0; intIndex < getChildCount(); intIndex++)
		{
			drawChild(canvas, getChildAt(intIndex), 100);
		}
	}
	
	/**
	 * 터치 이벤트
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(mVelocityTracker == null) mVelocityTracker = VelocityTracker.obtain();
		mVelocityTracker.addMovement(event);
		
		switch(event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			if(!mScroller.isFinished())
			{
				mScroller.abortAnimation(); // 자동스크롤 중지
			}
			mLastPoint.set(event.getX(), event.getY());
			break;
			
		case MotionEvent.ACTION_MOVE:
			// 이전 지점과 현재 지점의 차이만큼 스크롤 이동
			int intX = (int)(event.getX() - mLastPoint.x);
			scrollBy(-intX, 0);
			/*
			if(mBoolFirstMove && getChildCount() > 2)
			{
				if(intX < 0 && mCurPage == getChildCount() - 1)
				{
					overScroll();
				}
				else if(intX > 0 && mCurPage == 0)
				{
					underScroll();
				}
			}
			mBoolFirstMove = false;
			*/
			invalidate();
			mLastPoint.set(event.getX(), event.getY());
			break;
			
		case MotionEvent.ACTION_UP:
			// 스크롤 속도측정 
			mVelocityTracker.computeCurrentVelocity(1000); // 속도단위 1초지정
			int intVelocity = (int)mVelocityTracker.getXVelocity(); // X축 이동 속도
			int intGap = getScrollX() - mCurPage * getWidth();
			int intNextPage = mCurPage;
			
			if((intVelocity > Constants.SNAP_VELOCITY || intGap < -getWidth()/2) && mCurPage > 0)
			{
				intNextPage--;
			}
			else if((intVelocity < -(Constants.SNAP_VELOCITY) || intGap > getWidth()/2) && mCurPage < getChildCount()-1)
			{
				intNextPage++;
			}
			
			int intMove = 0;
			if(mCurPage != intNextPage)
			{
				intMove = getChildAt(0).getWidth() * intNextPage - getScrollX();
			}
			else
			{
				intMove = getWidth() * mCurPage - getScrollX();
			}
			
			mScroller.startScroll(getScrollX(), 0, intMove, 0, Math.abs(intMove));
			
			if(mToast != null)
			{
				mToast.setText("Page : "+intNextPage);
			}
			else
			{
				mToast = Toast.makeText(getContext(), "Page : "+intNextPage, Toast.LENGTH_SHORT);
			}
			mToast.show();
			invalidate();
			mCurPage = intNextPage;
			
			mCurTouchState = Constants.TOUCH_STATE_NORMAL;
			mVelocityTracker.recycle();
			mVelocityTracker = null;
			//mBoolFirstMove = true;
			break;
		}		
		return true;
	}
	
	/**
	 * 스크롤 될때마다 실행
	 */
	@Override
	public void computeScroll()
	{
		super.computeScroll();
		// onTouchEvent에서 지정된 mScroller의 목표 스크롤 지점으로 스크롤하는데 필요한 중간 좌표값을 얻을 수 있으면 return true
		if(mScroller.computeScrollOffset())
		{
			// 값을 얻게되면 이동하면서 animation 효과를 가져옴
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			invalidate();
		}
	}
	
	/**
	 * onTouchEvent를 ChildView에게 주거나 뺐는 이벤트
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event)
	{
		int intX = (int)event.getX();
		int intY = (int)event.getY();
		
		int intAction = event.getAction();
		switch(intAction)
		{
		case MotionEvent.ACTION_DOWN:
			mCurTouchState = mScroller.isFinished() ? Constants.TOUCH_STATE_NORMAL : Constants.TOUCH_STATE_SCROLLING;
			mLastPoint.set(intX, intY);
			break;
		
		case MotionEvent.ACTION_MOVE:
			int intMoveX = Math.abs(intX - (int)mLastPoint.x);
			if(intMoveX > mTouchSlop)
			{
				mCurTouchState = Constants.TOUCH_STATE_SCROLLING;
				mLastPoint.set(intX, intY);
			}
			break;
		}
		
		return mCurTouchState == Constants.TOUCH_STATE_SCROLLING;
	}
	
	/*
	private void underScroll()
	{
		View view = getChildAt(getChildCount() - 1);
		removeViewAt(getChildCount() - 1);
		addView(view, 0);
		setPage(getChildCount() - 1);
	}
	
	private void overScroll()
	{
		View view = getChildAt(0);
		removeViewAt(0);
		setPage(0);
		addView(view);		
	}
	
	public void setPage(int intPage)
	{
		if(!mScroller.isFinished())
		{
			mScroller.abortAnimation();
		}
		
		if(intPage < 0 || intPage > getChildCount()) return;
		
		mCurPage = intPage;
		scrollTo(getWidth() * intPage, 0);
	}
	*/
}