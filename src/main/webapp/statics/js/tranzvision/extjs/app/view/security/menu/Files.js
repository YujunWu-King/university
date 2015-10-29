Ext.define('KitchenSink.view.security.menu.Files', {
    extend: 'Ext.data.TreeStore',
    alias: 'store.filesTreeStore',
   // proxy: {
	//		type: 'ajax',
		//	url:  '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_BJXZQ.TZ_BJXZQ.FieldFormula.IScript_test', 
	//		reader: {
	//			type: 'json',
	//			rootProperty: 'root'
	//		}
	//	}
  //   autoLoad: true,
    root: {
        text: 'Ext JS',
        expanded: true,
        children: [
            {
                text: 'MBA中心',
                expanded: true,
                children: [
                    {
                        text: '菜单A',
                        expanded: true,
                        children: [
                            { leaf:true, text: '菜单A1' },
                            { leaf:true, text: '菜单A2' }
                        ]
                   },
                    {
                        text: '菜单B',
                        expanded: true,
                        children: [
                            { leaf:true, text: '菜单B1' },
                            { leaf:true, text: '菜单B2' },
                            { leaf:true, text: '菜单B3' }
                        ]
                    },
                   { leaf:true, text: '菜单C' },
                    { leaf:true, text: '菜单D' },
                    { leaf:true, text: '菜单E' }
                ]
            }
        ]
    }
});
