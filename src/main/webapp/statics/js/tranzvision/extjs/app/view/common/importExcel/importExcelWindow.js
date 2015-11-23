Ext.define('KitchenSink.view.common.importExcel.importExcelWindow', {
    extend: 'Ext.window.Window',
    requires: [
        'Ext.layout.container.Card'
    ],
    xtype: 'importExcelWindow',
    layout:'fit',
    ignoreChangesFlag: true,
    viewModel: {
        data: {
            title: '导入Excel'
        }
    },
    bind: {
        title: '{title}'
    },
    constructor: function (config) {
        this.importType=config.importType,//导入类型A：上传Excel；B：粘贴Excel数据
            this.businessHandler=config.businessHandler,//回调函数
            this.tplResId = config.tplResId,
            this.callParent();
    },
    modal:true,
    defaults: {
        border:false
    },
    defaultListenerScope: true,
    listeners:{
        resize: function(win){
            win.doLayout();
        },
		afterrender:function(){
			//处理在火狐下fieldset下的上传控件按钮宽度不够的问题
			var filebutton = this.down("#excelFile").getTrigger('filebutton');;
			filebutton.el.dom.style.width='65px';
		}
    },
    initComponent: function(){
        var me = this,
            uploadExcelCollapsedFlag = me.importType=="B",
            pasteExcelDataCollapsedFlag = !uploadExcelCollapsedFlag,
            columnWidths = me.tplResId!=undefined?[.8,.2]:[1,0],
            excelLinkHidden=me.tplResId!=undefined?false:true,
            excelTplUrl;

        if(!excelLinkHidden){
            var tzParams = '{"ComID":"TZ_IMPORT_EXCEL_COM","PageID":"TZ_IMP_EXCEL_STD","OperateType":"tzGetExcelTplUrl","comParams":{"tplResId":"'+me.tplResId+'"}}';
            Ext.tzLoadAsync(tzParams,function(respData){
                excelTplUrl = respData.url;
				if(excelTplUrl==undefined){
					columnWidths=[1,0];
					excelLinkHidden=true;
				}
            });
        };
        Ext.apply(this,{
            items:[
                {
                    xtype:'panel',
                    layout: 'card',
                    width: 800,
                    minHeight:200,
                    maxHeight:500,
                    bbar: [
                        {
                            xtype:'tbtext',
                            itemId: 'rowCount',
                            baseCls:Ext.baseCSSPrefix+'panel-header-title-light',
                            defaultTextMsg    : TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00028"),
                            hidden:true
                        },
                        '->',
                        {
                            itemId: 'card-prev',
                            iconCls:'prev',
                            text: '上一步',
                            handler: 'showPrevious',
                            disabled: true
                        },
                        {
                            itemId: 'card-next',
                            iconCls:'next',
                            text: '下一步',
                            handler: 'showNext'
                        },
                        {
                            text: '关闭',
                            iconCls:"close",
                            handler: function(btn){
                                btn.findParentByType('window').close();
                            }
                        }
                    ],
                    items: [

                        {title:'导入Excel',
                            header:false,
                            name:'importExcel',
                            bodyPadding:10,
                            xtype:'form',
                            itemId:'card-0',
                            items:[
                                {
                                    xtype:'form',
                                    name:'uploadExcelForm',
                                    items:[{
                                        xtype:'fieldset',
                                        title: '<span style="font-weight: bold;" class="themeColor">上传Excel文件</span>',
                                        name:'uploadExcel',
                                        collapsible:true,
                                        collapsed:uploadExcelCollapsedFlag,
                                        toggleOnTitleClick:true,
                                        checkboxToggle:true,
                                        listeners:{
                                            expand:function( fieldset, eOpts ){
                                                fieldset.findParentByType('importExcelWindow').down('fieldset[name=pasteExcelData]').collapse( );
                                            },
                                            beforecollapse:function( fieldset, eOpts ){
                                                var pasteExcelData=fieldset.findParentByType('importExcelWindow').down('fieldset[name=pasteExcelData]');
                                                if(pasteExcelData.collapsed)pasteExcelData.expand();
                                            }
                                        },
                                        defaults: {
                                            anchor: '100%',
                                            layout: {
                                                type: 'hbox',
                                                defaultMargins: {top: 0, right: 5, bottom: 0, left: 0}
                                            }
                                        },
                                        items:[
                                            {
                                                layout:'column',
                                                items:[
                                                    {
                                                        xtype: 'filefield',
                                                        name: 'excelFile',
                                                        itemId:'excelFile',
                                                        msgTarget: 'side',
                                                        allowBlank: false,
                                                        anchor: '100%',
                                                        buttonText: '浏览...',
                                                        columnWidth:columnWidths[0],
                                                        validator:function(value){
                                                            var excelReg = /\.([xX][lL][sS]){1}$|\.([xX][lL][sS][xX]){1}$/;
                                                            if(!excelReg.test(value)&&value){
                                                                Ext.Msg.alert('提示','文件类型错误,请选择 [xls,xlsx] 格式的Excel文件');
                                                                return '文件类型错误,请选择 [xls,xlsx] 格式的Excel文件';
                                                            }else{
                                                                return true
                                                            }
                                                        }
                                                    },
                                                    {
                                                        xtype:'toolbar',
                                                        name:'downloadExcelTpl',
                                                        hidden:excelLinkHidden,
                                                        padding:0,
                                                        style:'margin-left:20px',
                                                        columnWidth:columnWidths[1],
                                                        items:[
                                                            {
                                                                xtype:'button',
                                                                text:'<span class="themeColor" style="text-decoration: underline">下载Excel模板</span>',
                                                                cls:'themeColor',
                                                                border:false,
                                                                style:{
                                                                    background:'white'
                                                                },
                                                                href:excelTplUrl
                                                            }
                                                        ]
                                                    }
                                                ]
                                            }
                                            ,
                                            {
                                                xtype: 'checkboxfield',
                                                boxLabel: '首行是标题行',
                                                name: 'firstLineTitle_1',
                                                inputValue: 'Y'
                                            }
                                        ]
                                    }]
                                },
                                {
                                    xtype:'fieldset',
                                    title: '<span style="font-weight: bold;" class="themeColor">粘贴Excel数据</span>',
                                    name:'pasteExcelData',
                                    collapsible:true,
                                    toggleOnTitleClick:true,
                                    checkboxToggle:true,
                                    collapsed:pasteExcelDataCollapsedFlag,
                                    listeners:{
                                        expand:function( fieldset, eOpts ){
                                            fieldset.findParentByType('importExcelWindow').down('fieldset[name=uploadExcel]').collapse( );
                                        },
                                        beforecollapse:function( fieldset, eOpts ){
                                            var uploadExcel=fieldset.findParentByType('importExcelWindow').down('fieldset[name=uploadExcel]');
                                            if(uploadExcel.collapsed)uploadExcel.expand();
                                        }
                                    },
                                    defaults: {
                                        anchor: '100%',
                                        layout: {
                                            type: 'hbox',
                                            defaultMargins: {top: 0, right: 5, bottom: 0, left: 0}
                                        }
                                    },
                                    items:[
                                        {
                                            xtype: 'textarea',
                                            name: 'excelText',
                                            itemId:'excelText',
                                            msgTarget: 'side',
                                            grow      : true,
                                            growMax :150,
                                            anchor    : '100%',
                                            allowBlank: false
                                        },
                                        {
                                            xtype: 'checkboxfield',
                                            boxLabel: '首行是标题行',
                                            name: 'firstLineTitle_2',
                                            inputValue: 'Y'
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            title:'预览导入数据(仅预览20列*1000行以内的数据)',
                            header:false,
                            name:'previewExcelData',
                            itemId:'card-1',
                            xtype:'form',
                            ignoreLabelWidth: true
                        },
                        {
                            //此步骤为使用者自定义函数处理，该处不进行任何操作
                            title:'处理导入数据',
                            header:false,
                            name:'dealExcelData',
                            itemId:'card-2'
                        }
                    ]
                }
            ]
        });
        this.callParent()
    },
    showNext: function (btn) {
        this.doCardNavigation(1);
    },

    showPrevious: function (btn) {
        this.doCardNavigation(-1);
    },

    doCardNavigation: function (incr) {
        var me = this;
        var l = me.child('panel').getLayout();
        var i = l.activeItem.itemId.split('card-')[1];
        var next = parseInt(i, 10) + incr;
        var displayRowCount=me.down('#rowCount');

        /*第一步：导入或者粘贴Excel数据*/
        if( l.activeItem.name == 'previewExcelData'&&incr==-1){
            displayRowCount.setVisible(false);
        }

        /*第二步：解析并预览数据Excel*/
        if( l.activeItem.name == 'importExcel'){
            var uploadExcel =  l.activeItem.down('fieldset[name=uploadExcel]'),
                pasteExcelData = l.activeItem.down('fieldset[name=pasteExcelData]');


            /*预览数据columns 和 data*/
            var columns,
                data = [],
                dataArray = [],
                dataWithColumns = [],
                columnsLength;

            if(!uploadExcel.collapsed){
                //解析上传Excel文件
                var filename = me.down("#excelFile").getValue();
                var form = me.down('form[name=uploadExcelForm]').getForm();
                if(filename&&form.isValid()){
                    //var dateStr = Ext.Date.format(new Date(), 'Ymd');
                    var filePath = '/linkfile/FileUpLoad'
                    var updateUrl = TzUniversityContextPath + '/UpdServlet';

                    form.submit({
                        url: updateUrl,
                        waitMsg: '正在上传Excel...',
                        params: {
                            filePath: filePath
                        },
                        success: function (form, action) {
                            var sysFileName = action.result.msg.sysFileName;
                            var path = action.result.msg.path;
                            /*后台解析Excsel*/
                            Ext.MessageBox.show({
                                msg: '解析数据中，请稍候...',
                                progress: true,
                                progressText:'解析中...',
                                width: 300,
                                wait: {
                                    interval: 50
                                }
                            });

                            var tzParams = '{"ComID":"TZ_IMPORT_EXCEL_COM","PageID":"TZ_IMP_EXCEL_STD","OperateType":"tzAnalysisExcel","comParams":{"path":'+Ext.JSON.encode(path)+',"sysFileName":'+Ext.JSON.encode(sysFileName)+'}}';

                            Ext.tzLoad(tzParams,function(responseData){
                                if(responseData.error){
                                    Ext.Msg.alert("错误",responseData.error);
                                    return;
                                }
                                dataWithColumns = responseData;
                                var firstLineTitle_1 = me.down('checkboxfield[name=firstLineTitle_1]').getValue();
                                for(var i = 0;i<dataWithColumns.length;i++){
                                    if(dataArray.length==1000)break;/*超过1000行的数据不展示*/
                                    columnsLength = (columnsLength==undefined?dataWithColumns[i].length:((columnsLength<dataWithColumns[i].length)?dataWithColumns[i].length:columnsLength));

                                    if(i==0&&firstLineTitle_1/*首行为标题行*/){
                                        columns = dataWithColumns[i];
                                    }else{
                                        dataArray.push(dataWithColumns[i]);

                                        var jsonData = ""
                                        for(var j=0;j<dataWithColumns[i].length;j++){
                                            if(j==20)break;/*超过20行的数据不展示*/

                                            var encodeColumnData = Ext.JSON.encode(dataWithColumns[i][j].replace(/</g,'&lt').replace(/>/g,'&gt'));
                                            if(jsonData==""){
                                                jsonData ='column_'+j+':'+encodeColumnData;
                                            }else{
                                                jsonData =jsonData+',column_'+j+':'+encodeColumnData;
                                            }
                                        }
                                        data.push(Ext.JSON.decode("{"+jsonData+"}"));
                                    }
                                }
                                me.dataWithColumns=dataWithColumns;
                                me.dataArray=dataArray;
                                me.columnArray=(columns==undefined?[]:columns);
                                me.pieceGrid(me,columns,columnsLength,data);
                                displayRowCount.setText(Ext.String.format(displayRowCount.defaultTextMsg, firstLineTitle_1&&dataWithColumns.length==1?0:1,dataArray.length,firstLineTitle_1?dataWithColumns.length-1:dataWithColumns.length));

                                Ext.MessageBox.hide();
                            });

                        },
                        failure: function (form, action) {
                            Ext.MessageBox.alert("错误", action.result.msg);
                            return;
                        }
                    });
                }else
                {
                    Ext.MessageBox.show({
                        title: '提示',
                        msg: '未选择Excel文件!',
                        buttons: Ext.Msg.OK,
                        scope: this,
                        icon:  Ext.Msg.WARNING});
                    return;
                }
            }else{
                //解析粘贴Excel数据
                var excelText = me.down('#excelText').getValue();
                if(!excelText||excelText.replace(/(^\s*)|(\s*$)/g, "")==""){
                    Ext.MessageBox.show({
                        title: '提示',
                        msg: '请粘贴Excel数据!',
                        buttons: Ext.Msg.OK,
                        scope: this,
                        icon:  Ext.Msg.WARNING});
                    return;
                };
                var firstLineTitle_2 = me.down('checkboxfield[name=firstLineTitle_2]').getValue(),
                    columnsData = excelText.split("\n");//获取每行数据

                var displayCount,allCount;
                for(var i = 0;i<columnsData.length;i++){
                    if(columnsData[i].replace(/(^\s*)|(\s*$)/g, "")=="") continue;

                    //var columnData = columnsData[i].split(/\s+/);//以空格拆分每一列
                    var columnData = columnsData[i].split('\t');//以制表符拆分每一列
                    columnsLength = (columnsLength==undefined?columnData.length:((columnsLength<columnData.length)?columnData.length:columnsLength));

                    dataWithColumns.push(columnData);
                    if(i==0&&firstLineTitle_2/*首行为标题行*/){
                        columns = columnData;
                    }else{
                        dataArray.push(columnData);

                        if(dataArray.length<=1000){
                            var jsonData = ""
                            for(var j=0;j<columnData.length;j++){
                                var encodeColumnData = Ext.JSON.encode(columnData[j].replace(/</g,'&lt').replace(/>/g,'&gt'));
                                if(jsonData==""){
                                    jsonData ='column_'+j+':'+encodeColumnData;
                                }else{
                                    jsonData =jsonData+',column_'+j+':'+encodeColumnData;
                                }
                            }
                            data.push(Ext.JSON.decode("{"+jsonData+"}"));
                        }
                    }
                }
                me.dataWithColumns=dataWithColumns;
                me.dataArray=dataArray;
                me.columnArray=(columns==undefined?[]:columns);
                me.pieceGrid(me,columns,columnsLength,data);
                displayRowCount.setText(Ext.String.format(displayRowCount.defaultTextMsg, dataArray.length==0?0:1,dataArray.length,dataArray.length));
            }
            displayRowCount.setVisible(true);
        }

        /*第三步：用户自定义函数处理解析后的数据*/
        if( l.activeItem.name == 'previewExcelData'&&incr==1){
            if(me.businessHandler!=undefined)me.businessHandler(me.columnArray,me.dataArray);
            me.close();
            return;
        }
        l.setActiveItem(next);
        me.getViewModel().set('title', l.activeItem.title);
        me.down('#card-prev').setDisabled(next===0);
        me.down('#card-next').setDisabled(next===2);
    },
    pieceGrid:function(me,columns,columnsLength,data){
        var modelFields = new  Array();
        for(var i = 0;i<columnsLength;i++){
            modelFields.push('column_'+i);
        };

        var gridStore = new Ext.data.Store({
                model:new Ext.data.Model({fields: modelFields }),
                data:{root:data},
                autoLoad: true,
                proxy: {
                    type: 'memory',
                    reader: {
                        type: 'json',
                        rootProperty: 'root'
                    }
                }
            }),
            gridColumns = [];

        //拼装展示列
        gridColumns.push({
            xtype:'rownumberer'
        });
        if(columns!=undefined){
            for(var i = 0;i<columns.length;i++){
                var columnWidth = columns[i].toString().length*15+30;

                var gridColumn = new Ext.grid.Column({
                    header:'<span style="font-size:14px">'+columns[i].toString().replace(/</g,'&lt').replace(/>/g,'&gt')+'</span>',
                    minWidth:columnWidth>100?columnWidth:100,
                    flex:1,
                    sortable:false,
                    menuDisabled: true,
                    dataIndex:'column_'+i,
                    renderer:function(value){
                        value=(value==undefined?"":value);
                        return '<span style="font-size:14px;">' + value + '</span>';
                    }
                })
                gridColumns.push(gridColumn);
            }
        }else{
            for(var i = 0;i<columnsLength;i++){
                var gridColumn = new Ext.grid.Column({
                    header:'<span style="font-size:14px">'+'第'+(i+1)+'列'+'</span>',
                    minWidth:100,
                    flex:1,
                    sortable:false,
                    menuDisabled: true,
                    dataIndex:'column_'+i,
                    renderer:function(value){
                        value=(value==undefined?"":value);
                        return '<span style="font-size:14px;">' + value + '</span>';
                    }
                })
                gridColumns.push(gridColumn);
            }
        };

        var form = me.down('form[name=previewExcelData]');
        var grid = me.down('grid[name=previewExcelDataGrid]');
        if(grid!=undefined)form.remove(grid);
        grid={
            xtype:'grid',
            name : 'previewExcelDataGrid',
            width:'100%',
            minHeight:200,
            maxHeight:300,
            store:gridStore,
            columns:gridColumns
        };

        form.insert(0,grid);
    }
});