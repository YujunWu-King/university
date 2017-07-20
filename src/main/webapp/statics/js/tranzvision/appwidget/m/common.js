$(document).ready(function(){


      // 切换
     //tab(".bg .swiper-wrapper ",".bg .tabNote","active","mousedown");
     //tab(".bg  .slide_ul   ",".bg .tabNote","","mousedown");
     var swiper = new Swiper('.swiper-container', {
        pagination: '.swiper-pagination',
        slidesPerView: 'auto',
        paginationClickable: true,
        spaceBetween:20
    });

    //返回顶部
        var offset = 300,
        offset_opacity = 800,
        scroll_top_duration = 700,
        $back_to_top = $('.cd-top');

    $(window).scroll(function(){
        ( $(this).scrollTop() > offset ) ? $back_to_top.addClass('cd-is-visible') : $back_to_top.removeClass('cd-is-visible cd-fade-out');
        if( $(this).scrollTop() > offset_opacity ) { 
            $back_to_top.addClass('cd-fade-out');
        }
    });

    $back_to_top.on('click', function(event){
        event.preventDefault();
        $('body,html').animate({
            scrollTop: 0 ,
            }, scroll_top_duration
        );
    });
   
    //顶部右侧显示隐藏
    $(".drop").click(function(){
       $(".slide_nav").show();
    });
     $(".nav_tit i").click(function(){
       $(".slide_nav").hide();
    });
      $(".slide_ul li").click(function(){
       $(".slide_nav").hide();
    });
    //顶部滑动
    $(".slide_ul li").click(function(){
    	//console.log("111");
        swiper.slideTo($(this).attr("index"), 1000, false)
        $(this).parents("").prev().children().find(".swiper-wrapper li").removeClass("active");
        $(".swiper-wrapper li").eq($(this).attr("index")).addClass("active");
        $(".slide_nav").hide();
    });
   

    //time
    var calendar = new LCalendar();
    calendar.init({
        'trigger': '#demo1', //标签id
        'type': 'date', //date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择,
        'minDate': '1900-1-1', //最小日期
        'maxDate': new Date().getFullYear() + '-' + (new Date().getMonth() + 1) + '-' + new Date().getDate() //最大日期
    });
     var calendarym = new LCalendar();
    calendarym.init({
        'trigger': '#demo2',
        'type': 'ym',
        'minDate': '1900-1',
        'maxDate': new Date().getFullYear() + '-' + (new Date().getMonth() + 1)
    });
     var calendarym = new LCalendar();
    calendarym.init({
        'trigger': '#demo3',
        'type': 'ym',
        'minDate': '1900-1',
        'maxDate': new Date().getFullYear() + '-' + (new Date().getMonth() + 1)
    });
     var calendarym = new LCalendar();
    calendarym.init({
        'trigger': '#demo4',
        'type': 'ym',
        'minDate': '1900-1',
        'maxDate': new Date().getFullYear() + '-' + (new Date().getMonth() + 1)
    });
     var calendarym = new LCalendar();
    calendarym.init({
        'trigger': '#demo5',
        'type': 'ym',
        'minDate': '1900-1',
        'maxDate': new Date().getFullYear() + '-' + (new Date().getMonth() + 1)
    });
     var calendarym = new LCalendar();
    calendarym.init({
        'trigger': '#demo6',
        'type': 'ym',
        'minDate': '1900-1',
        'maxDate': new Date().getFullYear() + '-' + (new Date().getMonth() + 1)
    });
     var calendarym = new LCalendar();
    calendarym.init({
        'trigger': '#demo7',
        'type': 'ym',
        'minDate': '1900-1',
        'maxDate': new Date().getFullYear() + '-' + (new Date().getMonth() + 1)
    });
    var calendarym = new LCalendar();
    calendarym.init({
        'trigger': '#demo8',
        'type': 'ym',
        'minDate': '1900-1',
        'maxDate': new Date().getFullYear() + '-' + (new Date().getMonth() + 1)
    });
     var calendarym = new LCalendar();
    calendarym.init({
        'trigger': '#demo9',
        'type': 'ym',
        'minDate': '1900-1',
        'maxDate': new Date().getFullYear() + '-' + (new Date().getMonth() + 1)
    });
    






    
});
 $(window).load(function(){
 
 
});

$(window).resize(function(){
 
  
});


function tab(nav,content,on,type)
      {
        $(nav).children().bind(type,(function(){
          var $tab=$(this);
          var tab_index=$tab.prevAll().length;
          var $content = $(content).children();
          $(nav).children().removeClass(on);
          $tab.addClass(on);
          $content.hide();
          $content.eq(tab_index).show();
        }));
      }



