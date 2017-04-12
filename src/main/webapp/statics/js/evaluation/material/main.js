function loadExtClass()
{
	Ext.require([
		'Ext.grid.*',
		'Ext.data.*',
		'Ext.util.*',
		'Ext.state.*',
		'Ext.chart.*',
		'Ext.fx.target.Sprite',
		'Ext.layout.container.Fit',
		'Ext.LoadMask',
		'Ext.window.MessageBox'
	]);
}


function tzEvaluateObject()
{
	this.baokaoDirectionID = '';
	this.baokaoDirectionName = '';
	this.baokaoYear = '';
	this.baokaoBatch = '';
	this.baokaoZhiyuan = '';
	this.applicantName = '';
	this.applicantInterviewID = '';
	this.applicantBaomingbiaoID = '';
}


function tzPageSlider(divElem,divWidth,speed,count)
{
	this.divElem = $('#'+ divElem);
	this.divWidth = divWidth;
	this.slideSpeed = speed;
	this.pageCount = count;
	this.num = 0;
	this.pageIndex = 0;
	this.divPage = $("div[tztype='page']");
	this.autoScrollHtmlTagId = "";
	
	this.divElem.height(this.divPage.eq(this.pageIndex).height());
	
	
	if(Ext.isIE == false)
	{
		this.divElem.width(this.divWidth);
	}
}


