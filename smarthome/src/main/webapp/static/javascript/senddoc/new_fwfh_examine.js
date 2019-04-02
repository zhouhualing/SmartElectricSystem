/**
4	0001-市政府发文 
	0002-市政府发函 
	0005-办公厅发文 
	0006-办公厅发函 
 */
var type = getURLParam("type");//获取当前发文类型 	 
var id = getURLParam("id"); //当前发文id
var mark = getURLParam("mark");//当前是否是市长秘书点击进来的 002:是 
var realRoleCode = null;
var taskId = getURLParam("taskId");  
var fromPage = getURLParam("fromPage"); 
var fullYear = new Date().getFullYear(); //编号-年
var repCode = "";//同政发/同办发
var fullReportCode =null;
var userTask = "";//当前流程
var ltPage = getURLParam("ltPage"); //小纸条是否可编辑
var btnSource = "app";
var roleCode = "";
var roleName = "";
var reportType="";
var appFlag = getURLParam("appFlag"); 
if(type =="0001"||type =="0002"){
    $("#rmzfbgt").html("大同市人民政府");
    $("#qfps").html("市长审签：");
    $("#fgfszsq").html("分管副市长审核：");
    $("#mszsq").html("秘书长审核：");
}
if(fromPage == 'pc' && appFlag != 1){
	btnSource = fromPage;
}
$("#id").val(id);
$("#businessKey").val(id);
$("#reportType").val(type);

var businessType = getURLParam("businessType");
//打开时默认显示请示页:

