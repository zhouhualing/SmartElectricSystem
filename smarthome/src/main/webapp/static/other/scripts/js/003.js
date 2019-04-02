// JavaScript Document

function toggle(targetid, objN) {

    var target = document.getElementById(targetid);
    var aa = document.getElementById(objN);

    if (target.style.display == "block") {
        target.style.display = "none";
    } else {
        target.style.display = "block";
    }


}
function info_show() {
    var i, p, v, obj, args = info_show.arguments;
    for (i = 0; i < (args.length - 2); i += 3)
        with (document)
            if (getElementById && ((obj = getElementById(args[i])) != null)) {
                v = args[i + 2];
                if (obj.style) {
                    obj = obj.style;
                    v = (v == 'show') ? 'visible' : (v == 'hide') ? 'hidden' : v;
                }
                obj.visibility = v;
            }
}

