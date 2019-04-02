$("#supersumMonth").change(function(){
	$(this).hide();
	$("#supersumMonthSpan").html($(this).children('option:selected').text());
});

$("#supersumMonthSpan").dblclick(function(){
	$(this).html('');
	$("#supersumMonth").show();
});

$(function(){
	//初始化流程按钮
	wf_getOperator("superviseSumList",function(data){
	});
	
	doJsonRequest("/super/getSuperSumList",null,function(data){
		var data = data.data;
		if(data!=null && data!=''){
			$.each(data,function(n,value){
				var files = value.files;
				var filesDom = "";
				if(files!=null){
					$.each(files,function(i,file){
						filesDom+="<span name='"+file.id+"'><a href='"+file.pdfUrl+"' target='_blank' class='print'>"+(i+1)+"："+file.name+"</a><br><button onclick='delFile(this)' style='align:right'>删除</button></span><br>";
					});
				}
				var $_tr = $('<tr><td style="width:5%;min-height:40px;">'+(n+1)+'</td>'
					+ '<td style="width:5%;">'+(value.issue == null ? '': value.issue)+'</td>'
					+ '<td class="cont" onclick="content(this)" style="width:15%;text-align:left;padding:5px 5px 5px 5px;cursor:pointer;" title="点击进行编辑">'+(value.content == null ? '': value.content)+'</td>'
					+ '<td style="width:15%;text-align:left;padding:5px 5px 5px 5px ">'+(value.szOpinion == null ? '': value.szOpinion)+'</td>'
					+ '<td style="width:15%;text-align:left;padding:5px 5px 5px 5px ">'+(value.fszOpinion == null ? '': value.fszOpinion)+'</td>'
					+ '<td style="width:6%;">'+(value.units == null ? '': value.units)+'</td>'
					+ '<td class="unitOp" onclick="unitOpinion(this)" style="width:18%;text-align:left;padding:5px 5px 5px 5px;cursor:pointer;" title="点击进行编辑">'+(value.unitOpinion == null ? '': value.unitOpinion)+'</td>'
					+ '<td style="width:9%;text-align:right;padding:5px 5px 5px 5px ">'+filesDom+'</td>'
					+ '<td class="rem" onclick="remark(this)" style="width:12%;text-align:left;padding:5px 5px 5px 5px;cursor:pointer;" title="点击进行编辑">'+(value.remark == null ? '': value.remark)+'</td></tr>');
				$_tr.data('d',value).appendTo($("#mainTable"));
			})
			
		}
		
	});
	
	$("#sendDate").parent().children('span').hide();
	$("#sendDate").click(function(){
		$("#sendDate").parent().children('span').trigger('click');
	});
});

//删除附件
function delFile(dom){
	$(dom).parent().remove();
}

function getAddData(){
	var superviseDTOs = new Array();
	$("#mainTable tr").each(function(){
		if($(this).data('d')!=null && $(this).data('d')!=''){
			var dto = {};
			dto.id=$(this).data('d').id;
			dto.sort = $(this).children().first('td').html();
			var shortUnitOp = "";
			if(typeof($(this).contents().find('.unitOpinion').val()) != "undefined"){
				shortUnitOp = $(this).contents().find('.unitOpinion').val();
			}else{
				shortUnitOp = $(this).children('.unitOp').html();
			}
			dto.shortUnitOp = shortUnitOp;
			
			var shortContent = "";
			if(typeof($(this).contents().find('.content').val()) != "undefined"){
				shortContent = $(this).contents().find('.content').val();
			}else{
				shortContent = $(this).children('.cont').html();
			}
			dto.shortContent = shortContent;
			
			var shortRemark = "";
			if(typeof($(this).contents().find('.remark').val()) != "undefined"){
				shortRemark = $(this).contents().find('.remark').val();
			}else{
				shortRemark = $(this).children('.rem').html();
			}
			dto.shortRemark = shortRemark;
			
			
			var attrIds = "";
			$(this).contents().find('span').each(function(){
				attrIds +=$(this).attr("name")+",";
			})
			dto.attrIds = attrIds;
			superviseDTOs.push(dto);
		}
	})
	var dto = {
		title:'《政府工作动态》（'+$("#supersumMonthSpan").html()+'月份）领导批示落实情况汇总表（已落实）',
		sendDate:$("#sendDate").val(),
		superviseDTOs:superviseDTOs,
		status:'0002',
		flag:0
	};
	return dto;
}

function goSuccess(data) {
	var roleName = data.assignRoleName;
	window.location.href="../tododocs/docs_down.html?roleName="+roleName+"&fromPage=0001&btnSource=app";
}

function unitOpinion(dom){
	if(!$(dom).is(":has(textarea)")){
		var height = $(dom).height();
		var cont = $(dom).html();
		//将<br>替换为\n
		cont = cont.replace(/<br>/g,'\n');
		$(dom).empty().append("<textarea class='unitOpinion' style='width:100%;height:"+height+"px;overflow-y:hidden'>"+cont+"</textarea>");
	}
};

function content(dom){
	if(!$(dom).is(":has(textarea)")){
		var height = $(dom).height();
		var cont = $(dom).html();
		//将<br>替换为\n
		cont = cont.replace(/<br>/g,'\n');
		$(dom).empty().append("<textarea class='content' style='width:100%;height:"+height+"px;overflow-y:hidden'>"+cont+"</textarea>");
	}
};

function remark(dom){
	if(!$(dom).is(":has(textarea)")){
		var height = $(dom).height();
		var cont = $(dom).html();
		//将<br>替换为\n
		cont = cont.replace(/<br>/g,'\n');
		$(dom).empty().append("<textarea class='remark' style='width:100%;height:"+height+"px;overflow-y:hidden'>"+cont+"</textarea>");
	}
};

$("#sendDate").datepicker({
	showSecond: false, //显示秒
	timeFormat: 'HH:mm',//格式化时间
	stepHour: 1,//设置步长
	stepMinute: 5,
	stepSecond: 10,
	dateFormat:"yy-m-d",
	currentText:'现在',
	closeText:'确定',
	hourMax:'23',
	hourText:'时',
	minuteText:'分',
	secondText:'秒',
	timeText:'时间',
	buttonText:"选择",
	buttonClass:"btn_click"
});

//校验
function wf_beforeValid(){
	if($("#sendDate").val()==''){
		$.alert('请输入时间。');
		return false;
	}
	if($('#supersumMonthSpan').html()==''){
		$.alert('请选择月份。');
		return false;
	}
	return true;
}