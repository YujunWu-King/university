Ext.define('KitchenSink.view.sendEmailAndSMS.emlTempletDef.emlTempletInfoMth', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.emlTempletInfoMth', 
	onFormSave: function(){
		var comView = this.getView();
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			/*
				var emlContentMode = form.findField("emlContentMode");
				var dom = form.findField("emltmpcontentHtml").getEl();
			
				if(dom == undefined)
				{
					console.log("dom未实例化");
					emlContentMode.setValue('textarea');
				}else
				{
					emlContentMode.setValue('ueditor');
					console.log(dom);
				}
			*/
			var tzParams = this.getEmlTmplInfoParams();
			Ext.tzSubmit(tzParams,function(responseData){
				comView.actType = "update";	
				form.findField("emltempid").setReadOnly(true);
				form.findField("emltemporg").setReadOnly(true);
				form.findField("emltempid").addCls("lanage_1");
				form.findField("emltemporg").addCls("lanage_1");
				/*
				var emlContent = form.findField("emltmpcontent");
				var emlContentHtml = form.findField("emltmpcontentHtml");
				console.log(emlContent.value);
				console.log(emlContentHtml.value);
				*/
				
				var contentPanel;
				contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
				contentPanel.child("emlTempletDef").store.reload();
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
			var tzParams = this.getEmlTmplInfoParams();	
			//form.findField("emltmpcontentHtml").setValue("Hello");
			Ext.tzSubmit(tzParams,function(responseData){
				comView.actType = "update";	
				//重置表单
				//form.reset();
				//关闭窗口
				comView.close();
				var contentPanel;
				contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
				contentPanel.child("emlTempletDef").store.reload();
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
	insertemlitem: function(grid,rowIndex, colIndex){
		/**/
		var rec = grid.getStore().getAt(rowIndex);
		var parainfoitem = rec.get('parainfoitem');

		var form = this.getView().child("form").getForm();
		var ishtmlCheck = form.findField("ishtml").checked;
		if(ishtmlCheck)
		{
			var objEmlContentHtml = form.findField("emltmpcontentHtml");
			var dom = form.findField("emltmpcontentHtml").getEl();
			var editorId = dom.id + "-ueditor";
			UE.getEditor(editorId).execCommand("inserthtml",parainfoitem);
			/*
			var objEmlContentHtml = form.findField("emltmpcontentHtml");
			var sel = objEmlContentHtml.win.getSelection();
			var cur = sel.focusOffset; 
			var val = objEmlContentHtml.getValue();
			var val1 = val.substring(0, cur);
			var val2 = val.substring(cur, val.length);
			objEmlContentHtml.setValue(val1 + parainfoitem + val2);
			*/
		}else
		{
			var dom = form.findField("emltmpcontent").inputEl.dom;
			var objEmlContent = form.findField("emltmpcontent");
			this.insertAtCursor(dom,parainfoitem,objEmlContent);
		}
	},
	
	copyemlitem: function(grid,obj,rowIndex, colIndex){

		var form = this.getView().child("form").getForm();
		var rec = grid.getStore().getAt(rowIndex);
		var parainfoitem = rec.get('parainfoitem');
		//form.findField("emltmpcontent").value = "xxx"; 
		//this.copyToClipBoard(parainfoitem);
		
		/*
		var clip = new ZeroClipboard.Client();
		clip.setHandCursor(true); // 设置鼠标为手型
		clip.setText("测试完成11"); // 设置要复制的文本
		clip.glue("copy1-inputEl","itemEmlCopy");
		*/
		
	},
	getEmlTmplInfoParams: function(){
		//组件注册表单
		var form = this.getView().child("form").getForm();
		//组件信息标志
		var actType = this.getView().actType;
		//更新操作参数
		var comParams = "";

		//新增
		if(actType == "add"){
			comParams = '"add":[{"typeFlag":"EMLTMPLINFO","data":'+Ext.JSON.encode(form.getValues())+'}]';
		}
		//修改json字符串
	
		var editJson = "";
		if(actType == "update"){
			editJson = '{"typeFlag":"EMLTMPLINFO","data":'+Ext.JSON.encode(form.getValues())+'}';
		}
		
		if(editJson != ""){
			if(comParams == ""){
				comParams = '"update":[' + editJson + "]";
			}else{
				comParams = comParams + ',"update":[' + editJson + "]";
			}
		}
		
		//提交参数
		var tzParams = '{"ComID":"TZ_EML_TMPL_MG_COM","PageID":"TZ_EML_TMPL_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
	},
	
	insertAtCursor: function(myField, myValue,obj) {

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
		
		obj.setValue(myField.value);
		
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
		//var	emlTmplItemGrid = panel.down('grid[name=emlTmplItemGrid]');
		var emlTmplItemGrid=this.lookupReference('emlTmplItemGrid');
		var tzStoreParamsItem = '{"restempid":"'+combo.value+'"}';
		emlTmplItemGrid.store.tzStoreParams = tzStoreParamsItem;
		emlTmplItemGrid.store.load();

		/*加载默认邮箱*/
		
		var tzParams = '{"restempid":"' + combo.value + '","searchType":"searchResTmpl"}';
        tzParams = '{"ComID":"TZ_EML_TMPL_MG_COM","PageID":"TZ_EML_TMPL_MG_STD","OperateType":"QF","comParams":' + tzParams +'}';
   
        Ext.Ajax.request({
			 url: Ext.tzGetGeneralURL,
			 params: {
			 tzParams: tzParams
        },
        success: function(response){
         
          var responseText = Ext.util.JSON.decode(response.responseText);

          var tempemailserv = responseText.comContent.tempemailserv;
          
		  form.findField("tempemailserv").setValue(tempemailserv);
		  
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
		
		form.findField("tempemailserv").setValue("");
		/**/
		var emlTmplItemGrid=this.lookupReference('emlTmplItemGrid');
		emlTmplItemGrid.store.removeAll();
	},
	htmlSwitch:function(checkbox ,checked)
	{
		if(checked)
		{
			var form = this.getView().child("form").getForm();
			var emlContent = form.findField("emltmpcontent");
			var emlContentHtml = form.findField("emltmpcontentHtml");
			emlContent.setHidden(true);
			emlContentHtml.setHidden(false);
			emlContentHtml.setValue(emlContent.value);
		}else
		{
			
			var form = this.getView().child("form").getForm();
			var emlContent = form.findField("emltmpcontent");
			var emlContentHtml = form.findField("emltmpcontentHtml");
			emlContent.setValue(emlContentHtml.value);
			emlContent.setHidden(false);
			emlContentHtml.setHidden(true);
		}
	}

}); 

