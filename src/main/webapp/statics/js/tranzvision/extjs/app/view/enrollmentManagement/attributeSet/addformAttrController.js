Ext.define('KitchenSink.view.enrollmentManagement.attributeSet.addformAttrController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.addformAttrController', 

	//设置下拉值 窗口 保存
	onAttrItemSave: function(btn){
		var bool=this.onreCheck(btn);
		if (bool!=false) {
			//获取窗口
			var win = btn.findParentByType("window");
			//页面注册信息表单
			var form = win.child("form").getForm();
			if (form.isValid()) {
				/*保存页面注册信息*/
				var tzParams = this.getItmeOptionsParams(win);

				Ext.tzSubmit(tzParams,function(responseData){

				},"",true,this);
				
			}
		}
	},	
	//确定
	onAttrItemEnsure: function(btn){
		var bool=this.onreCheck(btn);
		if (bool!=false) {
		
			//获取窗口
			var win = btn.findParentByType("window");
			//页面注册信息表单
			var form = win.child("form").getForm();
			if (form.isValid()) {
				/*保存页面注册信息*/
				var tzParams = this.getItmeOptionsParams(win);

				Ext.tzSubmit(tzParams,function(responseData){
					//重置表单
					form.reset();
					//关闭窗口
					win.close();
				},"",true,this);
			}	
		}	
	},
	onComRegClose: function(btn){
		//关闭窗口
		this.getView().close();
	},
	onPageRegSave: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		if (form.isValid()) {
			/*保存页面注册信息*/
			this.savePageRegInfo(win);
		}
	},
	onPageRegEnsure: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		if (form.isValid()) {
			/*保存页面注册信息*/
			this.savePageRegInfo(win);
			//重置表单
			form.reset();
			//关闭窗口
			win.close();
		}	
	},
	//关闭窗口
	onPageClose: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		win.close();
	}

});