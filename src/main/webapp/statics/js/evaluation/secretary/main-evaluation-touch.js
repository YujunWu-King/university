var baseUrl = ContextPath+"/dispatcher?classid=secretary&OperateType=EJSON";

$(document).ready(function() {
	showLoader();
	var groupId = $("#msGroup option:selected").text();
	/****解析json数据***/
	$.getJSON(baseUrl+'&type=data&BaokaoClassID='+ClassId+'&BaokaoPCID='+BatchId+'&groupId='+groupId+'&RequestDataType=A&MaxRowCount=200',function (data) { 

		$("#description").html(data.comContent.ps_description);
		var jsonObject = data.comContent;

		useJson(jsonObject);
   		hideLoader();
	});  

}); 

function loadData(){
	showLoader();
	var groupId = $("#msGroup option:selected").text();
	/****解析json数据***/
	$.getJSON(baseUrl+'&type=data&BaokaoClassID='+ClassId+'&BaokaoPCID='+BatchId+'&groupId='+groupId+'&RequestDataType=A&MaxRowCount=200',function (data) { 

		$("#description").html(data.comContent.ps_description);
		var jsonObject = data.comContent;

		useJson(jsonObject);
   		hideLoader();
	});  
}

function goMainpage(){
	window.open('index','_self');
}

//显示加载器
function showLoader() {  
    $.mobile.loading('show', {  
        text: '加载中，请稍候...', //加载器中显示的文字    
        textVisible: true, //是否显示文字    
        theme: 'a',        //加载器主题样式 
        textonly: false,   //是否只显示文字    
        html: ""           //要显示的html内容，如图片等    
    });  
}  
//隐藏加载器
function hideLoader() {  
    $.mobile.loading('hide');  
}  
function refreshKslist(){
	var groupId = $("#msGroup option:selected").text();
		$.getJSON(
				baseUrl+'&type=data&BaokaoClassID='+ClassId+'&BaokaoPCID='+BatchId+'&groupId='+groupId+'&RequestDataType=S&MaxRowCount=200',
				function(data){
					var jsonObject = data.comContent; 
					useJson(jsonObject);
				});

}

