package com.typartner.find.common.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

/**  
 * Copyright (C) 2012 软通动力
 * 文件名: PinYinUtil.java
 * 包路径: com.iss.common.util
 * 创建描述  
 *        创建人：zhaohy
 *        创建日期：2013-7-22 上午10:34:37
 *        内容描述：
 * 修改描述  
 *        修改人：zhaohy 
 *        修改日期：2013-7-22 上午10:34:37 
 *        修改内容:
 * 版本: V1.0   
 */
public class PinyinUtil {
	
	/** 
     * 获取汉字串拼音首字母，英文字符不变 
     * 
     * @param chinese 汉字串 
     * @return 汉语拼音首字母 
     */ 
    public static String cnToFirstSpell(String chinese) { 
            StringBuffer pybf = new StringBuffer(); 
            char[] arr = chinese.toCharArray(); 
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat(); 
            defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);     //设置大小写格式,转换后以全小写方式输出
            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  //无声调表示，例如：liu
            defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);	  //设置特殊拼音ü的显示格式,以V表示该字符，例如：lv
            for (int i = 0; i < arr.length; i++) { 
                if (arr[i] > 128) { 
                    try { 
                        String[] _t = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat); 
                        if (_t != null) { 
                             pybf.append(_t[0].charAt(0)); 
                        } 
                    } catch (Exception e) { 
                            e.printStackTrace(); 
                    } 
                } else { 
                        pybf.append(arr[i]); 
                } 
            } 
            return pybf.toString().replaceAll("\\W", "").trim(); 
    } 

    /** 
     * 获取汉字串拼音，英文字符不变 
     * 
     * @param chinese 汉字串 
     * @return 汉语拼音 
     */ 
    public static String cnToSpell(String chinese) { 
            StringBuffer pybf = new StringBuffer(); 
            char[] arr = StringUtil.stringFilter(chinese).toCharArray(); 
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat(); 
            defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);     //设置大小写格式,转换后以全小写方式输出
            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  //无声调表示，例如：liu
            defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);	  //设置特殊拼音ü的显示格式,以V表示该字符，例如：lv
            for (int i = 0; i < arr.length; i++) { 
                if (arr[i] > 128) { 
                    try { 
                        pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0]); 
                    } catch (Exception e) { 
                            e.printStackTrace(); 
                    } 
                } else { 
                        pybf.append(arr[i]); 
                } 
            } 
            return pybf.toString(); 
    }
}
