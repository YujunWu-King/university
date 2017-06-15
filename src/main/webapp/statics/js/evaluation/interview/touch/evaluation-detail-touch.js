//考生列表显示
function show1(){
	$("#div1").attr({style:"display:block"});
	$("#div2").attr({style:"display:none"});
	$("#div3").attr({style:"display:none"});
    
    ks_list();
}

//报名表显示
function show2(){
    var SHOW_KSH_BMBID = AppInsId;
	
    if (SHOW_KSH_BMBID ==""){
		$("#div1").attr({style:"display:none"});
		$("#div2").attr({style:"display:none"});
		$("#div3").attr({style:"display:none"});
		
		alert("请先选择考生！");
	}else{
		tz_ks_bmb_menu(SHOW_KSH_BMBID);

		$("#div1").attr({style:"display:none"});
		$("#div2").attr({style:"display:block"});
		$("#div3").attr({style:"display:none"});
	}
}

//打分区显示
function show3(){
    var TZ_CLASS_ID = ClassId;
	var TZ_APPLY_PC_ID = BatchId;
    var KSH_BMBID = AppInsId;

	if (KSH_BMBID ==""){
			alert("请先选择考生！");

			$("#div1").attr({style:"display:none"});
			$("#div2").attr({style:"display:none"});
			$("#div3").attr({style:"display:none"});
	}else{
			$("#div1").attr({style:"display:none"});
			$("#div2").attr({style:"display:none"});
			$("#div3").attr({style:"display:block"});

			ks_show_df_info(TZ_CLASS_ID,TZ_APPLY_PC_ID,KSH_BMBID);
	}
}

//考生列表开始
function ks_list(){

	var TZ_CLASS_ID = ClassId;
    var TZ_APPLY_PC_ID = BatchId;

	var baseUrl = ContextPath+"/dispatcher?classid=interview&OperateType=EJSON";

	var url = baseUrl+'&type=data&BaokaoClassID='+ClassId+'&BaokaoPCID='+BatchId+'&RequestDataType=S&MaxRowCount=200';
    
  	//使用getJSON方法取得JSON数据
    $.getJSON(
    		url, 
	        function(data){ //处理数据 data指向的是返回来的JSON数据
    			var jsonObject = data.comContent;

				//考生列表
				var collist="";
				var ksheadObject=jsonObject['ps_data_kslb']['ps_ksh_list_headers'];
				for(var i in ksheadObject) {
					collist+='<th>'+ksheadObject[i]+'</th>';
				}
				
				
				var detallist="";
				var ksbodyArray=jsonObject['ps_data_kslb']['ps_ksh_list_contents'];
				for (var i=0;i<ksbodyArray.length;i++){		
					var concollist="";
					for(var j in ksheadObject) {
						concollist+="<td class='alt' >"+(ksbodyArray[i][j]||"")+'</td>';
					}

					detallist+="<tr>";
					detallist+="<td class='alt'>"+ksbodyArray[i]['ps_ksh_xh']+"</td>";
					detallist+="<td class='alt'><a href='javascript:void(0);' onclick='tz_ks_bmb("+ksbodyArray[i]['ps_ksh_bmbid']+");'>"+ksbodyArray[i]['ps_msh_id']+"</a></td>";
					detallist+="<td class='alt'>"+ksbodyArray[i]['ps_ksh_xm']+"</td>";
					detallist+=concollist;
					detallist+="<td class='alt'>"+ksbodyArray[i]['ps_ksh_school']+"</td>";
					detallist+="<td class='alt'>"+ksbodyArray[i]['ps_ksh_company']+"</td>";
					detallist+="<td class='alt'>"+ksbodyArray[i]['ps_ksh_zt']+"</td>";
					detallist+="<td class='alt'>"+ksbodyArray[i]['ps_ksh_dt']+"</td>";
					detallist+="</tr>";
				}
				

				var examerinfoHeader="";
				examerinfoHeader+="<th scope='col' style='text-align:center;'>面试顺序</th>";
				examerinfoHeader+="<th scope='col' style='text-align:center;'>面试申请号</th>";
				examerinfoHeader+="<th scope='col' style='text-align:center;'>考生姓名</th>";
				var headbutton="";
		  	    headbutton+="<th scope='col' style='text-align:center;'>本科院校</th>";
		  	  	headbutton+="<th scope='col' style='text-align:center;'>工作单位</th>";
		  	  	headbutton+="<th scope='col' style='text-align:center;'>评议状态</th>";
		  	  	headbutton+="<th scope='col' style='text-align:center;'>评审时间</th>";

				var examerinfolist="";
			 	examerinfolist="<table id='mytable' cellspacing='0' width='100%' summary='the technical specifications of the apple powermac g5 series'>";
			 	examerinfolist+="<tr>"+examerinfoHeader+collist+headbutton+"</tr>";
			 	examerinfolist+=detallist+'</table>';

			    // $("#examerinfo").html(examerinfolist);

				$("#ks_list_head").html("");
				$("#ks_list_head").append(examerinfolist);
				
    		}
    		);
}
//考生列表结束

