$("#textPage a").tab("show");
var id = getURLParam("id"); //当前收文id
var fromPage = getURLParam("fromPage"); //页面来源
var type = getURLParam("type"); //页面来源
var taskId = getURLParam("taskId");  
var textIframe = "";//正文iframe地址
var mark = getURLParam("mark");//当前是否是市长秘书点击进来的 002:是 
var allBtn = "revision_final_saveFile_savePDF";//weboffice按钮控制
$("#id").val(id);
$("#businessKey").val(id);
$("#adviceDraftId").val(id);
$("#reportType").val(type);
var infoCollFlag = "";
var roleCode = "";
var roleName = "";
//鼠标移入触发正文保存事件
var wodto = {};
var businessType = getURLParam("businessType");
var realRoleCode = null;
$(function(){
	//获取当前登陆人信息
	getCurrenUserInfo(function(data){
		$("#userCode").val(data.userCode);
		$("#userOrg").val(data.orgName);
		$("#userName").val(data.userName);
		$("#phoneNumber").val(data.phoneNumber);
		roleCode = data.roleCodes;
		roleName = data.roleNames;
		isMayor = data.isMayor;
		var redHfes = data.redHeadFileEntities;
		realRoleCode = data.realRoleCode;
		for(var i = 0;i<redHfes.length;i++){
			var redHfe = redHfes[i];
			if(type == redHfe.reportType){
				var span = $("<span></span>");
				var btn = $(document.createElement("button")).addClass('btn').addClass('btn_click').attr('id','redBtn').attr('display','none').html(redHfe.mark).data('redData',redHfe);
				btn.appendTo(span);
				btn.click(function(){
					var thisRed = $(this).data('redData');
					//读取模板文件套红(URL:文件路径;endUrl:尾部套红路径；code:套红编号;ccuser;套红抄送人;company:发文单位(ZF政府、BGT办公厅);)
					var URL = baseRedHeadUrl+thisRed.redHeadTemp;
					var endUrl = baseRedHeadUrl+thisRed.endTemp;
					weboffice.window.insertRedHeadFromUrl(URL,endUrl,'001','','');
					$(this).hide();
				});
				span.appendTo($("#buttonList"));
				$("#redBtn").hide();
			}
		}
	if(id != null) {
		$("#id").val(id);
		var dto = {
				id:id
		}
		doJsonRequest("/receivedoc/getReceiveDoc",dto,function(data){
			if(data.result) {
				var data = data.data;
				//来文类型
				$("#reportType").val(data.reportType);
				//来文单位
				$("#docCameOrgan").val(data.docCameOrgan);
				//文号
				$("#receiveCode").val(data.receiveCode);
				//秘密等级
				$("input[name='secretLevel'][value='"+data.secretLevel+"']").attr("checked",'checked');
				//紧急程度
				if(data.urgency!='' && data.urgency!=null ){
					$("#urgency").val(data.urgency);
					$("#spanUrgency").html($("#urgency").find("option:selected").text());
				}
				//文件标题
				$("#receiveTitle").val(data.receiveTitle);
				//秘书审核
				$("#fmszOpinion").val(data.fmszOpinion);
				$("#mszOpinion").val(data.mszOpinion);
				var mszpsAudit = "";
				if(data.mszOpinion!='' && data.mszOpinion!=null){
					mszpsAudit += data.mszOpinion + '<br>';
				}
				if(data.fmszOpinion!='' && data.fmszOpinion!=null){
					mszpsAudit += data.fmszOpinion;
				}
				$("#mszpsAudit").html(mszpsAudit);
				//市长审核
				$("#fszOpinion").val(data.fszOpinion);
				$("#szOpinion").val(data.szOpinion);
				var ldpsAudit = "";
				if(data.szOpinion!='' && data.szOpinion!=null){
					ldpsAudit += data.szOpinion + '<br>';
				}
				if(data.fszOpinion!='' && data.fszOpinion!=null){
					ldpsAudit += data.fszOpinion;				
				}
				$("#ldpsAudit").html(ldpsAudit);
				$("#status").val(data.status);
				if(data.status == "0006"){
					$("#titleFlag").html($("#titleFlag").html()+"(已分发)");
				}
				//呈批意见
				//$("#hgOpinion").val(data.hgOpinion);
                $("#hgOpinionSPAN").html(data.hgOpinion);
				//备注
				$("#remark").val(data.remark);
				//正文
				$("#text").val(data.text);
				$("#textId").val(data.textId);
				infoCollFlag = data.infoCollFlag;
				$("#infoCollFlag").val(data.infoCollFlag);
				$("#drafterName").val(data.drafterName);
				$("#drafterPhoneNum").val(data.drafterPhoneNum);
				if(data.receiveDate!=''&&data.receiveDate!=null){
					$("#receiveDate").html(new Date(data.receiveDate).format('yyyy年M月d日'));
				}
				if(data.undertakeDate!=''&&data.undertakeDate!=null){
					$("#undertakeDate").html(new Date(data.undertakeDate).format('yyyy年M月d日'));
				}
				$("#lwCode").val(data.lwCode);
				$("#upCardFlag").val(data.upCardFlag);
				$("#createUserName").val(data.createUserName);
				//小纸条
				/*$("#liPage").val(data.littlePage);*/
				$("#lPageSpan").html(data.littlePage);
				$("#isStartSolicit").val(data.isStartSolicit);
				//若当前文件没有转办过其他委局，则不显示信息汇总卡
				var xxhzkflag = false;
				if(data.isStartSolicit=='1' || data.isStartSolicit=='2'){
					xxhzkflag = true;
				}
				var isAdviceDraft = '';
				if($("#text").val().indexOf("~!~!")>0){
					//当前为征求过委局意见的正文
					isAdviceDraft = '2';
				}
				textIframe = '<iframe id="webOfficeIframe" name="weboffice" src="../weboffice/weboffice2.html?officeUrl='+$("#text").val()+'&showPrintBtn='+$("#status").val()+'&textId='+$("#textId").val()+'&isStart=0&taskid='+id+'&isAdviceDraft='+isAdviceDraft+'" style="z-index:9999;width:100%;height:900px;"></iframe>';
				if(data.taskIds!=null && data.taskIds!=''){
					//var taskIds = parseInt(data.taskIds)+1;
					var taskIds = data.taskIds;
					$("#approvalDiv").append('<input type="hidden" id="orderId" name="orderId" value="'+taskIds+',9999999999999">');
				}
				$("#isReturn").val(data.isReturn);
				doQueryWF("reportInfo","approvalDiv");
				var dto = {taskId:taskId}
				//初始化流程按钮
				wf_getOperator(dto,function(data){
					userTask = data.userTask;//获取当前流程
					
					if(userTask!="usertask6" && userTask!="usertask7"){
						$("#buttonList").show();
					}
					
                    if(userTask=="usertask2"){
                    	if(xxhzkflag){
                    		$("#xxhzk").show();
                    	}
                          $("#urgency").removeAttr("disabled");
                    }
					//根据当前步骤控制页面可输入项:
					if(userTask == 'usertask1'){
						//退回起草人的时候只显示作废按钮
						if($("#upCardFlag").val() != '1'){
							$("#qsy").hide();
							$("#textPage").trigger("click");
						}else{
							$("#qsy").show();
							$("#qsy a").tab("show");
						}
						if($("#isReturn").val()==1){
							$("#buttonList span").hide();
							$("#toVoid").show();
							/*$("#buttonList").empty().append('<span wf-id="flow30"></span>');*/
						}
					}else if(userTask == 'usertask2' && $("#isStartSolicit").val() == '1'){
						if(xxhzkflag){
                    		$("#xxhzk").show();
                    	}
						//办理人在征求委局意见，委局回复后，办理人打开页面
						//打开时默认显示正文页:
						$("#qsy").hide();
						//将此条来文记录内容删除，以便重新上卡重新填写信息
							$("#upCard").removeClass("hidden");
							$("#zxqtwj").hide();//没有上卡不能直接咨询其他委局
							$("#tjwsbzr").hide();//没有上卡不能提交文书审核
							$("#ldshBtn").hide();//没有上卡不能提交领导审核
							$("#ff").hide();//没有上卡不能分发
							$("#wc").hide();//没有上卡不能完成
							//处理正文页，生成所有委局回复的正文
							textIframe = '<iframe id="webOfficeIframe" name="weboffice" src="../weboffice/weboffice2.html?officeUrl=&isStart=0&showPrintBtn='+$("#status").val()+'&taskid='+id+'&isAdviceDraft=2" style="z-index:9999;width:100%;height:900px;"></iframe>';
							$("#textPage").trigger("click");
							
							$("#upCard").click(function(){
								$("#tjwsbzr").show();//没有上卡不能提交文书办主任
								//doJsonRequest("/receivedoc/removeContent",dto,function(data){
								$("#qsy").show();
								$("#qsy a").tab("show");
								$(this).hide();
								//将普通选项置为可选:
								
								if(flag){
									var dtoo = weboffice.window.getAllData();
									//重置请示卡：
									$("#docCameOrgan").val(dtoo.docCameOrgan);
									$("#receiveCode").val(dtoo.receiveCode);
									$("#receiveTitle").val(dtoo.receiveTitle);
								}
								$("#ldpsAudit").html('');
								$("#szOpinion").val('');
								$("#fszOpinion").val('');
								$("#mszpsAudit").html('');
								$("#mszOpinion").val('');
								$("#fmszOpinion").val('');
								$("#hgOpinionSPAN").html('');
								
								$("#remark").val('');
								
								$("#docCameOrgan").removeAttr("disabled");
								$("#receiveCode").removeAttr("disabled");
								$("#urgency").show().val('0004');
								$("#spanUrgency").hide();
								$("#receiveTitle").removeAttr("disabled");
								$("#remark").removeAttr("disabled");
								$("#lwCode").removeAttr("disabled");
								$("#drafterName").removeAttr("disabled");
								$("#drafterPhoneNum").removeAttr("disabled");
								//点击上卡,生成承办时间,承办人,承办电话,承办编号
								var date = new Date();
								var undertakeDate = date.format('yyyy年M月d日');
								$("#undertakeDate").html(undertakeDate);
								$("#drafterName").val($("#userName").val());
								$("#drafterPhoneNum").val($("#phoneNumber").val());
								doJsonRequest("/code/getCode","5002",function(data){
									if(data.result) {
										$("#lwCode").val(data.data);
									} else {
										$.alert("获取编码出错。");
									}
								});
								//});
							});
					}else if($("#upCardFlag").val() != '1' && userTask == 'usertask2'){
						if(xxhzkflag){
                    		$("#xxhzk").show();
                    	}              
						//办理人第一次进入页面时需要点击上卡按钮进行上卡
						//打开时默认显示正文页:
						$("#qsy").hide();
						$("#zxqtwj").hide();//没有上卡不能直接咨询其他委局
						$("#tjwsbzr").hide();//没有上卡不能提交文书审核
						$("#ldshBtn").hide();//没有上卡不能提交领导审核
						$("#ff").hide();//没有上卡不能分发
						$("#wc").hide();//没有上卡不能完成
						//$("#textPage a").tab("show");
						$("#upCard").removeClass("hidden");
						$("#textPage").trigger("click");
						//上卡
						$("#upCard").click(function(){
							$("#tjwsbzr").show();//没有上卡不能提交文书办主任
							$("#qsy").show();
							$("#qsy a").tab("show");
							$(this).hide();
							
							//将普通选项置为可选:
							$("#docCameOrgan").removeAttr("disabled");
							$("#receiveCode").removeAttr("disabled");
							$("#urgency").show().val('0004');
							$("#spanUrgency").hide();
							$("#receiveTitle").removeAttr("disabled");
							$("#remark").removeAttr("disabled");
							$("#lwCode").removeAttr("disabled");
							$("#drafterName").removeAttr("disabled");
							$("#drafterPhoneNum").removeAttr("disabled");
							//点击上卡,生成承办时间,承办人,承办电话,承办编号
							var date = new Date();
							var undertakeDate = date.format('yyyy年M月d日');
							$("#undertakeDate").html(undertakeDate);
							$("#drafterName").val($("#userName").val());
							$("#drafterPhoneNum").val($("#phoneNumber").val());
							doJsonRequest("/code/getCode","5002",function(data){
								if(data.result) {
									$("#lwCode").val(data.data);
								} else {
									$.alert("获取编码出错。");
								}
							});
						});
					}else{
						$("#qsy a").tab("show");
					}
					if(userTask == 'usertask3'){
						if(xxhzkflag){
                    		$("#xxhzk").show();
                    	}
						$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
						//核稿
						$("#hgOpinion").removeAttr("disabled");
					}
					if(userTask == 'usertask4'||userTask == 'usertask5'){
						if(xxhzkflag){
                    		$("#xxhzk").show();
                    	}
						$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
						//秘书
						$("#mszps").removeAttr("disabled");
					}
					if(userTask == 'usertask6'){
						if(xxhzkflag){
                    		$("#xxhzk").show();
                    	}
						if(realRoleCode!=null && realRoleCode!='null' && realRoleCode!=''){
							$("#ldps").removeAttr("disabled");
							$("#buttonList").show();
							$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
						}else{
							$("#ldps").attr('disabled',true);
							$("#buttonList").empty().show().append('<span style="line-height:35px;color:blue;font-weight:bold;">领导文件</span>');
						}
					}
					if(userTask == 'usertask7'){
						if(xxhzkflag){
                    		$("#xxhzk").show();
                    	}
						//市长
//						$("#littlePage").show();
						/*if(mark=='002' && $("#userCode").val()!='lijm'){
//							$("#buttonList").append("<button type='button'  class='btn btn_click'  onclick='saveLittlePage()' style='margin-left:8px;' >保存便签</button>");
							$("#ldps").attr('disabled',true);
							$("#buttonList span").hide();
						}else{
							$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
						}*/
//						$("#liPage").removeAttr("disabled");
						
						if(realRoleCode!=null && realRoleCode!='null' && realRoleCode!=''){
							$("#ldps").removeAttr("disabled");
							$("#buttonList").show();
							$("#buttonList").append("<button type='button' onclick='commonLanguage()' class='btn btn_click'>常用语</button>");
						}else{
							$("#ldps").attr('disabled',true);
							$("#buttonList").empty().show().append('<span style="line-height:35px;color:blue;font-weight:bold;">领导文件</span>');
						}
						
					}
					if(userTask == 'usertask8'){
						/*if(mark=='002' && $("#userCode").val()!='lijm'){//分发给市长，市长秘书进入时隐藏确认收文按钮
							$("#beSended").hide();
						}*/
						//分发给市长，市长秘书进入时隐藏确认收文按钮
						if((mark=='002' && realRoleCode!='002') || (mark=='048' && realRoleCode!='048') || (mark=='051' && realRoleCode!='051') || (mark=='049' && realRoleCode!='049') || (mark=='003' && realRoleCode!='003') || (mark=='050' && realRoleCode!='050') || (mark=='039' && realRoleCode!='039') || (mark=='043' && realRoleCode!='043')){
							$("#buttonList").empty().show().append('<span style="line-height:35px;color:blue;font-weight:bold;">领导文件</span>');
						}
					}
					//征求委局意见
					if(userTask == 'usertask9'){
						flag = true;
						$("#qsy").hide();
						$("#lcgz").hide();
						$('#textPage').unbind("click");
						$("#textPage a").tab("show");
						$("#textIframe").empty().append('<iframe id="webOfficeIframe" name="weboffice" src="../weboffice/weboffice2.html?officeUrl=&showPrintBtn='+$("#status").val()+'&reportType=5002&isStart=1&taskid='+id+'&allBtn='+allBtn+'&isAdviceDraft=1" style="z-index:9999;width:100%;height:900px;"></iframe>');
						$("#redBtn").show();
					}
					
					//优化领导签批样式：
					if($("#ldps").attr("disabled") == "disabled"){
						$("#ldps").hide();
					}
					if($("#mszps").attr("disabled") == "disabled"){
						$("#mszps").hide();
					}
                    if($("#hgOpinion").attr("disabled") == "disabled"){
						$("#hgOpinion").hide();
					}
				});
			} else {
				$.alert("获取信息失败。");
			}
		});
	}
	});
});

