Ext.define('KitchenSink.view.common.store.comboxStore', {
    extend: 'Ext.data.Store',
    alias: 'store.comboxStore',
	autoLoad: true,
	tzStoreParams:'',
	fields:[],
	tzType:'COMBOX',
	proxy: {
		type: 'ajax',
		url : Ext.tzGetGeneralURL(),
		reader: {
			type: 'json',
			rootProperty: 'comContent'
		}
	},
	constructor: function (config) {
		//搜索表或视图名称
		this.recname = config.recname;
		//搜索条件
        this.condition = config.condition;
        //搜索结果字段
        this.result = config.result;
        //搜索结果字段
        var result = config.result;
        var array = result.split(",");
        var fields = [];
        for(var i =0;i<array.length;i++){
        	fields.push(new Ext.data.field.Field({
        		name: array[i]
        	}));
        }
        this.fields = fields;
        this.listeners = config.listeners;
        //交互参数
        this.tzStoreParams = '{"OperateType":"'+this.tzType+'","recname":"'+this.recname+'","condition":'+Ext.encode(this.condition)+',"result":"'+this.result+'"}';
        this.config.proxy.reader.rootProperty = 'comContent.'+this.recname;
        this.callParent();
    }
});