//非超链接路径显示报名表开始(点击页签)
function tz_ks_bmb_menu(bmb_id){
	//每次点击考生的时候，把当前的考生报名表id给赋值 
	document.getElementById("ks_search_tz_app_ins_id").value = bmb_id;
	document.getElementById("ks_show_tz_app_ins_id").value = bmb_id;

	//班级编号
	var TZ_CLASS_ID = ClassId;
    //申请批次编号
    var TZ_APPLY_PC_ID = BatchId;

    //当前考生报名表iframe编号
    iframe_tmp_id = "bmb_iframe_" +TZ_CLASS_ID+"_"+TZ_APPLY_PC_ID + "_" + bmb_id;

    //已经存在的考生报名表iframe编号串
    var ks_iframe_str_id = $("#ks_iframe_str_id").val();

	var arr = ks_iframe_str_id.split("=");
	
	//判断是否已存在，如果存在，显示，把其他的隐藏
    var flag = "N";
	for (var j=0 ; j< arr.length ; j++) {
		var ifr_tmp = arr[j];
		if (arr[j] == iframe_tmp_id){
			flag = "Y";
			$("#div_"+ifr_tmp).show();
			$("#div1").hide();
			$("#div2").show();
		}else{	
			$("#div_"+ifr_tmp).hide();
        }
	}
	
	if(flag == "Y"){
		$("#ks_list").removeAttr("class");
		$("#ks_list").attr({class:"ui-btn ui-btn-inline ui-btn-up-c"});
		$("#ks_bmb").removeAttr("class");
		$("#ks_bmb").attr({class:"ui-btn ui-btn-inline ui-btn-up-c ui-btn-active"});
		$("#ks_dfq").removeAttr("class");
		$("#ks_dfq").attr({class:"ui-btn ui-btn-inline ui-btn-up-c"});

		for (var k=0 ; k< arr.length ; k++){
			var ifr_tmp_k = arr[k];
			if (arr[k] == iframe_tmp_id){
				$("#div_"+ifr_tmp_k).show();
				$("#div1").hide();
				$("#div2").show();
			}else{
				$("#div_"+ifr_tmp).hide();
			}
		}
	} else {
		
		//iframe 的id 串
		var iframe_tmp = "=bmb_iframe_" +TZ_CLASS_ID+"_"+TZ_APPLY_PC_ID + "_" + bmb_id;
		var div_tmp = "div_" + iframe_tmp;

		$("#ks_iframe_str_id").val($("#ks_iframe_str_id").val() + iframe_tmp); 

		var bmb_url = "";
		var tzParamsBmbUrl='{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"HTML","comParams":{"TZ_APP_INS_ID":"'+bmb_id+'","TZ_APP_TPL_ID":"'+AppTplId+'","isReview":"Y"}}';
		bmb_url = scoreUrl + "?tzParams=" + encodeURIComponent(tzParamsBmbUrl);
		
		var bmbArea = "";
		bmbArea += '<div id="'+ div_tmp +'" style="width:auto; height:auto">';
		bmbArea += '<iframe id="'+ iframe_tmp +'" name="'+ iframe_tmp +'" width="100%" height="100%" frameborder="0" src="'+ bmb_url +'"></iframe></div>';
		
		$("#div2").append(bmbArea); 
		
		$("#ks_list").removeAttr("class");
		$("#ks_list").attr({class:"ui-btn ui-btn-inline ui-btn-up-c"});
		$("#ks_bmb").removeAttr("class");
		$("#ks_bmb").attr({class:"ui-btn ui-btn-inline ui-btn-up-c ui-btn-active"});
		$("#ks_dfq").removeAttr("class");
		$("#ks_dfq").attr({class:"ui-btn ui-btn-inline ui-btn-up-c"});

		
		/*
		$.ajax({
	  		type: 'POST',
			async:false,
	  		url: "%bind(:6)",
	  		data: {"TZ_APP_INS_ID":ks_id,"TZ_APPLY_PC_ID":TZ_APPLY_PC_ID},
	  		success: function(msg) {
				//iframe 的id 串
				var iframe_tmp = "=bmb_iframe_" +TZ_APPLY_PC_ID + "_" + ks_id;

				$("#ks_iframe_str_id").val($("#ks_iframe_str_id").val() + iframe_tmp); 

				$("#div2").append(msg); 

				$("#ks_list").removeAttr("class");
				$("#ks_list").attr({class:"ui-btn ui-btn-inline ui-btn-up-c"});
				$("#ks_bmb").removeAttr("class");
				$("#ks_bmb").attr({class:"ui-btn ui-btn-inline ui-btn-up-c ui-btn-active"});
	        },
	  		dataType: "html"
		}); 
		*/
	}
}
//非超链接路径显示报名表结束(点击页签)

//考生列表点击面试申请号显示报名表开始
function tz_ks_bmb(bmbId){
	document.getElementById("ks_search_msid").value = "";
    document.getElementById("ks_search_name").value = "";

    //记录此考生可以显示打分区
    document.getElementById("ks_show_dfq").value = "Y";


	//每次点击考生的时候，把当前的考生报名表id 给赋值 
	document.getElementById("ks_search_tz_app_ins_id").value = bmbId;
	document.getElementById("ks_show_tz_app_ins_id").value = bmbId;

	//班级编号
	var TZ_CLASS_ID = ClassId;
    //申请批次编号
    var TZ_APPLY_PC_ID = BatchId;

	AppInsId = bmbId;

    //当前考生报名表iframe编号
    iframe_tmp_id = "bmb_iframe_" +TZ_CLASS_ID+"_"+TZ_APPLY_PC_ID + "_" + bmbId;

    //已经存在的考生报名表iframe编号串
    var ks_iframe_str_id = $("#ks_iframe_str_id").val();

	var arr = ks_iframe_str_id.split("=");
	
	//判断是否已存在，如果存在，显示，把其他的隐藏
    var flag = "N";
	for (var j=0 ; j< arr.length ; j++){
		var ifr_tmp = arr[j];
		if (arr[j] == iframe_tmp_id){
			flag = "Y";
			$("#div_"+ifr_tmp).show();
			$("#div1").hide();
			$("#div2").show();
		}else{
			$("#div_"+ifr_tmp).hide();
        }
	}
	
	if(flag == "Y"){
		$("#ks_list").removeAttr("class");
		$("#ks_list").attr({class:"ui-btn ui-btn-inline ui-btn-up-c"});
		$("#ks_bmb").removeAttr("class");
		$("#ks_bmb").attr({class:"ui-btn ui-btn-inline ui-btn-up-c ui-btn-active"});

		for (var k=0 ; k< arr.length ; k++){
			var ifr_tmp_k = arr[k];
			if (arr[k] == iframe_tmp_id){
				$("#div_"+ifr_tmp_k).show();
				$("#div1").hide();
				$("#div2").show();
			}else{
				$("#div_"+ifr_tmp).hide();
            }
		}
	} else {
		//iframe 的id 串
		var iframe_tmp = "=bmb_iframe_" +TZ_CLASS_ID+"_"+TZ_APPLY_PC_ID + "_" + bmbId;
		var div_tmp = "div_" + iframe_tmp;
		
		$("#ks_iframe_str_id").val($("#ks_iframe_str_id").val() + iframe_tmp); 
		
		var bmb_url = "";
		var tzParamsBmbUrl='{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"HTML","comParams":{"TZ_APP_INS_ID":"'+bmbId+'","TZ_APP_TPL_ID":"'+AppTplId+'","isReview":"Y"}}';
		bmb_url = scoreUrl + "?tzParams=" + encodeURIComponent(tzParamsBmbUrl);
		
		var bmbArea = "";
		bmbArea += '<div id="'+ div_tmp +'" style="width:auto; height:auto">';
		bmbArea += '<iframe id="'+ iframe_tmp +'" name="'+ iframe_tmp +'" width="100%" height="100%" frameborder="0" src="'+ bmb_url +'"></iframe></div>';
		
		$("#div2").append(bmbArea); 
		
		$("#ks_list").removeAttr("class");
		$("#ks_list").attr({class:"ui-btn ui-btn-inline ui-btn-up-c"});
		$("#ks_bmb").removeAttr("class");
		$("#ks_bmb").attr({class:"ui-btn ui-btn-inline ui-btn-up-c ui-btn-active"});

		show2();
		
		/*
		$.ajax({
	  		type: 'POST',
			async:false,
	  		url: "%bind(:6)",
	  		data: {"TZ_APP_INS_ID":ks.id,"TZ_APPLY_PC_ID":TZ_APPLY_PC_ID},
	  		success: function(msg) {
				//iframe 的id 串
				var iframe_tmp = "=bmb_iframe_" +TZ_APPLY_PC_ID + "_" + ks.id;
				
				$("#ks_iframe_str_id").val($("#ks_iframe_str_id").val() + iframe_tmp); 

				$("#div2").append(msg); 

				$("#ks_list").removeAttr("class");
				$("#ks_list").attr({class:"ui-btn ui-btn-inline ui-btn-up-c"});
				$("#ks_bmb").removeAttr("class");
				$("#ks_bmb").attr({class:"ui-btn ui-btn-inline ui-btn-up-c ui-btn-active"});

				show2();
	        },
	  		dataType: "html"
		}); 
		*/
	}
}
//考生列表点击面试申请号显示报名表开始

