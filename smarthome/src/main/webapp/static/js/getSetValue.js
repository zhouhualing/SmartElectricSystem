/**
 * Created by Administrator on 2015/1/6.
 * 根据id设置或者获取元素数据
 */

var domGetValue = function (id) {
    var dom = $("#" + id);
    var tagName = $("#" + id)[0].tagName.toUpperCase();
    var returnName = "";
    if (tagName == "INPUT" && (dom.attr("type") == "checkbox" || dom.attr("type") == "RADIO")) {
        if (dom.attr("checked")) {
            return dom.val();
        }else{
            return "";
        }
    }
    else if (tagName == "INPUT" || tagName == "SELECT" || tagName == "BUTTON" || tagName == "TEXTAREA" || tagName == "OPTION") {
        return dom.val();
    }
    else {
        return dom.html();
    }
};
var domSetValue = function (id, value) {
    var dom = $("#" + id);
    var tagName = $("#" + id)[0].tagName.toUpperCase();
    if (tagName == "INPUT" && (dom.attr("type") == "checkbox" || dom.attr("type") == "RADIO")) {
        dom.attr("checked", "checked");
      //  $("lable").html(value);
        
        
        dom.val(value);
        return;
    }
    if (tagName == "INPUT" || tagName == "SELECT" || tagName == "BUTTON" || tagName == "TEXTAREA" || tagName == "OPTION") {
        dom.val(value);
        return;
    } else {
        dom.html(value)
    }
}