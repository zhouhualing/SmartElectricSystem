
$("body").append('<script id="template-upload" type="text/x-tmpl">'+
'{% for (var i=0, file; file=o.files[i]; i++) { %}'+
'   <tr class="template-upload">'+
		'        <td align="left" style="border-right:0px">'+
		'          <p class="name">{%=file.name%}</p>'+
		'            <strong class="error"></strong>'+
		'      </td>'+
		'       <td align="right" style="border-left:0px">'+
		'        {% if (!i && !o.options.autoUpload) { %}'+
		'             <button class="start" >上传</button>'+
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
		'        <a style="color:#428bca" href="javascript:void(0);" theUrl= "{%=file.pdfUrl%}" onclick="openAttachments(&quot;{%=file.url%}&quot;,&quot;{%=file.type%}&quot;)" title="{%=file.name%}">{%=file.name%}</a>'+
		'   </p>'+
		'   {% if (file.error) { %}'+
		'       <div><span class="error">Error</span> {%=file.error%}</div>'+
		'   {% } %}'+
		' </td>'+
		' <td class="hideTd" align="right" style="border-left:0px">'+
		'   <div><br/></div>'+
		'   <a href = "{%=file.url%}"><button type="button" onclick ="window.location.href=\'{%=file.url%}\'" class="btn_click">下载</button></a>'+
		'   <div><br/></div>'+
		' </td>'+
		' </tr>'+
		' {% } %}'+
		' </script>');
