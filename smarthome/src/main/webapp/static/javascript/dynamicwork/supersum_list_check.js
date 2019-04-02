var id = getURLParam("id"); //当前收文id
$("#id").val(id);
$(function(){
	
	if(id!=null){
		doJsonRequest("/super/getSuperSum",{id:id},function(data){
			var data = data.data;
			$(".moban_title").html(data.title);
			$("#sendDate").html(new Date(data.sendDate).format('yyyy年M月d日'));
			$("#status").val(data.status);
			var dtos = data.superviseDTOs;
			if(dtos!=null && dtos!=''){
				$.each(dtos,function(n,value){
					var files = value.files;
					var filesDom = "";
					if(files!=null){
						$.each(files,function(i,file){
							filesDom+="<span name='"+file.id+"'><a href='"+file.pdfUrl+"' target='_blank'>"+(i+1)+"："+file.name+"</a></span><br>";
						});
					}
					var $_tr = $('<tr><td style="width:5%;min-height:40px;">'+(n+1)+'</td>'
						+ '<td style="width:5%;">'+(value.issue == null ? '': value.issue)+'</td>'
						+ '<td style="width:15%;text-align:left;padding:5px 5px 5px 5px ">'+(value.shortContent == null ? '': value.shortContent)+'</td>'
						+ '<td style="width:15%;text-align:left;padding:5px 5px 5px 5px ">'+(value.szOpinion == null ? '': value.szOpinion)+'</td>'
						+ '<td style="width:15%;text-align:left;padding:5px 5px 5px 5px ">'+(value.fszOpinion == null ? '': value.fszOpinion)+'</td>'
						+ '<td style="width:6%;">'+(value.units == null ? '': value.units)+'</td>'
						+ '<td style="width:15%;text-align:left;padding:5px 5px 5px 5px ">'+(value.shortUnitOp == null ? '': value.shortUnitOp)+'</td>'
						+ '<td style="width:10%;text-align:right;padding:5px 5px 5px 5px ">'+filesDom+'</td>'
						+ '<td style="width:10%;text-align:left;padding:5px 5px 5px 5px ">'+(value.shortRemark == null ? '': value.shortRemark)+'</td></tr>');
					$_tr.data('d',value).appendTo($("#mainTable"));
				})
			}
		});
		
	}
	
});
