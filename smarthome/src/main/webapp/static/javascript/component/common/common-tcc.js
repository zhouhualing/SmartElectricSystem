/**
 * Created by Administrator on 2015/1/8.
 * 弹出层样式
 */

(function () {
    var TanChuK = function (config) {
        this._config = config;
        this._height = this.getConfig("height") || $(window).height();
        this._width = this.getConfig("width") || $(window).width();
        this._title = this.getConfig("title") || "";

    };
    if (typeof (harmazing) === "undefined") {
        harmazing = {};
    }
    harmazing.TanChuK = TanChuK;
    TanChuK.prototype.init = function () {
        // 生成弹出框

        var me = this;
        console.log(me);
        $(".easyDialog_wrapper").remove();

        var easyDialog_wrapper = $("<div></div>").addClass("userBasicInfo").css({
            width: me._width,
            height: me._height,

            "overflow": "hidden"



        });

        var easyDialog_content = $("<div></div>").addClass("easyDialog_content");
        var div = $("<div></div>").addClass("widget-header").css({
            cursor: "move"
        });
        div.append($("<h3></h3>").css({
           
            "font-size": "16px"
        }).html("<strong>" + me._title + "</strong>"))
        //  var h4 = $("<h4></h4>").addClass("easyDialog_title").html('<a href="javascript:close()"  title="关闭窗口" class="close_btn" id="closeBtn">×</a>' + me._title);
        easyDialog_content.append(div);
        easyDialog_wrapper.append(easyDialog_content);
        var easyDialog_text = $("<div></div>").addClass("easyDialog_text").css({
            height: me._height - 110 + "px",

            "overflow": "hidden"
        }).attr("id", "easyDialog_text");
        easyDialog_content.append(easyDialog_text);
        var bottom = $("<div></div>").addClass("form-actions").css({
            "bottom": "0px",
            width: "100%",
            align:"right",
            "position": "absolute"
        })
        var input = $("<input>").addClass("btn btn-primary").attr("type", "button").attr("value", "返 回").css({
            position:"relative",
            right:"30px"
        })

        easyDialog_content.append(bottom);
        bottom.append(input)
        var shadow = $("<div></div>").addClass("shadow");
        $("body").append(easyDialog_wrapper)
        //关闭弹出框
        $(".close_btn").on("click", function () {
            close();
        });
        input.on("click", function () {
            close();
        });
        //关闭弹出框
        function close() {
            easyDialog_wrapper.remove();
            shadow.remove();
            history.back()
        }

        return easyDialog_text
    };

    TanChuK.prototype.getConfig = function (key) {
        if (this._config == null) {
            return null;
        } else {
            if (typeof (this._config[key]) != "undefined") {
                return this._config[key];
            } else {
                return null;
            }
        }
    }
})();