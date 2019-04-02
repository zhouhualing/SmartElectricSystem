/**
 0001-市政府发文 
 0002-市政府发函 
 0005-办公厅发文 
 0006-办公厅发函 
 */
var type = getURLParam("type");//获取当前发文类型 	 
var id = getURLParam("id"); //当前发文id
var taskId = getURLParam("taskId");
var fromPage = getURLParam("fromPage");
var userTask = "";//当前流程
var ltPage = getURLParam("ltPage"); //小纸条是否可编辑
var lcbz = "";//流程备注信息
var textUrl = "";
var redFlag = false;
var mark = getURLParam("mark");//当前是否是市长秘书点击进来的 002:是 
$("#id").val(id);
$("#businessKey").val(id);
$("#reportType").val(type);
var businessType = getURLParam("businessType");
var roleCode = "";
var roleName = "";
var realRoleCode = null;


var userName = "";

//正文加载webOffice控件
var flag = false;
var allBtn = "revision_final_saveFile";//weboffice按钮控制
var printBtnFlag;
$(function(){
	getCurrenUserInfo(function(data){
		userName = data.userName;
        roleCode = data.roleCodes;
        roleName = data.roleNames;
        realRoleCode = data.realRoleCode;
        $("#userOrg").val(data.orgName);
		$("#userCode").val(data.userCode);
		isMayor = data.isMayor;
		var redHfes = data.redHeadFileEntities;
		for(var i = 0;i<redHfes.length;i++){
			var redHfe = redHfes[i];
			if(type == redHfe.reportType){
				var span = $("<span></span>");
				var btn = $(document.createElement("button")).addClass('btn').addClass('btn_click').attr('name','redBtn').html(redHfe.mark).data('redData',redHfe);
				btn.appendTo(span);
				btn.click(function(){
					//校验 发文卡上的编号、印发日期、印数。
					if($("#reportCode").val() == ''){
						$.alert("请输入编号");
						return false;
					}
                    /*
                    if($("#reportCode").val().length>25){
						$.alert("编号应少-于25字");
						return false;
					}*/
                                    if(redFlag)
                                        return;
                    setTimeout(function(){
                        redFlag=false;
                    },5000)
						var thisRed = $(this).data('redData');
						//读取模板文件套红(URL:文件路径;endUrl:尾部套红路径；code:套红编号;ccuser;套红抄送人;company:发文单位(ZF政府、BGT办公厅);)
						var URL = baseRedHeadUrl+thisRed.redHeadTemp;
						var endUrl = baseRedHeadUrl+thisRed.endTemp;
						weboffice.window.insertRedHeadFromUrl(URL,endUrl,'001','','');
                                                redFlag =true;
					//	$("button[name='redBtn']").hide();
				});
				span.appendTo($("#buttonList"));
			}
		}
		
		$("#textPage").click(function() {
		    if (!flag) {
		        $("#textIframe").append('<iframe name="weboffice" src="../weboffice/weboffice.html?officeUrl=' + textUrl +'&allBtn='+allBtn+'&textId='+$("#textId").val()+ '&taskid=' + id + '&shAttBtn=1&showPrintBtn='+$("#status").val()+'&&userName='+userName+'"style="width:100%;height:800px;"></iframe>');
		        flag = true;
		    }
		});
	})
	
	if(id != null) {
		$("#id").val(id);
		$("#businessId").val(id);
		$("#backBtn").removeClass("hidden");
		var dto = {
				id:id
		};
		doJsonRequest("/senddoc/getReport",dto,function(data){
                 //   console.log(data);
			if(data.result) {
				var data = data.data;
				if(data.nfwId != null) {
					$("#businessKey1").val(data.nfwId);
					initNfwInfoA(data.nfwId);
					doQueryWF("reportInfo1","approvalDiv1");
				} else {
					$("#hiddenDiv1").hide();
				}
				//属性
				$("#openType").val(data.openType);
				$("#spanOpenType").html($("#openType").find("option:selected").text());
				$("#fileAttr").html($("#openType").find("option:selected").text());//属性与是否公开的选项一致
				
				//标题
				$("#reportTitle").val(data.reportTitle);
				$("#wjbt").html(data.reportTitle);
				//发送单位
				$("#sendUnit").val(data.sendUnit);
				//密级
				$("input[name='secretLevel'][value='"+data.secretLevel+"']").attr("checked",'checked');
				//紧急程度
				$("#urgency").val(data.urgency);
				//编号
				$("#reportCode").val(data.reportCode);
				$("[name='secCode']").html(data.reportCode);
				//日期
                            
				if(data.reportDate!=null){
					$("#reportDate").html(new Date(data.reportDate).format('yyyy年M月d日'));
				}
				if(data.auditTime!=null){
					$("#auditTime").html(new Date(data.auditTime).format('yyyy年M月d日'));
				}
				//经办人
				$("#attnPerson").val(data.attnPerson+":"+data.fromNumber);
				$("#jbr").html(data.attnPerson);
				$("#lxdh").html(data.fromNumber);
				
				//经办部门
				$("#fromOrgName").html(data.fromOrgName);
				$("#orgName").html(data.fromOrgName);
				$("#bmks").html(data.fromOrgName);
				//公开与否说明
				var openTypeReason = data.openTypeReason;
				if(openTypeReason!=''){
					openTypeReason = openTypeReason.replace("<br>","");
				}
				$("#openTypeReason").html(openTypeReason),
				//$("#proposedAdvice").val(data.proposedAdvice),
				$("#proposedAdviceSPAN").html(data.proposedAdvice);
				//审批单位意见日期:
				/*$("#proposedDate").html(data.proposedDate);*/
				//备注
				$("#remarks").val(data.remarks);
				textUrl = data.text;
				$("#text").val(data.text);
				$("#textId").val(data.textId);
				//小纸条
				/*$("#liPage").val(data.littlePage);*/
				$("#lPageSpan").html(data.littlePage);
				
				//$("#hgOpinion").val(data.hgOpinion);
				if(data.hgOpinion == '' || data.hgOpinion == null){
					//若核稿人未核稿，则不允许文书科直接提给领导审核
					$("#next8").hide();
				}
                $("#hgOpinionSPAN").html(data.hgOpinion);
                if(data.hgOpinion){
                    $("#hgOpinion").attr("placeholder","");
                }
				
				$("#attnCode").val(data.attnCode);//办理人code
				$("#attnName").val(data.attnName);//办理人name
				//附件
				$("#attachments").val(data.attachments);
				$("#status").val(data.status);
				$("#szOpinion").html(data.szOpinion);
				$("#fszOpinion").html(data.fszOpinion);
				$("#mszOpinion").html(data.mszOpinion);
				$("#fmszOpinion").html(data.fmszOpinion);
				var leadingAudit = "";
				if(data.szOpinion!='' && data.szOpinion!=null){
					leadingAudit += data.szOpinion +"</br>";
				}
				if(data.fszOpinion!='' && data.fszOpinion!=null){
					leadingAudit += data.fszOpinion +"</br>";				
				}
				if(data.mszOpinion!='' && data.mszOpinion!=null){
					leadingAudit += data.mszOpinion +"</br>";
				}
				if(data.fmszOpinion!='' && data.fmszOpinion!=null){
					leadingAudit += data.fmszOpinion;
				}
				$("#leadingAudit").html(leadingAudit);
				$("#pageSize").val(data.pageSize);
				$("#isReturn").val(data.isReturn);
				//初始化流程跟踪页
				doQueryWF("reportInfo","approvalDiv");
				//初始化流程按钮
				var dto = {taskId:taskId}
				wf_getOperator(dto,function(data){
					userTask = data.userTask;//获取当前流程
					
					//被分发单位回执
					if(userTask == 'usertask16'){
						allBtn= "";//需要放在$("#textPage").trigger("click")之前，修改代码请注意;
						
						var dto = {workFlowTypeId:"sendTelegramApprovalV1",userTask:userTask,businessKey:id}
						doJsonRequest("/workflow/getIntentPersonRole",dto,function(data){
							if(data.result) {
								var data = data.data;
								for(var i=0; i<data.length; i++) {
									var arrO = data[i].otherUserCode;//分发之前所有的参与人员
									var currentUserCode = data[i].currentUserCode;//当前用户
									if(($.inArray(currentUserCode,arrO)==-1)) {
										//若当前用户没有参与过审批流程
										//隐藏请示页,保密审核页,流程跟踪页,并且默认添加流程备注信息:"收到"
										$("#textPage a").tab("show");
										$("#textPage").trigger("click");
									}else{
										$("#qsy").show();
										$("#xxgksh").show();
										$("#lcgz").show();
										$(".nav-tabs a:first").tab("show");
									}
								}
							} else {
								$.alert("获取任务信息出错。");
							}
						},{async:false});
						
						printBtnFlag = 1;
						$("#printBtn").hide();
						lcbz = "收到";
						/*if(mark=='002' && $("#userCode").val()!='lijm'){//分发给市长，市长秘书进入时隐藏确认收文按钮
							$("#beSended").hide();
						}*/
						
						//分发给市长，市长秘书进入时隐藏确认收文按钮
						if((mark=='002' && realRoleCode!='002') || (mark=='048' && realRoleCode!='048') || (mark=='051' && realRoleCode!='051') || (mark=='049' && realRoleCode!='049') || (mark=='003' && realRoleCode!='003') || (mark=='050' && realRoleCode!='050') || (mark=='039' && realRoleCode!='039') || (mark=='043' && realRoleCode!='043')){
							$("#buttonList").empty().show().append('<span style="line-height:35px;color:blue;font-weight:bold;">领导文件</span>');
						}
					}else{
						$("#qsy").show();
						$("#xxgksh").show();
						$("#lcgz").show();
						$(".nav-tabs a:first").tab("show");
					}
					
					if(userTask!='usertask12' && userTask!='usertask13'){
						$("#buttonList").show();
					}
					
                     // 发电编号编辑状态 ，除了 userTask =usertask4
                     if(userTask!="usertask4"){
                         $("#reportCode").attr("disabled",true);
                     }
					/*var actionArr =  getPropertyArrayFromObjList(data.workFlowOperaterDTOs, "id");*/
					//控制页面元素是否可编辑
					//退回起草人
					if(userTask == 'usertask1'){
						if($("#isReturn").val()==1){
							$("#buttonList span").hide();
							$("#toVoid").show();
							/*$("#buttonList").empty().append('<span wf-id="flow49"></span>');*/
						}else{
							//文件标题
							$("#reportTitle").removeAttr("disabled");
							//主送
							$("#sendUnit").removeAttr("disabled");
							//附件
							$("#attachments").removeAttr("disabled");
							//紧急程度
							$("#urgencyspan").hide();
							$("#urgency").show();
							//公开属性
							$("#spanOpenType").hide();
							$("#openType").show();
							//公开属性原由
							$("#lcOTReason").show();
							//公开审核备注
							$("#remarks").removeAttr("disabled");
						}
						$("#leaderOpinion").remove();
						$("#hgOpinion").hide();
						$("#proposedAdvice").hide();
					}
					//办理人
					if(userTask == 'usertask4'){
						allBtn= allBtn+"_savePDF";
						$("#leaderOpinion").remove();
						$("#hgOpinion").hide();
						$("#proposedAdvice").hide();
						if( $("#status").val() !='0006' && $("#status").val()!='0007'){
							//当数据库签发人字段不为空时,说明领导已经审批完毕,交给了文书办办理,当前步骤可以套红/写文件编号/印发时间/印发份数
							//当status为已分发或者已完成时，不可编辑，且隐藏套红按钮。
//							//印发日期:
//							$("#reportDate").html(new Date().format('yyyy年MM月dd日'));
//							//印发份数
//							$("#issuedCount").attr("display","block");
//							$("#issued").removeAttr("disabled");
							//文件编号变为可填写
							$("#reportCode").removeAttr("disabled");
							$("#pageSize").removeAttr("disabled");
							//当前为套红步骤
							$("button[name='redBtn']").show();
							$("#redBtn").removeClass("hidden");
						}
					}
					//文书核稿
					if(userTask == 'usertask9'){
						$("#leaderOpinion").remove();
						$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
						$("#hgOpinion").removeAttr("disabled");
						$("#lcOTReason").removeAttr("disabled");
						$("#lcOTReason").show();
						$("#proposedAdvice").removeAttr("disabled");
						$("#remarks").removeAttr("disabled");
	                    if(!$("#proposedAdviceSPAN").html()){
	                        $("#proposedAdvice").attr("placeholder","请填入审核意见");
	                    }
					}
					//分管秘书长审批/秘书长审批
					if(userTask == 'usertask10' || userTask == 'usertask11'){
						$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
						$("#leaderOpinion").removeAttr("disabled");
						$("#hgOpinion").hide();
					}
					
					//副市长审批
					if(userTask == 'usertask12'){
						$("#hgOpinion").hide();
						if(realRoleCode!=null && realRoleCode!='null' && realRoleCode!=''){
							$("#buttonList").show();
							$("#leaderOpinion").removeAttr("disabled");
							$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
						}else{
							$("#leaderOpinion").remove();
							$("#buttonList").empty().show().append('<span style="line-height:35px;color:blue;font-weight:bold;">领导文件</span>');
						}
					}
					
					//市长审批
					if(userTask == 'usertask13'){
						$("#proposedAdvice").hide();
						$("#hgOpinion").hide();
						//如果当前为市长审批,则显示标签,并判断登录人是市长还是秘书,秘书没有操作权限,只有添加标签信息和保存权限.
//						$("#littlePage").show();
						/*if(mark=='002' && $("#userCode").val()!='lijm'){
//							$("#buttonList").append("<button type='button'  class='btn btn_click'  onclick='saveLittlePage()' style='margin-left:8px;' >保存便签</button>");
							$("#leaderOpinion").remove();
							$("#buttonList span").hide();
						} else {
							$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
							$("#leaderOpinion").removeAttr("disabled");
						}*/
//						$("#liPage").removeAttr("disabled");
						
						
						if(realRoleCode!=null && realRoleCode!='null' && realRoleCode!=''){
							$("#buttonList").show();
							$("#leaderOpinion").removeAttr("disabled");
							$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
						}else{
							$("#leaderOpinion").remove();
							$("#buttonList").empty().show().append('<span style="line-height:35px;color:blue;font-weight:bold;">领导文件</span>');
						}
						
					}
				});
			} else {
				$.alert("获取信息失败。");
			}
		});
	}else{
		//根据发文类型动态生成title
		var title = "";
		switch (type) {
		case '0001':
			title = "大同市人民政府发文卡片";
			break;
		case '0002':
			title = "大同市人民政府发函卡片";	
			break;
		case '0005':
			title = "大同市人民政府办公厅发文卡片";
			break;
		case '0006':
			title = "大同市人民政府办公厅发函卡片";
			break;
		}
		$("#title").text(title);
	}
});


