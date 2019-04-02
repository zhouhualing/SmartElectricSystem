var ids = getURLParam("ids");
var nids = getURLParam("nids");

if (nids) {
	$('#orgTargetDiv').attr('queryId','permissionNotInQuery');
}

function doCallBack() {
	var rows = clickmedTables.orgTargetDiv.datas;
	var selectIds = clickmedTables.orgTargetDiv.selectIds;
	return $.grep(rows,function(n,i){
		for (var j=0;j<selectIds.length;j++) {
			if (n.id==selectIds[j]) {
				return true;
			}
		}
		return false;
	});
}

window.onload=function(){
	if (ids) {
		$('#id').val(ids);
	} else if (nids) {
		$('#id').val(nids);
	}
	doQuery("orgTargetDiv");
};