//正文加载webOffice控件
var flag = false;
var allBtn = "revision_final_saveFile";//weboffice按钮控制
var redFlag =false;
/*var attaNames="";*///正文附件
var printBtnFlag;
$(function(){
	//获取当前起草人信息
	getCurrenUserInfo(function(data){
		$("#userOrg").val(data.orgName);
		$("#userName").val(data.userName);
		$("#userCode").val(data.userCode);
		/*$("#signature").val(data.signature);*/
		isMayor = data.isMayor;
        roleCode = data.roleCodes;
        roleName = data.roleNames;
        realRoleCode = data.realRoleCode;
		var redHfes = data.redHeadFileEntities;
		for(var i = 0;i<redHfes.length;i++){
			var redHfe = redHfes[i];
               
			if(type == redHfe.reportType){
				var span = $("<span style='margin-right:7px;'></span>");
				var btn = $(document.createElement("button")).addClass('btn').addClass('btn_click').attr('display','none').attr('name','redBtn').html(redHfe.mark).data('redData',redHfe);
				btn.appendTo(span);
				btn.click(function(){
                                    if(redFlag)
                                        return ;
                    setTimeout(function(){
                        redFlag=false
                    },2000)
                  if(flag){
                         if(!$("#reportCode").attr("disabled")){
                           if($("#reportCode").val()==''||$("#fullYear").val()==''){
                              $.alert('请填写编号'); 
                              return false;
                           }
                             var re = /^[0-9]+.?[0-9]*$/;
                           if(!re.test($("#reportCode").val())||!re.test($("#fullYear").val())){
                                $.alert('编号不合理'); 
                                return false;
                           }
                           if($("#reportCode").val().length>5){
                                $.alert('编号应小于5位'); 
                                return false;

                           }
                        }
                         if(!$("#issued").attr("disabled")){
                            if($("#issued").val()==''){
                               $.alert('请填写印发份数'); 
                               return false;
                            }
                             var re = /^[0-9]+.?[0-9]*$/;
                            if(!re.test($("#issued").val())){
                                 $.alert('印发份数不合理'); 
                                 return false;
                            }
                            if($("#issued").val().length>5){
                                 $.alert('印发份数应小于5位'); 
                                 return false;
                            }
                         }
						//校验 发文卡上的编号、印发日期、印数。
						if($("#reportCode").val() == ''&&(!$("#reportCode").attr("disabled"))){
							$.alert("请输入编号");
							return false;
						}
						if($("#auditTime").html().length<=2 && $("#auditTime").html().length>0){
							$.alert("请输入印发日期");
							return false;
						}
						if($("#issued").val() == ''&&!$("#issued").attr("disabled")){
							$.alert("请输入印数");
							return false;
						}
						var thisRed = $(this).data('redData');
						//读取模板文件套红(URL:文件路径;endUrl:尾部套红路径；code:套红编号;ccuser;套红抄送人;company:发文单位(ZF政府、BGT办公厅);)
						var URL = baseRedHeadUrl+thisRed.redHeadTemp;
						var endUrl = baseRedHeadUrl+thisRed.endTemp;
						/*alert(URL);
						alert(endUrl)
						alert(weboffice.window.insertRedHeadFromUrl);*/
						weboffice.window.insertRedHeadFromUrl(URL,endUrl,'001','','');
						$("button[name='redBtn']").parent().hide();
                        redFlag =true;
                        window.scrollTo(0,0);
					}
					else{
						$.alert("请先审阅正文");
					}
				});
				$("#flow24").before(span);
				/*span.appendTo($("#buttonList"));*/
			}
		}
		
		$("#textPage").click(function(){
			if(!flag){
				$("#textIframe").append('<iframe name="weboffice" src="../weboffice/weboffice.html?officeUrl='+$("#text").val()+'&allBtn='+allBtn+'&textId='+$("#textId").val()+'&taskid='+id+'&shAttBtn=1&showPrintBtn='+$("#status").val()+'&userName='+$("#userName").val()+'"style="width:100%;height:800px;"></iframe>');
				flag = true;
			}
		});
	});
	if(id != null) {
		$("#id").val(id);
		$("#businessId").val(id);
		$("#backBtn").removeClass("hidden");
		var dto = {
				id:id
		};
		doJsonRequest("/senddoc/getReport",dto,function(data){
			if(data.result) {
				var data = data.data;
				if(data.nfwId != null) {
					$("#businessKey1").val(data.nfwId);
					initNfwInfoA(data.nfwId);
					doQueryWF("reportInfo1","approvalDiv1");
				} else {
					$("#hiddenDiv1").hide();
				}
				if(data.reportType!='' && data.reportType!=null && data.reportType!='null'){
					$("#reportType").val(data.reportType);
					//根据发文类型动态生成title
					var title = "";
					switch (data.reportType) {
					case '0001':
						title = "大同市人民政府发文卡片";
						repCode = "同政发";
						break;
					case '0002':
						title = "大同市人民政府发函卡片";	
						repCode = "同政函";
						break;
					case '0005':
						title = "大同市人民政府办公厅发文卡片";
						repCode = "同政办发";
						break;
					case '0006':
						title = "大同市人民政府办公厅发函卡片";
						repCode = "同政办函";
						break;
					}
					$("#title").text(title);
					$("#repCode").text(repCode);
				}
				if(data.reportType == "0003") {
					$("#showInfoxx").html("查看拟发电详细信息");
				}
				if((data.reportType == "0002") || (data.reportType == "0006")) {
					$("#showInfoxx").html("查看拟发函详细信息");
				}
				if(data.reportCode!=null&&data.reportCode!=''){
					var strs= new Array(); //定义一数组 
					strs=data.reportCode.split("_"); //字符分割 
					fullYear = strs[0];
					$("#fullYear").val(strs[0]);
					$("#reportCode").val(strs[1]);
					$("#secCode").html(repCode+"【"+$("#fullYear").val()+"】"+$("#reportCode").val()+"号");
				}
				$("#reportTitle").val(data.reportTitle);
				$("#wjbt").html(data.reportTitle);
				$("#sendUnit").val(data.sendUnit);
				$("#ccUnit").val(data.ccUnit);
				$("input[name='secretLevel'][value='"+data.secretLevel+"']").attr("checked",'checked');
				$("#urgency").val(data.urgency);
				$("#urgencyspan").html($("#urgency").find("option:selected").text());
				$("#putOnRecords").val(data.putOnRecords);
				$("#openType").val(data.openType);
				$("#fileAttr").html($("#"+data.openType).html());//属性与是否公开的选项一致
				$("input[name='ot'][value='"+data.openType+"']").attr("checked",'checked');
				$("#theIssuer").val(data.theIssuer);
				$("#openTypeReason").html(data.openTypeReason);
                $("#proposedAdviceSPAN").html(data.proposedAdvice);
				/*$("#proposedDate").html(data.proposedDate);*/
				$("#remarks").val(data.remarks);
				$("#text").val(data.text);//正文,暂时的
				$("#textId").val(data.textId);//正文
				if($("#auditTime").html() == ''){
					$("#auditTime").html(new Date(data.auditTime).format('yyyy年M月d日'));
				}
				$("#reportDate").html(new Date(data.reportDate).format('yyyy年M月d日'));
				//拟办单位
				if(data.fromOrgName!=null && data.fromOrgName!=''){
					$("#createUserOrgName").val(data.fromOrgName);
					$("#fromOrgName").html(data.fromOrgName);
					$("#bmks").html(data.fromOrgName);
				}
				//拟文人
				if(data.attnPerson!=null && data.attnPerson!=''){
					$("#createUserName").val(data.attnPerson);
					$("#jbr").html(data.attnPerson);
				}
				//联系电话
				if(data.fromNumber!=null && data.fromNumber!=''){
					$("#phoneNumber").val(data.fromNumber);
					$("#lxdh").html(data.fromNumber);
				}
				//小纸条
				/*$("#liPage").val(data.littlePage);*/
				$("#lPageSpan").html(data.littlePage);
             //   增加 span
			//	$("#fzbOpinion").val(data.fzbOpinion);
                $("#fzbOpinionSPAN").html(data.fzbOpinion);
				/*$("#fzb").append(data.fzbAuditing);
				$("#fzbDate").html(data.fzbDate);*/
                              
			//	$("#hgOpinion").val(data.hgOpinion);
                if(data.hgOpinion == '' || data.hgOpinion == null){
                	$("#next8").hide();
                }
                $("#hgOpinionSPAN").html(data.hgOpinion);
				/*$("#hg").append(data.hgAuditing);
				$("#hgDate").html(data.hgDate);*/

			//	$("#fmszOpinion").val(data.fmszOpinion);
                $("#fmszOpinionSPAN").html(data.fmszOpinion);
				/*$("#fmsz").append(data.fmszAuditing);
				$("#fmszDate").html(data.fmszDate);*/

			//	$("#mszOpinion").val(data.mszOpinion);
                $("#mszOpinionSPAN").html(data.mszOpinion);
				/*$("#msz").append(data.mszAuditing);
				$("#mszDate").html(data.mszDate);*/

		//		$("#fszOpinion").val(data.fszOpinion);
                $("#fszOpinionSPAN").html(data.fszOpinion);
				/*$("#fsz").append(data.fszAuditing);
				$("#fszDate").html(data.fszDate);*/

			//	$("#szOpinion").val(data.szOpinion);
                $("#szOpinionSPAN").html(data.szOpinion);
				/*$("#sz").append(data.szAuditing);
				$("#szDate").html(data.szDate);*/
				$("#attnCode").val(data.attnCode);//办理人code
				$("#attnName").val(data.attnName);//办理人name
				//附件
				$("#attachments").val(data.attachments);
				
				//份数
				if(data.issued!=''){
					$("#issuedCount").attr("display","block");
					$("#issued").val(data.issued);
				}
				$("#status").val(data.status);
				$("#isReturn").val(data.isReturn);
                // 页面初始化完成，开始检查各领导是否需要签名
               /* checkName();*/
				//初始化流程跟踪页
				doQueryWF("reportInfo","approvalDiv");
				//初始化流程按钮
				var dto = {taskId:taskId}
				wf_getOperator(dto,function(data){
					userTask = data.userTask;//获取当前流程
					//被分发单位回执
					if(userTask == 'usertask16'){
						allBtn= "";//需要放在$("#textPage").trigger("click")之前，修改代码请注意;
						
						hideAll();
						var dto = {workFlowTypeId:"sendDocApprovalV1",userTask:userTask,businessKey:id}
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
						//分发给市长，市长秘书进入时隐藏确认收文按钮
						if((mark=='002' && realRoleCode!='002') || (mark=='048' && realRoleCode!='048') || (mark=='051' && realRoleCode!='051') || (mark=='049' && realRoleCode!='049') || (mark=='003' && realRoleCode!='003') || (mark=='050' && realRoleCode!='050') || (mark=='039' && realRoleCode!='039') || (mark=='043' && realRoleCode!='043')){
							$("#buttonList").empty().show().append('<span style="line-height:35px;color:blue;font-weight:bold;">领导文件</span>');
							//$("#beSended").remove();
						}
					}else{
						$("#qsy").show();
						$("#xxgksh").show();
						$("#lcgz").show();
						$(".nav-tabs a:first").tab("show");
					}
					
					if(userTask != 'usertask12' && userTask != 'usertask13'){
						$("#buttonList").show();
					}
					//文书办理
					if(userTask=='usertask4'){
						allBtn= allBtn+"_savePDF";
					}
					/*var actionArr =  getPropertyArrayFromObjList(data.workFlowOperaterDTOs, "id");*/
					//控制页面元素是否可编辑
					//退回起草人
					if(userTask == 'usertask1'){
						if($("#isReturn").val()==1){
							$("#buttonList span").hide();
							$("#toVoid").show();
							/*$("#buttonList").empty().append.('<span wf-id="flow49"></span>');*/
						}else{
							//文件标题
							$("#reportTitle").removeAttr("disabled");
							//主送
							$("#sendUnit").removeAttr("disabled");
							//抄送
							$("#ccUnit").removeAttr("disabled");
							//附件
							$("#attachments").removeAttr("disabled");
							//紧急程度
							$("#urgencyspan").hide();
							$("#urgency").show();
							//公开属性
							$(".ot").removeAttr("disabled");
							//公开属性原由
							$("#lcOTReason").show();
							//公开审核备注
							$("#remarks").removeAttr("disabled");
							//报备
							$("#putOnRecords").removeAttr("disabled");
						}
						$("#hgOpinion").hide();
						$("#fzbOpinion").hide();
						$("#fmszOpinion").hide();
						$("#mszOpinion").hide();
						$("#fszOpinion").hide();
						$("#szOpinion").hide();
						$("#proposedAdvice").hide();
					}
					//办理人
					if(userTask == 'usertask4'){
						$("#createUserOrgName").removeAttr("disabled");
                        $("#createUserName").removeAttr("disabled");
                        $("#phoneNumber").removeAttr("disabled");
						$("#hgOpinion").hide();
						$("#fzbOpinion").hide();
						$("#fmszOpinion").hide();
						$("#mszOpinion").hide();
						$("#fszOpinion").hide();
						$("#szOpinion").hide();
						$("#proposedAdvice").hide();
						if(($("#fmszOpinionSPAN").html()!='' || $("#mszOpinionSPAN").html()!='' ||$("#fszOpinionSPAN").html()!='' ||$("#szOpinionSPAN").html()!='') && $("#status").val() !='0006' && $("#status").val()!='0007'){
							//当数据库签发人字段不为空时,说明领导已经审批完毕,交给了文书办办理,当前步骤可以套红/写文件编号/印发时间/印发份数
							//当status为已分发或者已完成时，不可编辑，且隐藏套红按钮。
							//印发日期:
							$("#auditTime").html(new Date().format('yyyy年M月d日'));
							//印发份数
							$("#issuedCount").attr("display","block");
							$("#issued").removeAttr("disabled");
							//文件编号变为可填写
							$("#fullYear").removeAttr("disabled");
							$("#fullYear").val(fullYear);
							$("#reportCode").removeAttr("disabled");
							//当前为套红步骤
							$("button[name='redBtn']").show();
						}
						
					}
					//法制办审核
					if(userTask == 'usertask17'){
						$("#hgOpinion").hide();
						$("#fmszOpinion").hide();
						$("#mszOpinion").hide();
						$("#fszOpinion").hide();
						$("#szOpinion").hide();
						$("#proposedAdvice").hide();		
					}
					//文书核稿
					if(userTask == 'usertask9'){
						$("#fzbOpinion").hide();
						$("#fmszOpinion").hide();
						$("#mszOpinion").hide();
						$("#fszOpinion").hide();
						$("#szOpinion").hide();
                        if($("#proposedAdviceSPAN").html().length){
                            $("#proposedAdvice").attr("placeholder","");
                        }
						//文件标题
						$("#reportTitle").removeAttr("disabled");
						//主送
						$("#sendUnit").removeAttr("disabled");
						//抄送
						$("#ccUnit").removeAttr("disabled");
						//附件
						$("#attachments").removeAttr("disabled");
						//紧急程度
						$("#urgencyspan").hide();
						$("#urgency").show();
						//公开属性
						$(".ot").removeAttr("disabled");
						//公开属性原由
						$("#lcOTReason").show();
						//公开审核备注
						$("#remarks").removeAttr("disabled");
						//报备
						$("#putOnRecords").removeAttr("disabled");
						$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
					}
					//分管秘书长审批
					if(userTask == 'usertask10'){
						$("#hgOpinion").hide();
						$("#fzbOpinion").hide();
						$("#mszOpinion").hide();
						$("#fszOpinion").hide();
						$("#szOpinion").hide();
						$("#proposedAdvice").hide();
						$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
					}
					//秘书长审批
					if(userTask == 'usertask11'){
						$("#hgOpinion").hide();
						$("#fzbOpinion").hide();
						$("#fmszOpinion").hide();
						$("#fszOpinion").hide();
						$("#szOpinion").hide();
						$("#proposedAdvice").hide();
						$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
					}
					//副市长审批
					if(userTask == 'usertask12'){
						$("#hgOpinion").hide();
						$("#fzbOpinion").hide();
						$("#fmszOpinion").hide();
						$("#mszOpinion").hide();
						$("#szOpinion").hide();
						$("#proposedAdvice").hide();
						
						if(realRoleCode!=null && realRoleCode!='null' && realRoleCode!=''){
							$("#buttonList").show();
							$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
						}else{
							$("#fszOpinion").attr('disabled',true);
							//$("#buttonList span").empty().append(roleName);
							$("#buttonList").empty().show().append('<span style="line-height:35px;color:blue;font-weight:bold;">领导文件</span>');
						}
					}
					//市长审批
					if(userTask == 'usertask13'){
						$("#hgOpinion").hide();
						$("#fzbOpinion").hide();
						$("#fmszOpinion").hide();
						$("#mszOpinion").hide();
						$("#fszOpinion").hide();
						$("#proposedAdvice").hide();
						//如果当前为市长审批,则显示标签,并判断登录人是市长还是秘书,秘书没有操作权限,只有添加标签信息和保存权限.
						//$("#littlePage").show();
						/*if(mark=='002' && $("#userCode").val()!='lijm'){
							//$("#buttonList").append("<button type='button'  class='btn btn_click'  onclick='saveLittlePage()' style='margin-left:8px;' >保存便签</button>");
							$("#szOpinion").attr('disabled',true);
							$("#buttonList span").hide();
						}else{
							$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
						}*/
						//$("#liPage").removeAttr("disabled");
						
						if(realRoleCode!=null && realRoleCode!='null' && realRoleCode!=''){
							$("#buttonList").show();
							$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
						}else{
							$("#szOpinion").attr('disabled',true);
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
		/*自动生成编号
		$("#auditTime").html(new Date().format("yyyy-m-d"));
		doJsonRequest("/code/getCode","0001",function(data){
			if(data.result) {
				$("#reportCode").html(data.data);
			} else {
				$.alert("获取编码出错。");
			}
		});
		*/
	}
});


//----------与信息审核页的录入信息同步(开始)------------
//文件名称
$("#reportTitle").on("blur",function(){
	$("#wjbt").html($("#reportTitle").val());
});
//单位/部门
$("#createUserOrgName").on("blur",function(){
	$("#fromOrgName").html($("#createUserOrgName").val());
	$("#bmks").html($("#createUserOrgName").val());
});
//经办人
$("#createUserName").on("blur",function(){
	$("#jbr").html($("#createUserName").val());
});
//联系电话
$("#phoneNumber").on("blur",function(){
	$("#lxdh").html($("#phoneNumber").val());
});

//选择公开属性:
$("#chooseOpenType input[name='ot']").click(function(){
	$("#openType").val($(this).val());
	$("#fileAttr").html($("#"+$(this).val()).html());
});

$("#reportCode").on("blur",function(){
	if($("#fullYear").val()!=null&&$("#reportCode").val()!=null){
		$("#secCode").html(repCode+"【"+$("#fullYear").val()+"】"+$("#reportCode").val()+"号");
	}
});
$("#fullYear").on("blur",function(){
	if($("#fullYear").val()!=null&&$("#reportCode").val()!=null){
		$("#secCode").html(repCode+"【"+$("#fullYear").val()+"】"+$("#reportCode").val()+"号");
	}
});
//公开信息审核页 审批单位意见动态添加日期:
/*$("#proposedAdvice").on("blur",function(){
	if($("#proposedAdvice").val()!=''){
		$("#proposedDate").html(new Date().format('yyyy-M-d hh:mm'));
	}else{
		$("#proposedDate").html('');
	}
});*/
//----------审批人员签批(开始)------------
/*var flaghg = false;
$("#fzbOpinion,#hgOpinion,#fmszOpinion,#mszOpinion,#fszOpinion,#szOpinion").on("blur",function(){
	if($(this).val()==''){
		$(this).parent().children("label").html('');
		$(this).parent().children("P").children("span").html('');
		flaghg = false;
	}else{
		if(!flaghg){
			if($("#signature").val()!=''){
				$(this).parent().children("label").append("<img alt='' style='width:80px;height:40px;' src='/cmcp/static/images/"+$("#signature").val()+"'>");
			}
			$(this).parent().children("P").children("span").html(new Date().format('yyyy-M-d hh:mm'));
                       
			flaghg = true;
		}
	}
});*/

//----------审批人员签批(结束)------------

//----------与信息审核页的录入信息同步(结束)------------

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
	var thisText = "";
	if(userTask == 'usertask9'){
		//文书核稿
		if(!$("#tab3").is(":hidden")){
			thisText = $("#proposedAdvice").val();
			$("#proposedAdvice").val(thisText+language);
			$("#proposedAdvice")[0].focus();
		}else{
			thisText = $("#hgOpinion").val();
			$("#hgOpinion").val(thisText+language);
			$("#hgOpinion")[0].focus();
		}
	}else if(userTask == 'usertask10'){
		//副秘书长审批
		thisText = $("#fmszOpinion").val();
		$("#fmszOpinion").val(thisText+language);
		$("#fmszOpinion")[0].focus();
	}else if(userTask == 'usertask11'){
		//秘书长审批
		thisText = $("#mszOpinion").val();
		$("#mszOpinion").val(thisText+language);
		$("#mszOpinion")[0].focus();
	}else if(userTask == 'usertask12'){
		//副市长审批
		thisText = $("#fszOpinion").val();
		$("#fszOpinion").val(thisText+language);
		$("#fszOpinion")[0].focus();
	}else if(userTask == 'usertask13'){
		//市长审批
		thisText = $("#szOpinion").val();
		$("#szOpinion").val(thisText+language);
		$("#szOpinion")[0].focus();
	}
	/*var $_dom = $("#fzbOpinion,#hgOpinion,#fmszOpinion,#mszOpinion,#fszOpinion,#szOpinion");
	$_dom.each(function(){
		if(!$(this).attr("disabled")){
			if($(this).attr('id') == 'hgOpinion' && !$("#tab3").is(":hidden") ){
				$("#proposedAdvice").val($("#proposedAdvice").val()+language);
			}else{
				$(this).val($(this).val()+language);
				if(!flaghg){
					if($("#signature").val()!=''){
						$(this).parent().children("label").append("<img alt='' style='width:80px;height:40px;' src='/cmcp/static/images/"+$("#signature").val()+"'>");
					}
					$(this).parent().children("P").children("span").html(new Date().format('yyyy-M-d hh:mm'));
					flaghg = true;
				}
				return false;
			}
		}
	});*/
}
//----------常用语（结束）------------

//*************************
//提交发文
//$("#submitBtn").on("click",function(){
//	
//});

$("#completeDate").datetimepicker({
	showSecond: true, //显示秒
	timeFormat: 'HH:mm:ss',//格式化时间
	stepHour: 1,//设置步长
	stepMinute: 5,
	stepSecond: 10,
	dateFormat:"yy-mm-dd",
	currentText:'现在',
	closeText:'确定',
	hourMax:'23',
	hourText:'时',
	minuteText:'分',
	secondText:'秒',
	timeText:'时间'
});

function goSuccess(data) {
	if(data.operaterId == 'flow16' && data.assignUserName != null){
		//起草页面,如果当前操作为制定办理人,且办理人角色下只对应一个用户,保存办理人(如果对应多个用户,需在用户认领任务页面保存办理人)
		addTransactor(data);
	}
	window.location.href="../tododocs/docs_down.html?roleName="+data.assignRoleName+"&fromPage=0002&operaterId="+data.operaterId+"&btnSource="+btnSource+"&mark="+mark;
}
function addTransactor(data){
	var dto = {
			id:data.businessKey,
			attnCode:data.assignUserCode
	};
	doJsonRequest("/senddoc/addReportText",dto,function(data){});
}
function goSuccessEnd() {
	window.location.href="../tododocs/docs_down.html?fromPage=0002&flag=1"+"&btnSource="+btnSource+"&mark="+mark;
}
function goVoidEnd(){
	window.location.href="../tododocs/docs_down.html?fromPage=0002&flag=3"+"&btnSource="+btnSource+"&mark="+mark;
}
function goSuccessSend(data){
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
					window.location.href = "../tododocs/docs_down.html?fromPage=0002&flag=8&btnSource="+btnSource+"&mark="+mark;
				} else {
					$.alert("创建失败。");
					return false;
				}
			},{showWaitting:true})				
		}else{
			window.location.href="../tododocs/docs_down.html?fromPage=0002&flag=0"+"&btnSource="+btnSource+"&mark="+mark;
		}
	}else{
		window.location.href="../tododocs/docs_down.html?fromPage=0002&flag=0"+"&btnSource="+btnSource+"&mark="+mark;
	}
}

function trim(str){ 
    return str.replace(/(^\s*)|(\s*$)/g, "");
}


function getAddData(){
	var attachmentDTOs = new Array();
	if(flag){
		weboffice.window.saveFileToUrl();
		attachmentDTOs = weboffice.window.getAttachmentDTOs();
		var officefileUrl= window.frames["weboffice"].document.getElementById('officeUrl').value;
		var textId= window.frames["weboffice"].document.getElementById('textId').value;
		$("#text").val(officefileUrl);
		$("#textId").val(textId);
	}
	var auditTime = "";
	if($("#auditTime").html()!=''){
		auditTime=$("#auditTime").html().split('日')[0].replace(/[^\d]/g,'-');
	}
	//公开属性原由
	var fnOpenTypeReason = "";
	var openTypeReason = $("#openTypeReason").html();//读出来的
	var lcOTReason = $("#lcOTReason").val();//当前输入的
	if(lcOTReason != ''){
		lcOTReason = lcOTReason + "  [" + $("#userName").val() + "  " + new Date().format("yyyy-M-d hh:mm") + "]";
	}
	if(openTypeReason == ''){
		fnOpenTypeReason = lcOTReason;
	}else if(lcOTReason == ''){
		fnOpenTypeReason = openTypeReason;
	}else{
		fnOpenTypeReason = lcOTReason + '<br>' +  openTypeReason ;
	}
	if($("#reportCode").val()!=null&&$("#reportCode").val()!=''&&$("#fullYear").val()!=null&&$("#fullYear").val()!=''){
		fullReportCode =$("#fullYear").val()+"_"+$("#reportCode").val();
	}
	var dto = {};
	//退回起草人
	if(userTask == 'usertask1'){
		dto = {
				id:id,
				reportTitle:$("#reportTitle").val(),
				sendUnit:$("#sendUnit").val(),
				ccUnit:$("#ccUnit").val(),
				attachments:$("#attachments").val(),
				//秘密等级
				secretLevel:$("#secretLevel").val(),
				//紧急程度
				urgency:$("#urgency").val(),
				//报备
				putOnRecords:$("#putOnRecords").val(),
				//属性
				openType:$("#openType").val(),
				//公开与否说明
				openTypeReason:fnOpenTypeReason,
				//备注
				remarks:$("#remarks").val(),
				attachmentDTOs:attachmentDTOs,
				text:$("#text").val(),
				textId:$("#textId").val(),
				flag:1,//当前为审批.
		}
		//核稿人审核
	}else if(userTask == 'usertask9'){
		dto = {
				id:id,
				hgOpinion:opStr($("#hgOpinion").val(),$("#hgOpinionSPAN").html()),//核稿人意见
				/*hgAuditing:$("#hg").html(),//核稿人签字
				hgDate:$("#hgDate").html(),*/
				/*proposedDate:proposedDate,*/
				proposedAdvice:opStr($("#proposedAdvice").val(),$("#proposedAdviceSPAN").html()),
				reportTitle:$("#reportTitle").val(),
				sendUnit:$("#sendUnit").val(),
				ccUnit:$("#ccUnit").val(),
				attachments:$("#attachments").val(),
				//秘密等级
				secretLevel:$("#secretLevel").val(),
				//紧急程度
				urgency:$("#urgency").val(),
				//报备
				putOnRecords:$("#putOnRecords").val(),
				//属性
				openType:$("#openType").val(),
				//公开与否说明
				openTypeReason:fnOpenTypeReason,
				//备注
				remarks:$("#remarks").val(),
				
				attachmentDTOs:attachmentDTOs,
				text:$("#text").val(),
				textId:$("#textId").val(),
				/*attachments:attaNames,*/
				flag:1,//当前为审批.
		}
	}else{
		dto = {
				id:id,
				fzbOpinion:opStr($("#fzbOpinion").val(),$("#fzbOpinionSPAN").html()),//法制办审核
				/*fzbAuditing:$("#fzb").html(),//法制办签字
				fzbDate:$("#fzbDate").html(),//法制办日期
*/				fmszOpinion:opStr($("#fmszOpinion").val(),$("#fmszOpinionSPAN").html()),//副秘书长意见
				/*fmszAuditing:$("#fmsz").html(),//副秘书长签字
				fmszDate:$("#fmszDate").html(),*/
				mszOpinion:opStr($("#mszOpinion").val(),$("#mszOpinionSPAN").html()),//秘书长意见
				/*mszAuditing:$("#msz").html(),//秘书长签字
				mszDate:$("#mszDate").html(),*/
				fszOpinion:opStr($("#fszOpinion").val(),$("#fszOpinionSPAN").html()),//副市长意见
				/*fszAuditing:$("#fsz").html(),//副市长签字
				fszDate:$("#fszDate").html(),*/
				szOpinion:opStr($("#szOpinion").val(),$("#szOpinionSPAN").html()),//市长意见
				/*szAuditing:$("#sz").html(),//市长签字
				szDate:$("#szDate").html(),*/
				theIssuer:$("#userName").val(),//去后台判断当前用户是否是完成流程的用户,是的话保存这个签发人字段,不是的话则去掉这个字段
				/*proposedDate:proposedDate,
				proposedAdvice:$("#proposedAdvice").val(),*/
				text:$("#text").val(),
				textId:$("#textId").val(),
				flag:1,//当前为审批.
				auditTime:auditTime,
				issued:$("#issued").val(),
				reportCode:fullReportCode,
				littlePage:getLittileMsg()
				/*attachmentDTOs:attachmentDTOs*/
		}
	}
	return dto;
}

function opStr(hgOpinion,hgOpinionSPAN){
	var hgOp = "";
	if(hgOpinion != ''){
		hgOpinion = hgOpinion + "  [" + $("#userName").val() + "  " + new Date().format("yyyy-M-d hh:mm") + "]";
		if(hgOpinionSPAN!=''){
			hgOp = hgOpinion + "<br>" + hgOpinionSPAN;
		}else{
			hgOp = hgOpinion;
		}
	}
	return hgOp;
}

function getLittileMsg(){
	var lps = $("#liPage").val();
	if(lps!=''){
		lps = lps + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]";
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
		lps = lps + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]";
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
		window.location.href="../tododocs/docs_down.html?fromPage=0002&flag=2&operaterId="+data.operaterId+"&btnSource="+btnSource +"&mark="+mark;
	},{showWaiting:true});
}

