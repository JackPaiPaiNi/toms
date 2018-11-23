package cn.tcl.platform.training.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tcl.platform.training.dao.CourseTypeDao;
import cn.tcl.platform.training.service.CourseTypeService;
import cn.tcl.platform.training.vo.CourseType;

@Service("courseTypeService")
public class CourseTypeServiceImpl implements CourseTypeService {
	
	protected Logger log=Logger.getLogger(this.getClass());
	
	@Autowired
	private CourseTypeDao courseTypeDao;

	@Override
	public List<CourseType> GetLevelOneList() throws Exception {
		// TODO Auto-generated method stub
		return courseTypeDao.GetLevelOneList();
	}

	@Override
	public List<CourseType> GetSubTypeListById(int typeId) throws Exception {
		// TODO Auto-generated method stub
		return courseTypeDao.GetSubTypeListById(typeId);
	}

}
