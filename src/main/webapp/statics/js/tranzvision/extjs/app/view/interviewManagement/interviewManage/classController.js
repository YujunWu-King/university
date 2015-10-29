Ext.define('KitchenSink.view.interviewManagement.interviewManage.classController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.interviewClassMgr',
    interviewManage:function(grid, rowIndex, colIndex){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_MS_MGR_COM"]["TZ_MS_CHSBAT_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }

        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MS_CHSBAT_STD，请检查配置。');
            return;
        }

        var record = grid.store.getAt(rowIndex);
        var classID = record.data.classID;

		var win = this.lookupReference('msMgrBatchListWin');
			
		if (!win) {
				//className = 'KitchenSink.view.activity.applyOptionsWindow';
				Ext.syncRequire(className);
				ViewClass = Ext.ClassManager.get(className);
				//新建类
			win = new ViewClass();
			this.getView().add(win);
		}

		var windowgrid = this.lookupReference('msMgrBatchWinGrid');

		var tzStoreParams = "{'classID':'"+classID+"'}";
		windowgrid.store.tzStoreParams = tzStoreParams;
		windowgrid.store.load();		

		win.show();
    },

	chooseMsBatch:function(grid, rowIndex, colIndex){
        var batchWindow = grid.up('window');

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_MS_MGR_COM"]["TZ_MS_IVWMGR_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MS_IVWMGR_STD，请检查配置。');
            return;
        }
		
        var contentPanel, cmp, ViewClass, clsProto;

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
            // <debug warn>
            // Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
            // </debug>
        }

        var record = grid.store.getAt(rowIndex);
        var classID = record.data.classID;
		var batchID = record.data.batchID;
		//Ext.MessageBox.alert('提示', '班级编号：'+ classID + '面试批次编号：'+ batchID);
		
        cmp = new ViewClass();

        cmp.on('afterrender',function(panel){
            var form = panel.child('form').getForm();

            var tzParams = '{"ComID":"TZ_MS_MGR_COM","PageID":"TZ_MS_IVWMGR_STD","OperateType":"QF","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'"}}';
            Ext.tzLoad(tzParams,function(responseData){
                form.setValues(responseData['clsbat']);
                //可配置搜索store.reload()时参数传递
                /*
                Params= '{"cfgSrhId":"TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.TZ_MS_ITWMGRL_V","condition":{"TZ_CLASS_ID-operator":"01","TZ_CLASS_ID-value":"'+classID+'","TZ_BATCH_ID-operator":"01","TZ_BATCH_ID-value":"'+ batchID+'"}}';
                panelgrid.store.tzStoreParams = Params;
                panelgrid.store.reload();
                */
                //加载是否参加Transvalue
                var msResJoinStateStore = new KitchenSink.view.common.store.appTransStore("TZ_MS_JOIN_STATE");
                msResJoinStateStore.load();
                //为是否参加filter添加值
                var msResJoinStateSortFilterOptions=[];
                msResJoinStateStore.addListener("load",function(store, records, successful, eOpts){
                    for(var i=0;i<records.length;i++){
                        msResJoinStateSortFilterOptions.push([records[i].data.TValue,records[i].data.TSDesc]);
                    };
                });
                //加载面试状态Transvalue
                var msResResultStore  = new KitchenSink.view.common.store.appTransStore("TZ_MS_RESULT");
                msResResultStore.load();
                //为面试状态filter添加值
                var msResResultSortFilterOptions=[];
                msResResultStore.addListener("load",function(store, records, successful, eOpts){
                    for(var i=0;i<records.length;i++){
                        msResResultSortFilterOptions.push([records[i].data.TValue,records[i].data.TSDesc]);
                    };
                });
                //预约状态
                var msArrStateStore = new KitchenSink.view.common.store.appTransStore("TZ_MS_ARR_STATE");
                //msArrStateStore.load();
                //为filter添加值
                var msArrStateFilterOptions=[];
                msArrStateStore.addListener("load",function(store, records, successful, eOpts){
                    for(var i=0;i<records.length;i++){
                        msArrStateFilterOptions.push([records[i].data.TValue,records[i].data.TSDesc]);
                    };
                });

                //确认状态
                var msConfStateStore = new KitchenSink.view.common.store.appTransStore("TZ_MS_CONF_STA");
                //为filter添加值
                var msConfStateFilterOptions=[];
                msConfStateStore.addListener("load",function(store, records, successful, eOpts){
                    for(var i=0;i<records.length;i++){
                        msConfStateFilterOptions.push([records[i].data.TValue,records[i].data.TSDesc]);
                    };
                });
                //msConfStateStore.load();

                //gridStore添加filterchange监听
                var msResGridStore = new KitchenSink.view.interviewManagement.interviewManage.interviewMgrStore({
                    listeners:{
                        filterchange:function( store, filters, eOpts ){
                            var clearFiltersBtn =  msResGrid.down('toolbar').items.first();
                            if(filters.length>0){
                                clearFiltersBtn.setDisabled( false );
                            }else{
                                clearFiltersBtn.setDisabled( true );
                            }
                        }
                    }
                });

                //类别
                var orgColorSortStore = new KitchenSink.view.common.store.comboxStore({
                    recname:'TZ_ORG_COLOR_V',
                    condition:{
                        TZ_JG_ID:{
                            value:Ext.tzOrgID,
                            operator:'01',
                            type:'01'
                        }},
                    result:'TZ_COLOR_SORT_ID,TZ_COLOR_NAME,TZ_COLOR_CODE'
                });
                var stuGridColorSortFilterOptions=[];/*考生类别的过滤器数据*/
                orgColorSortStore.addListener("load",function(store, records, successful, eOpts){
                    for(var i=0;i<records.length;i++){
                        stuGridColorSortFilterOptions.push([records[i].data.TZ_COLOR_SORT_ID,records[i].data.TZ_COLOR_NAME]);
                    };
                });
                orgColorSortStore.load();

                Ext.util.CSS.createStyleSheet(" .x-grid-cell.msMgrArrStateYesStyle {background-color: #66cc66;}","msMgrArrStateYesStyle");
                Ext.util.CSS.createStyleSheet(" .x-grid-cell.msMgrArrStateNoStyle {background-color: #ff0000;}","msMgrArrStateNoStyle");

                Ext.util.CSS.createStyleSheet(" .x-grid-cell.msMgrConfigStateYesStyle {background-color: #66cc66;}","msMgrConfigStateYesStyle");
                Ext.util.CSS.createStyleSheet(" .x-grid-cell.msMgrConfigStateNAStyle {background-color: #ff0000;}","msMgrConfigStateNAStyle");
                Ext.util.CSS.createStyleSheet(" .x-grid-cell.msMgrConfigStateNoStyle {background-color: #ffa000;}","msMgrConfigStateNoStyle");

                var msResGridColumns = [
                    {
                        text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.appId","报名表编号") ,
                        locked:true,
                        lockable:false,
                        filter: {
                            type: 'number',
                            fields:{gt: {iconCls: Ext.baseCSSPrefix + 'grid-filters-gt', margin: '0 0 3px 0',emptyText: '输入数字'}, lt: {iconCls: Ext.baseCSSPrefix + 'grid-filters-lt', margin: '0 0 3px 0',emptyText: '输入数字'}, eq: {iconCls: Ext.baseCSSPrefix + 'grid-filters-eq', margin: 0,emptyText: '输入数字'}}
                        },
                        sortable: true,
                        dataIndex: 'appId',
                        width: 125
                    },{
                        text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.stuName","姓名") ,
                        locked:true,
                        lockable:false,
                        filter: {
                            type: 'string',
                            itemDefaults: {
                                emptyText: '搜索...'
                            }
                        },
                        sortable: true,
                        dataIndex: 'stuName',
                        width: 100
                    },{
                        text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.stuSex","性别") ,
                        lockable:false,
                        filter: {
                            type: 'list'
                        },
                        sortable: true,
                        dataIndex: 'stuSex',
                        width: 60
                    },{
                        text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.msGroupId","面试组") ,
                        lockable:false,
                        filter: {
                            type: 'list'
                        },
                        sortable: true,
                        dataIndex: 'msGroupId',
                        width: 75
                    },{
                        text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.msDate","面试日期") ,
                        lockable:false,
                        xtype:'datecolumn',
                        format:'Y-m-d',
                        filter: {
                            type: 'date',
                            dateFormat: 'Y-m-d',
                            fields:{lt: {text: '小于'}, gt: {text: '大于'}, eq: {text: '等于'}}
                        },
                        sortable: true,
                        dataIndex: 'msDate',
                        width: 105
                    },{
                        text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.msStartTime","开始时间") ,
                        lockable:false,
                        sortable: true,
                        dataIndex: 'msStartTime',
                        width: 90
                    },{
                        text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.msEndTime","结束时间") ,
                        lockable:false,
                        sortable: true,
                        dataIndex: 'msEndTime',
                        width: 90
                    },{
                        text: Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.msArrState","安排状态"),
                        lockable:false,
                        filter: {
                            type: 'list',
                            options: msArrStateFilterOptions
                        },
                        dataIndex: 'msArrState',
                        width: 100,
                        renderer : function(value, metadata, record) {
                            if (value=="B"){
                                metadata.tdCls = 'msMgrArrStateYesStyle';
                            }else{
                                metadata.tdCls = 'msMgrArrStateNoStyle';
                            }
                            //alert("render"+value);
                            var index = msArrStateStore.find('TValue',value);
                            if(index!=-1){
                                return msArrStateStore.getAt(index).data.TSDesc;
                            }
                            return record.get('msZGFlag');
                        }
                    },{
                        text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.msConfigState","确认状态") ,
                        lockable:false,
                        filter: {
                            type: 'list',
                            options: msConfStateFilterOptions
                        },
                        dataIndex: 'msConfigState',
                        width: 100,
                        renderer : function(value, metadata, record) {
                            if (value=="C"){
                                metadata.tdCls = 'msMgrConfigStateYesStyle';
                            }else if (value=="NA"){
                               metadata.tdCls = 'msMgrConfigStateNAStyle';
                            }else{
                                metadata.tdCls = 'msMgrConfigStateNoStyle';
                            }
                            var index = msConfStateStore.find('TValue',value);
                            if(index!=-1){
                                return msConfStateStore.getAt(index).data.TSDesc;
                            }
                            return record.get('msZGFlag');
                        }
                    },{
                        text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.msJoinState","是否参加") ,
                        lockable:false,
                        filter: {
                            type: 'list',
                            options: msResJoinStateSortFilterOptions
                        },
                        dataIndex: 'msJoinState',
                        width: 100,
                        editor:{
                            xtype: 'combobox',
                            //emptyText:"请选择",
                            name: 'msJoinStaCb',
                            queryMode: 'remote',
                            valueField: 'TValue',
                            displayField: 'TSDesc',
                            editable: false,
                            store:msResJoinStateStore
                            //allowBlank:false
                        },
                        renderer : function(value, metadata, record) {
                            //alert("render"+value);
                            var index = msResJoinStateStore.find('TValue',value);
                            if(index!=-1){
                                return msResJoinStateStore.getAt(index).data.TSDesc;
                            }
                            return record.get('msJoinState');
                        }
                    },{
                        text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.msResult","面试结果") ,
                        lockable:false,
                        filter: {
                            type: 'list',
                            options:msResResultSortFilterOptions
                        },
                        dataIndex: 'msResult',
                        width: 100,
                        editor:{
                            xtype: 'combobox',
                            //emptyText:"请选择...",
                            name: 'msResultCb',
                            queryMode: 'remote',
                            valueField: 'TValue',
                            displayField: 'TSDesc',
                            editable: false,
                            store:msResResultStore
                        },
                        renderer : function(value, metadata, record) {
                            var index = msResResultStore.find('TValue',value);
                            if(index!=-1){
                                return msResResultStore.getAt(index).data.TSDesc;
                            }
                            return record.get('msResult');
                        }
                    },{
                        text: Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.msArrDemo","备注"),
                        lockable:false,
                        filter: {
                            type: 'string',
                            itemDefaults: {
                                emptyText: '搜索...'
                            }
                        },
                        dataIndex: 'msArrDemo',
                        width: 100,
                        editor:{
                            xtype:'textfield'
                            //allowBlank:false
                        }
                    },{
                        text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.sort","类别")  ,
                        sortable: true,
                        dataIndex: 'sort',
                        minWidth: 140,
                        flex:1,
                        filter: {
                            type: 'list',
                            options: stuGridColorSortFilterOptions
                        },
                        renderer:function(v){
                            if(v){
                                var rec = orgColorSortStore.find('TZ_COLOR_SORT_ID',v,0,true,true,false);
                                if(rec>-1){
                                    return "<div  class='x-colorpicker-field-swatch-inner' style='width:30px;height:50%;background-color: #"+orgColorSortStore.getAt(rec).get("TZ_COLOR_CODE")+"'></div><div style='margin-left:40px;'>"+orgColorSortStore.getAt(rec).get("TZ_COLOR_NAME")+"</div>";
                                }else{
                                    return "";
                                }
                            }
                        },
                        editor: {
                            xtype: 'combo',
                            queryMode:'local',
                            valueField: 'TZ_COLOR_SORT_ID',
                            displayField: 'TZ_COLOR_NAME',
                            triggerAction: 'all',
                            editable : false,
                            triggers:{
                                clear: {
                                    cls: 'x-form-clear-trigger',
                                    handler: function(field){
                                        field.setValue("");
                                    }
                                }
                            },
                            store:orgColorSortStore,
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
                                    var colorSortID =selList[0].raw.sort;
                                    if(colorSortID.length<=0){
                                        combo.setValue("");
                                    }
                                }
                            }
                        }
                    },{
                        text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.msArrCkFrontPubRes","查看前台公布内容"),
                        align: 'center',
                        groupable: false,
                        width: 150,
                        lockable:false,
                        renderer: function(v,m,record) {
                            var classId = record.get('classId');
                            var oprId = record.get('oprId');
                            var tzParams='?tzParams={ComID:"TZ_BMGL_BMBSH_COM",PageID:"TZ_BMGL_AUDIT_STD",OperateType:"HTML",comParams:{oprId:"'+oprId+'","classId":"' + classId +'"}}';
                            var url = Ext.tzGetGeneralURL() + tzParams;
                            $('.viewFrontCon').fancybox();
                            return "<a href='"+url+"' class='viewFrontCon fancybox.ajax'>查看</a>";
                        }
                    }];
                var clsbmlcbz=responseData['clsbmlcbz']
                for(var i=0;i<clsbmlcbz.length;i++){
                    var dataIndexId = clsbmlcbz[i]['strBMLCBZAId']+"#"+clsbmlcbz[i]['strBMLCBZName'];
                    msResGridColumns.push({
                        text:clsbmlcbz[i]['strBMLCBZName'] ,
                        dataIndex: dataIndexId,
                        lockable:false,
                        width: 150,
                        editable:false,
                        renderer:function(value){
                            var msResBMLCColorDes=value.split(",");
                            if(value!=""){
                                return "<div class='x-colorpicker-field-swatch-inner' style='width:20%;height:50%;background-color: #"+msResBMLCColorDes[0]+"'></div><div style='width:70%;margin-left:auto;'>"+msResBMLCColorDes[1]+"</div>";
                            }
                        }
                    });
                }
                var msResGridName="interviewMgrGrid"+classID;
                var msResGridHeight=panel.getHeight()-44-100-8;
                var msResGrid = Ext.create("Ext.grid.Panel",{
                    height: msResGridHeight,
                    frame: true,
                    name: msResGridName,
                    reference: 'interviewMgrGrid',
                    viewConfig : {
                        enableTextSelection:true
                    },

                    store:msResGridStore,
                    dockedItems:[{
                        xtype:"toolbar",
                        items:[{
                                text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.tbarClearFilters","清除筛选条件"),
                                tooltip:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.tbarClearFiltersTip","清除筛选条件"),
                                iconCls:"reset",
                                handler:'onClearFilters',
                                disabled:true
                            },"-",
                            {
                                text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.tbarExport","导出面试安排excel表"),
                                tooltip:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.tbarExportTip","导出面试安排excel表"),
                                iconCls:"",
                                handler:"msResExport"
                            },"-",
                            {
                                text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.tbarImport","导入面试结果"),
                                tooltip:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.tbarImportTip","导入面试结果"),
                                iconCls:"",
                                handler:'impItwRes'
                            },
                            "-",
                            {
                                text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.tbarPubRes","面试结果公布"),
                                tooltip:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.tbarPubResTip","面试结果公布"),
                                iconCls:"",
                                handler:'pubItwResPl'
                             },
                            '->',
                            {
                                xtype:'splitbutton',
                                text:'更多操作',
                                iconCls:  'list',
                                glyph: 61,
                                menu:[
                                    {
                                        text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.tbarSendEmail","发送面试结果邮件"),
                                        handler:'sendEmail'
                                    },{
                                        text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.tbarViewAllStu","考生面试情况一览表"),
                                        handler:'msArrPreview'
                                    }
                                ]
                            }
                        ]
                    }],
                    columnLines: true,    //显示纵向表格线
                    selModel:{
                        type: 'checkboxmodel'
                    },
                    plugins:[             //可编辑单元格
                        //Ext.create('Ext.grid.plugin.CellEditing', {
                        //    clicksToEdit: 1   //单击进行编辑
                        //})
                        {
                            ptype: 'gridfilters'
                        },
                        {
                            ptype: 'cellediting',
                            clicksToEdit: 1
                        },
                        {
                            ptype: 'gridexporter'
                        }
                    ],
                    columns: msResGridColumns
                });
                //锁定列数
                var msResGridLockedColCou=0;
                for(var i=0;i<msResGrid.columns.length;i++){
                    if(msResGrid.columns[i].locked){
                        msResGridLockedColCou++;
                    }
                }
                msResGrid.on("cellclick",function( table,td, cellIndex, record, tr, rowIndex, e, eOpts ){
                    if(cellIndex>10){
                        var lciddataindexarr = msResGrid.columns[cellIndex+msResGridLockedColCou].dataIndex.split("#");
                        var lciddataindex=lciddataindexarr[0];
                        var classId = msResGrid.store.getAt(rowIndex).data.classId;
                        var appId = msResGrid.store.getAt(rowIndex).data.appId;
                        var bmlcId = lciddataindex;
                        Ext.tzSetCompResourses("TZ_BMGL_LCJGPUB_COM");
                        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_LCJGPUB_COM"]["TZ_PER_LCJGPUB_STD"];

                        if( pageResSet == "" || pageResSet == undefined){
                            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
                            return;
                        }
                        var className = pageResSet["jsClassName"];
                        if(className == "" || className == undefined){
                            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，请检查配置。');
                            return;
                        }

                        var win = this.lookupReference('perLcjgPubWindow');
                        if (!win) {
                            Ext.syncRequire(className);
                            ViewClass = Ext.ClassManager.get(className);
                            win = new ViewClass({
                                //perLcjgCallBack:me.testLqlc2
                                perLcjgCallBack:function(){
                                    //Ext.MessageBox.alert('测试',"这是一个回调函数");
                                    //me.msResPubGridRefresh(grid);
                                    msResGrid.store.reload();
                                }
                            });
                            var record = grid.store.getAt(rowIndex);
                            //alert(record+"__"+classId+"__"+bmlcId+"__"+appId);
                            var classID = classId;
                            var bmlc_id = bmlcId;
                            var bmb_id = appId;
                            var form = win.child('form').getForm();
                            var lm_mbStore = new KitchenSink.view.common.store.comboxStore({
                                recname: 'TZ_CLS_BMLCHF_T',
                                condition:{
                                    TZ_CLASS_ID:{
                                        value:classID,
                                        operator:"01",
                                        type:"01"
                                    },
                                    TZ_APPPRO_ID:{
                                        value:bmlc_id,
                                        operator:"01",
                                        type:"01"
                                    }
                                },
                                result:'TZ_APPPRO_HF_BH,TZ_CLS_RESULT'
                            });
                            form.findField("jg_id").setStore(lm_mbStore);
                            win.on('afterrender',function(panel){
                                var tzParams = '{"ComID":"TZ_BMGL_LCJGPUB_COM","PageID":"TZ_PER_LCJGPUB_STD","OperateType":"QF","comParams":{"bj_id":"'+classID+'","bmlc_id":"'+bmlc_id+'","bmb_id":"'+bmb_id+'"}}';
                                //"callback"'+this.testLqlc2()+'"
                                Ext.tzLoad(tzParams,function(responseData){
                                    var formData = responseData.formData;
                                    form.setValues(formData);
                                });
                            });
                            //this.getView().add(win);
                        }
                        win.show();
                    }
                })

                cmp.add(msResGrid);

                /*
                 Params= '{"cfgSrhId":"TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.TZ_MS_ITWMGRL_V","condition":{"TZ_CLASS_ID-operator":"01","TZ_CLASS_ID-value":"'+classID+'","TZ_BATCH_ID-operator":"01","TZ_BATCH_ID-value":"'+ batchID+'"}}';
                 panelgrid.store.tzStoreParams = Params;
                 panelgrid.store.reload();
                 */
                var Params= '{"classID":"'+classID+'","batchID":"'+batchID+'"}';
                var panelgrid = panel.down('grid');
                panelgrid.store.tzStoreParams = Params;
                //panelgrid.store.reload();

                msArrStateStore.load({
                    callback: function(records, options, success){
                        msConfStateStore.load({
                            callback: function(records, options, success){
                                panelgrid.store.reload();
                            }
                        });
                    }
                 });

            });
        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        };

        batchWindow.hide();
    },

    //查询
    queryClassInfo:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_MS_MGR_COM.TZ_MS_MGR_CLS_STD.TZ_CLASS_OPR_V',
            condition:{
                TZ_DLZH_ID:TranzvisionMeikecityAdvanced.Boot.loginUserId
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
	//关闭窗口
	onWindowClose: function(btn){
		var win = btn.findParentByType("window");
		win.close();
	}
});