function getStatusData(){
	var dto = {
			id:id
	};
	return dto;
}

//当办理人分发文件或者办结文件时,可以保存文件编号/印发时间/印发份数(后续需要集成在套红操作中)
function getFFBJData(){
	if(flag){
		weboffice.window.saveFileToUrl();
		var officefileUrl= window.frames["weboffice"].document.getElementById('officeUrl').value;
		var textId= window.frames["weboffice"].document.getElementById('textId').value;
		$("#text").val(officefileUrl);
		$("#textId").val(textId);
	}
	var auditTime = "";
	if($("#auditTime").html()!=''){
		auditTime=$("#auditTime").html().split('日')[0].replace(/[^\d]/g,'-');
	}
	if($("#reportCode").val()!=null&&$("#reportCode").val()!=''&&$("#fullYear").val()!=null&&$("#fullYear").val()!=''){
		fullReportCode =$("#fullYear").val()+"_"+$("#reportCode").val();
	}
	var dto = {
			id:id,
			auditTime:auditTime,
			issued:$("#issued").val(),
			reportCode:fullReportCode,
			text:$("#text").val(),
			textId:$("#textId").val()
	};
	return dto;
	
}

function saveRedBtn(url){
	var dto = getStatusData();
	dto.text = url;
	addReportText(dto);
}

