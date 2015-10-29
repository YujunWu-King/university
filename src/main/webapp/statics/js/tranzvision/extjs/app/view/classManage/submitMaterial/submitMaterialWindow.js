Ext.define('KitchenSink.view.classManage.submitMaterial.submitMaterialWindow', {
    extend: 'Ext.window.Window',
    xtype: 'classSubmitMaterialWindow',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*'
    ],
    title: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.djzlsz","递交资料设置"),
    ignoreChangesFlag:true,
    reference:'classSubmitMaterialWindow',
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
                fieldLabel:  Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nrjc","内容简称"),
                name: 'djzl_name',
                allowBlank:false
            },
            {
                xtype: 'textfield',
                fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.remark","备注"),
                name: 'djzl_bz'
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
                    var store = btn.findParentByType('ClassInfo').down('grid[name=applyItemGrid1]').getStore();
                    var index = store.findBy(function(record,id){
                        if(record.get('djzl_name')==form.findField('djzl_name').getValue()){
                            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.djzlnrcf","递交资料内容重复！"));
                            return true;
                        }
                    })
                    if(index==-1){
                        var model = new KitchenSink.view.classManage.DjzlModel({
                            bj_id:btn.findParentByType('ClassInfo').bj_id,
                            djzl_id:'',
                            djzl_xh:store.getCount()+1,
                            djzl_name:form.findField('djzl_name').getValue(),
                            djzl_bz:form.findField('djzl_bz').getValue()
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
                        if(record!=editRecord&&record.get('djzl_name')==form.findField('djzl_name').getValue()){
                            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.djzlnrcf","递交资料内容重复！"));
                            return true;
                        }
                    })
                    if(index==-1){
                        editRecord.set('djzl_name',form.findField('djzl_name').getValue());
                        editRecord.set('djzl_bz',form.findField('djzl_bz').getValue());
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