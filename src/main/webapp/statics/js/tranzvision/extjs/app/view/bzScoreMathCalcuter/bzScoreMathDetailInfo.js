
Ext.define('KitchenSink.view.bzScoreMathCalcuter.bzScoreMathDetailInfo', {
    extend: 'Ext.window.Window',
    xtype: 'bzscoreDelinfo',
    controller: 'zsbfjgMgController',
    actType:'add',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'Ext.selection.CellModel',
        'KitchenSink.view.bzScoreMathCalcuter.bzScoreMathController',
        'KitchenSink.view.bzScoreMathCalcuter.bzScoreMathDetailStore'
    ],
    ignoreChangesFlag: true,//让框架程序不要提示用户保存的属性设置
    reference:"bzscoreDelinfo",
    autoScroll:false,
    actType:'add',
    id:'bzscoreDelinfo_Delinfo',
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    title:Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.ScoreMath","标准成绩计算器"),
    frame:true,
    height:500,
    width:1000,
    //初始化传值
    constructor: function (config) {
        this.coulumdt = config.coulumdt;
        this.callParent();
    },
    initComponent:function(){
    	this.cellEditing = new Ext.grid.plugin.CellEditing({
            clicksToEdit: 1
        });
    	var store=new KitchenSink.view.bzScoreMathCalcuter.bzScoreMathDetailStore();
    	var coulumdt=this.coulumdt;
    	var coulum_num=[];
    	coulum_num.push({
                                    text: Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.rysbbh", "人员识别编号"),
                                    dataIndex: 'xmid',
                                    editor: {
                                           allowBlank: false
                                        },                                    
                                    width: 110
                                },
                                    {
                                        text: Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.Name", "姓名"),
                                        dataIndex: 'xmName',
                                        editor: {
                                           allowBlank: false
                                        },                                        
                                        width: 150
                                    },
                                    {
                                        text: Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.teamID", "组编号"),
                                        dataIndex: 'teamID',
                                        editor: {
                                           allowBlank: false
                                        },
                                        width: 130
                                    }
                                    );
                                    for( var i=1;i<=coulumdt;i++){
                                    coulum_num.push(
                                    {
                                        text: Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.judge_"+i, "评委"+i+"编号"),
                                        dataIndex: 'judge_'+i+'_id',
                                        editor: {
                                           allowBlank: false
                                        },
                                        width: 80
                                    },
                                    {
                                        text: Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.judge_score"+i, "评委"+i+"原始分"),
                                        dataIndex: 'judge_'+i+'_score',
                                         editor: {
                                           allowBlank: false
                                        },                                       
                                        width: 98,
                                        renderer: Ext.util.Format.numberRenderer('0.0000')
                                    },{
                                        text: Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.judge_zscore"+i, "评委"+i+"标准分数"),
                                        dataIndex: 'judge_'+i+'_zscore',
                                        editor: {
                                           allowBlank: false
                                        },                                        
                                        width: 108,
                                        renderer: Ext.util.Format.numberRenderer('0.0000')
                                    },{
                                        text: Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.judge_rank"+i, "评委"+i+"排名"),
                                        dataIndex: 'judge_'+i+'_rank',
                                        editor: {
                                           allowBlank: false
                                        },                                        
                                        width: 80
                                    },{
                                        text: Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.judge_num"+i, "评委"+i+"评审人数"),
                                        dataIndex: 'judge_'+i+'_num',
                                        editor: {
                                           allowBlank: false
                                        },                                        
                                        width: 108
                                    })
                                    }
                                    coulum_num.push({
                                        text: Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.judge_sunscore", "评委标准分数和"),
                                        dataIndex: 'judge_sunscore',
                                        editor: {
                                           allowBlank: false
                                        },                                        
                                        width: 120,
                                        renderer: Ext.util.Format.numberRenderer('0.0000')
                                    },{
                                        text: Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.judge_sunrank"+i, "评委标准分数和组内排名"),
                                        dataIndex: 'judge_sunrank',
                                        editor: {
                                           allowBlank: false
                                        },                                        
                                        width: 180
                                    },{
                                        text: Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.handle", "操作"),
                                        menuDisabled: true,
                                        sortable: false,
                                        width: 60,
                                        align: 'center',
                                        xtype: 'actioncolumn',
                                        items: [
                                            {
                                                iconCls: 'add',
                                                tooltip: Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.edit", "编辑"),
                                                handler: this.onAddClick
                                            },
                                            {
                                                iconCls: 'remove',
                                                tooltip: Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.delete", "删除"),
                                                handler: this.onRemoveClick
                                            }
                                        ]
                                    });
                                   
                                    
        Ext.apply(this,{

                items: [
                    {
                            xtype: 'textfield',
                            fieldLabel: Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.heightscore", "评委最高分"),
                            labelWidth: 110,
                            name: 'heightscore',
                            allowBlank: false,
                            maxLength: 14,
                            style: "margin:10px",
                            width: 400,
                            renderer: Ext.util.Format.numberRenderer('0.0000')

                        },
                            {
                                xtype: 'textfield',
                                fieldLabel: Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.lowscore", "评委最低分"),
                                labelWidth: 110,
                                name: 'lowscore',
                                allowBlank: false,
                                maxLength: 14,
                                style: "margin:10px",
                                width: 400,
                                renderer: Ext.util.Format.numberRenderer('0.0000')

                            },
                             {
                            xtype: 'textfield',
                            fieldLabel: Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.heightscore", "评委最高分"),
                            labelWidth: 110,
                            name: 'ismathflag',
                            allowBlank: false,
                            maxLength: 14,
                            style: "margin:10px",
                            width: 400,
                            hidden:true,
                            renderer: Ext.util.Format.numberRenderer('0.0000')

                        },

                            {
                                xtype: 'grid',
                                columnLines: true,
                                style: "margin:0px",
                                selModel: {
                                    type: 'checkboxmodel'
                                },
                                plugins: [this.cellEditing],
                                layout: 'fit',
                                minHeight: 340,
                                frame: true,
                                reference:'afterBzscoreGrid',
                                dockedItems: [{
                                    xtype: "toolbar",
                                    items: [
                                        {
                                            text: Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.importexcel", "导入加工后EXCEL"),
                                            tooltip: Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.importlastexcel", "导入加工后EXCEL"),                        
                                            handler: 'importlastexcel'
                                        }, "-",
                                        {
                                            text: Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.mathScore", "计算标准成绩"),
                                            tooltip: Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.mathScore", "计算标准成绩"),
                                            handler: 'CalculationBzScore'
                                        }, "-",
                                        {
                                            text: Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.checkpersonid", "检查人员识别编号时候合法"),
                                            tooltip: Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.checkpersonid", "检查人员识别编号时候合法"),
                                            handler: 'checkPerson'
                                        }, "-",
                                        {
                                            text: Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.viewStudenInfo", "查看考生面试信息"),
                                            tooltip: Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.viewStudenInfo", "查看考生面试信息"),
                                            handler: 'exportStudentInform'
                                        }

                                    ]
                                }],
                                columns: coulum_num,
                                store:store
                                /*bbar: {
                                    xtype: 'pagingtoolbar',
                                    pageSize: 10,
                                    // store: store,
                                    displayInfo: true,
                                    displayMsg: "显示{0}-{1}条，共{2}条",
                                    beforePageText: "第",
                                    afterPageText: "页/共{0}页",
                                    emptyMsg: "没有数据显示",
                                    plugins: new Ext.ux.ProgressBarPager()
                                }*/

                    }],
                    
            buttons:[
            /*{
                text: Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.save","保存"),
                handler:'DelinfoSave',
                iconCls:'save'
            },{
                text:Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.sure","确定"),
                handler:'ensureonDelinfoSave',
                iconCls:'ensure'
            },*/{
                text:Ext.tzGetResourse("TZ_BZCJ_SRC_COM.TZ_BZCJ_JIS_STD.close","关闭"),
                iconCls:'close',
                handler:'DelinfoClose'
            }]
        
    });
this.callParent();
},
onAddClick: function(btn){
        // Create a model instance
        var rec = new KitchenSink.view.bzScoreMathCalcuter.bzScoreMathDetailModel({
            xmid: '',
            xmName: '',
            teamID: '',
            judgeUser: '',
            rawscore: '',
            judgescore:''
        });
        
        hbfsAllRecs = btn.findParentByType("grid[reference=afterBzscoreGrid]").store.getRange();
        btn.findParentByType("grid[reference=afterBzscoreGrid]").getStore().insert(hbfsAllRecs.length, rec);
       // this.cellEditing.startEdit(rec, 0);
    },
    onRemoveClick: function(grid, rowIndex){
        grid.getStore().removeAt(rowIndex);
    }
});