function addReportText(dto) {
	doJsonRequest("/senddoc/addReportText",dto,function(data){},{showWaiting:true});
}

function wf_getDate(data) {
	if($("#urgency").val() == '0001') {
		return getTheDate(86400);
	}
	if($("#urgency").val() == '0002') {
		return getTheDate(86400);
	}
	if($("#urgency").val() == '0003') {
		return getTheDate(86400);
	}
	if($("#urgency").val() == '0004') {
		return getTheDate(86400);
	}
}

//流程备注信息
function wf_getMark(operaterId){
	if(operaterId == 'flow26'){
		return "收到";
	}else if(operaterId == 'flow28'){
		return $("#hgOpinion").val();
	}else if(operaterId == 'flow44'){
		return $("#fmszOpinion").val();
	}else if(operaterId == 'flow45'){
		return $("#mszOpinion").val();
	}else if(operaterId == 'flow46'){
		return $("#fszOpinion").val();
	}else if(operaterId == 'flow47'){
		return $("#szOpinion").val();
	}else if(operaterId == 'flow51'){
		return $("#fzbOpinion").val();
	}else{
		return '';
	}
	
}

var nfwIdd = null;
function initNfwInfoA(NfwId) {
	nfwIdd = NfwId;
//	$("#showNfwInfoA").attr("href","../senddoc/new_nfw_check.html?id="+NfwId+"&type=0009").attr("target","_blank");
}

