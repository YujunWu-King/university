Ext.define('KitchenSink.view.clueManagement.clueManagement.clueProblemPanel',{
    extend:'Ext.grid.Panel',
    xtype:'clueProblemPanel',
    controller:'clueProblemController',
    requires:[
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'tranzvision.extension.grid.feature.CheckboxGrouping',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.clueManagement.clueManagement.clueProblemStore',
        'KitchenSink.view.clueManagement.clueManagement.clueProblemController'
    ],
    title:'有问题的线索',
    reference:'clueProblemPanel',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    header:false,
    frame:true,
    dockedItems:[
        {
            xtype:"toolbar",
            dock:"bottom",
            ui:"footer",
            items:['->',
                {minWidth:80,text:"系统建议操作",iconCls:"send",handler:"suggestClueProblem"},
                {minWidth:80,text:"选中线索确认",iconCls:"ensure",handler:"ensureClueProblem"},
                {minWidth:80,text:"关闭",iconCls:"close",handler:"closeClueProblem"}
            ]
        }
    ],
    plugins:[
        {
            ptype: 'cellediting',
            clicksToEdit: 1
        }
    ],
    initComponent:function() {

        var store = new KitchenSink.view.clueManagement.clueManagement.clueProblemStore();
        /*加载完成后默认全部展开*/
        store.load({
            callback:function(records,successful,operation,eOpts) {
                var currentView = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab();
                var feature = currentView.view.findFeature('CheckboxGrouping');
                feature.expandAll();
            }
        });

        //建议操作store
        var jyczStore = new KitchenSink.view.common.store.appTransStore("TZ_WTXS_JYCZ");

        Ext.apply(this,{
            collapsible:true,
            columns:[{
                xtype: 'checkcolumn',
                dataIndex: 'isChecked',
                hidden:true
            },{
                text:'线索客户',
                dataIndex:'clueDesc',
                width:220,
                flex:1,
                hidden:true
            },{
                text:'姓名',
                dataIndex:'name',
                width:130
            },{
                text:'手机',
                dataIndex:'mobile',
                width:130
            },{
                text:'公司',
                dataIndex:'company',
                width:150
            },{
                text:'创建方式',
                dataIndex:'createWayDesc',
                width:160
            },{
                text:'常住地',
                dataIndex:'localName',
                width:110
            },{
                text:'创建时间',
                dataIndex:'createDttm',
                width:160,
                renderer : Ext.util.Format.dateRenderer('Y-m-d H:i')
            },{
                dataIndex: 'chargeOprid',
                hidden:true
            },{
                text:'责任人',
                dataIndex:'chargeName',
                width:130,
                editor:{
                    xtype:'textfield',
                    editable:false,
                    triggers: {
                        /*
                        clear: {
                            cls: 'x-form-clear-trigger',
                            handler: 'clearCharge'
                        },
                        */
                        search: {
                            cls: 'x-form-search-trigger',
                            handler: "searchCharge"
                        }
                    }
                }
            },{
                text:'建议操作',
                dataIndex:'suggestOperation',
                store: jyczStore,
                valueField: 'TValue',
                displayField: 'TSDesc',
                width:160,
                editor:{
                    xtype:'combo',
                    store: jyczStore,
                    autoShow: true,
                    valueField: 'TValue',
                    displayField: 'TSDesc',
                    queryMode: 'local',
                    editable: false,
                    allowBlank: false
                },
                renderer: function(value,metadata,record){
                    var index = jyczStore.find('TValue',value);
                    if(index!=-1){
                        return jyczStore.getAt(index).data.TSDesc;
                    }
                    return value;
                }
            },{
                text:'系统规则提示',
                dataIndex:'systemRuleTip',
                width:160,
                flex:1
            }],
            store:store,
            features:[{
                ftype:'CheckboxGrouping',
                hideGroupHeader: true,
                enableGroupingMenu: false
                //startCollapsed:false
            }]
        });

        this.callParent();
    }
});

