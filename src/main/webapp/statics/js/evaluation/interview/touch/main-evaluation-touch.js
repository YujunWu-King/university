var baseUrl = ContextPath+"/dispatcher?classid=interview&OperateType=EJSON";

$(document).ready(function() {
	showLoader();
	/****解析json数据***/
	$.getJSON(baseUrl+'&type=data&BaokaoClassID='+ClassId+'&BaokaoPCID='+BatchId+'&RequestDataType=A&MaxRowCount=200',function (data) { 

		$("#description").html(data.comContent.ps_description);
		var jsonObject = data.comContent;

		useJson(jsonObject);
   		hideLoader();
	});  

}); 

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

		$.getJSON(
				baseUrl+'&type=data&BaokaoClassID='+ClassId+'&BaokaoPCID='+BatchId+'&RequestDataType=S&MaxRowCount=200',
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
	var msGroupHtml = "<select id='msGroup' name='msGroup' style='width: 150px;'>";
	var msGroupArray = jsonObject['ps_ms_group'];
	for(var m=0;m<msGroupArray.length;m++) {
		msGroupHtml += "<option value='"+ msGroupArray[m]['msGroupId'] +"'>"+ msGroupArray[m]['msGroupName'] +"</option>"
	}
	msGroupHtml += "</select>";
	$("#msGroupSpan").html(msGroupHtml);

	
	//考生列表
	var collist="";
	var ksheadObject=jsonObject['ps_data_kslb']['ps_ksh_list_headers'];
	
	var m = 0;
	for(var i in ksheadObject) {
		m++;
		var colName = '00' + m;
		colName = 'col' + colName.substr(colName.length - 2);
		collist+='<th>'+ksheadObject[colName]+'</th>';
    }
	
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

		detallist+="<tr id='"+ksbodyArray[i]['ps_ksh_id']+"'>";
		detallist+="<td  class='alt' style='border-left: 1px solid #c1dad7;'>"+ksbodyArray[i]['ps_ksh_xh']+"</td>";
		detallist+="<td  class='alt' style='border-left: 1px solid #c1dad7;'>"+ksbodyArray[i]['ps_ksh_bmbid']+"</td>";
		detallist+="<td  class='alt' style='border-left: 1px solid #c1dad7;'>"+ksbodyArray[i]['ps_ksh_xm']+"</td>";
		detallist+="<td  class='alt' style='border-left: 1px solid #c1dad7;'>"+ksbodyArray[i]['ps_ksh_sex']+"</td>";
		detallist+="<td  class='alt'>"+ksbodyArray[i]['ps_msh_id']+"</td>";
		detallist+=concollist;
		detallist+="</td>"+"<td  class='alt'>"+(ksbodyArray[i]['ps_ksh_zt']||"")+"</td>";
		detallist+="<td  class='alt'>"+ksbodyArray[i]['ps_ksh_dt']+"</td>";
		if(GroupLeader=="Y") {
			//组长，可看其他评委打分
			var ksh_bmbid = ksbodyArray[i]['ps_ksh_bmbid'];
			detallist+="<td class='alt' style='padding:0'><a data-icon='arrow-r' data-mini='true' data-inline='true' data-role='button' data-ajax='false' id='favrecipelink' href='javascript:void(0);' onclick='viewOtherJudge(" +ksh_bmbid+")' data-corners='true' data-shadow='true'data-iconshadow='true' data-wrapperels='span' data-theme='a' class='ui-btn ui-shadow ui-icon-arrow-r ui-btn-corner-all ui-mini ui-btn-inline ui-btn-icon-left ui-btn-up-c'><span class='ui-btn-inner ui-btn-corner-all ui-corner-top ui-corner-bottom'><span class='ui-btn-text'>查看</span></span></a></td>";
		}

	}

	 //列名
	 var examerHeaderlist="<th scope='col'  style='text-align:center;'>序号</th>";
	examerHeaderlist += "<th scope='col' style='text-align:center;'>报名表编号</th>";
	examerHeaderlist += "<th scope='col'  style='text-align:center;'>姓名</th>";
	examerHeaderlist += "<th scope='col'  style='text-align:center;'>性别</th>";
	examerHeaderlist += "<th scope='col'  style='text-align:center;'>面试申请号</th>";

  	 var headbutton="<th scope='col'  style='text-align:center;'>评审状态</th>";
	 headbutton+="<th scope='col'  style='text-align:center;'>评审时间</th>";
	 if(GroupLeader=="Y") {
		 //组长，可看其他评委打分
		 headbutton += "<th scope='col'  style='text-align:center;'>"+"其他评委打分"+"</th>";
	 }
	 var examerinfolist="<table  id='mytable' cellspacing='0' width='100%' summary='the technical specifications of the apple powermac g5 series'>";
	 examerinfolist += "<tr>"+examerHeaderlist+collist+headbutton+'</tr>'+detallist+'</table>';

	 $("#examerinfo").html(examerinfolist);


	//其他评委打分
	/*
	if(GroupLeader=="Y") {

		var qtpwScoreHtml = "";

		//列名
		var qtpwColName = "<th scope='col'  style='text-align:center;'>评委账号</th>";
		qtpwColName += "<th scope='col' style='text-align:center;'>评委姓名</th>";
		qtpwColName += "<th scope='col' style='text-align:center;'>类型</th>";
		qtpwColName += collist;

		for (var i = 0; i < ksbodyArray.length; i++) {

			var qtpwValue = "";
			var qtpwArray = ksbodyArray[i]['ps_other_pw'];

			for (var j = 0; j < qtpwArray.length; j++) {
				qtpwValue += "<tr id='" + qtpwArray[j]['otherPwDlzhId'] + "'>";
				qtpwValue += "<td  class='alt' style='border-left: 1px solid #c1dad7;'>" + qtpwArray[j]['otherPwDlzhId'] + "</td>";
				qtpwValue += "<td  class='alt' style='border-left: 1px solid #c1dad7;'>" + qtpwArray[j]['otherPwName'] + "</td>";
				qtpwValue += "<td  class='alt' style='border-left: 1px solid #c1dad7;'>" + qtpwArray[j]['otherPwTypeDesc'] + "</td>";

				var num = 0;
				for (var k in ksheadObject) {
					num++;
					var colName = '00' + num;
					colName = 'col' + colName.substr(colName.length - 2);
					qtpwValue += "<td class='alt' >" + (qtpwArray[j][colName] || "") + '</td>';
				}
				qtpwValue += "</tr>";
			}

			var otherScoreDivId = "score" + ksbodyArray[i]['ps_ksh_bmbid'];

			var qtpwScore ='<div id=' + otherScoreDivId + '>';
			qtpwScore += '<div>学生姓名：' + ksbodyArray[i]['ps_ksh_xm'] + '</div>';
			qtpwScore += '<table cellspacing="0" width="100%" summary="the technical specifications of the apple powermac g5 series">';
			qtpwScore += '<tr>' + qtpwColName + '</tr>';
			qtpwScore += qtpwValue;
			qtpwScore += '</table>';
			qtpwScore += "</div>";

			qtpwScoreHtml += qtpwScore;
		}

		$("#otherJudgeScore").append(qtpwScoreHtml);
	}
	*/


	//已评审考生得分统计
