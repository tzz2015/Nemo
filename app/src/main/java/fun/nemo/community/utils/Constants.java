package fun.nemo.community.utils;

/**
 * @description:
 * @author: 刘宇飞
 * @date :  2021/4/14$ 9:45$
 */
public class Constants {
    public static String ARTICLE_URL = "https://www.link-nemo.com/u/";
    public static String[] WHITE_URL =
            {
                    ARTICLE_URL,
                    "https://www.link-nemo.com/my/article/category",
                    "https://www.link-nemo.com/personal/setting",
                    "https://www.link-nemo.com/moderator/dashboard",
                    "https://www.link-nemo.com/note"
            };
    public static String HOST_URL = "https://www.link-nemo.com/";

    public static String INJECTION_JS = "javascript:(function(){\n" +
            "               var classNames = [\"blog-article-content\",\"bplist\",\"post\"];\n" +
            "               var array=new Array();\n" +
            "               var index=0;\n" +
            "               for(var clsIdx=0;clsIdx < classNames.length; clsIdx ++){\n" +
            "                   var clsName = classNames[clsIdx];\n" +
            "                   var boxs = document.getElementsByClassName(clsName);\n" +
            "                   if(!!boxs){\n" +
            "                       for(var boxIdx=0;boxIdx < boxs.length; boxIdx ++){\n" +
            "                           var box = boxs[boxIdx];\n" +
            "                           var objs = box.getElementsByTagName(\"img\");\n" +
            "                           console.log(objs);\n" +
            "                           if(!!objs){\n" +
            "                              for(var i=0;i<objs.length;i++)  {\n" +
            "                                    objs[i].onclick=function(){\n" +
            "                                       if(array.length == 0){\n" +
            "                                            for(var j=0;j<objs.length;j++)  {\n" +
            "                                                 if(objs[j].src.search(\"common_lodding.gif\") == -1){\n" +
            "                                                     array[index]=objs[j].src;\n" +
            "                                                     index++;\n" +
            "                                                 }\n" +
            "                                            }\n" +
            "                                       }\n" +
            "                                      window.imagelistener.openImage(this.src,array);\n" +
            "                                    }\n" +
            "                              }\n" +
            "                           }\n" +
            "                       }\n" +
            "                   }\n" +
            "               }})()";
}
