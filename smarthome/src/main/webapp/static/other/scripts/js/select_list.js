// JavaScript Document
var EX = {
  addEvent:function(k,v){
    var me = this;
    if (me.addEventListener)
      me.addEventListener(k, v, false);
    else if(me.attachEvent)
      me.attachEvent("on" + k, v);
    else
      me["on" + k] = v;
  },
  removeEvent:function(k,v){
    var me = this;
    if (me.removeEventListener)
      me.removeEventListener(k, v, false);
    else if (me.detachEvent)
      me.detachEvent("on" + k, v);
    else
      me["on" + k] = null;
  },
  stop:function(evt){
    evt = evt || window.event;
    evt.stopPropagation?evt.stopPropagation():evt.cancelBubble=true;
  }
};
document.getElementById('select_list').onclick = EX.stop;
var url = '#'; 
function show(){ 
var o = document.getElementById('select_list'); 
o.style.display = ""; 
setTimeout(function(){EX.addEvent.call(document,'click',hide);});
} 
function hide(){ 
var o = document.getElementById('select_list'); 
o.style.display = "none"; 
EX.removeEvent.call(document,'click',hide);
} 