var id = getURLParam("id"); //当前收文id
var fromPage = getURLParam("fromPage"); //页面来源
var type = getURLParam("type"); //页面来源
var taskId = getURLParam("taskId");  
var mark = getURLParam("mark");//当前是否是市长秘书点击进来的 002:是 
var btnSource = "app";
$("#id").val(id);
$("#businessKey").val(id);
$("#reqRepType").val(type);
$("#qsy a").tab("show");
var appFlag = getURLParam("appFlag"); 
if(fromPage == 'pc' && appFlag != 1){
	btnSource = fromPage;
}
var realRoleCode = null;
var officeflag = true;
var officeUrl = "";

$(function () {
    'use strict';
    /*$("body").hide();*/
    // Initialize the jQuery File Upload widget:
    $('#dataInputForm').fileupload({
        disableImageResize: false,
        // Uncomment the following to send cross-domain cookies:
        //xhrFields: {withCredentials: true},
        url: '/cmcp/attachment/upload'
    });

    // Enable iframe cross-domain access via redirect option:
    $('#dataInputForm').fileupload('option', {
        // Enable image resizing, except for Android and Opera,
        // which actually support image resizing, but fail to
        // send Blob objects via XHR requests:
        disableImageResize: /Android(?!.*Chrome)|Opera/
            .test(window.navigator.userAgent),
            maxFileSize: attachment_maxSize,
            acceptFileTypes:attachment_regixType
    });
    
});
$("#textPage").click(function(){
	if(officeflag){
		$("#editmain_left").append('<iframe name="weboffice" src="../weboffice/webpdf.html?officeUrl=' +officeUrl+'"style="width:100%;height:800px;"></iframe>');
		officeflag = false;
	}
});
$(function(){
	//获取当前登陆人信息
	getCurrenUserInfo(function(data){
		$("#userCode").val(data.userCode);
		$("#userOrg").val(data.orgName);
		$("#userName").val(data.userName);
		realRoleCode = data.realRoleCode;
	});
	if(id != null) {
		$("#id").val(id);
		var dto = {
				id:id
		}
		doJsonRequest("/reqrep/getReqRep",dto,function(data){
			if(data.result) {
				var data = data.data;
				if(null!=data.files[0]){
					officeUrl = data.files[0].pdfUrl;
					$('#dataInputForm').fileupload('option', 'done').call($('#dataInputForm'), $.Event('done'), {result: data});
					$(".name","#attachmentDiv").children("a").attr("onclick","showPDF(this)");
				}
				$("#remark").val(data.remark);
				$("#reqRepText").val(data.reqRepText);
				
				/**
				 * 内容高度自适应。
				 */
				var height = $("#reqRepText")[0].scrollHeight + "px";
				$("#reqRepText").css('height',height);
				
				$("#reqRepType").val(data.reqRepType);
				$("#reqRepCode").val(data.reqRepCode);
				$("#reqRepTitle").val(data.reqRepTitle);
				$("#docCameOrgan").val(data.docCameOrgan);
				$("#drafterPhone").val(data.drafterPhone);
				$("#drafterName").val(data.drafterName);
				if(data.reqRepDate!=''&&data.reqRepDate!=null){
					$("#reqRepDate").html(new Date(data.reqRepDate).format('yyyy年MM月dd日'));
				}
				//领导审核
				$("#fgldOpinion").val(data.fmszOpinion);
				$("#mszOpinion").val(data.mszOpinion);
				$("#fszOpinion").val(data.fszOpinion);
				$("#szOpinion").val(data.szOpinion);
				var ldpsAudit = "";
				var mszAudit = "";
				var bmldAudit = "";
				if(data.szOpinion!='' && data.szOpinion!=null){
					ldpsAudit += data.szOpinion+ '<br>';
				}
				if(data.fszOpinion!='' && data.fszOpinion!=null){
					ldpsAudit += data.fszOpinion+ '<br>';				
				}
				if(data.mszOpinion!='' && data.mszOpinion!=null){
					mszAudit += data.mszOpinion+ '<br>';
				}
				if(data.fgldOpinion!='' && data.fgldOpinion!=null){
					bmldAudit += data.fgldOpinion+ '<br>';
				}
				$("#ldpsAudit").html(ldpsAudit);
				$("#mszAudit").html(mszAudit);
				$("#bmldAudit").html(bmldAudit);
				$("#isReturn").val(data.isReturn);
				$("#lPageSpan").html(data.littlePage);
			} else {
				$.alert("获取信息失败。");
			}
			
			//初始化流程按钮
			var dto = {
				taskId:taskId	
			}
			wf_getOperator(dto,function(data){
				userTask = data.userTask;//获取当前流程
				
				if(userTask!='usertask4' && userTask!='usertask5'){
					$("#buttonList").show();
				}
				
				//根据当前步骤控制页面可输入项:
				
				/*if(userTask == 'usertask1' && $("#isReturn").val()==1){
					$("#buttonList span").hide();
					$("#toVoid").show();
				}*/
				if(userTask == 'usertask2' || userTask == 'usertask3'){
					//秘书长
					$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
					$("#mszps").removeAttr("readonly");
				}
				
				if(userTask == 'usertask4'){
					if(realRoleCode!=null && realRoleCode!='null' && realRoleCode!=''){
						$("#buttonList").show();
						$("#ldps").removeAttr("readonly");
						$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
					}else{
						$("#ldps").attr('readonly',true);
						$("#buttonList").empty().show().append('<span style="line-height:35px;color:blue;font-weight:bold;">领导文件</span>');
					}
				}
				
				//领导审批：
				if(userTask == 'usertask5'){
//					$("#littlePage").show();
					/*if(mark=='002' && $("#userCode").val()!='lijm'){
//						$("#buttonList").append("<button type='button'  class='btn btn_click'  onclick='saveLittlePage()' style='margin-left:8px;' >保存便签</button>");
						$("#ldps").attr('readonly',true);
						$("#buttonList span").hide();
					}else{
						$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
						$("#ldps").removeAttr("readonly");
					}*/
//					$("#liPage").removeAttr("readonly");
					
					if(realRoleCode!=null && realRoleCode!='null' && realRoleCode!=''){
						$("#buttonList").show();
						$("#ldps").removeAttr("readonly");
						$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
					}else{
						$("#ldps").attr('readonly',true);
						$("#buttonList").empty().show().append('<span style="line-height:35px;color:blue;font-weight:bold;">领导文件</span>');
					}
					
				}
				if(userTask=='usertask6'){
					/*if(mark=='002' && $("#userCode").val()!='lijm'){//分发给市长，市长秘书进入时隐藏确认收文按钮
						$("#beSended").hide();
					}*/
					
					//分发给市长，市长秘书进入时隐藏确认收文按钮
					if((mark=='002' && realRoleCode!='002') || (mark=='048' && realRoleCode!='048') || (mark=='051' && realRoleCode!='051') || (mark=='049' && realRoleCode!='049') || (mark=='003' && realRoleCode!='003') || (mark=='050' && realRoleCode!='050') || (mark=='039' && realRoleCode!='039') || (mark=='043' && realRoleCode!='043')){
						$("#buttonList").empty().show().append('<span style="line-height:35px;color:blue;font-weight:bold;">领导文件</span>');
					}
				}
				if($("#ldps").attr('readonly')=="readonly"){
					$("#ldps").remove();
				}
				if($("#bmldps").attr('readonly')=="readonly"){
					$("#bmldps").remove();
				}
				if($("#mszps").attr('readonly')=="readonly"){
					$("#mszps").remove();
				}
			});
		});
	}
	doQueryWF("reportInfo","approvalDiv");
});
function showPDF(_this){
//	$("#mailFrame").attr("src",$(_this).attr("theUrl"))
	 weboffice.window.openFileFromUrl($(_this).attr("theUrl"));
}

