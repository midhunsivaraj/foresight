
$(document).ready(function () {



	 $('.co-icon').click(function(){
        	$("i", this).toggleClass("fas fa-minus fas fa-plus");
        });



                $('#dropdown').click(function(){
           $('.custom-dropdown').toggle(300);
        });
                

             $('#forward').click(function(){
           $('.step-two, .step-one').toggleClass('hide');
           $('#h-2').html('counter offer');
        });

var current_step;
var next_step;

$(".next-step").click(function(){
current_step = $(this).parent();
next_step = $(this).parent().next();
$("#progressbar li").eq($("fieldset").index(next_step)).addClass("active");
$("#progressbar li").eq($("fieldset").index(current_step)).removeClass("active");
next_step.show();
current_step.hide();
});


$('.tabs').tabslet({
	active: 3
});





       

});

$(document).ready(function () {
	const ps = new PerfectScrollbar('#container');
});