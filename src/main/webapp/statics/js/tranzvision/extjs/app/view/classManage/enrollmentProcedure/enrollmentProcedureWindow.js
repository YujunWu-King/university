Ext.define('KitchenSink.view.classManage.enrollmentProcedure.enrollmentProcedureWindow', {
    extend: 'Ext.window.Window',
    xtype: 'classEnrollmentProcedureWindow',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*'
    ],
    title: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bmlcsz","报名流程设置"),
    ignoreChangesFlag:true,
    reference:'classEnrollmentProcedureWindow',
    modal:true,
    width:500,
    editRecord:"",
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        bodyStyle:'overflow-y:auto;overflow-x:hidden',
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },
        items: [
            {
                xtype: 'textfield',
                fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bmlc_ame","流程名称"),
                name: 'bmlc_name',
                allowBlank:false
            }]
    }],
    buttons: [{
        text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.ensure","确定"),
        iconCls:"ensure",
        handler: function(btn){
            var actType =btn.findParentByType('window').actType;
            var form  = btn.findParentByType('window').child('form').getForm();
            if(actType=='add'){
                if(form.isValid()){
                    var store = btn.findParentByType('ClassInfo').down('grid[name=applyItemGrid]').getStore();
                    var index = store.findBy(function(record,id){
                        if(record.get('bmlc_name')==form.findField('bmlc_name').getValue()){
                            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.lcmccf","流程名称重复！"));
                            return true;
                        }
                    })
                    if(index==-1){
                        var model = new KitchenSink.view.classManage.BmlcModel({
                         bj_id:btn.findParentByType('ClassInfo').bj_id,
                         bmlc_id:'',
                         bmlc_xh:store.getCount()+1,
                         bmlc_name:form.findField('bmlc_name').getValue(),
                         bmlc_desc:form.findField('bmlc_name').getValue()
                        })
                        store.add(model);
                        btn.findParentByType('window').close();
                    }
                }
            }else{

                if(form.isValid()){
                    var editRecord=btn.findParentByType('window').editRecord;

                    var store = editRecord.store;
                    var index = store.findBy(function(record,id){
                        if(record!=editRecord&&record.get('bmlc_name')==form.findField('bmlc_name').getValue()){
                            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.lcmccf","流程名称重复！"));
                            return true;
                        }
                    })
                    if(index==-1){
                        editRecord.set('bmlc_name',form.findField('bmlc_name').getValue());
                        btn.findParentByType('window').close();
                    }
                }
            }
        }
    }, {
        text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.close","关闭"),
        iconCls:"close",
        handler: function(btn){
            btn.findParentByType('window').close();
        }
    }]
});