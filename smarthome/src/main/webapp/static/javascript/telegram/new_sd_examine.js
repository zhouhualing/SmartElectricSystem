$(".fileupload-buttons").hide();
var id = getURLParam("id"); //当前收文id
var fromPage = getURLParam("fromPage"); //页面来源
var type = getURLParam("type"); //页面来源
var taskId = getURLParam("taskId");  
var mark = getURLParam("mark");//当前是否是市长秘书点击进来的 002:是 
var localText = "";
var lcbz = "";
var officeflag = true;
$("#id").val(id);
$("#businessKey").val(id);
$("#reportType").val(type);
$("#qsy a").tab("show");
var roleCode = "";
var roleName = "";
var businessType = getURLParam("businessType");
var printBtnFlag;
var userTask = '';
var realRoleCode = null;

$(function () {
    'use strict';
    $("body").hide();
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
$(function(){
	//获取当前登陆人信息
	getCurrenUserInfo(function(data){
		$("#userCode").val(data.userCode);
		$("#userOrg").val(data.orgName);
		$("#userName").val(data.userName);
		$("#phoneNumber").val(data.phoneNumber);
        roleCode = data.roleCodes;
        roleName = data.roleNames;
        isMayor = data.isMayor;
        realRoleCode = data.realRoleCode;
	});
	if(id != null) {
		$("#id").val(id);
		var dto = {
				id:id
		}
		doJsonRequest("/receivedoc/getReceiveDoc",dto,function(data){
                 
			if(data.result) {
				var data = data.data;
                                   window.Data =data;
                              
				if(null!=data.files[0]){
					//if(data.files[0].pdfUrl.endWith(".pdf")){
						localText = data.files[0].pdfUrl;
						//$("#mailFrame").attr("src",data.files[0].pdfUrl);
					//}
					 $('#dataInputForm').fileupload('option', 'done').call($('#dataInputForm'), $.Event('done'), {result: data});
					 $(".name","#attachmentDiv").children("a").attr("onclick","showPDF(this)")
				}
				//来文单位
				$("#docCameOrgan").val(data.docCameOrgan);
				//文号
				$("#receiveCode").val(data.receiveCode);
				//紧急程度
                $("#urgency").val(data.urgency);
				//文件标题
				$("#receiveTitle").val(data.receiveTitle);
				//秘书审核
				$("#fmszOpinion").val(data.fmszOpinion);
				$("#mszOpinion").val(data.mszOpinion);
				
				var mszpsAudit = "";
				if(data.mszOpinion!='' && data.mszOpinion!=null){
					mszpsAudit += data.mszOpinion + '<br>';
				}
				if(data.fmszOpinion!='' && data.fmszOpinion!=null){
					mszpsAudit += data.fmszOpinion;
				}
				$("#mszpsAudit").html(mszpsAudit);
				//市长审核
				$("#fszOpinion").val(data.fszOpinion);
				$("#szOpinion").val(data.szOpinion);
				var ldpsAudit = "";
				if(data.szOpinion!='' && data.szOpinion!=null){
					ldpsAudit += data.szOpinion + '<br>';
				}
				if(data.fszOpinion!='' && data.fszOpinion!=null){
					ldpsAudit += data.fszOpinion;				
				}
				$("#ldpsAudit").html(ldpsAudit);

				var leadingAudit = "";
                            
				if(data.szOpinion ){
					leadingAudit += data.szOpinion + '<br>';
				}
				if(data.fszOpinion){
					leadingAudit += data.fszOpinion + '<br>';				
				}
				if(data.mszOpinion){
					leadingAudit += data.mszOpinion + '<br>';
				}
				if(data.fmszOpinion ){
					leadingAudit += data.fmszOpinion;
				}
				$("#leadingAudit").html(leadingAudit);
				
				//呈批意见
				$("#hgOpinion").val(data.hgOpinion);
				$("#hgOp").val(data.hgOpinion);
				/*$("#wskOpinion").html(data.wskOpinion);*/
				
				/*var nbyjAudit = "";
				if(data.hgOpinion!='' && data.hgOpinion!=null){
					nbyjAudit += data.hgOpinion + '<br>';
				}*/
				/*if(data.wskOpinion!='' && data.wskOpinion!=null){
				//	nbyjAudit += data.wskOpinion + '<br>';				
				}*/
				/*$("#nbyjAudit").html(data.hgOpinion);*/
				//备注
				//正文
				$("#text").val(data.text);
				$("#drafterName").html(data.drafterName);
				$("#drafterPhoneNum").html(data.drafterPhoneNum);
				if(data.receiveDate!=''&&data.receiveDate!=null){
					$("#receiveDate").val(new Date(data.receiveDate).format('yyyy年M月d日'));
				}
                          
				$("#lwCode").val(data.lwCode);
				$("#upCardFlag").val(data.upCardFlag);
				$("#createUserName").val(data.createUserName);
				//小纸条
				/*$("#liPage").val(data.littlePage);*/
				$("#lPageSpan").html(data.littlePage);
				//当前是否是退回办理人
				$("#isReturn").val(data.isReturn);
				doQueryWF("reportInfo","approvalDiv");
				//初始化流程按钮
				var dto = {
					taskId:taskId	
				}
				wf_getOperator(dto,function(data){
					userTask = data.userTask;//获取当前流程
					
					if(userTask!='usertask5' && userTask!='usertask6'){
						$("#buttonList").show();
					}
					
					//根据当前步骤控制页面可输入项:
					//文书核稿
                     /*if(userTask =='usertask7'){
                            $("#lwCode").removeAttr("disabled");
                     }*/
					if(userTask=='usertask2') {
						$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
						$("#hgOpinion").removeAttr("disabled");
                        if($("#hgOpinion").val()){
                            $("#hgOpinion").attr("placeholder","")
                        }
					}
					
					if(userTask=='usertask4' || userTask=='usertask3'){
						$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
						$("#leaderOpinion").removeAttr("disabled");
					}
					
					if(userTask=='usertask5'){
						if(realRoleCode!=null && realRoleCode!='null' && realRoleCode!=''){
							$("#buttonList").show();
							$("#leaderOpinion").removeAttr("disabled");
							$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
						}else{
							$("#buttonList").empty().show().append('<span style="line-height:35px;color:blue;font-weight:bold;">领导文件</span>');
						}
					}
					
					if(userTask=='usertask6'){
						//市长签批便签
//						$("#littlePage").show();
						/*if(mark=='002' && $("#userCode").val()!='lijm'){
//							$("#buttonList").append("<button type='button'  class='btn btn_click'  onclick='saveLittlePage()' style='margin-left:8px;' >保存便签</button>");
							$("#buttonList span").hide();
						}else{
							$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
							$("#leaderOpinion").removeAttr("disabled");
						}*/
//						$("#liPage").removeAttr("disabled");
						
						if(realRoleCode!=null && realRoleCode!='null' && realRoleCode!=''){
							$("#buttonList").show();
							$("#leaderOpinion").removeAttr("disabled");
							$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
						}else{
							$("#buttonList").empty().show().append('<span style="line-height:35px;color:blue;font-weight:bold;">领导文件</span>');
						}
						
					}
					
					/*if(userTask=="usertask7" && $("#isReturn").val()==1){
						//文书科办理：当退回时，只显示作废按钮
						$("#buttonList span").hide();
						$("#toVoid").show();
					}*/
					if(userTask=="usertask8") {
						/*if(mark=='002' && $("#userCode").val()!='lijm'){//分发给市长，市长秘书进入时隐藏确认收文按钮
							$("#beSended").hide();
						}*/
						//分发给市长，市长秘书进入时隐藏确认收文按钮
						if((mark=='002' && realRoleCode!='002') || (mark=='048' && realRoleCode!='048') || (mark=='051' && realRoleCode!='051') || (mark=='049' && realRoleCode!='049') || (mark=='003' && realRoleCode!='003') || (mark=='050' && realRoleCode!='050') || (mark=='039' && realRoleCode!='039') || (mark=='043' && realRoleCode!='043')){
							$("#buttonList").empty().show().append('<span style="line-height:35px;color:blue;font-weight:bold;">领导文件</span>');
						}
						//$(".nav-tabs").find("li").hide();
						printBtnFlag = 1;
						$("#printBtn").hide();
						//$("#textPage").find("a").trigger("click")
						//$("#editmain_left").attr("style","margin-top:0px")
						  $("body").show();
					} else {
						 $("body").show();
					}
					
					/*if($("#nbyj").attr("disabled") == "disabled"){
						$("#nbyj").hide();
					}*/
					if($("#leaderOpinion").attr("disabled") == "disabled"){
						$("#leaderOpinion").remove();
					}
				});
			} else {
				$.alert("获取信息失败。");
			}
		});
	}
});

$("#textPage").click(function(){
	if(officeflag){
		$("#editmain_left").append('<iframe name="weboffice" src="../weboffice/webpdf.html?officeUrl=' + localText+'"style="width:100%;height:800px;"></iframe>');
		officeflag = false;
	}
});

function showPDF(_this){
//	$("#mailFrame").attr("src",$(_this).attr("theUrl"))
	weboffice.window.openFileFromUrl($(_this).attr("theUrl"));
}
function getAddData(){
	//核稿
	var hgOpinion = $("#hgOpinion").val();
	/*var realHgOpinion = hgOpinion.replace(/[ ]/g,"");
	realHgOpinion = realHgOpinion.replace(/(\n)+|(\r\n)+/g,"");
	
	var hgOp = $("#hgOp").val();
	var realHgOp = hgOp.replace(/[ ]/g,"");
	realHgOp = realHgOp.replace(/(\n)+|(\r\n)+/g,"");*/
	if(userTask == 'usertask2'){
		/*alert("当前意见不是空:"+(hgOpinion!=''));
		alert("当前有改过："+(realHgOpinion!=realHgOp));
		alert("当前没有签字："+(hgOpinion.indexOf('[')<=-1 && hgOpinion.indexOf(']')<=-1));
		alert(hgOpinion!='' && (realHgOpinion!=realHgOp || (hgOpinion.indexOf('[')<=-1 && hgOpinion.indexOf(']')<=-1)))
		return false;*/
		/*if(hgOpinion!='' && (realHgOpinion!=realHgOp || (hgOpinion.indexOf('[')<=-1 && hgOpinion.indexOf(']')<=-1))){
			hgOpinion = hgOpinion + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]";
		}*/
		
		if(hgOpinion!=''){
			if(hgOpinion.indexOf('[')>-1 && hgOpinion.indexOf(']')>-1){
				hgOpinion = hgOpinion.split('[')[0] + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]";
			}else{
				hgOpinion = hgOpinion + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]";
			}
		}
	}
       /* if($("#nbyjAudit").html().length)
        hgOpinion +="</br>"+ $("#nbyjAudit").html();*/
	var ldsh = $("#leaderOpinion").val();
	
	var fmszOpinion=window.Data.fmszOpinion||"";
	var mszOpinion=window.Data.mszOpinion||"";
	var fszOpinion=window.Data.fszOpinion||"";
	var szOpinion=window.Data.szOpinion||"";

	if(ldsh!=''){
		ldsh = ldsh + "  [" + $("#userName").val()+"  "+new Date().format('yyyy-M-d hh:mm') + "]";
                if($("#leadingAudit").html().length)
                    ;
            //      ldsh +=  "</br>"+$("#leadingAudit").html();
           
		if(userTask == 'usertask3'){
			//如果当前环节为副秘书长审批
			if(fmszOpinion!=''){
				fmszOpinion = ldsh + "<br>" + fmszOpinion;
			}else{
				fmszOpinion = ldsh;
			}
		}else if(userTask == 'usertask4'){
			//如果当前环节为秘书长审批
			if(mszOpinion!=''){
				mszOpinion = ldsh + '<br>' + mszOpinion;
			}else{
				mszOpinion = ldsh;
			}
		}else if(userTask == 'usertask5'){
			//如果当前环节为副市长审批
			if(fszOpinion!=''){
				fszOpinion = ldsh + '<br>' + fszOpinion;
			}else{
				fszOpinion = ldsh;
			}
		}else if(userTask == 'usertask6'){
			//如果当前环节为市长审批
			if(szOpinion!=''){
				szOpinion = ldsh + '<br>' + szOpinion;
			}else{
				szOpinion = ldsh;
			}
		}
	}
	var dto = {};
	if(userTask == 'usertask7') {
		//办理人环节忽略提交信息
		dto = {
			id:$("#id").val(),
			ignore:true
		}
	}else if(userTask == 'usertask2'){
		//核稿
		dto = {
				id:$("#id").val(),
				//呈批意见
				hgOpinion:hgOpinion,
				//附件
				//attachmentDTOs:attachmentDTOs,
				status:'0002',
				flag:1,//当前为审批流程.
			};
	}else if(userTask == 'usertask3' ||  userTask == 'usertask4'){
		//秘书长审核
		dto = {
				id:$("#id").val(),
				//秘书审核
				fmszOpinion:fmszOpinion,
				mszOpinion:mszOpinion,
				flag:1,//当前为审批流程.
			};
	}else if(userTask == 'usertask5' ||  userTask == 'usertask6'){
		//市长审核
		dto = {
				id:$("#id").val(),
				//市长审核
				fszOpinion:fszOpinion,
				szOpinion:szOpinion,
				littlePage:getLittileMsg(),
				//附件
				//attachmentDTOs:attachmentDTOs,
				flag:1,//当前为审批流程.
			};
	}else{
		dto = {
				id:$("#id").val(),
				flag:1,//当前为审批流程.
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
function goSuccessSend(data) {
	if("flow16" == data.operaterId && isMayor.indexOf(10)!=-1){
		//如果当前为可以创建一事一表的角色，则提示是否创建一事一表
		if(confirm("是否通知责任部门创建一事一表？")){
			var dto = {
					docId:$("#id").val(),
					docType:$("#reportType").val(),
					docTitle:$("#receiveTitle").val(),
					orgName:$("#userOrg").val(),
					dcRoleCode:roleCode,
					dcRoleName:roleName,
					status:"0002"
			}
			doJsonRequest("/actionList/addToBeCreatedList", dto, function(da){
				if(da.result) {
					$.alert("创建成功。");
					window.location.href = "../tododocs/docs_down.html?fromPage=0002&flag=8&btnSource="+btnSource+"&mark="+mark;
				} else {
					$.alert("创建失败。");
					return false;
				}
			},{showWaitting:true})				
		}else{
			window.location.href="../tododocs/docs_down.html?fromPage=0002&flag=0&btnSource="+btnSource+"&mark="+mark;
		}
	}else{
		window.location.href="../tododocs/docs_down.html?fromPage=0002&flag=0&btnSource="+btnSource+"&mark="+mark;
	}
}
//办结
function goSuccessEnd() {
	window.location.href="../tododocs/docs_down.html?fromPage=0002&flag=1&btnSource="+btnSource+"&mark="+mark;
}
function goVoidEnd(){
	window.location.href="../tododocs/docs_down.html?fromPage=0002&flag=3&btnSource="+btnSource+"&mark="+mark;
}

function wf_getDate(data) {
	if($("#urgency").val() == '0001') {
		return getTheDate(86400);
	}
	if($("#urgency").val() == '0002') {
		return getTheDate(86400);
	}
	if($("#urgency").val() == '0003') {
		return getTheDate(86400);
	}
	if($("#urgency").val() == '0004') {
		return getTheDate(86400);
	}
	return getTheDate(86400);
}



//校验
function wf_beforeValid(){
	//文件名称不能为空
	if($("#receiveTitle").val() == ''){
		$.alert("文件标题不能为空");
		return false;
	}
//	//秘密等级
//	if($('input:checkbox[name="secretLevel"]:checked').val()==null){
//		$.alert("请确认此文件为非涉密件");
//		return false;
//	}
	//紧急程度
	if($("#urgency").val() == ''){
		$.alert("请选择紧急程度");
		return false;
	}
	
	//呈批意见
	//&&($("#nbyjAudit").html().length===0)
	if(userTask == 'usertask2' && $("#hgOpinion").val() == ''){
		$.alert("请输入拟办意见");
		return false;
	}/*
        else if(userTask == 'usertask2' && validLength($("#nbyj").val())>30){
		$.alert("拟办意见应少-于30字");
		return false;
	}*/else if((userTask == 'usertask3' ||  userTask == 'usertask4') && $("#leaderOpinion").val()==''){
	//秘书长审核
		$.alert("请输入批示意见");
		return false;
                
	}
    /*
        else if((userTask == 'usertask3' ||  userTask == 'usertask4') && validLength($("#leaderOpinion").val())>30){
	//秘书长审核
		$.alert("批示意见应小-于30字");
		return false;
                
	}
        else if((userTask == 'usertask5' ||  userTask == 'usertask6') && validLength($("#leaderOpinion").val())>30){
	//秘书长审核
		$.alert("批示意见应小-于30字");
		return false;
                
	}*/
        else if((userTask == 'usertask5' ||  userTask == 'usertask6') && $("#leaderOpinion").val()==''){
	//领导审核
		$.alert("请输入批示意见");
		return false;
	}
          window.scrollTo(0,0);
	return true;
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
		$.alert('请输入备注信息');
		return false;
	}
	var dto = { id:id,littlePage:lps};
	doJsonRequest("/receivedoc/addLittlePage",dto,function(data){
             $("#lPageSpan").html(dto.littlePage);
            $("#liPage").val("");
            $.alert("保存成功");
            return;
		window.location.href="../tododocs/docs_down.html?fromPage=0002&flag=2&operaterId="+data.operaterId+"&btnSource="+btnSource+"&mark="+mark;
	},{showWaiting:true});
}




function wf_getMark(operaterId){
	var hgOpinion = $("#hgOpinion").val();
	var ldsh = $("#leaderOpinion").val();
	if(userTask == 'usertask2' && hgOpinion != ''){
		lcbz = hgOpinion;
	}else if((userTask == 'usertask3' ||  userTask == 'usertask4' || userTask == 'usertask5' ||  userTask == 'usertask6') && ldsh!=''){
		lcbz = ldsh;
	}
	if(operaterId == "flow16") {
		lcbz = "确认收到";
	}
	
	return lcbz;
}

//批示意见一栏，如果当前有签字标示，则当在文书审核环节，本文档被点击时 删除签字标示
$("#hgOpinion").focus(function(){
	var thisText = $(this).val();
	if(userTask == 'usertask2' && thisText.indexOf('[')>-1 && thisText.indexOf(']')>-1){
		$(this).val(thisText.split('[')[0]);
	}
})

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
	if(userTask == 'usertask2'){
		thisText = $("#hgOpinion").val();
		$("#hgOpinion").val(thisText+language);
		$("#hgOpinion")[0].focus();
	}else if(userTask == 'usertask3' ||  userTask == 'usertask4'||userTask == 'usertask5' ||  userTask == 'usertask6'){
		thisText = $("#leaderOpinion").val();
		$("#leaderOpinion").val(thisText+language);
		$("#leaderOpinion")[0].focus();
	}
	
}
//----------常用语（结束）------------
