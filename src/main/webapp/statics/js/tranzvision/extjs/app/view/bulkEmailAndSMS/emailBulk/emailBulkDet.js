var refreshTaskMgr=new Ext.util.TaskRunner();
Ext.define('KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDet', {
    extend: 'Ext.panel.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'tranzvision.extension.grid.column.Link',
        'KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetSenderStore',
        'KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkMgrStore',
        'KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetController'
    ],
    xtype: 'emailBulkDet',
    title:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.panelTitle","邮件发送定义"),
    controller: 'emailBulkDetController',
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
                    if(currentPage.id.indexOf('emailBulkDet')!=-1){
                        if (t.BulkTaskId!=" "){
                            params = '"emlQfId":"'+t.BulkTaskId+'"';
                            var tzParams = '{"ComID":"TZ_EMLQ_COM","PageID":"TZ_EMLQ_DET_STD","OperateType":"getRwzxZt","comParams":{'+params+'}}';
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
                                    /*
									if (jsonObject.comContent.rwzxZt == "C" || jsonObject.comContent.rwzxZt == "D") {
                                        t.down('button[reference=revokeBtn]').setVisible(false);
										t.down('displayfield[name=dsfsInfo]').setVisible(false);
                                    }
									
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
        var numreg = /^[0-9]*$/i;
        Ext.apply(Ext.form.field.VTypes, {
            numberOnly: function(val, field) {
                var bolFlag;
                bolFlag = numreg.test(val);
                return bolFlag;
            },
            numberOnlyText: '请输入大于0的正整数'
            //numberOnlyMask: /[\d\s:amp]/i
        });

        Ext.util.CSS.createStyleSheet(" .readOnly-tagfield-BackgroundColor div {background:#f4f4f4;}","readOnly-tagfield-BackgroundColor");
        Ext.util.CSS.createStyleSheet(" .readOnly-combox-BackgroundColor input {background:#f4f4f4;}","readOnly-combox-BackgroundColor");
		Ext.util.CSS.createStyleSheet("div.tz_emlatt_upload_btn{position:absolute;}","tz_emlatt_upload_btn");

        Ext.apply(this, {
            items: [{
                xtype: 'form',
                reference: 'emlBulkDetForm',
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
                    fieldLabel:"群发任务id" ,
                    name: 'emlQfId',
                    hidden:true
                },{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.emlQfDesc","群发任务名称") ,
                    name: 'emlQfDesc'
                },/*{
                    xtype: 'combobox',
                    fieldLabel:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.sender","发件人") ,
                    name: 'sender',
                    allowBlank:false,
                    queryMode: 'remote',
                    valueField: 'email',
                    displayField: 'desc',
                    editable: false
                },*/{
                    xtype: 'fieldset',
                    border:false,
                    reference:'sendModelSet',
                    defaultType: 'radio', // each item will be a radio button
                    layout: 'hbox',
                    defaults: {
                        hideEmptyLabel: true
                    },
                    style:{
                        margin:'0 0 0 -10px'
                    },
                    items: [{
                        xtype:'label',
                        baseCls:'x-form-item-label-inner x-form-item-label-inner-default',
                        width:'125px',
                        style:{
                            padding:'8px 0 0 0',
                            'font-weight':'bold'
                        },
                        text: Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.sendModel","发送模式:")
                    }, {
                        checked: true,
                        boxLabel: '一般发送',
                        name: 'sendModel',
                        inputValue: 'NOR',
                        reference:'sendModelNor',
                        listeners: {
                            change: 'norSend'
                        }
                    }, {
                        xtype:'button',
                        iconCls:'fa fa-question-circle',
                        tooltip:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.NorTip","使用邮件模板或者直接编写邮件内容发送，选择邮件模板时收件人只能添加听众"),
                        border:false,
                        style:{
                            background:'#fff',
                            padding:'8px 0 0 0'
                        },
                        disabled:true
                    },{
                        xtype:'splitter',
                        width:100,
                        style:{
                            background:'#fff'
                        }
                    }, {
                        boxLabel: '导入Excel发送',
                        name: 'sendModel',
                        inputValue: 'EXC',
                        reference:'sendModelExc',
                        listeners: {
                            change: 'excSend'
                        }
                    },{
                        xtype:'button',
                        iconCls:'fa fa-question-circle',
                        tooltip:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.ExcTip","导入Excel之后，可以设置Excel中对应的邮件收件人、内容等，并使用设置内容进行发送"),
                        border:false,
                        style:{
                            background:'#fff',
                            padding:'8px 0 0 0'
                        },
                        disabled:true
                    },{
                        xtype:'button',
                        text:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.excBtn","导入Excel"),
                        tooltip:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.excBtnTip","导入Excel"),
                        handler:"importFromExcel",
                        reference:'impExc',
                        bind: {
                            hidden: '{!sendModelExc.checked}'
                        }
                    }]
                },{
                    xtype:'splitter',
                    height:8,
                    style:{
                        background:'#fff'
                    }
                },{
                    xtype: 'tagfield',
                    fieldLabel: Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.recever","收件人"),
                    //emptyText:'可手工填写电子邮箱；也可从EXCEL中粘贴某一列的电子邮箱',
                    reference: 'recever',
                    name:'recever',
                    allowBlank:false,
                    displayField: 'desc',
                    valueField: 'id',
                    //maxHeight:80,
                    //height:80,
					autoScroll: true,
                    createNewOnEnter: true,
                    createNewOnBlur: true,
                    filterPickList: true,
                    queryMode: 'local',
                    publishes: 'value',
                    listeners: {
                        change:'receverChange'
                    },
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
                    xtype: 'textfield',
                    fieldLabel:"收件人" ,
                    name: 'receverOrigin',
                    hidden:true
                },{
                    layout: {
                        type:'hbox'
                    },
                    padding:'0 0 8px 0',
                    xtype:"toolbar",
                    items:["->"/*,{
                        xtype:'button',
                        reference:'addAudienceBtn',
                        text:'<span style="color:#fff">添加听众</span>',
                        tooltip:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.addAudienceTip","添加听众"),
                        handler:'addAudience',
                        baseCls:'x-btn x-unselectable x-box-item x-toolbar-item x-btn-default-small x-btn-inner x-btn-inner-default-small'
                    }*/,{
                        xtype:'button',
                        reference:'clearAllBtn',
                        text:'<span style="color:#fff">清除所有</span>',
                        tooltip:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.clearAllTip","清除所有"),
                        handler:'clearAll',
                        baseCls:'x-btn x-unselectable x-box-item x-toolbar-item x-btn-default-small x-btn-inner x-btn-inner-default-small'
                    },{
                        xtype:'button',
                        reference:'pasteFromExcelBtn',
                        iconCls:'fa fa-clipboard',
                        tooltip:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.pasteFromExcel","从Excel粘贴"),
                        handler:'pasteFromExcel',
                        baseCls:'x-btn-inner x-btn-inner-default-small',
                        style:{
                            color:'#666',
							cursor:'pointer'
                        },
                        border:false
                    }]
                },{
                    xtype: 'fieldset',
                    reference:'tsfsSet',
                    border:false,
                    layout: {
                        type:'hbox'
                    },
                    style:{
                        padding:0
                    },
                    items: [{
                        xtype:'splitter',
                        width:125,
                        style:{
                            background:'#fff'
                        }
                    }, {
                        xtype:'checkbox',
                        boxLabel: Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.tsfsEmail","同时发送给"),
                        reference: 'tsfsFlag',
                        name: 'tsfsFlag',
                        width:125
                    },{
                        xtype: 'textfield',
                        name: 'tsfsEmail',
                        labelSeparator:' ',
                        width:'75%',
                        vtype: 'email',
                        bind: {
                            disabled: '{!tsfsFlag.checked}'
                        }
                    }]
                },{
                    xtype: 'tagfield',
                    fieldLabel: Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.mailCC","抄送"),
                    reference: 'mailCC',
                    displayField: 'desc',
                    valueField: 'email',
                    createNewOnEnter: true,
                    createNewOnBlur: true,
                    filterPickList: true,
                    queryMode: 'local',
                    publishes: 'value',
                    listeners: {
                        change:function(field,newValue, oldValue){
                            var newValue=newValue+"";
                            var oldValue=oldValue+"";
                            var arrNewValue = newValue.split(',');
                            var newinput= arrNewValue[arrNewValue.length-1];
                            if (newinput!="") {
                                if(newValue.length>oldValue.length) {
                                    var EmlReg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
                                    if (!EmlReg.test(newinput)) {
                                        Ext.Msg.alert("提示", "请输入正确的邮箱地址.");
                                        field.setValue(oldValue);
                                    }
                                    return;
                                }
                            }
                        }
                    }
                },{
                    layout: {
                        type: 'column'
                    } ,
                    padding:'0 0 8px 0',
                    items:[{
                            xtype: 'combobox',
                            fieldLabel:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.emlTmpId","邮件模版") ,
                            name: 'emlTmpId',
                            reference:'emlTmpId',
                            queryMode: 'remote',
                            valueField: 'emailtmpl',
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
                                            "emlSubj": "",
                                            "emlCont": ""
                                        };
                                        var form = field.findParentByType('form');
                                        form.getForm().setValues(formdatanull);
                                        form.down('grid[reference=emlTmplItemGrid]').store.removeAll();
                                    }
                                }
                            },
                            listeners: {
                                change:function(t, newValue, oldValue, eOpts){
                                    var emlBkDetForm = t.findParentByType('form');
                                    if(emlBkDetForm.child('fieldset[reference=sendModelSet]').child('radio[reference=sendModelNor]').checked){
                                        if(newValue!=""){
                                            emlBkDetForm.child('tagfield[reference=recever]').setEditable(false);
                                            emlBkDetForm.child('tagfield[reference=recever]').disabled=true;
                                            emlBkDetForm.down('tagfield[reference=recever]').addCls('readOnly-tagfield-BackgroundColor');
                                            emlBkDetForm.child('toolbar').child('button[reference=pasteFromExcelBtn]').disabled=true;

                                            emlBkDetForm.down('button[reference=setEmlTmpl]').disabled=false;
                                            emlBkDetForm.down('button[reference=setEmlTmpl]').removeCls('x-item-disabled x-btn-disabled');
                                            //加载邮件模版信息
                                            var form = emlBkDetForm.getForm();
                                            var tzParams = '{"ComID":"TZ_EMLQ_COM","PageID":"TZ_EMLQ_DET_STD","OperateType":"getEmlTmpInfo","comParams":{"emlTmpId":"'+newValue+'"}}';
                                            Ext.tzLoadAsync(tzParams,function(responseData){
                                                var formData = responseData;
                                                form.setValues(formData);
                                            });
                                            var tzParams = '{"ComID":"TZ_EMLQ_COM","PageID":"TZ_EMLQ_DET_STD","OperateType":"getEmlTmpItem","comParams":{"emlTmpId":"'+newValue+'"}}';
                                            Ext.tzLoadAsync(tzParams,function(responseData){
                                                emlBkDetForm.down('grid[reference=emlTmplItemGrid]').store.removeAll(true);
                                                emlBkDetForm.down('grid[reference=emlTmplItemGrid]').store.add(responseData['root']);

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
                                            emlBkDetForm.child('tagfield[reference=recever]').setEditable(true);
                                            emlBkDetForm.child('tagfield[reference=recever]').disabled=false;
                                            emlBkDetForm.down('tagfield[reference=recever]').removeCls('readOnly-tagfield-BackgroundColor');
                                            emlBkDetForm.child('toolbar').child('button[reference=pasteFromExcelBtn]').disabled=false;

                                            emlBkDetForm.down('button[reference=setEmlTmpl]').disabled=true;
                                        }
                                    }
                                }
                            }
                        }, {
                            xtype:'button',
                            iconCls:'set',
                            reference:'setEmlTmpl',
                            tooltip:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.setTml","设置邮件模版"),
                            border:false,
                            style:{
                                background:'#fff'
                            },
                            disabled: true,
                            handler:'setEmlTmpl',
                            width:30
                        }
                    ]
                },{
                    xtype: 'combobox',
                    fieldLabel:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.sender","发件人") ,
                    name: 'sender',
                    allowBlank:false,
                    queryMode: 'remote',
                    valueField: 'email',
                    displayField: 'desc',
                    editable: false
                },{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.emlSubj","邮件主题") ,
                    name: 'emlSubj',
                    allowBlank:false
                },{
                    xtype: 'ueditor',
                    fieldLabel: Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.emlCont","邮件内容") ,
                    zIndex: 10,
                    //allowBlank:false,
                    height: 300,
                    name:'emlCont',
                    panelXtype: 'emailBulkDet'
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
                        name: 'emlTmplItemGrid',
                        reference: 'emlTmplItemGrid',
                        columnLines: true,    //显示纵向表格线
                        viewConfig: {
                            enableTextSelection: true
                        },
                        columns: [{
                            text: Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.parainfoitem","信息项名称"),
                            sortable: true,
                            dataIndex: 'parainfoitem',
                            minWidth: 200,
                            flex:1
                        },{
                            xtype:'linkcolumn',
                            text: Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.iteminsert","插入"),
                            sortable: false,
                            width:70,
                            items:[{
                                text:"插入",
                                handler: "insertemlitem",
                                tooltip:"插入"
                            }]
                        },{
                            text: Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.itememlCopy","复制"),
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
                    title:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.attaPanelTitle", '上传附件'),
                    collapsible: true,
                    collapsed: true,
                    defaults: {anchor: '100%'},
                    layout: 'anchor',
                    items: [{
                        xtype: 'grid',
                        height:200,
                        border: true,
                        margin:'0 0 8px 0',
                        name: 'emlInfoItemGrid',
                        reference: 'emlInfoItemGrid',
                        columnLines: true,    //显示纵向表格线
                        selModel: {
                            type: 'checkboxmodel'
                        },
						
                        tbar: [
                            {
                                xtype: 'form',
                                //bodyStyle: 'padding:10px 0px 0px 0px',
								height: 32,
                                items: [
                                    {
                                        xtype: 'filefield',
                                        //buttonText: '上传附件',
                                        //name: 'attachmentUpload',
                                        name: 'orguploadfile',
                                        buttonOnly: true,
                                        width: 78,
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
						
						/*
						dockedItems:[{
							xtype: 'toolbar',
							dock: 'top',
							items: [{
								xtype: 'form',
								layout: {
									type: 'vbox',     
									align: 'stretch' 
								},
								items: [{
									xtype: 'filebutton',
									text: '上传附件',
									name: 'attachmentUpload',
									//buttonOnly: true,
									width: 88,
									listeners: {
										change: 'addAttachment'
									}
								}]
							},"-",
                            {iconCls: 'remove', text: '删除', tooltip: "删除选中的数据", handler: 'deleteArtAttenments'}]
						}],
						*/
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
                    title: '发送设置',
                    collapsible: true,
                    collapsed: true,
                    defaults: {anchor: '100%'},
                    layout: 'anchor',
                    items: [{
                        xtype: 'checkbox',
                        boxLabel: Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.edmFlag","启用EDM统计功能"),
                        name:'edmFlag',
                        margin:'8px 0 0 8px',
                        inputValue:'Y',
                        uncheckedValue:'N'
                    },{
                        xtype: 'checkbox',
                        boxLabel: Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.qxdyFlag","邮件内容包含取消订阅链接"),
                        name:'qxdyFlag',
                        margin:'8px 0 0 8px',
                        inputValue:'Y',
                        uncheckedValue:'N'
                    },{
                        xtype: 'checkbox',
                        boxLabel: Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.qypfFlag","启用频发策略"),
                        name:'qypfFlag',
                        reference:'qypfFlag',
                        margin:'8px 0 0 8px',
                        inputValue:'Y',
                        uncheckedValue:'N'
                    }, {
                        layout: {
                            type: 'hbox',
                            align: 'stretch'
                        },
                        items: [{
                            xtype: 'textfield',
                            fieldLabel: Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.fsslXs", "每小时发送"),
                            name: 'fsslXs',
                            margin: '8px 0 0 33px',
                            width:150,
                            labelWidth: 80,
                            labelSeparator: ' ',
                            minValue: 0,
                            vtype:'numberOnly',
                            labelStyle: 'font-weight:normal',
                            bind: {
                                hidden: '{!qypfFlag.checked}'
                            }
                        },{
                            xtype:'label',
                            text: Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.fsslXsLab","封邮件"),
                            margin: '16px 0 0 5px',
                            bind: {
                                hidden: '{!qypfFlag.checked}'
                            }
                        }]
                    },{
                        xtype: 'checkbox',
                        boxLabel: Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.dsfsFlag","定时发送"),
                        name:'dsfsFlag',
                        reference:'dsfsFlag',
                        margin:'8px 0 0 8px',
                        inputValue:'Y',
                        uncheckedValue:'N',
                        listeners: {
                            change:function(t,newValue, oldValue){
                                if(!newValue){
                                    t.findParentByType('form').down('displayfield[name=dsfsInfo]').setVisible(false);
                                }
                            }
                        }
                    },{
                        xtype: 'datefield',
                        fieldLabel: Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.dsfsDate","发送日期"),
                        name: 'dsfsDate',
                        margin:'8px 0 0 33px',
                        itemId: 'dsfsDate',
                        repeatTriggerClick:true,
                        format: 'Y-m-d',
                        labelStyle: 'font-weight:normal',
                        labelWidth: 80,
                        bind: {
                            hidden: '{!dsfsFlag.checked}'
                        }
                    },{
                        xtype: 'timefield',
                        fieldLabel:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.dsfsTime","发送时间"),
                        margin:'8px 0 0 33px',
                        name: 'dsfsTime',
                        format: 'H:i',
                        increment: 5,
                        labelStyle: 'font-weight:normal',
                        labelWidth: 80,
                        bind: {
                            hidden: '{!dsfsFlag.checked}'
                        }
                    },{
                        xtype: 'checkbox',
                        boxLabel: Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.qzfsFlag","不考虑免打扰，强制推送"),
                        name:'qzfsFlag',
                        margin:'8px 0 0 8px',
                        inputValue:'Y',
                        uncheckedValue:'N'
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
                        fieldLabel: Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.crePer","创建人"),
                        margin:'8px 0 0 8px',
                        name: 'crePer'
                    }/*,{
                        xtype: 'displayfield',
                        fieldLabel: Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.dept","所属部门"),
                        margin:'8px 0 0 8px',
                        name: 'dept'
                    },{
                        xtype: 'displayfield',
                        fieldLabel: Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.setId","setID"),
                        margin:'8px 0 0 8px',
                        name: 'setId'
                    }*/,{
                        xtype: 'displayfield',
                        fieldLabel: Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_DET_STD.creDt","创建时间"),
                        margin:'8px 0 0 8px',
                        name: 'creDt',
                        hidden:true
                    }]
                },{
                    xtype: 'displayfield',
                    fieldLabel: " ",
                    labelSeparator:' ',
                    margin:'8px 0 0 8px',
                    name: 'dsfsInfo',
                    hidden:true
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
        handler: 'preViewEmail'
    },{
        text: '发送',
        iconCls:"send",
        handler: 'sendEmail',
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
        reference:"viewHisBtn",
        //disabled:true
    },{
        text: '关闭',
        iconCls:"close",
        handler: 'onPanelClose'
    }]
});
