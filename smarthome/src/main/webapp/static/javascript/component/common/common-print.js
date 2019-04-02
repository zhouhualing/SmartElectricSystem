/**
 *  Example:
 *      Print Button: <div id="print_button">Print</div>
 *      Print Area  : <div class="PrintArea" id="MyId" class="MyClass"> ... html ... </div>
 *      Javascript  : <script>
 *                       $("div#print_button").click(function(){
 *                           $("div.PrintArea").printArea( [OPTIONS] );
 *                       });
 *                     </script>
 *  options are passed as json (example: {mode: "popup", popClose: false})
 *retainAttr : ["id","class","style"] 
 */
(function($) {
    var counter = 0;
    var modes = { iframe : "iframe", popup : "popup" };
    var standards = { strict : "strict", loose : "loose", html5 : "html5" };
    var defaults = { mode       : modes.popup,
                     standard   : standards.html5,
                     popHt      : 500,
                     popWd      : 400,
                     popX       : 200,
                     popY       : 200,
                     popTitle   : '',
                     popClose   : false,
                     extraCss   : '',
                     extraHead  : '',
                     retainAttr : ["id"] };

    var settings = {};//global settings

    $.fn.printArea = function( options )
    {
        $.extend( settings, defaults, options );

        counter++;
        var idPrefix = "printArea_";
        $( "[id^=" + idPrefix + "]" ).remove();

        settings.id = idPrefix + counter;

        var $printSource = $(this);

        var PrintAreaWindow = PrintArea.getPrintWindow();

        PrintArea.write( PrintAreaWindow.doc, $printSource );

        setTimeout( function () { PrintArea.print( PrintAreaWindow ); }, 1000 );
    };

    var PrintArea = {
        print : function( PAWindow ) {
            var paWindow = PAWindow.win;

            $(PAWindow.doc).ready(function(){
                paWindow.focus();
                paWindow.print();

                if ( settings.mode == modes.popup && settings.popClose )
                    setTimeout(function() { paWindow.close(); }, 2000);
            });
        },
        write : function ( PADocument, $ele ) {

            PADocument.open();
            PADocument.write( PrintArea.docType() + "<html>" + PrintArea.getHead() + PrintArea.getBody( $ele ) + "</html>" );
            PADocument.close();
        },
        docType : function() {
            if ( settings.mode == modes.iframe ) return "";

            if ( settings.standard == standards.html5 ) return "<!DOCTYPE html>";
            
            var transitional = settings.standard == standards.loose ? " Transitional" : "";
            var dtd = settings.standard == standards.loose ? "loose" : "strict";
            return '<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01' + transitional + '//EN" "http://www.w3.org/TR/html4/' + dtd +  '.dtd">';
        },
        getHead : function() {
            var extraHead = "";
            var links = "";

            if ( settings.extraHead ) settings.extraHead.replace( /([^,]+)/g, function(m){ extraHead += m });

            $(document).find("link")
                .filter(function(){ // Requirement: <link> element MUST have rel="stylesheet" to be considered in print document
                        var relAttr = $(this).attr("rel");
                        return ($.type(relAttr) === 'undefined') == false && relAttr.toLowerCase() == 'stylesheet';
                    })
                .filter(function(){ // Include if media is undefined, empty, print or all
                        var mediaAttr = $(this).attr("media");
                        return $.type(mediaAttr) === 'undefined' || mediaAttr == "" || mediaAttr.toLowerCase() == 'print' || mediaAttr.toLowerCase() == 'all'
                    })
                .each(function(){
                        links += '<link type="text/css" rel="stylesheet" href="' + $(this).attr("href") + '" />';
                    });
            $(document).find("style").each(function(){
            	var type="type='text/css'";
            	links += "<style "+type+">"+$(this)[0].innerHTML+"</style>";
            })
            if ( settings.extraCss ) settings.extraCss.replace( /([^,\s]+)/g, function(m){ links += '<link type="text/css" rel="stylesheet" href="' + m + '"/>' });
            return "<head>"
//            +
//            '<script type="text/javascript">'+
//            '	var hkey_root,hkey_path,hkey_key'+
//            '	hkey_root="HKEY_CURRENT_USER"'+
//            '	hkey_path="\\Software\\Microsoft\\Internet Explorer\\PageSetup\\"'+
//            '	//设置网页打印的页眉页脚为空'+
//            '	function pagesetup_null()'+
//            '	{'+
//            '	  try{'+
//            '	    var RegWsh = new ActiveXObject("WScript.Shell")'+
//            '	    hkey_key="header"    '+
//            '	    RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"")'+
//            '	    hkey_key="footer"'+
//            '	    RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"")'+
//            '	  }catch(e){}'+
//            '	}'+
//            '</script>'
            +"<title>" + settings.popTitle + "</title>" + extraHead + links + "<style media='print'>.Noprint{display: none;}.PageNext{page-break-after: always;}</style><style>pre{background-color:#bbbbbb!important;text-align:left!important}</style></head>";
        },
        getBody : function ( elements ) {
            var htm = "";
            var attrs = settings.retainAttr;
            elements.each(function(i) {
                var ele = PrintArea.getFormData( $(this) );
                var str = "";
                var attributes = ""
                for ( var x = 0; x < attrs.length; x++ )
                {
                    var eleAttr = $(ele).attr( attrs[x] );
                    if ( eleAttr ) attributes += (attributes.length > 0 ? " ":"") + attrs[x] + "='" + eleAttr + "'";
                }
                if(i !=0) {
                	str = "<div class='PageNext'></div>";
                }
                htm += str+'<div ' + attributes + '>' + $(ele).html() + '</div>';
            });
            return "<body ><OBJECT classid='CLSID:8856F961-340A-11D0-A96B-00C04FD705A2' height=0 id='wb' name='wb' width=0></OBJECT>" + htm + "</body>";
        },
        getFormData : function ( ele ) {
            var copy = ele.clone();
            var copiedInputs = $("input,select,textarea,button,p,label,span,a,u", copy);
            $("input,select,textarea,button,p,label,span,a,u", ele).each(function( i ){
                var typeInput = $(this).attr("type");
                if ($.type(typeInput) === 'undefined') typeInput = $(this).is("select") ? "select" : $(this).is("textarea") ? "textarea" : $(this).is("button") ? "button":$(this).is("p") ? "p":$(this).is("label") ? "label":$(this).is("span") ? "span":"";
                var copiedInput = copiedInputs.eq( i );
                if ( typeInput == "radio" || typeInput == "checkbox" ) {
                	if(typeInput == "radio" ) {
	                	if($(this).is(":checked")) {
	                		copiedInput.parent().html($(this).next("span").html());
	                	} 
	                	var name = $(this).attr("name")
	                	if($("[name='"+name+"']:checked").length ==0) {
	                		copiedInput.parent().html("");
	                	}
                	} else if(typeInput == "checkbox") {
	                	if($(this).is(":checked")) {
	                		copiedInput.parent().html($(this).next("span").html());
	                	} 
	                	var name = $(this).attr("name")
	                	if($("[name='"+name+"']:checked").length ==0) {
	                		copiedInput.parent().html("");
	                	}		
                	}
                	
                }
                else if ( typeInput == "text" ) { 
                	copiedInput.attr( "value", $(this).val() ).hide();
                	var str=$(this).val(); 
                	if(str==null||str=="") {
                		if($(this).attr("printNullLength")) {
                			var theLength = parseInt($(this).attr("printNullLength"));
                			for(var i=0; i<theLength;i++) {
                				str= str+"&nbsp;&nbsp;"
                			}
                		}
                		
                	}
                	copiedInput.parent().append(str);
                }
                else if ( typeInput == "select" ) {
                	if($(this).filter(":visible").length == 0) {
                	} else {
                        $(this).find( "option" ).each( function( i ) {
                            if ( $(this).is(":selected")) {
                            	if($(this).val() == "") {
                            		copiedInput.parent().html("");      
                            	} else {
                                	$("option", copiedInput).eq( i ).attr( "selected", true );
                                	copiedInput.parent().append($("option", copiedInput).eq( i ).text()==null?"":$("option", copiedInput).eq( i ).text());  
                                	copiedInput.remove();
                            	}

                            }
                        });                		
                	}
          	
                } else if ( typeInput == "textarea" ) { 
                	copiedInput.text( $(this).val() ).hide();
                	copiedInput.parent().append("<pre>"+$(this).val()+"</pre>");
                } else if(typeInput == "hidden"){
                	
                } else if(typeInput == "button"){
                	copiedInput.addClass("hidden");
                } else if(typeInput == "p" || typeInput=="label"||typeInput=="span"){
                	if($(this).filter(".noPrint").length >0) {
                		copiedInput.addClass("hidden");
                	}
                	
                }else {
                	
                	if($(this).is("a")) {
                		copiedInput.attr("href",null);
                    	if($(this).filter(".noPrint").length >0) {
                    		copiedInput.addClass("hidden");
                    	}
                	}else if($(this).filter(".print").length >0) {
                			
                	}else {
                    	if($(this).is("u")) {
                        	copiedInput.hide();
                        	copiedInput.parent().append($(this).html());                        		
                    	} else {
                        	copiedInput.attr( "value", $(this).val() ).hide();
                        	copiedInput.parent().append($(this).val());                		
                    	}                		
                	}

                }
            });
            return copy;
        },
        getPrintWindow : function () {
            switch ( settings.mode )
            {
                case modes.iframe :
                    var f = new PrintArea.Iframe();
                    return { win : f.contentWindow || f, doc : f.doc };
                case modes.popup :
                    var p = new PrintArea.Popup();
                    return { win : p, doc : p.doc };
            }
        },
        Iframe : function () {
            var frameId = settings.id;
            var iframeStyle = 'border:0;position:absolute;width:0px;height:0px;right:0px;top:0px;';
            var iframe;

            try
            {
                iframe = document.createElement('iframe');
                document.body.appendChild(iframe);
                $(iframe).attr({ style: iframeStyle, id: frameId, src: "#" + new Date().getTime() });
                iframe.doc = null;
                iframe.doc = iframe.contentDocument ? iframe.contentDocument : ( iframe.contentWindow ? iframe.contentWindow.document : iframe.document);
            }
            catch( e ) { throw e + ". iframes may not be supported in this browser."; }

            if ( iframe.doc == null ) throw "Cannot find document.";
            
            return iframe;
        },
        Popup : function () {
            var windowAttr = "location=yes,statusbar=no,directories=no,menubar=no,titlebar=no,toolbar=no,dependent=no";
            windowAttr += ",width=" + settings.popWd + ",height=" + settings.popHt;
            windowAttr += ",resizable=yes,screenX=" + settings.popX + ",screenY=" + settings.popY + ",personalbar=no,scrollbars=yes";

            var newWin = window.open( "", "_blank",  windowAttr );

            newWin.doc = newWin.document;

            return newWin;
        }
    };
})(jQuery);



