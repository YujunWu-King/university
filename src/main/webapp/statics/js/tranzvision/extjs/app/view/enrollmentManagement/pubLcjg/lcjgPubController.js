Ext.define('KitchenSink.view.enrollmentManagement.pubLcjg.lcjgPubController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.lcjgPub',
    //保存单独公布结果
	SaveperLcjgPubWindow: function(btn){
        var me = this;
		var form = this.getView().child("form").getForm();
		var comParams = "";
		comParams = '"add":['+Ext.JSON.encode(form.getValues())+']';
		var tzParams = '{"ComID":"TZ_BMGL_LCJGPUB_COM","PageID":"TZ_PER_LCJGPUB_STD","OperateType":"U","comParams":{'+comParams+'}}';
		var comView = this.getView();
        Ext.tzSubmit(tzParams, function(){me.getView().perLcjgCallBack()},"",true,this);
	},
	//确定单独公布结果
	EnsureperLcjgPubWindow: function(btn){
        var me = this;
		var form = this.getView().child("form").getForm();
		var comParams = "";
		comParams = '"add":['+Ext.JSON.encode(form.getValues())+']';
		var tzParams = '{"ComID":"TZ_BMGL_LCJGPUB_COM","PageID":"TZ_PER_LCJGPUB_STD","OperateType":"U","comParams":{'+comParams+'}}';
		var comView = this.getView();

        Ext.tzSubmit(tzParams, function(){me.getView().perLcjgCallBack()},"",true,this);
		this.getView().close();
	},
	//关闭单独公布结果页面
	CloseperLcjgPubWindow: function(){
		this.getView().close();
	},
	//保存批量公布结果
	SaveperLcjgPubWindowP: function(btn){
        var me = this;
		var form = this.getView().child("form").getForm();
		var comParams = "";
		comParams = '"add":['+Ext.JSON.encode(form.getValues())+']';
		var tzParams = '{"ComID":"TZ_BMGL_LCJGPUB_COM","PageID":"TZ_PER_LCJGP_STD","OperateType":"U","comParams":{'+comParams+'}}';
		var comView = this.getView();
        Ext.tzSubmit(tzParams,function(){me.getView().perLcjgCallBack()},"",true,this);

	},
    //确定批量公布结果
    EnsureperLcjgPubWindowP: function(btn){
        var me = this;
        var form = this.getView().child("form").getForm();
        var comParams = "";
        comParams = '"add":['+Ext.JSON.encode(form.getValues())+']';
        var tzParams = '{"ComID":"TZ_BMGL_LCJGPUB_COM","PageID":"TZ_PER_LCJGP_STD","OperateType":"U","comParams":{'+comParams+'}}';
        var comView = this.getView();

        Ext.tzSubmit(tzParams, function(){me.getView().perLcjgCallBack()},"",true,this);
        this.getView().close();


    },
    //关闭批量公布结果页面
    CloseperLcjgPubWindowP: function(){
        this.getView().close();
    }
});