//查找考生开始
function ks_look(){
	//记录此考生可以显示打分区
    document.getElementById("ks_show_dfq").value = "N";

	var TZ_CLASS_ID = ClassId;
    var TZ_APPLY_PC_ID = BatchId;
    var ks_search_msid = document.getElementById("ks_search_msid").value;
    var ks_search_name = document.getElementById("ks_search_name").value;

	if (ks_search_msid!="" || ks_search_name!="") {
		var searchKSData = '{"CLASSID":"' + TZ_CLASS_ID + '","APPLY_BATCHID":"' + TZ_APPLY_PC_ID + '","KSH_SEARCH_MSID":"' + ks_search_msid + '","KSH_SEARCH_NAME":"' + ks_search_name + '"}';
		var tzParams = '{"ComID":"TZ_EVA_INTERVIEW_COM","PageID":"TZ_MSPS_DF_STD","OperateType":"tzSearchExaminee","comParams":' + searchKSData + '}';

		$.ajax({
			type: 'post',
			dataType: 'json',
			async: false,
			url: scoreUrl,
			data: {"tzParams": tzParams},
			success: function (data) {
				var comContent = data.comContent;
				var result = comContent.result;
				var resultMsg = comContent.resultMsg;

				if (result == "0") {
					var searchedOprid = comContent.oprid;
					var searchedBmbId = comContent.bmbId;
					var searchedMssqh = comContent.mssqh;
					var searchedName = comContent.name;

					document.getElementById("ks_search_tz_app_ins_id").value = searchedBmbId;
					document.getElementById("ks_show_tz_app_ins_id").value = searchedBmbId;
					document.getElementById("ks_look_back_info").innerHTML = "您搜索的考生面试编号为：【" + searchedMssqh + "】，姓名为：【" + searchedName + "】";
				} else {
					document.getElementById("ks_look_back_info").innerHTML = resultMsg;
					document.getElementById("ks_search_tz_app_ins_id").value = "";
					alert(resultMsg);
				}

				/*
				 var arr = msg.split("|");

				 if (arr[0] == "Y"){
				 document.getElementById("ks_search_tz_app_ins_id").value = arr[1];
				 document.getElementById("ks_show_tz_app_ins_id").value = arr[1];
				 document.getElementById("ks_look_back_info").innerHTML = "您搜索的考生面试编号为：【"+arr[6]+"】，姓名为：【"+arr[3]+"】";
				 }else{
				 document.getElementById("ks_look_back_info").innerHTML = msg;
				 document.getElementById("ks_search_tz_app_ins_id").value = "";
				 alert(msg);
				 }
				 */
			}
		});
	} else {
		alert("申请号或姓名至少输入一项！");
	}

}
//查找考生结束

//进行评审开始
function ks_pingsheng(){
	var TZ_CLASS_ID = ClassId;
	var TZ_APPLY_PC_ID = BatchId;
    var KSH_BMBID = AppInsId;
	if (KSH_BMBID == ""){
		alert("请选择考生！");
	}else{
		
		var searchKSData = '{"CLASSID":"'+TZ_CLASS_ID+'","APPLY_BATCHID":"'+TZ_APPLY_PC_ID+'","KSH_BMBID":"'+KSH_BMBID+'"}';
        var tzParams = '{"ComID":"TZ_EVA_INTERVIEW_COM","PageID":"TZ_MSPS_DF_STD","OperateType":"tzAddExaminee","comParams":'+searchKSData+'}';
		
		$.ajax({
	  		type: 'post',
	  		dataType: 'json',
			async:false,
	  		url: scoreUrl,
	  		data: {"tzParams":tzParams},
	  		success: function(data) {
	  			var comContent = data.comContent;
	  			var result = comContent.result;
	  			var resultMsg = comContent.resultMsg;
	  			
	  			if(result=="0") {
	  				//记录此考生可以显示打分区
			        document.getElementById("ks_show_dfq").value = "Y";
	  				
			        $("#div1").attr({style:"display:none"});
					$("#div2").attr({style:"display:none"});
					$("#div3").attr({style:"display:block"});
					
					$("#ks_list").removeAttr("class");
					$("#ks_list").attr({class:"ui-btn ui-btn-inline ui-btn-up-c"});
					$("#ks_bmb").removeAttr("class");
					$("#ks_bmb").attr({class:"ui-btn ui-btn-inline ui-btn-up-c"});
					$("#ks_dfq").removeAttr("class");
					$("#ks_dfq").attr({class:"ui-btn ui-btn-inline ui-btn-up-c ui-btn-active"});
					
					ks_show_df_info(TZ_CLASS_ID,TZ_APPLY_PC_ID,KSH_BMBID);
	  			} else {
	  				alert(resultMsg);
	  			}
	  			
	  			/*
				if(msg.error_code =="0" || msg.error_code =="2" || msg.error_decription =="2"){

			        //记录此考生可以显示打分区
			        document.getElementById("ks_show_dfq").value = "Y";

					$("#div1").attr({style:"display:none"});
					$("#div2").attr({style:"display:none"});
					$("#div3").attr({style:"display:block"});

					$("#ks_list").removeAttr("class");
					$("#ks_list").attr({class:"ui-btn ui-btn-inline ui-btn-up-c"});
					$("#ks_bmb").removeAttr("class");
					$("#ks_bmb").attr({class:"ui-btn ui-btn-inline ui-btn-up-c"});
					$("#ks_dfq").removeAttr("class");
					$("#ks_dfq").attr({class:"ui-btn ui-btn-inline ui-btn-up-c ui-btn-active"});
				
					ks_show_df_info(TZ_APPLY_PC_ID,KSH_BMBID);
				}else{	
					alert(msg.error_decription);
				}
	  			*/
	        }
		}); 
	}
}
//进行评审结束

