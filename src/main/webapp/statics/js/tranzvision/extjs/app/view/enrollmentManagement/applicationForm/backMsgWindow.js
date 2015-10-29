Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.backMsgWindow', {
    extend: 'Ext.window.Window',
    reference: 'auditAppFormBackMsgWindow',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollmentManagement.applicationForm.backMsgStore'
    ],
    title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.setReplyPhrases","常用回复短语设置"),
    actType : "add",//默认add
    width: 610,
    rowIndex:-1,
    height: 400,
    bodyStyle:'overflow-y:auto;overflow-x:hidden;',
    layout: 'fit',
    fileID:"",
    classID:"",
    auditAplicationFormPanel:[],
    resizable: true,
    listeners:{
        resize: function(win){
            win.doLayout();
        }
    },
    modal: true,
    items: [{
        xtype: 'grid',
        autoHeight: true,
        minHeight:300,
        bodyStyle:'overflow-y:auto;overflow-x:hidden;',
        viewConfig: {
            enableTextSelection: true
        },
        columnLines: true,
        dockedItems:[{
            xtype:"toolbar",
            items:[
                {text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.add","新增"),iconCls:"add",handler:'addLastBackMsg'}
            ]
        }],

        plugins: {
            ptype: 'cellediting',
            pluginId: 'dataCellediting',
            clicksToEdit: 1
        },

        viewConfig: {
            plugins: {
                ptype: 'gridviewdragdrop',
                containerScroll: true,
                dragGroup: this,
                dropGroup: this
            },
            listeners: {
                drop: function(node, data, dropRec, dropPosition) {
                    data.view.store.beginUpdate();
                    var items = data.view.store.data.items;
                    for(var i = 0;i< items.length;i++){
                        items[i].set('order',i+1);
                    }
                    data.view.store.endUpdate();
                }
            }
        },

        columns: [{
            text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.replyPhraseContent","回复短语内容"),
            dataIndex: 'msgContent',
            minWidth: 200,
            flex:1,
            editor: {
                xtype:'textfield'
            }
        },{
            menuDisabled: true,
            sortable: false,
            width:60,
            xtype: 'actioncolumn',
            align: 'center',
            items:[
                {iconCls: 'remove',tooltip: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.delete","删除"), handler: 'deleteBackMsg'}
            ]
        }],	//columns-end
        store: {
            type: 'classBackMsgStore'
        }
    }],

    buttons: [{
        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.save","保存"),
        iconCls:"save",
        handler: 'onBackMsgSave'
    }, {
        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.close","关闭"),
        iconCls:"close",
        handler: 'onBackMsgClose'
    }]
});
