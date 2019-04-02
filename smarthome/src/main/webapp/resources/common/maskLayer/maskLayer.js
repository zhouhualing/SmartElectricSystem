//$(document).ready(function() {
//	$('body').append('<div id="loadingMaskLayerDiv" style="filter:alpha(opacity=50);display:none;width:100%;height:100%;z-index:1000;background-color:gray;opacity:0.5;position:fixed;left:0px;top:0px;">'+
//						'<div class="loading"><i></i><span id="windowMaskLayerInfo"></span></div>'+
//						'</div>');
//});
///**
// * 显示出页面遮罩层
// * @param info 显示信息
// */
//function loadingMaskLayer(info){
//	if(!info)
//		info = '请稍等！';
//	$('body').find('#windowMaskLayerInfo').text(info);
//	$('body').find('#loadingMaskLayerDiv').show(500);
//}
///**
// * 结束页面遮罩层
// * @param info 显示信息
// */
//function overMaskLayer(info){
//	if(!info)
//		info = '请稍等！';
//	$('body').find('#windowMaskLayerInfo').text(info);
//	$('body').find('#loadingMaskLayerDiv').delay(1500).hide(500);
//}