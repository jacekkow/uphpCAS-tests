<form action="<?php echo htmlspecialchars($url); ?>" method="POST">
<input type="hidden" name="ticket" value="<?php echo htmlspecialchars($ticket); ?>" />
<input type="submit" />
</form>
<script type="text/javascript">
document.forms[0].submit();
</script>