//----------与信息审核页的录入信息同步(开始)------------
$("#reportCode").on("blur", function() {
    $("[name='secCode']").html($("#reportCode").val());
});

//单位/部门
$("#fromOrgName").on("blur", function() {
    $("#orgName").html($("#fromOrgName").val());
    $("#bmks").html($("#fromOrgName").val());
});
//经办人
$("#attnPerson").on("blur", function() {
    $("#jbr").html($("#attnPerson").split(":")[0]);
});
//联系电话
$("#phoneNumber").on("blur", function() {
    if ($("#attnPerson").split(":")[1]) {
        $("#lxdh").html($("#attnPerson").split(":")[1]);
    }
});
//属性
$("#openType").change(function() {
    $("#fileAttr").html($(this).children('option:selected').text());
});

//文件名称
$("#reportTitle").on("blur", function() {
    $("#wjbt").html($("#reportTitle").val());
});

//----------与信息审核页的录入信息同步(结束)------------


function doSearchRole() {
    var obj = {
        title: '选择角色',
        height: "500px",
        width: "750px",
        url: '../users/role_dialog.html?selectCount=0001',
        myCallBack: "initFollowRoleInfo"
    };
    new jqueryDialog(obj);
}

function initFollowRoleInfo(data) {
    $("#reportRoleName").val(data.roleName);
    $("#reportRoleCode").val(data.roleCode);
}



