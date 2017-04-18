/*====================================================================
+ 功能描述：班级控件，用于选择班级									++
+ 开发人：曹阳														++
+ 开发日期：2017-01-16												++
=====================================================================*/
SurveyBuild.extend("ChooseClass", "baseComponent", {
	itemName: "项目选择",
	title:"项目选择",
	isDoubleLine:"Y",
	isSingleLine: "Y",
	fixedContainer:"Y",// 固定容器标识
	children:{
		"bmrClass": {
			"instanceId": "bmrClass",
			"itemId": "CC_Project",
			"itemName": MsgSet["CHOOSE_PRJ"],
			"title": MsgSet["CHOOSE_PRJ"],
			"orderby": 1,
			"value": "",
			"StorageType": "S",
			"wzsm": "",
			"classname":"baseComponent"
		},
		"bmrBatch": {
			"instanceId": "bmrBatch",
			"itemId": "CC_Batch",
			"itemName": MsgSet["INTERVIEW_BATCH"],
			"title": MsgSet["INTERVIEW_BATCH"],
			"orderby": 2,
			"value": "",
			"StorageType": "S",
			"option": {},
			"wzsm" : "",
			"classname":"SingleTextBox"
		}
	},
	minLines:"1",
	maxLines:"1",
	_getHtml : function(data,previewmode){
		 // 预览模式
		 var c = '',e='',params='',desc = '';
		 //console.log("data:"+data);
		 ////console.dir(data);
		 //console.log("previewmode:"+previewmode);
		 //console.log("_readonly:"+SurveyBuild._readonly);
		 ////console.dir(data);
		
		 
	     if(previewmode){
	    	 var child=data["children"][0];
	    	 
	    	 if (child == undefined) {
	    		 child=data["children"];
	    	 }
			// console.log("child:"+child);
			 ////console.dir(child);
			 var val=child.bmrClass.value;
			 var classid = $("#ClassId").val();
			 var batchId = $("#BatchId").val();
			 
			// console.log("classid:"+classid);
	    	 if($("#ClassId").length > 0){
	    		 // var classid = $("#ClassId").val();
	             params = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_OTHER_STD","OperateType":"EJSON","comParams":{"OType":"CLASSINFO","CLASSID":' + classid + '}}';
	                $.ajax({
	                    type: "get",
	                    dataType: "JSON",
	                    data:{
	                        tzParams:params
	                    },
	                    async:false,
	                    url:SurveyBuild.tzGeneralURL,
	                    success: function(f) {
	                        if(f.state.errcode == "0"){
	                            val = f.comContent.classCode;
	                            desc = f.comContent.className;
	                            child.bmrClass.value = val;
	                            child.bmrClass.wzsm = desc;
	                        }
	                    }
	                });
	            }
	            
	    	 e += '<div class="input-list">';
	    	 e += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + child.bmrClass.title + '</div>';
	    	 
	    	 //console.log("wzsm:"+child.bmrClass.wzsm);
	    	 //console.log("bmrClass:"+child.bmrClass.value);
	    	// //console.dir("wzsm:"+child.bmrClass);
	    	 
	    	 if(SurveyBuild._readonly || $("#ClassId").length <= 0){
	    		 e += '	<div class="input-list-text left">' + child.bmrClass.wzsm + '</div>';
	    	 } else {
	    		 e += '	<div class="input-list-text left" ><span id="'+data["itemId"]+child.bmrClass.itemId+'_SPAN">' + child.bmrClass.wzsm + '</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" id="'+data["itemId"]+child.bmrClass.itemId+'_Btnselect">'+MsgSet["CHAGE_CLASS"]+'</a></div>';
	    	 }
	    	 
	    	 e += '	<div class="input-list-suffix left"></div>';
	    	 e += '	<div class="clear"></div>';
	    	 e += '</div>';
	    	 
	    	 e += '<input id="' +data["itemId"]+child.bmrClass.itemId + '" type="hidden" name="' + child.bmrClass.itemId + '" value="' + child.bmrClass.value + '">';
	            
	         if(SurveyBuild._readonly){
	                // 只读模式
	        	 // child.bmrBatch.isRequire="Y";
	        	 e += '<div class="input-list">';
	        	 e += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire  == "Y" ? "*": "") + '</span>' + child.bmrBatch.title + '</div>';
	        	 e += '  <div class="input-list-text left">' + child.bmrBatch.wzsm + '</div>';
	        	 e += '  <div class="input-list-suffix left"></div>';
	        	 e += '  <div class="clear"></div>';
	        	 e += '</div>';
	            }else {
	                // 编辑模式
	                var params = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_OTHER_STD","OperateType":"EJSON","comParams":{"OType":"BATCH","CLASSID":"' + classid + '"}}';
	                $.ajax({
	                    type: "get",
	                    dataType: "JSON",
	                    data: {
	                        tzParams: params
	                    },
	                    async: false,
	                    url: SurveyBuild.tzGeneralURL,
	                    success: function (f) {
	                        if (f.state.errcode == "0") {
	                        	child.bmrBatch.option = f.comContent;
	                        }
	                    }
	                });
	                
	                //var  lensss = child.bmrBatch.["option"].length;
	                //console.dir(child.bmrBatch);
	                //console.log(lensss);
	                //console.log("itemId:"+child.bmrBatch.itemId);
	                //console.log("title:"+child.bmrBatch.title);
	                //console.log("wzsm:"+child.bmrBatch.wzsm);
	                var op='';
	                op += '<option value="">' + MsgSet["PLEASE_SELECT"] + '</option>';
	                
	                //var index=0;
	                //for (var i in child.bmrBatch.option) {
	                //	console.log("i="+index);
	                //	for (var y in child.bmrBatch.option) {
	                //		console.log("y="+child.bmrBatch["option"][y]["index"]);
	                //		if (child.bmrBatch["option"][y]["index"] ==  index.toString()) {
	                //			console.log("OK");
	                //			op+= '<option ' + (child.bmrBatch.value == child.bmrBatch["option"][y]["code"] ? "selected='selected'": "") + 'value="' + child.bmrBatch["option"][y]["code"] + '">' + child.bmrBatch["option"][y]["txt"] + '</option>';
	                //			
	                //		}
	                //	}	
	                //	index =index+1;
	                //}
	                
	                //排序
	                for (var i in child.bmrBatch.option) {
	                		op+= '<option ' + (child.bmrBatch.value == child.bmrBatch["option"][i]["code"] ? "selected='selected'": "") + 'value="' + child.bmrBatch["option"][i]["code"] + '">' + child.bmrBatch["option"][i]["txt"] + '</option>';
	                }
	                e += '<div class="input-list">';
	                e += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + child.bmrBatch.title + '</div>';
	                e += '    <div class="input-list-text left input-edu-select">';
	                e += '          <select name="' + child.bmrBatch.itemId + '" class="chosen-select" id="' + data["itemId"]+child.bmrBatch.itemId + '" style="width:100%;" title="' + child.bmrBatch.itemName + '">';
	                e +=                    op;
	                e += '          </select>';
	                e += '    </div>';
	                e += '    <div class="input-list-suffix left"><div id="' +data["itemId"]+child.bmrBatch.itemId + 'Tip" class="onShow"><div class="onShow"></div></div></div>';
	                e += '    <div class="clear"></div>';
	                e += '</div>'; 
	            } 
	            
	         
				c += '<div class="main_inner_content_top"></div>';
				c += '<div class="main_inner_content">';
				c += e;
				c += '</div>';
				c += '<div class="main_inner_content_foot"></div>';
	        }else{ 
	        	e ='';
				e += '<div class="edu_item_li">';
				e += '	<span class="edu_item_label">报考方向：</span>';
				e += '		<b class="read-select" style="min-width:120px;"> - 请选择 - </b>';
				e += '	</div>';

				e += '	<div class="edu_item_li">';
				e += '		<span class="edu_item_label">面试批次：</span>';
				e += '		<b class="read-select" style="min-width:120px;"> - 请选择 - </b>';
				e += '	</div>';

				c += '<div class="question-answer">';
				c += '	<div class="DHContainer" style="border:1px solid #ddd;padding:10px 20px;">'+ e +'</div>';
				c += '</div>';
	        }
	        return c;
	},
	_edit : function(data){
		var e ='';

        //规则设置
        e += '<div class="edit_jygz">';
        e += '	    <span class="title"><i class="icon-cog"></i> 校验规则</span>';
        e += '      <div class="groupbox">';
        e += '          <div class="edit_item_warp" style="margin-top:5px;">';
        e += '              <input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填</label>';
        e += '          </div>';
        e += '      </div>';
        //高级设置
        e += '      <div class="edit_item_warp">';
        e += '          <a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
        e += '      </div>';
        e += '</div>';
		return e;
	},
	
	_eventbind: function(data) {
		var child = data["children"][0];
		if (child == undefined) {
   		 child=data["children"];
   	 	}
		var $selectBtn = $("#"+data["itemId"] +child.bmrClass.itemId+ "_Btnselect");

		var siteId=$("#siteId").val();
		
		var classId=$("#ClassId").val();
		//console.log($selectBtn);
		////console.dir($selectBtn);
		var prov;
		
		$selectBtn.on("click",function(){
			var provinceUrl = SurveyBuild.tzGeneralURL + '?tzParams=';
			var params = '{"ComID":"TZ_COMMON_COM","PageID":"TZ_CLASS_STD","OperateType":"HTML","comParams":{"TZ_PROV_ID":"' 
				+ data["itemId"]
				+ child.bmrClass.itemId + '","linkId":"' 
				+ data["itemId"] 
				+ child.bmrBatch.itemId + '","siteId":"' 
				+ siteId + '","classId":"' 
				+ classId+ '"}}';
			provinceUrl = provinceUrl + window.escape(params);
			//弹出页面
			prov = $.layer({
				type: 2,
				title: false,
				fix: false,
				closeBtn: false,
				shadeClose: false,
				shade : [0.3 , '#000' , true],
				border : [3 , 0.3 , '#000', true],
				offset: ['100px',''],
				area: ['588px','300px'],
				iframe: {src: provinceUrl}
			});  
		}); 
		
		var $obj = $("#" + data["itemId"] +child.bmrBatch.itemId);
		//console.log("$obj:"+$obj);
		//console.dir($obj);
		$obj.on("change",function(){
            var selectIndex = $obj[0].selectedIndex;
            if($obj[0].options[selectIndex].value){
            	//console.log("value:"+$obj[0].options[selectIndex].value);
            	child.bmrBatch.wzsm = $obj[0].options[selectIndex].text;
            	$("#BatchId").val($obj[0].options[selectIndex].value);
            	
            }else{
            	child.bmrBatch.wzsm = "";
            }
        });
		
		
		$obj.formValidator({tipID:(data["itemId"] +child.bmrBatch.itemId+'Tip'), onShow:"", onFocus:"&nbsp;", onCorrect:"&nbsp;"});
		$obj.functionValidator({
			fun:function(val,el){
				if (data.isRequire == "Y"){
					if ($("#BatchId").val().length>0) {
						return 	true;
					} else {
						return MsgSet["REQUIRE"];
					}
				}
			}	
		});
		
	}
	
});