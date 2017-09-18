Ext.define('KitchenSink.view.stuCertificateManagement.stuCertOprLogManagement.stuCertOprLogList', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.stuCertificateManagement.stuCertOprLogManagement.stuCertOprLogController',
        'KitchenSink.view.stuCertificateManagement.stuCertOprLogManagement.stuCertOprLogModel',
        'KitchenSink.view.stuCertificateManagement.stuCertOprLogManagement.stuCertOprLogStore'
    ],
    xtype: 'stuCertOprLog',
    controller: 'stuCertOprLogController',
    store: {
        type: 'stuCertOprLogStore'
    },
    columnLines: true,//是否显示列分割线，默认为false
    selModel: {
        type: 'rowmodel'
    },
    style: "margin:8px",
    multiSelect: true,
    title: '证书日志查询',
    viewConfig: {
        enableTextSelection: true //可以复制单元格文字
    },
    header: false,//表头文字
    frame: true,
    dockedItems: [{
        xtype: "toolbar",
        dock: "bottom",
        ui: "footer",
        items: ['->',
            {minWidth: 80, text: "关闭", iconCls: "close", handler: "closeList"}
        ]
    }, {
        xtype: "toolbar",
        items: [
            {text: "查询", tooltip: "查询数据", iconCls: "query", handler: 'queryList'}
        ]
    }],
    initComponent: function () {
    	var store = new KitchenSink.view.stuCertificateManagement.stuCertOprLogManagement.stuCertOprLogStore();
//    	var cztype = new KitchenSink.view.common.store.appTransStore("TZ_CZ_TYPE");
//        cztype.load();
        Ext.apply(this, {
        	plugins: [
                      {
                          ptype: 'gridfilters',
//                          controller: 'stuCertOprLogController'
                      }
                  ],
            columns: [{
       			text : '',//序号
       			dataIndex : 'id',
       			xtype : 'rownumberer',
       			width : 50,
       			align : 'center'
           },{
       			text : '机构ID',//
       			dataIndex : 'jgId',
       			hidden : true
           },{ 
				text: '证书编号',
				dataIndex: 'certNo',
				width: 200
			},{
				text: '证书类型',
				dataIndex: 'certTypeName',
				minWidth: 5,
				flex: 1
			},{ 
				text: '姓名',
				dataIndex: 'name',
				width: 100
			},{ 
				text: '班级',
				dataIndex: 'className',
				width: 200
			},{
				text: '操作类型',
				dataIndex: 'operationType',
				width: 200,
				filter: {
                    type: 'list'
                },
                /*renderer : function(value, metadata, record) {
                	
                    var index = cztype.find('TValue',value);
                    if(index!=-1){
                        return cztype.getAt(index).data.TSDesc;
                    }
                    return record.get('');
                }*/
//				renderer:function(v){
//                    if(v=='CK'){
//                        return "证书查看";
//                    }else if(v=='ZF'){
//                    	return "证书转发";
//                    }else if(v=='YZ'){
//                    	return "验证证书";
//                    }else if(v=='RL'){
//                    	return "认领证书";
//                    }
//                    /*else if(v=='DX'){
//                    	return "吊销";
//                    }else if(v=='LI'){
//                    	return "LinkedIn";
//                    }else if(v=='BF'){
//                    	return "颁发";
//                    }*/
//                } 
			},/*{
				text: '操作人',
				dataIndex: 'operationer',
				width: 200
			},*/{
				text: '时间戳',
				dataIndex: 'timeStamp',
				width: 200
			},{
				text: '日期格式',
				dataIndex: 'timeStampDate',
				hidden: true
			}],
            store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                store: store,
                displayInfo: true,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });

        this.callParent();
    }
});