var btnSource = "app";
var appFlag = getURLParam("appFlag"); 
if(fromPage == 'pc' && appFlag != 1){
    btnSource = fromPage;
}
function goSuccess(data) {
    if (data.operaterId == 'flow16' && data.assignUserName != null) {
        //起草页面,如果当前操作为制定办理人,且办理人角色下只对应一个用户,保存办理人(如果对应多个用户,需在用户认领任务页面保存办理人)
        addTransactor(data);
    }
    window.location.href = "../tododocs/docs_down.html?roleName=" + data.assignRoleName + "&fromPage=0002&operaterId=" + data.operaterId + "&btnSource=" + btnSource+"&mark="+mark;
}
function addTransactor(data) {
    var dto = {
        id: data.businessKey,
        attnCode: data.assignUserCode
    };
    doJsonRequest("/senddoc/addReportText", dto, function(data) {
    });
}
function goSuccessEnd() {
    window.location.href = "../tododocs/docs_down.html?fromPage=0002&flag=1&btnSource=" + btnSource+"&mark="+mark;
}
//分发
function goSuccessSend(data) {
	if("flow26" == data.operaterId && isMayor.indexOf(10)!=-1){
		//如果当前为可以创建一事一表的角色，则提示是否创建一事一表
		if(confirm("是否通知责任部门创建一事一表？")){
			var dto = {
						docId:$("#id").val(),
						docType:$("#reportType").val(),
						docTitle:$("#reportTitle").val(),
						orgName:$("#userOrg").val(),
						dcRoleCode:roleCode,
						dcRoleName:roleName,
						status:"0002"
			}
			doJsonRequest("/actionList/addToBeCreatedList", dto, function(da){
				if(da.result) {
					$.alert("创建成功。");
					window.location.href = "../tododocs/docs_down.html?fromPage=0002&flag=8&btnSource=" + btnSource+"&mark="+mark;
				} else {
					$.alert("创建失败。");
					return false;
				}
			},{showWaitting:true})				
		}else{
			window.location.href = "../tododocs/docs_down.html?fromPage=0002&flag=0&btnSource=" + btnSource+"&mark="+mark;
		}
	}else{
		window.location.href = "../tododocs/docs_down.html?fromPage=0002&flag=0&btnSource=" + btnSource+"&mark="+mark;
	}
}
function goVoidEnd() {
    window.location.href = "../tododocs/docs_down.html?fromPage=0002&flag=3&btnSource=" + btnSource+"&mark="+mark;
}
//这两个要在起草阶段保存
//orgAudOpinion:$("#orgAudOpinion").val(),
//orgAuditing:$("#fzr").html()

