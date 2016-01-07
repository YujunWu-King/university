Ext.onReady(function() {
/*
	var urlParams=getQueryString("tzParams");

	urlParams = Ext.util.JSON.decode(urlParams);

	var siteid=urlParams.comParams.siteId;
*/
	//var siteid= $("#siteid").val();
	var siteid= TZ_GD_LOGIN_SITEI_ID;

	var areaid=	Ext.get(Ext.query('.main_left')[0]).getAttribute("area-id");

	if (!areaid)
	{
		areaid="";
	}
	var areaZone=Ext.get(Ext.query('.main_left')[0]).getAttribute("area-postion");
	if (!areaZone)
	{
		areaZone="";
	}
	var areaType=Ext.get(Ext.query('.main_left')[0]).getAttribute("area-type");
	if (!areaType)
	{
		areaType="";
	}

	var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_AREA_SETED_STD","OperateType":"HTML","comParams":{"siteId":"'+siteid+'","areaId":"'+areaid+'","areaZone":"'+areaZone+'","areaType":"'+areaType+'","oprate":"D"}}';
	
	Ext.Ajax.request({
		url: urlBegin,
		params: {
			tzParams:tzParams
		},
		method : 'POST',    
		success: function(response){
		
			if (response.responseText !="false"){
				Ext.get(Ext.query('.main_left')[0]).dom.innerHTML =response.responseText; 
			//	new ElBindEvent().bindBasic();
			}else{
				
			}
		}
	});

});