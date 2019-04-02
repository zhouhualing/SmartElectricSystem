$("#left").width(leftWidth);
$("#openClose").click(function(){
    if($(this).hasClass("close")){
        $(this).removeClass("close");
        $(this).addClass("open");
        $("#left").animate({width:0,opacity:"hide"});
        $("#right").animate({width:$("#content").width()-$("#openClose").width()-5})
    }else{
        $(this).addClass("close");
        $(this).removeClass("open");
        $("#left").animate({width:leftWidth,opacity:"show"});
        $("#right").animate({width:$("#content").width()-$("#openClose").width()-leftWidth-9})
    }});
    if(!Array.prototype.map){
        Array.prototype.map=function(e,d){
            var a=[],b=0;
            for(var c=0,f=this.length;c<f;c++){
                if(c in this){
                    a[b++]=e.call(d,this[c],c,this)
                }
            }
            return a
        }
    }

    var getWindowSize=function(){
        return["Height","Width"].map(function(a){
            return window["inner"+a]||document.compatMode==="CSS1Compat"&&document.documentElement["client"+a]||document.body["client"+a]})
    };$(window).resize(function(){wSize()});wSize();