tzPageSlider.prototype =
{
	leftAnimate:function(animateObject,leftCheckPosition,animateTime)
	{
		var currentLeftOffset = 0;
		
		if(window.myPageSlider[0].divElem[0].style.left != 'undefined' && window.myPageSlider[0].divElem[0].style.left != '' && window.myPageSlider[0].divElem[0].style.left != null)
		{
			var tmpLeftPosition = window.myPageSlider[0].divElem[0].style.left;
			
			tmpLeftPosition = tmpLeftPosition.replace(/px/g,'');
			tmpLeftPosition = tmpLeftPosition.replace(/pt/g,'');
			
			currentLeftOffset = Math.abs(1 * tmpLeftPosition);
		}
		if(Math.abs(leftCheckPosition - currentLeftOffset) <= 5)
		{
			window.myPageSlider[0].divElem[0].style.left = leftPosition;
			
			
			if(Ext.isIE == false)
		  {
		  	window.setTimeout(function()
		  	{
		  		var count = 0;
		      
		  		for(var i=0;i<window.myPageSlider[0].divPage.length;i++)
		  		{
		  			if(i > window.myPageSlider[0].pageIndex)
			      	{
			        	window.myPageSlider[0].divPage.eq(i).hide();
			        }
			        else
			        {
			        	count ++;
			        }
		  		}
					
			      window.myPageSlider[0].divElem.width(count * window.myPageSlider[0].divWidth);
			      window.myPageSlider[0].divElem[0].style.left = -1 * window.myPageSlider[0].pageIndex * window.myPageSlider[0].divWidth;
			},
			10);
		  }
		  else
		  {
		  	window.myPageSlider[0].divElem[0].style.left = -1 * window.myPageSlider[0].pageIndex * window.myPageSlider[0].divWidth;
		  }
			return;
		}
		
		
		animateObject.animate({left:"-=" + (Math.abs(leftCheckPosition - currentLeftOffset) / 2) + "px"},animateTime / 2,'swing',
				function(){
					window.myPageSlider[0].leftAnimate(animateObject,leftCheckPosition,animateTime / 2);
				}
		);
	},
	
	leftScroll:function(tipCount,scrollBackTagId)
	{
		if(this.pageIndex == 1 && (scrollBackTagId == '' || scrollBackTagId == null || scrollBackTagId == 'undefined'))
		{
			return;
		}
		
		if(tipCount <= 0 || tipCount > this.pageCount) return;
		
		if(this.num >= this.divWidth * (this.pageCount - 1)) return;
		
		
		if(Ext.isIE == false)
		{
			this.divElem.width(this.divWidth * this.pageCount);
			this.divPage.show();
		}
		
		
		this.pageIndex += tipCount;
		this.divElem.height(this.divPage.eq(this.pageIndex).height());
		
		
		$('html,body').scrollTop(0);
		if(scrollBackTagId != null && scrollBackTagId != '' && scrollBackTagId != 'undefined')
		{
			this.autoScrollHtmlTagId = scrollBackTagId;
		}
		
		this.num += this.divWidth * tipCount;
		
		this.divElem.stop().animate({left:"-=" + this.divWidth * tipCount + "px"},
		                     this.slideSpeed,
		                     'swing',
		                     function(){
             							if(Ext.isIE == false)
             							{
             						 		window.setTimeout(function()
				 												{
				 													var count = 0;
				 													
				 													for(var i=0;i<window.myPageSlider[0].divPage.length;i++)
				 													{
				 														if(i > window.myPageSlider[0].pageIndex)
				 														{
				 															window.myPageSlider[0].divPage.eq(i).hide();
				 														}
				 														else
				 														{
				 															count ++;
				 														}
				 													}
				 													
				 													window.myPageSlider[0].divElem.width(count * window.myPageSlider[0].divWidth);
				 													
				 													
				 													var leftPosition = -1 * window.myPageSlider[0].pageIndex * window.myPageSlider[0].divWidth;
				 													window.myPageSlider[0].divElem[0].style.left = leftPosition;
				 												}
		                     					 				,10
		                     				);
             					 		}
             					 		else
             					 		{
             					 			var leftPosition = -1 * window.myPageSlider[0].pageIndex * window.myPageSlider[0].divWidth;
             					 			window.myPageSlider[0].divElem[0].style.left = leftPosition;
             					 		}
		                     		}
		                    );
	},
	
	rightAnimate:function(animateObject,leftCheckPosition,animateTime)
	{
		var currentLeftOffset = 0;
		
		if(window.myPageSlider[0].divElem[0].style.left != 'undefined')
		{
			currentLeftOffset = window.myPageSlider[0].divElem[0].style.left;
		}
		if(Math.abs(leftCheckPosition - currentLeftOffset) <= 5)
		{
			window.myPageSlider[0].divElem[0].style.left = leftPosition;
			
			
			if(Ext.isIE == false)
			{
				window.setTimeout(function()
						{
							var count = 0;
		      
							for(var i=0;i<window.myPageSlider[0].divPage.length;i++)
						    {
						      	if(i > window.myPageSlider[0].pageIndex)
						      	{
						        	window.myPageSlider[0].divPage.eq(i).hide();
						        }
						        else
						        {
						        	count ++;
						        }
						     }
					
							window.myPageSlider[0].divElem.width(count * window.myPageSlider[0].divWidth);
						},
						10);
		  } 
			return;
		}
		
		
		animateObject.stop().animate({left:"+=" + ((leftCheckPosition - currentLeftOffset) / 2) + "px"},animateTime / 2,'swing',
				function(){
							window.myPageSlider[0].leftAnimate(animateObject,leftCheckPosition,animateTime / 2);
						  }
		);
	},
	
	rightScroll:function(tipCount,autoScroll)
	{
		if(tipCount <= 0 || tipCount > this.pageCount ) return;
		
		if(this.num <= 0) return;
		
		
		if(Ext.isIE == false)
		{
			this.divElem.width(this.divWidth * this.pageCount);
			this.divPage.show();
		}
		
		
		this.pageIndex -= tipCount;
		this.divElem.height(this.divPage.eq(this.pageIndex).height());
		
		
		if(autoScroll == true)
		{
			$('html,body').scrollTop(0);
		}
		else
		{
			if(this.autoScrollHtmlTagId != null && this.autoScrollHtmlTagId != '' && this.autoScrollHtmlTagId != 'undefined')
			{
				var tmpHtmlObject = $('#' + this.autoScrollHtmlTagId);
				
				if(tmpHtmlObject != null && $(window)[0] != null && this.pageIndex == 1)
				{
					$(window)[0].scrollTo(tmpHtmlObject.offset().left,tmpHtmlObject.offset().top);
				}
				
				this.autoScrollHtmlTagId = "";
			}
		}
		
		this.num -= this.divWidth * tipCount;

		this.divElem.stop().animate({left:"+=" + this.divWidth * tipCount + "px"},
		                     this.slideSpeed,
		                     'swing',
		                     function(){
             							if(Ext.isIE == false)
             							{
             						 		window.setTimeout(function()
				 												{
				 													var count = 0;
				 													
				 													for(var i=0;i<window.myPageSlider[0].divPage.length;i++)
				 													{
				 														if(i > window.myPageSlider[0].pageIndex)
				 														{
				 															window.myPageSlider[0].divPage.eq(i).hide();
				 														}
				 														else
				 														{
				 															count ++;
				 														}
				 													}
				 													
				 													window.myPageSlider[0].divElem.width(count * window.myPageSlider[0].divWidth);
				 													
				 													
				 													var leftPosition = -1 * window.myPageSlider[0].pageIndex * window.myPageSlider[0].divWidth;
				 													window.myPageSlider[0].divElem[0].style.left = leftPosition;
				 												}
				 												,10
		                     					 			);
		                     				}
             					 		else
             					 		{
             					 			var leftPosition = -1 * window.myPageSlider[0].pageIndex * window.myPageSlider[0].divWidth;
             					 			window.myPageSlider[0].divElem[0].style.left = leftPosition;
             					 		}
		                     		}
		                    );
	},
	
	adjustHeight:function()
	{
		this.divElem.height(this.divPage.eq(this.pageIndex).height());
	},
	
	showAllDivpages:function()
	{
		this.divPage.show();
	},
	
	hideAllDivpages:function()
	{
		for(var i=0;i<this.divPage.length;i++)
		{
			if(i > this.pageIndex)
			{
				this.divPage.eq(i).hide();
			}
		}
	}
}


