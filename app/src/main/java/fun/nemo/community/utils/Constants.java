package fun.nemo.community.utils;

/**
 * @description:
 * @author: 刘宇飞
 * @date :  2021/4/14$ 9:45$
 */
public class Constants {

    public static String[] WHITE_URL =
            {
                    "https://www.link-nemo.com/u/",
                    "https://www.link-nemo.com/my/article/category",
                    "https://www.link-nemo.com/personal/setting",
                    "https://www.link-nemo.com/moderator/dashboard",
                    "https://www.link-nemo.com/note"
            };
    public static String HOST_URL = "https://www.link-nemo.com/";

    public static String INJECTION_JS = "javascript:(function(){" +
            "var objs = document.getElementsByTagName(\"img\"); " +
            " var array=new Array(); " +
            " for(var j=0;j<objs.length;j++){ array[j]=objs[j].src; }" +
            "for(var i=0;i<objs.length;i++)  " +
            "{"
            + "    objs[i].onclick=function()  " +
            "    {  "
            + "        window.imagelistener.openImage(this.src,array);  " +
            "    }  " +
            "}" +
            "})()";
}
