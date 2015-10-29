Ext.define('KitchenSink.view.materialsReview.materialsReview.materialsReviewApplicants', {
    extend: 'Ext.panel.Panel',
    xtype: 'materialsReviewApplicants',
    controller: 'materialsReview',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'Ext.grid.filters.Filters',
        'KitchenSink.AdvancedVType',
        'KitchenSink.view.materialsReview.materialsReview.materialsReviewApplicantsStore',
		 'KitchenSink.view.enrollmentManagement.applicationForm.dynamicInfo.dynamicColumn'
    ],
	
    title: '材料评审考生名单',
    classID:'',
    batchID:'',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
	 constructor: function (obj){
        this.orgColorSortStore=obj.orgColorSortStore;
        this.initData=obj.initData;
        this.stuGridColorSortFilterOptions=obj.stuGridColorSortFilterOptions;
        this.classID=obj.classID;
        this.callParent();
    },
    initComponent:function(){
		 var me = this;
        /*初始颜色类别数据*/
        var initData=me.initData;
        /*grid类别过滤数据*/
        var stuGridColorSortFilterOptions=me.stuGridColorSortFilterOptions;
        var orgColorSortStore =  me.orgColorSortStore;
		
		
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
var ddd;
        var dynamicColorSortStore = Ext.create("Ext.data.Store",{
            fields:[
                "TZ_COLOR_SORT_ID","TZ_COLOR_NAME","TZ_COLOR_CODE"
            ],
          data:initData
        });

        Ext.define('filter', {
            extend: 'Ext.grid.filters.Filters'

        });

        Ext.apply(this,{
				  
            items: [{
                xtype: 'form',
                reference: 'materialsReviewApplicantsForm',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,
                bodyStyle:'overflow-y:auto;overflow-x:hidden',

                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 110,
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
                        fieldLabel: "报考班级",
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
                        fieldLabel: "报考考生数量",
                        name: 'applicantsNumber',
                        fieldStyle:'background:#F4F4F4',
                        readOnly:true
                    }, {
                        xtype: 'numberfield',
                        fieldLabel: "材料评审考生",
                        name: 'materialsReviewApplicantsNumber',
                        fieldStyle:'background:#F4F4F4',
                        readOnly:true
                    },{
                        xtype: 'textfield',
                        name: 'status',
                        hidden:true

                    },
                    {
                        xtype: 'grid',
                        title:'考生名单',
                        minHeight: 260,
                        name:'materialsReviewApplicantsGrid',
                        reference:'materialsReviewApplicantsGrid',
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
                            items:[
                                {text:"新增",tooltip:"添加考生参与本批次材料评审",iconCls:"add",handler:"addApplicants"},"-",
                                {text: "编辑",tooltip: "编辑选中考生", iconCls:"view", handler: 'editApplicants'},"-",
                                {text:"删除",tooltip:"从列表中移除选中的考生",iconCls:"remove",handler:"removeApplicants"},

                                {text:"增加五金考生",tooltip:"增加五金考生",iconCls:"add",handler:'addApplicantsFromFiveGold',hidden:true},"->",
                                {
                                    xtype:'splitbutton',
                                    text:'更多操作',
                                    iconCls:  'list',
                                    glyph: 61,
                                    menu:[
                                        { text: '设置面试资格',handler: 'setOwnQuary'},
                                        {text:"发送邮件",tooltip:"发送邮件",handler:"sendStudentEmail"}
                                         ]


                        }]}],
                        plugins: [{
                            ptype: 'cellediting'
							

                        },{
                            ptype: 'gridfilters'

                        }
                        ],
                        store:{
                            type:'materialsReviewApplicants'
                        },
                        columns: [
                          {
                                text: "姓名",
                                dataIndex: 'realName',
                                width:100,
                                filter: {
                                    type: 'string',
                                    itemDefaults: {
                                        emptyText: 'Search for...'
                                    }
                                }
                            },{
                                text: "报名表编号",
                                dataIndex: 'appInsID',
                                width:130,
                                filter: {
                                    type: 'string',
                                    itemDefaults: {
                                        emptyText: 'Search for...'
                                    }
                                }
                            }
                            ,{
                                text: "性别",
                                dataIndex: 'gender',
                                width:70,
                                filter: {
                                    type: 'list'
                                }
                            },{
                                text: "评委",
                                dataIndex: 'judgeList',
                               // minWidth:130,
                                flex:1
//                                renderer:function(v) {
//                                    if (v) {
//                                        return '<a  href = "javaScript:void(0)" >' + v + '</a>';
//                                    } else {
//                                        return "";
//                                    }}
                            }
                            ,{
                                text: "面试资格",
                                dataIndex: 'interviewQualification',
                                width:100,
                                renderer:function(v){
                                    switch(v){
                                        case 'W':
                                            return '待定';
                                        case 'N':
                                            return '无';
                                        case 'Y':
                                            return '有';
                                        case '':
                                            return '待定';

                                    }
                                },
                                filter: {
                                    type: 'list'
                                }
                            },
                            {
                                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.colorType","类别"),
								//id:'colorType',
								//itemId:'colorType',
								name:'colorType',
                                dataIndex: 'colorType',
                                lockable   : false,
                                width: 140,
                                filter: {
                                    type: 'list',
                                    options: stuGridColorSortFilterOptions
                                },editor: {
                                    xtype: 'combo',
									disabled : true,
                                    queryMode:'local',
                                    valueField: 'TZ_COLOR_SORT_ID',
									readOnly:true,
                                    displayField: 'TZ_COLOR_NAME',
                                    triggerAction: 'all',
                                    editable : false,
                                    store:dynamicColorSortStore,
                                    tpl: Ext.create('Ext.XTemplate',
                                        '<tpl for=".">',
                                        '<div class="x-boundlist-item"><div class="x-colorpicker-field-swatch-inner" style="margin-top:6px;width:30px;height:50%;background-color: #{TZ_COLOR_CODE}"></div><div style="margin-left:40px;display: block;  overflow:  hidden; white-space: nowrap; -o-text-overflow: ellipsis; text-overflow:  ellipsis;"> {TZ_COLOR_NAME}</div></div>',
                                        '</tpl>'
                                    ),
                                    displayTpl: Ext.create('Ext.XTemplate',
                                        '<tpl for=".">',
                                        '{TZ_COLOR_NAME}',
                                        '</tpl>'
                                    ),
                                    listeners: {
                                        focus: function (combo,event, eOpts) {
                                            var selList = this.findParentByType("grid").getView().getSelectionModel().getSelection();

                                            var colorSortID =selList[0].raw.colorType;

                                            var arrayData = new Array();
                                            for(var i=0;i<validColorSortStore.getCount();i++){
                                                arrayData.push(validColorSortStore.data['items'][i].data);
                                            };
                                          //  if(colorSortID.length>0&&validColorSortStore.find("TZ_COLOR_SORT_ID",colorSortID)==-1){
                                          //      var tmpRec = orgColorSortStore.getAt(orgColorSortStore.find("TZ_COLOR_SORT_ID",colorSortID));
                                          //      arrayData.push(tmpRec.data);
                                          //  };
                                            if(arrayData.length<1){
                                                arrayData.push({TZ_COLOR_SORT_ID:'',TZ_COLOR_NAME:'',TZ_COLOR_CODE:''});
                                            }
                                            combo.store.loadData(arrayData);
                                        },
                                        blur: function (combo,event, eOpts) {
                                            combo.store.loadData(initData);
                                        }
                                    }
                                }
								,
                                renderer:function(v){
                                    if(v!=" "){
									
                                        var rec = orgColorSortStore.find('TZ_COLOR_SORT_ID',v,0,true,true,false);
                                        if(rec>-1){
                                            return "<div  class='x-colorpicker-field-swatch-inner' style='width:30px;height:50%;background-color: #"+orgColorSortStore.getAt(rec).get("TZ_COLOR_CODE")+"'></div><div style='margin-left:40px;'>"+orgColorSortStore.getAt(rec).get("TZ_COLOR_NAME")+"</div>";
                                        }else{
                                            return " ";
                                        }
                                    }
                                }
                                
                            },

                    {
                                text: "备注",
                                dataIndex: 'remark',
                                width: 130
                            },{
                                text: "管理员意见",
                                dataIndex: 'adminRemark',
                                width: 130

                            },{
                                menuDisabled: true,
                                text: "操作",
                                sortable: false,
                                width:80,
                                xtype: 'actioncolumn',
                                items:[
                                    {iconCls: 'remove',tooltip: '移除考生',  handler: function(view, rowIndex){
                                        if( view.findParentByType("grid").findParentByType("form").getForm().findField('status').getValue()=='进行中')
                                        {Ext.Msg.alert('提示','当前评审状态为进行中，不可移除考生');
                                            return ;}
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
                                    {iconCls:'edit',tooltip:'编辑考生信息',handler:'editCurrentApplicant'},
                                    {iconCls: 'people',tooltip: '为考生指定评委',handler:'setAppJudge'}
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
