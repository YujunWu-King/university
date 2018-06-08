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
			"zssm":"",
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
		
	     if(previewmode){
	    	 var child=data["children"][0];
	    	 
	    	 if (child == undefined) {
	    		 child=data["children"];
	    	 }
			 var val=child.bmrClass.value;
			 var classid = $("#ClassId").val();
			 var batchId = $("#BatchId").val();

	    	 if($("#ClassId").length > 0){
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
	                            desc1 = f.comContent.classZssm;
	                            child.bmrClass.value = val;
	                            child.bmrClass.wzsm = desc;
	                            child.bmrClass.zssm = desc1;
	                        }
	                    }
	                });
	            }
			 //手机
			 if(SurveyBuild.accessType == "M"){
				 e += '<div class="item">';
				 e += '<p>'+ child.bmrClass.title +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span>';
				 if(SurveyBuild._readonly || $("#ClassId").length <= 0){
					 e += '</p>';
				 }else{
					 e += '<a href="#" style="display: inline;margin-left: 30px;" id="'+data["itemId"]+child.bmrClass.itemId+'_Btnselect">'+MsgSet["CHAGE_CLASS"]+'</a></p>';
				 }
				 e += '<div class="text-box"><input type="text" class="text1" readonly="true" id="' + data["itemId"] + child.bmrClass.itemId + '_SPAN" value="' + child.bmrClass.wzsm + '"/></div>';
				 e += '</div>';
				 e += '<input id="' +data["itemId"]+child.bmrClass.itemId + '" type="hidden" name="' + child.bmrClass.itemId + '" value="' + child.bmrClass.value + '">';

				 if(SurveyBuild._readonly){

					 // 只读模式(批次显示)
					 e += '<div class="item">';
					 e += '<p>'+ child.bmrBatch.title +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span>';
					 e += '</p><div class="text-box"><input ' + ' type="text" class="text1" readonly="true" value="' + child.bmrBatch.wzsm + '"/></div>';
					 e += '</div>';
					
				 }else{

					 // 编辑模式(选择批次)
					 var paramsP = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_OTHER_STD","OperateType":"EJSON","comParams":{"OType":"BATCH","CLASSID":"' + classid + '"}}';
					 $.ajax({
						 type: "get",
						 dataType: "JSON",
						 data: {
							 tzParams: paramsP
						 },
						 async: false,
						 url: SurveyBuild.tzGeneralURL,
						 success: function (f) {
							 if (f.state.errcode == "0") {
								 child.bmrBatch.option = f.comContent;
							 }
						 }
					 });

					 var opP='';
					 var array=new Array();
					 opP += '<option value="">' + MsgSet["PLEASE_SELECT"] + '</option>';

					 //排序
					 for (var i in child.bmrBatch.option) {
						 opP+= '<option ' + (child.bmrBatch.value == child.bmrBatch["option"][i]["code"] ? "selected='selected'": "") + 'value="' + child.bmrBatch["option"][i]["code"] + '">' + child.bmrBatch["option"][i]["txt"] + '</option>';
						 array.push(child.bmrBatch["option"][i]["code"]);
					 }
					 
					 if(array.length != 0){
						 
						 e += '<div class="item" id="div1">';
						 e += '<p>'+ child.bmrBatch.title +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span></p>';
						 e += '<div id="' + data["itemId"]+child.bmrBatch.itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
						 e += '<div class="text-box">';
						 e += '<select name="' + child.bmrBatch.itemId + '" class="select1" id="' + data["itemId"]+child.bmrBatch.itemId + '" title="' + child.bmrBatch.itemName + '">';
						 e += opP;
						 e += '</select>';
						 e += '</div>';
						 e += '</div>';
						 
					 }else{
						 
						 e += '<div class="item" id="div1" style="display:none">';
						 e += '<p>'+ child.bmrBatch.title +'<span>'+(data.isRequire == "Y" ? "*": "")+'</span></p>';
						 e += '<div id="' + data["itemId"]+child.bmrBatch.itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
						 e += '<div class="text-box">';
						 e += '<select name="' + child.bmrBatch.itemId + '" class="select1" id="' + data["itemId"]+child.bmrBatch.itemId + '" title="' + child.bmrBatch.itemName + '">';
						 e += opP;
						 e += '</select>';
						 e += '</div>';
						 e += '</div>';
						 
					 }
					
				 }
				 
				 c += e;

			 }else{

				 e += '<div class="input-list">';
				 e += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + MsgSet["CHOOSE_PRJ"] + '</div>';

				 if(SurveyBuild._readonly || $("#ClassId").length <= 0){
					 //存在班级情况下只读模式
					 e += '	<div class="input-list-text left">' + child.bmrClass.wzsm + '</div>';
				 } else {
					 e += '	<div class="input-list-text left" ><span id="'+data["itemId"]+child.bmrClass.itemId+'_SPAN">' + child.bmrClass.wzsm + '</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" id="'+data["itemId"]+child.bmrClass.itemId+'_Btnselect">'+MsgSet["CHAGE_CLASS"]+'</a></div>';
				 }

				 //e += '	<div class="input-list-suffix left"></div>';
				 e += '	<div class="clear"></div>';
				 e += '</div>';

				 e += '<input id="' +data["itemId"]+child.bmrClass.itemId + '" type="hidden" name="' + child.bmrClass.itemId + '" value="' + child.bmrClass.value + '">';

				 if(SurveyBuild._readonly){
					 if(batchId !=''){
						 // 只读模式
						 e += '<div class="input-list">';
						 e += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire  == "Y" ? "*": "") + '</span>' + MsgSet["INTERVIEW_BATCH"] + '</div>';
						 e += '  <div class="input-list-text left">' + child.bmrBatch.wzsm + '</div>';
						 e += '  <div class="input-list-suffix left"></div>';
						 e += '  <div class="clear"></div>';
						 e += '</div>';
						 if(child.bmrClass.zssm !=''){
							 e += '<div class="mainright-box pos-rela">' + child.bmrClass.zssm + '</div>';
						 }
					 }else{
						 if(child.bmrClass.zssm !=''){
							 e += '<div class="mainright-box pos-rela">' + child.bmrClass.zssm + '</div>';
						 }
					 }

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

					 var op='';
					 var array=new Array();

					 op += '<option value="">' + MsgSet["PLEASE_SELECT"] + '</option>';
					 for (var i in child.bmrBatch.option) {

						 op+= '<option ' + (child.bmrBatch.value == child.bmrBatch["option"][i]["code"] ? "selected='selected'": "") + 'value="' + child.bmrBatch["option"][i]["code"] + '">' + child.bmrBatch["option"][i]["txt"] + '</option>';
						 array.push(child.bmrBatch["option"][i]["code"]);
					 }
					 if(array.length != 0){
						 e += '<div class="input-list" id="div1" style="display:">';
						 e += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + MsgSet["INTERVIEW_BATCH"] + '</div>';
						 e += '    <div class="input-list-text left input-edu-select">';
						 e += '          <select name="' + child.bmrBatch.itemId + '" class="chosen-select" id="' + data["itemId"]+child.bmrBatch.itemId + '" style="width:100%;" title="' + child.bmrBatch.itemName + '">';
						 e +=                    op;
						 e += '          </select>';
						 e += '    </div>';
						 e += '    <div class="input-list-suffix left"><div id="' +data["itemId"]+child.bmrBatch.itemId + 'Tip" class="onShow"><div class="onShow"></div></div></div>';
						 e += '    <div class="clear"></div>';
						 e += '</div>';
					 }else{
						 child.bmrBatch.wzsm = "";
						 e += '<div class="input-list" id="div1" style="display:none">';
						 e += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + MsgSet["INTERVIEW_BATCH"] + '</div>';
						 e += '    <div class="input-list-text left input-edu-select">';
						 e += '          <select name="' + child.bmrBatch.itemId + '" id="' + data["itemId"]+child.bmrBatch.itemId + '" style="width:100%;" title="' + child.bmrBatch.itemName + '">';
						 e +=                    op;
						 e += '          </select>';
						 e += '    </div>';
						 e += '    <div class="input-list-suffix left"><div id="' +data["itemId"]+child.bmrBatch.itemId + 'Tip" class="onShow"><div class="onShow"></div></div></div>';
						 e += '    <div class="clear"></div>';
						 e += '</div>';
					 }

				 }

				 c += '<div class=""></div>';
				 c += '<div class="main_inner_content">';
				 c += e;
				 c += '</div>';
				 c += '<div class="main_inner_content_foot"></div>';
			 }

	        }else{ 
	        	e ='';
				e += '<div class="edu_item_li">';
				e += '	<span class="edu_item_label">报考方向：</span>';
				e += '		<b class="read-select" style="min-width:120px;"> - 请选择 - </b>';
				e += '	</div>';

				e += '	<div class="edu_item_li">';
				e += '		<span class="edu_item_label">申请批次：</span>';
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
		var prov;
		var flag =false;
		//判断是否存在其他项目
		var paramsO = '{"ComID":"TZ_COMMON_COM","PageID":"TZ_CLASS_STD","OperateType":"otherClass","comParams":{"TZ_PROV_ID":"'
            + data["itemId"]
            + child.bmrClass.itemId + '","linkId":"'
            + data["itemId"]
            + child.bmrBatch.itemId + '","siteId":"'
            + siteId + '","classId":"'
            + classId+ '"}}';
		$.ajax({
			type: "post",
			async :false,
			data:{
				tzParams:paramsO
			},
			url: TzUniversityContextPath + "/dispatcher",
			dataType: "JSON",
			success: function(result){
				if(result.comContent>0){
					flag=true;
				}
			}
		})
		if(flag==true){
			$selectBtn.show();
		}else{
			$selectBtn.hide();
		}
		$selectBtn.on("click",function(){
			if(SurveyBuild.accessType == "M"){

				var tzParam = '{"ComID":"TZ_COMMON_COM","PageID":"TZ_M_CLASS_STD","OperateType":"HTML","comParams":{"TZ_PROV_ID":"'
					+ data["itemId"]
					+ child.bmrClass.itemId + '","linkId":"'
					+ data["itemId"]
					+ child.bmrBatch.itemId + '","siteId":"'
					+ siteId + '","classId":"'
					+ classId+ '"}}';

				$.ajax({
					type: "post",
					async :false,
					data:{
						tzParams:tzParam
					},
					url: TzUniversityContextPath + "/dispatcher",
					dataType: "html",
					success: function(result){

						$("#searchCountry").html("");
						$("#searchCountry").html(result);
						$("#MainDiv").hide();
						$("#searchCountry").fadeIn("slow");

					}
				});

			}else{

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
			}

		});
		
		var $obj = $("#" + data["itemId"] +child.bmrBatch.itemId);
		$obj.on("change",function(){
            var selectIndex = $obj[0].selectedIndex;
            if($obj[0].options[selectIndex].value){
            	child.bmrBatch.wzsm = $obj[0].options[selectIndex].text;
            	$("#BatchId").val($obj[0].options[selectIndex].value);
            	
            }else{
            	child.bmrBatch.wzsm = "";
            }
        });
		
		
		$obj.formValidator({tipID:(data["itemId"] +child.bmrBatch.itemId+'Tip'), onShow:"", onFocus:"&nbsp;", onCorrect:"&nbsp;"});
		$obj.functionValidator({
			fun:function(val,el){

				if (val=="") {
					return MsgSet["REQUIRE"];

				} else {
					return 	true;
				}
			}	
		});
		
	}
	
});