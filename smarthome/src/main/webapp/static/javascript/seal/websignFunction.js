var WebObj = document.getElementById("DWebSignSeal");//盖章控件
var sealSvrUrl ="";//印章服务器地址
//各个区域已经盖章数目
var szsize = 0;
var fssize = 0;
var mssize = 0;
var fmsize = 0;
var fzsize = 0;
var gksize = 0;
var qtsize = 0;


/***********************************************
说明：
    OnLoad 初始化，设置服务器路径
***********************************************/
function setSealSvr(){
	 doJsonRequest("/seal/getSealSvrUrl",{},function(data){
	    	if(data.result) {
				var data = data.data;
				if(data=="faile"){
					alert("获取印章服务器地址失败");
				}else{
					sealSvrUrl=data;
				}
			}
	    });
	//网络版服务器路径
	WebObj.HttpAddress = "http://"+sealSvrUrl+"/inc/seal_interface/";
	//网络版的唯一页面ID ，SessionID
	WebObj.RemoteID = "0100018";
}

 /***********************************************
说明：
    loadSeal 加载印章数据到当前页面显示
***********************************************/	
function loadSeal(businessId,businessType){
	var dto = {"businessId":businessId,"businessType":businessType};
	doJsonRequest("/seal/getInfo",dto,function(data){
		if(data.result){
			var i=0;
			var info = data.data;
			if(info!=null){
				if(info.SZ!=null){
					szsize = info.SZ.length;
					for(i=0;i<szsize;i++){
						$("#szoption").append('<li><span>'+info.SZ[i].text+'</span><div id ="pos'+info.SZ[i].sealName+'"></div><img class ="sealImg" src="" /></li><div style="clear:both;"></div>');
						WebObj.SetStoreData(info.SZ[i].code);
						WebObj.ShowWebSeals();
						WebObj.SetSealSignData(info.SZ[i].sealName,info.SZ[i].text);
					}
				}
				if(info.FS!=null){
					fssize = info.FS.length;
					for(i=0;i<fssize;i++){
						$("#fsoption").append('<li><span>'+info.FS[i].text+'</span><div id ="pos'+info.FS[i].sealName+'"></div><img class ="sealImg" src="" /></li><div style="clear:both;"></div>');
						WebObj.SetStoreData(info.FS[i].code);
						WebObj.ShowWebSeals();
						WebObj.SetSealSignData(info.FS[i].sealName,info.FS[i].text);
					}
				}
				if(info.MS!=null){
					mssize = info.MS.length;
					for(i=0;i<mssize;i++){
						$("#msoption").append('<li><span>'+info.MS[i].text+'</span><div id ="pos'+info.MS[i].sealName+'"></div><img class ="sealImg" src="" /></li><div style="clear:both;"></div>');
						WebObj.SetStoreData(info.MS[i].code);
						WebObj.ShowWebSeals();
						WebObj.SetSealSignData(info.MS[i].sealName,info.MS[i].text);
					}
				}
				if(info.FM!=null){
					fmsize = info.FM.length;
					for(i=0;i<fmsize;i++){
						$("#fmoption").append('<li><span>'+info.FM[i].text+'</span><div id ="pos'+info.FM[i].sealName+'"></div><img class ="sealImg" src="" /></li><div style="clear:both;"></div>');
						WebObj.SetStoreData(info.FM[i].code);
						WebObj.ShowWebSeals();
						WebObj.SetSealSignData(info.FM[i].sealName,info.FM[i].text);
					}
				}
				if(info.FZ!=null){
					fzsize = info.FZ.length;
					for(i=0;i<fzsize;i++){
						$("#fzoption").append('<li><span>'+info.FZ[i].text+'</span><div id ="pos'+info.FZ[i].sealName+'"></div><img class ="sealImg" src="" /></li><div style="clear:both;"></div>');
						WebObj.SetStoreData(info.FZ[i].code);
						WebObj.ShowWebSeals();
						WebObj.SetSealSignData(info.FZ[i].sealName,info.FZ[i].text);
					}
				}
				if(info.QT!=null){
					qtsize = info.QT.length;
					for(i=0;i<qtsize;i++){
						$("#qtoption").append('<li><span>'+info.QT[i].text+'</span><div id ="pos'+info.QT[i].sealName+'"></div><img class ="sealImg" src="" /></li><div style="clear:both;"></div>');
						WebObj.SetStoreData(info.QT[i].code);
						WebObj.ShowWebSeals();
						WebObj.SetSealSignData(info.QT[i].sealName,info.QT[i].text);
					}
				}
				if(info.GK!=null){
					gksize = info.GK.length;
					for(i=0;i<gksize;i++){
						$("#gkoption").append('<li><span>'+info.GK[i].text+'</span><div id ="pos'+info.GK[i].sealName+'"></div><img class ="sealImg" src="" /></li><div style="clear:both;"></div>');
						WebObj.SetStoreData(info.GK[i].code);
						WebObj.ShowWebSeals();
						WebObj.SetSealSignData(info.GK[i].sealName,info.GK[i].text);
					}
				}
			}
		}
	});
}

