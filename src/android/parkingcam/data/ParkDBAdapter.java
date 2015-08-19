/**
	@file	ParkDBAdapter.java
	@date	2015/08/10
	@author	JaeHwanM
	@brief	Base 데이터베이스 아답터
*/
package android.parkingcam.data;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 데이터 아답터 클래스
 * @author JaeHwanM
 * @version 1.0
 */
public class ParkDBAdapter
{
	private static final int DB_VERSION	= 1;					/**< 데이터베이스 버전 */
	private static final String DB_NAME	= "parking-cam.db";		/**< 데이터베이스 명 */
    
    private DatabaseHelper	mDhHelper;							/**< 데이터베이스 헬퍼 클래스 */
    private SQLiteDatabase	mSdDataBase;						/**< SQLite 데이터베이스 */
    private final Context	mCtxContext;						/**< 컨텍스트 */

    /**
     * 생성자
     * @param ctxContext 컨텍스트
     */
    public ParkDBAdapter(Context ctxContext)
    {
    	this.mCtxContext = ctxContext;
    }
    
    /**
     * 데이터베이스 객체 획득(Open)
     * @return BaseDBAdapter 아답터
     * @throws SQLException SQL 예외
     */
    public ParkDBAdapter open() throws SQLException
    {
        mDhHelper	= new DatabaseHelper(mCtxContext);
        mSdDataBase	= mDhHelper.getWritableDatabase();
        return this;
    }
    
    /**
     * 데이터 베이스 종료
     */
    public void close()
    {
    	try{ if(mSdDataBase != null) { mSdDataBase.close(); mSdDataBase = null; } } catch(Exception ex){}
    	try{ if(mDhHelper	!= null) { mDhHelper.close(); mDhHelper = null; } } catch(Exception ex){}
    }

    /**
     * 트랜잭션 시작
     */
    public void beginTransaction()
    {
    	if(mSdDataBase != null) mSdDataBase.beginTransaction();
    }
    
    /**
     * 트랜잭션 종료
     */
    public void endTransaction()
    {
    	if(mSdDataBase != null) mSdDataBase.endTransaction();
    }
    
    /**
     * 트랜잭션이 성공적으로 끝난것으로 설정
     */
    public void setTransactionSuccessful()
    {

    	if(mSdDataBase != null) mSdDataBase.setTransactionSuccessful();
    }    
    
    /**
     * 데이터베이스 SELECT 쿼리
     * @param strTableName 테이블명
     * @param selection 조건
     * @param selectionArgs 조건파라미터
     * @param strSortOrder 정렬
     * @return Cursor 커서
     */
	public Cursor selectQuery(String strTableName, String selection, String[] selectionArgs, String strSortOrder)
	{
		return mSdDataBase.query(strTableName, null, selection, selectionArgs, null, null, strSortOrder);
	}


	/**
	 * 데이터베이스 INSERT 쿼리
	 * @param strTableName 테이블명
	 * @param cvValues 삽입필드
	 * @return 결과
	 */
    public long insertQuery(String strTableName, ContentValues cvValues)
    {
    	long lngCount = -1;
    	lngCount = mSdDataBase.insert(strTableName, null, cvValues);
    	return lngCount;
    }
    
    /**
     * 데이터베이스 UPDATE 쿼리
     * @param strTableName 테이블명
     * @param values 삽입필드 
     * @param strSelection 조건
     * @param selectionArgs 조건파라미터
     * @return 업데이트결과
     */
    public int updateQuery(String strTableName, ContentValues values, String strSelection, String[] selectionArgs)
	{
    	int intCount = -1;
    	intCount = mSdDataBase.update(strTableName, values, strSelection, selectionArgs);
    	return intCount;
	}	    
	
    /**
     * 데이터베이스 DELETE 쿼리
     * @param strTableName 테이블명
     * @param selection 조건
     * @param selectionArgs 조건파라미터
     * @return 삭제결과
     */
	public int deleteQuery(String strTableName, String selection, String[] selectionArgs)
	{
		int intCount = -1;
		intCount = mSdDataBase.delete(strTableName, selection, selectionArgs);
		return intCount;
	}
	
	/**
	 * Raw 쿼리
	 * @param strQuery SQL문장
	 * @return Cursor 커서
	 * @throws SQLException SQL 예외
	 */
    public Cursor rawQuery(String strQuery) throws SQLException
    {
    	return mSdDataBase.rawQuery(strQuery, null);
    }
  
    /**
     * 데이터베이스 헬퍼
     * 
     * @author 이용민
     * @version 1.0
     */    
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
    	/**
    	 * 생성자
    	 * @param ctxContext 컨텍스트
    	 */
        DatabaseHelper(Context ctxContext)
        {
            super(ctxContext, DB_NAME, null, DB_VERSION);
        }

        /**
         * 생성 이벤트
         * @param sdDataBase 데이터베이스객체
         */	
        @Override
        public void onCreate(SQLiteDatabase sdDataBase)
        {
			System.out.println("----------------------------");
			System.out.println("*DataBaseHelper.onCreate()");
        	ArrayList<String> arlCreateQuery = ParkDBTable.getCreateTable();
        	for(String strQuery:arlCreateQuery)
        	{
        		System.out.println(" - strQuery:" + strQuery);
        		sdDataBase.execSQL(strQuery);
        	}
			System.out.println("----------------------------");        	
        }

        /**
         * 데이터베이스 업그레이드 이벤트
         * @param sdDataBase 데이터베이스객체 
         * @param intOldVersion Old 버전 
         * @param intNewVersion New 버전 
         */
        @Override
        public void onUpgrade(SQLiteDatabase sdDataBase, int intOldVersion, int intNewVersion)
        {
			System.out.println("----------------------------");
			System.out.println("*DataBaseHelper.onUpgrade()");
			ArrayList<String> arlDropQuery = ParkDBTable.getDropTable();
			for(String strQuery:arlDropQuery)
			{
				System.out.println(" - strQuery:" + strQuery);
				sdDataBase.execSQL(strQuery);
			}			
			onCreate(sdDataBase);
			System.out.println("----------------------------");
        }
    }    

}