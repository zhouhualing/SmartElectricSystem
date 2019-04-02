//产品类型查看页面 获取数据方法

$(function(){
	
	var dto = {
			id:getURLParam("id")
	};
	
	$("#basicEdit").on("click", function () {
	    $("#bizAreaButton").show();
	})

	$("#basicCancel").on("click", function () {
	    $("#bizAreaButton").hide();
	})

	$("#bizAreaButton").on("click", function () {
	    C_doAreaSelect({
	        params: {
	            classification: 1
	        },
	        myCallBack: function (data) {
	            $("#bizAreaParentId").val(data.id);
	            $("#bizAreaName").val(data.name);
	            // doQuery();
	        }
	    })
	})
	
	doJsonRequest("/spmsProductType/doQuery",dto,function(data){
		
		if(data.result) {
			var data = data.data;
			var dataKey = [];
			for(var key in data) {
				dataKey.push(key+"S");
			}
			$("div",$("fieldset")).each(function(){
				if($.inArray($(this).attr("id"),dataKey) != -1) {
					var nowFieldId = $(this).attr("id").substring(0,$(this).attr("id").length-1);
					if($("#"+nowFieldId).is(".the_select2")) {
						$(this).html(data[(nowFieldId+"Text")]);
						$(this).attr("eValue",data[nowFieldId]);
						$("#"+nowFieldId).select2('val',1);
					} else {
						$(this).html(data[nowFieldId]);
						$('#bizAreaParentId').val(data['areaId']);
					}
				}
			})
			$("#id").val(data.id)
		} else {
			$.alert("信息获取失败");
		}
	})
	
})


function doEdit() {

    finishEditText($(".edit_complete")[0], 'basic', '/user/editUser');


}
//产品类型查看页面修改方法
function sava() {
    $("#inputForm").submit();
}

$("#inputForm").validate({submitHandler:function(){
		var flag= true;
		$(".temp").each(function(){
			if($(this).val()<15 || $(this).val()>35){
			flag =false;
			}
		});
		if(!flag){
			$.alert("温度在15-35℃之间");
			return;
		}
		if($("#zhiLengMix").val()>$("#zhiLengMax").val()){
			$.alert("制冷最低温度应该小于等于其最高温度");
			return;
		}
		if($("#zhiReMix").val()>$("#zhiReMax").val()){
			$.alert("制热最低温度应该小于等于其最高温度");
			return;
		}
		var dto = {
				id:getURLParam("id"),
				names:$("#names").val(),
				muluId:$("#muluId").val(),
				status:$("#status").val(),
				
				validPeriod:$("#validPeriod").val(),
				areaDTO:{
					id:$("#bizAreaParentId").val()
				},
				describes:$("#describes").val(),
				countRmb:$("#countRmb").val(),
				rmbType:$("#rmbType").val(),
				indexRmb:$("#indexRmb").val(),
				electrovalenceRmb:$("#electrovalenceRmb").val(),
				configurationInformation:$("#configurationInformation").val(),
				//temperatureRange:$("#temperatureRange").val(),
				/*deviceType:$("#deviceType").val(),*/
				 lianDong:$("#lianDong").val(),//传感器联动 0 是 1否
		 	 	 zhiLengMix:$("#zhiLengMix").val(),//制冷最小温度
		 	 	 zhiLengMax:$("#zhiLengMax").val(), //制冷最大温度
		 		 zhiReMix:$("#zhiReMix").val(), //制热最小温度
		 	 	 zhiReMax:$("#zhiReMax").val(),//制热最大温度
		 	 	
		 	 	 kongTiaoCount:$("#kongTiaoCount").val(), //空调最大绑定数
		 	 	 chuangGanCount:$("#chuangGanCount").val()//传感器最大绑定数
				
				}
			doJsonRequest("/spmsProductType/doUpdate",dto,function(data){
				if(data.result) {
					
					window.location.href = "spmsProductTypeList.html";
				} else {
					alert("保存失败")
				}
			})
	}});

//产品类型页面物理删除方法
function deleteDL() {
    var config = {
        msg: "您确定要删除么？",
        confirmClick: confirmClick
    }
    $.confirm(config);
    function confirmClick() {
        var dto = {
            id: getURLParam("id")
        };
        doJsonRequest("/spmsProductType/doDelete", dto, function (data) {
            if (data.result) {
                if (data.data.success) {

                    window.location.href = "spmsProductTypeList.html";
                } else {
                    $.alert(data.data.msg);
                }
            }
        }, {
            showWaiting: true
        });
    }

}









