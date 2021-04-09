package net.tatans.tensorflowtts.utils;

import android.content.Context;
import android.util.Log;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.WordDictionary;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import net.sourceforge.pinyin4j.multipinyin.MultiPinyinConfig;
import net.tatans.tensorflowtts.utils.Zhuan;

/**
 * Create by zhp on 2021/2/5
 * Description:
 */
public class ZhProcessor {
    private static final String TAG = "ZhProcessor";

    private static final HashMap<String, String[]> PINYIN_DICT = new HashMap<>();
    private static final HashMap<String, Integer> SYMBOL_TO_ID = new HashMap<>();
    private static final Map<String, String> NUMBER_2_HAN_ZI = new HashMap<>();

    private static final Pattern ZH_PATTERN = Pattern.compile("[\u4e00-\u9fa5]");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("[0-9]");
    private static final Pattern SYMBOL_PATTERN = Pattern.compile("[\n，。？?！!,;；、:：]");
    private static final int TYPE_UNKONWN = -1;
    private static final int TYPE_ZH = 0;
    private static final int TYPE_NUMBER = 1;
    // 匹配数字的正则表达式
    private static final Pattern COMMA_NUMBER_RE = Pattern.compile("([0-9][0-9\\,]+[0-9])");
    private static final Pattern DECIMAL_RE = Pattern.compile("([0-9]+\\.[0-9]+)");
    private static final Pattern POUNDS_RE = Pattern.compile("£([0-9\\,]*[0-9]+)");
    private static final Pattern DOLLARS_RE = Pattern.compile("\\$([0-9.\\,]*[0-9]+)");
    private static final Pattern RMB_RE = Pattern.compile("￥([0-9.\\,]*[0-9]+)");
    private static final Pattern NUMBER_RE = Pattern.compile("[0-9]+");
    private static final Pattern DATE_RE = Pattern.compile("([0-9]{2,4}-[0-9]{2}-[0-9]{2} )?([0-9]{2}:[0-9]{2}:[0-9]{2})?");
    JiebaSegmenter segmenter = new JiebaSegmenter(); //初始化一次，会比较久

    static {
        NUMBER_2_HAN_ZI.put("0", "零");
        NUMBER_2_HAN_ZI.put("1", "一");
        NUMBER_2_HAN_ZI.put("2", "二");
        NUMBER_2_HAN_ZI.put("3", "三");
        NUMBER_2_HAN_ZI.put("4", "四");
        NUMBER_2_HAN_ZI.put("5", "五");
        NUMBER_2_HAN_ZI.put("6", "六");
        NUMBER_2_HAN_ZI.put("7", "七");
        NUMBER_2_HAN_ZI.put("8", "八");
        NUMBER_2_HAN_ZI.put("9", "九");
        NUMBER_2_HAN_ZI.put(".", "点");
    }

