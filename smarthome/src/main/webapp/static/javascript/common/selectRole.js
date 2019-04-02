/**
 * Created by Administrator on 2015/3/18.
 */
var getAllRole = null;
var roleIds = null;
function selectRole() {
    if (!getAllRole) {
        doJsonRequest("/user/getAllRole", null, function (dat) {
            if (dat.result) {
                getAllRole = dat["data"];
            }
            makeSelect();
        });
    }
    else {
        makeSelect();
    }
}
function makeSelect() {

    var unselect = getAllRole;
    var easyDialog_wrapper = $("<div></div>").addClass("easyDialog_wrapper").attr("id", "easyDialog_wrapper");
    var easyDialog_content = $("<div></div>").addClass("easyDialog_content");
    var h4 = $("<h4></h4>").addClass("easyDialog_title").html('<a href="javascript:close()"  title="关闭窗口" class="close_btn" id="closeBtn">×</a>选择角色');
    easyDialog_content.append(h4);
    easyDialog_wrapper.append(easyDialog_content);
    var easyDialog_text = $("<div></div>").addClass("easyDialog_text");
    easyDialog_content.append(easyDialog_text);
    var shadow = $("<div></div>").addClass("shadow");
    $("body").append(easyDialog_wrapper).append(shadow);
    var ul = $("<ul></ul>").css({
        width: "80%",
        margin: "1% 10%"
    })
    easyDialog_text.append(ul).addClass("unselect");
    for (var k in unselect) {
        var li = $("<li></li>").attr("id", k).css("cursor", "pointer").css("float", "left")
            .css("width", "100%").css("position", "relative").attr("name", unselect[k])
            .html("<p>" + unselect[k] + "</p>");
        ul.append(li);
        li.append($("<p>").css({
            position: "absolute",
            //   color: "#4b9",
            "top": "0px",
            right: "20px"
        }).addClass("added"));
    }

    $(".unselect li").on("click", function () {
        if ($(this).hasClass("sel")) {
            $(this).removeClass("sel");
            $(this).find(".added").html("");
        }
        else {
            $(this).addClass("sel");
            $(this).find(".added").html("√");
        }

    });
    var p = $("<p></p>").css({
        "text-align": "center",
        width: "100%"
    });
    easyDialog_content.append(p);
    var ensure = $("<button></button>").addClass("ensure").html("确  定");
    var cancel = $("<button></button>").addClass("cancel").html("取 消");

    p.append(ensure).append(cancel);
    //关闭弹出框
    cancel.on("click", function () {
        close();
    });
    //关闭弹出框
    $(".close_btn").on("click", function () {
        close();
    });


    ensure.on("click", function () {
        roleIds = null;
        $("#roles").val("");
        $(".unselect li").each(function () {
            if ($(this).hasClass("sel")) {
                if (!roleIds)
                    roleIds = $(this).attr("id");
                else {
                    roleIds += "," + $(this).attr("id");
                }
            }
        });
        var tempRoles = roleIds.split(",");
        //console.log(roleIds, tempRoles, getAllRole);

        for (var name in tempRoles) {
            name = tempRoles[name];
            if ($("#roles").val().length)
                $("#roles").val($("#roles").val() + "," + getAllRole[name]);

            else {
                $("#roles").val(getAllRole[name]);
            }
        }
        $("#roles").attr("title",$("#roles").val());
        $(".input_view[name='roleIdsS']").html( $("#roles").val());
        close();

    });
    //关闭弹出框
    function close() {
        easyDialog_wrapper.remove();
        shadow.remove();
    }
}