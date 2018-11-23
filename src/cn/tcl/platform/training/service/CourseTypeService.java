package cn.tcl.platform.training.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.training.vo.CourseType;

public interface CourseTypeService {
	//查询一级课程类别列表
	public List<CourseType> GetLevelOneList() throws Exception;
	//根据类别id查询更低一级课程类别列表
	public List<CourseType> GetSubTypeListById(@Param("typeId")int typeId) throws Exception;
}
