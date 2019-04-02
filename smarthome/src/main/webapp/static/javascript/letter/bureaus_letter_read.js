var id = getURLParam("id");
var fromPage = getURLParam("fromPage"); //页面来源
var type = getURLParam("type"); //页面来源
var taskId = getURLParam("taskId"); 
var userTask = "";
$("#reportType").val(type);
$("#businessKey").val(id);
//打开时默认显示函件:
$(".nav-tabs a:first").tab("show");
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

//回复
/*$("#replayBtn").on("click", function(){
	window.location.href="drafting_replayconsultation.html?fromPage="+fromPage+"&id="+id;
})*/


$(function(){
	getCurrenUserInfo(function(data){
		$("#userCode").val(data.userCode);
		$("#userOrg").val(data.orgName);
		$("#userName").val(data.userName);
		$("#redHeadTemp").val(data.redHeadTemp);
	});
	
	/*if(flag=="0001") {
		$("#replayTd").show();
	}
	$("#createDate").html(new Date().format("yyyy-MM-dd"))*/
	var dto = {
		id:id
	}
	$('#dataInputForm').addClass('fileupload-processing');
	doJsonRequest("/burLetter/getBurLetter", dto, function(data){
		if(data.result) {
			var data = data.data;
			var replyLetters = data.replyLetters;
			$("#mailFrame").attr("src","/cmcp"+data.text);
			$("#editmain_left").append('<iframe name="weboffice" src="../weboffice/webpdf.html?officeUrl=' + data.text+'"style="width:100%;height:800px;"></iframe>');
			$('#dataInputForm').fileupload('option', 'done').call($('#dataInputForm'), $.Event('done'), {result: data});
			$('#dataInputForm').removeClass('fileupload-processing');
			$("[id='file_removeBtn']").remove();
			$(".name","#attachmentDiv").children("a").attr("onclick","showPDF(this)")
			
			$("#contactPerson").html(data.contactPerson);
			$("#createOrgCode").val(data.orgName);
			$("#createOrgName").html(data.orgName);
			$("#contactNumber").html(data.contactNumber);
			$("#reportTitle").html(data.reportTitle);
			$("#text").val(data.text);
			//letterType:'0001',
			$("#urgencySelect").val(data.urgency);
			$("#urgency").html($("#urgencySelect").find("option:selected").text());
			$("#reportType").val(data.reportType);
			
			doQueryWF("reportInfo","approvalDiv");
			//发起委局
			var table = $("<table width='100%' class='table_1'><tr><td colspan='3'>分发单位回执</td></tr><tr><td>序号</td><td>单位</td><td>回复</td><tr></table>");
			$("#orgReplayTd").append(table);
			var datas = replyLetters;
			for(var i=0; i<datas.length; i++) {
				var tdStr = "";
				if(datas[i].id != null) {
					if(datas[i].text == null) {
						tdStr = "确认收到"
					}  else {
						tdStr = "<a target='_blank' href='bureaus_letter_check.html?fromPage=0002&id="+datas[i].id+"' style='line-height:4px;color:blue;'>查看回复</a>";
					}
					
				}
				var tr = $("<tr><td align='center'>"+(parseInt(i)+1)+"</td><td>"+datas[i].orgName+"</td><td>"+tdStr+"</td></tr>");
				table.append(tr);
			}	
		} else {
			$.alert("获取函文出错。");
		}
		
	})
})

//-----------------委办局函文，以上已经js完成------------------------------------------------


function showPDF(_this){
	$(_this).attr("href",$(_this).attr("theUrl")).attr("target","_blank")
}