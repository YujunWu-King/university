Ext.define('KitchenSink.view.common.store.promptStore', {
    extend: 'Ext.data.Store',
    alias: 'store.promptStore',
	autoLoad: true,
	pageSize: 50,
	tzStoreParams:'',
	fields:[],
	tzType:'PROMPT',
	proxy: Ext.tzListProxy(),
	constructor: function (recname,condition,srhresult,maxRow) {
		var num = 1;
        //搜索结果字段
        var srhresults = "";
        var fields = [];
        for(var fieldName in srhresult) {
        	fields.push(new Ext.data.field.Field({
        		name: fieldName
        	}));
        	if(num == 1){
        		srhresults = fieldName;
        	}else{
        		srhresults = srhresults + "," + fieldName;
        	}
        	num = num + 1;
        }	
        //搜索最大行数
        if(maxRow == undefined || maxRow == ""){
        	maxRow = -1;
        }
        this.fields = fields;
        this.srhresults = srhresults;
        this.maxRow = maxRow;
        
        //交互参数
        this.tzStoreParams = '{"OperateType":"'+this.tzType+'","maxRow":"'+maxRow+'","recname":"'+recname+'","condition":'+Ext.encode(condition)+',"result":"'+srhresults+'"}';
        this.callParent();
    }	
});