function trim(str) {
    return str.replace(/(^\s*)|(\s*$)/g, "");
}


function getAddData() {

    //公开属性原由
    var proposedAdvice = $("#proposedAdvice").val();
    if (proposedAdvice != '') {
        proposedAdvice = proposedAdvice + "  [" + userName + "  " + new Date().format("yyyy-M-d hh:mm") + "]";
        proposedAdvice += "</br>"+$("#proposedAdviceSPAN").html();
    }
    var attachmentDTOs = new Array();
    if (flag) {
        weboffice.window.saveFileToUrl();
        attachmentDTOs = weboffice.window.getAttachmentDTOs();
        var officefileUrl = window.frames["weboffice"].document.getElementById('officeUrl').value;
        textUrl = officefileUrl;
        $("#text").val(officefileUrl);
    }
    var dto = {};
    //退回起草人
    if (userTask == 'usertask1') {
        dto = {
            id: id,
            reportTitle: $("#reportTitle").val(),
            sendUnit: $("#sendUnit").val(),
            attachments: $("#attachments").val(),
            //秘密等级
            secretLevel: $("#secretLevel").val(),
            //紧急程度
            urgency: $("#urgency").val(),
            //报备
//				putOnRecords:$("#putOnRecords").val(),
            //属性
//				openType:$("#openType").val(),
            //公开与否说明

            reportDate: tempDate,
            //备注
//				remarks:$("#remarks").val(),
            attachmentDTOs: attachmentDTOs,
            text: textUrl,
            flag: 1, //当前为审批.
        }
        //核稿人审核
    } else if (userTask == 'usertask9') {
        dto = {
            id: id,
            hgOpinion: $("#hgOpinion").val().length?($("#hgOpinion").val()+" [" + userName +   new Date().format('yyyy-M-d hh:mm') + "]</br>"+$("#hgOpinionSPAN").html()):$("#hgOpinionSPAN").html(), //核稿人意见
            hgAuditing: $("#hg").html(), //核稿人签字
            openTypeReason: $("#lcOTReason").val().length?($("#lcOTReason").val()+" [" + userName +   new Date().format('yyyy-M-d hh:mm') + "]</br>"+$("#openTypeReason").html()):$("#openTypeReason").html(), //公开原因
            remarks:$("#remarks").val(),
            hgDate: $("#hgDate").html(),
//				proposedDate:proposedDate,
            proposedAdvice: proposedAdvice,
            reportTitle: $("#reportTitle").val(),
            sendUnit: $("#sendUnit").val(),
            attachments: $("#attachments").val(),
            //秘密等级
            secretLevel: $("#secretLevel").val(),
            //紧急程度
            urgency: $("#urgency").val(),
            attachmentDTOs: attachmentDTOs,
            text: textUrl,
            /*attachments:attaNames,*/
            flag: 1, //当前为审批.
        }
    } else {
        var ldsh = $("#leaderOpinion").val();
        var fmszOpinion = $("#fmszOpinion").html();
        var mszOpinion = $("#mszOpinion").html();
        var fszOpinion = $("#fszOpinion").html();
        var szOpinion = $("#szOpinion").html();

        if (ldsh != '') {
            ldsh = ldsh + "  [" + userName + "  " + new Date().format('yyyy-M-d hh:mm') + "]";
           
            if (userTask == 'usertask10') {
                //如果当前环节为副秘书长审批
                if (fmszOpinion != '') {
                    fmszOpinion = ldsh + "<br>" + fmszOpinion;
                } else {
                    fmszOpinion = ldsh + "<br>";
                }
            } else if (userTask == 'usertask11') {
                //如果当前环节为秘书长审批
                if (mszOpinion != '') {
                    mszOpinion = ldsh + '<br>' + mszOpinion;
                } else {
                    mszOpinion = ldsh + "<br>";
                }
            } else if (userTask == 'usertask12') {
                //如果当前环节为副市长审批
                if (fszOpinion != '') {
                    fszOpinion = ldsh + '<br>' + fszOpinion;
                } else {
                    fszOpinion = ldsh + "<br>";
                }
            } else if (userTask == 'usertask13') {
                //如果当前环节为市长审批
                if (szOpinion != '') {
                    szOpinion = ldsh + '<br>' + szOpinion;
                } else {
                    szOpinion = ldsh + "<br>";
                }
            }
             ldsh =ldsh +"</br>"+$("#leadingAudit").html();
        }
        dto = {
            id: id,
            fmszOpinion: fmszOpinion, //副秘书长意见
            mszOpinion: mszOpinion, //秘书长意见
            fszOpinion: fszOpinion, //副市长意见
            szOpinion: szOpinion, //市长意见
            littlePage:getLittileMsg(),
            theIssuer: userName, //去后台判断当前用户是否是完成流程的用户,是的话保存这个签发人字段,不是的话则去掉这个字段
            proposedAdvice: proposedAdvice,
            text: $("#text").val(),
            flag: 1, //当前为审批.
            issued: $("#issued").val(),
            reportCode: $("#reportCode").val(),
            pageSize:$("#pageSize").val()
        }
    }

    return dto;
}

