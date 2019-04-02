var emailUrl = "";
$(function() {
	var tbid = getURLParam("userid");
	doRequest("/user/getEmailInfo",null,function(data){
		if(data.result=true){
			emailUrl = data.data.url;
		}
	});
	if (tbid != null) {
		$('input[name="userId"]').val(tbid);
		$('input[name="userCode"]').attr("readonly","readonly");
		$('input[name="userCode"]').removeAttr("ajaxurl");
		getUserInfo(tbid);
	} else {
		$('input[name="userId"]').val("");
		$('input[name="userCode"]').focusout(
				function(){
					$('input[name="email"]').val($('input[name="userCode"]').val()+"@dt.gov.cn");
				});
	}
	// 配置artDialog全局默认参数
	(function(config) {
		config['lock'] = true;
		config['fixed'] = true;
		config['resize'] = false;
		config['background'] = '#000';
		config['opacity'] = 0.5;
	})($.dialog.defaults);
	// toolTip
	$('[rel="tooltip"]').tooltip();
	// 表单提示
	$.extend($.Datatype,{
		"nzh" : /^[^\u3400-\u9FFF]*$/,
		"sjh" : /^([0-9]{11},){0,3}$/
	});
	$.Tipmsg.w["nzh"]="密码中不能出现中文"
	$.Tipmsg.w["sjh"]="手机号码必须为11位数字，末尾需加英文逗号,号码总数少于3个。";
	$("[datatype]").change(
			function() {
				$(this).parent().addClass('info').children('.infomsg').show()
						.siblings('.help-inline').hide();
			}).focusout(
			function() {
				$(this).parent().removeClass('info').children('.infomsg')
						.hide().siblings('.help-inline').show();
			});
	// detailIframe
	openDetailIframe = function(url) {
		ZENG.msgbox.show('正在载入中，请稍后...', 6, 100000);
		$('#detailIframe iframe').attr('src', url).load(function() {
			$('body').css('overflow', 'hidden');
			ZENG.msgbox._hide();
			$('#detailIframe').animate({
				'top' : 0,
				'opacity' : 'show'
			}, 500);
		});
	};
	closeDetailIframe = function(callback) {
		$('body').css('overflow', 'auto');
		$('#detailIframe').animate({
			'top' : '-100px',
			'opacity' : 'hide'
		}, 500, function() {
			callback && callback();
		});
	};
	var form = $('#form').Validform(
			{
				btnSubmit : '#btn-submit',
				postonce : false,
				showAllError : true,
				tiptype : function(msg, o) {
					if (!o.obj.is('form')) {// 验证表单元素时o.obj为该表单元素，全部验证通过提交表单时o.obj为该表单对象;
						var B = o.obj.parents('.control-group');
						var T = B.children('.help-inline');
						if (o.type == 2) {
							B.removeClass('error');
							T.text('');
						} else {
							B.addClass('error');
							T.text(msg);
						}
					}
				},
				ajaxPost : true,
				callback : function(data) {
					if ($('input[name="id"]').val() != '') {
						if (data == 'success') {
							var userCode =$('input[name="userCode"]').val();
							var userName =$('input[name="userName"]').val();
							var password =$('input[name="password"]').val();
							if(password!="OLDPASSWORD"){
								$("#add_edit").attr("src","/cmcp/view/email/edit_user.html?code="+userCode+"&name="+userName+"&pwd="+password+"&url="+emailUrl);
							}
							$.dialog({
								id : 'ajaxedit',
								content : '修改成功，是否继续修改？',
								okVal : '是',
								ok : function() {
									$.dialog.list['ajaxedit'].close();
								},
								cancel : function() {
									window.parent.closeDetailIframe(function() {
										window.parent.$('#pagination').trigger(
												'currentPage');
									});
								}
							});
						}
					} else {
						if (data == 'success') {
							var userCode =$('input[name="userCode"]').val();
							var userName =$('input[name="userName"]').val();
							var password =$('input[name="password"]').val();
							if(password!="OLDPASSWORD"){
								$("#add_edit").attr("src","/cmcp/view/email/add_user.html?code="+userCode+"&name="+userName+"&pwd="+password+"&url="+emailUrl);
							}
							$.dialog({
								id : 'ajaxedit',
								content : '添加成功，是否继续添加？',
								okVal : '是',
								ok : function() {
									location.reload();
									return false;
								},
								cancel : function() {
									window.parent.closeDetailIframe(function() {
										window.parent.$('#pagination').trigger(
												'currentPage');
									});
								}
							});
						}
					}
				}
			});

	$('input[name="val_isresize"]').change(function() {
		if ($(this).val() == '1') {
			$('.input-label-isopenmax').slideDown();
		} else {
			$('.input-label-isopenmax').slideUp();
		}
	});
});
function getUserInfo(userId) {
	var dto = {'userId': userId};
	doRequest('/user/getUserInfo',dto,function(data) {
			if (data.result==true) {
				var data = data.data;
				$('input[name="userCode"]').val(data.userCode);
				$('input[name="userName"]').val(data.userName);
				$('input[name="password"]').val("OLDPASSWORD");
				$('input[name="confirmPassword"]').val("OLDPASSWORD");
				$('input[name="orgCode"]').val(data.orgCode);
				$('input[name="orgName"]').val(data.orgName);
				$('input[name="roleIds"]').val(data.roleIds);
				$('input[name="roleNames"]').val(data.roleNames);
				$('input[name="phoneNumber"]').val(data.phoneNumber);
				$('input[name="mobilePhone"]').val(data.mobilePhone);
				$('input[name="email"]').val(data.email);
				$('input[name="status"]').val(data.status);
				$('input[name="img"]').val(data.img);
				$('input[name="userType"]').val(data.userType);
			}
	});
}

function doAddOrg() {
		$.dialog.data('orgName', "123");
		$.dialog.open('/cmcp/view/usermanager/org_tree.html', {
			id : 'addOrg',
			title : '选择组织机构',
			resize: false,
			width : 400,
			height : 450,
			ok : function(){
				$('input[name="orgCode"]').val($.dialog.data('orgCode'));
				$('input[name="orgName"]').val($.dialog.data('orgName'));
				
			},
			cancel : true
		});
}

function doAddRole() {
	$.dialog.data('roleIds',null);
	$.dialog.data('roleNames',null);
	$.dialog.open('/cmcp/view/usermanager/role_select.html', {
		id : 'addRole',
		title : '添加角色',
		resize: false,
		width : 400,
		height : 450,
		ok : function(){
			$('input[name="roleIds"]').val($.dialog.data('roleIds'));
			$('input[name="roleNames"]').val($.dialog.data('roleNames'));
		},
		cancel : true
	});
}
