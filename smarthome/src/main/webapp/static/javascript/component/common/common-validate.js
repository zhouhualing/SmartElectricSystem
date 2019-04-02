$(function(){

	doValideScan();
})

var nowTheValidate=null;
function doValideScan() {
	if($("form[validate]").length > 0) {
		$("form[validate]").each(function(){
			var sendForm = true;
			if($(this).attr("nosubmit")!=undefined) {
				sendForm = false;
			}
			nowTheValidate = $(this).validate({
				onChange : true,
				sendForm:sendForm,
				eachValidField : function() {
					$(this).closest('div').removeClass('has-error').addClass('has-success');
				},
				eachInvalidField : function() {
					$(this).closest('div').removeClass('has-success').addClass('has-error');
				}
			});	
		})
	}
}

function valide( formId) {
	
	$('#'+formId).submit();
	if($(".has-error",$('#'+formId)).length > 0) {
		return false;
	}
	return true;
}

function valideClean(formId) {
	$(".has-error",$('#'+formId)).removeClass("has-error")
}