function saveLittlePage(){
	var lps = $("#liPage").val();
	if(lps!=''){
		lps = lps + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]";
		if($("#lPageSpan").html()!=''){
			if($("#userCode").val() == 'lijm'){
				lps = lps + '<br/>' + $("#lPageSpan").html();
			}else{
				lps = $("#lPageSpan").html() + '<br/>' + lps;
			}
		}
	}else{
		$.alert('请输入意见。');
		return false;
	}
	var dto = { id:id,littlePage:lps};
	doJsonRequest("/reqrep/addLittlePage",dto,function(data){
             $("#lPageSpan").html(dto.littlePage);
            $("#liPage").val("");
            $.alert("保存成功");
            return;
		window.location.href="../tododocs/docs_down.html?fromPage=0002&flag=2&operaterId="+data.operaterId+"&btnSource="+btnSource+"&mark="+mark;
	},{showWaiting:true});
}

function getAddData(){
	
	var fgldOpinion = $("#fgldOpinion").val();
	var mszOpinion = $("#mszOpinion").val();
	var fszOpinion = $("#fszOpinion").val();
	var szOpinion = $("#szOpinion").val();
	var ldps = $("#ldps").val();
	var mszps = $("#mszps").val();
	var bmldps = $("#bmldps").val();
	if(ldps!=''){
		ldps = ldps + "  [" + $("#userName").val()+"  "+new Date().format('yyyy-MM-dd hh:mm') + "]";
		if(userTask == 'usertask4'){
			//如果当前环节为副市长审批
			if(fszOpinion!=''){
				fszOpinion = ldps + '<br>' + fszOpinion;
			}else{
				fszOpinion = ldps;
			}
		}else if(userTask == 'usertask5'){
			//如果当前环节为市长审批
			if(szOpinion!=''){
				szOpinion = ldps + '<br>' + szOpinion;
			}else{
				szOpinion = ldps;
			}
		}
	}
	if(mszps!='' && userTask == 'usertask3'){
		mszps = mszps + "  [" + $("#userName").val()+"  "+new Date().format('yyyy-MM-dd hh:mm') + "]";
		//如果当前环节为秘书长审批
		if(mszOpinion!=''){
			mszOpinion = mszps + '<br>' + mszOpinion;
		}else{
			mszOpinion = mszps;
		}
	}
	if(mszps!='' && userTask == 'usertask2'){
		mszps = mszps + "  [" + $("#userName").val()+"  "+new Date().format('yyyy-MM-dd hh:mm') + "]";
		//如果当前环节为分管领导审批
		if(mszOpinion!=''){
			mszOpinion = mszOpinion + '<br>' + mszps;
		}else{
			mszOpinion = mszps;
		}
	}
	var dto = {};
	if(userTask == 'usertask1'){
		//承办人
		dto = {
			id:$("#id").val(),
			flag:1,//当前为审批流程.
			docCameOrgan:$("#docCameOrgan").val(),
			drafterPhone:$("#drafterPhone").val(),
			drafterName:$("#drafterName").val(),
			reqRepTitle:$("#reqRepTitle").val(),
			remark:$("#remark").val(),
			reqRepText:$("#reqRepText").val(),
			isReturn:0
		};
	}else if(userTask == 'usertask2' || userTask == 'usertask3' || userTask == 'usertask4' ||  userTask == 'usertask5'){
		//秘书长审核
		dto = {
			id:$("#id").val(),
			mszOpinion : mszOpinion,
			fszOpinion : fszOpinion,
			szOpinion : szOpinion,
			littlePage:getLittileMsg(),
			flag:1,//当前为审批流程.
			isReturn:0
		};
	}
	return dto;
}