function initializeTzPageSlider()
{
	if(window.myPageSlider.length == 0)
	{
		window.myPageSlider[0] = new tzPageSlider('tz_zlps_container',Ext.getBody().getWidth(),500,3);
	}
}


function initializeExtObjects(jsonObject)
{
	try
	{
		loadExtClass();
		initializeGridColumnHeaders();
		Ext.QuickTips.init();
		initializeEvaluatePiciGrid(jsonObject);
	
		$("#tz_zlps_loading").fadeOut(2000);
	
		window.setTimeout(initializeTzPageSlider,10);
	}
	catch(e1)
	{
		alert('资料评审初始化错误，请与系统管理员联系：[' + e1 + ']');
	}
}


//记录当前显示的页面与首页（即默认页）的层级深度，应用于点击导航栏的"首页"链接
var top_menu_home_deep = 0; 

function showNextEvaluatePage(tipCount,scrollBackTagId)
{
	if(top_menu_home_deep<2) top_menu_home_deep++;
	myPageSlider[0].leftScroll(tipCount,scrollBackTagId);
	
	//unmask window
	unmaskWindow();
}


function showPreviousEvaluatePage(tipCount,autoScroll)
{
	//alert(top_menu_home_deep);
	if(top_menu_home_deep>0) top_menu_home_deep--;
	myPageSlider[0].rightScroll(tipCount,autoScroll);
}


function topMenuHomePage(){
	showPreviousEvaluatePage(top_menu_home_deep);
	top_menu_home_deep = 0;
}


function onLeaveEstimateSystem()
{
	/*
	Ext.Msg.confirm('提示', '单击“是”重新加载刷新页面数据，单击“否”离开当前页面。', function(button)
	{
		if (button === 'yes')
		{
			//todo
			return false;
		}
	});
	*/
}


function initializeEvaluateDataObjects(urlObject)
{
	if(window.myPageSlider == null) window.myPageSlider = [];
	if(window.mainPageObjectArray == null) window.mainPageObjectArray = {'PreviousBatchId':'','DivObjectList':{},'PreviousBatchId2':'','ApplicantEvaluatePageList':{}};
	if(window.gridColumnHeaders_01 == null) window.gridColumnHeaders_01 = [];
	if(window.batchEvaluateMainPageObject == null) window.batchEvaluateMainPageObject = [];
	if(window.batchJSONArray == null) window.batchJSONArray = {};
	if(window.onbeforeunload == null) window.onbeforeunload = onLeaveEstimateSystem;
	
	
	window.getBatchListUrl = urlObject['getBatchListUrl'];
	window.getBatchDataUrl = urlObject['getBatchDataUrl'];
	window.getNextApplicantUrl = urlObject['getNextApplicantUrl'];
	window.getApplicantDataUrl = urlObject['getApplicantDataUrl'];
	window.submitApplicantDataUrl = urlObject['submitApplicantDataUrl'];
	window.printStatisticsTableUrl = urlObject['printStatisticsTableUrl'];
	window.scoreUrl = urlObject['scoreUrl'];
	window.evaluateSystemDebugFlag = 'Y';
	
	//library_main_evalute_page 的评审考生列表GRID对象，用于实现第二、三个页面考生 GRID 的自动HIGHLIGHT
	if(window.library_main_evalute_page_ks_grid == null)
	{
		window.library_main_evalute_page_ks_grid = {};
	}
	
}


function initializeEvaluateSystem(urlObject)
{
	initializeEvaluateDataObjects(urlObject);
	
	var width = Ext.getBody().getWidth();
	
	$("#tz_zlps_pclb").width(width);
	//$("#tz_zlps_pclb").height(480);
	
	$("#tz_zlps_zym").width(width);
	//$("#tz_zlps_zym").height(480);
	
	$("#tz_zlps_dfym").width(width);
	//$("#tz_zlps_dfym").height(480);
	
	if(Ext.isIE == true)
	{
		$("#tz_zlps_container").height(480);
		$("#tz_zlps_pclb").height(480);
		$("#tz_zlps_zym").height(480);
		$("#tz_zlps_dfym").height(480);
	}
	
	
	//$("#tz_zlps_loading").width($("#tz_zlps_pclb").width());
	//$("#tz_zlps_loading").height($("#tz_zlps_pclb").height());
	
	//加载当前登录评委的评审批次数据
	window.setTimeout (function(){loadEvaluateBatchData(initializeExtObjects);},100);
}


