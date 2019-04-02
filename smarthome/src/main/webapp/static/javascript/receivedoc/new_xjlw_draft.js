var pdfUrl = "";
//打开时默认显示正文页:
$("#textPage a").tab("show");
var redFlag = false;
var id = getURLParam("id"); //当前收文id
var fromPage = getURLParam("fromPage"); //页面来源
var type = getURLParam("type"); //页面来源
$("#reportType").val(type);
var date = new Date();
$("#receiveDate").val(date.format('yyyy年M月d日'));
var redFlag =false;
var roleCode = "";
var allBtn = "revision_final_saveFile_savePDF";//weboffice按钮控制
$(function(){
	//初始化流程按钮
	wf_getOperator("getDocFromLower",function(data){
	});
	//获取当前登陆人信息
	getCurrenUserInfo(function(data){
		$("#userCode").val(data.userCode);
		$("#userOrg").val(data.orgName);
		$("#userName").val(data.userName);
		$("#phoneNumber").val(data.phoneNumber);
		roleCode = data.roleCodes;
		var redHfes = data.redHeadFileEntities;
		for(var i = 0;i<redHfes.length;i++){
			var redHfe = redHfes[i];
			if(type == redHfe.reportType){
				var span = $("<span></span>");
				var btn = $(document.createElement("button")).addClass('btn').addClass('btn_click').attr('id','redBtn').html(redHfe.mark).data('redData',redHfe);
				btn.appendTo(span);
				btn.click(function(){
                                   
                                    if(redFlag)
                                        return;
                    setTimeout(function(){
                        redFlag =false;
                    },2000);
					var thisRed = $(this).data('redData');
					//读取模板文件套红(URL:文件路径;endUrl:尾部套红路径；code:套红编号;ccuser;套红抄送人;company:发文单位(ZF政府、BGT办公厅);)
					var URL = baseRedHeadUrl+thisRed.redHeadTemp;
					var endUrl = baseRedHeadUrl+thisRed.endTemp;
					weboffice.window.insertRedHeadFromUrl(URL,endUrl,'001','','');
                                        redFlag =true;
                                   
				});
				span.appendTo($("#buttonList"));
			}
		}
		
		if(id != null) {
			$("#id").val(id);
			var dto = {
					id:id
			}
			doJsonRequest("/receivedoc/getReceiveDoc",dto,function(data){
				if(data.result) {
					var data = data.data;
					//正文
					$("#text").val(data.text);
				} else {
					$.alert("获取信息失败。");
				}
				$("#textPage").trigger("click");
			});
		}else{
			$("#textPage").trigger("click");
		}
	});
	

});

//鼠标移入触发正文保存事件
var attachmentDTOs = new Array();
var wodto = {};
var toPDFFlag = false;

//流程参数
function getAddData() {
    var dto = {
        id: $("#id").val(),
        reportType: $("#reportType").val(),
        receiveTitle: wodto.receiveTitle,
        receiveCode: wodto.receiveCode,
        secretLevel: wodto.secretLevel,
        docCameOrgan: wodto.docCameOrgan,
        contactPerson:wodto.contactPerson,
		contactNumber:wodto.contactNumber,
        receiveDate:$("#receiveDate").val().split('日')[0].replace(/[^\d]/g,'-'),
        upCardFlag: '2',
        //正文
        text: $("#text").val(),
        textId: $("#textId").val(),
        //附件
        attachmentDTOs: attachmentDTOs,
        status: '0002',
        flag: 0, //当前为新建流程.
    };
    return dto;
}

//保存到草稿箱
$("#tempSubmitBtn").on("click", function() {
	if (flag) {
        weboffice.window.saveFileToUrl();
        wodto = weboffice.window.getAllData();
        toPDFFlag = weboffice.window.toPDFFlag;
        attachmentDTOs = weboffice.window.getAttachmentDTOs();
        var officefileUrl = window.frames["weboffice"].document.getElementById('officeUrl').value;
        $("#text").val(officefileUrl);
        $("#textId").val(wodto.textId);
    }
    var dto = getAddData();
    dto.status = "0001";
    doJsonRequest("/receivedoc/addReceiveDoc", dto, function(data) {
        if (data.result) {
            var data = data.data;
            $("#id").val(data.id);
            $.alert("暂存成功。");
        } else {
            alert("添加报告出错。");
        }
    }, {showWaiting: true});
});


