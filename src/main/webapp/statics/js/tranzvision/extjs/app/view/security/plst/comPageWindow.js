Ext.define('KitchenSink.view.security.plst.comPageWindow', {
    extend: 'Ext.window.Window',
    reference: 'comPageWindow',
    controller: 'plstMg',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.security.plst.comPageModel',
        'KitchenSink.view.security.plst.comPageStore'
    ],
    title: '页面许可权',
    bodyStyle:'overflow-y:hidden;overflow-x:hidden;padding-top:10px',
    actType: 'update',
    width: 650,
    y:10,
    minWidth: 400,
    minHeight: 350,
    maxHeight: 460,
    resizable: true,
    modal:true,
    listeners:{
        resize: function(win){
            win.doLayout();
        }
    },
    viewConfig: {
        enableTextSelection: true
    },
    items: [{
        xtype: 'form',
        reference: 'comPageForm',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        fieldDefaults: {
            msgTarget: 'side',
            labelStyle: 'font-weight:bold'
        },
        border: false,
        bodyPadding: 10,
        bodyStyle:'overflow-y:auto;overflow-x:hidden',

        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },

        items: [{
            xtype: 'textfield',
            fieldLabel: '组件ID',
            name: 'comID',
            readOnly:true,
            fieldStyle:'background:#F4F4F4'
        }, {
            xtype: 'textfield',
            fieldLabel: '组件名称',
            name: 'comName',
            readOnly:true,
            fieldStyle:'background:#F4F4F4'
        }]
    },{
        xtype: 'grid',
        height: 'auto',
        title: '页面列表',
        autoHeight:true,
        minHeight:120,
        maxHeight:250,
        columnLines: true,
        reference: 'comPageGrid',
        //style:"margin:10px",
        store: {
            type: 'comPageStore'
        },
        columns: [{
            text: '页面ID',
            dataIndex: 'pageID',
            width: 230,
            sortable:false
        },{
            text: '页面名称',
            dataIndex: 'pageName',
            minWidth: 250,
            sortable:false,
            flex:1
        },{
            xtype: 'checkcolumn',
            text: "只读",
            dataIndex: 'readonly',
            listeners:{
                checkchange:function(item, rowIndex, checked, eOpts ){
                    var store = item.findParentByType("grid").store;
                    var record = store.getAt(rowIndex);

                    if(checked){
                        record.set('modify',false);
                    }
                }
            },
            sortable:false,
            width: 70
        },{
            xtype: 'checkcolumn',
            text: '修改',
            dataIndex: 'modify',
            sortable:false,
            listeners:{
                checkchange:function(item, rowIndex, checked, eOpts ){
                    var store = item.findParentByType("grid").store;
                    var record = store.getAt(rowIndex);

                    if(checked){
                        record.set('readonly',false);
                    }
                }
            },
            width: 70
        }],
        bbar: {
            xtype: 'pagingtoolbar',
            pageSize: 5,
            reference: 'rolePlstToolBar',
            listeners:{
                afterrender: function(pbar){
                    var grid = pbar.findParentByType("grid");
                    pbar.setStore(grid.store);
                }
            },
            plugins: new Ext.ux.ProgressBarPager()
        }
    }],
    buttons: [{
        text: '保存',
        iconCls:"save",
        handler: 'onPlstComInfoSave'
    }, {
        text: '确定',
        iconCls:"ensure",
        handler: 'onPlstComInfoEnsure'
    },{
        text: '关闭',
        iconCls:"close",
        handler: function(btn){
            //获取窗口
            var win = btn.findParentByType("window");
            var form = win.child("form").getForm();
            //关闭窗口
            win.close();
        }
    }]
});
