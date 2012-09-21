<html>
<head>
  <title>Personal web</title>
  <script src='js-lib/jquery.min.js'></script>
  <script src='js-lib/jquery-ui.min.js'></script>
  <script src="js-lib/ember-0.9.8.1.js"></script>
  <script src="simple.js"></script>

  <script>
//    $(document).ready(poll);
      $(document).ready(function() {
        $("#datepicker").datepicker({
              showOn: "button",
//              buttonText: "Select"
//              buttonImage: "images/calendar.gif",
//              buttonImageOnly: true
         });
         });
  </script>
</head>

<body>
<a id="click" href="#">Click me</a>

<div class="demo">

<p>Date: <input type="hidden" id="datepicker"></p>

</div><!-- End demo -->

<p>

<div id="content">
</div>


</body>

</html>
