<!-- A basic addgrade.html file served from the "/" URL. -->
<html>
  <body>
    <p>Enter the information for a batch of grade:</p>
    <form action="/grade/enqueue_batch" method="post">
      grade name:
      <input type="text" name="name"><br>
      grader:
      <input type="text" name="grader"><br>
      studentID, score, attribute; (use ';' to seperate grades, attribute may be ignored):<br>
      <input type="text" name="content" style="width: 400px; height: 500px"><br>
      <input type="submit">
    </form>
  </body>
</html>
