var showObj = {
    text:"查看",
    fun:function(data) {
        goPage(data.businessId,data.businessType);

    }
};

var deleObj = {
    text:"去除",
    fun:function(data) {
        $.confirm({
                title: '提示信息',
                msg: '确定要去除么？',
                height: 180,
                confirmBtn: true,
                cancelBtn: true,
                closeBtn: false,
                confirmClick: function(){
                    var dto = {
                        id:data.id
                    }
                    doJsonRequest("/meet/meetContent/doDeleteMeetContentDoc",dto,function(data){
                        doQuery();
                        if(data.result) {
                            $.alert("去除成功。");
                        } else {
                            $.alert("去除失败。");
                        }
                    },{showWaitting:true})
                }
        })
    }
};

$(function(){
    doQuery();
})

var oppObj = [showObj,deleObj];

/**
 * do link to createurl.
 */
function createMeetContentLib() {
    window.location.href="meet_content_lib_create.html"
}

function initFun(data, key) {

    if(key =="businessTitle") {
        return "<div onclick ='goPage("+data.businessId+","+data.businessType+")' title='"+data[key]+"'>"+data[key]+"</div>";
    }

}
function goPage(businessId,type){
    if(type == "0001" || type == "0002"  || type == "0005"  || type == "0006" ){
        //发文、发函
        window.location.href="../senddoc/new_fwfh_check.html?id="+businessId+"&type="+type+"&showCard=1";
    }else if(type == "0003"){
        //发电
        window.location.href="../telegram/new_fd_check.html?id="+businessId+"&type="+type;
    }else if(type == "0009"){
        //拟发文
        window.location.href="../senddoc/new_nfw_check.html?id="+businessId+"&type="+type+"&showCard=1";
    }else if(type == "5001"){
        //上级来文
        window.location.href="../receivedoc/new_sjlw_check.html?id="+businessId+"&type="+type;
    }else if(type == "5002"){
        //下级来文
        window.location.href="../receivedoc/new_xjlw_check.html?id="+businessId+"&type="+type+"&checkFlag=1";
    }else if(type == "5003"){
        //请示报告
        window.location.href="../report/new_reqrep_check.html?id="+businessId+"&type="+type+"&checkFlag=1";
    }else if(type == "6001"){
        //来电
        window.location.href="../telegram/new_sd_check.html?id="+businessId+"&type="+type;
    }else if(type == "7001"){
        //委办局函文
        window.location.href="../letter/bureaus_letter_read.html?id="+businessId+"&type="+type;
    }
}
