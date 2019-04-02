$(function() {
    getRecord();

	setInterval("getRecord()", 60000);
});
function getRecord() {
    var pagedata = {"start": startNum, "lenght": pageDataCount};
    getdata(pagedata);
}
function getdata(pagedata) {
    $.ajax({
        type: 'POST',
        url: clickmedBaseUrl + "/noticenter/findTodos",
        data: pagedata,
        dataType: "json",
        error: function() {
            $("#todolist").html("<span style=\"font-size:16px; display:block;text-align:center; line-height:345px;\">获取通知中心失败</span>");
        },
        success: function(data) {
            if (data != null && data.length != 0) {
                var todohtml = "";
                /*totleCount = data[0].totleNum;
                listNum = data.length;
                if (totleCount > 0) {
                    $("#hm_head").text(totleCount);
                }*/
                var tempTitle ="";
                for (var i = 0; i < data.length; i++) {
                    var Top = Math.floor(data[i].todoTheme.length / 28) * 0;
                    tempTitle =data[i].todoTheme;
                    if (data[i].todoTheme.length >= 24) {

                    data[i].todoTheme = data[i].todoTheme.substr(0, 1204)
                    }
                    todohtml = todohtml
                            + "<li>"
                            + "<div class='reportTitles'  title= '"+tempTitle+"'  onclick='tothis(" + JSON.stringify(data[i]) + ")'>"
                            + "&nbsp;【" + data[i].businessTypeText + "】"
                            + "" + data[i].todoTheme + ""
                            + "<span style=top:" + Top + "px;>" + data[i].time + "</span>"

                            + "</div>"
                            + "</li>"
                }
                
               /* if (totleCount >= pagePer) {
                    if (pageDataCount >= totleCount) {
                        todohtml = todohtml + " <div class=\"hm_over\" style=\"margin-top:2px\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;没有更多了</div>"
                    }
                    else {
                        todohtml = todohtml + "<div class=\"hm_loading\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;加载中...</div>"
                    }
                }*/
            } else {
                $("#hm_head").text("0");
                todohtml = "<span style=\"font-size:16px; display:block;text-align:center; line-height:145px;\">无消息通知</span>"
            }
            $("#todolist").html(todohtml);
        }
    });
}
/** 点击待办任务后跳转至应用 */
function tothis(data) {
    window.parent.HROS.window.createTemp({
        appid: 274,
        title: data.businessTypeText,
        url: clickmedBaseUrl + data.todoURL+"&id="+data.businessId+"&fromId="+data.id+"&cardIds="+data.createCardIds+"&canGoBack=canGoBack",
        width: 1200,
        height: 650,
        isflash: false,
        isresize: 1,
        isopenmax: true
    })
}


/** 点击待办任务后跳转至应用 */
function todbsx() {
    window.parent.HROS.window.createTemp({
        appid: '277',
        title: '通知中心',
        url: clickmedBaseUrl + '/view/notice/self_notice_list.html',
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
 