function useJson(varjsonData){
	var data=varjsonData;
	var jsonObject = varjsonData; 

	var charts_arr0 = [];
	var charts_arr = [];

	var yAxisArr=[];
	var yAxisArrName=[];
	var varArr = [];
	var legendArr = [];
	var xAxisArr=[];
	var series2Name="";


	$("#ypsinfo").html(data.ps_gaiy_info);
	if(data.ps_kslb_submtall=="Y"){
		 $("#submtall_flag").html("已提交");
	}
	else if(data.ps_kslb_submtall=="N"){
		 $("#submtall_flag").html("未提交");
	}
	else if(data.ps_kslb_submtall=="C"){
		 $("#submtall_flag").html("未提交");
	}
	else
	{
		$("#submtall_flag").html("未提交");
	}


	//选择面试组
	/*
	var msGroupHtml = "<select id='msGroup' name='msGroup' style='width: 150px;'>";
	var msGroupArray = jsonObject['ps_ms_group'];
	for(var m=0;m<msGroupArray.length;m++) {
		msGroupHtml += "<option value='"+ msGroupArray[m]['msGroupId'] +"'>"+ msGroupArray[m]['msGroupName'] +"</option>"
	}
	msGroupHtml += "</select>";
	$("#msGroupSpan").html(msGroupHtml);

	var firstValue= "";
	var msGroupOption = "";
	var msGroupArray = jsonObject['ps_ms_group'];
	for(var m=0;m<msGroupArray.length;m++) {
		if(m==0) {
			firstValue = msGroupArray[m]['msGroupId'];
		}
		msGroupOption += "<option value='"+ msGroupArray[m]['msGroupId'] +"'>"+ msGroupArray[m]['msGroupName'] +"</option>"
	}
	$("#msGroup").append(msGroupOption);
	*/
	//$("#msGroup option[value='"+ firstValue+"'] ").attr("selected",true);

	//考生列表
	var collist="";
	var ksheadObject=jsonObject['ps_data_kslb']['ps_ksh_list_headers'];
	
	var m = 0;
	/*for(var i in ksheadObject) {
		m++;
		var colName = '00' + m;
		colName = 'col' + colName.substr(colName.length - 2);
		collist+='<th>'+ksheadObject[colName]+'</th>';
    }*/
	
	var ksbodyArray=jsonObject['ps_data_kslb']['ps_ksh_list_contents'];

	var detallist="";
	for (var i=0;i<ksbodyArray.length;i++){		
		var concollist="";
		
		var n = 0;
		for(var j in ksheadObject) {
			n++;
			var colName = '00' + n;
			colName = 'col' + colName.substr(colName.length - 2);
			concollist+="<td class='alt' >"+(ksbodyArray[i][colName]||"")+'</td>';
	    }
		var ps_ksh_bmbid = ksbodyArray[i]['ps_ksh_bmbid'];
		//面试申请号
		var ps_msh_id = ksbodyArray[i]['ps_msh_id'];
		var name = ksbodyArray[i]['ps_ksh_xm'];
		detallist+="<tr id='"+ksbodyArray[i]['ps_ksh_bmbid']+"' style='text-align: center;'>";
		detallist+="<td  class='alt' style='border-left: 1px solid #c1dad7;'>"+ksbodyArray[i]['ps_ksh_xh']+"</td>";
		//detallist+="<td  class='alt' style='border-left: 1px solid #c1dad7;'>"+ksbodyArray[i]['ps_ksh_bmbid']+"</td>";
		detallist+="<td  class='alt' style='border-left: 1px solid #c1dad7;'>"+ksbodyArray[i]['ps_ksh_xm']+"</td>";
		detallist+="<td  class='alt' style='border-left: 1px solid #c1dad7;'>"+ps_msh_id+"</td>";
		//detallist+="<td  class='alt' style='border-left: 1px solid #c1dad7;'>"+ksbodyArray[i]['ps_ksh_sex']+"</td>";
		//detallist+=concollist + "</td>";
		detallist+="</td>";
		//detallist+="</td>"+"<td  class='alt'>"+(ksbodyArray[i]['ps_ksh_zt']||"")+"</td>";
		//detallist+="<td  class='alt'>"+ksbodyArray[i]['ps_ksh_dt']+"</td>";
		var ps_mszt = ksbodyArray[i]['ps_mszt'];
		var ps_mszt2;
		if(ps_mszt == '0'){
			ps_mszt2 = '<p style="color:red">未面试</p>'
			detallist+="<td  class='alt'>"+ps_mszt2+"</td>";
			detallist+="<td class='alt' style='padding:0'><a data-icon='arrow-r' data-mini='true' data-inline='true' data-role='button' data-ajax='false' id='favrecipelink' href='javascript:void(0);' onclick=startMs(" +ps_ksh_bmbid+",'"+name+"',"+ps_mszt+") data-corners='true' data-shadow='true'data-iconshadow='true' data-wrapperels='span' data-theme='a' class='ui-btn ui-shadow ui-icon-arrow-r ui-btn-corner-all ui-mini ui-btn-inline ui-btn-up-c'><span class='ui-btn-inner ui-btn-corner-all ui-corner-top ui-corner-bottom'><span class='ui-btn-text'>开始面试</span></span></a></td>";
			detallist+="<td class='alt' style='padding:0'><a data-icon='arrow-r' data-mini='true' data-inline='true' data-role='button' data-ajax='false' id='favrecipelink' href='javascript:void(0);' onclick='endMs(" +ps_ksh_bmbid+","+ps_mszt+")' data-corners='true' data-shadow='true'data-iconshadow='true' data-wrapperels='span' data-theme='a' class='ui-btn ui-shadow ui-icon-arrow-r ui-btn-corner-all ui-mini ui-btn-inline ui-btn-up-c'><span class='ui-btn-inner ui-btn-corner-all ui-corner-top ui-corner-bottom'><span class='ui-btn-text' style='color:#999'>结束面试</span></span></a></td>";
		}else if(ps_mszt == '1'){
			ps_mszt2 = '进行中'
			detallist+="<td  class='alt'>"+ps_mszt2+"</td>";
			detallist+="<td class='alt' style='padding:0'><a data-icon='arrow-r' data-mini='true' data-inline='true' data-role='button' data-ajax='false' id='favrecipelink' href='javascript:void(0);' onclick=startMs(" +ps_ksh_bmbid+",'"+name+"',"+ps_mszt+") data-corners='true' data-shadow='true'data-iconshadow='true' data-wrapperels='span' data-theme='a' class='ui-btn ui-shadow ui-icon-arrow-r ui-btn-corner-all ui-mini ui-btn-inline ui-btn-up-c'><span class='ui-btn-inner ui-btn-corner-all ui-corner-top ui-corner-bottom'><span class='ui-btn-text' style='color:#999'>开始面试</span></span></a></td>";
			detallist+="<td class='alt' style='padding:0'><a data-icon='arrow-r' data-mini='true' data-inline='true' data-role='button' data-ajax='false' id='favrecipelink' href='javascript:void(0);' onclick='endMs(" +ps_ksh_bmbid+","+ps_mszt+")' data-corners='true' data-shadow='true'data-iconshadow='true' data-wrapperels='span' data-theme='a' class='ui-btn ui-shadow ui-icon-arrow-r ui-btn-corner-all ui-mini ui-btn-inline ui-btn-up-c'><span class='ui-btn-inner ui-btn-corner-all ui-corner-top ui-corner-bottom'><span class='ui-btn-text'>结束面试</span></span></a></td>";
		}else{
			ps_mszt2 = '<p style="color:green">已面试</p>'
			detallist+="<td  class='alt'>"+ps_mszt2+"</td>";
			detallist+="<td class='alt' style='padding:0'><a data-icon='arrow-r' data-mini='true' data-inline='true' data-role='button' data-ajax='false' id='favrecipelink' href='javascript:void(0);' onclick=startMs(" +ps_ksh_bmbid+",'"+name+"',"+ps_mszt+") data-corners='true' data-shadow='true'data-iconshadow='true' data-wrapperels='span' data-theme='a' class='ui-btn ui-shadow ui-icon-arrow-r ui-btn-corner-all ui-mini ui-btn-inline ui-btn-up-c'><span class='ui-btn-inner ui-btn-corner-all ui-corner-top ui-corner-bottom'><span class='ui-btn-text' style='color:#999'>开始面试</span></span></a></td>";
			detallist+="<td class='alt' style='padding:0'><a data-icon='arrow-r' data-mini='true' data-inline='true' data-role='button' data-ajax='false' id='favrecipelink' href='javascript:void(0);' onclick='endMs(" +ps_ksh_bmbid+","+ps_mszt+")' data-corners='true' data-shadow='true'data-iconshadow='true' data-wrapperels='span' data-theme='a' class='ui-btn ui-shadow ui-icon-arrow-r ui-btn-corner-all ui-mini ui-btn-inline ui-btn-up-c'><span class='ui-btn-inner ui-btn-corner-all ui-corner-top ui-corner-bottom'><span class='ui-btn-text' style='color:#999'>结束面试</span></span></a></td>";
		}
		detallist+="<td class='alt' style='padding:0'><a data-icon='arrow-r' data-mini='true' data-inline='true' data-role='button' data-ajax='false' id='favrecipelink' href='javascript:void(0);' onclick='viewBmb(" +ps_ksh_bmbid+")' data-corners='true' data-shadow='true'data-iconshadow='true' data-wrapperels='span' data-theme='a' class='ui-btn ui-shadow ui-icon-arrow-r ui-btn-corner-all ui-mini ui-btn-inline ui-btn-up-c'><span class='ui-btn-inner ui-btn-corner-all ui-corner-top ui-corner-bottom'><span class='ui-btn-text'>查看考生资料</span></span></a></td>";
		if(GroupLeader=="Y") {
			//组长，可看其他评委打分
			var ksh_bmbid = ksbodyArray[i]['ps_ksh_bmbid'];
			detallist+="<td class='alt' style='padding:0'><a data-icon='arrow-r' data-mini='true' data-inline='true' data-role='button' data-ajax='false' id='favrecipelink' href='javascript:void(0);' onclick='viewOtherJudge(" +ksh_bmbid+")' data-corners='true' data-shadow='true'data-iconshadow='true' data-wrapperels='span' data-theme='a' class='ui-btn ui-shadow ui-icon-arrow-r ui-btn-corner-all ui-mini ui-btn-inline ui-btn-icon-left ui-btn-up-c'><span class='ui-btn-inner ui-btn-corner-all ui-corner-top ui-corner-bottom'><span class='ui-btn-text'>查看</span></span></a></td>";
		}

	}

	 //列名
	 var examerHeaderlist="<th scope='col'  style='text-align:center;'>序号</th>";
	 	examerHeaderlist += "<th scope='col'  style='text-align:center;'>姓名</th>";
	 	examerHeaderlist += "<th scope='col'  style='text-align:center;'>面试申请号</th>";

	 var headbutton = "<th scope='col'  style='text-align:center;'>面试状态</th>";
	 headbutton +="<th scope='col'  style='text-align:center;'>开始面试</th>";
	 headbutton+="<th scope='col'  style='text-align:center;'>结束面试</th>";
	 headbutton+="<th scope='col'  style='text-align:center;'>查看考生资料</th>";
	/* if(GroupLeader=="Y") {
		 //组长，可看其他评委打分
		 headbutton += "<th scope='col'  style='text-align:center;'>"+"其他评委打分"+"</th>";
	 }*/
	 var examerinfolist="<table  id='mytable' cellspacing='0' width='100%' summary='the technical specifications of the apple powermac g5 series'>";
	 examerinfolist += "<tr>"+examerHeaderlist+collist+headbutton+'</tr>'+detallist+'</table>';

	 $("#examerinfo").html(examerinfolist);

}

