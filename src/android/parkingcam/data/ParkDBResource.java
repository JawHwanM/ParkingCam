/**
	@file	ParkDBResource.java
	@date	2015/08/12
	@author	JaeHwanM
	@brief	Base 데이터베이스 자원관리
*/
package android.parkingcam.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.parkingcam.common.Constants;
import android.widget.Toast;

/**
 * 데이터 베이스 자원 클래스
 * @author JaeHwanM
 * @version 1.0
 */
public class ParkDBResource 
{
	private final Context	mCtxContext;							/**< 컨텍스트 */
	
	/**
     * 생성자
     * @param ctxContext 컨텍스트
     */
    public ParkDBResource(Context ctxContext)
    {
    	this.mCtxContext = ctxContext;
    }
    
    /**
     * 사용자정보 정보를 리턴한다.
     * @param strFieldValue 코드구분
     * @param strInitCodeName 초기코드명
     * @return 공통코드리스트
     */
    public List<UserInfo> getUserInfo(String strSearchValue)
    {
    	List<UserInfo> lstCode = null;
    	ParkDBAdapter clsDBAdapter = null;
		Cursor csCursor = null;
		String strWhere	= null;
		String strOrder	= null;
		
		try
		{
			lstCode = new ArrayList<UserInfo>();
			
			strWhere	= "FIELD_VALUE='"+ strSearchValue +"' ";
			strOrder	= "COMM_ORDER";
			
			clsDBAdapter = new ParkDBAdapter(this.mCtxContext);
			clsDBAdapter.open();
			csCursor = clsDBAdapter.selectQuery(Constants.TABLE_NAME_USER_INFO, strWhere, null, strOrder);
		    		    
		    if(csCursor != null)
	        {
	        	while(csCursor.moveToNext())
	    		{
	        		UserInfo clsUserInfo = new UserInfo();
	        		clsUserInfo.setFirstLoad(csCursor.getString(csCursor.getColumnIndex("FIRST_LOAD")));
    				lstCode.add(clsUserInfo);
	    		}
	        }
		}
		catch (Exception e)
		{
			Toast.makeText(mCtxContext, "데이터 검색중 에러가 발생하였습니다", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		finally
		{
			csCursor.close();
			clsDBAdapter.close();
		}
		return lstCode;
    }
    
    /**
     * 사진정보 정보를 리턴한다.
     * @param strFieldValue 코드구분
     * @param strInitCodeName 초기코드명
     * @return 공통코드리스트
     */
    public List<PhotoInfo> getPhotoInfo(String strSearchValue)
    {
    	List<PhotoInfo> lstCode = null;
    	ParkDBAdapter clsDBAdapter = null;
		Cursor csCursor = null;
		String strWhere	= null;
		String strOrder	= null;
		
		try
		{
			lstCode = new ArrayList<PhotoInfo>();
			
			strWhere	= "FIELD_VALUE='"+ strSearchValue +"' ";
			strOrder	= "COMM_ORDER";
			
			clsDBAdapter = new ParkDBAdapter(this.mCtxContext);
			clsDBAdapter.open();
			csCursor = clsDBAdapter.selectQuery(Constants.TABLE_NAME_USER_INFO, strWhere, null, strOrder);
		    		    
		    if(csCursor != null)
	        {
	        	while(csCursor.moveToNext())
	    		{
	        		PhotoInfo clsPhotoInfo = new PhotoInfo();
	        		clsPhotoInfo.setPhotoDate(csCursor.getString(csCursor.getColumnIndex("PHOTO_DATE")));
	        		clsPhotoInfo.setPhotoMemo(csCursor.getString(csCursor.getColumnIndex("PHOTO_MEMO")));
	        		clsPhotoInfo.setCoordX(csCursor.getString(csCursor.getColumnIndex("COORD_X")));
	        		clsPhotoInfo.setCoordY(csCursor.getString(csCursor.getColumnIndex("COORD_Y")));
    				lstCode.add(clsPhotoInfo);
	    		}
	        }
		}
		catch (Exception e)
		{
			Toast.makeText(mCtxContext, "데이터 검색중 에러가 발생하였습니다", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		finally
		{
			csCursor.close();
			clsDBAdapter.close();
		}
		return lstCode;
    }
}
