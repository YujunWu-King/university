Ext.define('KitchenSink.view.template.kjgl.kjInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'kjInfo',
    controller: 'kj',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.template.kjgl.auditKjYtStore',
        'KitchenSink.view.template.kjgl.auditKjJygzStore',
        'KitchenSink.view.template.kjgl.kjController',
        'KitchenSink.view.template.kjgl.kjJygzWindow'
    ],
    actType: 'add',//默认新增,

   // constructor:function(cfg){
   //     this.kjInfoPanel=cfg;
   //     this.callParent();
   // },

    initComponent:function(){
      //  var kjInfoPanel = this.kjInfoPanel;

         var auditKjYtQyStore=new KitchenSink.view.common.store.appTransStore("TZ_QY_BZ");
         auditKjYtQyStore.load();
         var auditKjYtIdStore=new KitchenSink.view.common.store.appTransStore("TZ_COM_YT_ID");
         auditKjYtIdStore.load();
         var auditKjlxStore=new KitchenSink.view.common.store.appTransStore("TZ_COM_BMB_LX");
         auditKjlxStore.load();

        Ext.apply(this,{
            //kjInfoPanel:kjInfoPanel,
            items: [
                {
                xtype: 'form',
                reference: 'kjInfoForm',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,
                //heigth: 600,
                bodyStyle:'overflow-y:auto;overflow-x:hidden',

                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 110,
                    labelStyle: 'font-weight:bold'
                },

                items: [{
                    xtype: 'textfield',
                    fieldLabel: '控件编号',
                    name: 'kjID',
                    allowBlank:false,
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                }, {
                    xtype: 'textfield',
                    fieldLabel: '控件名称',
                    allowBlank:false,
                    name: 'kjName',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                },{
                    xtype: 'textfield',
                    fieldLabel: '控件英文名称',
                    allowBlank:false,
                    name: 'kjEnName',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                },{
                    xtype: 'textfield',
                    fieldLabel: 'JS文件路径',
                    allowBlank:false,
                    name: 'kjJsUrl',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                } , {
                    xtype: 'textfield',
                    fieldLabel: '图标路径',
                    allowBlank:false,
                    name: 'kjPtUrl',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                }, {
                    xtype: 'textarea',
                    fieldLabel: '导出HTML',
                    editable:true,
                    name: 'kjExpHtml'
                },
                  {
                        xtype: 'combo',
                        fieldLabel: '状态',
                        allowBlank:false,
                        queryMode:"local",
                        editable:false,
                        store:{
                            fields:[
                                {name:'statusCode'},
                                {name:'statusDesc'}
                            ],
                            data:[
                                {statusCode:'Y',statusDesc:'生效'},
                                {statusCode:'N',statusDesc:'失效'}
                            ]
                        },
                        valueField:'statusCode',
                        displayField:'statusDesc',
                        allowBlank:false,
                        value:'Y',/*默认有效*/
                        name: 'kjState',
                        afterLabelTextTpl: [
                            '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                        ]
                    }
                ]
            },
                {
                    xtype: 'tabpanel',
                    frame: true,
                    activeTab: 0,
                    plain:false,
                    style:'margin:0 10px 0 10px',
                    resizeTabs:true,
                    defaults :{
                        autoScroll: false
                    },
                    listeners:{
                        tabchange:function(tabPanel, newCard, oldCard){
                            var queryType;
                           // var form = tabPanel.findParentByType('kjInfoForm').child('form').getForm();
                            var form = tabPanel.ownerCt.child('form').getForm();
                            var kjID = form.findField('kjID').getValue();
                            //console.log(newCard.name);
                            if (newCard.name == "kjJygzGrid"){
                                queryType = "JYGZ";

                                var tzStoreParams;
                                //var kjJygzStore = newCard.down('grid[name=kjJygzGrid]').store;
                                //var kjJygzStore = newCard.down('grid[name=kjJygzGrid]');
                                var kjJygzStore =  tabPanel.down('grid[name=kjJygzGrid]').store;
                                if(!kjJygzStore.isLoaded()){
                                    tzStoreParams = '{"kjID":"'+kjID+'","queryType":"JYGZ"}';
                                    kjJygzStore.tzStoreParams = tzStoreParams;
                                    kjJygzStore.load();
                                }
                            }else{
                                queryType = "YT";
                                this.doLayout();
                            }
                        }
                    },
                    items:[
                        {
                            title: "控件用途",
                            xtype: 'grid',
                            autoHeight: true,
                            minHeight:150,
                            bodyBorder:false,
                            //frame: true,
                            columnLines: true,
//                        selModel: {
//                            type: 'checkboxmodel'
//                        },
                            viewConfig: {markDirty: false},
                            name: 'kjYtGrid',
                            reference: 'kjYtGrid',
                            plugins: {
                                ptype: 'cellediting',
                                pluginId: 'kjYtCellEditing',
                                clicksToEdit: 1
                            },
                            store: {
                                type: 'auditKjYtStore'
                            },
                            columns: [
                                {
                                    text: "序号",
                                    dataIndex: 'tz_order',
                                    hidden:true,
                                    minWidth:150,
                                    flex:1
                                },
                                {
                                    text: "控件编号",
                                    dataIndex: 'kjID',
                                    hidden:true,
                                    minWidth:150,
                                    flex:1
                                },{
                                    text: "是否启用",
                                    dataIndex: 'isQy',
                                    hidden:false,
                                    minWidth:150,
                                    flex:1,
                                    value:"Y",
                                    editor: {
                                        editable:true,
                                        xtype:"combo",
                                        forceSelection: true,
                                        valueField: 'TValue',
                                        displayField: 'TSDesc',
                                        store: auditKjYtQyStore,
                                        typeAhead:true,
                                        mode: 'remote'
                                    },
                                    renderer:function(v,metadata,record){
                                        if(v){
                                            var rec = auditKjYtQyStore.find('TValue',v);
                                            if(rec>-1){
                                                return auditKjYtQyStore.getAt(rec).get("TSDesc");
                                            }else{
                                                return "";
                                            }
                                        }else{
                                            return "";
                                        }
                                    }
                                },
                                {
                                    text: "控件用途",
                                    dataIndex: 'kjYtIDMx',
                                    minWidth:150,
                                    flex:1
                                },
                                {
                                    text: "控件用途ID",
                                    dataIndex: 'kjYtID',
                                    sortable: false,
                                    minWidth:150,
                                    hidden:true,
                                    flex:1,
                                    editor: {
                                        editable:true,
                                        xtype:"combo",
                                        forceSelection: true,
                                        valueField: 'TValue',
                                        displayField: 'TSDesc',
                                        store: auditKjYtIdStore,
                                        mode:'remote'
                                    },
                                    renderer:function(v,metadata,record){
                                        if(v){
                                            var rec = auditKjYtIdStore.find('TValue',v);
                                            if(rec>-1){
                                                return auditKjYtIdStore.getAt(rec).get("TSDesc");
                                            }else{
                                                return "";
                                            }
                                        }else{
                                            return "";
                                        }
                                    }
                                },
                                {
                                    text: "控件类型",
                                    dataIndex: 'kjLx',
                                    sortable: false,
                                    minWidth:150,
                                    flex:1,
                                    editor: {
                                        editable:true,
                                        xtype:"combo",
                                        forceSelection: true,
                                        valueField: 'TValue',
                                        displayField: 'TSDesc',
                                        store: auditKjlxStore,
                                        mode:'remote'
                                    },
                                    renderer:function(v){
                                        if(v){
                                            var rec = auditKjlxStore.find('TValue',v);
                                            if(rec>-1){
                                                return auditKjlxStore.getAt(rec).get("TSDesc");
                                            }else{
                                                return "";
                                            }
                                        }else{
                                            return "";
                                        }
                                    }
                                }
                            ]
                        },
                        {
                            title: "控件校验规则",
                            xtype: 'grid',
                            autoHeight: true,
                            minHeight:150,
                            bodyBorder:false,
                            //frame: true,
                            columnLines: true,
                            selModel: {
                                type: 'checkboxmodel'
                            },
                            viewConfig: {markDirty: false},
                            name: 'kjJygzGrid',
                            reference: 'kjJygzGrid',
                            plugins: {
                                ptype: 'cellediting',
                                pluginId: 'kjJygzCellEditing',
                                clicksToEdit: 1
                            },
                            store: {
                                type: 'auditKjJygzStore'
                            },
                            dockedItems:[
                                {
                                    xtype:"toolbar",
                                    items:[
                                        {text:"新增",tooltip:"新增控件校验规则",iconCls:"add",handler:'addKjJygz'},"-",
                                        {text:"编辑",tooltip:"编辑选中的校验规则",name:"toolbarEdit",iconCls:"edit",handler:'editKjJygz'},"-",
                                        {text:"删除",tooltip:"删除选中的校验规则",iconCls:"remove",handler:'deleteKjJygz'}
                                    ]
                                }
                            ],
                            viewConfig: {
                                plugins: {
                                    ptype: 'gridviewdragdrop',
                                    dragText: '拖拽进行选项的排序'
                                },
                                listeners: {
                                    drop: function(node, data, dropRec, dropPosition) {

                                        data.view.store.beginUpdate();
                                        var items = data.view.store.data.items;

                                        for (var i = 0; i < items.length; i++) {
                                            items[i].set('tz_order', i + 1);
                                        }
                                        data.view.store.endUpdate();
                                    }
                                }
                            },
                            columns: [
                                {
                                    text: "序号",
                                    dataIndex: 'tz_order',
                                    hidden:true,
                                    minWidth:150,
                                    flex:1
                                },
                                {
                                    text: "控件编号",
                                    dataIndex: 'kjID',
                                    hidden:true,
                                    minWidth:150,
                                    flex:1
                                },{
                                    text: "是否启用",
                                    dataIndex: 'isQy',
                                    hidden:false,
                                    minWidth:150,
                                    flex:1,
                                    editor: {
                                        editable:false,
                                        xtype:"combo",
                                        forceSelection: true,
                                        valueField: 'TValue',
                                        displayField: 'TSDesc',
                                        store: auditKjYtQyStore,
                                        typeAhead:true,
                                        mode: 'remote'
                                    },
                                    renderer:function(v,metadata,record){
                                        if(v){
                                            var rec = auditKjYtQyStore.find('TValue',v);
                                            if(rec>-1){
                                                return auditKjYtQyStore.getAt(rec).get("TSDesc");
                                            }else{
                                                return "";
                                            }
                                        }else{
                                            return "";
                                        }
                                    }
                                },
                                {
                                    text: "校验规则ID",
                                    dataIndex: 'kjJygzID',
                                    hidden:true,
                                    minWidth:150,
                                    flex:1
                                },
                                {
                                    text: "校验规则名称",
                                    dataIndex: 'kjJygzMx',
                                    minWidth:150,
                                    flex:1
                                },
                                {
                                    text: '操作',
                                    menuDisabled: true,
                                    sortable: false,
                                    width:60,
                                    xtype: 'actioncolumn',
                                    items:[
                                        {iconCls: 'edit',tooltip: '编辑',handler:'currKjJygzEdit'},
                                        {iconCls: 'remove',tooltip: '删除',handler:'deleteKjJygz'}
                                    ]
                                }
                            ]
                        }]
                }
            ]
        });

        this.callParent();
    },
    title: '控件信息',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    buttons: [{
        text: '保存',
        iconCls:"save",
        handler: 'onKjInfoSave'
    }, {
        text: '确定',
        iconCls:"ensure",
        handler: 'onKjInfoEnsure'
    }, {
        text: '关闭',
        iconCls:"close",
        handler: 'onKjInfoClose'
    }]
});