function showNfwInfo() {
	
	window.open("../senddoc/new_nfw_check.html?id="+nfwIdd+"&type=0009","_target")
}

//校验
function wf_beforeValid(opId){
	  //保存正文
	if(flag){
		weboffice.window.saveFileToUrl();
		var officefileUrl= window.frames["weboffice"].document.getElementById('officeUrl').value;
		var textId= window.frames["weboffice"].document.getElementById('textId').value;
		$("#text").val(officefileUrl);
		$("#textId").val(textId);
	}
	if(!$("#text").val().endWith(".pdf") && (opId == 'flow24' || opId == 'flow25')){
		$.alert('请将正文转为PDF。'); 
		return false;
	}
	//文件名称不能为空
	if($("#reportTitle").val() == ''){
		$.alert("文件标题不能为空");
		return false;
	}
    /*
        if($("#reportTitle").val().length>40){
		$.alert("文件标题应少-于40字");
		return false;
	}*/
	//秘密等级
	if($('input:checkbox[name="secretLevel"]:checked').val()==null){
		$.alert("请确认此文件为非涉密件");
		return false;
	}
	//紧急程度
	if($("#urgency").val() == ''){
		$.alert("请选择紧急程度");
		return false;
	}
	//属性
	if($('input:radio[name="ot"]:checked').val()==null){
		$.alert("请选择属性");
		return false;
	}
    /*
    if(validLength($("#lcOTReason").val())>40){
         $.alert("公开属性原因应少-于40字");
         return false;
    }
    if($("#remarks").val().length>35){
         $.alert("备注应少-于35字");
         return false;
    }
    
     if(validLength($("#proposedAdvice").val())>40){
        $.alert("审批单位意见应少-于40字");
        return false;
    }
    if($("#reportTitle").val().length>40){
		$.alert("文件标题应少-于40字");
		return false;
	}
         if($("#sendUnit").val().length>50){
		$.alert("主送应少-于50字");
		return false;
	}*/
         if(!$("#sendUnit").val()){
		$.alert("请填写主送单位");
		return false;
	} /*
         if($("#ccUnit").val().length>35){
		$.alert("抄送应少-于35字");
		return false;
               
	}
         if($("#attachments").val().length>4*38){
		$.alert("附件太长，应少-于150字");
		return false;
             
	}
       if(validLength($("#orgAudOpinion").val())>35){
            $.alert("主办单位审核应少-于35字");
            return false;
        }
         if($("#putOnRecords").val().length>10){
        
		$.alert("报备应少-于10字");
		return false;
           
	}*/
	//公开属性原由
	if(($("#openType").val() == '0002' || $("#openType").val() == '0003') && $("#openTypeReason").html() == ''){
		$.alert("请填写"+$("#fileAttr").html()+"原由");
		return false;
	}
	
	//领导签字校验:
	if(userTask == 'usertask9'){
		if($("#hgOpinion").val()=='' && $("#hgOpinionSPAN").html()==''){
			$.alert('请填写审批意见');
			return false;
		}/*
                if(validLength($("#hgOpinion").val())>35){
			$.alert('审批意见应少 于35字');
			return false;
		}*/
        if($("#proposedAdvice").val()==''&&$("#proposedAdviceSPAN").html().length===0){
			$.alert('请填写办公厅审核意见');
			return false;
		}  
	}
	if(userTask == 'usertask10'){
		if($("#fmszOpinion").val()=='' ){
			$.alert('请填写审批意见');
			return false;
		}
        /*
        if(validLength($("#fmszOpinion").val())>35){
			$.alert('审批意见应少-于35字');
			return false;
		}*/
	}
	if(userTask == 'usertask11'){
		if($("#mszOpinion").val()=='' ){
			$.alert('请填写审批意见');
			return false;
		}/*
                if(validLength($("#mszOpinion").val())>35){
			$.alert('审批意见应少-于35字');
			return false;
		}*/
	}
	if(userTask == 'usertask12'){
		if($("#fszOpinion").val()=='' ){
			$.alert('请填写审批意见');
			return false;
		}
        /*
        if(validLength($("#fszOpinion").val())>35){
			$.alert('审批意见应少-于35字');
			return false;
		}*/
	}
	
	if(userTask == 'usertask17'){
        if($("#fzbOpinion").val()=='' && $("#fzbOpinionSPAN").html()==''){
			$.alert('请填写审批意见');
			return false;
        }
        /*
        if(validLength($("#fzbOpinion").val())>35){
			$.alert('审批意见应少-于35字');
			return false;
        }*/
    }
	if(userTask == 'usertask13'){
		if($("#szOpinion").val()==''){
			$.alert('请填写审批意见');
			return false;
		}/*
        if(validLength($("#szOpinion").val())>30){
			$.alert('审批意见应少-于30字');
			return false;
		} */
	}
    window.scrollTo(0,0);
	return true;
}

//打印控制：返回正文id
function wf_getPrintInfo(operaterId){
	var dto = {};
	if("flow24"==operaterId||"flow25"==operaterId){
		var tempDTO= [{businessId:id,businessType:businessType,textId:$("#textId").val()}];
		dto.docLists = tempDTO;
		dto.countLists = [{roleCode:roleCode,count:'99'}];
	}
	
	return dto;
};


function beforePrint() {
	if($("#reportCode").val() == null || ($("#reportCode").val().length ==0)) {
		
		$("#secCode").addClass("noPrint");
	}
}

function afterPrint() {
	if($("#reportCode").val() == null || ($("#reportCode").val().length ==0)) {
		$("#secCode").removeClass("noPrint");
	}
}
	
function hideAll(){
	$("#szOpinion").hide();
	$("#fszOpinion").hide();
	$("#mszOpinion").hide();
	$("#fmszOpinion").hide();
	$("#fzbOpinion").hide();
	$("#hgOpinion").hide();
	$("#proposedAdvice").hide();
}