//显示、隐藏窗体的蒙板层
function maskWindow(msg){
	var maskMsg = msg!=undefined&&msg!=""?msg:"加载中，请稍候...";
	
	Ext.getBody().mask(maskMsg);
}


function unmaskWindow(){
	Ext.getBody().unmask();
}

function changePassword(){
	var win = Ext.modifyPwdWindow;
	if(!win){
		Ext.define('KitchenSink.view.common.modifyPwdWindow', {
		    extend: 'Ext.window.Window',
		    xtype: 'modifyPwdWindow', 
		    title: '修改密码', 
		    width: 500,
		    height: 200,
		    layout: 'fit',
		    resizable: false,
		    modal: true,
		    closeAction: 'hide',
			items: [{
				xtype: 'form',	
				layout: {
					type: 'vbox',
					align: 'stretch'
				},
				border: false,
				bodyPadding: 10,
				ignoreLabelWidth: true,
			
				fieldDefaults: {
					msgTarget: 'side',
					labelWidth: 120,
					fieldStyle:"margin-bottom:5px",
					labelStyle: 'font-size:13px;'
				},
				items: [{
					xtype: 'textfield',
					fieldLabel: "原密码",
					name: 'oldPwd',
					inputType: 'password',
					afterLabelTextTpl: [
						'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
					],
					allowBlank: false
				}, {
					xtype: 'textfield',
					fieldLabel: "新密码",
					name: 'newPwd',
					inputType: 'password',
					afterLabelTextTpl: [
						'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
					],
					allowBlank: false
				}, {
					xtype: 'textfield',
					fieldLabel: "确认新密码",
					name: 'comfirmPwd',
					inputType: 'password',
					afterLabelTextTpl: [
						'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
					],
					allowBlank: false
				}]
			}],
		    buttons: [
			{
				text: '确定',
				handler: function(btn){
					var bl = true;
					var win = btn.findParentByType("window");
					var form = win.child("form").getForm();
					if(!form.isValid()){
						bl =  false;
					}
					//表单数据
					var formParams = form.getValues();
					//密码
					if(formParams["newPwd"] != formParams["comfirmPwd"]){
						Ext.MessageBox.alert('提示', '两次密码输入不一致', this);
						bl =  false;
					}
					//新旧密码不能相同
					if(formParams["oldPwd"] == formParams["newPwd"]){
						Ext.MessageBox.alert('提示', '新旧密码不能相同', this);
						bl =  false;
					}
					if (bl == true){
					//提交参数
					var tzParams = '{"ComID":"TZ_GD_XGPWD_COM","PageID":"TZ_GD_XGPWD_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
					Ext.tzSubmit(tzParams,function(){
						var form = win.child("form").getForm();
						form.reset();
						win.close();
					},"密码修改成功！");
				}
				}
			}, {
				text: '关闭',
				handler: function(btn){
					btn.findParentByType("window").close();
				}
			}]
		});
		Ext.modifyPwdWindow = win = new KitchenSink.view.common.modifyPwdWindow();
	}
	win.show();
}

Ext.tzSubmit =  function(params,callback,msg)
{
    maskWindow();

    try
    {
        Ext.Ajax.request(
            {
                url: ContextPath+"/dispatcher",
                params:{tzParams: params},
                timeout: 60000,
                async: false,
                success: function(response, opts)
                {
                    //返回值内容
                    var jsonText = response.responseText;
                    try
                    {
                        var jsonObject = Ext.util.JSON.decode(jsonText);
                        /*判断服务器是否返回了正确的信息*/
                        if(jsonObject.state.errcode == 1){
                        	Ext.Msg.alert("提示",jsonObject.state.errdesc);
                        }else{
                        	typeof callback == "function"&&callback();
                        	Ext.Msg.alert("提示",msg||"保存成功！");
                        }
                    }
                    catch(e)
                    {
                        Ext.Msg.alert("提示","密码修改失败："+e.toString()+"，请与系统管理员联系。");
                    }
                },
                failure: function(response, opts)
                {
                	var respText = Ext.util.JSON.decode(response.responseText);
                	Ext.Msg.alert("提示","密码修改失败："+respText.error+"，请与系统管理员联系。");
                },
                callback: function(opts,success,response)
                {
                    unmaskWindow();
                }
            });
    }
    catch(e1)
    {
    	Ext.Msg.alert("提示","密码修改失败：请与系统管理员联系。");
    	unmaskWindow();
    }
} 