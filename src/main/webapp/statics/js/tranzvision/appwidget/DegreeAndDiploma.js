SurveyBuild.extend("DegreeAndDiploma", "baseComponent", {
	itemName: "学历学位",
	title:"学历学位",
	isSingleLine: "Y",
	fixedContainer: "Y",//固定容器标识
	degreeoption: {},
	diplomaoption: {},	
	children: [
	       //学历、学历编码、上传学历证书，学位、学位编码、上传学位证书
	    {
			"itemId": "diploma_level", //学历
			"itemName": MsgSet["DIPLOMA_LEVEL"],
			"title": MsgSet["DIPLOMA_LEVEL"],
			"value": "",
			"StorageType": "S",
			"orderby":1,
			"classname":"Select",
			"wzsm":""
		},
		{
   			"itemId": "diploma_code",  //学历编码
   			"itemName":  MsgSet["DIPLOMA_CODE"],
   			"title":  MsgSet["DIPLOMA_CODE"],
   			"value": "",
   			"StorageType": "S",
   			"orderby":2,
   			"classname":"SingleTextBox"
   		},
   		{
			"prompt": "请上传格式为jpg,png,jpeg,pdf的文件，大小为10M以内。",  //上传学历证书
			"itemId": "diploma_upload",
			"instanceId": "DiplomaUpload",
			"itemName": MsgSet["DIPLOMA_UPLOAD"],
			"title": MsgSet["DIPLOMA_UPLOAD"],
			"isRequire":"Y",
			"orderby": 3,
			"value": "",
			"classname": "imagesUpload",
			"filename": "",
			"sysFileName": "",
			"path": "",
			"accessPath": "",
			"fileType": "jpg,png,jpeg,pdf",//允许上传类型
		    "fileSize": "10",//允许上传大小
		    "isAllowTailoring":"N",   //是否允许裁剪
		    "tailoringStandard":"",   //裁剪类型
			"allowMultiAtta": "Y",//允许多附件上传
			"isDownLoad":"Y",//允许打包下载
			"StorageType":"F",//存储类型-附件
			"children": [{"itemId":"attachment_Upload","itemName":"图片上传","title":"图片上传","orderby":"","fileName":"","sysFileName":"","accessPath":"","viewFileName":""}]
		}, 
		{
			"itemId": "degree_level", //学位
			"itemName": MsgSet["DEGREE_LEVEL"],
			"title":MsgSet["DEGREE_LEVEL"],
			"value": "",
			"StorageType": "S",
			"orderby":4,
			"classname":"Select",
			"wzsm":""
		},
		{
   			"itemId": "degree_code",  //学位编码
   			"itemName":  MsgSet["DEGREE_CODE"],
   			"title":  MsgSet["DEGREE_CODE"],
   			"value": "",
   			"StorageType": "S",
   			"orderby":5,
   			"classname":"SingleTextBox"
   		},
		{
			"prompt": "请上传格式为jpg,png,jpeg,pdf的文件，大小为10M以内。", //上传学位证书
			"itemId": "degree_upload",
			"instanceId": "DegreeUpload",
			"itemName": MsgSet["DEGREE_UPLOAD"],
			"title": MsgSet["DEGREE_UPLOAD"],
			"isRequire":"Y",
			"orderby": 6,
			"value": "",
			"classname": "imagesUpload",
			"filename": "",
			"sysFileName": "",
			"path": "",
			"accessPath": "",
			"fileType": "jpg,png,jpeg,pdf",//允许上传类型
		    "fileSize": "10",//允许上传大小
		    "isAllowTailoring":"N",   //是否允许裁剪
		    "tailoringStandard":"",   //裁剪类型
			"allowMultiAtta": "Y",//允许多附件上传
			"isDownLoad":"Y",//允许打包下载
			"StorageType":"F",//存储类型-附件
			"children": [{"itemId":"attachment_Upload","itemName":"图片上传","title":"图片上传","orderby":"","fileName":"","sysFileName":"","accessPath":"","viewFileName":""}]
		}
	],
	
	
	//用于动态初始化控件的参数，参数d为当前控件实例ID。
	//在调用_gethtml方法之前时被调用
	_init: function(d, previewmode) {

		var degree=new Array();
		degree[0]="博士";
		degree[1]="硕士";
		degree[2]="学士";
		degree[3]="无";
		
		var diploma=new Array();
		diploma[0]="博士研究生";
		diploma[1]="硕士研究生";
		diploma[2]="本科";
		diploma[3]="大专";
		diploma[4]="无";
		
		
		for (var i = 1; i <= 4; ++i) {
			this.degreeoption[d + i] = {
				code: i,
				txt: degree[i-1],
				orderby: i,
				defaultval: 'N',
				other: 'N',
				weight: 0
			}
		}
		
		for (var i = 1; i <= 5; ++i) {
			this.diplomaoption[d + i] = {
				code: i,
				txt: diploma[i-1],
				orderby: i,
				defaultval: 'N',
				other: 'N',
				weight: 0
			}
		} 
	},
	_getHtml: function(data, previewmode) {
		//var c = '';
		var c = '',e='',msg='',typeMsg='',children = data.children,msg2='',typeMsg2='';
		var _sysFilename,_fileSuffix;
		
		//console.log(children);
		
		//console.log(children.lengt);
		
		if (previewmode) {
			
			 if(children[2].fileType != "" || children[2].fileSize != ""){
		        	var typeArr = children[2].fileType.split(",");
		        	var fileType = "";
		        	for(var i = 0; i < typeArr.length; i++){
		        		if (SurveyBuild.BMB_LANG == "ENG"){
		        			fileType = fileType + typeArr[i] + ",";
		        		} else {
		        			fileType = fileType + "." + typeArr[i] + "、";
		        		}	
		        	}
		        	if (fileType != ""){
		        		fileType = fileType.substring(0,fileType.length-1);
		        	}

	        		typeMsg = fileType != "" ? MsgSet["FILETYPE"].replace("【TZ_FILE_TYPE】",fileType) : "";
	        		if (SurveyBuild.BMB_LANG == "ENG"){
	        			msg = children[2].fileSize != "" ? (typeMsg != "" ? typeMsg : "") + " " + MsgSet["FILESIZE"].replace("【TZ_FILE_SIZE】",children[2].fileSize) : typeMsg;
	        		}else{
	        			msg = children[2].fileSize != "" ? (typeMsg != "" ? typeMsg : "") + "，" + MsgSet["FILESIZE"].replace("【TZ_FILE_SIZE】",children[2].fileSize) : typeMsg;	
	        		}
		        }
			 
			 
			 if(children[5].fileType != "" || children[5].fileSize != ""){
		        	var typeArr2 = children[5].fileType.split(",");
		        	var fileType2 = "";
		        	for(var i = 0; i < typeArr2.length; i++){
		        		if (SurveyBuild.BMB_LANG == "ENG"){
		        			fileType2 = fileType2 + typeArr2[i] + ",";
		        		} else {
		        			fileType2 = fileType2 + "." + typeArr2[i] + "、";
		        		}	
		        	}
		        	if (fileType2 != ""){
		        		fileType2 = fileType2.substring(0,fileType2.length-1);
		        	}

	        		typeMsg2 = fileType2 != "" ? MsgSet["FILETYPE"].replace("【TZ_FILE_TYPE】",fileType2) : "";
	        		if (SurveyBuild.BMB_LANG == "ENG"){
	        			msg2 = children[5].fileSize != "" ? (typeMsg2 != "" ? typeMsg2 : "") + " " + MsgSet["FILESIZE"].replace("【TZ_FILE_SIZE】",children[5].fileSize) : typeMsg2;
	        		}else{
	        			msg2 = children[5].fileSize != "" ? (typeMsg2 != "" ? typeMsg2 : "") + "，" + MsgSet["FILESIZE"].replace("【TZ_FILE_SIZE】",children[5].fileSize) : typeMsg2;	
	        		}
		        }
			
			if(SurveyBuild.accessType == "M"){
				if(SurveyBuild._readonly){
					//只读模式
					//学历、学历编码、上传学历证书，学位、学位编码、上传学位证书

					//1.学历 只读手机
 		            e = '<option value="">' + MsgSet["PLEASE_SELECT"] + '</option>';
	                for (var i in data.diplomaoption) {
	                    e += '<option ' + (children[0].value == data["diplomaoption"][i]["code"] ? "selected='selected'": "") + 'value="' + data["diplomaoption"][i]["code"] + '">' + data["diplomaoption"][i]["txt"] + '</option>';
	                }
		            c += '<div class="item">';
		            c += '	<p>'+children[0].title+'<span>'+ (data.isRequire == "Y" ? "*": "") +'</span></p>';
		            c += '	<div class="text-box">';
		            c +='		<select name="' +data["itemId"] + children[0].itemId  + '" class="select1" id="' +data["itemId"] + children[0].itemId  + '"  title="' +data["itemId"] + children[0].itemName + '" disabled>';
		            c +=			e;
		            c +='		</select>';
		            c += '	</div>';
		            c += '</div>';

					//如果学历选择的是无，学历代码和学历上传的文件不显示
					if (children[0].value !="5") {
						//2学历代码 只读手机
//						c += '<div class="item">';
//						c += '<p>'+children[1].title+'<span>'+(data.isRequire == "Y" ? "*": "")+'</span></p>';
//						c += '<div class="text-box"><input readonly="true" type="text" class="text1" id="' + data["itemId"] + children[1].itemId + '" name="' + data["itemId"] + children[1].itemId + '" value="' + children[1].value + '"/></div>';
//						c += '<p style="color:#666;font-size:0.56rem;"></p>';
//						c += '</div>';

						//3学历上传附件  只读手机
						c += '<div class="item">';
						c += '	<p>'+children[2].title+'<span>'+(data.isRequire == "Y" ? "*": "")+'</span></p>';
						c += '  <div id="' + data.itemId + children[2].itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
						c += ' 	<div class="text-box" style="border:none;display:' + (SurveyBuild._readonly?'none':'block') +' " >';
						c += '		<div class="handle">';
						c += '   		<div class="ncsc-upload-btn">';
						c += '    			 <a href="#" class="ncsc-upload-btn-a">';
						c += '					<span class="ncsc-upload-btn-span">';
						c +=' 						<input type="file" hidefocus="true" size="1" class="input-file" name="goods_image" data-instancid="' + data.itemId+ children[2].instanceId + '" id="'+ data.itemId + children[2].itemId + '" name="'+ data.itemId + children[2].itemId + '" title="' + data.itemId + children[2].itemName + '" onchange="SurveyBuild.degreeUploadAttachment(this,\''+ data.instanceId +'\',\'' + data.itemId+ children[2].instanceId + '\',\'diploma\')"  ></span>';
						c +='   				<div class="ncsc-upload-btn-p">'+ MsgSet["UPLOAD_BTN_MSG2"] +'<img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/upload.png'+'"></div>';
						c +='      			</a>';
						c +='         	</div>';
						c +='       </div>';
						c +='   </div>';
						c +='<p class="mSuffix" style="color:#666;font-size:0.56rem;display:' + (SurveyBuild._readonly?'none':'block') +' ">' + (data.suffixUrl != "" ? "<a href='" + data.suffixUrl + "'>" : "") + (data.suffix != "" ? data.suffix : "") + (data.suffixUrl != "" ? "</a>" : "") +'</p>';
						c +='<p style="color:#666;font-size:0.56rem;margin-top:5px;display:' + (SurveyBuild._readonly?'none':'block') +' ">'+msg+'</p>';

						c +='</div>';
						
						if(children[2].children[0].viewFileName==""){
							c += ' <div class="upload_list" id="'+data.itemId + children[2].itemId+'_AttList" style="display:none">';
							c += ' <p style="display:' + (SurveyBuild._readonly?'none':'') +'">'+ MsgSet["UP_FILE_LIST"] +'</p>';
							c += '</div>';
						}else{
							c += ' <div class="upload_list"  id="'+data.itemId + children[2].itemId+'_AttList" style="display:' + (children[2].children.length < 1 ? 'none':'block') + '">';
							c += ' <p style="display:' + (SurveyBuild._readonly?'none':'') +'">'+ MsgSet["UP_FILE_LIST"] +'</p>';
							if(children[2].allowMultiAtta == "Y"){
								for(var i = 0; i < children[2].children.length; i++){
									if (children[2].children[i].viewFileName != "" && children[2].children[i].sysFileName != ""){
										c += '<li class="fileLi"><span><a id="a_'+data.itemId + children[2].itemId+'_'+i+'"   onclick="SurveyBuild.viewImageSetFordegree(this,\''+ data.instanceId +'\',\'' + data.itemId+ children[2].instanceId + '\',\'' + i + '\',\'diploma\')" file-index="' +  children[2].children[i].orderby + '">'+ children[2].children[i].viewFileName+'</a></span><i  onclick="SurveyBuild.deleteFileForDegree(this,\'' + data.instanceId + '\',\'' + data.itemId+ children[2].instanceId + '\',\'diploma\')" style="display: ' + (SurveyBuild._readonly?'none':'block') + ';background:url(' + TzUniversityContextPath + '/statics/images/appeditor/m/de.png'+') no-repeat center center""></i></li>';
									}
								}
							}else{
								for(var i=0; i<children[2].children.length; i++){
									if (children[2].children[i].viewFileName != "" && children[2].children[i].sysFileName != ""){
										c += '<li class="fileLi"><span><a  id="a_'+data.itemId + children[2].itemId+'_'+i+'"   onclick="SurveyBuild.viewImageSetFordegree(this,\''+ data.instanceId +'\',\'' + data.itemId+ children[2].instanceId + '\',\'' + i + '\',\'diploma\')" file-index="' + children[2].children[i].orderby + '">'+ children[2].children[i].viewFileName+'</a></span><i  onclick="SurveyBuild.deleteFileForDegree(this,\'' + data.instanceId + '\',\'' + data.itemId+ children[2].instanceId + '\',\'diploma\')" style="display: ' + (SurveyBuild._readonly?'none':'block') + ';background:url(' + TzUniversityContextPath + '/statics/images/appeditor/m/de.png'+') no-repeat center center""></i></li>';
									}
								}
							}
							c +='</div>';
						}
						c += '<div class="img_shade" id ="shade_'+data.itemId + children[2].itemId+'" style="display:none"></div>';
						c += '<img class="img_pop_close" style="display:none"id ="close_'+data.itemId + children[2].itemId+'" src="'+ TzUniversityContextPath + '/statics/images/appeditor/m/rl_btn.png'+'">';
						c += '<div class="img_pop_body" style="display:none" id ="body_'+data.itemId + children[2].itemId+'">'  ;
						c += ' <img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/" id ="img_'+data.itemId + children[2].itemId+'">';
						c += '</div>';

					}
		            
		            //4.学位
		            e = '<option value="">' + MsgSet["PLEASE_SELECT"] + '</option>';
	                for (var i in data.degreeoption) {
	                    e += '<option ' + (children[3].value == data["degreeoption"][i]["code"] ? "selected='selected'": "") + 'value="' + data["degreeoption"][i]["code"] + '">' + data["degreeoption"][i]["txt"] + '</option>';
	                }
		            c += '<div class="item">';
		            c += '	<p>'+children[3].title+'<span>'+ (data.isRequire == "Y" ? "*": "") +'</span></p>';
		            c += '	<div class="text-box">';
		            c +='		<select name="' +data["itemId"] + children[3].itemId  + '" class="select1" id="' +data["itemId"] + children[3].itemId  + '"  title="' +data["itemId"] + children[3].itemName + '" disabled>';
		            c +=			e;
		            c +='		</select>';
		            c += '	</div>';
		            c += '</div>';


					//如果学位选择的是无，学位代码和学位上传的文件不显示
					if (children[3].value !="4") {

						//5学位代码
//						c += '<div class="item">';
//						c += '<p>'+children[4].title+'<span>'+(data.isRequire == "Y" ? "*": "")+'</span></p>';
//						c += '<div class="text-box"><input readonly="true" type="text" class="text1" id="' + data["itemId"] + children[4].itemId + '" name="' + data["itemId"] + children[4].itemId + '" value="' + children[4].value + '"/></div>';
//						c += '<p style="color:#666;font-size:0.56rem;"></p>';
//						c += '</div>';

						//6学位上传附件
						c += '<div class="item">';
						c += '	<p>' + children[5].title + '<span>' + (data.isRequire == "Y" ? "*" : "") + '</span></p>';
						c += '  <div id="' + data.itemId + children[5].itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
						c += ' 	<div class="text-box" style="border:none;display:' + (SurveyBuild._readonly ? 'none' : 'block') + ' " >';
						c += '		<div class="handle">';
						c += '   		<div class="ncsc-upload-btn">';
						c += '    			 <a href="#" class="ncsc-upload-btn-a">';
						c += '					<span class="ncsc-upload-btn-span">';
						c += ' 						<input type="file" hidefocus="true" size="1" class="input-file" name="goods_image" data-instancid="' + data.itemId + children[5].instanceId + '" id="' + data.itemId + children[5].itemId + '" name="' + data.itemId + children[5].itemId + '" title="' + data.itemId + children[5].itemName + '" onchange="SurveyBuild.degreeUploadAttachment(this,\'' + data.instanceId + '\',\'' + data.itemId + children[5].instanceId + '\',\'degree\')"  ></span>';
						c += '   				<div class="ncsc-upload-btn-p">' + MsgSet["UPLOAD_BTN_MSG2"] + '<img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/upload.png' + '"></div>';
						c += '      			</a>';
						c += '         	</div>';
						c += '       </div>';
						c += '   </div>';
						c += '<p class="mSuffix" style="color:#666;font-size:0.56rem;display:' + (SurveyBuild._readonly ? 'none' : 'block') + ' ">' + (data.suffixUrl != "" ? "<a href='" + data.suffixUrl + "'>" : "") + (data.suffix != "" ? data.suffix : "") + (data.suffixUrl != "" ? "</a>" : "") + '</p>';
						c += '<p style="color:#666;font-size:0.56rem;margin-top:5px;display:' + (SurveyBuild._readonly ? 'none' : 'block') + ' ">' + msg2 + '</p>';

						c += '</div>';
						if (children[5].children[0].viewFileName == "") {
							c += ' <div class="upload_list" id="' + data.itemId + children[5].itemId + '_AttList" style="display:none">';
							c += ' <p style="display:' + (SurveyBuild._readonly ? 'none' : '') + '">' + MsgSet["UP_FILE_LIST"] + '</p>';
							c += '</div>';
						} else {
							c += ' <div class="upload_list"  id="' + data.itemId + children[5].itemId + '_AttList" style="display:' + (children[5].children.length < 1 ? 'none' : 'block') + '">';
							c += ' <p style="display:' + (SurveyBuild._readonly ? 'none' : '') + '">' + MsgSet["UP_FILE_LIST"] + '</p>';
							if (children[5].allowMultiAtta == "Y") {
								for (var i = 0; i < children[5].children.length; i++) {
									if (children[5].children[i].viewFileName != "" && children[5].children[i].sysFileName != "") {
										c += '<li class="fileLi"><span><a id="a_' + data.itemId + children[5].itemId + '_' + i + '"   onclick="SurveyBuild.viewImageSetFordegree(this,\'' + data.instanceId + '\',\'' + data.itemId + children[5].instanceId + '\',\'' + i + '\',\'degree\')" file-index="' + children[5].children[i].orderby + '">' + children[5].children[i].viewFileName + '</a></span><i  onclick="SurveyBuild.deleteFileForDegree(this,\'' + data.instanceId + '\',\'' + data.itemId + children[5].instanceId + '\',\'degree\')" style="display: ' + (SurveyBuild._readonly ? 'none' : 'block') + ';background:url(' + TzUniversityContextPath + '/statics/images/appeditor/m/de.png' + ') no-repeat center center""></i></li>';
									}
								}
							} else {
								for (var i = 0; i < children[5].children.length; i++) {
									if (children[5].children[i].viewFileName != "" && children[5].children[i].sysFileName != "") {
										c += '<li class="fileLi"><span><a  id="a_' + data.itemId + children[5].itemId + '_' + i + '"   onclick="SurveyBuild.viewImageSetFordegree(this,\'' + data.instanceId + '\',\'' + data.itemId + children[5].instanceId + '\',\'' + i + '\',\'degree\')" file-index="' + children[5].children[i].orderby + '">' + children[5].children[i].viewFileName + '</a></span><i  onclick="SurveyBuild.deleteFileForDegree(this,\'' + data.instanceId + '\',\'' + data.itemId + children[5].instanceId + '\',\'degree\')" style="display: ' + (SurveyBuild._readonly ? 'none' : 'block') + ';background:url(' + TzUniversityContextPath + '/statics/images/appeditor/m/de.png' + ') no-repeat center center""></i></li>';
									}
								}
							}
							c += '</div>';
						}
						c += '<div class="img_shade" id ="shade_' + data.itemId + children[5].itemId + '" style="display:none"></div>';
						c += '<img class="img_pop_close" style="display:none"id ="close_' + data.itemId + children[5].itemId + '" src="' + TzUniversityContextPath + '/statics/images/appeditor/m/rl_btn.png' + '">';
						c += '<div class="img_pop_body" style="display:none" id ="body_' + data.itemId + children[5].itemId + '">';
						c += ' <img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/" id ="img_' + data.itemId + children[5].itemId + '">';
						c += '</div>';
					}
		            
				}else{
					//编辑模式
					//学历、学历编码、上传学历证书，学位、学位编码、上传学位证书
					//1.学位
					e = '<option value="">' + MsgSet["PLEASE_SELECT"] + '</option>';
	                for (var i in data.diplomaoption) {
	                    e += '<option ' + (children[0]["value"] == data["diplomaoption"][i]["code"] ? "selected='selected'": "") + 'value="' + data["diplomaoption"][i]["code"] + '">' + data["diplomaoption"][i]["txt"] + '</option>';
	                }
	                c += '<div class="item">';
		            c += '	<p>'+children[0].title+'<span>'+ (data.isRequire== "Y" ? "*": "") +'</span></p>';
		            c += '  <div id="' + data["itemId"]+ children[0].itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
		            c += '	<div class="text-box">' ;
		            c +='		<select name="' + data["itemId"]+ children[0].itemId + '" class="select1" id="' + data["itemId"]+ children[0].itemId + '"  title="' + data["itemId"]+ children[0].itemName + '">';
		            c +=			e;
		            c +='		</select>';
		            c += '	</div>';
		            //c += '	<p  class="mSuffix" style="color:#666;font-size:0.56rem;">' +(data["suffix"] ? data.suffix:" ") + '</p>';
		            c += '</div>';
		            
		            //2学历编码
//					if (children[0]["value"]!="5" && children[0]["value"]!="") {
//						c += '<div class="item" id="'+data.itemId +children[1].itemId+'Show">';
//					} else {
//						c += '<div class="item" style="display:none" id="'+data.itemId +children[1].itemId+'Show">';
//					}
//					if (children[0]["value"]!="5" && children[0]["value"]!=""&&data.isRequire=="Y") {
//						c += '<p>'+children[1].title+'<span id ="'+data.itemId +children[1].itemId+'span" > * </span></p>';
//					}else{
//						c += '<p>'+children[1].title+'<span id ="'+data.itemId +children[1].itemId+'span"> </span></p>';
//					}
//					c += '<div id="' + data["itemId"]+ children[1].itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
//					c += '<div class="text-box"><input  type="text" class="text1" id="' + data["itemId"]+ children[1].itemId + '" name="' + data["itemId"]+ children[1].itemId + '" value="' + children[1]["value"] + '"/></div>';
//					c += '</div>';


					//3 学历上传附件
					if (children[0]["value"]!="5"  && children[0]["value"]!="") {
						c += '<div class="item" id="upload_'+data.itemId +children[2].itemId+'">';
					} else {
						c += '<div class="item" id="upload_'+data.itemId +children[2].itemId+'" style="display:none">';
					}
					if (data.isRequire=="Y") {
						c += '<p>'+children[2].title+'<span id ="'+data.itemId +children[2].itemId+'span"> * </span></p>';
					}else{
						c += '<p>'+children[2].title+'<span id ="'+data.itemId +children[2].itemId+'span"> </span></p>';
					}
					c += '  <div id="' + data.itemId + children[2].itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
					c += ' 	<div class="text-box" style="border:none;display:' + (SurveyBuild._readonly?'none':'block') +' " >';
					c += '		<div class="handle">';
					c += '   		<div class="ncsc-upload-btn">';
					c += '    			 <a href="#" class="ncsc-upload-btn-a">';
					c += '					<span class="ncsc-upload-btn-span">';
					c +=' 						<input type="file" hidefocus="true" size="1" class="input-file" name="goods_image" data-instancid="' + data.itemId+ children[2].instanceId + '" id="'+ data.itemId + children[2].itemId + '" name="'+ data.itemId + children[2].itemId + '" title="' + data.itemId + children[2].itemName + '" onchange="SurveyBuild.degreeUploadAttachment(this,\''+ data.instanceId +'\',\'' + data.itemId+ children[2].instanceId + '\',\'diploma\')"  ></span>';
					c +='   				<div class="ncsc-upload-btn-p">'+ MsgSet["UPLOAD_BTN_MSG2"] +'<img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/upload.png'+'"></div>';
					c +='      			</a>';
					c +='         	</div>';
					c +='       </div>';
					c +='   </div>';
					c +='<p class="mSuffix" style="color:#666;font-size:0.56rem;display:' + (SurveyBuild._readonly?'none':'block') +' ">' + (data.suffixUrl != "" ? "<a href='" + data.suffixUrl + "'>" : "") + (data.suffix != "" ? data.suffix : "") + (data.suffixUrl != "" ? "</a>" : "") +'</p>';
					c +='<p style="color:#666;font-size:0.56rem;margin-top:5px;display:' + (SurveyBuild._readonly?'none':'block') +' ">'+msg+'</p>';

					c +='</div>';
					console.log("学历viewFileName"+children[2].children[0].viewFileName);
					
					console.dir(children[2]);
					if(children[2].children[0].viewFileName=="" || children[0]["value"]=="5"){
						c += ' <div class="upload_list" id="'+data.itemId + children[2].itemId+'_AttList" style="display:none">';
						c += ' <p style="display:' + (SurveyBuild._readonly?'none':'') +'">'+ MsgSet["UP_FILE_LIST"] +'</p>';
						c += '</div>';
					}else{
						c += ' <div class="upload_list"  id="'+data.itemId + children[2].itemId+'_AttList" style="display:' + (children[2].children.length < 1 ? 'none':'block') + '">';
						c += ' <p style="display:' + (SurveyBuild._readonly?'none':'') +'">'+ MsgSet["UP_FILE_LIST"] +'</p>';
						if(children[2].allowMultiAtta == "Y"){
							for(var i = 0; i < children[2].children.length; i++){
								if (children[2].children[i].viewFileName != "" && children[2].children[i].sysFileName != ""){
									c += '<li class="fileLi"><span><a id="img_'+data.itemId + children[2].itemId+'_'+i+'"   onclick="SurveyBuild.viewImageSetFordegree(this,\''+ data.instanceId +'\',\'' + data.itemId+ children[2].instanceId + '\',\'' + i + '\',\'diploma\')" file-index="' +  children[2].children[i].orderby + '">'+ children[2].children[i].viewFileName+'</a></span><i  onclick="SurveyBuild.deleteFileForDegree(this,\'' + data.instanceId + '\',\'' + data.itemId+ children[2].instanceId + '\',\'diploma\')" style="display: ' + (SurveyBuild._readonly?'none':'block') + ';background:url(' + TzUniversityContextPath + '/statics/images/appeditor/m/de.png'+') no-repeat center center""></i></li>';
								}
							}
						}else{
							for(var i=0; i<children[2].children.length; i++){
								if (children[2].children[i].viewFileName != "" && children[2].children[i].sysFileName != ""){
									c += '<li class="fileLi"><span><a  id="img_'+data.itemId + children[2].itemId+'_'+i+'"   onclick="SurveyBuild.viewImageSetFordegree(this,\''+ data.instanceId +'\',\'' + data.itemId+ children[2].instanceId + '\',\'' + i + '\',\'diploma\')" file-index="' + children[2].children[i].orderby + '">'+ children[2].children[i].viewFileName+'</a></span><i  onclick="SurveyBuild.deleteFileForDegree(this,\'' + data.instanceId + '\',\'' + data.itemId+ children[2].instanceId + '\',\'diploma\')" style="display: ' + (SurveyBuild._readonly?'none':'block') + ';background:url(' + TzUniversityContextPath + '/statics/images/appeditor/m/de.png'+') no-repeat center center""></i></li>';
								}
							}
						}
						c +='</div>';
					}
					c += '<div class="img_shade" id ="shade_'+data.itemId + children[2].itemId+'" style="display:none"></div>';
					c += '<img class="img_pop_close" style="display:none"id ="close_'+data.itemId + children[2].itemId+'" src="'+ TzUniversityContextPath + '/statics/images/appeditor/m/rl_btn.png'+'">';
					c += '<div class="img_pop_body" style="display:none" id ="body_'+data.itemId + children[2].itemId+'">'  ;
					c += ' <img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/" id ="img_'+data.itemId + children[2].itemId+'">';
					c += '</div>';


		            //4。学位
		            e = '<option value="">' + MsgSet["PLEASE_SELECT"] + '</option>';
	                for (var i in data.degreeoption) {
	                    e += '<option ' + (children[3]["value"] == data["degreeoption"][i]["code"] ? "selected='selected'": "") + 'value="' + data["degreeoption"][i]["code"] + '">' + data["degreeoption"][i]["txt"] + '</option>';
	                }
	                c += '<div class="item">';
		            c += '	<p>'+children[3].title+'<span>'+ (data.isRequire == "Y" ? "*": "") +'</span></p>';
		            c += '  <div id="' + data["itemId"]+ children[3].itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
		            c += '	<div class="text-box">' ;
		            c +='		<select name="' + data["itemId"]+ children[3].itemId + '" class="select1" id="' + data["itemId"]+ children[3].itemId + '"  title="' + data["itemId"]+ children[3].itemName + '">';
		            c +=			e;
		            c +='		</select>';
		            c += '	</div>';
		            //c += '	<p  class="mSuffix" style="color:#666;font-size:0.56rem;">' +(data["suffix"] ? data.suffix:" ") + '</p>';
		            c += '</div>';

					//5 学位编码
//					if (children[3]["value"]!="4" && children[3]["value"]!="") {
//						c += '<div class="item" id="'+data.itemId +children[4].itemId+'Show">';
//					} else {
//						c += '<div class="item" style="display:none" id="'+data.itemId +children[4].itemId+'Show">';
//					}
//					if (children[3]["value"]!="4" && children[3]["value"]!=""&& data.isRequire) {
//						c += '<p>'+children[4].title+'<span id ="'+data.itemId +children[4].itemId+'span"> * </span></p>';
//					}else{
//						c += '<p>'+children[4].title+'<span id ="'+data.itemId +children[4].itemId+'span"> </span></p>';
//					}
//					
//					c += '<div id="' + data["itemId"]+ children[4].itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
//					c += '<div class="text-box"><input  type="text" class="text1" id="' + data["itemId"]+ children[4].itemId + '" name="' + data["itemId"]+ children[4].itemId + '" value="' + children[4]["value"] + '"/></div>';
//					c += '</div>';
		            
		          	//6.学位上传附件
					if (children[3]["value"]!="4" && children[3]["value"]!="") {
						c += '<div class="item" id="upload_'+data.itemId +children[5].itemId+'">';
					} else {
						c += '<div class="item" id="upload_'+data.itemId +children[5].itemId+'" style="display:none">';
					}
					if (data.isRequire=="Y") {
						 c+= '	<p>'+children[5].title+'<span id ="'+data.itemId +children[5].itemId+'span"> * </span></p>';
					}else{
						 c+= '	<p>'+children[5].title+'<span id ="'+data.itemId +children[5].itemId+'span"> </span></p>';
					}
					
					c += '  <div id="' + data.itemId + children[5].itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
					c += ' 	<div class="text-box" style="border:none;display:' + (SurveyBuild._readonly?'none':'block') +' " >';
					c += '		<div class="handle">';
					c += '   		<div class="ncsc-upload-btn">';
					c += '    			 <a href="#" class="ncsc-upload-btn-a">';
					c += '					<span class="ncsc-upload-btn-span">';
					c +=' 						<input type="file" hidefocus="true" size="1" class="input-file" name="goods_image" data-instancid="' + data.itemId+ children[5].instanceId + '" id="'+ data.itemId + children[5].itemId + '" name="'+ data.itemId + children[5].itemId + '" title="' + data.itemId + children[5].itemName + '" onchange="SurveyBuild.degreeUploadAttachment(this,\''+ data.instanceId +'\',\'' + data.itemId+ children[5].instanceId + '\',\'degree\')"  ></span>';
					c +='   				<div class="ncsc-upload-btn-p">'+ MsgSet["UPLOAD_BTN_MSG2"] +'<img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/upload.png'+'"></div>';
					c +='      			</a>';
					c +='         	</div>';
					c +='       </div>';
					c +='   </div>';
					c +='<p class="mSuffix" style="color:#666;font-size:0.56rem;display:' + (SurveyBuild._readonly?'none':'block') +' ">' + (data.suffixUrl != "" ? "<a href='" + data.suffixUrl + "'>" : "") + (data.suffix != "" ? data.suffix : "") + (data.suffixUrl != "" ? "</a>" : "") +'</p>';
					c +='<p style="color:#666;font-size:0.56rem;margin-top:5px;display:' + (SurveyBuild._readonly?'none':'block') +' ">'+msg2+'</p>';

					c +='</div>';
					console.log("学位viewFileName"+children[5].children[0].viewFileName);
					if(children[5].children[0].viewFileName=="" || children[3]["value"]=="4"){
						c += ' <div class="upload_list" id="'+data.itemId + children[5].itemId+'_AttList" style="display:none">';
						c += ' <p style="display:' + (SurveyBuild._readonly?'none':'') +'">'+ MsgSet["UP_FILE_LIST"] +'</p>';
						c += '</div>';
					}else{
						c += ' <div class="upload_list"  id="'+data.itemId + children[5].itemId+'_AttList" style="display:' + (children[5].children.length < 1 ? 'none':'block') + '">';
						c += ' <p style="display:' + (SurveyBuild._readonly?'none':'') +'">'+ MsgSet["UP_FILE_LIST"] +'</p>';
						if(children[5].allowMultiAtta == "Y"){
							for(var i = 0; i < children[5].children.length; i++){
								if (children[5].children[i].viewFileName != "" && children[5].children[i].sysFileName != ""){
									c += '<li class="fileLi"><span><a id="img_'+data.itemId + children[5].itemId+'_'+i+'"   onclick="SurveyBuild.viewImageSetFordegree(this,\''+ data.instanceId +'\',\'' + data.itemId+ children[5].instanceId + '\',\'' + i + '\',\'degree\')" file-index="' +  children[5].children[i].orderby + '">'+ children[5].children[i].viewFileName+'</a></span><i  onclick="SurveyBuild.deleteFileForDegree(this,\'' + data.instanceId + '\',\'' + data.itemId+ children[5].instanceId + '\',\'degree\')" style="display: ' + (SurveyBuild._readonly?'none':'block') + ';background:url(' + TzUniversityContextPath + '/statics/images/appeditor/m/de.png'+') no-repeat center center""></i></li>';
								}
							}
						}else{
							for(var i=0; i<children[5].children.length; i++){
								if (children[5].children[i].viewFileName != "" && children[5].children[i].sysFileName != ""){
									c += '<li class="fileLi"><span><a  id="img_'+data.itemId + children[5].itemId+'_'+i+'"   onclick="SurveyBuild.viewImageSetFordegree(this,\''+ data.instanceId +'\',\'' + data.itemId+ children[5].instanceId + '\',\'' + i + '\',\'degree\')" file-index="' + children[5].children[i].orderby + '">'+ children[5].children[i].viewFileName+'</a></span><i  onclick="SurveyBuild.deleteFileForDegree(this,\'' + data.instanceId + '\',\'' + data.itemId+ children[5].instanceId + '\',\'degree\')" style="display: ' + (SurveyBuild._readonly?'none':'block') + ';background:url(' + TzUniversityContextPath + '/statics/images/appeditor/m/de.png'+') no-repeat center center""></i></li>';
								}
							}
						}
						c +='</div>';
					}
					c += '<div class="img_shade" id ="shade_'+data.itemId + children[5].itemId+'" style="display:none"></div>';
					c += '<img class="img_pop_close" style="display:none"id ="close_'+data.itemId + children[5].itemId+'" src="'+ TzUniversityContextPath + '/statics/images/appeditor/m/rl_btn.png'+'">';
					c += '<div class="img_pop_body" style="display:none" id ="body_'+data.itemId + children[5].itemId+'">'  ;
					c += ' <img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/" id ="img_'+data.itemId + children[5].itemId+'">';
					c += '</div>';
				}
			}else{
				//PC版本
				if(SurveyBuild._readonly){
					//只读模式
					//学历、学历编码、上传学历证书，学位、学位编码、上传学位证书
					//1.学历
					c += '<div class="input-list">';
					c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + children[0].title + '</div>';
					c += '  <div class="input-list-text left">' + children[0].wzsm ;
					c += '  <input id="' +data["itemId"] + children[0].itemId  + '" type="hidden" name="" value="" readonly="" disabled="">';
					c += '  </div>';
					c += '  <div class="input-list-suffix left"></div>';
					c += '  <div class="clear"></div>';
					c += '</div>';

					//如果学历选择的是无，学历代码和学历上传的文件不显示
					if (children[0].value !="5") {
						//2.学历编码
//						c += '<div class="input-list" >';
//						c += '	<div class="input-list-info left"><span class="red-star">' + (SurveyBuild._readonly == "Y" ? "*": "") + '</span>' + children[1].title + '</div>';
//						c += '  <div class="input-list-text left">' + children[1].value + '</div>';
//						c += '  <div class="input-list-suffix left"></div>';
//						c += '  <div class="clear"></div>';
//						c += '</div>';

						//3.学历上传附件
						c += '<div class="input-list-blank margart15" id="upload_' + data.itemId + children[2].itemId + '">';
						c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' +  children[2].title  + '</div>';
						c += '   	<div class="input-list-texttemplate left" style="display:' + (SurveyBuild._readonly?'none':'block') + '">';
						c += '		<div>' + data.onShowMessage + '</div>';
						c += '		<div class="filebtn left">';
						c += '			<div class="filebtn-org"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/upload.png" />&nbsp;&nbsp;' + MsgSet["UPLOAD_BTN_MSG2"] + '</div>';
						c += '			<input type="file" class="filebtn-orgtext" data-instancid = "' +data.itemId+ children[2].instanceId + '" id = "' + data.itemId + children[2].itemId + '" name = "' + data.itemId + children[2].itemId + '" title="' + data.itemId + children[2].itemName + '" onchange="SurveyBuild.degreeUploadAttachment(this,\''+ data.instanceId +'\',\'' + data.itemId+ children[2].instanceId + '\',\'diploma\')">';
						c += '		</div>';
						c += '	<div class="file-list-suffix" style="display:' + (SurveyBuild._readonly?'none':'block') + '">' + (data.suffixUrl != "" ? "<a href='" + data.suffixUrl + "'>" : "") + (data.suffix != "" ? data.suffix : "") + (data.suffixUrl != "" ? "</a>" : "") +'</div>';
						c += '		<div class="clear"></div>';
						c += '		<div>' + msg + '<div id="' + data.itemId + children[2].itemId+ 'Tip" class="onShow" style="line-height:32px;height:18px;"><div class="onShow"></div></div></div>';
						c += '	</div>';
						c += '	<div class="input-list-info-blank left" style="display:' + (SurveyBuild._readonly?'none':'block') + '"><span class="red"></div>';
						c += '   	<div class="input-list-upload left">';
						c += '		<div class="input-list-upload-con" id="'+data.itemId + children[2].itemId+'_AttList" style="display:' + (children[2].children.length < 1 ? 'none':'black') + '">';
						if(children[2].allowMultiAtta == "Y"){
							for(var i = 0; i < children[2].children.length; i++){
								_sysFilename = children[2].children[i].sysFileName;
								_fileSuffix = (_sysFilename.substring(_sysFilename.lastIndexOf(".") + 1)).toUpperCase();
								if (children[2].children[i].viewFileName != "" && children[2].children[i].sysFileName != ""){
									c += '<div class="input-list-uploadcon-list">';
									c += '	<div class="input-list-uploadcon-listl left">';
									c += '		<a class="input-list-uploadcon-list-a" onclick=SurveyBuild.downLoadFileForDegree(this,\''+ data.instanceId +'\',"' + data.itemId+ children[2].instanceId + '",\'diploma\') file-index = "' + children[2].children[i].orderby + '">' + children[2].children[i].viewFileName + '</a>';
									if(_fileSuffix == "PDF" && data.isOnlineShow == "Y"){
										c += '<div class="input-list-uploadcon-list-pdf" onclick="SurveyBuild.PDFpreviewForDegree(this,\''+ data.instanceId +'\',\'' + data.itemId+ children[2].instanceId + '\',\'diploma\')" file-index = "' + children[2].children[i].orderby + '">&nbsp;&nbsp;<img src="' + TzUniversityContextPath + '/statics/images/appeditor/preview.png" title="' + MsgSet["PDF_VIEW"] + '"/>&nbsp;</div>';
									}
									c +='	</div>';
									c += '<div class="input-list-uploadcon-listr left" style="display: ' + (SurveyBuild._readonly?'none':'block') + ';line-height:46px;" onclick="SurveyBuild.deleteFileForDegree(this,\''+ data.instanceId +'\',\'' + data.itemId+ children[2].instanceId + '\',\'diploma\')"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="' + MsgSet["DEL"] + '"/>&nbsp;</div>';
									c += '	<div class="clear"></div>';
									c += '</div>';
								}
							}
						}else{
							for(var i = 0; i < children[2].children.length; i++){
								_sysFilename = children[2].children[i].sysFileName;
								_fileSuffix = (_sysFilename.substring(_sysFilename.lastIndexOf(".") + 1)).toUpperCase();
								if (children[2].children[i].viewFileName != "" && children[2].children[i].sysFileName != ""){
									c += '<div class="input-list-uploadcon-list">';
									c += '	<div class="input-list-uploadcon-listl left">';
									c += '		<a class="input-list-uploadcon-list-a" onclick=SurveyBuild.downLoadFileForDegree(this,"' + data.itemId+ children[2].instanceId + '",\'diploma\') file-index = "' + children[2].children[i].orderby + '">' + children[2].children[i].viewFileName + '</a>';
									if(_fileSuffix == "PDF" && data.isOnlineShow == "Y"){
										c += '<div class="input-list-uploadcon-list-pdf" onclick="SurveyBuild.PDFpreviewForDegree(this,\''+ data.instanceId +'\',\'' + data.itemId+ children[2].instanceId + '\',\'diploma\')" file-index = "' + children[2].children[i].orderby + '">&nbsp;&nbsp;<img src="' + TzUniversityContextPath + '/statics/images/appeditor/preview.png" title="' + MsgSet["PDF_VIEW"] + '"/>&nbsp;</div>';
									}
									c +='	</div>';
									c += '<div class="input-list-uploadcon-listr left" style="display: ' + (SurveyBuild._readonly?'none':'block') + ';line-height:46px;" onclick="SurveyBuild.deleteFile(this,\'' + data.itemId+ children[2].instanceId + '\',\'diploma\')"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="' + MsgSet["DEL"] + '"/>&nbsp;</div>';
									c += '	<div class="clear"></div>';
									c += '</div>';
								}
							}
						}

						c += '		</div>';
						c += '	</div>';
						c += '	<div class="input-list-suffix-blank left"></div>';
						c += '	<div class="clear"></div>';
						c += '</div>';

					}

					
					//4.学位
					c += '<div class="input-list">';
					c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + children[3].title + '</div>';
					c += '  <div class="input-list-text left">' + children[3].wzsm ;
					c += '  <input id="' +data["itemId"] + children[3].itemId  + '" type="hidden" name="" value="" readonly="" disabled="">';
					c += '  </div>';
					c += '  <div class="input-list-suffix left"></div>';
					c += '  <div class="clear"></div>';
					c += '</div>';



					//如果学位选择的是无，学位代码和学位上传的文件不显示
					if (children[3].value !="4") {

						//5.学位编码
//						c += '<div class="input-list" >';
//						c += '	<div class="input-list-info left"><span class="red-star">' + (SurveyBuild._readonly == "Y" ? "*": "") + '</span>' + children[4].title + '</div>';
//						c += '  <div class="input-list-text left">' + children[4].value + '</div>';
//						c += '  <div class="input-list-suffix left"></div>';
//						c += '  <div class="clear"></div>';
//						c += '</div>';

						//6.学位上传附件
						c += '<div class="input-list-blank margart15" id="upload_' + data.itemId + children[5].itemId + '">';
						c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*" : "") + '</span>' + children[5].title + '</div>';
						c += '   	<div class="input-list-texttemplate left" style="display:' + (SurveyBuild._readonly ? 'none' : 'block') + '">';
						c += '		<div>' + data.onShowMessage + '</div>';
						c += '		<div class="filebtn left">';
						c += '			<div class="filebtn-org"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/upload.png" />&nbsp;&nbsp;' + MsgSet["UPLOAD_BTN_MSG2"] + '</div>';
						c += '			<input type="file" class="filebtn-orgtext" data-instancid = "' + data.itemId + children[5].instanceId + '" id = "' + data.itemId + children[5].itemId + '" name = "' + data.itemId + children[5].itemId + '" title="' + data.itemId + children[5].itemName + '" onchange="SurveyBuild.degreeUploadAttachment(this,\'' + data.instanceId + '\',\'' + data.itemId + children[5].instanceId + '\',\'degree\')">';
						c += '		</div>';
						c += '	<div class="file-list-suffix" style="display:' + (SurveyBuild._readonly ? 'none' : 'block') + '">' + (data.suffixUrl != "" ? "<a href='" + data.suffixUrl + "'>" : "") + (data.suffix != "" ? data.suffix : "") + (data.suffixUrl != "" ? "</a>" : "") + '</div>';
						c += '		<div class="clear"></div>';
						c += '		<div>' + msg + '<div id="' + data.itemId + children[5].itemId + 'Tip" class="onShow" style="line-height:32px;height:18px;"><div class="onShow"></div></div></div>';
						c += '	</div>';
						c += '	<div class="input-list-info-blank left" style="display:' + (SurveyBuild._readonly ? 'none' : 'block') + '"><span class="red"></div>';
						c += '   	<div class="input-list-upload left">';
						c += '		<div class="input-list-upload-con" id="' + data.itemId + children[5].itemId + '_AttList" style="display:' + (children[5].children.length < 1 ? 'none' : 'black') + '">';
						if (children[5].allowMultiAtta == "Y") {
							for (var i = 0; i < children[5].children.length; i++) {
								_sysFilename = children[5].children[i].sysFileName;
								_fileSuffix = (_sysFilename.substring(_sysFilename.lastIndexOf(".") + 1)).toUpperCase();
								if (children[5].children[i].viewFileName != "" && children[5].children[i].sysFileName != "") {
									c += '<div class="input-list-uploadcon-list">';
									c += '	<div class="input-list-uploadcon-listl left">';
									c += '		<a class="input-list-uploadcon-list-a" onclick=SurveyBuild.downLoadFileForDegree(this,\'' + data.instanceId + '\',"' + data.itemId + children[5].instanceId + '",\'degree\') file-index = "' + children[5].children[i].orderby + '">' + children[5].children[i].viewFileName + '</a>';
									if (_fileSuffix == "PDF" && data.isOnlineShow == "Y") {
										c += '<div class="input-list-uploadcon-list-pdf" onclick="SurveyBuild.PDFpreviewForDegree(this,\'' + data.instanceId + '\',\'' + data.itemId + children[5].instanceId + '\',\'degree\')" file-index = "' + children[5].children[i].orderby + '">&nbsp;&nbsp;<img src="' + TzUniversityContextPath + '/statics/images/appeditor/preview.png" title="' + MsgSet["PDF_VIEW"] + '"/>&nbsp;</div>';
									}
									c += '	</div>';
									c += '<div class="input-list-uploadcon-listr left" style="display: ' + (SurveyBuild._readonly ? 'none' : 'block') + ';line-height:46px;" onclick="SurveyBuild.deleteFileForDegree(this,\'' + data.instanceId + '\',\'' + data.itemId + children[5].instanceId + '\',\'degree\')"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="' + MsgSet["DEL"] + '"/>&nbsp;</div>';
									c += '	<div class="clear"></div>';
									c += '</div>';
								}
							}
						} else {
							for (var i = 0; i < children[5].children.length; i++) {
								_sysFilename = children[5].children[i].sysFileName;
								_fileSuffix = (_sysFilename.substring(_sysFilename.lastIndexOf(".") + 1)).toUpperCase();
								if (children[5].children[i].viewFileName != "" && children[5].children[i].sysFileName != "") {
									c += '<div class="input-list-uploadcon-list">';
									c += '	<div class="input-list-uploadcon-listl left">';
									c += '		<a class="input-list-uploadcon-list-a" onclick=SurveyBuild.downLoadFileForDegree(this,"' + data.itemId + children[5].instanceId + '",\'degree\') file-index = "' + children[5].children[i].orderby + '">' + children[5].children[i].viewFileName + '</a>';
									if (_fileSuffix == "PDF" && data.isOnlineShow == "Y") {
										c += '<div class="input-list-uploadcon-list-pdf" onclick="SurveyBuild.PDFpreviewForDegree(this,\'' + data.instanceId + '\',\'' + data.itemId + children[5].instanceId + '\',\'degree\')" file-index = "' + children[5].children[i].orderby + '">&nbsp;&nbsp;<img src="' + TzUniversityContextPath + '/statics/images/appeditor/preview.png" title="' + MsgSet["PDF_VIEW"] + '"/>&nbsp;</div>';
									}
									c += '	</div>';
									c += '<div class="input-list-uploadcon-listr left" style="display: ' + (SurveyBuild._readonly ? 'none' : 'block') + ';line-height:46px;" onclick="SurveyBuild.deleteFile(this,\'' + data.itemId + children[5].instanceId + '\',\'degree\')"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="' + MsgSet["DEL"] + '"/>&nbsp;</div>';
									c += '	<div class="clear"></div>';
									c += '</div>';
								}
							}
						}

						c += '		</div>';
						c += '	</div>';
						c += '	<div class="input-list-suffix-blank left"></div>';
						c += '	<div class="clear"></div>';
						c += '</div>';
					}

				}else{
					//填写模式
					//学历、学历编码、上传学历证书，学位、学位编码、上传学位证书

					//1.学历
					e = '<option value="">' + MsgSet["PLEASE_SELECT"] + '</option>';
	               
					for (var i in data.diplomaoption) {
	                    e += '<option ' + (children[0]["value"] == data["diplomaoption"][i]["code"] ? "selected='selected'": "") + 'value="' + data["diplomaoption"][i]["code"] + '">' + data["diplomaoption"][i]["txt"] + '</option>';
	                }
					c += '<div class="input-list">';
					c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + children[0].title + '</div>';
					c += '    <div class="input-list-text left input-edu-select">';
	                c += '          <select name="' +data["itemId"]+ children[0].itemId + '" class="chosen-select" id="' +data["itemId"]+ children[0].itemId + '" style="width:100%;">';
	                c +=                    e;
	                c += '          </select>';
					c += '    </div>';
					
					c += '  <div class="input-list-suffix left">  <div id="' +data["itemId"]+ children[0].itemId + 'Tip" class="onShow"><div class="onShow"></div></div></div>';
					c += '    <div class="clear"></div>';
					c += '</div>';


					//2学历编码
//					if (children[0].value!="5" && children[0].value!="") {
//						c += '<div class="input-list" id="'+data.itemId +children[1].itemId+'Show">';
//					} else {
//						c += '<div class="input-list" style="display:none" id="'+data.itemId +children[1].itemId+'Show">';
//					}
//					if(children[0]["value"]!="5" && children[0]["value"]!="" && data.isRequire == "Y"){
//						c += '	<div class="input-list-info left"><span class="red-star" id ="'+data.itemId +children[1].itemId+'span"> * </span>' + children[1].title + '</div>';
//					}else{
//						c += '	<div class="input-list-info left"><span class="red-star" id ="'+data.itemId +children[1].itemId+'span"> </span>' + children[1].title + '</div>';
//					}
//					
//					c += '  <div class="input-list-text left"><input  type="text" class="inpu-list-text-enter" id="' + data.itemId +children[1].itemId + '" name="' + data.itemId +children[1].itemId + '" value="' + children[1].value + '"/></div>';
//					c += '  <div class="input-list-suffix left"> <div id="' + data.itemId +children[1].itemId + 'Tip" class="onShow"><div class="onShow"></div></div></div>';
//					//c += '  <div id="' + data.itemId +children[1].itemId + 'Tip" class="onShow"><div class="onShow"></div></div>';
//					c += '  <div class="clear"></div>';
//					c += '</div>';


					//3 学历上传附件
					if (children[0].value!="5" && children[0].value!="") {
						c += '<div class="input-list-blank margart15" id="upload_' + data.itemId + children[2].itemId + '">';
					} else {
						c += '<div class="input-list-blank margart15" id="upload_' + data.itemId + children[2].itemId + '" style="display:none">';
					}
					if(data.isRequire == "Y"){
						c += '	<div class="input-list-info left"><span class="red-star" id ="'+data.itemId +children[2].itemId+'span"> * </span>' + children[2].title + '</div>';
					}else{
						c += '	<div class="input-list-info left"><span class="red-star" id ="'+data.itemId +children[2].itemId+'span"> </span>' + children[2].title + '</div>';
					}
					c += '   	<div class="input-list-texttemplate left" style="display:' + (SurveyBuild._readonly?'none':'block') + '">';
					c += '		<div>' + data.onShowMessage + '</div>';
					c += '		<div class="filebtn left">';
					c += '			<div class="filebtn-org"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/upload.png" />&nbsp;&nbsp;' + MsgSet["UPLOAD_BTN_MSG2"] + '</div>';
					c += '			<input type="file" class="filebtn-orgtext" data-instancid = "' +data.itemId+ children[2].instanceId + '" id = "' + data.itemId + children[2].itemId + '" name = "' + data.itemId + children[2].itemId + '" title="' + data.itemId + children[2].itemName + '" onchange="SurveyBuild.degreeUploadAttachment(this,\''+ data.instanceId +'\',\'' + data.itemId+ children[2].instanceId + '\',\'diploma\')">';
					c += '		</div>';
					c += '	<div class="file-list-suffix" style="display:' + (SurveyBuild._readonly?'none':'block') + '">' + (data.suffixUrl != "" ? "<a href='" + data.suffixUrl + "'>" : "") + (data.suffix != "" ? data.suffix : "") + (data.suffixUrl != "" ? "</a>" : "") +'</div>';
					c += '		<div class="clear"></div>';
					c += '		<div>' + msg + '<div id="' + data.itemId + children[2].itemId+ 'Tip" class="onShow" style="line-height:32px;height:18px;"><div class="onShow"></div></div></div>';
					c += '	</div>';
					c += '	<div class="input-list-info-blank left" style="display:' + (SurveyBuild._readonly?'none':'block') + '"><span class="red"></div>';
					c += '   	<div class="input-list-upload left">';
					c += '		<div class="input-list-upload-con" id="'+data.itemId + children[2].itemId+'_AttList" style="display:' + (children[2].children.length < 1 ? 'none':'black') + '">';
					if(children[2].allowMultiAtta == "Y"){
						for(var i = 0; i < children[2].children.length; i++){
							_sysFilename = children[2].children[i].sysFileName;
							_fileSuffix = (_sysFilename.substring(_sysFilename.lastIndexOf(".") + 1)).toUpperCase();
							if (children[2].children[i].viewFileName != "" && children[2].children[i].sysFileName != ""){
								c += '<div class="input-list-uploadcon-list">';
								c += '	<div class="input-list-uploadcon-listl left">';
								c += '		<a class="input-list-uploadcon-list-a" onclick=SurveyBuild.downLoadFileForDegree(this,\''+ data.instanceId +'\',"' + data.itemId+ children[2].instanceId + '",\'diploma\') file-index = "' + children[2].children[i].orderby + '">' + children[2].children[i].viewFileName + '</a>';
								if(_fileSuffix == "PDF" && data.isOnlineShow == "Y"){
									c += '<div class="input-list-uploadcon-list-pdf" onclick="SurveyBuild.PDFpreviewForDegree(this,\''+ data.instanceId +'\',\'' + data.itemId+ children[2].instanceId + '\',\'diploma\')" file-index = "' + children[2].children[i].orderby + '">&nbsp;&nbsp;<img src="' + TzUniversityContextPath + '/statics/images/appeditor/preview.png" title="' + MsgSet["PDF_VIEW"] + '"/>&nbsp;</div>';
								}
								c +='	</div>';
								c += '<div class="input-list-uploadcon-listr left" style="display: ' + (SurveyBuild._readonly?'none':'block') + ';line-height:46px;" onclick="SurveyBuild.deleteFileForDegree(this,\''+ data.instanceId +'\',\'' + data.itemId+ children[2].instanceId + '\',\'diploma\')"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="' + MsgSet["DEL"] + '"/>&nbsp;</div>';
								c += '	<div class="clear"></div>';
								c += '</div>';
							}
						}
					}else{
						for(var i = 0; i < children[2].children.length; i++){
							_sysFilename = children[2].children[i].sysFileName;
							_fileSuffix = (_sysFilename.substring(_sysFilename.lastIndexOf(".") + 1)).toUpperCase();
							if (children[2].children[i].viewFileName != "" && children[2].children[i].sysFileName != ""){
								c += '<div class="input-list-uploadcon-list">';
								c += '	<div class="input-list-uploadcon-listl left">';
								c += '		<a class="input-list-uploadcon-list-a" onclick=SurveyBuild.downLoadFileForDegree(this,"' + data.itemId+ children[2].instanceId + '",\'diploma\') file-index = "' + children[2].children[i].orderby + '">' + children[2].children[i].viewFileName + '</a>';
								if(_fileSuffix == "PDF" && data.isOnlineShow == "Y"){
									c += '<div class="input-list-uploadcon-list-pdf" onclick="SurveyBuild.PDFpreviewForDegree(this,\''+ data.instanceId +'\',\'' + data.itemId+ children[2].instanceId + '\',\'diploma\')" file-index = "' + children[2].children[i].orderby + '">&nbsp;&nbsp;<img src="' + TzUniversityContextPath + '/statics/images/appeditor/preview.png" title="' + MsgSet["PDF_VIEW"] + '"/>&nbsp;</div>';
								}
								c +='	</div>';
								c += '<div class="input-list-uploadcon-listr left" style="display: ' + (SurveyBuild._readonly?'none':'block') + ';line-height:46px;" onclick="SurveyBuild.deleteFileForDegree(this,\'' + FinstanceId + '\',\'' + instanceId + '\',\'diploma\')"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="' + MsgSet["DEL"] + '"/>&nbsp;</div>';
								c += '	<div class="clear"></div>';
								c += '</div>';
							}
						}
					}

					c += '		</div>';
					c += '	</div>';
					c += '	<div class="input-list-suffix-blank left"></div>';
					c += '	<div class="clear"></div>';
					c += '</div>';

					//3.学位
					e = '<option value="">' + MsgSet["PLEASE_SELECT"] + '</option>';
		               
					for (var i in data.degreeoption) {
						e += '<option ' + (children[3]["value"] == data["degreeoption"][i]["code"] ? "selected='selected'": "") + 'value="' + data["degreeoption"][i]["code"] + '">' + data["degreeoption"][i]["txt"] + '</option>';
					}
					c += '<div class="input-list">';
					
					c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + ' </span>' + children[3].title + '</div>';
					c += '    <div class="input-list-text left input-edu-select">';
					c += '          <select name="' +data["itemId"]+ children[3].itemId + '" class="chosen-select" id="' +data["itemId"]+ children[3].itemId + '" style="width:100%;">';
					c +=                    e;
					c += '          </select>';
					c += '    </div>';
					c += '  <div class="input-list-suffix left">    <div id="' +data["itemId"]+ children[3].itemId + 'Tip" class="onShow"><div class="onShow"></div></div></div>';
					c += '    <div class="clear"></div>';
					c += '</div>';

					//4学位编码
//					if (children[3].value!="4" && children[3].value!="") {
//						c += '<div class="input-list" id="'+data.itemId +children[4].itemId+'Show">';
//					} else {
//						c += '<div class="input-list" style="display:none" id="'+data.itemId +children[4].itemId+'Show">';
//					}
//					if(children[3].value!="4" && children[3].value!=""&&data.isRequire == "Y"){
//						c += '	<div class="input-list-info left"><span class="red-star" id ="'+data.itemId +children[4].itemId+'span">*</span>' + children[4].title + '</div>';
//					}else{
//						c += '	<div class="input-list-info left"><span class="red-star" id ="'+data.itemId +children[4].itemId+'span"></span>' + children[4].title + '</div>';
//					}
//					c += '  <div class="input-list-text left"><input  type="text" class="inpu-list-text-enter" id="' + data.itemId +children[4].itemId + '" name="' + data.itemId +children[4].itemId + '" value="' + children[4].value + '"/></div>';
//					c += '  <div class="input-list-suffix left"> <div id="' + data.itemId +children[4].itemId + 'Tip" class="onShow"><div class="onShow"></div></div></div>';
//					//c += '  <div id="' + data.itemId +children[4].itemId + 'Tip" class="onShow"><div class="onShow"></div></div>';
//					c += '  <div class="clear"></div>';
//					c += '</div>';
					 
					 //上传附件
					if (children[3]["value"]!="4" && children[3]["value"]!="") {
						c += '<div class="input-list-blank margart15" id="upload_' + data.itemId + children[5].itemId + '">';
					} else{
						c += '<div class="input-list-blank margart15" style="display:none" id="upload_' + data.itemId + children[5].itemId + '">';
					}
					if(data.isRequire == "Y"){
						c += '	<div class="input-list-info left"><span class="red-star" id ="'+data.itemId +children[5].itemId+'span"> * </span>' + children[5].title + '</div>';
					}else{
						c += '	<div class="input-list-info left"><span class="red-star" id ="'+data.itemId +children[5].itemId+'span"> </span>' + children[5].title + '</div>';
					}
					
					c += '   	<div class="input-list-texttemplate left" style="display:' + (SurveyBuild._readonly?'none':'block') + '">';
					c += '		<div>' + data.onShowMessage + '</div>';
					c += '		<div class="filebtn left">';
					c += '			<div class="filebtn-org"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/upload.png" />&nbsp;&nbsp;' + MsgSet["UPLOAD_BTN_MSG2"] + '</div>';
					c += '			<input type="file" class="filebtn-orgtext" data-instancid = "' +data.itemId+ children[5].instanceId + '" id = "' + data.itemId + children[5].itemId + '" name = "' + data.itemId + children[5].itemId + '" title="' + data.itemId + children[5].itemName + '" onchange="SurveyBuild.degreeUploadAttachment(this,\''+ data.instanceId +'\',\'' + data.itemId+ children[5].instanceId + '\',\'degree\')">';
					c += '		</div>';
					c += '	<div class="file-list-suffix" style="display:' + (SurveyBuild._readonly?'none':'block') + '">' + (data.suffixUrl != "" ? "<a href='" + data.suffixUrl + "'>" : "") + (data.suffix != "" ? data.suffix : "") + (data.suffixUrl != "" ? "</a>" : "") +'</div>';
					c += '		<div class="clear"></div>';
					c += '		<div>' + msg2 + '<div id="' + data.itemId + children[5].itemId+ 'Tip" class="onShow" style="line-height:32px;height:18px;"><div class="onShow"></div></div></div>';
					c += '	</div>';
					c += '	<div class="input-list-info-blank left" style="display:' + (SurveyBuild._readonly?'none':'block') + '"><span class="red"></div>';
					c += '   	<div class="input-list-upload left">';
					c += '		<div class="input-list-upload-con" id="'+data.itemId + children[5].itemId+'_AttList" style="display:' + (children[5].children.length < 1 ? 'none':'black') + '">';
					if(children[5].allowMultiAtta == "Y"){
						for(var i = 0; i < children[5].children.length; i++){
							_sysFilename = children[5].children[i].sysFileName;
							_fileSuffix = (_sysFilename.substring(_sysFilename.lastIndexOf(".") + 1)).toUpperCase();
							if (children[5].children[i].viewFileName != "" && children[5].children[i].sysFileName != ""){
								c += '<div class="input-list-uploadcon-list">';
								c += '	<div class="input-list-uploadcon-listl left">';
								c += '		<a class="input-list-uploadcon-list-a" onclick=SurveyBuild.downLoadFileForDegree(this,\''+ data.instanceId +'\',"' + data.itemId+ children[5].instanceId + '",\'degree\') file-index = "' + children[5].children[i].orderby + '">' + children[5].children[i].viewFileName + '</a>';
								if(_fileSuffix == "PDF" && data.isOnlineShow == "Y"){
									c += '<div class="input-list-uploadcon-list-pdf" onclick="SurveyBuild.PDFpreviewForDegree(this,\''+ data.instanceId +'\',\'' + data.itemId+ children[5].instanceId + '\',\'degree\')" file-index = "' + children[5].children[i].orderby + '">&nbsp;&nbsp;<img src="' + TzUniversityContextPath + '/statics/images/appeditor/preview.png" title="' + MsgSet["PDF_VIEW"] + '"/>&nbsp;</div>';
								}
								c +='	</div>';
								c += '<div class="input-list-uploadcon-listr left" style="display: ' + (SurveyBuild._readonly?'none':'block') + ';line-height:46px;" onclick="SurveyBuild.deleteFileForDegree(this,\''+ data.instanceId +'\',\'' + data.itemId+ children[5].instanceId + '\',\'degree\')"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="' + MsgSet["DEL"] + '"/>&nbsp;</div>';
								c += '	<div class="clear"></div>';
								c += '</div>';
							}
						}
					}else{
						for(var i = 0; i < children[5].children.length; i++){
							_sysFilename = children[5].children[i].sysFileName;
							_fileSuffix = (_sysFilename.substring(_sysFilename.lastIndexOf(".") + 1)).toUpperCase();
							if (children[5].children[i].viewFileName != "" && children[5].children[i].sysFileName != ""){
								c += '<div class="input-list-uploadcon-list">';
								c += '	<div class="input-list-uploadcon-listl left">';
								c += '		<a class="input-list-uploadcon-list-a" onclick=SurveyBuild.downLoadFileForDegree(this,"' + data.itemId+ children[5].instanceId + '",\'degree\') file-index = "' + children[5].children[i].orderby + '">' + children[5].children[i].viewFileName + '</a>';
								if(_fileSuffix == "PDF" && data.isOnlineShow == "Y"){
									c += '<div class="input-list-uploadcon-list-pdf" onclick="SurveyBuild.PDFpreviewForDegree(this,\''+ data.instanceId +'\',\'' + data.itemId+ children[5].instanceId + '\',\'degree\')" file-index = "' + children[5].children[i].orderby + '">&nbsp;&nbsp;<img src="' + TzUniversityContextPath + '/statics/images/appeditor/preview.png" title="' + MsgSet["PDF_VIEW"] + '"/>&nbsp;</div>';
								}
								c +='	</div>';
								c += '<div class="input-list-uploadcon-listr left" style="display: ' + (SurveyBuild._readonly?'none':'block') + ';line-height:46px;" onclick="SurveyBuild.deleteFileForDegree(this,\'' + FinstanceId + '\',\'' + instanceId + '\',\'degree\')"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="' + MsgSet["DEL"] + '"/>&nbsp;</div>';
								c += '	<div class="clear"></div>';
								c += '</div>';
							}
						}
					}

					c += '		</div>';
					c += '	</div>';
					c += '	<div class="input-list-suffix-blank left"></div>';
					c += '	<div class="clear"></div>';
					c += '</div>';
				} 
				
			}
		} else {
			//报名表配置页面
			var typeLi = '';
			//学位
			typeLi += '<div class="type_item_li">';
			typeLi += '	<span class="type_item_label"><span class="red-star">'+MsgSet["DEGREE_LEVEL"]+'：</span>';
			typeLi += '		<b class="read-select" style="min-width:120px;">'+MsgSet["PLEASE_SELECT"]+'</b>';
			typeLi += '	</div>';

			//学历
			typeLi += '<div class="type_item_li">';
			typeLi += '	<span class="type_item_label"><span class="red-star">'+MsgSet["DIPLOMA_LEVEL"]+'：</span>';
			typeLi += '		<b class="read-select" style="min-width:120px;">'+MsgSet["PLEASE_SELECT"]+'</b>';
			typeLi += '	</div>';
			
			c += '<div class="question-answer"  style="border:1px solid #ddd;padding:10px;">';
			c += '	<div class="DHContainer">' + typeLi + '</div>';
			c += '</div>';
		}
		return c;
	},
	
	_edit: function(data) {
		var e = '';

 		//规则设置
		e += '<div class="edit_jygz">';
		e += '	<span class="title"><i class="icon-cog"></i> 校验规则：</span>';
		e += '  <div class="groupbox">';
		e += '	<div class="edit_item_warp" style="margin-top:5px;">';
		e += '		<input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填</label>&nbsp;&nbsp';
		e += '	</div>';
		e += '	</div>';
		
		e += '  <div class="edit_item_warp">';
		e += '      <a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
		e += '  </div>';
		e +='</div>';
		return e;
	},	
	//学历编码校验
	checkDegreeCode:function(inputPEl,inputEl){
		inputEl.formValidator({tipID: (inputEl.attr("id") + 'Tip'),onShow: "",onFocus: "&nbsp;",onCorrect: "&nbsp;"});
		inputEl.functionValidator({
			fun:function(val,el){
				if(val==""){
					return "此项必填";
				}else{
					return true;
				}
			}	
        });
	},
	checkDegreeFile:function(inputPEl,inputEl){
		var id = inputEl.attr("id");
		var filelist = id+"__AttList";
		//附件验证
		inputEl.formValidator({tipID: (inputEl.attr("id")+'Tip'), onShow:"", onFocus:"&nbsp;", onCorrect:"&nbsp;"});
		inputEl.functionValidator({
            fun:function(val,el){
                if ($("#"+filelist +" .fileLi").length > 0){
                    return 	true;
                } else {
                    return MsgSet["FILE_UPL_REQUIRE"];
                }
            }
        });
	},
	_eventbind: function(data) {
		var children = data.children;
		
		
		//学历验证
		var $inputBox = $("#" + data.itemId+children[0].itemId);
		
		//学位验证
		var $inputBox2 = $("#" + data.itemId+children[3].itemId);
		
		
		$inputBox.on("change",function(){
            var selectIndex = $inputBox[0].selectedIndex;
            if($inputBox[0].options[selectIndex].value){
            	children[0].wzsm = $inputBox[0].options[selectIndex].text;
                if ($inputBox[0].options[selectIndex].value=="5") {
                    $("#" + data.itemId+children[1].itemId+"Show").hide();
                    $("#"+data.itemId+children[1].itemId+"Prompt").hide();
                    $("#upload_" + data.itemId+children[2].itemId).hide(); 
                    if(SurveyBuild.accessType == "M"){
                    	$("#" + data.itemId+children[2].itemId+"_AttList").hide();
                    }
                } else {
                	
                	
                    $("#" + data.itemId+children[1].itemId+"Show").show();
                    $("#upload_" + data.itemId+children[2].itemId).show();
                    $("#"+data.itemId+children[1].itemId+"Prompt").show();
                    $("#"+data.itemId +children[1].itemId+"span").html();
//                    $("#"+data.itemId +children[1].itemId+"span").html("*");
                    $("#"+data.itemId +children[2].itemId+"span").html();
//                    $("#"+data.itemId +children[2].itemId+"span").html("*");
                    if(data.isRequire=="Y"){
                    	//校验学历编码
                    	//var $codeInput = $("#" + data.itemId+children[1].itemId);
                    	//data.checkDegreeCode($inputBox,$codeInput);
                    	//校验学历附件
//                    	var $fileInput = $("#" + data.itemId+children[2].itemId);
//                    	data.checkDegreeFile($inputBox,$fileInput);
                    }
                    
                    if(SurveyBuild.accessType == "M"){     
                    	//console.log($("#" + data.itemId+children[2].itemId+"_AttList").children(".fileLi").length);
                    	if ($("#" + data.itemId+children[2].itemId+"_AttList").children(".fileLi").length>0) {
                    		$("#" + data.itemId+children[2].itemId+"_AttList").show();
                    	}
                    }
                   
                }
            	
            }else{
            	children[0].wzsm = "";
            }
        });
		
		$inputBox2.on("change",function(){
            var selectIndex = $inputBox2[0].selectedIndex;
            if($inputBox2[0].options[selectIndex].value){
            	children[3].wzsm = $inputBox2[0].options[selectIndex].text;
                if ($inputBox2[0].options[selectIndex].value=="4") {
                    $("#" + data.itemId+children[4].itemId+"Show").hide();
                    $("#upload_" + data.itemId+children[5].itemId).hide();
                    if(SurveyBuild.accessType == "M"){
                    	$("#" + data.itemId+children[5].itemId+"_AttList").hide();
                    }
                } else {
                    $("#" + data.itemId+children[4].itemId+"Show").show();
                    $("#upload_" + data.itemId+children[5].itemId).show();
                    $("#"+data.itemId +children[4].itemId+"span").html();
//                    $("#"+data.itemId +children[4].itemId+"span").html("*");
                    $("#"+data.itemId +children[5].itemId+"span").html();
//                    $("#"+data.itemId +children[5].itemId+"span").html("*");
                    if(data.isRequire=="Y"){
                    	//校验学位编码
//                    	var $codeInput = $("#" + data.itemId+children[4].itemId);
//                    	data.checkDegreeCode($inputBox,$codeInput);
                    	//校验学位附件
//                    	var $fileInput = $("#" + data.itemId+children[5].itemId);
//                    	data.checkDegreeFile($inputBox,$fileInput);
                    }
                    if(SurveyBuild.accessType == "M"){
                    	if ($("#" + data.itemId+children[5].itemId+"_AttList").children(".fileLi").length>0) {
                    		$("#" + data.itemId+children[5].itemId+"_AttList").show();
                    	}
                    }
                }
            }else{
            	children[3].wzsm = "";
            }
        });
		
		var $fileInput = $("#" + data.itemId+children[2].itemId);
		var $uplBtn = $fileInput.prev(".bt_blue");


        var $fileInput2 = $("#" + data.itemId+children[5].itemId);
        var $uplBtn2 = $fileInput2.prev(".bt_blue");

		$fileInput.mouseover(function(e){
			$uplBtn.css("opacity","0.8");	
		});
		
		$fileInput.mouseout(function(e) {
            $uplBtn.css("opacity","1");
        });

        $fileInput2.mouseover(function(e){
            $uplBtn2.css("opacity","0.8");
        });

        $fileInput2.mouseout(function(e) {
            $uplBtn2.css("opacity","1");
        });


        var $codeInput = $("#" + data.itemId+children[1].itemId);
        var $codeInput2 = $("#" + data.itemId+children[4].itemId);


		 //所有select非空验证
		if(data.isRequire == "Y"){


		    //1.学历 校验
			$inputBox.formValidator({tipID:data["itemId"] + children[0]["itemId"] +'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$inputBox.functionValidator({
				fun: function(val,elem) {
					//执行高级设置中的自定义规则
					var _result = true;
					if (ValidationRules) {
						$.each(data["rules"],function(classname, classObj) {
							if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
								var _ruleClass = ValidationRules[classname];
								if (_ruleClass && _ruleClass._Validator) {
									_result = _ruleClass._Validator(data["itemId"] + children[0]["itemId"], classObj["messages"], data);
									if(_result !== true){
										return false;
									}
								}
							}
						});
						if(_result !== true){
							return _result;
						}
					}
				}
			});
			if(children[0]["value"]!="5" &&children[0]["value"]!=""){
				 //2. 学历编码 校验
//	            $codeInput.formValidator({tipID: (data["itemId"] + children[1]["itemId"] + 'Tip'),onShow: "",onFocus: "&nbsp;",onCorrect: "&nbsp;"});
//	            $codeInput.functionValidator({
//	                fun: function(val, elem) {
//	                    //执行高级设置中的自定义规则
//	                    /*********************************************\
//	                     ** 注意：自定义规则中不要使用formValidator **
//	                     \*********************************************/
//	                    var _result = true;
//	                    if (ValidationRules && $inputBox.val()!="5") {
//	                        $.each(data["rules"],
//	                            function(classname, classObj) {
//	                                //单	选钮不需要在高级规则中的必选判断
//	                                if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
//	                                    var _ruleClass = ValidationRules[classname];
//	                                    if (_ruleClass && _ruleClass._Validator) {
//	                                        _result = _ruleClass._Validator(data["itemId"] + children[1]["itemId"], classObj["messages"]);
//	                                        if (_result !== true) {
//	                                            return false;
//	                                        }
//	                                    }
//	                                }
//	                            });
//	                        if (_result !== true) {
//	                            return _result;
//	                        }
//	                    }
//	                    return _result;
//	                }
//	            });


	            //3.学历附件验证
	            $fileInput.formValidator({tipID:(data["itemId"]+children[2].itemId+'Tip'), onShow:"", onFocus:"&nbsp;", onCorrect:"&nbsp;"});
	            $fileInput.functionValidator({
	                fun:function(val,el){
	                    if (children[2].children.length > 1){
	                        return 	true;
	                    } else if (children[2].children.length == 1 && children[2].children[0].fileName != ""){
	                        return true;
	                    } else {
	                        return MsgSet["FILE_UPL_REQUIRE"];
	                    }
	                }
	            });

	            $fileInput.functionValidator({
	                fun:function(val,elem){
	                    //执行高级设置中的自定义规则
	                    var _result = true;
	                    if (ValidationRules && $inputBox.val()!="5") {
	                        $.each(data["rules"],function(classname, classObj) {
	                            //附件上传不需要在高级规则中的必选判断
	                            if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y" && classname!="RequireValidator") {
	                                var _ruleClass = ValidationRules[classname];
	                                if (_ruleClass && _ruleClass._Validator) {
	                                    _result = _ruleClass._Validator(data["itemId"]+ children[2]["itemId"], classObj["messages"]);
	                                    if(_result !== true){
	                                        return false;
	                                    }
	                                }
	                            }
	                        });
	                        if(_result !== true){
	                            return _result;
	                        }
	                    }
	                    return _result;
	                }
	            });

			}
           
            //4.学位校验
			$inputBox2.formValidator({tipID:data["itemId"] + children[3]["itemId"] +'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$inputBox2.functionValidator({
				fun: function(val,elem) {
					
					//执行高级设置中的自定义规则
					var _result = true;
					if (ValidationRules) {
						$.each(data["rules"],function(classname, classObj) {
							if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
								var _ruleClass = ValidationRules[classname];
								if (_ruleClass && _ruleClass._Validator) {
									_result = _ruleClass._Validator(data["itemId"] + children[3]["itemId"], classObj["messages"], data);
									if(_result !== true){
										return false;
									}
								}
							}
						});
						if(_result !== true){
							return _result;
						}
					}
				}
			});

			if (children[3]["value"]!="4" &&children[3]["value"]!=""){
//	            //5. 学位编码 校验
//	            $codeInput2.formValidator({tipID: (data["itemId"] + children[4]["itemId"] + 'Tip'),onShow: "",onFocus: "&nbsp;",onCorrect: "&nbsp;"});
//	            $codeInput2.functionValidator({
//	                fun: function(val, elem) {
//	                    //执行高级设置中的自定义规则
//	                    /*********************************************\
//	                     ** 注意：自定义规则中不要使用formValidator **
//	                     \*********************************************/
//	                    var _result = true;
//	                    if (ValidationRules && $inputBox2.val()!="4") {
//	                        $.each(data["rules"],
//	                            function(classname, classObj) {
//	                                //单	选钮不需要在高级规则中的必选判断
//	                                if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
//	                                    var _ruleClass = ValidationRules[classname];
//	                                    if (_ruleClass && _ruleClass._Validator) {
//	                                        _result = _ruleClass._Validator(data["itemId"] + children[4]["itemId"], classObj["messages"]);
//	                                        if (_result !== true) {
//	                                            return false;
//	                                        }
//	                                    }
//	                                }
//	                            });
//	                        if (_result !== true) {
//	                            return _result;
//	                        }
//	                    }
//	                    return _result;
//	                }
//	            });


	            //6.学位附件验证
	            $fileInput2.formValidator({tipID:(data["itemId"]+children[5].itemId+'Tip'), onShow:"", onFocus:"&nbsp;", onCorrect:"&nbsp;"});
	            $fileInput2.functionValidator({
	                fun:function(val,el){
	                    if (children[5].children.length > 1){
	                        return 	true;
	                    } else if (children[5].children.length == 1 && children[5].children[0].fileName != ""){
	                        return true;
	                    } else {
	                        return MsgSet["FILE_UPL_REQUIRE"];
	                    }
	                }
	            });

	            $fileInput2.functionValidator({
	                fun:function(val,elem){
	                    //执行高级设置中的自定义规则
	                    var _result = true;
	                    if (ValidationRules && $inputBox2.val()!="4") {
	                        $.each(data["rules"],function(classname, classObj) {
	                            //附件上传不需要在高级规则中的必选判断
	                            if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y" && classname!="RequireValidator") {
	                                var _ruleClass = ValidationRules[classname];
	                                if (_ruleClass && _ruleClass._Validator) {
	                                    _result = _ruleClass._Validator(data["itemId"]+ children[5]["itemId"], classObj["messages"]);
	                                    if(_result !== true){
	                                        return false;
	                                    }
	                                }
	                            }
	                        });
	                        if(_result !== true){
	                            return _result;
	                        }
	                    }
	                    return _result;
	                }
	            });
			}

		} 
	}
});