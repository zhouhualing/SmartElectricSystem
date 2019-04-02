var selectCount = getURLParam("selectCount");//0001/0002
function doCallBack() {
	var arrs = clickmedTables.orgTargetDiv.selectObjs;
	
	var roleCodes = new Array();
	var roleNames = new Array();
	
	if(arrs.length <= 0){
		$.alert("请选择一个角色");
		return false;
	}
	if(selectCount == '0001' && arrs.length > 1){
		$.alert("只能选择一个角色");
		return false;
	}
	if(arrs.length==1){
		var roleDTO =  {
				roleCode:arrs[0].roleCode,
				roleName:arrs[0].roleName
			};
		return roleDTO;
	}else{
		for(var i = 0;i<arrs.length;i++){
			roleCodes.push(arrs[i].roleCode);
			roleNames.push(arrs[i].roleName);
		}
		var roleDTO =  {
				roleCode:roleCodes,
				roleName:roleNames
			};
		return roleDTO;
	}
	
}
window.onload = function(){
	doQuery("orgTargetDiv");
};