javascript:(function(){
               var classNames = ["blog-article-content","bplist","post"];
               var array=new Array();
               var index=0;
               for(var clsIdx=0;clsIdx < classNames.length; clsIdx ++){
                   var clsName = classNames[clsIdx];
                   var boxs = document.getElementsByClassName(clsName);
                   if(!!boxs){
                       for(var boxIdx=0;boxIdx < boxs.length; boxIdx ++){
                           var box = boxs[boxIdx];
                           var objs = box.getElementsByTagName("img");
                           console.log(objs);
                           if(!!objs){
                              for(var i=0;i<objs.length;i++)  {
                                    objs[i].onclick=function(){
                                       if(array.length == 0){
                                            for(var j=0;j<objs.length;j++)  {
                                                 if(objs[j].src.search("common_lodding.gif") == -1){
                                                     array[index]=objs[j].src;
                                                     index++;
                                                 }
                                            }
                                       }
                                      window.imagelistener.openImage(this.src,array);
                                    }
                              }
                           }
                       }
                   }
               }})()