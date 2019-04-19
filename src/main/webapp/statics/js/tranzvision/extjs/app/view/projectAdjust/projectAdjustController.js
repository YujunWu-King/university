Ext.define('KitchenSink.view.projectAdjust.projectAdjustController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.projectAdjustController', 
	requires: [
       'KitchenSink.view.projectAdjust.setStateWin'
    ],
	//可配置搜索
	query_list: function(btn){     //searchComList为各自搜索按钮的handler event;
        Ext.tzShowCFGSearch({           
           cfgSrhId: 'TZ_PROADJUST_COM.TZ_PROADJUST_STD.TZ_PROADJUST_V',
           condition:
            {
                "TZ_JG_ID": Ext.tzOrgID           //设置搜索字段的默认值，没有可以不设置condition;
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });    
    },
    audit:function(btn,rowIndex){
    	var currentWin = btn.findParentByType("projectAdjust");
    	var store = btn.findParentByType("grid").store;
	 	var selRec = store.getAt(rowIndex);
   	 	var tz_proadjust_id = selRec.get("tz_proadjust_id");
   	 	/*//选中行
	   var selList = this.getView().getSelectionModel().getSelection();
    	console.log(selList)
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert("提示","请选择需要设置缴费状态的记录");   
			return;
	   }*/
		
		//var win = this.lookupReference('setStateWin');
           var  win = new KitchenSink.view.projectAdjust.setStateWin(currentWin);
			win.lookupReference('setStateForm').getForm().findField('tz_proadjust_id').setValue(tz_proadjust_id);
            this.getView().add(win);
        
        win.show();
    },
    setState:function(btn){
    	 var me = this;
    	var win = btn.findParentByType("setStateWin");
    	var tz_proadjust_id = win.child('form').getForm().findField("tz_proadjust_id").getValue();
    	var adjustStatus = win.child('form').getForm().findField("adjustStatus").getValue();
    	if(adjustStatus == null || adjustStatus == "" || adjustStatus == undefined || adjustStatus == 0){
    		Ext.Msg.alert("提示","请选择审核状态");   
			return;
    	}
    	//参数
		var tzParams = '{"ComID":"TZ_PROADJUST_COM","PageID":"TZ_PROADJUST_STD","OperateType":"setState","comParams":{"tz_proadjust_id":'+tz_proadjust_id+',"adjustStatus":'+adjustStatus+'}}';
		Ext.tzSubmit(tzParams,function(respDate){
			win.listWin.store.reload();
			me.getView().close();
		},"设置审核状态成功",true,this);
    }
});