var str = "";
var hour = '00'; //时  
var minus = '00'; //分  
var seconds = '00'; //秒  
var x = 0,
	y = 0,
	f = 0,
	a = 0,
	b = 0;
var index;
//开始面试
function startMs(appinsId,name,ps_mszt){
	/*console.log(endSeconds)
	console.log(new Date().getTime())
	if((new Date().getTime()-endSeconds) <= 5){
		alert("刚结束面试，请等待五秒再开启下一个面试");
		return;
	}
	endSeconds = 0;*/

	if(ps_mszt == 1 || ps_mszt == 2){
		return;
	}
	/*if(index != undefined){
		alert("请先结束当前面试，再开启下一个面试")
		return;
	}*/
	if($("#timer").attr("style") == 'display: block;'){
		alert("请先结束当前面试，再开启下一个面试")
		return;
	}
	$.ajax({
  		type: 'POST',
  		url: baseUrl+"&type=startMs",
  		data: {"BaokaoClassID":ClassId,"BaokaoPCID":BatchId,"KSH_BMBID":appinsId},
  		success: function(response) {
  			loadData();
  			refreshTimer();
	    },
	  		dataType: "json"
		});
	
	str = name + " 面试时间："
	var t1;
	var html = "<div id='timer' style='background-color: orange;font-size: 24px;height: 50px;line-height:48px'></div>";
	/*index = layer.open({
		  title: '',
		  content: html,
		  shade: 0,
		  closeBtn: 0,
		  skin: 'layui-layer-nobg',
		  shadeClose:true,
		  btn:[],
		  success:function(){
			  $("#timer").html(str + hour + ":" + minus + ":" + seconds);
			  t1=setInterval(beginS,1000);
		  },
		  end:function(){
			  x = 0,
			  y = 0,
			  f = 0,
			  a = 0,
			  b = 0;
			  hour = '00';
			  minus = '00';
			  seconds = '00';
			  clearInterval(t1);
			  index = undefined;
		  }
		});*/
}