/***********************************************
说明：
    AddSeal 添加印章并保存数据
参数说明：
	businessId 业务ID
	businessType 业务类型
    sealName 印章名
    sealText 印章绑定的内容
    area 盖章区域（SZ市长，FS副市长，MS秘书长，FM副秘书长，FZ法制办，GK信息公开审核，QT其他）
返回数据：
    true or false
***********************************************/
function AddSeal(businessId,businessType,sealName,sealText,area){
		try{
			//是否已经盖章
			var strObjectName;
			strObjectName = WebObj.FindSeal("",0);
			while(strObjectName  != ""){
				if(sealName == strObjectName){
					alert("当前页面已经加盖过印章：【"+sealName+"】，请核实！");
					return false;
				}
				strObjectName = WebObj.FindSeal(strObjectName,0);
			}
			//清空原绑定内容	
			WebObj.SetSignData("-");
			//印章绑定数据
			WebObj.SetSignData("+DATA:"+sealText);
			//固定印章的位置(pos+sealName)
			WebObj.SetPosition(0,0,"pos"+sealName);
			//调用盖章的接口
			var sealid=WebObj.AddSeal("", sealName);
			//印章加密数据，需要保存到数据库
			var security = WebObj.GetStoreDataEx(sealid);
			WebObj.LockSealPosition(sealid);
			//往数据库保存
			var dto = {"businessId":businessId,
					   "businessType":businessType,
					   "sealName":sealid,
					   "text":sealText,
					   "code":security,
					   "area":area
			};
			doJsonRequest("/seal/saveInfo",dto,function(data){
				if(data.result=true){
					var data = data.data;
					if(data==null||data!="success"){
						alert("保存印章数据失败");
						return false;
					}
				}
			});
			return true;
		}catch(e) {
			alert(sealUserName+"签名失败：" +e);
			return false;
		}
}

/***********************************************
说明：
    deleteSeal 删除印章
***********************************************/
function deleteSeal(){
}
//data:image/png;base64,i


/***********************************************
说明：
    打印前调用
***********************************************/
function beforePrint(){
	$(".sealImg").show();
	var strObjectName ;
	strObjectName = WebObj.FindSeal("",0);
	while(strObjectName  != ""){
		var vTempPath;
		//获取临时文件的路径
		vTempPath = WebObj.GetTempFileName() ;
		//把第一个印章保存到临时文件中
		WebObj.GetSealBP(strObjectName,vTempPath);
		$('#pos'+strObjectName).next().attr("src",vTempPath);
		WebObj.DelLocalFile(vTempPath);
		strObjectName = WebObj.FindSeal(strObjectName,0);
	}
}


/***********************************************
说明：
    打印后调用
***********************************************/
function afterPrint(){
	$(".sealImg").hide();
}
