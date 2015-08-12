/**
	@file	ParkDBTable.java
	@date	2015/08/12
	@author	JaeHwanM
	@brief	Base 데이터베이스 테이블 정의
*/
package android.parkingcam.data;

import java.util.ArrayList;

/**
 * 데이터 베이스 정의 클래스
 * @author JaeHwanM
 * @version 1.0
 */
public class ParkDBTable 
{
	/** 앱 기본정보 테이블 컬럼 */
	public static final String[][] TABLE_COLUMNS_APP_INFO = {
		{"FIRST_LOAD",	"TEXT"}
	};
	
	/** 사진/메모 테이블 컬럼 */
	public static final String[][] TABLE_COLUMNS_PHOTO_INFO = {
		{"PHOTO_DATE",	"TEXT"},
		{"PHOTO_MEMO",	"TEXT"},
		{"COORD_X",		"TEXT"},
		{"COORX_Y",		"TEXT"}
	};
	
	/**
	 * 테이블 생성문을 리턴한다.
	 * @return 쿼리
	 */
	public static ArrayList<String> getCreateTable()
	{
		ArrayList<String> lstQuery = new ArrayList<String>();
		lstQuery.add("CREATE TABLE IF NOT EXISTS CODE_MAST(" + getCreateColumn(TABLE_COLUMNS_APP_INFO) + ");" );
		lstQuery.add("CREATE TABLE IF NOT EXISTS CODE_DETAIL(" + getCreateColumn(TABLE_COLUMNS_PHOTO_INFO) + ");" );
		return lstQuery;
	}
	
	/**
	 * 테이블 DROP문장을 리턴한다.
	 * @return 쿼리
	 */
	public static ArrayList<String> getDropTable()
	{
		ArrayList<String> lstQuery = new ArrayList<String>();
		lstQuery.add("DROP TABLE IF EXISTS APP_INFO;");
		lstQuery.add("DROP TABLE IF EXISTS PHOTO_INFO;");
		return lstQuery;
	}	
	
	/**
	 * SQL Create 문장 생성에 필요한 컬럼을 콤마로 구분하여 나열한후 리턴한다.  
	 * @param arrColumnInfo 컬럼정보
	 * @return 쿼리
	 */
	public static String getCreateColumn(String[][] arrColumnInfo)
	{
		int intIndex = 0;
		
		StringBuffer sbBuffer = new StringBuffer();
		for(intIndex = 0; intIndex < arrColumnInfo.length; intIndex++)
		{
			sbBuffer.append(arrColumnInfo[intIndex][0] + " " + arrColumnInfo[intIndex][1] + ",");
		}
		return sbBuffer.toString().substring(0, sbBuffer.length()-1);
	}
}