//显示考生的打分区开始
function ks_show_df_info(TZ_CLASS_ID,TZ_APPLY_PC_ID,KSH_BMBID){
	if (dfq_arr == null){
		dfq_arr = new Array();
	}
	
	//记录此考生可以显示打分区
    var ks_show_dfq = document.getElementById("ks_show_dfq").value;
	
	if (ks_show_dfq =="Y"){
		var dfq_you_info = "N";
		
		for(var dfq_i= 0;dfq_i < dfq_arr.length;dfq_i++) {
			var dfq_geren_info = "";//取数组
			dfq_geren_info = dfq_arr[dfq_i];

			var dfq_gr_arr = dfq_geren_info.split("###");//取数组
			var bmbid_info = "";
			bmbid_info = dfq_gr_arr[0];
			var dfq_bmbid_arr = bmbid_info.split("@@@");

			if(dfq_bmbid_arr[1] == KSH_BMBID){
				dfq_you_info = "Y";
			}
		 }
	
		if(dfq_you_info == "Y"){
			for(var dfq_j= 0;dfq_j < dfq_arr.length;dfq_j++) {
				var dfq_geren_info_tmp = "";//取数组
		    	dfq_geren_info_tmp = dfq_arr[dfq_j];
			
			    var dfq_gr_arr_tmp = dfq_geren_info_tmp.split("###");//取数组
				var bmbid_info_tmp = "";
				bmbid_info_tmp = dfq_gr_arr_tmp[0];
				var dfq_bmbid_arr_tmp = bmbid_info_tmp.split("@@@");
			
				if(dfq_bmbid_arr_tmp[1] == KSH_BMBID){
					for(var dfq_gr_k= 0;dfq_gr_k < dfq_gr_arr_tmp.length;dfq_gr_k++) {
						var dfq_gr_cjx_info_tmp = "";
						dfq_gr_cjx_info_tmp = dfq_gr_arr_tmp[dfq_gr_k];

						var dfq_gr_cjx_arr_tmp = dfq_gr_cjx_info_tmp.split("@@@");//单个成绩项数组（第一个为id，第二个为value）
						
						if(dfq_gr_cjx_arr_tmp[0] == "ks_dfq_ms_id"){
							document.getElementById("ks_dfq_ms_id").innerHTML = dfq_gr_cjx_arr_tmp[1];
						}else if(dfq_gr_cjx_arr_tmp[0] == "ks_dfq_ms_name"){
							document.getElementById("ks_dfq_ms_name").innerHTML = dfq_gr_cjx_arr_tmp[1];
						}else if(dfq_gr_cjx_arr_tmp[0] == "ps_sx_ksbq"){
							document.getElementById("ps_sx_ksbq").innerHTML = dfq_gr_cjx_arr_tmp[1];
						}else if(dfq_gr_cjx_arr_tmp[0] == "ps_clpscj_ck"){
							document.getElementById("ps_clpscj_ck").innerHTML = dfq_gr_cjx_arr_tmp[1];
						}else if(dfq_gr_cjx_arr_tmp[0] == "ks_new_w_bmb"){
							$("#ks_new_w_bmb").attr({href:dfq_gr_cjx_arr_tmp[1]});
						}else {
							sf_qk_fjd = "N";

							document.getElementById(dfq_gr_cjx_arr_tmp[0]).value = dfq_gr_cjx_arr_tmp[1].replace("\\n","\n").replace("\\n","\n").replace("\\n","\n").replace("\\n","\n").replace("\\n","\n").replace("\\n","\n").replace("\\n","\n").replace("\\n","\n").replace("\\n","\n").replace("\\n","\n").replace("\\n","\n").replace("\\n","\n").replace("\\n","\n").replace("\\n","\n").replace("\\n","\n");
						}
					}
				
					$("input[type=number]").slider("refresh");
				}
			}
		} else {
		//没有cache开始
		
			//用于cache数据
			var bmb_cache_id = "";

        	//var url="%bind(:11)&BaokaoFXID="+TZ_APPLY_PC_ID+"&KSH_BMBID="+KSH_BMBID;
        	var tzParams = '{"ComID":"TZ_EVA_INTERVIEW_COM","PageID":"TZ_MSPS_DF_STD","OperateType":"QF","comParams":{"classId":"'+TZ_CLASS_ID+'","applyBatchId":"'+TZ_APPLY_PC_ID+'","bmbId":"'+KSH_BMBID+'"}}';
        	var url = scoreUrl + "?tzParams="+tzParams;
        
        	//使用getJSON方法取得JSON数据 
       		$.getJSON(　　　　
		        	url, 
		        	function(data){ //处理数据 data指向的是返回来的JSON数据 
		        		var comContent = data.comContent;
		        		var messageCode = comContent.messageCode;
		        		var message = comContent.message;
		        		
		        		if(messageCode=="0") {
		        			var bmbId = comContent.bmbId;
		        			var name = comContent.name;
		        			var interviewApplyId = comContent.interviewApplyId;
		        			var examineeTag = comContent.examineeTag;
		        			var materialReviewDescPad = comContent.materialReviewDescPad;
		        			
		        			//报名表链接
			        		var tzParamsBmbUrl='{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"HTML","comParams":{"TZ_APP_INS_ID":"'+bmbId+'","TZ_APP_TPL_ID":"'+AppTplId+'","isReview":"Y"}}';
			        		var bmb_url = scoreUrl + "?tzParams=" + encodeURIComponent(tzParamsBmbUrl);
			        			
			        		//新开窗口显示报名表信息
							$("#ks_new_w_bmb").attr({href:bmb_url});
			        		
							document.getElementById("ks_dfq_ms_id").innerHTML = interviewApplyId;
							document.getElementById("ks_dfq_ms_name").innerHTML = name;
							if(examineeTag!=""&&examineeTag!=null){
								document.getElementById("ps_sx_ksbq").innerHTML = examineeTag;
								document.getElementById("div_sx_ksbq").style.display="block";
							} else {
								document.getElementById("div_sx_ksbq").style.display="none";
							}

							document.getElementById("ps_clpscj_ck").innerHTML = materialReviewDescPad;
							
							//用于cache数据
							bmb_cache_id = "ks_search_tz_app_ins_id@@@" + KSH_BMBID + "###ks_new_w_bmb@@@" + bmb_url + "###ks_dfq_ms_id@@@" + interviewApplyId + "###ks_dfq_ms_name@@@" + name + "###ps_sx_ksbq@@@" + examineeTag + "###ps_clpscj_ck@@@" + materialReviewDescPad;
				        
							//li 列表项目 
							var cjx_lis=""; 　　　
	
							var tz_cjx_id_value = "";
							
							$.each(comContent.scoreContent, function(k, v) {
								//拼装成绩项id
								if (tz_cjx_id_value ==""){
									tz_cjx_id_value = v.itemId + "," + v.itemParentId + "," + v.itemType + "," + v.itemCommentLowerLimit + "," + v.itemCommentUpperLimit + "," + v.itemName + "," + v.itemLowerLimit + "," + v.itemUpperLimit;
								}else{
									tz_cjx_id_value = tz_cjx_id_value + ";" + v.itemId + "," + v.itemParentId + "," + v.itemType + "," + v.itemCommentLowerLimit + "," + v.itemCommentUpperLimit + "," + v.itemName + "," + v.itemLowerLimit + "," + v.itemUpperLimit;
								}
								
								//用于cache数据
								if(v.itemType=="C") {
									//评语类型
									bmb_cache_id = bmb_cache_id + "###" +v.itemId + "@@@" + v.itemComment;
								} else {
									bmb_cache_id = bmb_cache_id + "###" +v.itemId + "@@@" + v.itemValue;
								}
								
								cjx_lis += "<tr>";
			 					cjx_lis +=  "<div data-role='fieldcontain' class='ui-field-contain ui-body ui-br'> ";
			 					
			 					//根据层次缩进
			 					var paddingLeft = v.itemLevel*15;
			 					cjx_lis += "<td style='vertical-align:middle;'><label for='"+  v.itemId +"' style='width:180px;padding-left:"+ paddingLeft +"px;' class='' id='"+ v.itemId +"-label'>" + v.itemName + "</label></td>";
								
			 					//根据是否只读展现不同的形式
								if (v.itemType =="A") {
									//数字成绩汇总项
									cjx_lis += 	"<td><div style='width:55px;'><input type='text' style='width: 55px; height: 20px; background:#ddd' readonly='true' name='" +  v.itemId + "' id='" +  v.itemId + "' value='" + v.itemValue  +  "' min='" + v.itemLowerLimit  +  "' max='" + v.itemUpperLimit  +  "' /></div></td>";
								}else if (v.itemType =="B"){
									//数字成绩录入项
									cjx_lis += 	"<td width='410px'><input type='range' onchange='tz_parent_id_tmp(this);' style='width: 55px; height: 20px;' data-highlight='true' name='" +  v.itemId + "' id='" +  v.itemId + "' value='" + v.itemValue  +  "' min='" + v.itemLowerLimit  +  "' max='" + v.itemUpperLimit  +  "' /></td>";
								}else if (v.itemType =="C"){
									//评语
									//解决页面中换行出现反斜杠n 的情况
									var newstr = "";
									newstr = v.itemComment.replace("\\n","\n");
									newstr = newstr.replace("\\n","\n");
									newstr = newstr.replace("\\n","\n");
									newstr = newstr.replace("\\n","\n");
									newstr = newstr.replace("\\n","\n");
									newstr = newstr.replace("\\n","\n");
									newstr = newstr.replace("\\n","\n");
									newstr = newstr.replace("\\n","\n");
									newstr = newstr.replace("\\n","\n");
									newstr = newstr.replace("\\n","\n");
									newstr = newstr.replace("\\n","\n");
									newstr = newstr.replace("\\n","\n");
									newstr = newstr.replace("\\n","\n");
									newstr = newstr.replace("\\n","\n");
									newstr = newstr.replace("\\n","\n");
	
	 								cjx_lis += 	"<td><textarea  style='width: 450px; margin: 8px 0px; height: 66px;margin-right:15px;' rows='10' class='ui-input-text ui-body-c ui-corner-all ui-shadow-inset' name='" +  v.itemId + "' id='" +  v.itemId + "' value='" + v.itemComment  +  "' min='" + v.itemCommentLowerLimit  +  "' max='" + v.itemCommentUpperLimit  +  "'>"+newstr+"</textarea></td>";
								} else if (v.itemType=="D") {
									//下拉框
									cjx_lis += "<td width='410px'><select class='ui-input-text ui-body-c ui-corner-all ui-shadow-inset ui-slider-input' name='" +  v.itemId + "' id='" +  v.itemId + "'>";
									$.each(comContent.scoreContent.itemOptions, function(k1, v1) {
										if(v1.itemOptionValue==v.itemValue) {
											cjx_lis += "<option value='"+ v.itemOptionValue +"' selected>" + v.itemOptionName + "</option>";
										} else {
											cjx_lis += "<option value='"+ v.itemOptionValue +"'>"+ v.itemOptionName +"</option>";
										}	
									});
									cjx_lis += "</select></td>";
								}
							
								cjx_lis += 	"</div>";
								
								
								//在节点为叶子节点时，评议类和数据类区别提示
								if(v.itemIsLeaf =="Y"){
									if(v.itemType =="B" || v.itemType =="D"){
										cjx_lis += "<td style='vertical-align:middle;'>(" + v.itemLowerLimit + "-" + v.itemUpperLimit + ")</td>";
										cjx_lis += "<td width='15px'></td>";

										cjx_lis += "<td style='vertical-align:middle;'>";
										if(v.itemDfsm!="" && v.itemDfsm!=null) {
											cjx_lis += "<a href='#w_bz_"+v.itemId+"'  data-rel='popup' class='ui-link'>标准</a><div  data-role='popup'  id='w_bz_"+v.itemId+"'  class='ui-content' style='max-width:1000px, max-height:500px' data-theme='a' data-overlay-theme='b'><a href='#' data-rel='back' data-role='button' data-theme='a' data-icon='delete' data-iconpos='notext' class='ui-btn-right'>关闭</a><p>"+v.itemDfsm+"</p></div>";
										}
										cjx_lis += "</td>";
										cjx_lis += "<td width='15px'></td>";
										cjx_lis += "<td style='vertical-align:middle;'>";
										if(v.itemCkwt!="" && v.itemCkwt!=null) {
											cjx_lis += "<a  href='#w_sm_"+v.itemId+"'  data-rel='popup' class='ui-link'>说明</a><div   data-role='popup'   id='w_sm_"+v.itemId+"' class='ui-content' style='max-width:1000px, max-height:500px' data-theme='a' data-overlay-theme='b'><a href='#' data-rel='back' data-role='button' data-theme='a' data-icon='delete' data-iconpos='notext' class='ui-btn-right'>关闭</a><p>"+v.itemCkwt+"</p></div>";
										}
										cjx_lis += "</td>";
									    cjx_lis += "<td width='15px'></td>";
										cjx_lis += "<td style='vertical-align:middle;'>";
										if(v.itemMsff!="" && v.itemMsff!=null) {
											cjx_lis += "<a  href='#w_ms_" + v.itemId + "'  data-rel='popup' class='ui-link'>面试方法</a><div  data-role='popup'  id='w_ms_" + v.itemId + "'  class='ui-content' style='max-width:1000px, max-height:500px' data-theme='a' data-overlay-theme='b'><a href='#' data-rel='back' data-role='button' data-theme='a' data-icon='delete' data-iconpos='notext' class='ui-btn-right'>关闭</a><p>" + v.itemMsff + "</p></div>";
										}
										cjx_lis += "</td>";
										cjx_lis += "<td width='15px'></td>";
										cjx_lis += "<td style='vertical-align:middle;'>";
										if(v.itemCkzl!="" && v.itemCkzl!=null) {
											var ckzl_content_url = ContextPath + "/refMaterial/onload?classId=" + TZ_CLASS_ID + "&batchId=" + TZ_APPLY_PC_ID + "&appInsId=" + bmbId + "&model="+ v.scoreModelId + "&cjxId=" + v.itemId;
											//var ckzl_content_url = ContextPath + "/refMaterial/onload?classId=122&batchId=47&appInsId=200001&model=TZ_CLPS_MODEL&cjxId=XXHDJL";
											var ckzl_content = "<iframe src='"+ ckzl_content_url +"' frameborder='0' width='820' height='552'></iframe>";
											cjx_lis += "<a  href='#w_ck_" + v.itemId + "'  data-rel='popup' class='ui-link'>参考资料</a><div  data-role='popup'  id='w_ck_" + v.itemId + "'  class='ui-content' style='max-width:1000px, max-height:500px' data-theme='a' data-overlay-theme='b'><a href='#' data-rel='back' data-role='button' data-theme='a' data-icon='delete' data-iconpos='notext' class='ui-btn-right'>关闭</a><p>" + ckzl_content + "</p></div>";
										}
										cjx_lis += "</td>";
									} else if(v.itemType="C") {
										cjx_lis += "<td style='vertical-align:middle;'>(" + v.itemCommentLowerLimit + "-" + v.itemCommentUpperLimit + ")</td>";
										cjx_lis += "<td width='15px'></td>";
										cjx_lis += "<td></td>";
										cjx_lis += "<td width='15px'></td>";
										cjx_lis += "<td style='vertical-align:middle;'>" ;
										if(v.itemCkwt!="" && v.itemCkwt!=null) {
											cjx_lis += "<a href='#c_sm_" + v.itemId + "' data-rel='popup' class='ui-link'>说明</a><div  data-role='popup'  id='c_sm_" + v.itemId + "' class='ui-content' style='max-width:1000px, max-height:500px' data-theme='a' data-overlay-theme='b'><a href='#' data-rel='back' data-role='button' data-theme='a' data-icon='delete' data-iconpos='notext' class='ui-btn-right'>关闭</a><p>" + v.itemCkwt + "</p></div>";
										}
										cjx_lis += "</td>";
										cjx_lis += "<td width='15px'></td>";
										cjx_lis += "<td></td>";
									}
								} else {
									cjx_lis += "<td></td>";
								}
								
								cjx_lis += "</tr>";
								//用于cache数据
								
							});
							
							$("#ks_dfq_cjx").html("");
							
							$("#ks_dfq_cjx").append(cjx_lis).trigger("create"); 
							
							$('input[type="range"]').slider();
							
							dfq_arr.push(bmb_cache_id); 
	
							//保存成绩项id
						    document.getElementById("tz_cjx_ids").value = tz_cjx_id_value;	

		        		} else {
		        			alert(message);
		        		}
		        	
		        	
		        		/*
		        		//新开窗口显示报名表信息
						$("#ks_new_w_bmb").attr({href:data.ps_ksbmb_url});
		        
						document.getElementById("ks_dfq_ms_id").innerHTML = data.ps_ksh_msid;
						document.getElementById("ks_dfq_ms_name").innerHTML = data.ps_ksh_xm;
						document.getElementById("ps_clpscj_pjf").innerHTML = data.ps_clpscj_pjf;
						
						//用于cache数据
						bmb_cache_id = "ks_search_tz_app_ins_id@@@" + KSH_BMBID + "###ks_new_w_bmb@@@" +data.ps_ksbmb_url + "###TZ_dialog2@@@" + data.ps_ckao_info + "###ks_dfq_ms_id@@@" + data.ps_ksh_msid + "###ks_dfq_ms_name@@@" + data.ps_ksh_xm + "###ps_clpscj_pjf@@@" + data.ps_clpscj_pjf; 
			        
						//li 列表项目 
						var cjx_lis=""; 　　　

						var tz_cjx_id_value = "";
					
						$.each(data.ps_dfq_content, function(k, v) {
							//拼装成绩项id
							if (tz_cjx_id_value ==""){
								tz_cjx_id_value = v.ps_item_id + "," + v.ps_item_parentid + "," + v.ps_item_stype + "," + v.ps_item_pyzsxx + "," + v.ps_item_pyzs + "," + v.ps_item_label + "," + v.ps_item_limited1 + "," + v.ps_item_limited2;
							}else{
								tz_cjx_id_value = tz_cjx_id_value + ";" + v.ps_item_id + "," + v.ps_item_parentid + "," + v.ps_item_stype + "," + v.ps_item_pyzsxx + "," + v.ps_item_pyzs + "," + v.ps_item_label + "," + v.ps_item_limited1 + "," + v.ps_item_limited2;
							}
							
							//用于cache数据
							bmb_cache_id = bmb_cache_id + "###" +v.ps_item_id + "@@@" + v.ps_item_value;

							cjx_lis += "<tr>";
		 					cjx_lis +=  "<div data-role='fieldcontain' class='ui-field-contain ui-body ui-br'> ";
		 					
		 					//根据层次缩进
							if (v.ps_item_level == 0){
								cjx_lis += "<td><label for='"+  v.ps_item_id +"' style='width:200px' class='ui-input-text ui-slider' id='"+ v.ps_item_id +"-label'>&nbsp" + v.ps_item_label + "</label></td>";
							}else if(v.ps_item_level == 1){
								cjx_lis += "<td><label for='"+  v.ps_item_id +"' style='width:200px' class='ui-input-text ui-slider' id='"+ v.ps_item_id +"-label'>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp" + v.ps_item_label + "</label></td>";
							} else if(v.ps_item_level == 2){
								cjx_lis += "<td><label for='"+  v.ps_item_id +"' style='width:200px' class='ui-input-text ui-slider' id='"+ v.ps_item_id +"-label'>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp" + v.ps_item_label + "</label></td>";
							} else if(v.ps_item_level == 3){
								cjx_lis += "<td><label for='"+  v.ps_item_id +"' style='width:200px' class='ui-input-text ui-slider' id='"+ v.ps_item_id +"-label'>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp" + v.ps_item_label + "</label></td>";
							} else if(v.ps_item_level == 4){
								cjx_lis += "<td><label for='"+  v.ps_item_id +"' style='width:200px' class='ui-input-text ui-slider' id='"+ v.ps_item_id +"-label'>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp" + v.ps_item_label + "</label></td>";
							}
		 					
							//根据是否只读展现不同的形式
							if (v.ps_item_stype =="R") {
								cjx_lis += 	"<td><input type='text'style='width: 100px; height: 20px; background:#FFFF99' readonly='true'  class='ui-input-text ui-body-c ui-corner-all ui-shadow-inset ui-slider-input' name='" +  v.ps_item_id + "' id='" +  v.ps_item_id + "' value='" + v.ps_item_value  +  "' min='" + v.ps_item_limited1  +  "' max='" + v.ps_item_limited2  +  "' /></td>";
							}else if (v.ps_item_stype =="W"){
								cjx_lis += 	"<td width='410px'><input type='number' onchange='tz_parent_id_tmp(this);' style='width: 100px; height: 20px;' class='ui-input-text ui-body-c ui-corner-all ui-shadow-inset ui-slider-input' data-highlight='true' name='" +  v.ps_item_id + "' id='" +  v.ps_item_id + "' value='" + v.ps_item_value  +  "' min='" + v.ps_item_limited1  +  "' max='" + v.ps_item_limited2  +  "' /></td>";
							}else if (v.ps_item_stype =="C"){
								//解决页面中换行出现反斜杠n 的情况
								var newstr = "";
								newstr = v.ps_item_value.replace("\\n","\n");
								newstr = newstr.replace("\\n","\n");
								newstr = newstr.replace("\\n","\n");
								newstr = newstr.replace("\\n","\n");
								newstr = newstr.replace("\\n","\n");
								newstr = newstr.replace("\\n","\n");
								newstr = newstr.replace("\\n","\n");
								newstr = newstr.replace("\\n","\n");
								newstr = newstr.replace("\\n","\n");
								newstr = newstr.replace("\\n","\n");
								newstr = newstr.replace("\\n","\n");
								newstr = newstr.replace("\\n","\n");
								newstr = newstr.replace("\\n","\n");
								newstr = newstr.replace("\\n","\n");
								newstr = newstr.replace("\\n","\n");

 								cjx_lis += 	"<td><textarea  style='width: 500px; margin: 8px 0px; height: 66px; ' rows='10' class='ui-input-text ui-body-c ui-corner-all ui-shadow-inset' name='" +  v.ps_item_id + "' id='" +  v.ps_item_id + "' value='" + v.ps_item_value  +  "' min='" + v.ps_item_pyzsxx  +  "' max='" + v.ps_item_pyzs  +  "'>"+newstr+"</textarea></td>";
							}
						
							cjx_lis += 	"</div>";
							
							//加后面的滑动条
							if (v.ps_item_stype =="W"){
									//cjx_lis += 	"<td><div role='application' class='ui-slider  ui-btn-down-c ui-btn-corner-all'><a href='#' class='ui-slider-handle ui-btn ui-shadow ui-btn-corner-all ui-btn-up-c' data-corners='true' data-shadow='true' data-iconshadow='true' data-wrapperels='span' data-theme='c' role='slider' aria-valuemin='40' aria-valuemax='100' aria-valuenow='45' aria-valuetext='45' title='45' aria-labelledby='" + v.ps_item_id + "-label' style='left: 45%; '><span class='ui-btn-inner ui-btn-corner-all'><span class='ui-btn-text'></span></span></a></div></td>";
							}
						
							//在节点为叶子节点时，评议类和数据类区别提示
							if(v.ps_item_leaf =="Y"){
								if(v.ps_item_stype =="C"){
				  		        	cjx_lis += "<td>(" + v.ps_item_pyzsxx + "-" + v.ps_item_pyzs + ")</td>";
									cjx_lis += "<td width='30px'></td>";
									cjx_lis += "<td></td>";
									cjx_lis += "<td width='30px'></td>";
									// cjx_lis += "<td><a href='#c_sm_"+v.ps_item_id+"' data-rel='popup' class='ui-link'>说明</a><div  data-role='popup'  id='c_sm_"+v.ps_item_id+"'><p>"+v.ps_item_ckwt+"</p></div></td>";
									cjx_lis += "<td><a href='#c_sm_"+v.ps_item_id+"' data-rel='popup' class='ui-link'>说明</a><div  data-role='popup'  id='c_sm_"+v.ps_item_id+"' class='ui-content' style='max-width:1000px, max-height:1500px' data-theme='c' data-overlay-theme='a'><a href='#' data-rel='back' data-role='button' data-theme='a' data-icon='delete' data-iconpos='notext' class='ui-btn-right'>关闭</a><p>"+v.ps_item_ckwt+"</p></div></td>";
									cjx_lis += "<td width='30px'></td>";
									cjx_lis += "<td></td>";
								} else if(v.ps_item_stype =="W") {
									cjx_lis += "<td>(" + v.ps_item_limited1 + "-" + v.ps_item_limited2 + ")</td>";
									cjx_lis += "<td width='30px'></td>";
									//cjx_lis += "<td><a  href='#w_bz_"+v.ps_item_id+"'  data-rel='popup' class='ui-link'>标准</a><div  data-role='popup'  id='w_bz_"+v.ps_item_id+"'><p>"+v.ps_item_bzsm+"</p></div></td>";
									cjx_lis += "<td><a  href='#w_bz_"+v.ps_item_id+"'  data-rel='popup' class='ui-link'>标准</a><div  data-role='popup'  id='w_bz_"+v.ps_item_id+"'  class='ui-content' style='max-width:1000px, max-height:1500px' data-theme='c' data-overlay-theme='a'><a href='#' data-rel='back' data-role='button' data-theme='a' data-icon='delete' data-iconpos='notext' class='ui-btn-right'>关闭</a><p>"+v.ps_item_bzsm+"</p></div></td>";
									cjx_lis += "<td width='30px'></td>";
									//cjx_lis += "<td><a  href='#w_sm_"+v.ps_item_id+"'  data-rel='popup' class='ui-link'>说明</a><div   data-role='popup'   id='w_sm_"+v.ps_item_id+"'><p>"+v.ps_item_ckwt+"</p></div></td>";
									cjx_lis += "<td><a  href='#w_sm_"+v.ps_item_id+"'  data-rel='popup' class='ui-link'>说明</a><div   data-role='popup'   id='w_sm_"+v.ps_item_id+"' class='ui-content' style='max-width:1000px, max-height:1500px' data-theme='c' data-overlay-theme='a'><a href='#' data-rel='back' data-role='button' data-theme='a' data-icon='delete' data-iconpos='notext' class='ui-btn-right'>关闭</a><p>"+v.ps_item_ckwt+"</p></div></td>";
									cjx_lis += "<td width='30px'></td>";
									//cjx_lis += "<td><a  href='#w_ms_"+v.ps_item_id+"'  data-rel='popup' class='ui-link'>面试方法</a><div  data-role='popup'  id='w_ms_"+v.ps_item_id+"'><p>"+v.ps_item_msff+"</p></div></td>";
								    cjx_lis += "<td><a  href='#w_ms_"+v.ps_item_id+"'  data-rel='popup' class='ui-link'>面试方法</a><div  data-role='popup'  id='w_ms_"+v.ps_item_id+"'  class='ui-content' style='max-width:1000px, max-height:1500px' data-theme='c' data-overlay-theme='a'><a href='#' data-rel='back' data-role='button' data-theme='a' data-icon='delete' data-iconpos='notext' class='ui-btn-right'>关闭</a><p>"+v.ps_item_msff+"</p></div></td>";
								}
							} else {
								cjx_lis += "<td></td>";
							}
						
							cjx_lis += "</tr>";
							//用于cache数据
		 					
						 });
					
						$("#ks_dfq_cjx").html("");

						$("#ks_dfq_cjx").append(cjx_lis).trigger("create"); 
						
						$('input[type="number"]').slider();
						
						dfq_arr.push(bmb_cache_id); 

						//保存成绩项id
					    document.getElementById("tz_cjx_ids").value = tz_cjx_id_value;	
						*/
			        }
			        );

		}
			//没有cache结束
	} else {
		$("#div1").attr({style:"display:none"});
		$("#div2").attr({style:"display:none"});
		$("#div3").attr({style:"display:none"});

		alert("请选择考生，点击“进行评审”按钮，进行评审！");
	}

	sf_qk_fjd = "Y";

}
//显示考生的打分区结束

