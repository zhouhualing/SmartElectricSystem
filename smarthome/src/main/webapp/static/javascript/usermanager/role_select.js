$(document).ready(function(){
	doQuery("orgTargetDiv");
});
function getvalue(){
	var arrs = clickmedTables.orgTargetDiv.selectObjs;
	var roleIds = new Array();
	var roleNames = new Array();
	if(arrs.length>=1){
		for(var i = 0;i<arrs.length;i++){
			roleIds.push(arrs[i].id);
			roleNames.push(arrs[i].roleName);
		}
		var Ids = roleIds.join(',');
		var Names = roleNames.join('，');
		$.dialog.data('roleIds',Ids);
		$.dialog.data('roleNames',Names);
	}else{
		$.dialog.data('roleIds',null);
		$.dialog.data('roleNames',null);
	}
}
