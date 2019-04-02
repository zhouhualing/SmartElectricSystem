var id = getURLParam("id"); //当前收文id
var fromPage = getURLParam("fromPage"); //页面来源
var type = getURLParam("type"); //页面来源
var taskId = getURLParam("taskId");
var mark = getURLParam("mark");//当前是否是市长秘书点击进来的 002:是 
var lcbz = "";
var isMayor = 0;
var officeflag = true;
$("#id").val(id);
$("#businessKey").val(id);
$("#reportType").val(type);
$("#qsy a").tab("show");
var localText = "";
var roleCode = "";
var roleName = "";
var businessType = getURLParam("businessType");
var realRoleCode = null;
$(function() {
    'use strict';

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
$(function() {
    //获取当前登陆人信息
    getCurrenUserInfo(function(data) {
        $("#userCode").val(data.userCode);
        $("#userOrg").val(data.orgName);
        $("#userName").val(data.userName);
        roleCode = data.roleCodes;
        roleName = data.roleNames;
        $("#phoneNumber").val(data.phoneNumber);
        isMayor = data.isMayor;
        realRoleCode = data.realRoleCode;
    });
    if (id != null) {
        $("#id").val(id);
        var dto = {
            id: id
        }
        doJsonRequest("/receivedoc/getReceiveDoc", dto, function(data) {
            if (data.result) {
                var data = data.data;
                window.sjlw =data;
                if (null != data.files[0]) {
                	localText = data.files[0].pdfUrl;
                	/*if(data.files[0].pdfUrl.endWith(".pdf")){
                		$("#mailFrame").attr("src", data.files[0].pdfUrl);
                	}*/
                    $('#dataInputForm').fileupload('option', 'done').call($('#dataInputForm'), $.Event('done'), {result: data});
                    $(".name", "#attachmentDiv").children("a").attr("onclick", "showPDF(this)")
                }
                //来文类型
                $("#reportType").val(data.reportType);
                //来文单位
                $("#docCameOrgan").val(data.docCameOrgan);

                //文号
                $("#receiveCode").val(data.receiveCode);
                //秘密等级
                $("input[name='secretLevel'][value='" + data.secretLevel + "']").attr("checked", 'checked');
                //紧急程度
                $("#urgency").val(data.urgency);
                //文件标题
                $("#receiveTitle").val(data.receiveTitle);
                //秘书审核
                $("#fmszOpinion").val(data.fmszOpinion);
                $("#mszOpinion").val(data.mszOpinion);
                var mszpsAudit = "";
                if (data.mszOpinion != '' && data.mszOpinion != null) {
                    mszpsAudit += data.mszOpinion + '<br>';
                }
                if (data.fmszOpinion != '' && data.fmszOpinion != null) {
                    mszpsAudit += data.fmszOpinion;
                }
                $("#mszpsAudit").html(mszpsAudit);
                //市长审核
                $("#fszOpinion").val(data.fszOpinion);
                $("#szOpinion").val(data.szOpinion);
                var ldpsAudit = "";
                if (data.szOpinion != '' && data.szOpinion != null) {
                    ldpsAudit += data.szOpinion + '<br>';
                }
                if (data.fszOpinion != '' && data.fszOpinion != null) {
                    ldpsAudit += data.fszOpinion;
                }
                $("#ldpsAudit").html(ldpsAudit);

                //呈批意见
                $("#hgOpinion").val(data.hgOpinion);
                $("#hgOp").val(data.hgOpinion);
           
                //$("#hgOpinionSPAN").html(data.hgOpinion);
                //备注
                $("#remark").val(data.remark);
                //正文
                $("#text").val(data.text);
                $("#drafterName").html(data.drafterName);
                $("#drafterPhoneNum").html(data.drafterPhoneNum);
                if (data.receiveDate != '' && data.receiveDate != null) {
                    $("#receiveDate").html(new Date(data.receiveDate).format('yyyy年M月d日'));
                }
                $("#lwCode").val(data.lwCode);
                $("#upCardFlag").val(data.upCardFlag);
                $("#createUserName").val(data.createUserName);
                //小纸条
                /*$("#liPage").val(data.littlePage);*/
				$("#lPageSpan").html(data.littlePage);
                //当前是否是退回办理人
                $("#isReturn").val(data.isReturn);
                doQueryWF("reportInfo", "approvalDiv");
                //初始化流程按钮
                var dto = {
                    taskId: taskId
                }
                wf_getOperator(dto, function(data) {
                    userTask = data.userTask;//获取当前流程
                    
                    if(userTask!='usertask5' && userTask!='usertask9' && userTask!='usertask6'){
                    	$("#buttonList").show();
                    }
                    //根据当前步骤控制页面可输入项:
                    /*if (userTask == 'usertask7' && $("#isReturn").val() == 1) {
                        $("#buttonList span").hide();
                        $("#toVoid").show();
                    }*/
                    if (userTask == 'usertask8') {
                    	getCurrenUserInfo(function(data){
                    		if(data.orgCode=="huiwk") {
                    			$("#meetContentBtn").removeClass("hidden")
                    		}
                    	})
                    	
                    	/*if(mark=='002' && $("#userCode").val()!='lijm'){//分发给市长，市长秘书进入时隐藏确认收文按钮
							$("#beSended").hide();
						}*/
                    	
                    	//分发给市长，市长秘书进入时隐藏确认收文按钮
						if((mark=='002' && realRoleCode!='002') || (mark=='048' && realRoleCode!='048') || (mark=='051' && realRoleCode!='051') || (mark=='049' && realRoleCode!='049') || (mark=='003' && realRoleCode!='003') || (mark=='050' && realRoleCode!='050') || (mark=='039' && realRoleCode!='039') || (mark=='043' && realRoleCode!='043')){
							$("#buttonList").empty().show().append('<span style="line-height:35px;color:blue;font-weight:bold;">领导文件</span>');
						}
						lcbz = "收到";
                    }
                    if(userTask == 'usertask2'){
						$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
						$("#hgOpinion").removeAttr("disabled").attr("placeholder","请填写呈批意见");
	                    if($("#hgOpinion").val()){
	                        $("#hgOpinion").attr("placeholder","");
	                    }
					}
					if(userTask == 'usertask3'||userTask == 'usertask4'){
						$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
						$("#mszps").removeAttr("disabled").attr("placeholder","请填写批示意见");
					}
					if(userTask == 'usertask5' || userTask == 'usertask9'){
						if(realRoleCode!=null && realRoleCode!='null' && realRoleCode!=''){
							$("#buttonList").show();
							$("#ldps").removeAttr("disabled").attr("placeholder","请填写批示意见");
							$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
						}else{
							$("#ldps").attr('disabled', true);
							$("#buttonList").empty().show().append('<span style="line-height:35px;color:blue;font-weight:bold;">领导文件</span>');
						}
					}
                    if (userTask == 'usertask6') {
//                        $("#littlePage").show();
                        /*if(mark=='002' && $("#userCode").val()!='lijm'){
//                        	$("#buttonList").append("<button type='button'  class='btn btn_click'  onclick='saveLittlePage()' style='margin-left:8px;' >保存便签</button>");
                            $("#ldps").attr('disabled', true);
                            $("#buttonList span").hide();
                        }else{
                        	$("#ldps").removeAttr("disabled").attr("placeholder","请填写批示意见");
                        	 $("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
                        }*/
//                        $("#liPage").removeAttr("disabled");
                        
                        if(realRoleCode!=null && realRoleCode!='null' && realRoleCode!=''){
							$("#buttonList").show();
							$("#ldps").removeAttr("disabled").attr("placeholder","请填写批示意见");
							$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
						}else{
							$("#ldps").attr('disabled', true);
							$("#buttonList").empty().show().append('<span style="line-height:35px;color:blue;font-weight:bold;">领导文件</span>');
						}
                        
                    }

					if($("#ldps").attr("disabled") == "disabled"){
						$("#ldps").remove();
					}
					if($("#mszps").attr("disabled") == "disabled"){
						$("#mszps").remove();
					}
                  /*  if($("#hgOpinion").attr("disabled") == "disabled"){
						$("#hgOpinion").hide();
					}*/
				});
          
             
			} else {
				$.alert("获取信息失败。");
			}
		});
	}
});

$("#textPage").click(function(){
	if(officeflag){
		$("#editmain_left").append('<iframe name="weboffice" src="../weboffice/webpdf.html?officeUrl=' +localText+'"style="width:100%;height:800px;"></iframe>');
		officeflag = false;
	}
});

function showPDF(_this) {
//    $("#mailFrame").attr("src", $(_this).attr("theUrl"))
    weboffice.window.openFileFromUrl($(_this).attr("theUrl"));
}
function getAddData() {
    //核稿
	var hgOpinion = $("#hgOpinion").val();
	/*var realHgOpinion = hgOpinion.replace(/[ ]/g,"");
	realHgOpinion = realHgOpinion.replace(/(\n)+|(\r\n)+/g,"");
	
	var hgOp = $("#hgOp").val();
	var realHgOp = hgOp.replace(/[ ]/g,"");
	realHgOp = realHgOp.replace(/(\n)+|(\r\n)+/g,"");*/
	if(userTask == 'usertask2'){
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
    //秘书长审批
    var fmszOpinion = $("#fmszOpinion").val();
    fmszOpinion=window.sjlw.fmszOpinion||"";
    var mszOpinion = $("#mszOpinion").val();
    mszOpinion =window.sjlw.mszOpinion||"";

    var mszps = $("#mszps").val();
    if (mszps != '') {
        mszps = mszps + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]";
        if (userTask == 'usertask3') {
            //如果当前环节为副秘书长审批
            if (fmszOpinion != '') {
                fmszOpinion = mszps + "<br>" + fmszOpinion;
            } else {
                fmszOpinion = mszps;
            }
        } else if (userTask == 'usertask4') {
            //如果当前环节为秘书长审批
            if (mszOpinion != '') {
                mszOpinion = mszps + '<br>' + mszOpinion;
            } else {
                mszOpinion = mszps;
            }
        }
    }

    //市长审批
    var fszOpinion = $("#fszOpinion").val();
    fszOpinion =window.sjlw.fszOpinion||"";
    var szOpinion = $("#szOpinion").val();
    szOpinion=window.sjlw.szOpinion||"";
    var ldps = $("#ldps").val();
    if (ldps != '') {
        ldps = ldps + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]";
        if (userTask == 'usertask5') {
            //如果当前环节为副市长审批
            if (fszOpinion != '') {
                fszOpinion = ldps + '<br>' + fszOpinion;
            } else {
                fszOpinion = ldps;
            }
        } else if (userTask == 'usertask9') {
            //多头送副市长时：
            fszOpinion = ldps;//此时不读取副市长之前的信息，传到后台处理时再读取，这样解决多个副市长签批意见并发的问题。
        } else if (userTask == 'usertask6') {
            //如果当前环节为市长审批
            if (szOpinion != '') {
                szOpinion = ldps + '<br>' + szOpinion;
            } else {
                szOpinion = ldps;
            }
            
        }
    }


    /*var attachmentDTOs = new Array();
     //保存正文
     if(flag){
     weboffice.window.saveFileToUrl();
     attachmentDTOs = weboffice.window.getAttachmentDTOs();
     var officefileUrl= window.frames["weboffice"].document.getElementById('officeUrl').value;
     $("#text").val(officefileUrl);
     }*/
    var dto = {};
    if (userTask == 'usertask7') {
        dto = {
            id: $("#id").val(),
            ignore:true,
           /* //来文类型
            reportType: $("#reportType").val(),
            //来文单位
            docCameOrgan: $("#docCameOrgan").val(),
            //文号
            receiveCode: $("#receiveCode").val(),
            secretLevel: $('input:checkbox[name="secretLevel"]:checked').val(),
            //紧急程度
            urgency: $("#urgency").val(),
            //文件标题
            receiveTitle: $("#receiveTitle").val(),
            //秘书审核
             fmszOpinion:$("#fmszOpinion").val(),
             mszOpinion:$("#mszOpinion").val(),
             //市长审核
             fszOpinion:$("#fszOpinion").val(),
             szOpinion:$("#szOpinion").val(),
             //呈批意见
             hgOpinion:$("#hgOpinion").val(),
            //备注
            remark: $("#remark").val(),
            lwCode: $("#lwCode").val(),
            //正文
            text: $("#text").val(),
            //附件
            //attachmentDTOs:attachmentDTOs,
            status: '0002',
            drafterCode: $("#userCode").val(),
            receiveDate: $("#receiveDate").html().split('日')[0].replace(/[^\d]/g, '-'),
            flag: 1, //当前为审批流程.
            upCardFlag: '2',*/
        };
    } else if (userTask == 'usertask2') {
        //核稿
        dto = {
            id: $("#id").val(),
            //呈批意见
            hgOpinion: hgOpinion,
            //附件
            //attachmentDTOs:attachmentDTOs,
            status: '0002',
            flag: 1, //当前为审批流程.
        };
    } else if (userTask == 'usertask3' || userTask == 'usertask4') {
        //秘书长审核
        dto = {
            id: $("#id").val(),
            //秘书审核
            fmszOpinion: fmszOpinion,
            mszOpinion: mszOpinion,
            flag: 1, //当前为审批流程.
        };
    } else if (userTask == 'usertask5' || userTask == 'usertask6') {
        //市长审核
        dto = {
            id: $("#id").val(),
            //市长审核
            fszOpinion: fszOpinion,
            szOpinion: szOpinion,
            littlePage:getLittileMsg(),
            //附件
            //attachmentDTOs:attachmentDTOs,
            flag: 1, //当前为审批流程.
        };
    } else if (userTask == 'usertask9') {
        //副市长审核
        dto = {
            id: $("#id").val(),
            //市长审核
            fszOpinion: fszOpinion,
            flag: 1, //当前为审批流程.
        };
    } else {
        dto = {
            id: $("#id").val(),
            flag: 1, //当前为审批流程.
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
    window.location.href = "../tododocs/docs_down.html?roleName=" + roleName + "&fromPage=0002" + "&btnSource=" + btnSource+"&mark="+mark;
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
					/*$.alert("创建成功。");*/
					window.location.href = "../tododocs/docs_down.html?fromPage=0002&flag=8&btnSource=" + btnSource+"&mark="+mark;
				} else {
					$.alert("创建失败。");
					return false;
				}
			},{showWaitting:true})	
		}else{
			window.location.href = "../tododocs/docs_down.html?fromPage=0002&flag=0&btnSource=" + btnSource+"&mark="+mark;
		}
	}else{
		window.location.href = "../tododocs/docs_down.html?fromPage=0002&flag=0&btnSource=" + btnSource+"&mark="+mark;
	}
}
//多头发送至副市长时，副市长审批后送至文书办主任
function goSuccessWSBZR() {
    window.location.href = "../tododocs/docs_down.html?fromPage=0002&flag=5&btnSource=" + btnSource+"&mark="+mark;
}
//办结
function goSuccessEnd() {
    window.location.href = "../tododocs/docs_down.html?fromPage=0002&flag=1&btnSource=" + btnSource+"&mark="+mark;
}
function goVoidEnd() {
    window.location.href = "../tododocs/docs_down.html?fromPage=0002&flag=3&btnSource=" + btnSource+"&mark="+mark;
}