function getAddData(){
	//核稿
	var hgOpinion = $("#hgOpinion").val();
	var hgOp = hgOpinion.replace(/[ ]/g,"");
	if(userTask == 'usertask3'){
		if(hgOp!='' && hgOpinion.indexOf('[')<=-1 && hgOpinion.indexOf(']')<=-1){
			hgOpinion = hgOpinion + "  [" + $("#userName").val() + "  " + new Date().format('yyyy-M-d hh:mm') + "]<br>"+$("#hgOpinionSPAN").html();
		}else{
			hgOpinion = $("#hgOpinionSPAN").html();
		}
	}
	//秘书长审批
	var fmszOpinion = $("#fmszOpinion").val();
	var mszOpinion = $("#mszOpinion").val();
	
	var mszps = $("#mszps").val();
	if(mszps!=''){
		mszps = mszps + "  [" + $("#userName").val()+"  "+new Date().format('yyyy-M-d hh:mm') + "]";
		if(userTask == 'usertask4'){
			//如果当前环节为副秘书长审批
			if(fmszOpinion!=''){
				fmszOpinion = mszps + "<br>" + fmszOpinion;
			}else{
				fmszOpinion = mszps;
			}
		}else if(userTask == 'usertask5'){
			//如果当前环节为秘书长审批
			if(mszOpinion!=''){
				mszOpinion = mszps + '<br>' + mszOpinion;
			}else{
				mszOpinion = mszps;
			}
		}
	}
	
	//市长审批
	var fszOpinion = $("#fszOpinion").val();
	var szOpinion = $("#szOpinion").val();
	var ldps = $("#ldps").val();
	if(ldps!=''){
		ldps = ldps + "  [" + $("#userName").val()+"  "+new Date().format('yyyy-M-d hh:mm') + "]";
		if(userTask == 'usertask6'){
			//如果当前环节为副市长审批
			if(fszOpinion!=''){
				fszOpinion = ldps + '<br>' + fszOpinion;
			}else{
				fszOpinion = ldps;
			}
		}else if(userTask == 'usertask7'){
			//如果当前环节为市长审批
			if(szOpinion!=''){
				szOpinion = ldps + '<br>' + szOpinion;
			}else{
				szOpinion = ldps;
			}
		}
	}
	//var attachmentDTOs = new Array();

	var dto = {};
	if(userTask == 'usertask1'){
		dto = {
				id:$("#id").val(),
				upCardFlag:'1',//将已上卡的标示添加到数据库
				flag:1,//当前为审批流程.
		};
	}else if(userTask == 'usertask2'){
		if($("#upCardFlag").val()!='1' || $("#isStartSolicit").val() == '1'){
			//办理人环节，若当前为未上卡
			dto = {
					id:$("#id").val(),
					//来文类型
					reportType:$("#reportType").val(),
					//来文单位
					docCameOrgan:$("#docCameOrgan").val(),
					//文号
					receiveCode:$("#receiveCode").val(),
					secretLevel:$('input:checkbox[name="secretLevel"]:checked').val(),
					//紧急程度
					urgency:$("#urgency").val(),
					//文件标题
					receiveTitle:$("#receiveTitle").val(),
					//备注
					remark:$("#remark").val(),
					lwCode:$("#lwCode").val(),
					//正文
					text:$("#text").val(),
					textId:$("#textId").val(),
					//附件
					//attachmentDTOs:attachmentDTOs,
					status:'0002',
					drafterCode:$("#userCode").val(),
					drafterName:$("#drafterName").val(),
					drafterPhoneNum:$("#drafterPhoneNum").val(),
					upCardFlag:'1',//将已上卡的标示添加到数据库
					receiveDate:$("#receiveDate").html().split('日')[0].replace(/[^\d]/g,'-'),
					undertakeDate:$("#undertakeDate").html().split('日')[0].replace(/[^\d]/g,'-'),
					contactPerson:wodto.contactPerson,
					contactNumber:wodto.contactNumber,
					infoCollFlag:infoCollFlag,
					flag:1,//当前为审批流程.
				};
			if($("#isStartSolicit").val() == '1'){
				//当前为委局回复后的重新上卡环节，将主线的转办标示置为2，便于做是否显示信息汇总卡的判断依据。
				dto.isStartSolicit = '2'
			}
		}else{
			//办理人正常办理，不需要操作。
			dto = {
					id:$("#id").val(),
					ignore:true
				};
		}
	}else if(userTask == 'usertask3'){
		//核稿
		dto = {
				id:$("#id").val(),
				//呈批意见
				hgOpinion:hgOpinion,
				status:'0002',
				flag:1,//当前为审批流程.
			};
	}else if(userTask == 'usertask4' ||  userTask == 'usertask5'){
		//秘书长审核
		dto = {
				id:$("#id").val(),
				//秘书审核
				fmszOpinion:fmszOpinion,
				mszOpinion:mszOpinion,
				flag:1,//当前为审批流程.
			};
	}else if(userTask == 'usertask6' ||  userTask == 'usertask7'){
		//市长审核
		dto = {
				id:$("#id").val(),
				//市长审核
				fszOpinion:fszOpinion,
				szOpinion:szOpinion,
				littlePage:getLittileMsg(),
				//附件
				//attachmentDTOs:attachmentDTOs,
				flag:1,//当前为审批流程.
			};
	}else{
		dto = {
				id:$("#id").val(),
				flag:1,//当前为审批流程.
		};
	}
		return dto;
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
var btnSource = "app";
var appFlag = getURLParam("appFlag"); 
if(fromPage == 'pc' && appFlag != 1){
	btnSource = fromPage;
}
function goSuccess(data) {
	var roleName = data.assignRoleName;
	window.location.href="../tododocs/docs_down.html?roleName="+roleName+"&fromPage=0002&operaterId="+data.operaterId+"&btnSource="+btnSource+"&mark="+mark;
}
//委局意见回复
function goAdviceSend() {
	window.location.href="../tododocs/docs_down.html?fromPage=0002&flag=4&btnSource="+btnSource+"&mark="+mark;
}
//分发
function goSuccessSend(data) {
	if("flow29" == data.operaterId && isMayor.indexOf(10)!=-1){
		//如果当前为可以创建一事一表的角色，则提示是否创建一事一表
		if(confirm("是否通知责任部门创建一事一表？")){
			var dto = {
					docId:$("#id").val(),
					docType:$("#reportType").val(),
					docTitle:$("#receiveTitle").val(),
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
			window.location.href="../tododocs/docs_down.html?fromPage=0002&flag=0&btnSource="+btnSource+"&mark="+mark;
		}
	}else{
		window.location.href="../tododocs/docs_down.html?fromPage=0002&flag=0&btnSource="+btnSource+"&mark="+mark;
	}
}
//办结
function goSuccessEnd() {
	window.location.href="../tododocs/docs_down.html?fromPage=0002&flag=1&btnSource="+btnSource+"&mark="+mark;
}
//作废
function goVoidEnd(){
	window.location.href="../tododocs/docs_down.html?fromPage=0002&flag=3&btnSource="+btnSource+"&mark="+mark;
}
//正文加载webOffice控件
var flag = false;
$("#textPage").click(function(){
	if(!flag){
		$("#textIframe").append(textIframe);
		flag = true;
	}
});


/*
 * 其他委局回复办公厅征求的意见时点击’回复‘按钮触发：
 * 点击流程按钮时,默认先触发请示页的点击事件,以避免正文页窗体在最前端显示的问题
 */
/*$("#qtwjhf").on('mouseover',function(){
	//保存正文
		
});*/


//校验
function wf_beforeValid(){
	
	//保存正文
	if(flag){
		weboffice.window.saveFileToUrl();
		wodto = weboffice.window.getAllData();
		attachmentDTOs = weboffice.window.getAttachmentDTOs();
		if(wodto.text!=''){
			$("#text").val(wodto.text);
		}else{
			$("#text").val(window.frames["weboffice"].document.getElementById('officeUrl').value);
		}
		$("#textId").val(wodto.textId);
	}
	
	if(userTask == 'usertask1'){
		//退回起草人后起草人登陆时，不做校验。
		return true;
	}else if(userTask=='usertask9'){
		if(wodto.receiveTitle == ''){
			$.alert("请输入文件标题");
			return false;
		}
                /*
		if(wodto.receiveCode == ''){
			$.alert("请输入文件编号");
			return false;
		}
        */
		if(wodto.secretLevel == null){
			$.alert("请确认此文件为非涉密件");
			return false;
		}
        /*
                 if (wodto.receiveCode.length>25) {
                           $.alert("文件编号应少-于25字");
                            return false;
                         }*/
		if (wodto.opinion == '') {
	        $.alert("请选择回复意见");
	        return false;
	    }
	    if (wodto.opinion == '0003' && wodto.replyRemark == '') {
	        $.alert("请输入备注信息");
	        return false;
	    }
		if(!$("#text").val().endWith(".pdf")){
			$.alert("请将正文转为PDF");
			return false;
		}
	}else{
		/*if( $("#qsy").is(":hidden")){
			$.alert("请填写呈批单");
			return false;
		}*/
		//文件名称不能为空
		if($("#receiveTitle").val() == ''){
			$.alert("文件标题不能为空");
			return false;
		}
        /*
                if($("#receiveTitle").val().length>60){
			$.alert("文件标题应少-于60字");
			return false;
		}*/
                if($("#docCameOrgan").val()==""){
                    $.alert("来文单位不能为空");
			return false;
                }
        /*
                 if($("#docCameOrgan").val().length>40){
                    $.alert("来文单位应少-于40字");
			return false;
                }*/
                if($("#receiveCode").val()==""){
                     $.alert("文号不能为空");
			return false;
                }
                if($("#lwCode").val().length>5){
                     $.alert("编号应少于5位");
			return false;
                }
                  var re = /^[0-9]+.?[0-9]*$/;
                if($("#lwCode").val()&&!re.test($("#lwCode").val())){
                   
                     $.alert('编号不合理'); 
                    return false;
                 }
        /*
                  if($("#receiveCode").val().length>25){
                     $.alert("文号应少-于25字");
			return false;
                }*/
        /*
                if($("#remark").val().length>50){
                    $.alert("备注应少 于50字");
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
		
		//呈批意见
		if(userTask == 'usertask3'){
	        if(  $("#hgOpinion").val() == ''&&($("#hgOpinionSPAN").html().length===0)){
	            $.alert("请输入呈批意见");
	            return false;
	        }
            /*
	        if(validLength($("#hgOpinion").val())>40){
	        	$.alert("呈批意见应少-于40字");
	        	return false;
	        }*/
             
		}else if((userTask == 'usertask4' ||  userTask == 'usertask5') ){
		//秘书长审核
            if($("#mszps").val()==''){
				$.alert("请输入批示意见");
				return false;
            }
            /*
            if(validLength($("#mszps").val()) >40){
				$.alert("批示意见应少-于40字");
				return false;
            }*/
		}else if((userTask == 'usertask6' ||  userTask == 'usertask7')){
		//领导审核
			if( $("#ldps").val()==''){
               $.alert("请输入批示意见");
               return false; 
            }
			/*
	        if(validLength($("#ldps").val()) >40){
				$.alert("批示意见应少 于40字");
				return false;
			}*/
                
		}
	}
	window.scrollTo(0,0);
	return true;
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
	doJsonRequest("/receivedoc/addLittlePage",dto,function(data){
             $("#lPageSpan").html(dto.littlePage);
            $("#liPage").val("");
            $.alert("保存成功");
            return;
		window.location.href="../tododocs/docs_down.html?fromPage=0002&flag=2&operaterId="+data.operaterId+"&btnSource="+btnSource+"&mark="+mark;
	},{showWaiting:true});
}


function wf_getMark(operaterId){
	if(operaterId == 'flow28' || operaterId == 'flow26'){
		//领导审批
		return $("#ldps").val();
	}else if(operaterId == 'flow24' || operaterId == 'flow15'){
		//秘书审批
		return $("#mszps").val();
	}else if(operaterId=='flow11'){
		//文书核稿
		return $("#hgOpinion").val();
	}else if(operaterId=='flow29'){
		return "确认收文";
	}else if(operaterId=='flow8'){
		//分发时默认意见为空格
		return " ";
	}else{
		return "";
	}
}

function takeAdviceData(){
	dto = {
			id:$("#id").val(),
			isStartSolicit:'1',
	}
	return dto;
}

function adviceDraft(dom){
	//点击起草意见
/*	var dto = {id:$("#id").val(),};
	doJsonRequest("/receivedoc/removeContent",dto,function(data){});*/
	$("#qsy").hide();
	$("#lcgz").hide();
	$('#textPage').unbind("click");
	$("#textPage a").tab("show");
	$("#textIframe").empty().append('<iframe id="webOfficeIframe" name="weboffice" src="../weboffice/weboffice2.html?officeUrl=&showPrintBtn='+$("#status").val()+'&allBtn='+allBtn+'&isStart=1&taskid='+id+'&isAdviceDraft=1" style="z-index:9999;width:100%;height:900px;"></iframe>');
	
	//$("#buttonList").append("<span><button type='button' class='btn btn_click'  onclick='redBtn(this)'>套红</button></span>");
	$("#redBtn").show();
	$(dom).hide();
}

/**
 * 征求委局意见后，委局起草的回复意见
 */
function adviceDraftData(){
	var dto = {
			adviceDraftId:$("#id").val(),
			reportType:$("#reportType").val(),
			receiveTitle:wodto.receiveTitle,
			receiveCode:wodto.receiveCode,
			secretLevel:wodto.secretLevel,
			docCameOrgan:wodto.docCameOrgan,
			opinion:wodto.opinion,
			replyRemark:wodto.replyRemark,
			contactPerson:wodto.contactPerson,
			contactNumber:wodto.contactNumber,
			//正文
			text:$("#text").val(),
			textId:$("#textId").val(),
			upCardFlag:'2',
			//附件
			attachmentDTOs:attachmentDTOs,
			status:'0000',
			infoCollFlag:infoCollFlag
	}
	return dto;
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
	
	var thisText = "";
	if(userTask == 'usertask3'){
		thisText = $("#hgOpinion").val();
		$("#hgOpinion").val(thisText+language);
		$("#hgOpinion")[0].focus();
	}else if(userTask == 'usertask4' || userTask == 'usertask5'){
		thisText = $("#mszps").val();
		$("#mszps").val(thisText+language);
		$("#mszps")[0].focus();
	}else if(userTask == 'usertask6' || userTask == 'usertask7'){
		thisText = $("#ldps").val();
		$("#ldps").val(thisText+language);
		$("#ldps")[0].focus();
	}
	
/*	var $_dom = $("#hgOpinion,#mszps,#ldps");
	$_dom.each(function(){
		if(!$(this).attr("disabled")){
			$(this).val($(this).val()+language);
			return false;
		}
	});*/
}
//----------常用语（结束）------------
$("#xxhzk").click(function(){
	doQuery("reportInfoQuery");
})

//下级来文打印控制：返回所有正文id
function wf_getPrintInfo(operaterId){
	var dto = {};
	if("flow8"==operaterId){
		var tempDTO=null;
		doJsonRequest("/receivedoc/getAllRelationIds",{id:id},function(data){
			if(data.result){
				tempDTO= data.data;
			}
		},{async:false});
		dto.docLists = tempDTO;
		dto.countLists = [];
	}
	if("flow32"==operaterId){
		var tempDTO= [{businessId:id,businessType:businessType,textId:$("#textId").val()}];
		dto.docLists = tempDTO;
		dto.countLists = [{roleCode:roleCode,count:'99'}];
	}
	return dto;
}