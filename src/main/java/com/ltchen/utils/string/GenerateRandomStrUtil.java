package com.ltchen.utils.string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GenerateRandomStrUtil {

	 /**
     * 私有化默认构造器
     */
    private GenerateRandomStrUtil() {}
    
	
	public static enum Type {
	       /**
	        * 小写字符型
	        * 大写字符型
	        * 数字型
	        * 符号型
	        * 小写+大写字符型
	        * 小写字符+数字类型
	        * 大写字符+数字类型
	        * 小写字符+大写字符+数字型
	        * 小写字符+大写字符+数字+符号型
	        */
	       LOWERCASE,UPPERCASE,NUMBER,SIGN,
	       LOWERCASE_UPPERCASE,LOWERCASE_NUMBER,UPPERCASE_NUMBER,
	       LOWERCASE_UPPERCASE_NUMBER,LOWERCASE_UPPERCASE_NUMBER_SIGN;
	      }
	      private static String[] lowercase = {
	        "a","b","c","d","e","f","g","h","i","j","k",
	        "l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
	      private static String[] uppercase = {
	        "A","B","C","D","E","F","G","H","I","J","K",
	        "L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	      private static String[] number = {"1","2","3","4","5","6","7","8","9","0"};
	      private static String[] sign = {
	        "~","!","@","#","$","%","^","&","*","(",")","_","+","`","-","=",
	        "{","}","|",":","\"","<",">","?","[","]","\\",";","'",",",".","/"};
	     
	     public static String generateStr(int length){
	           return generateStr(length, Type.LOWERCASE_UPPERCASE_NUMBER_SIGN, true);
	     }
	     
	     public static String generateStr(int length, Type type){
	           return generateStr(length, type, true);
	     }
	     
	     public static String generateStr(int length, Type type, boolean isContainPerType){
	           String str = null;
	           switch (type) {
	           case LOWERCASE:
	                str = generate(length, isContainPerType, lowercase);
	                break;
	           case UPPERCASE:
	                str = generate(length, isContainPerType, uppercase);
	                break;
	           case NUMBER:
	                str = generate(length, isContainPerType, number);
	                break;
	           case SIGN:
	                str = generate(length, isContainPerType, sign);
	                break;
	           case LOWERCASE_UPPERCASE:
	                str = generate(length, isContainPerType, lowercase, uppercase);
	                break;
	           case LOWERCASE_NUMBER:
	                str = generate(length, isContainPerType, lowercase, number);
	                break;
	           case UPPERCASE_NUMBER:
	                str = generate(length, isContainPerType, uppercase, number);
	                break;
	           case LOWERCASE_UPPERCASE_NUMBER:
	                str = generate(length, isContainPerType, lowercase, uppercase, number);
	                break;
	           default:
	                str = generate(length, isContainPerType, lowercase, uppercase, number, sign);
	                break;
	           }
	           return str;
	     }
	     
	     private static String generate(int length,boolean isContainPerType,String[] ...types){
	           Random random = new Random();
	           StringBuffer sb = new StringBuffer();
	           //拼接所有类型字符串
	           List<String> chars = new ArrayList<String>();
	           for (String[] type : types) {
	                chars.addAll(Arrays.asList(type));
	           }
	           //如果需要包含每种字符串,生成字符串长度必须大于字符种类数
	           if(isContainPerType && types.length > length){
	                throw new IllegalArgumentException("the length must be equal or greater than the kinds of type,"
	                           + "the kinds of type is : " + types.length);
	           }
	           //是否包含每种字符
	           if(isContainPerType){
	                for (String[] type : types) {
	                     sb.append(type[random.nextInt(type.length-1)]);
	                }
	                for (int i = 0; i < length - types.length; i++) {
	                     sb.append(chars.get(random.nextInt(chars.size()-1)));
	                }
	           }
	           else{
	                for (int i = 0; i < length; i++) {
	                     sb.append(chars.get(random.nextInt(chars.size()-1)));
	                }
	           }
	           return sb.toString();
	     }

}