function wf_getDate(data) {
    if ($("#urgency").val() == '0001') {
        return getTheDate(86400);
    }
    if ($("#urgency").val() == '0002') {
        return getTheDate(86400);
    }
    if ($("#urgency").val() == '0003') {
        return getTheDate(86400);
    }
    if ($("#urgency").val() == '0004') {
        return getTheDate(86400);
    }
    return getTheDate(86400);
}



//校验
function wf_beforeValid() {
    //文件名称不能为空
    if ($("#receiveTitle").val() == '') {
        $.alert("文件标题不能为空");
        return false;
    }
    //秘密等级
    if ($('input:checkbox[name="secretLevel"]:checked').val() == null) {
        $.alert("请确认此文件为非涉密件");
        return false;
    }
    //紧急程度
    if ($("#urgency").val() == '') {
        $.alert("请选择紧急程度");
        return false;
    }
   if(!$("#mszps").attr("disabled")){
      if($("#mszps").val() == ''){
          $.alert("请输入呈批意见");
        return false;
      } /*
      if (validLength($("#mszps").val()) > 40) {
        $.alert("呈批意见应少-于40字");
        return false;
    }*/
      
  }if(!$("#ldps").attr("disabled")){
      if($("#ldps").val() == ''){
          $.alert("请输入呈批意见");
        return false;
      } /*
      if (validLength($("#ldps").val()) > 40) {
        $.alert("呈批意见应少-于40字");
        return false;
    }*/
      
  }
    //呈批意见
    if (userTask == 'usertask2' && $("#hgOpinion").val() == '') {
        $.alert("请输入呈批意见");
        return false;
        /*
        if (validLength($("#hgOpinion").val()) > 40) {
        $.alert("呈批意见应少-于40字");
        return false;
    }*/
    } else if ((userTask == 'usertask3' || userTask == 'usertask4') && $("#mszps").val() == '') {
        //秘书长审核
        $.alert("请输入批示意见");
        return false;
        /*
         if (validLength($("#mszps").val()) > 40) {
        $.alert("批示意见应少-于40字");
        return false;
    }*/
    } else if ((userTask == 'usertask5' || userTask == 'usertask6' || userTask == 'usertask9') && $("#ldps").val() == '') {
        //领导审核
        $.alert("请输入批示意见");
        return false;
        /*
         if (validLength($("#ldps").val()) > 40) {
        $.alert("批示意见应少-于40字");
        return false;
    }*/
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

function wf_getMark(operaterId) {
    var hgOpinion = $("#hgOpinion").val();
    var mszps = $("#mszps").val();
    var ldps = $("#ldps").val();
		//呈批意见
	if(userTask == 'usertask2' && hgOpinion != ''){
		lcbz = hgOpinion;
	}else if((userTask == 'usertask3' ||  userTask == 'usertask4') && mszps!=''){
		//秘书长审核
		lcbz = mszps;
	}else if((userTask == 'usertask5' ||  userTask == 'usertask6' || userTask == 'usertask9') && ldps!=''){
		//领导审核
		lcbz = ldps;
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
	}else if(userTask == 'usertask3' || userTask == 'usertask4'){
		thisText = $("#mszps").val();
		$("#mszps").val(thisText+language);
		$("#mszps")[0].focus();
	}else if(userTask == 'usertask5' || userTask == 'usertask6' || userTask == 'usertask9'){
		thisText = $("#ldps").val();
		$("#ldps").val(thisText+language);
		$("#ldps")[0].focus();
	}
}
//----------常用语（结束）------------


function saveToContentDoc() {
    saveToMeetContentDoc(id,'5001',$("#receiveTitle").val());
}