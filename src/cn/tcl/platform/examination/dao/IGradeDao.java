package cn.tcl.platform.examination.dao;

import java.util.List;
import java.util.Map;

import cn.tcl.platform.examination.vo.Grade;

public interface IGradeDao {

	/**
	 * 查看扫码考试数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Grade> queryTempGreadData (Map<String,Object> map) throws Exception;
	
	/**
	 * 查看扫码考试数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Grade> exportQueryTempGreadData (Map<String,Object> map) throws Exception;
	
	/**
	 * 查看扫码考试数据行数
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int queryTempGreadCount (Map<String,Object> map) throws Exception;
	
}
