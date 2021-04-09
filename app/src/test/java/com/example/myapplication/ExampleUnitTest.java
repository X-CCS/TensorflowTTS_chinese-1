package com.example.myapplication;

//import com.foodom.greetrobot.util.CRC16;
//import com.foodom.greetrobot.util.Util;
//import com.foodom.greetrobot.ybs.SubDisplayService;
//import com.foodom.ybs.base.utils.StringUtils;
//import com.foodom.yunji.utils.DigestUtils;
//import com.foodom.yunji.utils.SignUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.Locale;

import static org.junit.Assert.*;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import net.tatans.tensorflowtts.utils.Zhuan;
import net.tatans.tensorflowtts.utils.ZhProcessor;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.WordDictionary;
import android.content.Context;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.io.*;
import java.util.*;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.multipinyin.MultiPinyinConfig;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.multipinyin.MultiPinyinConfig;

//private static final Pattern COMMA_NUMBER_RE = Pattern.compile("([0-9][0-9\\,]+[0-9])");

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 * 单元测试
 */
@RunWith(JUnit4.class)
public class ExampleUnitTest {

    // 从数字中去除逗号 private
    public String removeCommasFromNumbers(String text) {
        Matcher m = Pattern.compile("([0-9][0-9\\,]+[0-9])").matcher(text);
        while (m.find()) {
//            String s = m.group().replaceAll(",", "");// 将,替换成空
//            text = text.replaceFirst(m.group(), s);
//            String s1 = m.group().replaceAll("，", "");// 将,替换成空
//            text = text.replaceFirst(m.group(), s1);
//            String s2 = m.group().replaceAll("。", "");// 将,替换成空

            String str = m.group().replaceAll( "[\\pP+~$`^=|<>～｀＄＾＋＝｜＜＞￥×。，]" , "");
            text = text.replaceFirst(m.group(), str);
        }
        return text;
    }

    //输出变量类型的类，
    public static String getType(Object test) {
        return test.getClass().getName().toString();

    }
    @Test
    public void aaa() throws BadHanyuPinyinOutputFormatCombination {
        JiebaSegmenter segmenter = new JiebaSegmenter();
        String text = "优碧胜 1+5=7 2020-04-05 123456 java Hello  imooc 测试";
//        String text = "这是一个伸手不见五指的黑夜。我叫孙悟空，我爱北京，我爱Python和Cpp。";

        // 1、去除string 中的标点符号 []中添加需要处理的标点符号
        text = text.replaceAll( "[\\~$`^|<>～｀＄＾｜＜＞￥×。，,]" , "");
        System.out.println("text去除标点符号之后的结果:"+text);

        // 2、文本正则化
        text = Zhuan.zuzhuang(text);
        System.out.println("text文本正则化之后的结果:"+text);

        // 3、分词
//        String text_test = "塑料管件我爱Python和C++" ;
//        System.out.println( "Jeba 分词  -------------" );
        // 词典路径为Resource/jieba_user.dict
        Path path = Paths.get(new File( getClass().getClassLoader().getResource("jieba_user.dict").getPath() ).getAbsolutePath() ) ;
        WordDictionary.getInstance().loadUserDict( path ) ;
        // 对分词后的结果进行解析
        List<SegToken> res= segmenter.process(text , JiebaSegmenter.SegMode.SEARCH);


        String str1="";
        for (int i=0;i<res.size();i++){
            SegToken token = res.get(i);
            str1 += token.word+" ";
        }
        System.out.println("text分词之后的结果:"+str1);
//        System.out.println("text分词之后的结果的类型:"+getType(str1));
       // 4、多音字处理
        // 自定义用户拼音词典 如 吸血鬼日记 (xi1,xue4,gui3,ri4,ji4)
        MultiPinyinConfig.multiPinyinPath="/Users/ccs/Desktop/BGY_projects/TensorflowTTS_chinese-1/app/src/main/resources/pinyindb/my_multi_pinyin.txt";
        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
        outputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);
        outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
//        "吸血鬼日记鬼日记..."
        text = PinyinHelper.toHanYuPinyinString(str1, outputFormat, " ", true);
//        System.out.println(PinyinHelper.toHanYuPinyinString(str1, outputFormat, ";", true));
        System.out.println("text转拼音之后的结果:"+text);
    }

    @Test
    public void testDemo() {
        JiebaSegmenter segmenter = new JiebaSegmenter();
        String[] sentences =
                new String[] {"这是一个伸手不见五指的黑夜。我叫孙悟空，我爱北京，我爱Python和C++。", "我不喜欢日本和服。", "雷猴回归人间。",
                        "工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作", "结果婚的和尚未结过婚的"};
        for (String sentence : sentences) {
            System.out.println(segmenter.process(sentence, JiebaSegmenter.SegMode.INDEX).toString());
        }
    }

//    @Test
//    public void addition_isCorrect() {
//        assertEquals(4, 2 + 2);
//    }
//
//    @Test
//    public void testCrc(){
//        // 数据	0xAA	0xA0	0x05	0x01, 0x23, 0x45, 0x67, 0x89	0x0442
//        String crcStr = CRC16.getCrc(/*new byte[]{0x05,0x01,0x23,0x45,0x67,(byte)0x89}*/ Util.hexStringToByteArray("AAA103020101")) ;
//        System.out.println("crc = " + crcStr );
//     //   Util.hexStringToByteArray("050123456789") ;
//    }
//
//    @Test
//    public  void ttttt() {
//      //  productId=WTBLS100965&taskId=1617341590049&ts=1617341781988&sign=afb99b07854748ed55ed1b55d1813299
//      //  productId=WTBLS100965&taskId=1617340621669&ts=1617341251201&sign=83c434271643575ff1147f0b724c45a9
//        // sn 15d60288b5bb42a39980d256502d38f0
//  // {"ts":1617341920262,"sign":"40f87f5c6c95f68d65a9db344e5f8319","productId":"WTBLS100965","target":"1621","type":"room","taskId":"1617341590049"}
//
//
//        final HashMap<String,String> params = new HashMap<>() ;
//        params.put("type","room");
//        params.put("productId","WTBLS100965");
//        params.put("taskId","1617341590049");
//        params.put("target","1621");
//        final String robot_sn = "15d60288b5bb42a39980d256502d38f0" ;
//        final String ts = "1617341920262" ;
//        final String sign  = SignUtils.sign(params,ts+"",robot_sn) ;
//        System.out.println("s = " +sign);
//
//
//    }
//
//    private  String validateOpenApiSign(String ts, Map<String, String> params) {
//        List<String> kvs = new ArrayList<>();
//        for (Map.Entry<String, String> entry : params.entrySet()) {
//            String key = StringUtils.trim(entry.getKey());
//            String value = StringUtils.trim(entry.getValue());
//            if (key.equalsIgnoreCase("appname") ||
//                    key.equalsIgnoreCase("secret") ||
//                    key.equalsIgnoreCase("ts") ||
//                    key.equalsIgnoreCase("sign") ||
//                    StringUtils.isBlank(value))
//                continue;
//
//            kvs.add(key + ":" + value);
//        }
//        Collections.sort(kvs);
//        kvs.add("ts:" + ts);
//        kvs.add("sn:" + ""); // FIXME 填机器人对应的sn
//
//        return DigestUtils.md5DigestAsHex(StringUtils.join(kvs, "|").getBytes());
//    }

}