function CommonDoPrint() {
    
     window.scrollTo(0,0);
	var obj = {
		    title:'选择打印页',
		    height:"110px",
		    width:"500px",
		    url:'../report/selectPrint.html',
		    fun:true,
		    showWebOffice:true,
		    myCallBack:function(a,b,c){
		    	try{
		    		beforePrint();
		    	}catch(e){
		    	}
		    	var nowTab = $("li").filter(".active");
		    	$("[href='#"+a[0]+"']").parent().parent().find("li").each(function(){
		    		$("a",this).trigger("click")
		    	})
		    	var str = a.join(",#");
		    	str="#"+str;
		    	nowTab.find("a").trigger("click")
		    	$(str).printArea();
		    	c();
		    	try{
		    		afterPrint();
		    	}catch(e){
		    	}
		    	
		    }
		}
		new jqueryDialog(obj);
		$(".dialog_div_").parent().addClass("wf_top").css({
                    left:"50%",
                    "margin-left":"-250px"
                });
}

$(function(){
	if(typeof(printBtnFlag)!=undefined && typeof(printBtnFlag)!='undefined'){
		if(printBtnFlag != 1){
			$("#printBtn").append("<button type=\"button\"  class='btn btn_click'  onclick=\"CommonDoPrint();\" >打印</button>");
		}
	}else{
		$("#printBtn").append("<button type=\"button\"  class='btn btn_click'  onclick=\"CommonDoPrint();\" >打印</button>");
	}
	
})