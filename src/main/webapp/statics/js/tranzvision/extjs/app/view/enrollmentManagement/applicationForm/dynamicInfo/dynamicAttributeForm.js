Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.dynamicInfo.dynamicAttributeForm', {
    extend: 'Ext.form.Panel',
    xtype:'dynamicAttrForm',
    minHeight: 150,
    autoHeight:true,
    fieldDefaults:{
        msgTarget: 'side',
        labelStyle:'font-weight:bold;',
        labelWidth:110
    },
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    border: false,
    bodyPadding: 10,
    appInsID:'',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    initComponent:function(){
        var conItems = [];
        var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_AUDIT_STD","OperateType":"getMoreInfoItems","comParams":{"appInsID":"'+this.appInsID+'"}}';
        Ext.tzLoadAsync(tzParams,function(responseData){
            var formData = responseData.formData;
            for(var fieldName in formData) {
                //字段类型
                var fldType = formData[fieldName].fldType;
                //如果是下拉框,则初始化下拉框默认值;
                var downCondition = null;
                var downConditionStr = "";
                if(fldType == "L"){
                    downConditionStr = '{ "TZ_JG_ID":{ "value" : "'+Ext.orgId+'", "operator":"01", "type":"01"}}';
                    downCondition = Ext.JSON.decode(downConditionStr);
                }

                var show_name=formData[fieldName].show_name;
                var fieldValue=formData[fieldName].zd_name;
                var typeField = {};
                switch (fldType)
                {
                    case 'T'://字符串
                        typeField = {
                            xtype: 'textfield',
                            columnWidth: 1,
                            fieldLabel:show_name,
                            hideEmptyLabel: true,
                            name: fieldName,
                            value: fieldValue
                        };
                        break;
                    case 'N'://数字
                        typeField = {
                            xtype: 'numberfield',
                            columnWidth: 1,
                            hideEmptyLabel: true,
                            value: fieldValue,
                            fieldLabel:show_name,
                            name: fieldName
                        };
                        break;
                    case 'D'://日期
                        typeField = {
                            xtype: 'datefield',
                            columnWidth: 1,
                            hideEmptyLabel: true,
                            format: 'Y-m-d',
                            fieldLabel:show_name,
                            value: fieldValue,
                            name: fieldName
                        };
                        break;
                    case 'L'://下拉框
                        typeField = {
                            xtype: 'combobox',
                            columnWidth: 1,
                            autoSelect: false,
                            editable:false,
                            fieldLabel:show_name,
                            store: new KitchenSink.view.common.store.comboxStore({
                                recname: 'TZ_F_ATTR_OPT_T',
                                condition:{
                                    TZ_IS_USED:{
                                        value:'Y',
                                        operator:"01",
                                        type:"01"
                                    },
                                    TZ_JG_ID:{
                                        value:Ext.tzOrgID,
                                        operator:"01",
                                        type:"01"
                                    },
                                    TZ_ATTRIBUTE_ID:{
                                        value:fieldName,
                                        operator:"01",
                                        type:"01"
                                    }
                                },
                                result:'TZ_DROP_DOWN_ID,TZ_DROP_DOWN_VALUE'
                            }),
                            valueField: 'TZ_DROP_DOWN_ID',
                            displayField: 'TZ_DROP_DOWN_VALUE',
                            queryMode: 'remote',
                            name: fieldName,
                            value: fieldValue
                        };
                        break;
                    default:
                        typeField = {
                            xtype: 'textfield',
                            columnWidth: 1,
                            hideEmptyLabel: true,
                            value: fieldValue,
                            name: fieldName
                        };
                }
                conItems.push(typeField);
            }
        });
        Ext.apply(this,{
            items: conItems
        });
        this.callParent();
    }
});
