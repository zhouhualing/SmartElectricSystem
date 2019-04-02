$(function() {
    getRecord();
    var ws = new CommonWebSocket({
        url: "/todoListener",
        afterGetMessageFun: function() {
        	setTimeout(function(){
        		 getRecord();
        	},3000)
           
        }
    });

//	setInterval("getRecord()", 5000);
});
function getRecord() {
    var pagedata = {"start": startNum, "lenght": pageDataCount};
    getdata(pagedata);
}
function getdata(pagedata) {
    $.ajax({
        type: 'POST',
        url: clickmedBaseUrl + "/todo/findTodosWithPage",
        data: pagedata,
        dataType: "json",
        error: function() {
            $("#todolist").html("<span style=\"font-size:16px; display:block;text-align:center; line-height:345px;\">获取待办任务失败</span>");
        },
        success: function(data) {
            if (data != null && data.length != 0) {
                var todohtml = "";
                totleCount = data[0].totleNum;
                listNum = data.length;
                if (totleCount > 0) {
                    $("#hm_head").text(totleCount);
                }
//				else 
//				    {$("#hm_head").text("待办任务列表");}
//				totlePage = Math.ceil(totleCount / pageDataCount);
                var  tempTitle ="";
                for (var i = 0; i < data.length; i++) {
                    var colorStr = "";
                    if (data[i].theFlag == 0) {
                    	
                    }
                    if (data[i].theFlag == 1) {
                    	colorStr = "color:#be3030"
                    }
                    if(data[i].title) {
                        tempTitle =data[i].title;
                    	 var Top = Math.floor(data[i].title.length / 28) * 0;
                         if (data[i].title.length >= 24) {
                         	data[i].title = data[i].title.substr(0, 1004)
                         }
                    }
                   
                  
                    todohtml = todohtml
                            + "<li>"
                            + "<div class='reportTitles' title= '"+tempTitle+"' onclick='tothis(" + JSON.stringify(data[i]) + ")' style=" + colorStr + ">"
                            + "&nbsp;" + data[i].num + "&nbsp;"
                            + "【" + data[i].appName + "】"
                            + "" + data[i].title + ""
                            + "<span style=top:" + Top + "px;" + colorStr + ">" + data[i].time + "</span>"

                            + "</div>"
                            + "</li>"
                }
                if (totleCount >= pagePer) {
                    if (pageDataCount >= totleCount) {
                    	todohtml = todohtml + "<div class=\"hm_over\" style=\"margin-top:2px;margin-left:40%;margin-right:40%;\">没有更多了</div>"
//                      todohtml = todohtml + "<div class=\"hm_over\" style=\"margin-top:2px\"></div>"
//                    	todohtml = todohtml + "<a style=\"margin-top:5px;margin-left:40%;margin-right:40%;color:#fff;\" href=\"javascript:;\" onclick=\"todbsx()\">点击查看更多</a>"
                    }
                    else {
                        todohtml = todohtml + "<div class=\"hm_loading\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;加载中...</div>"
                    }
                }
            } else {
                $("#hm_head").text("0");
                todohtml = "<span style=\"font-size:16px; display:block;text-align:center; line-height:145px;\">无待办任务</span>"
            }
            $("#todolist").html(todohtml);
//			if(totleCount>5) {
//				var other ="<p>当前第<span>"+currentPageNum+"</span>页，总共<span>"+totlePage+"</span>页</p>";
//				$("#current_pages").html(other);
//			    createPage();
//			}
        }
    });
}
/** 点击待办任务后跳转至应用 */
function tothis(data) {
    window.parent.HROS.window.createTemp({
        appid: data.appId,
        title: data.appName,
        url: clickmedBaseUrl + data.url + "&businessType=" + data.businessType+"&canGoBack=canGoBack",
        width: Number(data.width),
        height: Number(data.height),
        isflash: false,
        isresize: Number(data.isresize),
        isopenmax: true
    })
}


/** 点击待办任务后跳转至应用 */
function todbsx() {
    window.parent.HROS.window.createTemp({
        appid: '269',
        title: '待办事项',
        url: clickmedBaseUrl + '/view/workflow/self_todo_list.html',
        width: Number('1200'),
        height: Number('650'),
        isflash: false,
        isresize: Number('1'),
        isopenmax: true
    })
}
/** 设置分页标签 */
//function createPage() {
//	$("#video_pages").dlx({
//		count : totleCount, // 数据的总数
//		data : pageDataCount,// 每页显示几条数据
//		start : currentPageNum, // 当前展示页
//		display : pageBtnCount,// 显示多少页面按钮
//		onChange : function(e) { // 点击页面按钮触发事件
//			currentPageNum = e;
//			startNum = (e - 1) * 5 + 1;
//			getRecord();
//		}
//	});
//}
//
//$("#scrollDiv").scroll(function() {
//      //获取网页高度
//	  var hght= 68*listNum;
//	  //获取浏览器高度(默认385)
//	  var clientHeight =385;
//      //获取网页滚过的高度
//	  var top= window.pageYOffset||(document.compatMode == 'CSS1Compat' ?document.documentElement.scrollTop : document.body.scrollTop);
//      //当 top+clientHeight = hght的时候就说明到底儿了
//	  if(top>=(parseInt(hght)-clientHeight)){
//		  pageDataCount= pageDataCount+pagePer;
//		  if(pageDataCount>totleCount){
//			  pageDataCount = totleCount;
//		  }
//	      getRecord();
//	  }
//  });

$("#scrollDiv").scroll(function() {
    if ($("#scrollDiv").scrollTop() > 30) {
        pageDataCount = pageDataCount + pagePer;
        if (pageDataCount > totleCount) {
            pageDataCount = totleCount;
        }
        getRecord();
    }
})
 