//保存功能开始
function ks_save(){
	ks_check_pysj();
	
	if (tz_pysjx_failure == "Y"){
		alert(tz_save_show_info);
	}else {
		var TZ_CLASS_ID = ClassId;
		var TZ_APPLY_PC_ID = BatchId;
        var KSH_BMBID = AppInsId;
        var cjx_ids_tmp = document.getElementById("tz_cjx_ids").value;

      	//参数准备
		var tz_canshu_str = "";	

		var arr = cjx_ids_tmp.split(";");
		for (var i=0 ; i< arr.length ; i++){
			var dange_cjx_id = arr[i];
			var dange_arr = dange_cjx_id.split(",");
			if(tz_canshu_str!="") {
				tz_canshu_str += ',"'+ dange_arr[0] +'":"'+ $("#"+dange_arr[0]).val() +'"';
			} else {
				tz_canshu_str = '"'+ dange_arr[0] +'":"'+ $("#"+dange_arr[0]).val() +'"';
			}
		}
		var saveData = '{"ClassID":"'+TZ_CLASS_ID+'","BatchID":"'+TZ_APPLY_PC_ID+'","KSH_BMBID":"'+KSH_BMBID+'","OperationType":"SBM",'+ tz_canshu_str +'}';
		
		var comParams = "";
		comParams = '"update":['+saveData+']';
		
		var tzParams = '{"ComID":"TZ_EVA_INTERVIEW_COM","PageID":"TZ_MSPS_DF_STD","OperateType":"U","comParams":{'+comParams+'}}';

		showLoaderThis();
		$("#ks_save_bt").attr("onclick", "");

		$.ajax({
	  		type: 'POST',
	  		dataType: 'json',
			async:false,
	  		url: scoreUrl,
	  		data: {"tzParams":tzParams},
	  		beforeSend : function(xhr){
				//showLoaderThis();
				//$("#ks_save_bt").attr('disabled',true);
			},
	  		success: function(data) {
				var comContent = data.comContent;
				var messageCode = comContent.messageCode;
				if(messageCode =="0"){
					tz_save_cache();

					ks_show_df_info(TZ_CLASS_ID,TZ_APPLY_PC_ID,KSH_BMBID);
					alert("保存成功！");
				}else{
					alert(comContent.message);
				}
	        },
			complete : function(){
				hideLoaderThis();
				$("#ks_save_bt").attr("onclick", "ks_save();");
			}
		});
		
		/*
		//参数准备
		var tz_canshu_str = "";	
		tz_canshu_str = "BaokaoFXID=" + TZ_APPLY_PC_ID + "&KSH_BMBID=" + KSH_BMBID;

		var arr = cjx_ids_tmp.split(";");
		for (var i=0 ; i< arr.length ; i++){
			var dange_cjx_id = arr[i];
			var dange_arr = dange_cjx_id.split(",");
			tz_canshu_str = tz_canshu_str + "&" + dange_arr[0] + "=" + $("#"+dange_arr[0]).val();
		}
		
		$.ajax({
	  		type: 'POST',
			async:false,
	  		url: encodeURI("%bind(:12)&"+tz_canshu_str),
	  		data: {},
			beforeSend : function(xhr){
				$.mobile.showPageLoadingMsg();	
				if($('.page-loading').length ==0){
				var dv =	$('<div/>',{class : 'page-loading'})
			    $(document.body).append(dv);
				}
				$('.page-loading').show();
			},
	  		success: function(msg) {
				if(msg.error_code =="0"){
					tz_save_cache();

					ks_show_df_info(TZ_CLASS_ID,TZ_APPLY_PC_ID,KSH_BMBID);
					alert("保存成功！");
				}else{
					alert(msg.error_decription);
				}
	        },
			complete : function(){
				$.mobile.hidePageLoadingMsg()
				$('.page-loading').hide();
			},
	  		dataType: "json"
		}); 
		*/
	}
}
//保存功能结束

