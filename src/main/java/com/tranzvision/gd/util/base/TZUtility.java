package com.tranzvision.gd.util.base;

public class TZUtility {
	/* JSON特殊字符处理 */
	public static String transFormchar(String str_souce) {
		if (str_souce != null) {
			/*** Backslash caracter（反斜杠，ASCII码：5C）--> \\ ***/
			str_souce = str_souce.replace((char) (92), '\\');
			/*** Backspace（回退键，ASCII码：08）--> \b; **/
			str_souce = str_souce.replace((char) (8), '\b');
			/*** Form feed （换页符，ASCII码：0C）--> \f ***/
			str_souce = str_souce.replace((char) (12), '\f');
			/*** New line （换行符，ASCII码：0A）--> \n ***/
			str_souce = str_souce.replace((char) (10), '\n');
			/*** Carriage return（回车符，ASCII码：0D）--> \r ***/
			str_souce = str_souce.replace((char) (13), '\r');
			/*** Tab （制表符，ASCII码：09）--> \t ***/
			str_souce = str_souce.replace((char) (9), '\t');
			/*** Apostrophe or single quote（单引号，ASCII码：39）--> \' ***/
			str_souce = str_souce.replace((char) (39), '\'');
			/*** Double quote （双引号，ASCII码：34）--> \" ***/
			str_souce = str_souce.replace((char) (34), '\"');

			return str_souce;
		}
		return "";
	}
	
	/****自动编号*****/
	public int GetSeqNum(String tableName,String colName){
		int index = 0;
		try{
			/*
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			Connection conn = MySql.getMySqlConnection();
			String lockSQL = "update PS_TZ_SEQNUM_T set tz_seqnum = tz_seqnum + 1 where tz_table_name = ? and tz_col_name = ?";
			pstmt = conn.prepareStatement(lockSQL);
			pstmt.setString(1, tableName);
			pstmt.setString(2, colName);
			pstmt.executeUpdate();
			
			String selectSql = "select tz_seqnum from PS_TZ_SEQNUM_T where tz_table_name = ? and tz_col_name = ?";
			pstmt = conn.prepareStatement(selectSql);
			pstmt.setString(1, tableName);
			pstmt.setString(2, colName);
			rs = pstmt.executeQuery();
			if(rs.next()){
				index = rs.getInt(1);
			}
			
			String comitSQL = "commit";
			if(index == 0){
				String insertSQL = "insert into PS_TZ_SEQNUM_T (tz_table_name, tz_col_name, tz_seqnum) values (?, ?, 1)";
				pstmt = conn.prepareStatement(insertSQL);
				pstmt.setString(1, tableName);
				pstmt.setString(2, colName);
				pstmt.executeUpdate();
				
				pstmt = conn.prepareStatement(comitSQL);
				pstmt.execute();
			}else{
				pstmt = conn.prepareStatement(comitSQL);
				pstmt.execute();
			}
			rs.close();
			pstmt.close();
			conn.close();
			*/
		}catch(Exception e){
			e.printStackTrace();
		}
		return index;
	}

}
