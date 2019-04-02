$(function(){
//    GetAppList();
    GetAppType();
})


////获取应用
//function GetAppList(type,page){
//    var params={
//        type:type || 0,
//        page:page || 1
//    }
//    var url=""
//    $.ajax({
//        url:url,
//        type:"post",
//        dateType:'json',
//        data:{
//
//        },
//        success:function(data){
//            var apps=data;
//            var appstemp= $.templates("#AppsTemp");
//            appstemp.link("#apps",{data:apps});
//        },
//        error:function(){
//
//        }
//    })
//}


//获取应用类别
function GetAppType(){
    $.ajax({
       url:"/cmcp/app/getAppType",
        type:"post",
        dataType:"json",
        data:{},
        success:function(data){
            var app_types=data;
            var app_types_temp= $.templates("#AppTypeTemp");
            app_types_temp.link("#apptypes",{datas:app_types});
        }
    })
}


//test data
//apps=[
//    {
//        "app":{
//            "id":1,
//            "img_url":"../../uploads/shortcut/20130505/13677353829381.png",
//            "app_name":"愚人节整同学",
//            "app_description":"游戏中使用鼠标操作，点击道具整人。 如何开始: 输入你想整的人的名字后点击[开始整人]开始游戏"
//        }
//    },
//    {
//        "app":{
//            "id":2,
//            "img_url":"../../uploads/shortcut/20130505/13677353369336.png",
//            "app_name":"部落争霸",
//            "app_description":"鼠标点击发兵。 如何开始: 点击play-点击start game-选择关卡1开始游戏"
//        }
//    },
//    {
//        "app":{
//            "id":3,
//            "img_url":"../../uploads/shortcut/20130505/13677351957517.png",
//            "app_name":"黑猫警长救伙伴",
//            "app_description":"游戏中使用方向键进行推伙伴。 如何开始: 点击【进入游戏】--【开始】进行游戏"
//        }
//    },
//    {
//        "app":{
//            "id":4,
//            "img_url":"../../uploads/shortcut/20130505/1367735129442.png",
//            "app_name":"小刺猬去面试",
//            "app_description":"鼠标点击触发事件，帮助小刺猬解决遇到的问题，继续前行吧！ 如何开始: 游戏加载完成后点击continue - 再点击play即可开始游戏"
//        }
//    },
//    {
//        "app":{
//            "id":5,
//            "img_url":"../../uploads/shortcut/20130505/13677350213580.png",
//            "app_name":"猫和老鼠之万圣之夜",
//            "app_description":"游戏中使用空格键控制老鼠跳跃，不要被猫抓到了哦。 如何开始: 游戏加载完毕点击两次PLAY即可开始游戏"
//        }
//    }
//]
//
//var appstemp= $.templates("#AppsTemp");
//appstemp.link("#apps",{data:apps});
//
//
//app_types=[
//    {
//        "app_type":{
//            "type_id":"1",
//            "type_name":"工具"
//        }
//    },
//    {
//        "app_type":{
//            "type_id":"2",
//            "type_name":"游戏"
//        }
//    }
//]
//var app_types_temp= $.templates("#AppTypeTemp");
//app_types_temp.link("#apptypes",{datas:app_types});