function getLittileMsg(){
	var lps = $("#liPage").val();
	if(lps!=''){
		lps = lps + "  [" + userName + "  " + new Date().format('yyyy-M-d hh:mm') + "]";
		if($("#lPageSpan").html()!=''){
			if($("#userCode").val() == 'lijm'){
				lps = lps + '<br/>' + $("#lPageSpan").html();
			}else{
				lps = $("#lPageSpan").html() + '<br/>' + lps;
			}
		}
		return lps;
	}else{
		return $("#lPageSpan").html();
	}
}

function saveLittlePage(){
	var lps = $("#liPage").val();
	if(lps!=''){
		lps = lps + "  [" + userName + "  " + new Date().format('yyyy-M-d hh:mm') + "]";
		if($("#lPageSpan").html()!=''){
			if($("#userCode").val() == 'lijm'){
				lps = lps + '<br/>' + $("#lPageSpan").html();
			}else{
				lps = $("#lPageSpan").html() + '<br/>' + lps;
			}
		}
	}else{
		$.alert('请输入备注信息');
		return false;
	}
	var dto = { id:id,littlePage:lps};
	doJsonRequest("/senddoc/addReportText",dto,function(data){
             $("#lPageSpan").html(dto.littlePage);
            $("#liPage").val("");
            $.alert("保存成功");
            return;
		window.location.href="../tododocs/docs_down.html?fromPage=0002&flag=2&operaterId="+data.operaterId+"&btnSource="+btnSource+"&mark="+mark;
	},{showWaiting:true});
}

