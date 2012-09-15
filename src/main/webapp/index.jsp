<html>
<head>
  <script src='js-lib/jquery.min.js'></script>
  <script src='js-lib/jquery-ui.min.js'></script>
  <script src="js-lib/ember-0.9.8.1.js"></script>
<!--
  <script src="simple.js"></script>
-->

  <script>
    $(document).ready(function(){
      $("#click").click(function() {
        $.ajax({
          url: "s",
          data: "data",
          success: function(data) {
            alert("Received " + data);
          },
//          dataType: dataType
        });
      });
    })
  </script>
</head>

<body>
JSP works with Javascript.

<a id="click" href="#">Click me</a>

</body>

</html>
