var refreshTaskMgr=new Ext.util.TaskRunner();
Ext.define('KitchenSink.view.bulkEmailAndSMS.znx.znxDet', {
    extend: 'Ext.panel.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'tranzvision.extension.grid.column.Link',
        'KitchenSink.view.bulkEmailAndSMS.znx.znxDetController'
    ],
    xtype: 'znxBulkDet',
    title:Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_DET_STD.panelTitle","站内信发送定义"),
    controller: 'znxDetController',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    pageId:' ',
    BulkTaskId:' ',

    listeners: {
        close:function(t){
            for(var i =0;i<refreshTaskMgr.tasks.length;i++){
                if(refreshTaskMgr.tasks[i].id== t.pageId){
                    refreshTaskMgr.stop(refreshTaskMgr.tasks[i]);
                }
            }
        },
        afterrender:function(t){
            var params;

            t.pageId = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab().id;

            var refreshTask = {
                id: t.pageId,
                run: function () {
                    var currentPage = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab();
                    if(currentPage.id.indexOf('znxBulkDet')!=-1){
                     
                        if (t.BulkTaskId!=" "){
                            params = '"znxQfId":"'+t.BulkTaskId+'"';
                            var tzParams = '{"ComID":"TZ_ZNX_GL_COM","PageID":"TZ_ZNX_DET_STD","OperateType":"getRwzxZt","comParams":{'+params+'}}';
                            Ext.Ajax.request({
                                url: Ext.tzGetGeneralURL,
                                params:{tzParams:tzParams},
                                success: function(response, opts)
                                {
                                    //返回值内容
                                    var jsonText = response.responseText;
                                    var jsonObject = Ext.util.JSON.decode(jsonText);
                                    //console.log("ret-->"+currentPage.id+"___"+jsonObject.comContent.rwzxZt);
                                    if (jsonObject.comContent.rwzxZt==""||jsonObject.comContent.rwzxZt=="D"||jsonObject.comContent.rwzxZt=="E") {
										//新建、发送失败和中断时页面显示
                                        t.down('button[reference=saveBtn]').setDisabled(false);
                                        t.down('button[reference=sendBtn]').setDisabled(false);
                                        t.getController().pageFiledsDisControl(t.child('form'));
                                    } else {
										//正在发送和发送成功后页面只读
                                        t.down('button[reference=saveBtn]').setDisabled(true);
                                        t.down('button[reference=sendBtn]').setDisabled(true);
                                        t.getController().pageReadonly(t.child('form'));
                                    }
									if (jsonObject.comContent.rwzxZt == "C" || jsonObject.comContent.rwzxZt == "D") {
                                       t.down('button[reference=revokeBtn]').setVisible(false);
									   //t.down('displayfield[name=dsfsInfo]').setVisible(false);
                                    }
									
									/*
									if (jsonObject.comContent.rwzxZt == "B" && t.down('checkbox[name=dsfsFlag]').getValue()) {
										var dsfsInfo='<span style="color:red">邮件将正在发送中...</span>';
										t.down('displayfield[name=dsfsInfo]').setValue(dsfsInfo);
										t.down('displayfield[name=dsfsInfo]').setVisible(true);
                                    }
                                    */
                                }
                            });
                        }
                    }
                },
                interval: 5000,
                scopt: this
            };
            refreshTaskMgr.start(refreshTask);
        }
    },
    initComponent: function () {
        var me = this;

        Ext.util.CSS.createStyleSheet(" .readOnly-tagfield-BackgroundColor div {background:#f4f4f4;}","readOnly-tagfield-BackgroundColor");
        Ext.util.CSS.createStyleSheet(" .readOnly-combox-BackgroundColor input {background:#f4f4f4;}","readOnly-combox-BackgroundColor");
		Ext.util.CSS.createStyleSheet("div.tz_emlatt_upload_btn{position:absolute;}","tz_emlatt_upload_btn");
		Ext.util.CSS.createStyleSheet(" .disabled-button-color span {opacity: 0.8;}","disabled-button-color");

        Ext.apply(this, {
            items: [{
                xtype: 'form',
                reference: 'znxDetForm',
                layout: {
                    type: 'vbox',      // Arrange child items vertically
                    align: 'stretch'    // 控件横向拉伸至容器大小
                },
                border: false,
                bodyPadding: 8,
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 120,
                    labelStyle: 'font-weight:bold'
                },
                items: [{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_DET_STD.znxQfId","群发任务id") ,
                    name: 'znxQfId',
                    hidden:true
                },{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_DET_STD.znxQfDesc","群发任务名称") ,
                    name: 'znxQfDesc',
					allowBlank:false
                },{
                    xtype: 'tagfield',
                    fieldLabel: Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_DET_STD.recever","收件人"),
                    reference: 'recever',
                    name:'recever',
                    allowBlank:false,
                    displayField: 'desc',
                    valueField: 'id',
					autoScroll: true,
                    createNewOnEnter: false,
                    createNewOnBlur: false,
                    filterPickList: true,
                    queryMode: 'local',
                    publishes: 'value',
                    //editable: false,
                    /*
                    listeners: {
                        change:'receverChange'
                    },*/
                    listConfig:{
                        maxHeight:200
                    },
					beforeBodyEl: [
                                '<li id="{id}-viewMoreRecever" data-qtip="查看更多" data-ref="viewMoreRecever" class="x-tagfield-item x-tagfield-item-selected" style="cursor:pointer;right:5px;bottom:2px;position:absolute;display:none;">' +
                                '<span class="'+
                                'x-tagfield-item-text" style="padding-right:4px;">查看更多</span>' +
                                '</li>'
                    	],
					childEls: [
                            'viewMoreRecever'
                        ],
					listeners: {
					   select: function(combo,record,index,eOpts){
						        var me = this;
						        me.inputEl.dom.value = "";
						},
						change: function (field) {
							var receverEmailAddr = field.getValue();
							var viewMoreRecever = this.viewMoreRecever;
							var len = receverEmailAddr.length;
							if(len >= 20){
								viewMoreRecever.applyStyles("display:block");
								if(viewMoreRecever){
									viewMoreRecever.addListener("click", "viewMoreReceverForm");	
								}
							}else{
								viewMoreRecever.applyStyles("display:none");	
							}
						}
					}
                },{
                    layout: {
                        type:'hbox'
                    },
                    padding:'0 0 8px 0',
                    xtype:"toolbar",
                    items:["->",{
						xtype:'button',
                        reference:'selectStuBtn',
                        text:'<span style="color:#fff">选择考生</span>',
                        tooltip:Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_DET_STD.selectStuTip","选择考生"),
                        handler:'addStruData',
						baseCls:'x-btn x-unselectable x-column x-btn-default-small'	
					},{
                        xtype:'button',
                        reference:'addAudienceBtn',
                        text:'<span style="color:#fff">添加听众</span>',
                        tooltip:Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_DET_STD.addAudienceTip","添加听众"),
                        handler:'addAudience',
						baseCls:'x-btn x-unselectable x-column x-btn-default-small'
                    },{
                        xtype:'button',
                        reference:'clearAllBtn',
                        text:'<span style="color:#fff">清除所有</span>',
                        tooltip:Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_DET_STD.clearAllTip","清除所有"),
                        handler:'clearAll',
						baseCls:'x-btn x-unselectable x-column x-btn-default-small'
                    }]
                },{
                    layout: {
                        type: 'column'
                    } ,
                    padding:'0 0 8px 0',
                    items:[{
                            xtype: 'combobox',
                            fieldLabel:Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_DET_STD.znxTmpId","站内信模板") ,
                            name: 'znxTmpId',
                            reference:'znxTmpId',
                            queryMode: 'remote',
                            valueField: 'znxtmpl',
                            displayField: 'desc',
                            editable: false,
                            columnWidth:1,
                            triggerAction: 'all',
                            triggers:{
                                clear: {
                                    cls: 'x-form-clear-trigger',
                                    handler: function(field){
                                        field.setValue("");

                                        var formdatanull={
                                            //"sender": "",
                                            "znxSubj": "",
                                            "znxCont": ""
                                        };
                                        var form = field.findParentByType('form');
                                        form.getForm().setValues(formdatanull);
                                        form.down('grid[reference=znxTmplItemGrid]').store.removeAll();
                                    }
                                }
                            },
                            listeners: {
                                change:function(t, newValue, oldValue, eOpts){
                                	var emlBkDetForm = t.findParentByType('form');
                                    if(newValue!=""){
                                            emlBkDetForm.down('button[reference=setZnxTmpl]').disabled=false;
                                            emlBkDetForm.down('button[reference=setZnxTmpl]').removeCls('disabled-button-color');
                                            //加载邮件模版信息
                                            var form = emlBkDetForm.getForm();
                                            var tzParams = '{"ComID":"TZ_ZNX_GL_COM","PageID":"TZ_ZNX_DET_STD","OperateType":"getZnxTmpInfo","comParams":{"znxTmpId":"'+newValue+'"}}';
                                            Ext.tzLoadAsync(tzParams,function(responseData){
                                                var formData = responseData;
                                                form.setValues(formData);
                                            });
                                            var tzParams = '{"ComID":"TZ_ZNX_GL_COM","PageID":"TZ_ZNX_DET_STD","OperateType":"getZnxTmpItem","comParams":{"znxTmpId":"'+newValue+'"}}';
                                            Ext.tzLoadAsync(tzParams,function(responseData){
												var tmpItemGrid = emlBkDetForm.down('grid[reference=znxTmplItemGrid]');
												
												Ext.suspendLayouts();
												tmpItemGrid.store.suspendEvents();
												
                                                tmpItemGrid.store.removeAll(true);
                                                tmpItemGrid.store.add(responseData['root']);
												
												tmpItemGrid.store.resumeEvents();
												tmpItemGrid.reconfigure(tmpItemGrid.store);
												Ext.resumeLayouts(true);
												
                                                var userAgent = navigator.userAgent;
                                                if (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1) {
                                                    var copyItemsDom = document.getElementsByName("itememlCopy");
                                                    for (var i = 0; i < copyItemsDom.length; i++) {
                                                        $(copyItemsDom[i]).zclip({
                                                            beforeCopy: function () {
                                                                var itemHtml = this.parentNode.parentNode.parentNode.innerHTML;
                                                                var itemFirstCharPositon = itemHtml.indexOf("[");
                                                                var itemLastCharPositon = itemHtml.indexOf("]");
                                                                var itemPara = itemHtml.slice(itemFirstCharPositon, itemLastCharPositon + 1);
                                                                emlBkDetForm.down('textfield[name=copyfield]').setValue(itemPara);
                                                            },
                                                            copy: function () {
                                                                return emlBkDetForm.down('textfield[name=copyfield]').getValue();
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                    }else{
                                    	emlBkDetForm.down('button[reference=setZnxTmpl]').disabled=true;
										emlBkDetForm.down('button[reference=setZnxTmpl]').addCls('disabled-button-color');
                                    }
                                }
                            }
                        }, {
                            xtype:'button',
                            iconCls:'set',
                            reference:'setZnxTmpl',
                            tooltip:Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_DET_STD.setZnxTmpl","设置站内信模版"),
                            border:false,
                            style:{
                                background:'#fff'
                            },
                            disabled: true,
                            handler:'setZnxTmpl',
                            width:30
                        }
                    ]
                },{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_DET_STD.znxSubj","站内信标题") ,
                    name: 'znxSubj',
                    allowBlank:false
                },{
                    xtype: 'ueditor',
                    fieldLabel: Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_DET_STD.znxCont","站内信内容") ,
                    zIndex: 10,
                    //allowBlank:false,
                    height: 300,
                    name:'znxCont',
                    panelXtype: 'znxBulkDet'
                },{
                    xtype: 'textfield',
                    fieldLabel:"参数信息项复制",
                    name: 'copyfield',
                    hidden:true
                },{
                    xtype: 'panel',
                    title: '信息项设置',
                    collapsible:true,
                    collapsed:true,
                    width:'100%',
                    flex:1,
                    padding:'0 0 0 125px',
                    margin:'-25px 0 0 0',
                    items: [{
                        xtype: 'grid',
                        height:200,
                        border: true,
                        name: 'znxTmplItemGrid',
                        reference: 'znxTmplItemGrid',
                        columnLines: true,    //显示纵向表格线
                        viewConfig: {
                            enableTextSelection: true
                        },
                        columns: [{
                            text: Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_DET_STD.parainfoitem","信息项名称"),
                            sortable: true,
                            dataIndex: 'parainfoitem',
                            minWidth: 200,
                            flex:1
                        },{
                            xtype:'linkcolumn',
                            text: Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_DET_STD.iteminsert","插入"),
                            sortable: false,
                            width:70,
                            items:[{
                                text:"插入",
                                handler: "insertemlitem",
                                tooltip:"插入"
                            }]
                        },{
                            text: Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_DET_STD.itememlCopy","复制"),
                            menuDisabled: true,
                            sortable: false,
                            dataIndex: 'itemcopy',
                            width:70,
                            renderer:function(){
                                return '<a href="javascript:void(0)" name="itememlCopy">复制</a>';
                            }
                        }]}]
                },{
                    xtype:'fieldset',
                    title:Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_DET_STD.attaPanelTitle", '上传附件'),
                    collapsible: true,
                    collapsed: true,
                    defaults: {anchor: '100%'},
                    layout: 'anchor',
                    items: [{
                        xtype: 'grid',
                        height:200,
                        border: true,
                        margin:'0 0 8px 0',
                        name: 'znxInfoItemGrid',
                        reference: 'znxInfoItemGrid',
                        columnLines: true,    //显示纵向表格线
                        selModel: {
                            type: 'checkboxmodel'
                        },
						
                        tbar: [
                            {
                                xtype: 'form',
                                bodyStyle: 'padding:3px 0px 0px 0px',
								height: 30,
                                items: [
                                    {
                                        xtype: 'filefield',
                                        //buttonText: '上传附件',
                                        //name: 'attachmentUpload',
                                        name: 'orguploadfile',
                                        buttonOnly: true,
                                        width: 62,
										buttonConfig:{
											iconCls: 'upload',
											text: '<font style="color:#666;">上传</font>',
											cls: 'tz_emlatt_upload_btn x-btn-default-toolbar-small',
											tooltip: '上传附件',
										},
                                        listeners: {
                                            change: 'addAttachment'
                                        }
                                    }
                                ]},
                            "-",
                            {iconCls: 'remove', text: '删除', tooltip: "删除选中的数据", handler: 'deleteArtAttenments'}
                        ],
                        columns: [{
                            text: '附件名称',
                            dataIndex: 'attaName',
                            sortable: false,
                            minWidth: 100,
                            flex: 1,
                            renderer: function (v, d) {
                                return '<a href="' + d.record.data.attaUrl + '" target="_blank">' + v + '</a>';
                            }
                        },{
                            menuDisabled: true,
                            sortable: false,
                            width: 60,
                            xtype: 'actioncolumn',
                            items: [
                                {iconCls: 'remove', tooltip: '删除', handler: 'deleteArtAttenments'}
                            ]
                        }]
                    }]
                },{
                    xtype:'fieldset',
                    title: '创建人信息',
                    collapsible: true,
                    collapsed: true,
                    defaults: {anchor: '100%'},
                    layout: 'anchor',
                    items: [{
                        xtype: 'displayfield',
                        fieldLabel: Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_DET_STD.crePer","创建人"),
                        margin:'8px 0 0 8px',
                        name: 'crePer'
                    },{
                        xtype: 'displayfield',
                        fieldLabel: Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_DET_STD.dept","所属部门"),
                        margin:'8px 0 0 8px',
                        hidden: true,
                        name: 'dept'
                    },{
                        xtype: 'displayfield',
                        fieldLabel: Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_DET_STD.setId","setID"),
                        margin:'8px 0 0 8px',
                        hidden: true,
                        name: 'setId'
                    },{
                        xtype: 'displayfield',
                        fieldLabel: Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_DET_STD.creDt","创建时间"),
                        margin:'8px 0 0 8px',
                        name: 'creDt'
                    }]
                }]
            }]
        });
        this.callParent();
    },
    buttons: [ {
        text: '保存',
        iconCls:"save",
        handler: 'onPanelSave',
        reference:"saveBtn"
    },{
        text: '预览',
        iconCls:"preview",
        handler: 'preViewZnx'
    },{
        text: '发送',
        iconCls:"send",
        handler: 'sendZnx',
        reference:"sendBtn"
    },{
        text: '中断发送',
        iconCls:"revoke",
        handler: 'interSend',
        reference:"revokeBtn",
        hidden:true
    },{
        text: '查看发送状态',
        iconCls:"view",
        handler: 'viewSendHistory',
        reference:"viewHisBtn"
        //disabled:true
    },{
        text: '关闭',
        iconCls:"close",
        handler: 'onPanelClose'
    }]
});