function getStatusData() {
    var dto = {
        id: id
    };
    return dto;
}

//当办理人分发文件或者办结文件时,可以保存文件编号/印发时间/印发份数(后续需要集成在套红操作中)
function getFFBJData() {
	if(flag){
		weboffice.window.saveFileToUrl();
		var officefileUrl= window.frames["weboffice"].document.getElementById('officeUrl').value;
		var textId= window.frames["weboffice"].document.getElementById('textId').value;
		$("#text").val(officefileUrl);
		$("#textId").val(textId);
	}
    var tempDate = null;
    var tempPageSize = null;
    if ($("#reportDate").html() == null || $("#reportDate").html() == '') {
        tempDate = new Date().format("yyyy-M-dd h:mm");
    } else {
        tempDate = $("#reportDate").html().split('日')[0].replace(/[^\d]/g, '-');
    }
    tempPageSize = $("#pageSize").val();
    var dto = {
        id: id,
        reportDate: tempDate,
        pageSize: tempPageSize,
        reportCode: $("#reportCode").val(),
        text:$("#text").val(),
		textId:$("#textId").val()
    };
    return dto;

}

function saveRedBtn(url) {
    var dto = getStatusData();
    dto.text = url;
    addReportText(dto);
}

function addReportText(dto) {
    doJsonRequest("/senddoc/addReportText", dto, function(data) {
    }, {showWaiting: true});
}

