
$("body").append('<script id="template-upload" type="text/x-tmpl">'+
'{% for (var i=0, file; file=o.files[i]; i++) { %}'+
'   <tr class="template-upload">'+
		'        <td align="left" style="border-right:0px">'+
		'          <p class="name">{%=file.name%}</p>'+
		'            <strong class="error"></strong>'+
		'      </td>'+
		'       <td align="right" style="border-left:0px">'+
		'        {% if (!i && !o.options.autoUpload) { %}'+
		'             <button class="start btn_select" >上传</button>'+
		'        {% } %}'+
        '        {% if (!i) { %}'+
		'           <div><button class="cancel">取消</button></div>'+
		'     {% } %}'+
		'  </td>'+
		' </tr>'+
		' {% } %}'+
		' </script>'+
		' <!-- The template to display files available for download -->'+
		' <script id="template-download" type="text/x-tmpl">'+
		' {% for (var i=0, file; file=o.files[i]; i++) { %}'+
		'    <tr class="template-download">'+
		' <td align="left" style="border-right:0px">'+
		'    <p class="name">'+
		'		 <input type="hidden" name="attachmentId" value="{%=file.id%}"/>'+
		'        <a style="color:#428bca;height: 43px" href="javascript:void(0);" theUrl= "{%=file.pdfUrl%}" onclick="openAttachments(&quot;{%=file.pdfUrl%}&quot;,&quot;{%=file.type%}&quot;,this)" title="{%=file.name%}">{%=file.name%}</a>'+
		'   </p>'+
		'   {% if (file.error) { %}'+
		'       <div><span class="error">Error</span> {%=file.error%}</div>'+
		'   {% } %}'+
		' </td>'+
		' <td class="hideTd" align="left" style="border-left:0px;width: 62px">'+
		'  <button type="button" onclick ="window.location.href=\'{%=file.url%}\'" class="btn_select" >下载</button></a>'+
		'   <div><button  id="file_removeBtn" class=" cancel btn_select">删除</button></div>'+
		' </td>'+
		' </tr>'+
		' {% } %}'+
		' </script>');




//$("body").append('<script id="template-upload" type="text/x-tmpl">'+
//'{% for (var i=0, file; file=o.files[i]; i++) { %}'+
//'   <tr class="template-upload">'+
//		'        <td align="left" style="border-bottom:0px;padding:10px;" colspan="2">'+
//		'          <p class="name">{%=file.name%}</p>'+
//		'            <strong class="error"></strong>'+
//		'      </td>'+
////		'       <td align="right" style="border-left:0px">'+
////		'  </td>'+
//		' </tr>'+
//		'   <tr class="template-upload" >'+
//		'        <td align="left" style="border-right:0px;border-top:0px">'+
//		'      </td>'+
//		'       <td align="right" style="border-left:0px;width:90%;border-top:0px">'+
//		'        {% if (!i && !o.options.autoUpload) { %}'+
//		'             <button class="start btn_select" >上传</button>'+
//		'        {% } %}'+
//        '        {% if (!i) { %}'+
//		'           <button class="cancel ">取消</button>'+
//		'     {% } %}'+
//		'  </td>'+
//		' </tr>'+
//		' {% } %}'+
//		' </script>'+
//		' <!-- The template to display files available for download -->'+
//		' <script id="template-download" type="text/x-tmpl">'+
//		' {% for (var i=0, file; file=o.files[i]; i++) { %}'+
//		'    <tr class="template-download">'+
//		' <td align="left" style="border-bottom:0px;padding:10px;" colspan="2">'+
//		'    <p class="name">'+
//		'		 <input type="hidden" name="attachmentId" value="{%=file.id%}"/>'+
//		'        <a style="color:#428bca;height: 43px" href="javascript:void(0);" theUrl= "{%=file.pdfUrl%}" onclick="openAttachments(&quot;{%=file.pdfUrl%}&quot;,&quot;{%=file.type%}&quot;,this)" title="{%=file.name%}">{%=file.name%}</a>'+
//		'   </p>'+
//		'   {% if (file.error) { %}'+
//		'       <div><span class="error">Error</span> {%=file.error%}</div>'+
//		'   {% } %}'+
//		' </td>'+
//		' </tr>'+
//		'    <tr class="template-download">'+
//		' <td align="left" style="border-right:0px;border-top:0px">'+
//		' </td>'+
//		' <td class="hideTd" align="right" style="border-left:0px;width:90%;border-top:0px">'+
//		'  <button type="button" onclick ="window.location.href=\'{%=file.url%}\'" class="btn_select" >下载</button></a>'+
//		'   <button  id="file_removeBtn" class=" cancel btn_select">删除</button>'+
//		' </td>'+
//		' </tr>'+
//		' {% } %}'+
//		' </script>');