    public ZhProcessor(Context context){
//        segmenter = new JiebaSegmenter();
        try {
            InputStream inputStream =
                    context.getAssets().open("baker_mapper.json");

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer, 0, buffer.length)) > 0) {
                out.write(buffer, 0, len);
            }
            String json = new String(out.toByteArray());
            out.close();

            JSONObject root = new JSONObject(json);
            JSONObject pinyinDictObject = root.getJSONObject("pinyin_dict");
            Iterator<String> pinyinKeys = pinyinDictObject.keys();
            while (pinyinKeys.hasNext()) {
                String key = pinyinKeys.next();
                JSONArray jsonArray = pinyinDictObject.getJSONArray(key);
                String[] array = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    array[i] = jsonArray.getString(i);
                }
                PINYIN_DICT.put(key, array);
            }

            JSONObject symbolToIdObject = root.getJSONObject("symbol_to_id");
            Iterator<String> symbolKeys = symbolToIdObject.keys();
            while (symbolKeys.hasNext()) {
                String key = symbolKeys.next();
                Integer value = symbolToIdObject.getInt(key);
                SYMBOL_TO_ID.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int[] text2ids(String text) {
//        String parseText = parseText(text);
        // 有 throw的得抛出异常跟捕获异常才可以
        String parseText;
        try {
            parseText = parseText(text);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        String[] pinyin;
        try {
            pinyin = convert2Pinyin(parseText);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        String symbols = pinyin2Symbol(pinyin);
        Log.i(TAG, symbols);
        return symbol2ids(symbols.split(" "));
    }

    //输出变量类型的类，
    public static String getType(Object test) {
        return test.getClass().getName().toString();

    }

    // private
//    JiebaSegmenter segmenter = new JiebaSegmenter();
    public String parseText(String text) throws Exception {
//        text = removeCommasFromNumbers(text);
//        text = expandPounds(text);
//        text = expandRmb(text);
//        text = expandDollars(text);
//        text = expandDecimals(text);
//        text = expandDate(text);
//        text = expandCardinals(text);
//        text = Zhuan.zuzhuang(text);
///////////////////我的文本正则化//////////////////////////////////
//        JiebaSegmenter segmenter = new JiebaSegmenter();
        // 1、去除string 中的标点符号 []中添加需要处理的标点符号
        text = text.replaceAll( "[。，,]" , "");
//        text = removeCommasFromNumbers(text);
//        System.out.println("text去除标点符号之后的结果:"+text);

        // 2、文本正则化
        text = Zhuan.zuzhuang(text);
//        System.out.println("text文本正则化之后的结果:"+text);

        // 3、分词
//        String text_test = "塑料管件我爱Python和C++" ;
//        System.out.println( "Jeba 分词  -------------" );
        // 词典路径为Resource/jieba_user.dict

//        Path path = Paths.get(new File( getClass().getClassLoader().getResource("jieba_user.dict").getPath() ).getAbsolutePath() ) ;

//        WordDictionary.getInstance().loadUserDict( path ) ;
        // 对分词后的结果进行解析
        List<SegToken> res= segmenter.process(text , JiebaSegmenter.SegMode.SEARCH);


        String str1="";
        for (int i=0;i<res.size();i++){
            SegToken token = res.get(i);
            str1 += token.word+" ";
        }
//        System.out.println("text分词之后的结果:"+str1);
//        System.out.println("text分词之后的结果的类型:"+getType(str1));
        text = str1;
//        // 4、多音字处理（这里先不加）
//        // 自定义用户拼音词典 如 吸血鬼日记 (xi1,xue4,gui3,ri4,ji4)
//        MultiPinyinConfig.multiPinyinPath="/Users/ccs/Desktop/BGY_projects/TensorflowTTS_chinese-1/app/src/main/resources/pinyindb/my_multi_pinyin.txt";
//        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
//        outputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);
//        outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
////        "吸血鬼日记鬼日记..."
//        text = PinyinHelper.toHanYuPinyinString(str1, outputFormat, "", true);
////        System.out.println(PinyinHelper.toHanYuPinyinString(str1, outputFormat, ";", true));
//        System.out.println("text转拼音之后的结果:"+text);

///////////////////我的文本正则化//////////////////////////////////

        StringBuilder pinyinBuilder = new StringBuilder();
        char[] chars = text.toCharArray();
        String hanzi = "";
        String number = "";
        int lastType = TYPE_UNKONWN;
        int type = TYPE_UNKONWN;
        for (int i = 0; i < chars.length; i++) {
            String s = String.valueOf(chars[i]);
            if (isZhWord(s)) {
                hanzi += s;
                type = TYPE_ZH;
            } else if (isNumber(s)) {
                number += s;
                type = TYPE_NUMBER;
            } else if (".".equals(s) && lastType == TYPE_NUMBER) {
                number += s;
            } else if (isSymbol(s) && lastType != TYPE_UNKONWN) {
                pinyinBuilder.append(number2Hanzi(number))
                        .append(hanzi)
                        .append("#3 ");
                hanzi = "";
                number = "";
            }

            if (lastType != TYPE_UNKONWN && lastType != type) {
                switch (lastType) {
                    case TYPE_NUMBER:
                        pinyinBuilder.append(number2Hanzi(number));
                        number = "";
                        break;
                    case TYPE_ZH:
                        pinyinBuilder.append(hanzi);
                        hanzi = "";
                        break;
                }
            }

            lastType = type;
        }

        pinyinBuilder.append(hanzi)
                .append(number);

        return pinyinBuilder.toString();
    }
    // 从数字中去除逗号 private
    public String removeCommasFromNumbers(String text) {
        Matcher m = COMMA_NUMBER_RE.matcher(text);
        while (m.find()) {
            String s = m.group().replaceAll(",", "");// 将,替换成空
            text = text.replaceFirst(m.group(), s);
        }
        return text;
    }
    // 添加欧元后缀
    //private
    public String expandPounds(String text) {
        Matcher m = POUNDS_RE.matcher(text);
        while (m.find()) {
            text = text.replaceFirst(m.group(), m.group() + "欧元");
        }
        return text;
    }
    // 添加 元 后缀
    private String expandRmb(String text) {
        Matcher m = RMB_RE.matcher(text);
        while (m.find()) {
            text = text.replaceFirst(m.group(), m.group() + "元");
        }
        return text;
    }
    //添加美元 后缀
    private String expandDollars(String text) {
        Matcher m = DOLLARS_RE.matcher(text);
        while (m.find()) {
            String dollars = "0";
            String cents = "0";
            String spelling = "";
            String s = m.group().substring(1);
            String[] parts = s.split("\\.");
            if (!s.startsWith(".")) {
                dollars = parts[0];
            }
            if (!s.endsWith(".") && parts.length > 1) {
                cents = parts[1];
            }
            if (!"0".equals(dollars)) {
                spelling += parts[0] + "美元";
            }
            if (!"0".equals(cents) && !"00".equals(cents)) {
                spelling += parts[1] + "美分";
            }
            // \(转意）\ （原来的\） 类似 \a
            text = text.replaceFirst("\\\\" + m.group(), spelling);
        }
        return text;
    }
    //处理小数点
    private String expandDecimals(String text) {
        Matcher m = DECIMAL_RE.matcher(text);
        while (m.find()) {
            String[] ss = m.group().split("\\.");
            String s = ss[0] + "点" + number2Hanzi(ss[1]);
            text = text.replaceFirst(m.group(), s);
        }
        return text;
    }
    // 处理数字
    private String expandDate(String text) {
        Matcher m = DATE_RE.matcher(text);
        while (m.find()) {
            String s = NumberToCH.dateToCH(m.group());
            text = text.replaceFirst(m.group(), s);
        }
        return text;
    }
    // 扩展 基数
    private String expandCardinals(String text) {
        Matcher m = NUMBER_RE.matcher(text);
        while (m.find()) {
            // 解析 https://blog.csdn.net/u010502101/article/details/79162587
            int l = Integer.valueOf(m.group());
            String spelling = NumberToCH.numberToCH(l);
            text = text.replaceFirst(m.group(), spelling);
        }
        return text;
    }

    private static boolean isZhWord(String s) {
        return ZH_PATTERN.matcher(s).matches();
    }

    private static boolean isNumber(String s) {
        return NUMBER_PATTERN.matcher(s).matches();
    }

    private static boolean isSymbol(String s) {
        return SYMBOL_PATTERN.matcher(s).matches();
    }

    private static String[] convert2Pinyin(String text) throws Exception {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        String pinyin = PinyinHelper.toHanYuPinyinString(text, format, "", true);
        StringBuilder builder = new StringBuilder();
        char[] chars = pinyin.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            String s = String.valueOf(chars[i]);
            builder.append(s);
            if (isNumber(s)) {
                builder.append(" ");
            }
        }
        Log.i(TAG, builder.toString());
        return builder.toString().split(" ");
    }


    private static String pinyin2Symbol(String[] pinyins) {
        StringBuilder result = new StringBuilder();
        result.append("sil");
        boolean addEnd = true;
        for (String pinyin : pinyins) {
            if (pinyin.isEmpty()) {
                continue;
            }
            String[] split = PINYIN_DICT.get(pinyin.substring(0, pinyin.length() - 1));
            if (split != null && split.length >= 2) {
                result.append(" ")
                        .append(split[0])
                        .append(" ")
                        .append(split[1])
                        .append(pinyin.substring(pinyin.length() - 1))
                        .append(" ");
                addEnd = true;
            } else if (pinyin.startsWith("#")) {
                result.delete(result.length() - 2, result.length());
                result.append(pinyin);
                addEnd = false;
            } else {
                result.append(" ")
                        .append(pinyin)
                        .append(" ");
                addEnd = true;
            }

            if (addEnd)
                result.append("#0");
        }
        result.append(" eos");
        return result.toString();
    }

    private static int[] symbol2ids(String[] symbols) {
        int[] ids = new int[symbols.length];
        for (int i = 0; i < symbols.length; i++) {
            if (SYMBOL_TO_ID.get(symbols[i]) != null) {
                ids[i] = SYMBOL_TO_ID.get(symbols[i]);
            } else {
                ids[i] = 2;
            }
        }
        return ids;
    }

    private static String number2Hanzi(String number) {
        StringBuilder builder = new StringBuilder();
        char[] chars = number.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            builder.append(NUMBER_2_HAN_ZI.get(String.valueOf(chars[i])));
        }
        return builder.toString();
    }

//    public static void main(String[] args) {
//        System.out.println("text去除标点符号结果:");
//    }
}