//修改时清空父节点的值开始1
function tz_parent_id_tmp(dq_cjx){
	if(sf_qk_fjd == "N"){
	}else{
		dq_cjx_tmp = dq_cjx.id;
		tz_parent_id(dq_cjx_tmp);
	}
 }
//修改时清空父节点的值结束1

//修改时清空父节点的值开始2
function tz_parent_id(dq_cjx){
	var str = $("#tz_cjx_ids").val();
	
	var arr = str.split(";");
	for (var i = 0 ; i < arr.length; i++){
		var arr_cjx = arr[i].split(",");

		if (dq_cjx == arr_cjx[0] ) {
			if (arr_cjx[1] == ""){
				document.getElementById(arr_cjx[1]).value = "--";
			}else{
				document.getElementById(arr_cjx[1]).value = "--";
				
				var parent = arr_cjx[1];
				for (var j = 0 ; j < arr.length; j++){
					var arr_cjx_2 = arr[j].split(",");
					if (arr_cjx_2[0] == parent){
						if(arr_cjx_2[1] ==""){
						}else{
							document.getElementById(arr_cjx_2[1]).value = "--";
						}
					}
				}
			}
		}
	}	
}
//修改时清空父节点的值结束2

//检查评议数据项字数限制开始
// 判断是否有字数不合法的
var tz_pysjx_failure = "N";
// 错误的提示信息
var tz_save_show_info = "";
function ks_check_pysj(){
	//清空提示信息
	tz_save_show_info = "";
	tz_pysjx_failure = "N";
	
	var tz_cjx_ids_tmp = "";
	tz_cjx_ids_tmp = document.getElementById("tz_cjx_ids").value;

	var arr_tz_cjx_ids_1 = tz_cjx_ids_tmp.split(";");
	
	for (var cjx_i = 0 ; cjx_i < arr_tz_cjx_ids_1.length; cjx_i++){
		if(tz_pysjx_failure == "Y"){
			
		}else{
			var arr_tz_cjx_ids_2 = arr_tz_cjx_ids_1[cjx_i].split(",");

			if (arr_tz_cjx_ids_2[2] == "C"){
				var tmp_cjx_value = "";
				var tmp_len = 0;
				tmp_cjx_value = document.getElementById(arr_tz_cjx_ids_2[0]).value;

				tmp_len = TZ_GetLength (tmp_cjx_value);

				if (tmp_len < arr_tz_cjx_ids_2[3]){
					tz_save_show_info = arr_tz_cjx_ids_2[5] + "：字数范围不正确！";
					tz_pysjx_failure = "Y";
				}else if(tmp_len > arr_tz_cjx_ids_2[4]){
					tz_save_show_info = arr_tz_cjx_ids_2[5] + "：字数范围不正确！";
					tz_pysjx_failure = "Y";
				}
			} else if(arr_tz_cjx_ids_2[2] == "B") {

				var tmp_num_cjx_value = 0;
				tmp_num_cjx_value = Number(document.getElementById(arr_tz_cjx_ids_2[0]).value);

				if (tmp_num_cjx_value < Number(arr_tz_cjx_ids_2[6])){
					tz_save_show_info = arr_tz_cjx_ids_2[5] + "：数值范围不正确！";
					tz_pysjx_failure = "Y";
				}else if(tmp_num_cjx_value > Number(arr_tz_cjx_ids_2[7])){
					tz_save_show_info = arr_tz_cjx_ids_2[5] + "：数值范围不正确！";
					tz_pysjx_failure = "Y";
				}
			}
		}
	}
}
//检查评议数据项字数限制结束

