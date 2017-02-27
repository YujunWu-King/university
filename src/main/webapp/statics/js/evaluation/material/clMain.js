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
		window.myPageSlider[0] = new tzPageSlider('tz_zlps_container',1060,500,3);
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
	
	$("#tz_zlps_pclb").width(1060);
	//$("#tz_zlps_pclb").height(480);
	
	$("#tz_zlps_zym").width(1060);
	//$("#tz_zlps_zym").height(480);
	
	$("#tz_zlps_dfym").width(1060);
	//$("#tz_zlps_dfym").height(480);
	
	if(Ext.isIE == true)
	{
		$("#tz_zlps_container").height(480);
		$("#tz_zlps_pclb").height(480);
		$("#tz_zlps_zym").height(480);
		$("#tz_zlps_dfym").height(480);
	}
	
	
	$("#tz_zlps_loading").width($("#tz_zlps_pclb").width());
	$("#tz_zlps_loading").height($("#tz_zlps_pclb").height());
	
	//加载当前登录评委的评审批次数据
	window.setTimeout (function()
							{
								loadEvaluateBatchData(initializeExtObjects);
							},
							100
						);
}


//显示、隐藏窗体的蒙板层
var WindowMaskAll;
function maskWindow(){

	var setMaskHeight = $("div[class='header_outer_width']").height();
	var setMaskWidth = $("div[class='header_outer_width']").width();
	
	if(setMaskHeight<document.body.clientHeight) setMaskHeight = document.body.clientHeight;
	if(setMaskWidth<document.body.clientWidth) setMaskWidth = document.body.clientWidth;
	
	if(WindowMaskAll == null || WindowMaskAll == '' || WindowMaskAll == 'undefined')
	{
		WindowMaskAll = new Ext.LoadMask(document.body, {msg:"数据交互中，请稍等..."});
		
		WindowMaskAll.show();
		$("div[class='x-mask']").height(setMaskHeight).width(setMaskWidth);
	}
	else
	{
		WindowMaskAll.show();
		$("div[class='x-mask']").height(setMaskHeight).width(setMaskWidth);
	}
}


function unmaskWindow(){
	WindowMaskAll.hide();
}