function beginS() { // 计算秒
	x++;
	if (x < 10) {
		seconds = '0' + x;
	} else if (x >= 10 && x <= 59) {
		seconds = x;
	} else if (x > 59) {
		seconds = '00';
		x = 0;
		a++;
	}

	if (a < 10) {
		minus = '0' + a;
	} else if (a >= 10 && a <= 59) {
		minus = a;
	} else if (a > 59) {
		minus = '00';
		a = 0;
		b++;
	}

	if (b < 10) {
		hour = '0' + b;
	} else if (b >= 10 && b <= 59) {
		hour = b;
	}
	$("#timer").html(str + hour + ":" + minus + ":" + seconds)  ;
}
//结束面试
function endMs(appinsId,ps_mszt){
	if(ps_mszt == 0 || ps_mszt == 2){
		return;
	}
	layer.close(index);
	$.ajax({
  		type: 'POST',
  		url: baseUrl+"&type=endMs",
  		data: {"BaokaoClassID":ClassId,"BaokaoPCID":BatchId,"KSH_BMBID":appinsId},
  		success: function(response) {
  			loadData();
  			msStart = ""
  			$("#timer").hide();
	    },
	  		dataType: "json"
		});
	
	msStart = "";
	endSeconds = new Date().getTime();
}


//查看考生报名表
function viewBmb(appinsId){
	 var tzParams='{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"HTML","comParams":{"TZ_APP_INS_ID":"'+appinsId+'","isEdit":"N"}}';
     //location.href = "/dispatcher?tzParams="+encodeURIComponent(tzParams);
	 window.open("/dispatcher?tzParams="+encodeURIComponent(tzParams));
}

function getForm(){  
	var thischart = this;
    var data1 =   [34,45,89,15,14,56,39];                                                                                     
    thischart.series[0].setData(data1);	
}  