/*
		var headline1="";
		var headline2="";
		var headall="";
        var statable1="";
		var ksdfArray=jsonObject['ps_data_cy']['ps_tjzb_btmc'];
		var ksdfinfoArray=jsonObject['ps_data_cy']['ps_tjzb_mxsj'];


		for(var i=0;i<ksdfArray.length;i++){
			
			var colname='00'+(i+1);
			colname='col'+colname.substr(colname.length-2);
	
			if(ksdfArray[i]['ps_grp_flg']=='Y'){
				var headnum=0;
				   for(var j=0;j<ksdfArray[i]['ps_sub_col'].length;j++){


						if(ksdfArray[i]['ps_sub_col'][j]['ps_cht_flg']=='Y'){
							headnum+=1;
							var subcolname='00'+(j+1);
							subcolname='sub_col'+subcolname.substr(subcolname.length-2);

							headline2+="<th scope='col' style='text-align:center;'>"+ksdfArray[i]['ps_sub_col'][j][subcolname]+"</th>";
							
						}

					}
				headline1+="<th scope='col' style='text-align:center;' colspan='"+headnum+"'>"+ksdfArray[i][colname]+"</th>";

			}
			else{

			headline1+="<th scope='col' style='text-align:center;' rowspan='2'>"+ksdfArray[i][colname]+"</th>";
			}
		
		}
		
		var bodyline="";
			var bodyele="";
			for(var j=0;j<ksdfArray.length;j++){
				var colname='00'+(j+1);
				colname='col'+colname.substr(colname.length-2);

			
				if (ksdfArray[j]['ps_grp_flg']=="Y"){
					for(var k=0;k<ksdfArray[j]['ps_sub_col'].length;k++){
						if(ksdfArray[j]['ps_sub_col'][k]['ps_cht_flg']=="Y"){
							var subcolname='00'+(k+1);
							subcolname='sub_col'+subcolname.substr(subcolname.length-2);

							bodyele+="<td class='alt'>"+ksdfinfoArray[j][colname][subcolname]+"</td>";
						}

					}
				}
				else{
					bodyele+="<td class='alt'>"+ksdfinfoArray[j][colname]+"</td>";					
				}
		
			}

			bodyline+="<tr>"+bodyele+"</tr>";
		
		headall="<tr scope='col'  style='text-align:center;'>"+headline1+"</tr>"+"<tr scope='col'  style='text-align:center;'>"+headline2+"</tr>";
		statable1="<table id='mytable'  cellspacing='0' width='100%' summary='the technical specifications of the apple powermac g5 series'> "+headall+bodyline+"</table>";
	*/
	

	// 已评审考生得分分布统计;
	/*
	var fszbArray=jsonObject['ps_data_fb'];
	var dffbscroll="";
	for(var i=0;i<fszbArray.length;i++){
	
		var dffbhead="";
		var dffbbody="";
		var dffbtable="";

			dffbhead="<tr><th scope='col'  style='text-align:center;'>分布名称</th><th scope='col'  style='text-align:center;'>我目前评分分布比率</th><th scope='col'  style='text-align:center;'>我目前评分分布人数</th><th scope='col'  style='text-align:center;'>评委总体分布比率</th></tr>"
			
			for(var j=0;j<fszbArray[i]['ps_fszb_fbsj'].length;j++){
				
				dffbbody+="<tr><td class='alt' style='border-left: 1px solid #c1dad7;'>"+fszbArray[i]['ps_fszb_fbsj'][j]['ps_fb_mc']+"</td><td class='alt'>"+fszbArray[i]['ps_fszb_fbsj'][j]['ps_sjfb_bilv']+"</td><td class='alt'>"+fszbArray[i]['ps_fszb_fbsj'][j]['ps_sjfb_rshu']+"</td><td class='alt'>"+fszbArray[i]['ps_fszb_fbsj'][j]['ps_ztfb_bilv']+"</td></tr>";

			}
		dffbtable="<table id='mytable' cellspacing='0' width='100%' summary='the technical specifications of the apple powermac g5 series'>"+"<thead><h4 style='margin:10px 0'>"+fszbArray[i]['ps_fszb_mc']+"</h4></thead>"+dffbhead+dffbbody+"</table>";

		dffbscroll+="<div style='width:100%'><div>"+"</div><div>"+dffbtable+"</div></div>";
	}
	*/
	

	//设置图表区是否显示;
	/*
	if(data.ps_pwkj_tjb!="Y"){
		$("#pwkj_tjb1").css("display", "none");
		$("#pwkj_tjb2").css("display", "none");
	}
	else{

	 $("#statistics2").html(dffbscroll);
	 $("#statistics1").html(statable1);
	}
	if(data.ps_pwkj_fbt!="Y"){
	
		$("#columnGroup").css("display", "none");
		$("#columnFbt").css("display", "none");
	}
	else{

	}

  		// $( "#statistics2" ).collapsibleset( "refresh" );
		//图形;
		var tmpArray = jsonObject['ps_data_cy']['ps_tjzb_btmc'];	
		for(var i=0;i<tmpArray.length;i++)
		{
			var colName = '00' + (i + 1);
			var dfRow = [];
		
			colName = 'col' + colName.substr(colName.length - 2);
			if(tmpArray[i]['ps_grp_flg'] == 'Y')
			{
					yAxisArrName.push(tmpArray[i][colName]);

				for(var j=0;j<tmpArray[i]['ps_sub_col'].length;j++)
				{
					var subColName = '00' + (j + 1);
					var tmpSubColName = '';
				
					subColName = 'sub_col' + subColName.substr(subColName.length - 2);
					tmpSubColName = colName + '_' + subColName;
				
					if(tmpArray[i]['ps_sub_col'][j]['ps_cht_flg'] == 'Y')
					{
						varArr.push(subColName);
						legendArr.push(tmpArray[i]['ps_sub_col'][j][subColName]);

					}
				}
			}else{
				yAxisArrName.push(tmpArray[i][colName]);
				if(tmpArray[i]['ps_cht_flg'] == 'Y')
				{
					varArr.push(colName);
					legendArr.push(tmpArray[i][colName]);
				}
				
			}
		}

		tmpArray = jsonObject['ps_data_cy']['ps_tjzb_mxsj'];
		for(var i=0;i<tmpArray.length;i++)
		{
			var colName = '00' + (i + 1);
			colName = 'col' + colName.substr(colName.length - 2);
			if(varArr.includes(colName))
			{
				yAxisArr.push(parseFloat(tmpArray[i][colName]));
			}else{
				xAxisArr.push(tmpArray[i][colName]);
			}
		}
		*/

		/****曲线图****/
	/*
		var lineArray = jsonObject['ps_data_fb'];
		for(var i=0;i< lineArray.length;i++){
			var divId2="container1_"+i;
			var seriesName = lineArray[i]['ps_fszb_mc'];
			var xaxArr_line = [];
			var yAxisArr_line=[],y2AxisArr_line = [];
			var ps_fszb_fbsj_arr=[];
			var series1Name=""  , series2Name="评委总体分布曲线";


			if(Object.prototype.toString.call(lineArray[i]['ps_cht_flds']) == '[object Array]')
			{
				for(var chtfld_i=0;chtfld_i<lineArray[i]['ps_cht_flds'].length;chtfld_i++)
				{
					if(lineArray[i]['ps_cht_flds'][chtfld_i] == 'ps_bzfb_bilv')
					{

						series1Name="标准分布曲线";
						ps_fszb_fbsj_arr = lineArray[i]['ps_fszb_fbsj'];
						for(var j=0; j<ps_fszb_fbsj_arr.length; j++ ){
							xaxArr_line.push(ps_fszb_fbsj_arr[j]['ps_fb_mc']);
							yAxisArr_line.push(parseFloat(ps_fszb_fbsj_arr[j]['ps_bzfb_bilv']));
						}

					}
					else if(lineArray[i]['ps_cht_flds'][chtfld_i] == 'ps_sjfb_bilv')
					{
						series1Name="我的评分分布曲线";
						ps_fszb_fbsj_arr = lineArray[i]['ps_fszb_fbsj'];
						for(var j=0; j<ps_fszb_fbsj_arr.length; j++ ){
							xaxArr_line.push(ps_fszb_fbsj_arr[j]['ps_fb_mc']);
							yAxisArr_line.push(parseFloat(ps_fszb_fbsj_arr[j]['ps_sjfb_bilv']));
						}
					}
				}
					for(var ztpjfi = 0; ztpjfi < lineArray[i]['ps_fszb_fbsj'].length ; ztpjfi++ )
					{
						y2AxisArr_line.push(parseFloat(lineArray[i]['ps_fszb_fbsj'][ztpjfi]['ps_ztfb_bilv']));
							
					}
			
			}

		
			var linechar = new Highcharts.Chart({
				credits: {
				     enabled: false
				},
				chart: {
					renderTo: divId2,
					defaultSeriesType: 'spline',
					borderColor:'#ddd',
					borderWidth:1,
					marginRight: 20,
					marginBottom: 55,
					width:920
				},
				title: {
					text: '',
					x: 0 //center
				},
				subtitle: {
					text: '',
					x: 0
				},
				xAxis: {
					title:{
						text:seriesName
					},
					startOnTick:true,
					categories: xaxArr_line
				},
				yAxis: {
					max:100, // 定义Y轴 最大值  
		            min:0, // 定义最小值  
					title: {
						text: '分布比率'
					},
					plotLines: [{
						value: 0,
						width: 1,
						color: '#808080'
					}]
				},
				tooltip: {
					formatter: function() {
							// return '<b>'+ this.series.name +'</b><br/>'+ this.x +': '+ this.y +'°C';
						return '分布区间：'+this.x+'<br/>分布比率：'+ this.y ;
					}
				},
				legend: {
					layout: 'vertical',
					align: 'center',
					verticalAlign: 'top',
				//	x: 0,
					y: 10,
			    //  borderWidth: 0
				},
				series: [{
						name: series1Name,
						data:yAxisArr_line
					},{
						name: series2Name,
						data:y2AxisArr_line
					}]
			});
			charts_arr0.push(linechar);
		}
	*/


		/***柱状图****/
	/*
	   // var divWidth = $("#columnGroup").width();
		//alert(xAxisArr.length+"--"+yAxisArr.length);
		for(var chartNum = 0; chartNum < xAxisArr.length; chartNum++){
			var divId = "container2_"+chartNum;
			var cateGoriesArr = [];
		//	cateGoriesArr.push(xAxisArr[chartNum]);

			var seriesData = [] ,series2Data = [];


			for(var chartxNum=chartNum*yAxisArr.length/xAxisArr.length;chartxNum<(chartNum+1)*yAxisArr.length/xAxisArr.length;chartxNum++){

					seriesData.push(yAxisArr[chartxNum]);
					
			}
			for (var chartxNum=0;chartxNum<yAxisArr.length/xAxisArr.length;chartxNum++){

					cateGoriesArr.push(yAxisArrName[chartxNum]);
			}

			var char2 = new Highcharts.Chart({
				credits: {
				     enabled: false
				},
				chart: {
					renderTo: divId,
					defaultSeriesType: 'column',
					borderColor:'#ddd',
					borderWidth:1,
					marginRight: 20,
					marginBottom: 55,
					width:920
				},

				title: {
				 	text: '',
					x: 0 //center
				},

				xAxis: {
					title:{
						text: xAxisArr[chartNum]
					},
					startOnTick: true,
					categories: cateGoriesArr
				},

				yAxis: {
					min:0, 
					title: {
						text: '统计指标值'
					},
		
					plotLines: [{
						value: 0,
						width: 1,
						color: '#808080'
					}]
				},
				tooltip: {
					formatter: function() {
							return this.series.name+"：" + this.y ;
							//return this.x+"："+ this.y ;
					}
				},

				legend: {
					layout: 'vertical',
					align: 'center',
					verticalAlign: 'top',
					y: 0
				},

				series: [{
					name: legendArr[0],
					data: [seriesData[0]]
				},{
					name: legendArr[1],
					data: [seriesData[1]]
				}]
			});
			charts_arr.push(char2); 

		}
*/

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

	if(datapair[1].length<400){
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