//获取字符串的长度开始
function TZ_GetLength (str) {
	//获得字符串实际长度，中文2，英文1
	var realLength = 0, len = str.length, charCode = -1;
	for (var i = 0; i < len; i++) {
    	charCode = str.charCodeAt(i);
    	if (charCode >= 0 && charCode <= 128) realLength += 1;
    	else realLength += 2;
	}
	return realLength;
}
//获取字符串的长度结束

//处理cache开始
function tz_save_cache(){
	var KSH_BMBID = AppInsId;

	//把原先的删除
	var del_i = 9999;
	for(var dfq_i= 0;dfq_i < dfq_arr.length;dfq_i++) {
		var dfq_geren_info = "";//取数组每个人员
		dfq_geren_info = dfq_arr[dfq_i];

		var dfq_gr_arr = dfq_geren_info.split("###");//取数组每个成绩项
		var bmbid_info = "";
		bmbid_info = dfq_gr_arr[0];
		var dfq_bmbid_arr = bmbid_info.split("@@@");

		if(dfq_bmbid_arr[1] == KSH_BMBID){
			del_i = dfq_i;
		}
	}
	dfq_arr.splice(del_i,1);
}
//处理cache结束

//返回评审界面开始
function ks_back(){
   window.open("index?page=batch&classId="+ClassId+"&batchId="+BatchId,'_self');
}
//返回评审界面结束

//返回首页开始
function ks_first_page(){
   window.open("index",'_self');
}
//返回首页结束

//退出开始
function ks_esc(){
   window.open('../../logout?type=interview','_self');
}
//退出结束


//显示加载器开始
function showLoaderThis() {
	$.mobile.loading('show', {
		text: '加载中，请稍候...', //加载器中显示的文字
		textVisible: true, //是否显示文字
		theme: 'a',        //加载器主题样式
		textonly: false,   //是否只显示文字
		html: ""           //要显示的html内容，如图片等
	});
}
//显示加载器结束


//隐藏加载器开始
function hideLoaderThis() {
	$.mobile.loading('hide');
}
//隐藏加载器结束

