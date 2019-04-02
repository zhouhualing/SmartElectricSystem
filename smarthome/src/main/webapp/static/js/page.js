(function($) {
	/**
	 * @调用分页函数
	 * @author dlx
	 */
	$.fn.dlx = function(options) {
		var opts = $.extend({}, $.fn.dlx.defaults, options);
		return this.each(function() {
					var $this = $(this);
					var o = $.meta ? $.extend({}, opts, $this.data()) : opts;
					var selectedpage = o.start;
					$.fn.draw(o, $this, selectedpage);
				});
	};
	/**
	 * @初始化分页参数
	 * @param count
	 *            数据的总数
	 * @param data
	 *            每页显示几条数据
	 * @param start
	 *            当前展示页
	 * @param display
	 *            显示多少页面按钮
	 * @param onChange
	 *            点击页面按钮触发事件
	 * @author dlx
	 */
	$.fn.dlx.defaults = {
		count : 1,
		data : 10,
		start : 1,
		display : 5,
		onChange : function() {
			return false;
		}
	};
	/**
	 * @开始勾画分页
	 * @author dlx
	 */
	$.fn.draw = function(o, obj, selectedpage) {
		var $this = obj;
		var objid = obj.attr("id");
		var idflag = obj.attr("class")+"_";
		if(objid){
			idflag = objid+"_";
		}
		
		var _obj;
		var s;
		var e;
		if (selectedpage <= 0)
			selectedpage = 1;
		if (o.display <= 0)
			o.display = 5;
		if (o.count <= 0)
			o.count = 1;
		var a = Math.ceil(o.count / o.data);
		if (a <= o.display)
			o.display = a;
		if (selectedpage > a)
			selectedpage = a;
		$this.empty();
		/* 左侧开始 */
		var _first = $(document.createElement('a')).attr('href',
				'javascript:void(0)'
		).html("首页");
		var _rotleft = $(document.createElement('span')).addClass('leftPage')
				.html('<a href="javascript:void(0)">上一页</a>');
		var _spanwrapleft = $(document.createElement('span'))
				.addClass('jPag-control-back');
		_spanwrapleft.append(_first).append(_rotleft);
		/* 右侧开始 */
		var _rotright = $(document.createElement('span')).addClass('rightPage')
				.html('<a href="javascript:void(0)">下一页</a>');
		var _last = $(document.createElement('a')).attr('href',
				'javascript:void(0)'
		).html("末页");
		var _spanwrapright = $(document.createElement('span'))
				.addClass('jPag-control-front');
		_spanwrapright.append(_rotright).append(_last);
		// append all:
		$this.append(_spanwrapleft).append(_spanwrapright);
		$('#'+idflag+'gopage').click(function() {
					var ns = parseInt($("input[name='"+idflag+"pageinput']").val());
					if (ns > a)
						ns = a;
					if ("ChenJinfan" == o.onChange(ns))
						return;// 陈锦帆修改，当onChange方法返回"ChenJinfan"时，不进行勾画新的分页
					$.fn.draw(o, obj, ns);
				});
		$('.jPag-control-back a:eq(0)').click(function() {
					if ("ChenJinfan" == o.onChange(1))
						return;// 陈锦帆修改，当onChange方法返回"ChenJinfan"时，不进行勾画新的分页
					$.fn.draw(o, obj, 1);
				});
		$('.jPag-control-back a:eq(1)').click(function() {
					var sp = (selectedpage - 1) > 0 ? (selectedpage - 1) : 1;
					if ("ChenJinfan" == o.onChange(sp))
						return;// 陈锦帆修改，当onChange方法返回"ChenJinfan"时，不进行勾画新的分页
					$.fn.draw(o, obj, sp);
				});
		$('.jPag-control-front a:eq(0)').click(function() {
					var ep = (selectedpage + 1) <= a ? (selectedpage + 1) : a;
					if ("ChenJinfan" == o.onChange(ep))
						return;// 陈锦帆修改，当onChange方法返回"ChenJinfan"时，不进行勾画新的分页
					$.fn.draw(o, obj, ep);
				});
		$('.jPag-control-front a:eq(1)').click(function() {
					if (o.onChange(a) == "ChenJinfan")
						return;// 陈锦帆修改，当onChange方法返回"ChenJinfan"时，不进行勾画新的分页
					$.fn.draw(o, obj, a);
				});
	};
})(jQuery);