function refreshChart(){
	for(var i=0; i<charts_arr.length; i++){
		var data = [];
		var randomData = parseInt(Math.random()*100);
		data.push(randomData);
		charts_arr[i].series[0].setData(data);
	}	

	for(var i=0; i<charts_arr0.length; i++){
		var data = [];
		var seriesData = charts_arr0[i].series[0].data;
		for(var j=0; j<seriesData.length; j++){
			var randomData = parseInt(Math.random()*100);
			data.push(randomData);
		}


		charts_arr0[i].series[0].setData(data);
	}
	
}




function getForm2(){  
  
//	alert(data);               
//	thischart.series[0].setData(data);  
//	var thischart2 = this;

}
	//去左右空格;   
	 function trim(s){  
	    
	  return s.replace(/(^\s*)|(\s*$)/, "");
 
	 }
	
	//移除考生;
	function ycks(){
	var bmbid=document.getElementById("tz_hide_bmbid").value;
	
	$.ajax({
  		type: 'POST',
  		url: baseUrl+"&type=remove",
  		data: {"BaokaoClassID":ClassId,"BaokaoPCID":BatchId,"KSH_BMBID":bmbid},
  		success: function(response) {
  			var data = response.comContent;
	 		if (data.error_code=="0"){
				alert("移除成功");
				refreshKslist();
					}
				else{
				alert(data.error_decription);
					}
	        },
	  		dataType: "json"
		}); 

	}

//提示移除考生;
function toycks(sname,bmbid){
	$("#ks_name").html(sname);
	document.getElementById("tz_hide_bmbid").value=bmbid;
	document.getElementById("tz_hide_ksname").value=sname;

	$("#popupDialog").popup('open');
}

//提示提交所有;
function tosubmitall(){
	$("#popupDialogSubmit").popup('open');
}

//提交前校验
function beforeSubmit() {
	$.ajax({
		type: 'POST',
		url: baseUrl+"&type=before",
		data: {"BaokaoClassID":ClassId,"BaokaoPCID":BatchId},
		success: function(response) {
			var data = response.comContent;
			if (data.error_code=="0"){
				//校验成功，打开签名页面
				sign();
			}else{
				alert(data.error_decription);
				if(data.error_cfksid!=undefined){
					for (var i =0 ;i<data.error_cfksid.length;i++){
						$('#'+(data.error_cfksid)[i].ps_ksh_id).children('td').css({color:'red'})
					}
				}
			}
		},
		dataType: "json"
	});
}

//打开签名对话框
function sign(){
	setTimeout("loadSignatureDialog()",500);
}

//加载签名控件
function loadSignatureDialog(){
	$('#signatureDialog').popup('open');
	if($.signatureLoaded==undefined){
		$('#signature').jSignature({width:500,height:200});
		$.signatureLoaded = true;
	}else{
		$('#signature').jSignature("reset");
	}

}

//签名之后确定提交所有;
function submitall(){
	//输出签名图片
	var $sigdiv = $("#signature");
	var datapair = $sigdiv.jSignature("getData", "image");

	if(datapair[1].length<4000){
		alert("请您先签名！");
		return;
	}

	$.ajax({
		type: 'POST',
		url: baseUrl+"&type=submit",
		data: {"BaokaoClassID":ClassId,"BaokaoPCID":BatchId,"Signature":"data:" + datapair[0] + "," + datapair[1]},
		success: function(response) {
			var data = response.comContent;
			if (data.error_code=="0"){
				alert("提交成功");
				refreshKslist();
			}else{
				alert(data.error_decription);
				if(data.error_cfksid!=undefined){
					for (var i =0 ;i<data.error_cfksid.length;i++){
						$('#'+(data.error_cfksid)[i].ps_ksh_id).children('td').css({color:'red'})
					}
				}
			}
		},
		dataType: "json"
	});

	$("#closeSignatureDialog").trigger("click");
}
	

	//查找考生;
	function searchfor(){
	var searchid=document.getElementById("searchfor1").value;
	var searchname=document.getElementById("searchfor2").value;
		searchid=trim(searchid);
		searchname=trim(searchname);
    if (searchid!="" || searchname!=""){
	
	$.ajax({
  		type: 'POST',
  		url: baseUrl+"&type=search",
  		data: {"KSH_SEARCH_MSID":searchid,"KSH_SEARCH_NAME":searchname,"BaokaoClassID":ClassId,"BaokaoPCID":BatchId},
  		success: function(response) {
  			var data = response.comContent;
  			if (data.error_code=="0"){

  				$("#searchnotice").html("<b>查询结果：</b>考生申请号【"+data.ps_ksh_msid+"】，姓名【"+data.ps_ksh_xm+"】");
  				document.getElementById("tz_search_bmbid").value=data.ps_ksh_bmbid;
			}else {
				$("#searchnotice").html(data.error_decription);
				alert(data.error_decription);
				document.getElementById("tz_search_bmbid").value="";
			}
        },
  		dataType: "json"
	}); 

	}
	else
	{
		alert("申请号或姓名至少输入一项！");
	}
	
	}
	
	//进行评审;
	function jxps(){
		var ms_group_id = $("#msGroup option:selected").val();

		if (ms_group_id!=""){
			$.ajax({
				type: 'POST',
				url: baseUrl+"&type=add",
				data: {"MsGroupId":ms_group_id,"BaokaoClassID":ClassId,"BaokaoPCID":BatchId},
				success: function(response) {
					var data = response.comContent;
					if (data.error_code=="0"|| data.error_code =="2"||data.error_decription=="2"){
						var appInsId = data.appInsId;
						var translink="index?page=evaluation&classId="+ClassId+"&batchId="+BatchId+"&msGroupId="+ms_group_id+"&appInsId="+appInsId;
						window.open(translink,'_self');
					} else{
						alert(data.error_decription);
					}
				},
				dataType: "json"
			});
		} else {
			alert("请选择面试组!");
		}
	
	}