function goSuccess(data) {
    var roleName = data.assignRoleName;
    window.location.href = "../tododocs/docs_down.html?roleName=" + roleName + "&fromPage=0001" + "&btnSource=app";
}

function saveRedBtn(url) {
    pdfUrl = url;
}

//正文加载webOffice控件
var flag = false;
var saveflag = true;
$("#textPage").click(function() {
    if (!flag) {
        $("#textIframe").append('<iframe id="webOfficeIframe" name="weboffice" src="../weboffice/weboffice2.html?officeUrl=' + $("#text").val() +'&allBtn='+allBtn+ '&isStart=1&localRole=' +roleCode+ '&taskid=' + id + '" style="z-index:9999;width:100%;height:900px;"></iframe>');
        flag = true;
    }
});
//.mouseout(function() {
//    if (flag && saveflag) {
//        weboffice.window.saveFileToUrl();
//        saveflag = false;
//    }
//});
/*$("#redBtn").on('click',function(){
	//读取模板文件套红(URL:文件路径;code:套红编号;issuer:套红签发人;ccuser;套红抄送人;company:发文单位(ZF政府、BGT办公厅);)
	var URL = "../../static/officefile/template/";
	//以后应该匹配orgCode 或者 其他优化方式
	if($("#userOrg").val()=='公安局'){
		URL+="xjlwRedHead.doc";
	}else if($("#userOrg").val()=='监察局'){
		URL+="xjlwRedJCJ.doc";
	}else if($("#userOrg").val()=='发改委'){
		URL+="xjlwRedFGW.doc";
	}else if($("#userOrg").val()=='经委'){
		URL+="xjlwRedJW.doc";
	}else if($("#userOrg").val()=='市政管理委员会'){
		URL+="xjlwRedSZGLW.doc";
	}else if($("#userOrg").val()=='财政局'){
		URL+="xjlwRedCZJ.doc";
	}else if($("#userOrg").val()=='天镇县政府'){
		URL+="xjlwRedTZX.doc";
	}else if($("#userOrg").val()=='住建委'){
		URL+="xjlwRedZJW.doc";
	}else if($("#userOrg").val()=='文物局'){
		URL+="xjlwRedWWJ.doc";
	}else if($("#userOrg").val()=='体育局'){
		URL+="xjlwRedTYJ.doc";
	}else{
		URL+="xjlwRedHead.doc";//default
	}
	var code = "001";
	var	issuer = $("#theIssuer").val();
	var ccuser = $("#ccUnit").val();
	var company = "ZF";
	weboffice.window.insertRedHeadFromUrl(URL,code,issuer,ccuser,company);
});*/

//校验
function wf_beforeValid() {
	  //保存正文
    if (flag) {
        weboffice.window.saveFileToUrl();
        wodto = weboffice.window.getAllData();
        toPDFFlag = weboffice.window.toPDFFlag;
        attachmentDTOs = weboffice.window.getAttachmentDTOs();
        var officefileUrl = window.frames["weboffice"].document.getElementById('officeUrl').value;
        $("#text").val(officefileUrl);
        $("#textId").val(wodto.textId);
    }
    //文件名称不能为空
   //  console.log(wodto);
    if (wodto.receiveTitle == '') {
        $.alert("请输入文件标题");
        return false;
    }/*
   if(wodto.docCameOrgan.length>40){
         $.alert("来文单位应少-于40字");
        return false;
   }*/
    //$.alert(wodto.receiveTitle);
    /*
     if (wodto.receiveTitle.length>60) {
        $.alert("文件标题应少-于60字");
        return false;
    }*/
    if (wodto.receiveCode == '') {
        $.alert("请输入文件编号");
        return false;
    }
    /*
     if (wodto.receiveCode.length>25) {
        $.alert("文件编号应少-于25字");
        return false;
    }*/
    if (wodto.secretLevel == null) {
        $.alert("请确认此文件为非涉密件");
        return false;
    }
    if (!$("#text").val().endWith(".pdf")) {
        $.alert("请将正文转为PDF");
        return false;
    }
    window.scrollTo(0,0);
    return true;
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

function wf_getPrintInfo(operaterId){
	var dto = {};
	if("flow2"==operaterId){
		var tempDTO= [{businessId:'0',businessType:'0012',textId:$("#textId").val()}];
		dto.docLists = tempDTO;
		dto.countLists = [{roleCode:roleCode,count:'99'}];
	}
	return dto;
}


