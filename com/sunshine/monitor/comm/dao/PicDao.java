package com.sunshine.monitor.comm.dao;

import java.io.IOException;
import java.sql.SQLException;

public interface PicDao {

	public byte[] getPic2(String xh) throws SQLException, IOException;

	public byte[] getLjPicForBkxh(String xh)throws SQLException, IOException;
	
	public byte[] getKktpForKdbh(String xh)throws SQLException,IOException;
	
	public Boolean existPic(String bkxh);
	
	public byte[] getClbksqb(String xh) throws SQLException, IOException;
	
	public byte[] getLajds(String xh) throws SQLException, IOException;
	
	public byte[] getYjcns(String xh) throws SQLException, IOException;
}