function wf_getDate(data) {
    if ($("#urgency").val() == '0001') {
        return getTheDate(86400);
    }
    if ($("#urgency").val() == '0002') {
        return getTheDate(86400);
    }
    if ($("#urgency").val() == '0003') {
        return getTheDate(86400);
    }
    if ($("#urgency").val() == '0004') {
        return getTheDate(86400);
    }
}

//流程备注信息
function wf_getMark(operaterId) {
    return lcbz;
    /*//核稿意见
     if(operaterId == 'flow18'){
     return $("#hgOpinion").val();
     }
     //分管副秘书长意见
     if(operaterId == 'next1' || operaterId == 'flow40'){
     return $("#fmszOpinion").val();
     }
     //秘书长意见
     if(operaterId == 'next2' || operaterId == 'flow41'){
     return $("#mszOpinion").val();
     }
     //副市长意见
     if(operaterId == 'next3' || operaterId == 'flow42'){
     return $("#fszOpinion").val();
     }
     //市长意见
     if(operaterId == 'next4' || operaterId == 'flow43'){
     return $("#szOpinion").val();
     }*/
}

function initNfwInfoA(NfwId) {
    $("#showNfwInfoA").attr("href", "../senddoc/new_nfw_check.html?id=" + NfwId + "&type=0009").attr("target", "_blank");
}


