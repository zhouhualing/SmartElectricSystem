var messageType = getURLParam("messageType");
var mailMessage = getURLParam("mailMessage");
$(function(){
	$("[name='messageType']").each(function(){
		if($(this).val() == messageType) {
			$(this).attr("checked","");
			return false;
		}
	})
	
	$("#mailMessage").val(mailMessage).trigger("keyup");
})

function doCallBack() {
	var data = {
			messageType:$("[name='messageType']:checked").val(),
			mailMessage:$("#mailMessage").val()
	}
	return data;
}