//查看其他评委打分
function viewOtherJudge(bmbid) {

	$.ajax({
		type: 'POST',
		url: baseUrl+"&type=other",
		dataType: 'json',
		data: {"BaokaoClassID":ClassId,"BaokaoPCID":BatchId,"appinsId":bmbid},
		success: function(response) {
			var jsonObject = response.comContent;

			openOtherDialog(bmbid,jsonObject);

		}
	});

}


function openOtherDialog(bmbid,jsonObject) {
	//列名
	var qtpwColName = "<th scope='col'  style='text-align:center;'>评委账号</th>";
	qtpwColName += "<th scope='col' style='text-align:center;'>评委姓名</th>";
	qtpwColName += "<th scope='col' style='text-align:center;'>类型</th>";

	var collist="";
	var headObject = jsonObject['other_headers'];
	var m = 0;
	for(var i in headObject) {
		m++;
		var colName = '00' + m;
		colName = 'col' + colName.substr(colName.length - 2);
		collist+='<th>'+headObject[colName]+'</th>';
	}

	qtpwColName += collist;

	//值
	var qtpwValue = "";

	var valueArray = jsonObject['other_contents'];
	for (var i = 0; i < valueArray.length; i++) {

		qtpwValue += "<tr id='" + valueArray[i]['otherPwDlzhId'] + "'>";
		qtpwValue += "<td  class='alt' style='border-left: 1px solid #c1dad7;'>" + valueArray[i]['otherPwDlzhId'] + "</td>";
		qtpwValue += "<td  class='alt' style='border-left: 1px solid #c1dad7;'>" + valueArray[i]['otherPwName'] + "</td>";
		qtpwValue += "<td  class='alt' style='border-left: 1px solid #c1dad7;'>" + valueArray[i]['otherPwTypeDesc'] + "</td>";

		var num = 0;
		for (var k in headObject) {
			num++;
			var colName = '00' + num;
			colName = 'col' + colName.substr(colName.length - 2);
			qtpwValue += "<td class='alt' >" + (valueArray[i][colName] || "") + '</td>';
		}
		qtpwValue += "</tr>";
	}

	var otherScoreDivId = "score" + bmbid;

	if(document.getElementById(otherScoreDivId)!=undefined) {
		$("#"+otherScoreDivId).remove();
	}

	var qtpwScore ='<div id=' + otherScoreDivId + '>';
	qtpwScore += '<div>学生姓名：' + jsonObject['ksh_name'] + '</div>';
	qtpwScore += '<table cellspacing="0" width="100%" summary="the technical specifications of the apple powermac g5 series">';
	qtpwScore += '<tr>' + qtpwColName + '</tr>';
	qtpwScore += qtpwValue;
	qtpwScore += '</table>';
	qtpwScore += "</div>";


	var divId = "score" + bmbid;
	//var popupContent = $("#"+divId).html();

	$("#otherDialog .ui-content").html(qtpwScore);
	$("#otherDialog").popup('open');
}