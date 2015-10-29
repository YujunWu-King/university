Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.dynamicInfo.dynamicColumn', {
    extend: 'Ext.grid.Column',
    xtype:'appFormDynamicColumn',
    header:'<div style="text-align:left">'+Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.applicationInfo","报名表信息")+'</div>',
    menuDisabled: true,
    classID:'',
    store:{},
    initComponent:function(){
        var me = this;
        var columnArray;
        var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"tzLoadGridColumns","comParams":{"classID":"'+this.classID+'"}}';
        Ext.tzLoadAsync(tzParams,function(responseData){
            columnArray = responseData;
        });

        Ext.apply(this,{
            columns:columnArray
        });

        var dynamicFields = ['classID','oprID','appInsID','stuName','submitState','submitDate','auditState', 'colorType','moreInfo'];
        for(var i=0;i<columnArray.length;i++){
            var name = columnArray[i].dataIndex;
            var type = columnArray[i].filter.type=='date'?'date':'string';
            switch(columnArray[i].filter.type)
            {
                case "number":
                    type="number";
                    break;
                case "date":
                    type="date";
                    break;
                default:
                    type= "string"
            };
            dynamicFields.push({name:name,type:type});
        }
        var store = this.store;
        var model = new Ext.data.Model({
            fields:dynamicFields
        });
        store.setModel(model);
        this.callParent();
    }
});
