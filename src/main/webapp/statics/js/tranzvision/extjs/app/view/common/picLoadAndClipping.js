Ext.define('KitchenSink.view.common.picLoadAndClipping', {
    extend: 'Ext.window.Window',
    xtype: 'picLoadAndClipping',
    title: '图片处理',
    filename:'',
    sysFileName:'',
    path:'',
    accessPath:'',
    items: [{
		xtype:'container',
		width:840,
		height:500,
		html:'<iframe id="picClipping" name="picClipping" src="' + Ext.tzGetGeneralURL() + '?tzParams={\'ComID\':\'TZ_GD_FILTER_COM\',\'PageID\':\'TZ_PICCLIPPING_STD\',\'OperateType\':\'HTML\',\'comParams\':{}}" style="width:100%;height:100%;margin:0px;padding:0px;" frameborder="0"></iframe>'
	}],
    buttons: [{
		text: '确定',
		iconCls:"ensure",
		handler: function(){
			var picWin = this.findParentByType("picLoadAndClipping");
			var ifr = document.getElementById('picClipping');
			var win = ifr.window || ifr.contentWindow;
			if(win.imaName){
				picWin.filename = win.filename;
				picWin.sysFileName = win.imaName;
				picWin.path = win.path;
				picWin.accessPath = win.imaPath;
				//alert(picWin.filename+"**"+picWin.sysFileName+"**"+picWin.path+"**"+picWin.accessPath);
				
				picWin.close();
			}else{
				Ext.MessageBox.alert('提示', '请先上传一张照片。');
			}
		}
	},{
		text: '关闭',
		iconCls:"close",
		handler: function(){
			var picWin = this.findParentByType("picLoadAndClipping");
			picWin.filename = "";
			picWin.sysFileName = "";
			picWin.path = "";
			picWin.accessPath = "";
			//alert(picWin.filename+"**"+picWin.sysFileName+"**"+picWin.path+"**"+picWin.accessPath);
			
			var ifr = document.getElementById('picClipping');
			var win = ifr.window || ifr.contentWindow;
			win.onFormClose();
			
			picWin.close();
		}
	}]
});
//<iframe id="picClipping" name="picClipping" src="/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_ATT_D.TZ_PIC_CLIPPING.FieldFormula.IScript_uploadPhoHTML" style="width:100%;height:100%;margin:0px;padding:0px;" frameborder="0"></iframe>