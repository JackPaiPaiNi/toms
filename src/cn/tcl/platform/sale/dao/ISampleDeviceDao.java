package cn.tcl.platform.sale.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.sale.vo.SampleDevice;

public interface ISampleDeviceDao {
	//选择样机设备
	public List<SampleDevice> selectSampleDevices(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="searchStr") String searchStr,
			@Param(value="order") String order,
			@Param(value="sort") String sort,
			@Param(value="conditions") String conditions) throws Exception;
	//总数计算
	public int countSampleDevices(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="searchStr") String searchStr,
			@Param(value="conditions") String conditions) throws Exception;
	//获取其中一个
	public SampleDevice getSampleDevice(int pid);
	
	//导出
	public List<SampleDevice> searchExporSamples(@Param(value="conditions") String conditions) throws Exception;
}
