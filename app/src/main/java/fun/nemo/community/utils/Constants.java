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
            "            var objs = document.getElementsByTagName(\"img\");\n" +
            "            var array=new Array();\n" +
            "            for(var i=0;i<objs.length;i++)  {\n" +
            "                objs[i].onclick=function()\n" +
            "                {\n" +
            "                 if(array.length == 0){\n" +
            "                  var index=0;\n" +
            "                   for(var j=0;j<objs.length;j++){\n" +
            "                       if(objs[j].parentNode.nodeName ==\"P\"){\n" +
            "                            var isAvatar = false;\n" +
            "                            window.imagelistener.log(\"有属性：\"+objs[j].class);\n" +
            "                            array[index]=objs[j].src;\n" +
            "                            index++;\n" +
            "                       }\n" +
            "\n" +
            "                    }\n" +
            "                 }\n" +
            "                  if(this.parentNode.nodeName==\"P\"){\n" +
            "                    window.imagelistener.openImage(this.src,array);\n" +
            "                 }\n" +
            "                }\n" +
            "           }\n" +
            "            })()";
}
