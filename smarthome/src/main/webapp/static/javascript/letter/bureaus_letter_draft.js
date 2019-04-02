var text = "";
 var redFlagX =false;
 var id = getURLParam("id"); //当前收文id
 var fromPage = getURLParam("fromPage"); //页面来源
 var type = getURLParam("type"); //页面来源
 var redFlag =false;
 var allBtn = "revision_final_saveFile_savePDF";//weboffice按钮控制
 $("#reportType").val(type);
 $("#id").val(id);
$(function () {
   

    $('#dataInputForm').fileupload({
        disableImageResize: false,
        url: '/cmcp/attachment/upload'
    });
    $('#dataInputForm').fileupload('option', {
        // Enable image resizing, except for Android and Opera,
        // which actually support image resizing, but fail to
        // send Blob objects via XHR requests:
        disableImageResize: /Android(?!.*Chrome)|Opera/
            .test(window.navigator.userAgent),
            maxFileSize: attachment_maxSize,
            acceptFileTypes:attachment_regixType
    });
    
    $('#dataInputForm1').fileupload({
        disableImageResize: false,
        url: '/cmcp/attachment/upload'
    });
    $('#dataInputForm1').fileupload('option', {
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
	//初始化流程按钮
	wf_getOperator("letterForBureaus",function(data){
	});
	//获取当前登陆人信息
	getCurrenUserInfo(function(data){
		$("#userCode").val(data.userCode);
		$("#userOrg").val(data.orgName);
		$("#userName").val(data.userName);
		$("#phoneNumber").val(data.phoneNumber);
		/*$("#redHeadTemp").val(data.redHeadTemp);*/
		
		var redHfes = data.redHeadFileEntities;
		for(var i = 0;i<redHfes.length;i++){
			var redHfe = redHfes[i];
			if(type == redHfe.reportType){
				var span = $("<span></span>");
				var btn = $(document.createElement("button")).addClass('btn').addClass('btn_click').attr('name','redBtn').html(redHfe.mark).data('redData',redHfe);
				btn.appendTo(span);
				btn.click(function(){
                                    if(redFlag)
                                        return;
                    setTimeout(function(){
                        redFlag=false;
                    },2000);
						var thisRed = $(this).data('redData');
						//读取模板文件套红(URL:文件路径;endUrl:尾部套红路径；code:套红编号;ccuser;套红抄送人;company:发文单位(ZF政府、BGT办公厅);)
						var URL = baseRedHeadUrl+thisRed.redHeadTemp;
						var endUrl = baseRedHeadUrl+thisRed.endTemp;
						weboffice.window.insertRedHeadFromUrl(URL,endUrl,'001','','');
						//$("button[name='redBtn']").hide();
                                                redFlag =true;
				});
				span.appendTo($("#buttonList"));
			}
		}
		$("button[name='redBtn']").show();
	});
	
	/*if(id != null) {
		//读取草稿
		$("#id").val(id);
		var dto = {
				id:id
		}
		doJsonRequest("/burLetter/getBurLetter",dto,function(data){
			if(data.result) {
				var data = data.data;
				//正文
				$("#text").val(data.text);
			} else {
				$.alert("获取信息失败。");
			}
		});
	}*/
	$("#textIframe").append('<iframe id="webOfficeIframe" name="weboffice" src="../weboffice/weboffice.html?rightInfo=1&burletId='+$("#id").val()+'&allBtn='+allBtn+'" style="z-index:9999;width:100%;height:800px;"></iframe>');
});

$("#caBtn").on("click",function(){
	$.alert("暂未集成CA。");
})

//保存
function getSendData(){
	$("#webOfficeIframe")[0].contentWindow.saveFileToUrl();
	var datas =  $("#webOfficeIframe")[0].contentWindow.getInto();
	var dto = {
		contactPerson:datas.contactPerson,
		contactNumber:datas.contactNumber,
		orgCode:datas.createOrgCode,
		orgName:datas.createOrgName,
		reportTitle:datas.reportTitle,
		//letterType:'0001',
		text:datas.text,
		urgency:datas.urgency,
		secretLevel:datas.secretLevel,
		attachmentDTOs:datas.attachmentDTOs,
		reportType:$("#reportType").val()==""?"7001":$("#reportType").val(),
		status:"0002",
		flag:0
	};
	return dto;
}

//校验
function wf_beforeValid(){
	var datas =  $("#webOfficeIframe")[0].contentWindow.getInto();
	//文件名称不能为空
	if(datas.reportTitle == ''){
		$.alert("请输入文件标题");
		return false;
	}
	if(datas.urgency == ''){
		$.alert("请选择密级");
		return false;
	}
	if(!datas.text.endWith(".pdf")) {
		$.alert("请将正文转为PDF");
		return false;
	}
	if(datas.secretLevel == null || datas.secretLevel == ''){
		$.alert("请确认此文件为非涉密件");
	    return false;
	}
	return true;
}

/*$("#redBtn").click(function(){
	//读取红头文件
        if(redFlagX)
            return;
	weboffice.window.insertRedHeadFromUrl($("#redHeadTemp").val(),"","","","");
	redFlagX =true;
});*/

function wf_getDate(data) {
	if($("#urgency",$("#webOfficeIframe")[0].contentWindow.document).val() == '0001') {
		return getTheDate(86400);
	}
	if($("#urgency",$("#webOfficeIframe")[0].contentWindow.document).val() == '0002') {
		return getTheDate(86400);
	}
	if($("#urgency",$("#webOfficeIframe")[0].contentWindow.document).val() == '0003') {
		return getTheDate(86400);
	}
	if($("#urgency",$("#webOfficeIframe")[0].contentWindow.document).val() == '0004') {
		return getTheDate(86400);
	}
}

function goSuccess(data) {
	var roleName = data.assignRoleName;
	window.location.href="../tododocs/docs_down.html?roleName="+roleName+"&fromPage=0001&btnSource=app";
}

//保存到草稿箱
$("#tempSubmitBtn").on("click",function(){
	var dto = getSendData();
	dto.id = $("#id").val();
	dto.status = "0001";
	doJsonRequest("/burLetter/saveBurLetter",dto,function(data){
		if(data.result) {
			var data = data.data;
			$("#id").val(data.id);
			$.alert("暂存成功。");
		} else {
			alert("添加报告出错。");
		}
	},{showWaiting:true});
});

/*function doSend() {
	wf_doSearchRole(function(data){
		
		var dto = {
				contactPerson:datas.contactPerson,
				contactNumber:datas.contactNumber,
				orgCode:datas.createOrgCode,
				title:datas.title,
				type:'0001',
				roleCodes:data.roleCode,
				completeDate:data.completeDate,
				wordUrl:datas.wordUrl,
				pdfUrl:datas.pdfUrl,
				urgency:datas.urgency,
				attachmentDTOs:datas.attachmentDTOs
		}
		var data_ = data;
		doJsonRequest("/consultation/doSave",dto,function(data){
			if(data.result) {
				window.location.href="consulation_skip.html?fromPage=0001&roleName="+data_.roleName.join(",");
			} else {
				$.alert("发送失败。");
			}
		},{showWaitting:true})		
	})
}*/

/*function wf_doSearchRole(fun) {
	var str = "checkType=multi";
	var roleCodes = "036,034,033"
	var obj = {
	    title:'选择发往单位',
	    height:"320px",
	    width:"750px",
	    url:'../users/send_role_dialog.html?roleCodes='+roleCodes,
	    fun:true,
	    myCallBack:fun
	}
	new jqueryDialog(obj);
	$(".dialog_div_").parent().addClass("wf_top");
}*/

/*function saveRedBtn(url){
	pdfUrl = url;
}*/
var webOfficeEndUrl = "../../static/officefile/template/mailend.doc";