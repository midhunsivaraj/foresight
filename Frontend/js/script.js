
"use strict";
$(document).ready(function () {
$('.co-icon').click(function(){
$("i", this).toggleClass("fas fa-minus fas fa-plus");
});

 // $("#select-imi a").click(function(){

 //      $(".btn:first-child").text($(this).text());
 //      $(".btn:first-child").val($(this).text());

 //   });

$('#dropdown').click(function(){
$('.custom-dropdown').toggle(300);
});


$('#mob-trigger').click(function(){
$('.dash-side').toggleClass('slide-right');
});
  
                
$('#forward').click(function(){
$('.step-two, .step-one').toggleClass('hide');
$('#h-2').html('counter offer');
});
});

"use strict";
$(document).ready(function () {
// step-multi
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
});
// end

 
//         function _(el) {
//   return document.getElementById(el);
// }

// function uploadFile() {
//   var file = _("file1").files[0];
//   // alert(file.name+" | "+file.size+" | "+file.type);
//   var formdata = new FormData();
//   formdata.append("file1", file);
//   var ajax = new XMLHttpRequest();
//   ajax.upload.addEventListener("progress", progressHandler, false);
//   ajax.addEventListener("load", completeHandler, false);
//   ajax.addEventListener("error", errorHandler, false);
//   ajax.addEventListener("abort", abortHandler, false);
//   ajax.open("POST", "file_upload_parser.php"); // http://www.developphp.com/video/JavaScript/File-Upload-Progress-Bar-Meter-Tutorial-Ajax-PHP
//   //use file_upload_parser.php from above url
//   ajax.send(formdata);
// }

// function progressHandler(event) {
//   _("loaded_n_total").innerHTML = "Uploaded " + event.loaded + " bytes of " + event.total;
//   var percent = (event.loaded / event.total) * 100;
//   _("progressBar").value = Math.round(percent);
//   _("status").innerHTML = Math.round(percent) + "% uploaded... please wait";
// }

// function completeHandler(event) {
//   _("status").innerHTML = event.target.responseText;
//   _("progressBar").value = 0; //wil clear progress bar after successful upload
// }

// function errorHandler(event) {
//   _("status").innerHTML = "Upload Failed";
// }

// function abortHandler(event) {
//   _("status").innerHTML = "Upload Aborted";
// }
  "use strict";
$(document).ready(function () {
$('#tabs-one').tabslet({
	active: 3
});

$('#tabs-second').tabslet({
	active: 2
});

$('#tabs-three').tabslet({
  active: 1
});
$('.small-tab').tabslet({
  active: 1
});
});

"use strict";
$(document).ready(function () {
var ctx = document.getElementById('myChart');
var chart = new Chart(ctx, {
    // The type of chart we want to create
    type: 'line',

    // The data for our dataset
    data: {
        labels: ["January", "February", "March", "April", "May", "June","July"],
        datasets: [{
            label: "My First dataset",
            backgroundColor: 'rgb(61, 59, 238)',
            borderColor: '#03028d',
            data: [0, 600, 350, 200, 900, 500, 1250],
        }]
    },

    // Configuration options go here
    options: {
    	responsive: true,
    	legend:{
           display:false
    },
scales: {
    xAxes: [{
                gridLines : {
                    display : false,
                    tickMarkLength: 20
                }
            }],
    yAxes: [{
                gridLines: {
                    display:false
                },
                ticks: {
                  min: 0,
                  max: 1500,
                  stepSize: 250
              }   
            }]
    }
    }
});
// // "myAwesomeDropzone" is the camelized version of the HTML element's ID
// var dropzone = new Dropzone('#demo-upload', {
//  previewTemplate: document.querySelector('#preview-template').innerHTML,
//   parallelUploads: 2,
//   thumbnailHeight: 120,
//   thumbnailWidth: 120,
//   maxFilesize: 3,
//   filesizeBase: 1000,
//   acceptedFiles: ".pdf"
// });


});
"use strict";
$(document).ready(function () {
const ps = new PerfectScrollbar('#container');
});