//校验
function wf_beforeValid(opId) {
	 if (flag) {
	        weboffice.window.saveFileToUrl();
	        textUrl = window.frames["weboffice"].document.getElementById('officeUrl').value;
	        var textId= window.frames["weboffice"].document.getElementById('textId').value;
	        $("#text").val(textUrl);
	        $("#textId").val(textId);
	    }
	if(!textUrl.endWith(".pdf") && (opId == 'flow24' || opId == 'flow25')){
		$.alert('请将正文转为PDF。'); 
		return false;
	}
    //文件名称不能为空
    if ($("#reportTitle").val() == '') {
        $.alert("文件标题不能为空");
        return false;
    } /*
     if(validLength($("#openTypeReason").val())>40){
             $.alert("公开属性原因应少-于40字");
		return false;
        }
        if($("#remarks").val().length>30){
             $.alert("备注应少-于30字");
		return false;
        }

         if(!$("#reportCode").val()&&!$("#leadingAudit").html().length){
             $.alert("请填写发电编号");
            return false;
        }*/

    /*
         if($("#reportCode").val().length>25){
             $.alert("发电编号应少-于25字");
            return false;
        }*/
       /*
         if(validLength($("#proposedAdvice").val())>40&&!$("#proposedAdvice").attr("disabled")){
            $.alert("审批单位意见应少-于40字");
            return false;
        }*/
    //秘密等级
    if ($('input:checkbox[name="secretLevel"]:checked').val() == null) {
        $.alert("请确认此文件为非涉密件");
        return false;
    }
    //紧急程度
    if ($("#urgency").val() == '') {
        $.alert("请选择紧急程度");
        return false;
    }

    //领导签字校验:
    if (userTask == 'usertask9') {
        if ($("#hgOpinion").val() == ''&&$("#hgOpinionSPAN").html().length===0) {
            $.alert('请填写审核意见');
            return false;
        }/*
         if (validLength($("#hgOpinion").val())>20&&!$("#hgOpinion").attr("disabled")) {
            $.alert('审核意见应少-于20字');
            return false;
        }*/
        
        if(!(($("#proposedAdvice").val())||$("#proposedAdviceSPAN").html())){
            $.alert("请填写办公厅审批意见");
           return false;
       }
    }
    if (userTask == 'usertask10' || userTask == 'usertask11' || userTask == 'usertask12' || userTask == 'usertask13') {
        if ($("#leaderOpinion").val() == '') {
            $.alert('请填写审核意见');
            return false;
        }
        /*
        if (validLength($("#leaderOpinion").val()) > 30) {
            $.alert("批示意见应少-于30字");
            return false;
        }*/
    }
    //属性
    if ($("#openType").val() == '') {
        $.alert("请选择属性");
        return false;
    }

    //公开属性原由
    if (($("#openType").val() == '0002' || $("#openType").val() == '0003') && $("#openTypeReason").html() == '') {
        $.alert("请填写" + $("#fileAttr").html() + "原由");
        return false;
    }
   window.scrollTo(0,0);
    return true;
}

//----------常用语（开始）------------
function commonLanguage(){
	var obj = {
		    title:'选择常用语',
		    height:"350px",
		    width:"650px",
		    url:'../users/user_common_language.html?userCode='+$("#userCode").val(),
		    myCallBack:"addCommonLanguage"
		};
		new jqueryDialog(obj);
}

function addCommonLanguage(data){
	var language = "";
	$.each(data,function(){
		language += this.language+" ";
	});
	var $_dom = $("#hgOpinion,#leaderOpinion,#proposedAdvice");
	$_dom.each(function(){
		if(!$(this).is(":hidden")){
			$(this).val($(this).val()+language);
			$(this)[0].focus();
			return false;
		}
	});
}
//----------常用语（结束）------------

//打印控制：返回正文id
function wf_getPrintInfo(operaterId){
	var dto = {};
	if("flow24"==operaterId||"flow25"==operaterId){
		var tempDTO= [{businessId:id,businessType:businessType,textId:$("#textId").val()}];
		dto.docLists = tempDTO;
		dto.countLists = [{roleCode:roleCode,count:'99'}];
	}
	return dto;
}
