
$("body").append('<script id="template-upload" type="text/x-tmpl">'+
'{% for (var i=0, file; file=o.files[i]; i++) { %}'+
'   <tr class="template-upload">'+
//		'         <td >'+
//		'             <span class="preview"></span>'+
//		'        </td>'+
		'        <td align="left">'+
		'          <p class="name">{%=file.name%}</p>'+
		'            <strong class="error"></strong>'+
		'      </td>'+
		'       <td nowrap="nowrap">'+
		'            <p class="size">Processing...</p>'+
		'         <div class="progress"></div>'+
		'      </td>'+
		'       <td align="right">'+
		'        {% if (!i && !o.options.autoUpload) { %}'+
		'             <button class="start">上传</button>'+
		'        {% } %}'+
        '        {% if (!i) { %}'+
		'           <button class="cancel">取消</button>'+
		'     {% } %}'+
		'  </td>'+
		' </tr>'+
		' {% } %}'+
		' </script>'+
		' <!-- The template to display files available for download -->'+
		' <script id="template-download" type="text/x-tmpl">'+
		' {% for (var i=0, file; file=o.files[i]; i++) { %}'+
		'    <tr class="template-download">'+
		'<td width="5px">{%=i+1%}.</td>'+
//		'         <td>'+
//		'            <span class="preview" >'+
//		'        {% if (file.thumbnailUrl) { %}'+
//		'            <a href="{%=file.url%}" title="{%=file.name%}" download="{%=file.name%}" data-gallery ><img src="{%=file.thumbnailUrl%}"></a>'+
//		' 			<input type="hidden" name="attachmentId" value="{%=file.id%}"/>'+
//		'        {% } %}'+
//		'     </span>'+
//		' </td>'+
		' <td align="left">'+
		'    <p class="name">'+
		'		 <input type="hidden" name="attachmentId" value="{%=file.id%}"/>'+
		' 		 <a style="color:#428bca" href="{%=file.pdfUrl%}" target="_blank">{%=file.name%}</a>'+
//		'        <a style="color:#428bca" href="{%=file.url%}" title="{%=file.name%}" download="{%=file.name%}" {%=file.thumbnailUrl?\'data-gallery\':\'\'%}>{%=file.name%}.{%=file.type%}</a>'+
		'   </p>'+
		'   {% if (file.error) { %}'+
		'       <div><span class="error">Error</span> {%=file.error%}</div>'+
		'   {% } %}'+
		' </td>'+
		' <td class="hideTd1">'+
		'    <span class="size">{%=o.formatFileSize(file.size)%}</span>'+
		' </td>'+
		' <td class="hideTd" align="right">'+
		'   <button class="delete cancel">删除</button>'+
		' </td>'+
		' </tr>'+
		' {% } %}'+
		' </script>');