package cn.tcl.platform.sale.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.sale.vo.TerminalPhoto;

public interface ITerminalPhotoDao {
	//选择图片列表
	public List<TerminalPhoto> selectPhotos(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="searchStr") String searchStr,
			@Param(value="order") String order,
			@Param(value="sort") String sort,
			@Param(value="conditions") String conditions) throws Exception;
	//总数计算
	public int countPhotos(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="searchStr") String searchStr,
			@Param(value="conditions") String conditions) throws Exception;
	//获取其中一个
	public TerminalPhoto getPhoto(int pid);
}
