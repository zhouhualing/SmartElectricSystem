


$(function () {
	var id = getURLParam("id");
	var userId = getURLParam("userId");
	 var dto = {'id': id + ""};
	    doRequest('/spmsAccountBill/getInfo', dto, function (data) {
	    	var resultData = data.data;
	    	//$.alert($.toJSON(resultData))
	    	var userData = resultData.spmsUserDTO;
	    	var productMFeeList = resultData.spmdList;
	    	$("#name").text(userData.fullname);
	    	$("#mobile").text(userData.mobile);
	    	$("#billDate").text(new Date(resultData.billDate).format("yyyy-MM-dd"));
	    	$("#billCycle").text(resultData.billCycle);
	    	$("#countMonthRMB").text(resultData.countMonthRMB);
	    	$("#printDate").text(resultData.printDate);
	    	$("#accountMonthRest").text(resultData.payFee);
	    	$("#payFee").text();
	    	$("#monthCredit").text(resultData.monthCredit);
	    	/*产品费用赋值*/
	    	$("#accountMonthRest").text(resultData.accountMonthRest);
	    	$("#payFee").text(resultData.accountMonthRest>=0?0:Math.abs(resultData.accountMonthRest));
	    	$("#monthCredit").text(resultData.monthCredit);
	    	var appendList = "";
	    	for(var i=0;i<productMFeeList.length;i++){
	    		var productMFee = productMFeeList[i].productFee;
	    		var productTypeName = productMFeeList[i].prodductTypeName;
	    		var elecFee = productMFeeList[i].elecFee;
	    		appendList +="<div class=\"control-group\">"+
								"<label class=\"control-label\" style=\"width:230px\">套餐名称：</label>"+
								"<div class=\"controls\">"+
									"<div id=\"productTypeName\" class=\"input_view\">" + productTypeName + "</div>"+
								"</div>"+
							  "</div>";
	    		appendList +="<div class=\"control-group\">"+
	    		"<label class=\"control-label\" style=\"width:230px\">套餐费用：</label>"+
	    		"<div class=\"controls\">"+
	    		"<div id=\"productMFee\" class=\"input_view\">" + productMFee + "</div>"+
	    		"</div>"+
	    		"</div>";
	    		appendList +="<div class=\"control-group\">"+
	    		"<label class=\"control-label\" style=\"width:230px\">用电费用：</label>"+
	    		"<div class=\"controls\">"+
	    		"<div id=\"elecFee\" class=\"input_view\">" + (elecFee == null ? "" : elecFee) + "</div>"+
	    		"</div>"+
	    		"</div>";
	    	}
	    	$("#productFee").append(appendList);
	    });
	    
})
function detailFunction(){
	window.localtion.href="accountBillDetail.html?id="+getURLParam("id")+"&userId="+getURLParam("userId");
}