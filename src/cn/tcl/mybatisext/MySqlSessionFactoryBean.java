package cn.tcl.mybatisext;

import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import cn.tcl.common.WebPageUtil;
  
  
/** 
 * @ClassName: MySqlSessionFactoryBean 
 * @Description: mybatis自动扫描别名路径（新增通配符匹配功能） 
 * @author sunxuanying
 * @date 2014年12月9日 上午9:36:23 
 */  
public class MySqlSessionFactoryBean extends SqlSessionFactoryBean 
{  
	private final static Logger logger = Logger.getLogger(MySqlSessionFactoryBean.class);
    private static final String ROOT_PATH = "cn" + File.separator + "tcl" + File.separator;  
    private static final String ROOT_PATH_SPLIT = ",";  
    private static final String[] PATH_REPLACE_ARRAY = { "]" };  

    public void setTypeAliasesPackage(String typeAliasesPackage)
    {  
        if (!WebPageUtil.isStringNullAvaliable(typeAliasesPackage)) 
        {  
            super.setTypeAliasesPackage(typeAliasesPackage);  
            return;  
        }  
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();  
        StringBuffer typeAliasesPackageStringBuffer = new StringBuffer();  
        try
        {  
            for (String location : typeAliasesPackage.split(","))
            {  
                if (!WebPageUtil.isStringNullAvaliable(location)) 
                {  
                    continue;  
                }  
                location = "classpath*:" + location.trim().replace(".", File.separator);  
                typeAliasesPackageStringBuffer.append(getResources(resolver, location));  
            }  
        } 
        catch (IOException e)
        {  
            logger.error(e.getMessage(), e);  
        }  
        if ("".equals(typeAliasesPackageStringBuffer.toString())) 
        {  
            throw new RuntimeException("mybatis typeAliasesPackage 路径扫描错误！请检查applicationContext.xml@sqlSessionFactory配置！");  
        }  
        typeAliasesPackage = replaceResult(typeAliasesPackageStringBuffer.toString()).replace(File.separator, ".");  
        super.setTypeAliasesPackage(typeAliasesPackage);  
    }  
  
    private String getResources(ResourcePatternResolver resolver, String location) throws IOException 
    {  
        StringBuffer resourcePathStringBuffer = new StringBuffer();  
        for (Resource resource : resolver.getResources(location)) 
        {  
            String description = resource == null ? "" : resource.getDescription();  
            if (!WebPageUtil.isStringNullAvaliable(resource.getDescription()) || description.indexOf(ROOT_PATH) == -1) 
            {  
                continue;  
            }  
            resourcePathStringBuffer.append(description.substring(description.indexOf(ROOT_PATH))).append(ROOT_PATH_SPLIT);  
        }  
        return resourcePathStringBuffer.toString();  
    }  
  
    private String replaceResult(String resultStr) 
    {  
        for (String replaceStr : PATH_REPLACE_ARRAY) 
        {  
            resultStr = resultStr.replace(replaceStr, "");  
        }  
        return resultStr;  
    }  
}