function getLittileMsg(){
	var lps = $("#liPage").val();
	if(lps!=''){
		lps = lps + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]";
		if($("#lPageSpan").html()!=''){
			if($("#userCode").val() == 'lijm'){
				lps = lps + '<br/>' + $("#lPageSpan").html();
			}else{
				lps = $("#lPageSpan").html() + '<br/>' + lps;
			}
		}
		return lps;
	}else{
		return $("#lPageSpan").html();
	}
}

var btnSource = "app";
var appFlag = getURLParam("appFlag"); 
if(fromPage == 'pc' && appFlag != 1){
	btnSource = fromPage;
}
function goSuccess(data) {
	var roleName = data.assignRoleName;
	window.location.href="../tododocs/docs_down.html?roleName="+roleName+"&fromPage=0002&btnSource="+btnSource+"&mark="+mark;
}
//分发
function goSuccessSend() {
	window.location.href="../tododocs/docs_down.html?fromPage=0002&flag=0&btnSource="+btnSource+"&mark="+mark;
}
//办结
function goSuccessEnd() {
	window.location.href="../tododocs/docs_down.html?fromPage=0002&flag=1&btnSource="+btnSource+"&mark="+mark;
}
function goVoidEnd(){
	window.location.href="../tododocs/docs_down.html?fromPage=0002&flag=3&btnSource="+btnSource+"&mark="+mark;
}

