Ext.define('KitchenSink.view.classManage.ClassInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'ClassInfo',
    controller: 'ClassInfo',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'Ext.ux.DataView.DragSelector',
        'Ext.ux.DataView.LabelEditor' ,
        'KitchenSink.view.classManage.ZyfxStore',
        'KitchenSink.view.classManage.BmlcStore',
        'KitchenSink.view.classManage.PcglStore',
        'KitchenSink.view.classManage.GlryStore',
        'KitchenSink.view.classManage.DjzlStore',
        'KitchenSink.view.classManage.classInfoController',
        'KitchenSink.view.classManage.userWindows',
        'KitchenSink.view.classManage.hcgzStore'
    ],
    title: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bj_info_title","班级详情"),
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'add',//默认新增
    constructor: function (bj_id){
        this.bj_id=bj_id;
        this.callParent();
    },
    initComponent: function(){
        var me = this;
        var mee=this;
        var tzParams = '{"ComID":"TZ_GD_BJGL_COM","PageID":"TZ_BJ_QTXX","OperateType":"QF","comParams":{"bj_id":"'+this.bj_id+'"}}';
        //搜索条件
        var conItems = [];
        //加载数据
        Ext.tzLoadAsync(tzParams,function(responseData){
            var formData = responseData.formData;
            for(var fieldName in formData) {
                //字段类型
                var fldType = formData[fieldName].fldType;
                //如果是下拉框,则初始化下拉框默认值;
                var downCondition = null;
                var downConditionStr = "";
                if(fldType == "L"){
                    downConditionStr = '{ "TZ_JG_ID":{ "value" : "EMBA", "operator":"01", "type":"01"}}';
                    downCondition = Ext.JSON.decode(downConditionStr);
                }
                var show_name=formData[fieldName].show_name;
                var fieldValue=formData[fieldName].zd_name;
                var typeField = {};
                switch (fldType)
                {
                    case 'T'://字符串
                        typeField = {
                            xtype: 'textfield',
                            columnWidth: 1,
                            fieldLabel:show_name,
                            hideEmptyLabel: true,
                            name: fieldName,
                            value: fieldValue
                        };
                        break;
                    case 'N'://数字
                        typeField = {
                            xtype: 'numberfield',
                            columnWidth: 1,
                            hideEmptyLabel: true,
                            value: fieldValue,
                            fieldLabel:show_name,
                            name: fieldName
                        };
                        break;
                    case 'D'://日期
                        typeField = {
                            xtype: 'datefield',
                            columnWidth: 1,
                            hideEmptyLabel: true,
                            format: 'Y-m-d',
                            fieldLabel:show_name,
                            value: fieldValue,
                            name: fieldName
                        };
                        break;
                    case 'L'://下拉框
                        typeField = {
                            xtype: 'combobox',
                            columnWidth: 1,
                            autoSelect: false,
                            fieldLabel:show_name,
                            store: new KitchenSink.view.common.store.comboxStore({
                                recname: 'TZ_C_ATTR_OPT_T',
                                condition:{
                                    TZ_IS_USED:{
                                        value:'Y',
                                        operator:"01",
                                        type:"01"
                                    },
                                    TZ_JG_ID:{
                                        value:Ext.tzOrgID,
                                        operator:"01",
                                        type:"01"
                                    },
                                    TZ_ATTRIBUTE_ID:{
                                        value:fieldName,
                                        operator:"01",
                                        type:"01"
                                    }
                                },
                                result:'TZ_DROP_DOWN_ID,TZ_DROP_DOWN_VALUE'
                            }),
                            valueField: 'TZ_DROP_DOWN_ID',
                            displayField: 'TZ_DROP_DOWN_VALUE',
                            //typeAhead: true,
                            queryMode: 'remote',
                            name: fieldName,
                            value: fieldValue
                        };
                        break;
                    default:
                        typeField = {
                            xtype: 'textfield',
                            columnWidth: 1,
                            hideEmptyLabel: true,
                            value: fieldValue,
                            name: fieldName
                        };
                }
                var fieldItem = {
                    layout:'column',
                    bodyPadding: 10,
                    items:[typeField]
                }
                conItems.push(fieldItem);
            }
            if (conItems=="")
            {
                typeField = {
                    html: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.wszgdxxsjx","未设置更多信息数据项"),
                    minHeight:30
                };
                var fieldItem1 = {
                    layout:'column',
                    bodyPadding: 6,
                    items:[typeField]
                };
                conItems.push(fieldItem1);
            }
            typeField = {
                columnWidth:.2,
                html: "<a href='javascript:void(0)'>"+Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.gxhsz","个性化设置")+"</a>",
                listeners:
                {
                    click:{
                        element: 'el',
                        fn: 'dynamicFormSet'
                    }
                }
            };
            var fieldItem2 = {
                layout:'column',
                bodyPadding: 6,
                items:[typeField]
            };
            conItems.push(fieldItem2);
        });
        Ext.apply(this,{
            items: [
                {
                    xtype: 'form',
                    name:'form_1',
                    reference: 'siteAccountForm',
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    border: false,
                    bodyPadding: 10,
                    bodyStyle:'overflow-y:auto;overflow-x:hidden',
                    buttonAlign: 'center',
                    fieldDefaults: {
                        msgTarget: 'side',
                        labelWidth: 170,
                        labelStyle: 'font-weight:bold'
                    },
                    items: [
                        {
                            xtype: 'textfield',
                            readOnly:true,
                            fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bj_id","班级编号"),
                            name: 'bj_id',
                            fieldStyle:'background:#F4F4F4',
                            value: 'NEXT'
                        },{
                            xtype: 'textfield',
                            fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bj_name","班级名称"),
                            name: 'bj_name',
                            allowBlank: false
                        },{
                            layout: {
                                type: 'column'
                            },
                            items:[{
                                columnWidth:.5,
                                xtype: 'textfield',
                                fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.xm_id","所属项目"),
                                name: 'xm_id',
                                editable: false,
                                triggers: {
                                    search: {
                                        cls: 'x-form-search-trigger',
                                        handler: "xm_idChoice"
                                    }
                                },
                                allowBlank: false,
                                listeners:{
                                    change:function(file, value, eOpts){
                                        if (file.findParentByType('ClassInfo').actType=="add" ||(file.findParentByType('ClassInfo').actType=="update"&&value!=""&&eOpts!=""))
                                        {
                                            function callBack(id){
                                                if (id=="yes")
                                                {
                                                    var form = file.findParentByType('ClassInfo').down('form[name=form_1]').getForm();
                                                    var bj_id=form.getValues().bj_id;
                                                    var comParams='"add":[{"bj_id":"'+bj_id+'","xm_id":"'+value+'"}]';
                                                    var tzParams = '{"ComID":"TZ_GD_BJGL_COM","PageID":"TZ_BJ_ADD_STD","OperateType":"U","comParams":{'+comParams+'}}';
                                                    Ext.tzSubmit(tzParams,function(responseData){
                                                        //报名表编号、班级描述
                                                        form.findField("bj_desc").setValue(responseData.class_desc);
                                                        form.findField("bmb_mb").setValue(responseData.bmb_id);
                                                        form.findField("bmb_mb_desc").setValue(responseData.bmb_desc);
                                                        //专业方向页面重新加载
                                                        var zy_grid = file.findParentByType('ClassInfo').down('grid[name=zyfx_save]');
                                                        Params= '{"bj_id":"'+bj_id+'","queryID":"2"}';
                                                        zy_grid.store.tzStoreParams = Params;
                                                        zy_grid.store.reload();
                                                        //管理人员页面重新加载
                                                        var gl_grid = file.findParentByType('ClassInfo').down('grid[name=glry_save]');
                                                        Params= '{"bj_id":"'+bj_id+'","queryID":"4"}';
                                                        gl_grid.store.tzStoreParams = Params;
                                                        gl_grid.store.reload();
                                                        //报名流程页面重新加载
                                                        var _bm="";
                                                        var bm_grid = file.findParentByType('ClassInfo').down('grid[name=applyItemGrid]');
                                                        Params= '{"bj_id":"'+bj_id+'","queryID":"5","lc_id":"'+_bm+'"}';
                                                        bm_grid.store.tzStoreParams = Params;
                                                        bm_grid.store.reload();
                                                        //递交资料页面重新加载
                                                        /*var _zl="";
                                                        var zl_grid = file.findParentByType('ClassInfo').down('grid[name=applyItemGrid1]');
                                                        Params= '{"bj_id":"'+bj_id+'","queryID":"6","zl_id":"'+_zl+'"}';
                                                        zl_grid.store.tzStoreParams = Params;
                                                        zl_grid.store.reload();*/
                                                    });
                                                }
                                            }
											Ext.MessageBox.confirm("提示","是否复制项目下报名表模版、递交资料模型等默认数据到当前班级？",callBack);
                                        }
                                    }
                                }
                            },{
                                columnWidth:.5,
                                xtype: 'displayfield',
                                hideLabel: true,
                                name: 'xm_id_desc',
                                style:'margin-left:8px;'
                            }]
                        },{
                            xtype: 'combobox',
                            fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bm_kt","是否开通在线报名"),
                            editable:false,
                            emptyText:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.pleaseSelect",'请选择'),
                            queryMode: 'remote',
                            name: 'bm_kt',
                            valueField: 'TValue',
                            displayField: 'TSDesc',
                            store: new KitchenSink.view.common.store.appTransStore("TZ_IS_APP_OPEN"),
                            listeners: {
                                change: function(combox, newValue, oldValue){ 
                                	var form = combox.findParentByType("form").getForm();
                                	if(newValue == "Y"){
                                		form.findField("beginBm_time").allowBlank = false;
                                		form.findField("beginBm_tm").allowBlank = false;
                                		form.findField("endBm_time").allowBlank = false;
                                		form.findField("endBm_tm").allowBlank = false;
                                	}else{
                                		form.findField("beginBm_time").allowBlank = true;
                                		form.findField("beginBm_tm").allowBlank = true;
                                		form.findField("endBm_time").allowBlank = true;
                                		form.findField("endBm_tm").allowBlank = true;
                                	}
                                }
                            }
                        },{
                            xtype: "container",
                            layout: "hbox",
							hidden: false,
                            items: [
                                {
                                    fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.guest_apply","允许匿名报名"),
                                    xtype: 'checkbox',
                                    name:'guest_apply',
                                    listeners:{
                                        render:function(obj){
                                            obj.getEl().dom.onclick = function(){
                                                var form =  obj.up('form[name=form_1]').getForm();
                                                var bj_id = form.getValues().bj_id;
                                                var gruest_apply_value=obj.getValue();
                                                if(gruest_apply_value ==false){
                                                    gruest_apply_value=true;
                                                }else{
                                                    gruest_apply_value=false;
                                                }
                                                var comParams = '{"bj_id":"' + bj_id + '","guest_apply":"' + gruest_apply_value + '"}';
                                                if (gruest_apply_value ==true) {
                                                    var tzParams = '{"ComID":"TZ_GD_BJGL_COM","PageID":"TZ_BJ_ADD_STD","OperateType":"guest_apply","comParams":' + comParams + '}';
                                                    Ext.tzLoad(tzParams, function (responseData) {
                                                        form.findField("guest_apply_url").setValue(responseData.guest_apply_url);
                                                    })
                                                }else{
                                                    form.findField("guest_apply_url").setValue("");
                                                }
                                            }
                                                // obj.findParentByType("guest_apply").previousNode().setValue(obj.getValue());
                                            }

                                            /*  change:function(file, value, eOpts) {
                                                  var form = file.findParentByType('ClassInfo').down('form[name=form_1]').getForm();
                                                  var bj_id = form.getValues().bj_id;
                                                  var comParams = '{"bj_id":"' + bj_id + '","guest_apply":"' + value + '"}';
                                                  if (value ==true) {

                                                  var tzParams = '{"ComID":"TZ_GD_BJGL_COM","PageID":"TZ_BJ_ADD_STD","OperateType":"guest_apply","comParams":' + comParams + '}';
                                                  Ext.tzSubmit(tzParams, function (responseData) {

                                                      form.findField("guest_apply_url").setValue(responseData.guest_apply_url);
                                                  })
                                                   }else{
                                                      form.findField("guest_apply_url").setValue("");
                                                  }
                                                  }  */
                                        }
                                },{
                                    xtype:'displayfield',
                                    name:'guest_apply_url',
                                    hideLabel: true
                                }
                            ]
                        },{
							xtype:'datefield',
							fieldLabel:'入学日期',
							format:'Y-m-d',
							name:'rx_time'
						},{
                            xtype: 'datefield',
                            fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.begin_time","班级开始日期"),
                            format: 'Y-m-d',
                            name: 'begin_time'
                        },{
                            xtype: 'datefield',
                            fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.end_time","班级结束日期"),
                            format: 'Y-m-d',
                            name: 'end_time'
                        },{
                            xtype: 'datefield',
                            fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.beginBm_time","报名开始日期"),
                            format: 'Y-m-d',
                            name: 'beginBm_time'
                        },{
                            xtype: 'timefield',
                            fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.beginBm_tm","报名开始时间"),
                            format: 'H:i',
                			name: 'beginBm_tm'
                        },{
                            xtype: 'datefield',
                            fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.endBm_time","报名结束日期"),
                            format: 'Y-m-d',
                            name: 'endBm_time'
                        },{
                            xtype: 'timefield',
                            fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.endBm_tm","报名结束时间"),
                            format: 'H:i',
                			name: 'endBm_tm'
                        },{
                            layout: {
                                type: 'column'
                            },
                            //bodyStyle:'padding:10px 0 10px 0',
                            items:[{
                                columnWidth:.5,
                                xtype: 'textfield',
                                fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bmb_mb","在线报名模板"),
                                name: 'bmb_mb',
                                editable: false,
                                allowBlank: false,
                                triggers: {
                                    search: {
                                        cls: 'x-form-search-trigger',
                                        handler: "bmb_mbChoice"
                                    }
                                }
                            },{
                                columnWidth:.5,
                                xtype: 'displayfield',
                                hideLabel: true,
                                name: 'bmb_mb_desc',
                                style:'margin-left:8px'
                            }]
                        },{
                            layout: {
                                type: 'column'
                            },
                            items:[{
                                columnWidth:.5,
                                xtype: 'textfield',
                                fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.clps_cj_modal","材料评审成绩模型"),
                                name: 'clps_cj_modal',
                                editable: false,
                                triggers: {
                                    search: {
                                        cls: 'x-form-search-trigger',
                                        handler: "choiceScoreModal"
                                    }/*,
                                    clear: {
                                        cls: 'x-form-clear-trigger',
                                        handler: function(field){
                                            field.setValue();
                                            field.findParentByType('form').getForm().findField('clps_cj_modal_desc').setValue();
                                        }
                                    }*/
                                }
                            },{
                                columnWidth:.5,
                                xtype: 'displayfield',
                                hideLabel: true,
                                name: 'clps_cj_modal_desc',
                                style:'margin-left:8px'
                            }]
                        },{
                            layout: {
                                type: 'column'
                            },
                            items:[{
                                columnWidth:.5,
                                xtype: 'textfield',
                                fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.msps_cj_modal","面试评审成绩模型"),
                                name: 'msps_cj_modal',
                                editable: false,
                                triggers: {
                                    search: {
                                        cls: 'x-form-search-trigger',
                                        handler: "choiceScoreModal"
                                    }/*,
                                    clear: {
                                        cls: 'x-form-clear-trigger',
                                        handler: function(field){
                                            field.setValue();
                                            field.findParentByType('form').getForm().findField('msps_cj_modal_desc').setValue();
                                        }
                                    }*/
                                }
                            },{
                                columnWidth:.5,
                                xtype: 'displayfield',
                                hideLabel: true,
                                name: 'msps_cj_modal_desc',
                                style:'margin-left:8px'
                            }]
                        },{
                            layout: {
                                type: 'column'
                            },
                            bodyStyle:'padding:0 0 10px 0',
                            items:[{
                                columnWidth:.5,
                                xtype: 'textfield',
                                fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.psbmb_mb","评审用报名表模板"),
                                name: 'psbmb_mb',
                                editable: false,
                                triggers: {
                                    search: {
                                        cls: 'x-form-search-trigger',
                                        handler: "bmb_mbChoice"
                                    }/*,
                                    clear: {
                                        cls: 'x-form-clear-trigger',
                                        handler: function(field){
                                            field.setValue();
                                            field.findParentByType('form').getForm().findField('psbmb_mb_desc').setValue();
                                        }
                                    }*/
                                }
                            },{
                                columnWidth:.5,
                                xtype: 'displayfield',
                                hideLabel: true,
                                name: 'psbmb_mb_desc',
                                style:'margin-left:8px'
                            }]
                        }/*20170118*/,{
                            layout: {
                                type: 'column'
                            },
                            //bodyStyle:'padding:0 0 10px 0',
                            items:[{
                                columnWidth:.5,
                                xtype: 'textfield',
                                fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.csmb_id","自动初筛成绩模型"),
                                name: 'csmb_id',
                                editable: false,
                                triggers: {
                                    search: {
                                        cls: 'x-form-search-trigger',
                                        handler: "choiceScoreModal"
                                    }
                                }
                            },{
                                columnWidth:.5,
                                xtype: 'displayfield',
                                hideLabel: true,
                                name: 'csmb_desc',
                                style:'margin-left:8px'
                            }]
                        },{
                            layout: {
                                type: 'column'
                            },
                            //bodyStyle:'padding:0 0 10px 0',
                            items:[{
                                columnWidth:.5,
                                xtype: 'textfield',
                                fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.ttbl","自动初筛淘汰比率"),
                                name: 'ttbl'
                            },{
                                columnWidth:.5,
                                xtype: 'displayfield',
                                hideLabel: true,
                                name: 'ttbl2',
                                style:'margin-left:8px'
                            }]
                        },{
                            layout: {
                                type: 'column'
                            },
                           // bodyStyle:'padding:0 0 10px 0',
                            items:[{
                                columnWidth:.5,
                                xtype: 'textfield',
                                fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.zdbq_id","自动标签规则"),
                                name: 'zdbq_id',
                                editable: false,
                                triggers: {
                                    search: {
                                        cls: 'x-form-search-trigger',
                                        handler: "choiceBqzModal"
                                    }
                                }
                            },{
                                columnWidth:.5,
                                xtype: 'displayfield',
                                hideLabel: true,
                                name: 'zdbq_desc',
                                style:'margin-left:8px'
                            }]
                        },{
                            layout: {
                                type: 'column'
                            },
                            //bodyStyle:'padding:0 0 10px 0',
                            items:[{
                                columnWidth:.5,
                                xtype: 'textfield',
                                fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.fmqd_id","负面清单规则"),
                                name: 'fmqd_id',
                                editable: false,
                                triggers: {
                                    search: {
                                        cls: 'x-form-search-trigger',
                                        handler: "choiceBqzModal"
                                    }
                                }
                            },{
                                columnWidth:.5,
                                xtype: 'displayfield',
                                hideLabel: true,
                                name: 'fmqd_desc',
                                style:'margin-left:8px'
                            }]
                        },{
                            layout: {
                                type: 'column'
                            },
                            //bodyStyle:'padding:0 0 10px 0',
                            items:[{
                                columnWidth:.5,
                                xtype: 'textfield',
                                fieldLabel: "在线调查",
                                name: 'surveyId',
                                editable: false,
                                triggers: {
                                    search: {
                                        cls: 'x-form-search-trigger',
                                        handler: "choiceSurveyModal"
                                    }
                                }
                            },{
                                columnWidth:.5,
                                xtype: 'displayfield',
                                hideLabel: true,
                                name: 'surveyName',
                                style:'margin-left:8px'
                            }]
                        }
                        /*20170118-end*/,{
                            xtype : 'tabpanel',
                            activeTab: 0,
                            plain:false,
                            frame: true,
                            resizeTabs:true,
                            autoHeight:true,
                            defaults :{
                                autoScroll: false
                            },
                            listeners:{
                                tabchange:function(tp,p){
                                    var queryID;
                                    var bj_id = tp.findParentByType("form").getForm().findField('bj_id').getValue();
                                    if(p.name == "grid_pc"){
                                        if(p.child('checkbox').getValue()!=true){
                                            p.child('grid').hide();
                                        }else{
                                            p.child('grid').show();
                                        }
                                        queryID = "3";
                                    }
                                }
                            },
                            items : [
                                {
                                    title: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bj_desc","班级描述"),
                                    items: [
                                        {
                                            xtype: 'ueditor',
                                            name: 'bj_desc',
                                            zIndex:100
                                        }
                                    ]
                                },
                                //专业方向
                                {
                                    title:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.zyfx_save","专业方向"),
                                    xtype: 'grid',
                                    columnLines: true,
                                    minHeight: 250,
                                    name:'zyfx_save',
                                    reference: 'Zyfx_Grid',
                                    store: {
                                        type: 'ZyfxStore'
                                    },
                                    selModel: {
                                        type: 'checkboxmodel'
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
                                                    items[i].set('fx_xh',i+1);
                                                }
                                                data.view.store.endUpdate();
                                            }
                                        }
                                    },
                                    columns: [{
                                        text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.fx_xh","序号"),
                                        dataIndex: 'fx_xh',
                                        width: 100
                                    },{
                                        text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.fx_id","专业方向ID"),
                                        dataIndex: 'fx_id',
                                        width: 200
                                    },{
                                        text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.fx_name","专业方向名称"),
                                        dataIndex: 'fx_name',
                                        minWidth: 5,
                                        flex: 1
                                    },{
                                        menuDisabled: true,
                                        sortable: false,
                                        align: 'center',
                                        width: 120,
                                        xtype: 'actioncolumn',
                                        items:[
                                            {iconCls: 'edit',tooltip: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.edit","编辑"),handler: 'editZyfx'},
                                            {iconCls: 'remove',tooltip: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.delete","删除"),handler: 'deleteZyfx'}
                                        ]
                                    }],
                                    tbar: [{
                                        xtype:"toolbar",
                                        items: [
                                            {text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.add","新增"),iconCls:"add",handler:'addZyfx'},"-",
                                            {text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.edit","编辑"),iconCls:"edit",handler:'editZyfxT'},"-",
                                            {text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.delete","删除"),iconCls:"remove",handler:'deleteZyfxT'}
                                        ]
                                    }]
                                },
                                //批次管理
                                {
                                    title: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.grid_pc","批次管理"),
                                    name:"grid_pc",
                                    layout: {
                                        type: 'vbox',
                                        align: 'stretch'
                                    },
                                    bodyStyle:'overflow-y:auto;overflow-x:hidden',
                                    fieldDefaults: {
                                        msgTarget: 'side',
                                        labelWidth: 100,
                                        labelStyle: 'font-weight:bold'
                                    },
                                    items: [
                                        {
                                            xtype: 'checkbox',
                                            boxLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bj_xs","是否有下属批次"),
                                            margin:10,
                                            name: 'bj_xs',
                                            listeners:{
                                                change:function(file, value, eOpts){
                                                    var x = file.nextSibling();
                                                    if(value!=true){
                                                        x.store.removeAll();
                                                        x.hide();
                                                    }else{
                                                        x.show();
                                                    }
                                                }
                                            }
                                        },{
                                            xtype: 'grid',
                                            minHeight: 250,
                                            style:'border-top:1px solid #c1c1c1',
                                            name: 'pcgl_save',
                                            dockedItems: [{
                                                xtype: 'toolbar',
                                                items: [
                                                    {text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.add","新增"),iconCls:"add",handler:'addPcgl'},"-",
                                                    {text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.edit","编辑"),iconCls:"edit",handler:'editPcglT'},"-",
                                                    {text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.delete","删除"),iconCls:"remove",handler:'deletePcglT'}
                                                ]
                                            }],
                                            columnLines: true,
                                            selModel:{
                                                type: 'checkboxmodel'
                                            },
                                            reference: 'pcgl_grid',
                                            store: {
                                                type: 'PcglStore'
                                            },
                                            columns: [{
                                                text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.pc_name","批次名称"),
                                                dataIndex: 'pc_name',
                                                flex: 1
                                            },{
                                                text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.pc_st_time","开始日期"),
                                                dataIndex: 'pc_st_time',
                                                width: 150
                                            },{
                                                text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.pc_sp_time","结束日期"),
                                                dataIndex: 'pc_sp_time',
                                                width: 150
                                            },{
                                                text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.pc_stbm_time","报名截止日期"),
                                                dataIndex: 'pc_stbm_time',
                                                width: 150
                                            },{
                                                text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.pc_fb","发布外网报名"),
                                                dataIndex: 'pc_fb',
                                                width: 150
                                            },{
                                                menuDisabled: true,
                                                sortable: false,
                                                align: 'center',
                                                width: 120,
                                                xtype: 'actioncolumn',
                                                items:[
                                                    {iconCls: 'edit',tooltip:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.edit","编辑"),handler: 'editPcgl'},
                                                    {iconCls: 'remove',tooltip: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.delete","删除"),handler: 'deletePcgl'}
                                                ]
                                            }
                                            ]
                                        }
                                    ]
                                },
                                //管理人员
                                {
                                    title:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.glry_save","管理人员"),
                                    xtype: 'grid',
                                    columnLines: true,
                                    name:'glry_save',
                                    minHeight: 250,
                                    reference: 'glryGrid',
                                    store: {
                                        type: 'GlryStore'
                                    },
                                    selModel:{
                                        type: 'checkboxmodel'
                                    },
                                    columns: [{
                                        text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.gl_name","姓名"),
                                        dataIndex: 'gl_name',
                                        width: 200
                                    },{
                                        text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.gl_phone","电话"),
                                        dataIndex: 'gl_phone',
                                        width: 150
                                    },{
                                        text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.gl_email","邮件"),
                                        dataIndex: 'gl_email',
                                        flex: 1
                                    },{
                                        menuDisabled: true,
                                        sortable: false,
                                        width:80,
                                        align: 'center',
                                        xtype: 'actioncolumn',
                                        items:[
                                            {iconCls: 'remove',tooltip: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.delete","删除"),handler: 'deleteGlry'}
                                        ]
                                    }],
                                    tbar: [{
                                        xtype:"toolbar",
                                        items: [
                                            {text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.add","新增"),iconCls:"add",handler:'addGlryT'},
                                            {text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.delete","删除"),iconCls:"remove",handler:'deleteGlryT'}
                                        ]
                                    }]
                                },
                                //报名流程设置
                                {
                                    title: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.grid_bmlcsz","报名流程设置"),
                                    name:"grid_bmlc",
                                    layout: {
                                        align: 'stretch'
                                    },
                                    bodyStyle:'overflow-y:auto;overflow-x:hidden',
                                    fieldDefaults: {
                                        msgTarget: 'side',
                                        labelWidth: 30,
                                        labelStyle: 'font-weight:bold'
                                    },
                                    items: [
                                        {
                                            layout: {
                                                type: 'column'
                                            },
                                            margin:10,
                                            items:[
                                                {
                                                    xtype: 'combobox',
                                                    fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bmlc_mb","报名流程模板"),
                                                    name: 'bmlc_mb',
                                                    queryMode: 'remote',
                                                    editable:false,
                                                    valueField: 'TZ_APPPRO_TMP_ID',
                                                    displayField: 'TZ_APPPRO_TMP_NAME',
                                                    width:280,
                                                    columnWidth:.4,
                                                    labelWidth: 100,
                                                    margin:'0 10 0 0'
                                                },{
                                                    xtype:"button",
                                                    columnWidth:.15,
                                                    text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.sycmx","使用此模型"),
                                                    margin:'0 0 0 10',
                                                    listeners:{
                                                        click:function(btn){
                                                            var x = btn.findParentByType('ClassInfo').down('grid[name=applyItemGrid]');
                                                            var bj_id=btn.findParentByType('form').getForm().findField('bj_id').getValue();
                                                            var _bm_mb=btn.findParentByType('form').getForm().findField('bmlc_mb').getValue();
                                                            if (Ext.isEmpty(_bm_mb)){
                                                                Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.qxxzmx","请先选择模型！"));
                                                            }else{
                                                                x.store.tzStoreParams='{"lc_id":"'+_bm_mb+'","bj_id":"'+bj_id+'","queryID":"5"}',
                                                                    x.store.reload();
                                                            }
                                                        }
                                                    }
                                                },{
                                                    columnWidth:.2,
                                                    html: "<a href='javascript:void(0)'>"+Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bmlcsz","报名流程设置")+"</a>",
                                                    margin:'7 0 0 25',
                                                    listeners:
                                                    {
                                                        click:{
                                                            element: 'el',
                                                            fn: 'enrollmentProcedureSet'
                                                        }
                                                    }
                                                }]
                                        },{
                                            xtype: 'grid',
                                            style:'border-top:1px solid #c1c1c1',
                                            minHeight: 250,
                                            name: 'applyItemGrid',
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
                                                            items[i].set('bmlc_xh',i+1);
                                                        }
                                                        data.view.store.endUpdate();
                                                    }
                                                }
                                            },
                                            columnLines: true,
                                            selModel:{
                                                type: 'checkboxmodel'
                                            },
                                            reference: 'bmlc_xq',
                                            store: {
                                                type: 'BmlcStore'
                                            },
                                            dockedItems: [{
                                                xtype: 'toolbar',
                                                items: [
                                                    {text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.add","新增"),iconCls:"add",handler:'addBmlcT'},"-",
                                                    {text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.edit","编辑"),iconCls:"edit",handler:'editBmlc'},"-",
                                                    {text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.delete","删除"),iconCls:"remove",handler:'deleteBmlcT'}
                                                ]
                                            }],
                                            columns: [{
                                                text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bmlc_xh","序号"),
                                                dataIndex: 'bmlc_xh',
                                                width: 80
                                            },{
                                                text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bmlc_name","流程名称"),
                                                dataIndex: 'bmlc_name',
                                                editor: {
                                                    xtype:'textfield',
                                                    allowBlank:false
                                                },
                                                flex: 1
                                            },{
                                                text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bmlc_hfysz","回复语设置"),
                                                align: 'center',
                                                groupable: false,
                                                width: 150,
                                                renderer: function(v) {
                                                    return '<a href="javascript:void(0)">'+Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.set","设置")+'</a>';
                                                },
                                                listeners:{
                                                    click:'bmlcMrnrXs'
                                                }
                                            },{
                                                menuDisabled: true,
                                                sortable: false,
                                                align: 'center',
                                                width: 80,
                                                xtype: 'actioncolumn',
                                                items:[
                                                    {iconCls: 'edit',tooltip: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.edit","编辑"),handler: 'editBmlc'},
                                                    {iconCls: 'remove',tooltip:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.delete","删除"),handler: 'deleteBmlc'}
                                                ]
                                            }
                                            ]
                                        }
                                    ]
                                },
                                //递交资料设置
                                /*{
                                    title: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.grid_djzlsz","递交资料设置"),
                                    name:"grid_djzl",
                                    layout: {
                                        align: 'stretch'
                                    },
                                    bodyStyle:'overflow-y:auto;overflow-x:hidden',
                                    fieldDefaults: {
                                        msgTarget: 'side',
                                        labelWidth: 30,
                                        labelStyle: 'font-weight:bold'
                                    },
                                    items: [
                                        {
                                            layout: {
                                                type: 'column'
                                            },
                                            margin:10,
                                            items:[
                                                {
                                                    xtype: 'combobox',
                                                    fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.djzl_mx","递交资料模型"),
                                                    name: 'djzl_mx',
                                                    queryMode: 'remote',
                                                    editable:false,
                                                    valueField: 'TZ_SBMINF_TMP_ID',
                                                    displayField: 'TZ_SBMINF_TMP_NAME',
                                                    width:280,
                                                    columnWidth:.4,
                                                    labelWidth: 100,
                                                    margin:'0 10 0 0'
                                                },{
                                                    xtype:"button",
                                                    columnWidth:.15,
                                                    text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.djzl_sycmx","使用此模型"),
                                                    margin:'0 0 0 10',
                                                    listeners:{
                                                        click:function(btn){
                                                            var x = btn.findParentByType('ClassInfo').down('grid[name=applyItemGrid1]');
                                                            var bj_id=btn.findParentByType('form').getForm().findField('bj_id').getValue();
                                                            var _zl_mx=btn.findParentByType('form').getForm().findField('djzl_mx').getValue();
                                                            if (Ext.isEmpty(_zl_mx)){
                                                                Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.qxxzmx","请先选择模型！"));
                                                            }else{
                                                                x.store.tzStoreParams='{"zl_id":"'+_zl_mx+'","bj_id":"'+bj_id+'","queryID":"6"}',
                                                                    x.store.reload();
                                                            }
                                                        }
                                                    }
                                                },{
                                                    columnWidth:.2,
                                                    text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.djzl_djzlsz","递交资料设置"),
                                                    html: "<a href='javascript:void(0)'>"+Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.djzl_djzlsz","递交资料设置")+"</a>",
                                                    margin:'7 0 0 25',
                                                    listeners:
                                                    {
                                                        click:{
                                                            element: 'el',
                                                            fn: function(){
                                                                Ext.tzSetCompResourses("TZ_GD_SMTDTMDL_COM");
                                                                var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_SMTDTMDL_COM"]["TZ_GD_SMTDTLST_STD"];
                                                                if( pageResSet == "" || pageResSet == undefined){
                                                                    Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nmyqx","您没有权限"));
                                                                    return;
                                                                }
                                                                //该功能对应的JS类
                                                                var className = pageResSet["jsClassName"];
                                                                if(className == "" || className == undefined){
                                                                    Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
                                                                    return;
                                                                }
                                                                var contentPanel,cmp, className, ViewClass, clsProto;
                                                                var themeName = Ext.themeName;
                                                                contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
                                                                contentPanel.body.addCls('kitchensink-example');
                                                                if(!Ext.ClassManager.isCreated(className)){
                                                                    Ext.syncRequire(className);
                                                                }
                                                                ViewClass = Ext.ClassManager.get(className);
                                                                clsProto = ViewClass.prototype;
                                                                if (clsProto.themes) {
                                                                    clsProto.themeInfo = clsProto.themes[themeName];
                                                                    if (themeName === 'gray') {
                                                                        clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.classic);
                                                                    } else if (themeName !== 'neptune' && themeName !== 'classic') {
                                                                        if (themeName === 'crisp-touch') {
                                                                            clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes['neptune-touch']);
                                                                        }
                                                                        clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.neptune);
                                                                    }
                                                                    if (!clsProto.themeInfo) {
                                                                        Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                                                                            themeName + '\'. Is this intentional?');
                                                                    }
                                                                }
                                                                cmp = new ViewClass();
                                                                tab = contentPanel.add(cmp);
                                                                contentPanel.setActiveTab(tab);
                                                                Ext.resumeLayouts(true);
                                                                if (cmp.floating) {
                                                                    cmp.show();
                                                                }
                                                            }
                                                        }
                                                    }
                                                }]
                                        },{
                                            xtype: 'grid',
                                            style:'border-top:1px solid #c1c1c1',
                                            minHeight: 250,
                                            name: 'applyItemGrid1',
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
                                                            items[i].set('djzl_xh',i+1);
                                                        }
                                                        data.view.store.endUpdate();
                                                    }
                                                }
                                            },
                                            columnLines: true,
                                            selModel:{
                                                type: 'checkboxmodel'
                                            },
                                            dockedItems: [{
                                                xtype: 'toolbar',
                                                items: [
                                                    {text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.add","新增"),iconCls:"add",handler:'addDjzl'},"-",
                                                    {text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.edit","编辑"),iconCls:"edit",handler:'editDjzl'},"-",
                                                    {text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.delete","删除"),iconCls:"remove",handler:'deleteDjzlT'}
                                                ]
                                            }],
                                            reference: 'djzl_qqq',
                                            store: {
                                                type: 'DjzlStore'
                                            },
                                            columns: [{
                                                text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.djzl_xh","显示顺序"),
                                                dataIndex: 'djzl_xh',
                                                width: 100
                                            },{
                                                text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.djzl_name","内容简称"),
                                                dataIndex: 'djzl_name',
                                                editor: {
                                                    xtype:'textfield'
                                                },
                                                flex: 1
                                            },{
                                                text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.djzl_bz","备注"),
                                                dataIndex: 'djzl_bz',
                                                editor: {
                                                    xtype:'textfield'
                                                },
                                                width: 220
                                            },{
                                                text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.djzl_cyhfysz","常用回复短语设置"),
                                                align: 'center',
                                                groupable: false,
                                                width: 150,
                                                renderer: function(v) {
                                                    return '<a href="javascript:void(0)">'+Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.set","设置")+'</a>';
                                                },
                                                listeners:{
                                                    click:'DjzlDyhf'
                                                }
                                            },{
                                                menuDisabled: true,
                                                sortable: false,
                                                align: 'center',
                                                width: 80,
                                                xtype: 'actioncolumn',
                                                items:[
                                                    {iconCls: 'edit',tooltip: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.edit","编辑"),handler: 'editDjzl'},
                                                    {iconCls: 'remove',tooltip:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.delete","删除"),handler: 'deleteDjzl'}
                                                ]
                                            }
                                            ]
                                        }
                                    ]
                                },*/{
                                    title: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.hcgztab","互斥班级"),
                                    xtype: 'grid',
                                    columnLines: true,
                                    name:'hcgzGrid',
                                    minHeight: 250,
                                    reference: 'hcgzGrid',
                                    store: {
                                        type: 'hcgzStore'
                                    },
                                    columns: [{
                                        text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.hcgzRowNum","序号"),
                                        xtype: 'rownumberer',
                                        width:50
                                    },{
                                        text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.hcgzTabClsId","班级编号"),
                                        dataIndex: 'hcgzTabClsId',
                                        flex:1
                                    },{
                                        text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.hcgzTabClsName","班级名称"),
                                        dataIndex: 'hcgzTabClsName',
                                        flex:2
                                    }]
                                },
                                {
                                    title: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.form_2","更多信息"),
                                    xtype: 'form',
                                    name:'form_2',
                                    items: conItems,
                                    buttonAlign: 'left'
                                }
                            ]
                        }
                    ]

                }
            ]
        });

        this.callParent();
    },
    buttons: [{
        text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.save","保存"),
        iconCls:"save",
        handler: 'onFormSave'
    }, {
        text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.ensure","确定"),
        iconCls:"ensure",
        handler: 'onFormEnsure'
    }, {
        text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.close","关闭"),
        iconCls:"close",
        handler: 'onFormClose'
    }]
});
function add_Bmlc(){
    var me = this;
    console.log(me);
    Ext.tzSetCompResourses("TZ_PM_BMLCMBGL_COM");
    var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PM_BMLCMBGL_COM"]["TZ_GD_LCMBCHSE_STD"];
    if (pageResSet == "" || pageResSet == undefined) {
        Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nmyqx","您没有权限"));
        return;
    }
    var className = pageResSet["jsClassName"];
    if (className == "" || className == undefined) {
        Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
        return;
    }
    //var win = this.lookupReference('myBmbRegWindow');
    var win="";
    if (!win) {
        Ext.syncRequire(className);
        ViewClass = Ext.ClassManager.get(className);
        win = new ViewClass();
        //this.getView().add(win);
    }else{
        var activeTab = win.items.items[0].getActiveTab();
        document.getElementById(Ext.get(activeTab.id).query('input')[0].id).value = "";
    }
    win.show();
    if (!window.mybmb_cj) {
        window.mybmb_cj = function(el) {
            Ext.each(Ext.query(".tplitem"),
                function(i) {
                    this.style.backgroundColor = null
                });
            el.style.backgroundColor = "rgb(173, 216, 230)";
            var activeTab = win.items.items[0].getActiveTab();

            var newName = el.getElementsByClassName("tplname")[0].getAttribute("title")  + "_" + ( + new Date());
            document.getElementById(Ext.get(activeTab.id).query('input')[0].id).value = newName;
        }
    }
};

function add_djzl(me1){
    Ext.tzSetCompResourses("TZ_GD_SMTDTMDL_COM");
    var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_SMTDTMDL_COM"]["TZ_GD_NEWDJ_STD"];
    if (pageResSet == "" || pageResSet == undefined) {
        Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nmyqx","您没有权限"));
        return;
    }
    var className = pageResSet["jsClassName"];
    if (className == "" || className == undefined) {
        Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
        return;
    }
    var win;
    if (!win) {
        Ext.syncRequire(className);
        ViewClass = Ext.ClassManager.get(className);
        win = new ViewClass();
        me1.getView().add(win);
    }else{
        var activeTab = win.items.items[0].getActiveTab();
        document.getElementById(Ext.get(activeTab.id).query('input')[0].id).value = "";
    }
    win.show();
    if (!window.mybmb_cj) {
        window.mybmb_cj = function(el) {
            Ext.each(Ext.query(".tplitem"),
                function(i) {
                    me1.style.backgroundColor = null
                });
            el.style.backgroundColor = "rgb(173, 216, 230)";
            var activeTab = win.items.items[0].getActiveTab();
            var newName = el.getElementsByClassName("tplname")[0].getAttribute("title")  + "_" + ( + new Date());
            document.getElementById(Ext.get(activeTab.id).query('input')[0].id).value = newName;
        }
    }
};