Ext.define('KitchenSink.view.bzScoreMathCalcuter.bzScoreMathBasePage', {
    extend: 'Ext.panel.Panel',
    xtype: 'bzscoreBapage',
    controller: 'zsbfjgMgController',
    actType:'add',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.bzScoreMathCalcuter.bzScoreMathController',
        'Ext.selection.CellModel',
        'KitchenSink.view.bzScoreMathCalcuter.bzScoreMathBasePageStore',
        'KitchenSink.view.bzScoreMathCalcuter.bzScoreMathBasePageModel',
        'Ext.grid.plugin.CellEditing',
        'Ext.grid.plugin.BufferedRenderer'
    ],
    autoScroll:false,
    ignoreChangesFlag: true,//让框架程序不要提示用户保存的属性设置
    actType:'add',
    reference:'bzScoreMathCalcuter_bzscoreBapage',
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    title:Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_YSF_STD.ScoreMath","标准成绩计算器"),
    frame:true,
   /* listeners:{
        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
            var buttonHeight = 42;button height plus panel body padding
            var formHeight = 30;
            var formPadding = 20;
            var grid = panel.child('grid[name=appFormApplicants]');
            grid.setHeight( height- formHeight -buttonHeight-formPadding);
        }
    },*/
    initComponent:function(){
    	/*this.cellEditing = new Ext.grid.plugin.CellEditing({
            clicksToEdit: 1
        });*/
        var store= new KitchenSink.view.bzScoreMathCalcuter.bzScoreMathBasePageStore();
        
    	
    	/*var colum_nume=[];
    	colum_nume.push();*/
    	
        Ext.apply(this,{
            
                items: [
                	 {
                     layout: {
                       type: 'column'
                      },
                   items:[{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_YSF_STD.judgesNumber","每组最多评委数"),
                    labelWidth: 110,
                    name: 'adcertTmpl',
                    allowBlank: false,
                    maxLength:14,
                    style:"margin:10px",
                    width:400
                    
                  },{
                    xtype:'label',
                    text:'请注意：每组最多10位评委。',
                    cls: 'lable_1',
                    style:"margin:14px"
                 }]
                 },{
                  xtype: 'grid',
                  columnLines: true,
                  name:'appFormApplicants',
                  style:"margin:0px",             
                  selModel: {
                      type: 'checkboxmodel'
                  },
                  viewConfig : {
                    enableTextSelection:true
                  },
                  plugins: [                                          
                   	     {
                   	        ptype: 'cellediting',
                   	        clicksToEdit: 1
                   	    },{
                               ptype:'bufferedrenderer'
                           }
                    
                    ],
                  header:false,
                  
                 // layout:'fit',
                  height:489,
                  reference:'attachmentGrid',
                  frame:true,
                  dockedItems:[{
                  xtype:"toolbar",
                        items:[
                            {text:Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_YSF_STD.add","新增"),tooltip:Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_YSF_STD.adddata","新增数据"),iconCls:"add",handler:this.onAddClick},"-",
                            {text:Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_YSF_STD.edit","导入原始成绩"),tooltip:Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_YSF_STD.editdata","导入原始成绩"),handler:'importScore'},"-",
                            {text:Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_YSF_STD.remove","计算标准成绩"),tooltip:Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_YSF_STD.removedata","计算标准成绩"),handler:'calculationScore'}

                        ]
                    }],
                  columns:[
                  	{
                                text:Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_YSF_STD.rysbbh","人员识别编号") ,
                                dataIndex: 'xmid',
                                width: 110,
                                editor: {
                                   allowBlank: false
                                 }
                            },
                             {
                                 text:Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_YSF_STD.Name","姓名") ,
                                 dataIndex: 'xmName',
                                 width: 150,
                                 editor: {
                                   allowBlank: false
                                }
                             },
                             {
                             	text:Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_YSF_STD.teamID","组编号") ,
                                dataIndex: 'teamID',
                                width: 130,
                                editor: {
                                   allowBlank: false
                                }
                             },
                             {
                             	text:Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_YSF_STD.judgeUser","评委账号") ,
                                dataIndex: 'judgeUser',
                                width: 150,
                                editor: {
                                   allowBlank: false
                                }                             	
                             },{
                             	
                             	text:Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_YSF_STD.rawscore","原始成绩") ,
                                dataIndex: 'rawscore',
                                width: 100,
                                editor: {
                                   allowBlank: false
                                },
                                renderer: Ext.util.Format.numberRenderer('0.0000')
                                
                             },{
                             	text:Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_YSF_STD.judgescore","当前评委下考生成绩排名") ,
                                dataIndex: 'judgescore',
                                width: 170,
                                hidden:true,
                                editor: {
                                   allowBlank: false
                                },
                           
                             
                             },
                              {
                              text:Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_YSF_STD.handle","操作") ,
                              menuDisabled: true,
                              sortable: false,
                              width:60,
                              align:'center',
                              xtype: 'actioncolumn',
                              items:[
                                  {iconCls: 'add',tooltip:Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_YSF_STD.edit","编辑"),handler:this.onAddClick},
                                  {iconCls: 'remove',tooltip:Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_YSF_STD.delete","删除") ,handler:this.onRemoveClick}
                              ]
                             }],
                             store:store
                   /*bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 10,
                    store: store,
                    displayInfo: true,
                    displayMsg:"显示{0}-{1}条，共{2}条",
                    beforePageText:"第",
                    afterPageText:"页/共{0}页",
                    emptyMsg: "没有数据显示",
                    plugins: new Ext.ux.ProgressBarPager()
                }*/

                        }],

                buttons:[

                   /* {
                        text: Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_YSF_STD.save","保存"),
                        handler:'onschoolSave',
                        iconCls:'save'
                    },{
                        text:Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_YSF_STD.sure","确定"),
                        handler:'ensureonschoolSave',
                        iconCls:'ensure'
                    },*/{
                        text:Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_YSF_STD.close","关闭"),
                        iconCls:'close',
                        handler:'onSchoolClose'
                    }
                ]
        });
        this.callParent();
    },
        onAddClick: function(btn){
        // Create a model instance
        var rec = new KitchenSink.view.bzScoreMathCalcuter.bzScoreMathBasePageModel({
            xmid: '',
            xmName: '',
            teamID: 0,
            judgeUser: '',
            rawscore: '',
            judgescore:''
        });
        
        hbfsAllRecs = btn.findParentByType("grid[reference=attachmentGrid]").store.getRange();
        btn.findParentByType("grid[reference=attachmentGrid]").getStore().insert(hbfsAllRecs.length, rec);
       // this.cellEditing.startEdit(rec, 0);
    },
    onRemoveClick: function(grid, rowIndex){
        grid.getStore().removeAt(rowIndex);
    }
});






