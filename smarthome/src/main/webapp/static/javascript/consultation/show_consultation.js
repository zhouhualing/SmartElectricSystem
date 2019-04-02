var id = getURLParam("id");
var flag =  getURLParam("flag");
var fromPage =  getURLParam("fromPage");
$(function () {
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

$("#replayBtn").on("click", function(){
	window.location.href="drafting_replayconsultation.html?fromPage="+fromPage+"&id="+id;
})


$(function(){
	if(flag=="0001") {
		$("#replayTd").show();
	}
	$("#createDate").html(new Date().format("yyyy-MM-dd"))
	
	var dto = {
		id:id
	}
	$('#dataInputForm').addClass('fileupload-processing');
	doJsonRequest("/consultation/getInfo", dto, function(data){
		if(data.result) {
			 $("#mailFrame").attr("src","/cmcp"+data.data.pdfUrl);
			 $('#dataInputForm').fileupload('option', 'done').call($('#dataInputForm'), $.Event('done'), {result: data.data});
			 $('#dataInputForm').removeClass('fileupload-processing');
			 $("[id='file_removeBtn']").remove();
			$(".name","#attachmentDiv").children("a").attr("onclick","showPDF(this)")
			 var data = data.data;
			$("#contactPerson").html(data.contactPerson);
			$("#createOrgName").html(data.orgName);
			$("#contactNumber").html(data.contactNumber);
			$("#title").html(data.title);
			if(data.urgency=="0001") {
				$("#urgency").html("特提");
			}
			if(data.urgency=="0002") {
				$("#urgency").html("特急");
			}
			if(data.urgency=="0003") {
				$("#urgency").html("加急");
			}
			if(data.urgency=="0004") {
				$("#urgency").html("平级");
			}
			
			if(flag=="0001") {
				$("#replayBtn").hide()
				var table = $("<table width='100%' class='table_1'><tr><td colspan='4'>分发单位回执</td></tr><tr><td>序号</td><td>单位</td><td>状态</td><td>回复</td><tr></table>");
				$("#orgReplayTd").append(table)
				var datas = data.sendOrgs
				for(var i=0; i<datas.length; i++) {
					var tdStr = "";
					if(datas[i].id != null) {
						if(datas[i].pdfUrl == null) {
							tdStr = "确认收到"
						}  else {
							tdStr = "<a target='_blank' href='show_consultation.html?fromPage=0002&id="+datas[i].id+"' style='color:blue'>查看回复</a>";
						}
						
					}
					var tr = $("<tr><td align='center'>"+(parseInt(i)+1)+"</td><td>"+datas[i].orgName+"</td><td>"+datas[i].status+"</td><td>"+tdStr+"</td></tr>");
					table.append(tr);
				}				
			}

		} else {
			$.alert("获取函文出错。");
		}
		
	})
})

var orgCode = "";
$(function(){
	getCurrenUserInfo(function(data){
		orgCode = $.trim(data.roleCodes);
	})
})
$("#confirmBtn").on("click",function(){
	var dto = {
			orgCode:orgCode,
			title:"确认收函",
			mailId:id,
			type:'0002'
	}
	doJsonRequest("/consultation/doSave",dto,function(data){
		if(data.result) {
			window.location.href=window.location.href="consulation_skip.html?fromPage="+fromPage;
		} else {
			$.alert("回函失败。");
		}
	},{showWaitting:true})	
})

function showPDF(_this){
	$(_this).attr("href",$(_this).attr("theUrl")).attr("target","_blank")
}
	function clostApp() {
		$('#desk .window-container',parent.document).each(function(){
			parent.HROS.window.close($(this).attr('appid'));
		});
		//parent.HROS.copyright.hideP();
	}

if(fromPage=="0002") {
	$("#submitBtn").html("关闭")
	$("#submitBtn").on("click",function(){
		window.close();
	})
	$("#removeTable").remove();
	$("#replayBtn").hide();
} else if(fromPage=="pc"){
	$("#submitBtn").html("关闭")
	$("#submitBtn").on("click",function(){
		clostApp();
	});
	$("#confirmBtn").removeClass("hidden");

}else {
	$("#submitBtn").on("click",function(){
		goBack();
	})
}


function doSend() {
	
	var mailAttachmentDTOs = new Array();
	$("input[name='attachmentId']", $("#attachmentDiv")).each(function(){
		var obj = {
			id:$(this).val()
		}
		mailAttachmentDTOs.push(obj);
	});
	
	if(mailAttachmentDTOs.length <= 0) {
		$.alert("必须上传函件。");
		return false;
	} 
	wf_doSearchRole(function(data){
		var attachmentDTOs = new Array();
		$("input[name='attachmentId']", $("#attachmentDiv1")).each(function(){
			var obj = {
				id:$(this).val()
			}
			attachmentDTOs.push(obj);
		});
		var dto = {
				contactPerson:$("#contactPerson").val(),
				contactNumber:$("#contactNumber").val(),
				orgCode:$("#createOrgCode").val(),
				title:$("#title").val(),
				mark:$("#mark").val(),
				number:$("#number").val(),
				roleCodes:data.roleCode,
				completeDate:data.completeDate,
				mailAttachmentDTOs:mailAttachmentDTOs,
				attachmentDTOs:attachmentDTOs
		}
		
		doJsonRequest("/consultation/doSave",dto,function(data){
			if(data.result) {
				window.location.href="drafting_consultation.html";
			} else {
				$.alert("发送失败。");
			}
		},{showWaitting:true})		
	})
}

function wf_doSearchRole(fun) {
	var str = "checkType=multi";
	var roleCodes = "035,036"
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
}