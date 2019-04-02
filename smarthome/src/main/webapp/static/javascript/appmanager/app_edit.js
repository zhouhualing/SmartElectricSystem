$(function() {
	var tbid = getURLParam("appid");
	if (tbid != null) {
		getAppInfo(tbid);
	} else {
		$('input[name="val_type"][value="window"]').attr("checked", true);
		$('input[name="val_isresize"][value="1"]').attr("checked", true);
		$('input[name="val_isopenmax"][value="0"]').attr("checked", true);
		$('input[name="val_isflash"][value="0"]').attr("checked", true);
		$('input[name="val_client"][value="3"]').attr("checked", true);
	}
});

$(function() {
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
	$("[datatype]").focusin(
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
});

$(function() {
	var form = $('#form').Validform(
			{
				btnSubmit : '#btn-submit',
				postonce : false,
				showAllError : true,
				// msg：提示信息;
				// o:{obj:*,type:*,curform:*},
				// obj指向的是当前验证的表单元素（或表单对象），type指示提示的状态，值为1、2、3、4，
				// 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态, curform为当前form对象;
				// cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和
				// 当前提示的状态（既形参o中的type）;
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
	$('input[name="val_client"]').change(function() {
		if ($(this).val() == '1') {
			$('.input-mobileurl').slideUp();
			$('.input-pcurl,.input-label-window,.input-window-type,.input-label-isresize, .input-label-isopenmax, .input-label-isflash,.btn-preview').slideDown();
			if($('input[name="val_mobileurl"]').val()==""){
				$('input[name="val_mobileurl"]').val("无");
			}
		} else if ($(this).val() == '2') {
			$('.input-mobileurl').slideDown();
			$('.input-pcurl,.input-label-window,.input-window-type,.input-label-isresize, .input-label-isopenmax, .input-label-isflash,.btn-preview').slideUp();
			if($('input[name="val_url"]').val()==""){
				$('input[name="val_url"]').val("无");
			}
			if($('input[name="val_width"]').val()==""){
				$('input[name="val_width"]').val("800");
			}
			if($('input[name="val_height"]').val()==""){
				$('input[name="val_height"]').val("600");
			}
		} else if ($(this).val() == '3') {
			$('.input-pcurl,.input-label-window,.input-window-type,.input-label-isresize, .input-label-isopenmax, .input-label-isflash,.btn-preview').slideDown();
			$('.input-mobileurl').slideDown();
		}
	});
	$('input[name="val_type"]')
			.change(
					function() {
						if ($(this).val() == 'window') {
							$(
									'.input-label-isresize, .input-label-isopenmax, .input-label-isflash')
									.slideDown();
						} else {
							$('input[name="val_isresize"]').each(function() {
								if ($(this).val() == '1') {
									$(this).prop('checked', true);
								}
							});
							$('input[name="val_isopenmax"]').each(function() {
								if ($(this).val() == '0') {
									$(this).prop('checked', true);
								}
							});
							$('input[name="val_isflash"]').each(function() {
								if ($(this).val() == '0') {
									$(this).prop('checked', true);
								}
							});
							$(
									'.input-label-isresize, .input-label-isopenmax, .input-label-isflash')
									.slideUp();
						}
					});

	$('input[name="val_isresize"]').change(function() {
		if ($(this).val() == '1') {
			$('.input-label-isopenmax').slideDown();
		} else {
			$('.input-label-isopenmax').slideUp();
		}
	});

	// 选择应用图片
	$('.shortcut-selicon a').click(function() {
		$('.shortcut-addicon img').remove();
		$('.shortcut-addicon').addClass('bgnone').append($(this).html());
		$('#val_icon').val($(this).children('img').attr('valsrc')).focusout();
	});
	$('#uploadfilebtn')
			.on(
					'change',
					function(e) {
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
							$
									.dialog({
										id : 'uploadImg',
										title : '正在上传',
										content : '<div id="imgProgress" class="progress progress-striped active" style="width:200px;margin-bottom:0"><div class="bar"></div></div>',
										cancel : false
									});
							xhr.upload.addEventListener('progress',
									function(e) {
										if (e.lengthComputable) {
											var loaded = Math.ceil(e.loaded
													/ e.total * 100);
											$('#imgProgress .bar').css({
												width : loaded + '%'
											});
										}
									}, false);
							xhr.addEventListener('load', function(e) {
								$('#uploadfilebtn').val('');
								$.dialog.list['uploadImg'].close();
								if (xhr.readyState == 4 && xhr.status == 200) {
									var result = jQuery
											.parseJSON(e.target.responseText);
									if (result != null) {
										$('.shortcut-addicon img').remove();
										$('.shortcut-addicon').addClass(
												'bgnone').append(
												'<img src="'
														+ result + '"/>');
										$('#val_icon').val(result).focusout();
									}
								}
							}, false);
							xhr.open('post', clickmedBaseUrl + '/app/upfile',
									true);
							xhr.setRequestHeader('X-Requested-With',
									'XMLHttpRequest');
							xhr.send(fd);
						}
					});
	$('#btn-preview')
			.on(
					'click',
					function() {
						if (form.check()) {
							if ($('input[name="val_type"]:checked').val() == 'window') {
								window.top.HROS.window
										.createTemp({
											title : $('input[name="val_name"]')
													.val(),
											url : $('input[name="val_url"]')
													.val(),
											width : $('input[name="val_width"]')
													.val(),
											height : $(
													'input[name="val_height"]')
													.val(),
											isresize : $(
													'input[name="val_isresize"]:checked')
													.val() == 1 ? true : false,
											isopenmax : $(
													'input[name="val_isopenmax"]:checked')
													.val() == 1 ? true : false,
											isflash : $(
													'input[name="val_isflash"]:checked')
													.val() == 1 ? true : false
										});
							} else {
								window.top.HROS.widget.createTemp({
									url : $('input[name="val_url"]').val(),
									width : $('input[name="val_width"]').val(),
									height : $('input[name="val_height"]')
											.val(),
								});
							}
						} else {
							$.dialog({
								icon : 'error',
								content : '应用无法预览，请讲内容填写完整后再尝试预览'
							});
						}
					});
});
function getAppInfo(appid) {
	$
			.ajax({
				type : 'POST',
				url : clickmedBaseUrl + '/app/getAppInfo',
				data : 'appId=' + appid,
				success : function(data) {
					if (data != null) {
						$('input[name="id"]').val(data.appId);
						$('.shortcut-addicon').append('<img src="'+ data.appImgUrl + '"/>');
						$('#val_icon').val(data.appImgUrl);
						$('input[name="val_name"]').val(data.appName);
						$('input[name="val_width"]').val(data.width);
						$('input[name="val_height"]').val(data.height);
						$('input[name="val_mobileurl"]').val(data.appURL);
						$('input[name="val_url"]').val(data.appPCURL);
						$('#val_remark').text(data.description);
						if (data.appTypeCode == 'window') {
							$('input[name="val_type"][value="window"]').attr(
									"checked", true);
						} else if (data.appTypeCode == 'widget') {
							$('input[name="val_type"][value="widget"]').attr(
									"checked", true);

							$('.input-label-isresize, .input-label-isopenmax, .input-label-isflash')
							.slideUp();
						}
						if (data.isresize == '1') {
							$('input[name="val_isresize"][value="1"]').attr(
									"checked", true);
						} else if (data.isresize == '0') {
							$('input[name="val_isresize"][value="0"]').attr(
									"checked", true);
						}
						if (data.isopenmax == '1') {
							$('input[name="val_isopenmax"][value="1"]').attr(
									"checked", true);
						} else if (data.isopenmax == '0') {
							$('input[name="val_isopenmax"][value="0"]').attr(
									"checked", true);
						}
						if (data.isflash == '1') {
							$('input[name="val_isflash"][value="1"]').attr(
									"checked", true);
						} else if (data.isflash == '0') {
							$('input[name="val_isflash"][value="0"]').attr(
									"checked", true);
						}
						if (data.source == '1') {
							$('input[name="val_client"][value="1"]').attr(
									"checked", true);
							$('.input-mobileurl').slideUp();
						} else if (data.source == '2') {
							$('input[name="val_client"][value="2"]').attr(
									"checked", true);
							$('.input-pcurl,.input-label-window,.input-window-type,.input-label-isresize, .input-label-isopenmax, .input-label-isflash,.btn-preview').slideUp();
						} else if (data.source == '3') {
							$('input[name="val_client"][value="3"]').attr(
									"checked", true);
						}
					}
				}
			});
}
