/**
 * @description: 数据控制条 v0.1 此版本专门定制后期修改为通用的
 *  //TODO 需要后期改为通用的
 *  harmazing.chart.ControlBar
 * @author: Li JianHui (sillynemo)
 * @create: 14-9-19 下午1:51
 */

(function () {
    /**
     * 控件类
     * @param {Object}config 控件配置项
     * @constructor
     */
    var ControlBar = function (config) {

        this.getConfig(config);
//        this.drawAxis();
        this.init();
        this.addListener();
    };

    /**
     * 获取控件配置项
     */
    ControlBar.prototype.getConfig = function (config) {
        var me = this;
        me._renderTo = config.renderTo;
        me._callback = config.callbackFunction;
        me._endCallback = config.endCallbackFunction;
        me._clearRefresh = config.clearRefresh;
        me._defaultValue = config._defaultValue || 0;
        var h = me._height || 222;
        var w = me._width || 36
      //  me._bgImageUrl = config.bgImageUrl || "bg.svg";
        me._hasBg = config.bgImageUrl || false;
     //  me._buttonImageUrl = config.buttonImageUrl || "pointer.svg";
        me._hasButton = config.buttonImageUrl || false;
        me._height = h;
        me._width = w;
        me._min = config.min || 16;
        me._max = config.max || 32;
    };

    /**
     * 初始化控件
     */
    ControlBar.prototype.init = function () {
        var me = this, renderTo = me._renderTo, defaultValue = me._defaultValue;
        var bgImageUrl = me._bgImageUrl, buttonImageUrl = me._buttonImageUrl;
        var height = me._height, width = me._width;
        var bgDiv = $("<div></div>"); // 背景DIV
        var con = $("<div></div>")
            .css("position", "relative")
            .css("width", width + "px")
            .css("height", height + "px");
        $(renderTo).append(con);
        con.append(bgDiv);
        bgDiv.css("position", "absolute")
            .css("top", (me._hasBg ? 0 : width / 2) + "px")
            .css("height", (height - (me._hasBg ? 0 : width)) + "px")
            .css("width", (width / 2 + (me._hasBg ? 2 : 0)) + "px")
            .css("left", (me._hasBg ? (width / 5) : 0) + "px")
            .css("background-image", "url(" + bgImageUrl + ")")
            .css("background-size", "cover");
        me._bgDiv = bgDiv;

        var buttonDiv = $("<div></div>"); // 调节按钮DIV
        buttonDiv.addClass("harmazing_chart_control_bar_button");
        con.append(buttonDiv);
        buttonDiv.css("position", "absolute")
            .css("top", defaultValue * (height - width) + "px")
            .css("height", width + "px")
            .css("width", width + "px")
            .css("left", 0 + "px")
            .css("background-image", "url(" + buttonImageUrl + ")")
            .css("background-size", "cover")
            .css("cursor", "pointer");
        me._buttonDiv = buttonDiv;
        me.callback(defaultValue * (height - width));
    };

    /**
     * 改变默认温度
     */
    ControlBar.prototype.changeDefaultValue = function (value) {
        var me = this, defaultValue = value;
        var height = me._height, width = me._width;
        me._buttonDiv.css("position", "absolute")
            .css("top", defaultValue * (height - width) + "px");
        me.callback(defaultValue * (((height - width))));
    };

    /**
     * 添加监听器
     */
    ControlBar.prototype.addListener = function () {
        var me = this, buttonDiv = me._buttonDiv, startY, height = me._height, width = me._width;
        var mc = new Hammer(buttonDiv[0]);
        mc.get('pan').set({ direction: Hammer.DIRECTION_ALL });

        mc.on("panstart", onPanStart);
        mc.on("panend", onPanEnd);
        mc.on("panmove", onPan);

        function onPanStart() {
            startY = +buttonDiv.css("top").replace("px", "")
            buttonDiv.addClass("drag");
            me._clearRefresh();
        }

        function onPan(ev) {
            var y = startY + ev.deltaY;
            y = Math.max(0, y);
            y = Math.min(y, height - width);
            buttonDiv.css("top", y + "px");
            me.callback(y);
        }

        function onPanEnd(ev) {
            var y = startY + ev.deltaY;
            y = Math.max(0, y);
            y = Math.min(y, height - width);
            buttonDiv.removeClass("drag");
            me.endCallback(y);
        }
    };

    /**
     * 绘制坐标轴
     */
    ControlBar.prototype.drawAxis = function () {
        var me = this, renderTo = me._renderTo, width = me._width, height = me._height;
        var x = d3.scale.linear()
            .range([0, height - width])
            .domain([16, 32]);
        var axis = d3.svg.axis()
            .scale(x)
            .orient("left")
            .tickValues([16, 20, 24, 28, 32]);
        d3.select(renderTo)
            .append("div")
            .style("width", "100%")
            .style("height", "100%")
//            .style("z-index",9)
            .style("position", "absolute")
            .append("svg")
            .attr("class", "axis")
            .attr("width", width)
            .attr("height", height)
//            .style("margin-top","18px")
//            .attr("transform", "translate(0,18)")
            .append("g")
            .attr("transform", "translate(30," + width / 2 + ")")
            .call(axis);
        d3.select("path").style("fill", "none");//.style("stroke","black");
        d3.selectAll("line").style("fill", "none");//.style("stroke","black");
    };

    /**
     * 回调函数
     */
    ControlBar.prototype.callback = function (yValue) {
        var me = this, callback = me._callback, height = me._height, width = me._width;
        if (typeof callback === "function") {
            callback((yValue) / ((height - width)));
        }
    };
    ControlBar.prototype.endCallback = function (yValue) {
        var me = this, callback = me._endCallback, height = me._height, width = me._width;
        if (typeof callback === "function") {
            callback((yValue) / ((height - width)));
        }
    };

    // 初始化命名空间
    if (!window.Harmazing) {
        window.Harmazing = {};
    }
    if (!window.Harmazing.chart) {
        window.Harmazing.chart = {};
    }
    // 把该工具放到命名空间下
    window.Harmazing.chart.ControlBar = ControlBar;
})();