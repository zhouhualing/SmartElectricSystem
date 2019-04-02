var wsUrl = "ws"+baseUrl.substring(baseUrl.indexOf(":"));
var thes_ua = navigator.userAgent.toLowerCase();
var thes_explor = thes_ua.match(/msie ([\d.]+)/);
var CommonWebSocket = function(conf){
	this.url = null;
	this.ws = null;
	this.transports = [];
	this.afterOpenFun = null;
	this.afterGetMessageFun = null;
	this.afterCloseFun = null;
	$.extend(this,conf);
	var _this = this;
	this.connect = function(){
        _this.ws = ((thes_explor!=null)&&(parseInt(thes_explor[1]) < 10))? 
                new SockJS("/cmcp/sockjs"+_this.url, undefined, {protocols_whitelist: _this.transports}) : new WebSocket(wsUrl+_this.url);
                
        _this.ws.onopen = function (event) {
        	if(_this.afterOpenFun != null) {
        		_this.afterOpenFun(event);
        	}
         };
         _this.ws.onmessage = function (event) {
         	if(_this.afterGetMessageFun != null) {
        		_this.afterGetMessageFun(event.data);
        	}
         };
         _this.ws.onclose = function (event) {
          	if(_this.afterCloseFun != null) {
        		_this.afterCloseFun(event);
        	}
         };		
	}
	
	this.disconnect = function() {
        if (_this.ws != null) {
        	_this.ws.close();
        	_this.ws = null;
        }
    }
	
	this.connect();
}


