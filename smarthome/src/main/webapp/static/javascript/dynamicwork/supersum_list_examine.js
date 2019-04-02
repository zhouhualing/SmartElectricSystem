var id = getURLParam("id"); //当前收文id
$("#id").val(id);
var taskId = getURLParam("taskId");
var fromPage = getURLParam("fromPage");
var btnSource = "app";
var appFlag = getURLParam("appFlag");
if(fromPage == 'pc' && appFlag != 1){
    btnSource = fromPage;
}
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
                        + '<td style="width:18%;text-align:left;padding:5px 5px 5px 5px ">'+(value.shortUnitOp == null ? '': value.shortUnitOp)+'</td>'
                        + '<td style="width:9%;text-align:right;padding:5px 5px 5px 5px ">'+filesDom+'</td>'
                        + '<td style="width:7%;text-align:left;padding:5px 5px 5px 5px ">'+(value.shortRemark == null ? '': value.shortRemark)+'</td>'
                        + '<td style="width:10%;text-align:left;padding:5px 5px 5px 5px "><span><button type="button" onclick="initSuperviseCard('+value.id+','+value.issue+')" class="btn btn_click '+(value.isRevive == '0002' ? 'hidden':'')+'">生成批示单</button></span></td></tr>');
                    $_tr.data('d',value).appendTo($("#mainTable"));
                })
            }
        });

        var dto = {taskId:taskId}
        //初始化流程按钮
        wf_getOperator(dto,function(data){
            userTask = data.userTask;//获取当前流程
        });
    }

});

function getAddData(){
    var superviseDTOs = new Array();
    $("#mainTable tr").each(function(){
        if($(this).data('d')!=null && $(this).data('d')!=''){
            var dto = {};
            dto.id=$(this).data('d').id;
            dto.sort = $(this).children().first('td').html();
            superviseDTOs.push(dto);
        }
    })
    var dto = {
        title:'《政府工作动态》（'+$("#supersumMonthSpan").html()+'月份）领导批示落实情况汇总表（已落实）',
        sendDate:new Date(),
        superviseDTOs:superviseDTOs,
        status:'0002',
        flag:0
    };
    return dto;
}

function getStatusData(){
    return {id:id}
}

function goSuccess(data) {
    var roleName = data.assignRoleName;
    window.location.href="../tododocs/docs_down.html?mark=004&roleName="+roleName+"&fromPage=0002&btnSource="+btnSource;
}

function goSuccessEnd(data) {
    window.location.href="../tododocs/docs_down.html?mark=004&fromPage=0002&btnSource="+btnSource+"&flag=1";
}


//生成督办单
function initSuperviseCard(id,issue){
    window.location.href = "supervisecard_sz_draft.html?fromList=s&superId="+id+"&issue="+issue;
}