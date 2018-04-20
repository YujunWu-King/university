Ext.define('KitchenSink.view.clueManagement.clueManagement.import.clueImportWindow',{
    extend:'Ext.window.Window',
    reference:'clueImportWindow',
    xtype:'clueImportWindow',
    controller:'clueImportController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'KitchenSink.view.clueManagement.clueManagement.import.clueImportModel',
        'KitchenSink.view.clueManagement.clueManagement.import.clueImportStore',
        'KitchenSink.view.clueManagement.clueManagement.import.clueImportController'
    ],
    modal:true,
    title:'校验导入数据',
    width:880,
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    closable:false,
    ignoreChangesFlag:true,
	listeners:{
        resize: function(win){
            win.doLayout();
        },
		afterrender:function(win){
            var store = win.down('grid').getStore();
            Ext.defer(function(){
                //校验数据有效性
                win.getController().validate(store);
            },100,win,[]);
		}
    },
    constructor:function(obj) {
        this.impType = obj.impType;
        this.callParent();
    },
    initComponent: function () {
        var clueImportStore = new KitchenSink.view.clueManagement.clueManagement.import.clueImportStore();

        var localStore = new KitchenSink.view.common.store.comboxStore({
            recname:'TZ_XSXS_YXDQ_VW',
            condition:{
                TZ_JG_ID:{
                    value:Ext.tzOrgID,
                    operator:'01',
                    type:'01'
                }
            },
            result:'TZ_LABEL_NAME,TZ_LABEL_DESC'
        });
        var createWayStore = new KitchenSink.view.common.store.appTransStore("TZ_RSFCREATE_WAY");


        /*根据导入类型拼接动态列*/
        var dynamicsColumn="";
        if(this.impType=="ZSXS") {
            dynamicsColumn = [{
                dataIndex:'chargeOprid',
                hidden:true
            },{
                text:'责任人',
                dataIndex:'chargeName',
                width:100,
                editor:{
                    xtype:'textfield',
                    editable:false,
                    triggers: {
                        clear: {
                            cls: 'x-form-clear-trigger',
                            handler: 'clearCharge'
                        },
                        search: {
                            cls: 'x-form-search-trigger',
                            handler: "searchCharge"
                        }
                    }
                }
            },{
                text:'线索来源',
                dataIndex:'createWay',
                width:120,
                editor:{
                    xtype: 'combobox',
                    editable:false,
                    valueField: 'TValue',
                    displayField: 'TSDesc',
                    store: createWayStore,
                    queryMode: 'local'
                },
                renderer : function(value, metadata, record) {
                    var index = createWayStore.find('TValue',value);
                    if(index!=-1){
                        return createWayStore.getAt(index).data.TSDesc;
                    }
                    return record.get('createWay');
                }
            },{
                text:'创建时间',
                dataIndex:'createDttm',
                width:150,
                renderer : Ext.util.Format.dateRenderer('Y-m-d H:i'),
                editor:{
                    xtype:'datefield',
                    format: 'Y-m-d H:i'
                }
            }];
        }

        //固定列
        var gridColumnn = [{
            text: "序号",
            xtype: 'rownumberer',
            width:40
        },{
            xtype:'actioncolumn',
            width:30,
            header:'操作',
            sortable:false,
            items:[
                {iconCls:'remove',tooltip:'删除',handler:'deleteCurrImpRec'}
            ]
        },{
            xtype:'actioncolumn',
            align:'center',
            text:'校验',
            width:50,
            items:[{
                iconCls:'',
                tooltip:'',
                getClass:function(v,metadata,r){
                    var validationResult = r.get("validationResult");
                    if(validationResult==undefined) {
                        return "";
                    } else if(validationResult!=undefined&&validationResult==true) {
                        return "pass";
                    } else {
                        return "refuse";
                    }
                },
                /*
                 text:'&nbsp;',
                 getClass:function(v,metadata,r){
                 var validationResult = r.get("validationResult");
                 if(validationResult!=undefined&&validationResult==true){
                 return "fa fa-check-circle";
                 }else{
                 return "fa fa-exclamation-circle";
                 }
                 },
                 getStyle:function(v,metadata,r){
                 var style = "font-size:16px;";
                 var validationResult = r.get("validationResult");
                 if(validationResult!=undefined&&validationResult==true){
                 style=style+"color:#008B00";
                 }else{
                 style=style+"color:#EE0000";
                 }
                 return style;
                 },
                 */
                getTip:function(v,metadata,r){
                    var validationMsg = r.get("validationMsg");
                    if(validationMsg!=undefined&&validationMsg.length>0){
                        var tips = "";
                        Ext.each(validationMsg,function(tip,index){
                            tips = tips+(index>0?"，":"")+tip;
                        });
                        return tips;
                    }else{
                        return r.get("validationResult")==true?"校验通过":"校验未通过";
                    }
                }
            }]
        },{
            text:'姓名',
            dataIndex:'name',
            width:70,
            editor:{
                xtype:'textfield',
                allowBlank:false
            }
        },{
            text:'手机',
            dataIndex:'mobile',
            width:100,
            editor:{
                xtype:'textfield'
            }
        },{
            text:'公司',
            dataIndex:'companyName',
            width:150,
            editor:{
                xtype:'textfield',
                allowBlank:false
            }
        },{
            text:'职位',
            dataIndex:'position',
            width:100,
            editor:{
                xtype:'textfield'
            }
        },{
            text:'常住地',
            dataIndex:'localName',
            width:90,
            editor:{
                /*xtype:'textfield',
                 editable:false,
                 triggers: {
                 clear: {
                 cls: 'x-form-clear-trigger',
                 handler: 'clearLocal'
                 },
                 search: {
                 cls: 'x-form-search-trigger',
                 handler: "searchLocal"
                 }
                 }*/
                xtype: 'combobox',
                editable:false,
                valueField: 'TZ_LABEL_NAME',
                displayField: 'TZ_LABEL_DESC',
                store: localStore,
                queryMode: 'local'
            },
            renderer : function(value, metadata, record) {
                var index = localStore.find('TZ_LABEL_NAME',value);
                if(index!=-1){
                    return localStore.getAt(index).data.TZ_LABEL_DESC;
                }
                return record.get('localName');
            }
        },{
            text:'备注',
            dataIndex:'memo',
            width:120,
            editor:{
                xtype:'textfield'
            }
        }];


        var columns = [];
        for (var i=0; i<gridColumnn.length; i++){
            columns.push(gridColumnn[i]);
        }

        if(dynamicsColumn!="") {
            for(var j=0;j<dynamicsColumn.length;j++) {
                columns.push(dynamicsColumn[j]);
            }
        }



        Ext.apply(this,{
           items:[{
               xtype:'grid',
               frame:false,
               minHeight:150,
               maxHeight:300,
               width:"100%",
               height:'auto',
               columnLines: true,
               multiSelect: true,
               store:clueImportStore,
               name:'clueImportGrid',
               reference:'clueImportGrid',
               dockedItems: [{
                   xtype: "toolbar",
                   items: [
                       {
                           xtype:'displayfield',
                           value:'请检查与校验导入的数据格式，对于“校验”结果列中显示错误的数据，请进行修改或删除后再点击“导入”。' +
                           '<br>' +
                           '如果校验不通过，“校验”列显示×，鼠标浮动可显示校验不通过原因。如果校验通过，“校验”列显示√。',
                           fieldStyle:"color:#777;font-weight:bold"
                       }
                   ]
               }],
               plugins:[{
                   ptype:'cellediting',
                   clicksToEdit:1
               }],
               columns:columns
           },{
               xtype:'form',
               style:'margin:8px',
               items:[{
                   xtype:'fieldcontainer',
                   layout: 'hbox',
                   items:[{
                       width:740,
                       xtype:"displayfield",
                       hideLabel:true,
                       labelSeparator:"",
                       name:"ImpClueCount"
                   }]
               }]
           }]
        });
        this.callParent();
    },
    buttons:[{
        minWidth:80,
        text:"重新校验",
        iconCls:"ensure",
        name:"validateAgain",
        handler:"validateAgain"
    },{
        minWidth:80,
        text:'导入',
        iconCls:"import",
		name:"importWinConfirm",
        handler:"onWinImport"
    },{
        minWidth:80,
        text:"关闭",
        iconCls:"close",
        handler:'onWinClose'
    }],
    tools:[{
        type:'close',
        callback:function(win){
            var range = win.down("grid").getStore().getRange();

            if(range==undefined||range.length==0){
                win.close();
                return;
            }

            Ext.MessageBox.confirm('确认', '您还有数据没有导入，确定离开页面吗?', function(btnId){
                if(btnId == 'yes'){
                    win.close();
                }
            },this);
        }
    }]
});
