javascript:(function(){
            var objs = document.getElementsByTagName("img");
            var array=new Array();
            for(var i=0;i<objs.length;i++)  {
                objs[i].onclick=function()
                {
                 if(array.length == 0){
                  var index=0;
                   for(var j=0;j<objs.length;j++){
                       if(objs[j].parentNode.nodeName.toString()=="P"){
                            array[index]=objs[j].src;
                            index++;
                       }

                    }
                 }
                  if(this.parentNode.nodeName=="P"){
                    window.imagelistener.openImage(this.src,array);
                 }
                }
           }
            })()