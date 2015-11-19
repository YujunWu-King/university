Ext.define('KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewApplicants', {
    extend: 'Ext.panel.Panel',
    xtype: 'GSMmaterialsReviewApplicants',
    controller: 'GSMmaterialsReview',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'Ext.grid.filters.Filters',
        'KitchenSink.AdvancedVType',
        'KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewApplicantsStore',
        'tranzvision.extension.grid.Exporter'
    ],
    title: '材料评审申请人名单',
    classID:'',
    batchID:'',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    initComponent:function(){
        var judgeGroupStore = new KitchenSink.view.common.store.comboxStore({
            recname:'TZ_PWZDY_T',
            condition:{
                TZ_JUGTYP_STAT:{
                    value:'Y',
                    operator:'01',
                    type:'01'
                }},
            result:'TZ_PWZBH,TZ_PWZMS'
        });
        Ext.define('filter', {
            extend: 'Ext.grid.filters.Filters'

        });

        Ext.apply(this,{
            items: [{
                xtype: 'form',
                reference: 'GSMmaterialsReviewApplicantsForm',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,
                bodyStyle:'overflow-y:auto;overflow-x:hidden',

                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 130,
                    labelStyle: 'font-weight:bold'
                },

                items: [
                    {
                        xtype: 'textfield',
                        name: 'classID',
                        hidden:true
                    },
                    {
                        xtype: 'textfield',
                        name: 'batchID',
                        hidden:true
                    },{
                        xtype: 'textfield',
                        fieldLabel: "招聘项目名称",
                        name: 'className',
                        fieldStyle:'background:#F4F4F4',
                        readOnly:true
                    }, {
                        xtype: 'textfield',
                        fieldLabel: "批次",
                        name: 'batchName',
                        fieldStyle:'background:#F4F4F4',
                        readOnly:true
                    }, {
                        xtype: 'numberfield',
                        fieldLabel: "总评审人数",
                        name: 'applicantsNumber',
                        fieldStyle:'background:#F4F4F4',
                        readOnly:true,
                        hidden:true
                    }, {
                        xtype: 'numberfield',
                        fieldLabel: "已选择评审人数",
                        name: 'materialsReviewApplicantsNumber',
                        fieldStyle:'background:#F4F4F4',
                        readOnly:true
                    },{
                        layout: {
                            type: 'column'
                        } ,
                        padding:'0 0 8px 0',
                        items:[{
                            xtype:'displayfield',
                            fieldLabel:'评审状态'
                        },{
                            style:'margin-left:0',
                            xtype: 'button',
                            flagType:'positive',
                            name:'startReview',
                            defaultColor:'',
                            setType:0,
                            text: '开启评审',
                            handler: 'startReview',
                            width: 90
                        },{
                            style:'margin-left:10px',
                            xtype:'button',
                            text:'关闭评审',
                            defaultColor:'',
                            name:'closeReview',
                            flagType:'positive',
                            setType:0,
                            handler:'closeReview',
                            width:90
                        }]
                    },
                    {
                        xtype: 'grid',
                        title:'考生名单',
                        minHeight: 260,
                        name:'GSMmaterialsReviewApplicantsGrid',
                        reference:'GSMmaterialsReviewApplicantsGrid',
                        columnLines: true,
                        autoHeight: true,
                        frame:true,
                        selModel: {
                            selType: 'checkboxmodel'
                        },
                        dockedItems:[/*{
                         xtype:"toolbar",
                         dock:"bottom",
                         ui:"footer",
                         items:['->',{minWidth:80,text:"保存",iconCls:"save"}]
                         },*/{
                            xtype:"toolbar",
                            items:["-",
                               // {text:"查询",tooltip:"查询参与本批次材料评审的考生",iconCls:"query",handler:"queryApplicants"},"-",
                                {text:"新增",tooltip:"添加考生参与本批次材料评审",iconCls:"add",handler:"addApplicants"},"-",
                                {text: "编辑",tooltip: "编辑选中考生", iconCls:"edit", handler: 'editApplicants'},"-",
                                {text:"删除",tooltip:"从列表中移除选中的考生",iconCls:"remove",handler:"removeApplicants"},"-",
                                {text:"导出",tooltip:"导出考生列表", iconCls:"excel",handler:"exportExcelApplicants"},"->",
                                {
                                    xtype:'splitbutton',
                                    text:'更多操作',
                                    iconCls:  'list',
                                    glyph: 61,
                                    menu:[
                                        { text: '设置面试资格',handler: 'setOwnQuary'}
                                    ]  } ]

                        }],

                        plugins: [{
                            ptype: 'cellediting'

                        },{
                            ptype: 'gridfilters'

                        },{
                            ptype: 'gridexporter'

    }
                        ],
                        store:{
                            type:'materialsReviewApplicants'
                        },
                        columns: [
                           {
                                text: "报名表编号",
                                dataIndex: 'appInsID',
                                width:130,
                                filter: {
                                    type: 'string',
                                    itemDefaults: {
                                        emptyText: 'Search for...'
                                    }
                                },
                                align:'center'
                            }
                            ,{
                                text: "姓名",
                                dataIndex: 'realName',
                                width:130,
                                filter: {
                                    type: 'string',
                                    itemDefaults: {
                                        emptyText: 'Search for...'
                                    }
                                },
                                align:'center'
                            },{
                                text: "性别",
                                dataIndex: 'gender',
                                width:70,
                                filter: {
                                    type: 'list'
                                },
                                align:'center'
                            },{
                                text: "申请职位",
                                dataIndex: 'applyPosition',
                                width:150,
                                filter: {
                                    type: 'list'
                                },
                                align:'center'
                            },{
                                text: "志愿顺序",
                                dataIndex: 'intentOrder',
                                width:150,
                                filter: {
                                    type: 'list'
                                },
                                align:'center'
                            },{
                                text: "志愿系",
                                dataIndex: 'intentDepart',
                                flex:1,
                                filter: {
                                    type: 'list'
                                },
                                align:'center'
                            },{
                                text: "评委账号",
                                dataIndex: 'judgeList',
                                // minWidth:130,
                                flex:1,
                                align:'center',
                                hidden:true
//                                renderer:function(v) {
//                                    if (v) {
//                                        return '<a  href = "javaScript:void(0)" >' + v + '</a>';
//                                    } else {
//                                        return "";
//                                    }}
                            },{
                                text: "面试资格",
                                dataIndex: 'interviewQualification',
                                width:100,
                                filter: {
                                    type: 'list'
                                }
                            },{
                                menuDisabled: true,
                                text: "操作",
                                sortable: false,
                                width:80,
                                xtype: 'actioncolumn',
                                items:[
                                    {iconCls: 'remove',tooltip: '移除考生',  handler: function(view, rowIndex){
                                        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                                            if(btnId == 'yes'){
                                                var store = view.findParentByType("grid").store;
                                                store.removeAt(rowIndex);
//                                                if (store.getAt(rowIndex).data.judgeList!=0)
//                                                {Ext.Msg.alert('提示','该考生已被指定评委，不可删除')
//                                                return;}else{store.removeAt(rowIndex);}

                                            }
                                        },"",true,this);
                                    } },
                                    {iconCls:'edit',tooltip:'编辑考生信息',handler:'editCurrentApplicant'}
                                   // {iconCls: 'people',tooltip: '为考生指定评委',handler:'setAppJudge'}
                                ]
                            }
                        ]
                    }
                ]
            }]

        })
        this.callParent();
    },
    buttons: [{
        text: '保存',
        iconCls:"save",
        handler: 'onApplicantsSave'
    }, {
        text: '确定',
        iconCls:"ensure",
        handler: 'onApplicantsEnsure'
    }, {
        text: '关闭',
        iconCls:"close",
        handler: 'onApplicantsClose'
    }]
});
