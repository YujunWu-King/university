Ext.define('KitchenSink.view.sendEmailAndSMS.smsTempletDef.smsTempletInfoMth', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.smsTempletInfoMth', 
	onFormSave: function(){
		var comView = this.getView();
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			var tzParams = this.getSmsTmplInfoParams();
			Ext.tzSubmit(tzParams,function(responseData){
				comView.actType = "update";	
				form.findField("smstempid").setReadOnly(true);
				form.findField("smstemporg").setReadOnly(true);
				form.findField("smstempid").addCls("lanage_1");
				form.findField("smstemporg").addCls("lanage_1");
				var contentPanel;
				contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
				contentPanel.child("smsTempletDef").store.reload();
			},"",true,this);
		}	
	},
	onFormEnsure: function(){
		//获取窗口
		var comView = this.getView();
		//信息表单
		var form = comView.child("form").getForm();
		if (form.isValid()) {
			/*保存页面信息*/
			var tzParams = this.getSmsTmplInfoParams();	
			Ext.tzSubmit(tzParams,function(responseData){
				comView.actType = "update";
				//重置表单
				form.reset();
				//关闭窗口
				comView.close();
				var contentPanel;
				contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
				contentPanel.child("smsTempletDef").store.reload();
			},"",true,this);

		}
	},
	onFormClose: function(){
		this.getView().close();
	},
	deleteParaInfoItemRow: function(grid,rowIndex){
		Ext.MessageBox.confirm('确认', '确定删除该记录?', function(btn){
            if(btn!='yes') {
                return;
            }						            			
            //var grid=this.lookupReference('paragrid');
            //rowEditing.cancelEdit();
            var parastore=grid.getStore();
			var sm = grid.getSelectionModel();
			sm.select(rowIndex);
            parastore.remove(sm.getSelection());
        },this);
	},
	deleteContentRow: function(grid,rowIndex){
		Ext.MessageBox.confirm('确认', '确定删除该记录?', function(btn){
            if(btn!='yes') {
                return;
            }						            			    
            //rowEditing.cancelEdit();
			//var grid=this.lookupReference('tmpcontgrid')
            var contstore=grid.getStore();
			var sm = grid.getSelectionModel();
			sm.select(rowIndex);
            contstore.remove(sm.getSelection());
        },this);  
	},
	insertSmsitem: function(grid,rowIndex, colIndex){
		/**/
		var rec = grid.getStore().getAt(rowIndex);
		var parainfoitem = rec.get('parainfoitem');

		var form = this.getView().child("form").getForm();
		var dom = form.findField("smstmpcontent").inputEl.dom;
		this.insertAtCursor(dom,parainfoitem);
	},
	
	copySmsitem: function(grid,obj,rowIndex, colIndex){

		var form = this.getView().child("form").getForm();
		var rec = grid.getStore().getAt(rowIndex);
		var parainfoitem = rec.get('parainfoitem');
		//form.findField("smstmpcontent").value = "xxx"; 
		//this.copyToClipBoard(parainfoitem);
		
		/*
		var clip = new ZeroClipboard.Client();
		clip.setHandCursor(true); // 设置鼠标为手型
		clip.setText("测试完成11"); // 设置要复制的文本
		clip.glue("copy1-inputEl","itemSmsCopy");
		*/
		
	},
	getSmsTmplInfoParams: function(){
		//组件注册表单
		var form = this.getView().child("form").getForm();
		//组件信息标志
		var actType = this.getView().actType;
		//更新操作参数
		var comParams = "";

		//新增
		if(actType == "add"){
			comParams = '"add":[{"typeFlag":"SMSTMPLINFO","data":'+Ext.JSON.encode(form.getValues())+'}]';
		}
		//修改json字符串
	   console.log(form.getValues());
		var editJson = "";
		if(actType == "update"){
			editJson = '{"typeFlag":"SMSTMPLINFO","data":'+Ext.JSON.encode(form.getValues())+'}';
		}
		
		if(editJson != ""){
			if(comParams == ""){
				comParams = '"update":[' + editJson + "]";
			}else{
				comParams = comParams + ',"update":[' + editJson + "]";
			}
		}
		
		//提交参数
		var tzParams = '{"ComID":"TZ_SMS_TMPL_MG_COM","PageID":"TZ_SMS_TMPL_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
	},
	
	insertAtCursor: function(myField, myValue) {

				//IE support
		if (document.selection) {
			myField.focus();
			sel = document.selection.createRange();
			sel.text = myValue;
			sel.select();
		}
		//MOZILLA/NETSCAPE support 
		else if (myField.selectionStart || myField.selectionStart == '0') {
			
			var startPos = myField.selectionStart;
			var endPos = myField.selectionEnd;
			// save scrollTop before insert www.keleyi.com
			var restoreTop = myField.scrollTop;
			myField.value = myField.value.substring(0, startPos) + myValue + myField.value.substring(endPos, myField.value.length);
		if (restoreTop > 0) {
		myField.scrollTop = restoreTop;
		}
		myField.focus();
		myField.selectionStart = startPos + myValue.length;
		myField.selectionEnd = startPos + myValue.length;
		} else {

			myField.value += myValue;
			myField.focus();
		}
	},
	copyToClipBoard: function (copyText){
		if (window.clipboardData) 
		{ 
			window.clipboardData.setData("Text", copyText) 
		} 
		else 
		{	
			alert("暂不支持非IE内核的浏览器。");
		} 
	},
	changeResTmpl:function(combo,records,eOpts)
	{
		var form = this.getView().child("form").getForm();
		//var	smsTmplItemGrid = panel.down('grid[name=emlTmplItemGrid]');
		var smsTmplItemGrid=this.lookupReference('smsTmplItemGrid');
		var tzStoreParamsItem = '{"restempid":"'+combo.value+'"}';
		smsTmplItemGrid.store.tzStoreParams = tzStoreParamsItem;
		smsTmplItemGrid.store.load();

		/*加载默认邮箱*/
		
		var tzParams = '{"restempid":"' + combo.value + '","searchType":"searchResTmpl"}';
        tzParams = '{"ComID":"TZ_SMS_TMPL_MG_COM","PageID":"TZ_SMS_TMPL_MG_STD","OperateType":"QF","comParams":' + tzParams +'}';
   
        Ext.Ajax.request({
			 url: Ext.tzGetGeneralURL,
			 params: {
			 tzParams: tzParams
        },
        success: function(response){
         
          var responseText = Ext.util.JSON.decode(response.responseText);

          var tempsmsserv = responseText.comContent.tempsmsserv;
          
		  form.findField("tempsmsserv").setValue(tempsmsserv);
		  
         },
         failure: function (response) {
          //Ext.MessageBox.alert("错误", "错误");
         }
        });
		
	},
	changeOrg:function(combo,records,eOpts)
	{
		var form = this.getView().child("form").getForm();
		
		form.findField("restempid").setValue("");
		
		form.findField("tempsmsserv").setValue("");
		/**/
		var smsTmplItemGrid=this.lookupReference('smsTmplItemGrid');
		smsTmplItemGrid.store.removeAll();
	}

}); 

