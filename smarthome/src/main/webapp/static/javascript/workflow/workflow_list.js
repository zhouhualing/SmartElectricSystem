$(function(){
	doQuery();
})

function rowClick(data) {
	window.location.href="workflow_config.html?processDefinedId="+data.ID_;
}