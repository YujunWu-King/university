Ext.define('KitchenSink.view.content.contentMg.channelController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.channelCtl', 
	onFormSave: function(btn){
		//组件注册表单
		var form = this.getView().child("form").getForm();
		//组件信息标志
		var actType = this.getView().actType;
		//更新操作参数
		var comParams = "";

		//修改json字符串
		var editJson = "";

		editJson = Ext.JSON.encode(form.getValues());

		if(editJson != ""){
			if(comParams == ""){
				comParams = '"update":[' + editJson + "]";
			}else{
				comParams = comParams + ',"update":[' + editJson + "]";
			}
		}
		
		//提交参数
		var tzParams = '{"ComID":"TZ_CONTENT_MG_COM","PageID":"TZ_GD_ZDLM_STD","OperateType":"U","comParams":{'+comParams+'}}';

		var comView = this.getView();
		var thisObj=this;
		Ext.tzSubmit(tzParams,function(responseData){
			comView.actType = "update";
			/*
			if(actType == "add" && btn != "but_ensure"){
				var templateId = responseData.lm_id;
				form.findField('lm_id').setValue(templateId);
			}*/
			
			//contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
			//var templateGrid = contentPanel.child("siteTemplateInfoGL").child("form").child("tabpanel").getActiveTab();
			//templateGrid.store.reload();
			thisObj.onRefreshStore2(btn);
		},"",true,this);
	},
	onFormEnsure: function(btn){
		this.onFormSave(btn);
		this.getView().close();
	},
	onFormClose: function(){
		this.getView().close();
	},
	onRefreshStore:function(btn){
		var tvType;
		
		var win = btn.findParentByType("contentMg");

		tvType=win.down('combobox[name=myChannels]');
		//console.log(tvType);
		//tvType=Ext.getCmp('myChannels201507271129');

		var tzParams = '{"ComID":"TZ_CONTENT_MG_COM","PageID":"TZ_CONTENT_STD","OperateType":"QF","comParams":"{}"}';
		Ext.tzLoad(tzParams,function(responseData){
			var store = new Ext.data.Store({
				fields: ['TValue', 'TSDesc', 'TLDesc'],
				data:responseData.TransList
			});

			tvType.setStore(store);
			
			if(store.getCount() > 0){
				tvType.setValue(store.getAt(0).get("TValue"));
			}
			else{
				tvType.setValue('');
			}
		});
	},
	onRefreshStore2:function(btn){
		var tvType;
		
		var win = btn.findParentByType("contentMg");

		//tvType=Ext.getCmp('myChannels201507271129');
		tvType=win.down('combobox[name=myChannels]');
		//console.log(tvType);
		var tzParams = '{"ComID":"TZ_CONTENT_MG_COM","PageID":"TZ_CONTENT_STD","OperateType":"QF","comParams":"{}"}';
		Ext.tzLoad(tzParams,function(responseData){
			var store = new Ext.data.Store({
				fields: ['TValue', 'TSDesc', 'TLDesc'],
				data:responseData.TransList
			});

			tvType.setStore(store);
			/*
			if(store.getCount() > 0){
				tvType.setValue(store.getAt(0).get("TValue"));
			}
			*/
		});
	},
	//删除该栏目
	deleteChanl: function(btn){
	
		//this.getView().close();
		//组件注册表单
		var form = this.getView().child("form").getForm();
		//组件信息标志
		var actType = this.getView().actType;
		//更新操作参数
		var comParams = "";

		//修改json字符串
		var editJson = "";

		editJson = Ext.JSON.encode(form.getValues());
		comParams = '"delete":[' + editJson + "]";

		
		//提交参数
		var tzParams = '{"ComID":"TZ_CONTENT_MG_COM","PageID":"TZ_GD_ZDLM_STD","OperateType":"U","comParams":{'+comParams+'}}';

		var comView = this.getView();
		
		var thisObj=this;

		Ext.tzSubmit(tzParams,function(responseData){

			//var templateId = responseData.lm_id;
			//this.onRefreshStore(btn);
			thisObj.onRefreshStore(btn);
			comView.close();
		},"",true,this);
		
		
		
	},
	onDeleteChanl:function(btn){
	
		Ext.MessageBox.confirm('确认', '您确定要删除该栏目吗?', function(btnId){
			if(btnId == 'yes'){					   
			 this.deleteChanl(btn);
			}												  
		},this);  
	}
});