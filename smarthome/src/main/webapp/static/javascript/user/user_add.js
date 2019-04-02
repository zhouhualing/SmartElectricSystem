var getAllRole = null;
var roleIds = null;
$("#btnSubmit").on("click",function(){
	if($("#password").val() != ""){
		if($("#confirmPassword").val() == ""){
			$.alert("请确认密码");
			return false;
		}
		if($("#confirmPassword").val() != $("#password").val()){
			$.alert("两次所输入的密码不一致");
			return false;
		}
	}
    if($("#inputForm").valid()) {
        var dto = {
            userCode:$("#userCode").val(),
            userName:$("#userName").val(),
            mobilePhone:$("#mobilePhone").val(),
            password:$("#password").val(),
            email:$("#email").val(),
            mark:$("#mark").val(),
            roleIds:roleIds
        }
    //    console.log(dto);
        doJsonRequest("/user/addUser",dto,function(data){
            if(data.result) {
                if(data.data.message != null) {
                    $.alert(data.data.message.errorInfo);
                } else {
                    window.location.href="user_list.html";
                }
            } else {

            }
        },{showWaiting:true})
    }
})