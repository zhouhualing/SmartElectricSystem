$(function() {
	// 配置artDialog全局默认参数
	(function(config) {
		config['lock'] = true;
		config['fixed'] = true;
		config['resize'] = false;
		config['background'] = '#000';
		config['opacity'] = 0.5;
	})($.dialog.defaults);
	$.extend($.Datatype,{
		"sjh" : /^([0-9]{11},){0,3}$/
	});
	$.Tipmsg.w["sjh"]="手机号码必须为11位数字，末尾需加英文逗号,号码总数不多于3个。";
	
	//获取用户信息
	getUserInfo();
	// 表单提示
	$("[datatype]").change(
			function() {
				$(this).parent().addClass('info').children('.infomsg').show()
						.siblings('.help-inline').hide();
			}).focusout(
			function() {
				$(this).parent().removeClass('info').children('.infomsg')
						.hide().siblings('.help-inline').show();
			});
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
						if (data == 'success') {
								window.parent.ZENG.msgbox.show('操作成功', 4, 2000);
						}else{
								window.parent.ZENG.msgbox.show('操作失败', 5, 2000);
							
						}
				}
			});
	// 用户头像
	$('#uploadfilebtn').on('change',function(e) {
						var files = e.target.files || e.dataTransfer.files;
						if (files.length == 0) {
							return;
						}
						// 检测文件是不是图片
						if (files[0].type.indexOf('image') === -1) {
							alert('请上传图片');
							return false;
						}
						// 检测文件大小是否超过1M
						if (files[0].size > 1024 * 1024) {
							alert('图片大小超过1M');
							return;
						}
						var fd = new FormData();
						fd.append('xfile', files[0]);
						var xhr = new XMLHttpRequest();
						if (xhr.upload) {
							$.dialog({
										id : 'uploadImg',
										title : '正在上传',
										content : '<div id="imgProgress" class="progress progress-striped active" style="width:200px;margin-bottom:0"><div class="bar"></div></div>',
										cancel : false
									});
							xhr.upload.addEventListener('progress',
									function(e) {
										if (e.lengthComputable) {
											var loaded = Math.ceil(e.loaded/e.total * 100);
											$('#imgProgress .bar').css({width : loaded + '%'});
										}
									}, false);
							xhr.addEventListener('load', function(e) {
								$('#uploadfilebtn').val('');
								$.dialog.list['uploadImg'].close();
								if (xhr.readyState == 4 && xhr.status == 200) {
									var result = jQuery.parseJSON(e.target.responseText);
									if (result != null) {
										$('.shortcut-addicon img').remove();
										$('.shortcut-addicon').addClass(
												'bgnone').append(
												'<img src="'+ result + '"/>');
										$('#img').val(result).focusout();
									}
								}
							}, false);
							xhr.open('post', clickmedBaseUrl + '/app/upfile',true);
							xhr.setRequestHeader('X-Requested-With','XMLHttpRequest');
							xhr.send(fd);
						}
					});
});
function getUserInfo() {
	getCurrenUserInfo(
		function(data) {
			if (data != null) {
				$('.shortcut-addicon img').remove();
				$('.shortcut-addicon').addClass('bgnone').append('<img src="'+ data.img + '"/>');
				$('#img').val(data.img).focusout();
				$('input[name="id"]').val(data.userId);
				$('input[name="userCode"]').val(data.userCode);
				$('input[name="userName"]').val(data.userName);
				$('input[name="orgName"]').val(data.orgName);
				$('input[name="roleNames"]').val(data.roleNames);
				$('input[name="phoneNumber"]').val(data.phoneNumber);
				if(data.mobilePhone!=null){
					$('input[name="mobilePhone"]').val(data.mobilePhone+",");
				}
				$('input[name="email_s"]').val(data.email);
			}
		});
}
