package com.ltchen.utils.property;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 
 * @file : PropertyUtil.java
 * @date : 2017年3月28日
 * @author : ltchen
 * @email : loupipalien@gmail.com
 * @desc : property工具类
 */
public class PropertyUtil {

	/**
     * propPath：默认加载resource文件夹下的文件名,多层目录写相对路径,不以/开头
     * @param propPath
     * @param clazz
     * @return
     */
    public static Properties getProperty(String propPath, Class<?> clazz){
          Properties prop = new Properties();
          InputStream in;
          try {
               in = new BufferedInputStream (clazz.getClassLoader().getResource(propPath).openStream());
               prop.load(new InputStreamReader(in, "UTF-8"));
          } catch (IOException e) {
               e.printStackTrace();
          }
          return prop;
    }

}