function wf_getDate(data) {
	return getTheDate(172800);
}

//校验
function wf_beforeValid(){
	 if(userTask == 'usertask4' ||  userTask == 'usertask5'){
		 if($("#ldps").val()==''){
			$.alert("请输入审批意见。"); 
			return false;
		 }else{
                     window.scrollTo(0,0);
			 return true;
		 }
	 }else if(userTask == 'usertask2' || userTask == 'usertask3'){
             if($("#mszps").val().length>100||$("#mszps").val().split("\n").length>=3){
                 $.alert("签批字数太多");
                 return false;
             }
		 if($("#mszps").val()==''){
			$.alert("请输入审批意见。"); 
			return false;
		 }else{
                     window.scrollTo(0,0);
			 return true;
		 }
	 }else{
            window.scrollTo(0,0);
		 return true;
	 }
	
}

function wf_getMark(operaterId){
	if(userTask == 'usertask4' ||  userTask == 'usertask5'){
		return $("#ldps").val();
	 }else if(userTask == 'usertask2' || userTask == 'usertask3'){
		return $("#mszps").val();
	 }else if(userTask == 'usertask6'){
		return "收到";
	 }
}


//----------常用语（开始）------------
function commonLanguage(){
	var obj = {
		    title:'选择常用语',
		    height:"350px",
		    width:"650px",
		    url:'../users/user_common_language.html?userCode='+$("#userCode").val(),
		    myCallBack:"addCommonLanguage"
		};
		new jqueryDialog(obj);
}

function addCommonLanguage(data){
	var language = "";
	$.each(data,function(){
		language += this.language+" ";
	});
	
	var thisText = "";
	if(userTask == 'usertask2' || userTask == 'usertask3'){
		thisText = $("#mszps").val();
		$("#mszps").val(thisText+language);
		$("#mszps")[0].focus();
	}else if(userTask == 'usertask4' || userTask == 'usertask5'){
		thisText = $("#ldps").val();
		$("#ldps").val(thisText+language);
		$("#ldps")[0].focus();
	}
}
//----------常用语（结束）------------
$(function(){
    $("#mszps").on("blur",function(){
        var curRow = $(this).val().split("\n").length;
        if(curRow>=3||$(this).val().length>100){
            $.alert("签批字数太多");
        }
    });
});
var Tab1 =function(){
    $("#tab1,#tab2,#tab4").css({
        "display":"none",
        "height":"0px"
    });
    $("#tab1").css({
        "display":"block",
        "height":"auto"
    })
};
var Tab2 =function(){
    $("#tab1,#tab2,#tab4").css({
        "display":"none",
        "height":"0px"
    });
    $("#tab2").css({
        "display":"block",
        "height":"auto"
    })
};
var Tab4 =function(){
    $("#tab1,#tab2,#tab4").css({
        "display":"none",
        "height":"0px"
    });
    $("#tab4").css({
        "display":"block",
        "height":"auto"
    })
};