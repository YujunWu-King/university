Ext.define('KitchenSink.view.recommend.recommendInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'recommendInfo',
    controller: 'recommendCon',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.grid.filters.Filters',
        'KitchenSink.view.recommend.recommendInfoController',
        'KitchenSink.view.recommend.recommendInfoStore'
    ],
    listeners:{
        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
//            var buttonHeight = 42;
//            var formHeight = panel.lookupReference('recommendInfoForm').getHeight();
//            var formPadding = 20;
//            var grid = panel.child('grid[name=recommendPerson]');
//            grid.setHeight( height- formHeight -buttonHeight-formPadding);
        }
    },
    bodyPadding:10,
    constructor: function (obj){
        this.orgColorSortStore=obj.orgColorSortStore;
        this.initData=obj.initData;
        this.stuGridColorSortFilterOptions=obj.stuGridColorSortFilterOptions;
        this.classID=obj.classID;
        this.callParent();
    },
    initComponent:function(){
        var me = this;
        var recommendInfoStore = new KitchenSink.view.recommend.recommendInfoStore();

        /*初始颜色类别数据*/
        var initData=me.initData;
        /*grid类别过滤数据*/
        var stuGridColorSortFilterOptions=me.stuGridColorSortFilterOptions;
        var orgColorSortStore =  me.orgColorSortStore;
        var validColorSortStore =  new KitchenSink.view.common.store.comboxStore({
            recname:'TZ_COLOR_SORT_T',
            condition:{TZ_JG_ID:{
                value:Ext.tzOrgID,
                operator:'01',
                type:'01'
            },TZ_COLOR_STATUS:{
                value:'N',
                operator:'02',
                type:'01'
            }},
            result:'TZ_COLOR_SORT_ID,TZ_COLOR_NAME,TZ_COLOR_CODE'
        });

        var dynamicColorSortStore = Ext.create("Ext.data.Store",{
            fields:[
                "TZ_COLOR_SORT_ID","TZ_COLOR_NAME","TZ_COLOR_CODE"
            ],
            data:initData
        });
        Ext.apply(this,{
            items: [{
                xtype: 'form',
                bodyPadding:'10px 0 10px 0',
                reference: 'recommendInfoForm',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyStyle:'overflow-y:auto;overflow-x:hidden',

                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 110,
                    labelStyle: 'font-weight:bold'
                },

                items: [{
                    xtype: 'textfield',
                    name: 'classID',
                    hidden:true
                },{
                    xtype: 'textfield',
                    fieldLabel: '班级',
                    name: 'className',
                    cls:'lanage_1',
                    readOnly:true
                },{
                    xtype: 'textfield',
                    fieldLabel: '批次',
                    name: 'batchName',
                    cls:'lanage_1',
                    readOnly:true
                }
                ]
            },{
                xtype: 'grid',
                height:500,
                header:false,
                dockedItems:[{
            		xtype:"toolbar",
            		items:[
            			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:'cfgSearchAct'},
            			"->",{
            				xtype:'splitbutton',
            				text:'更多操作',
            				iconCls:  'list',
            				glyph: 61,
            				menu:[{
            					text:'发送邮件',
            					handler:'sendEmail'
            				},{
            					text:'邮件发送史',
            					handler:'emailSendHistory'
            				}]
            			}
            		]
            	}],
                name:'recommendPerson',
                frame: true,
//                viewConfig : {
//                    enableTextSelection:true
//                },
                columnLines: true,
                listeners: {
                  afterrender: {
                    	fn: function(stuGrid){ 
                    		stuGrid.getStore().addListener("refresh",
                    				function(thisStore){
                    			stuGrid.getView().getSelectionModel().deselectAll();
                    		},this);
                    	}
                    }
                },
                selModel: {
                    type: 'checkboxmodel',
                },
                reference: 'stuGrid',
                multiSelect: true,
                store: recommendInfoStore,
                columns: [
                    {
                        text: '推荐人姓名',
                        dataIndex: 'referrerName',
        				flex: 1
                    },{ 
                        text: '工作单位',
                        dataIndex: 'companyName',
        				flex: 1
                    },{ 
                        text: '职位',
                        dataIndex: 'position',
        				flex: 1
                    },{ 
                        text: '邮箱',
                        dataIndex: 'email',
        				flex: 1
                    },{ 
                        text: '手机',
                        dataIndex: 'phone',
        				flex: 1
                    },{ 
                        text: '经管校友',
                        dataIndex: 'oldBoy',
        				flex: 1
                    },{ 
                        text: '推荐信状态',
                        dataIndex: 'letterState',
        				flex: 1
                    },{ 
                        text: '推荐考生',
                        dataIndex: 'stuName',
        				flex: 1
                    },{ 
                        text: '报名表状态',
                        dataIndex: 'letterState',
        				flex: 1
                    },{
                        menuDisabled: true,
                        sortable: false,
                        text: '操作',
                        align: 'center',
                        xtype: 'actioncolumn',
                        width:'300',
                        items:[
                               {iconCls: 'preview',tooltip: '查看推荐信',handler:'viewLetter'},
                               {iconCls: 'view',tooltip: '查看报名表',handler:'viewApplicationForm'}
                               ]
                    }
                ]
            }]
        })
        this.callParent();
    },
    title: '推荐人列表',
    bodyStyle:'overflow-y:hidden;overflow-x:hidden',
    buttons: [{
        text: Ext.tzGetResourse("TZ_TJR_MANAGER_COM.TZ_TJR_DETAIL_STD.close","关闭"),
        iconCls:"close",
        handler: 'onComRegClose'
    }]
});
