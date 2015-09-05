<form action="" method="POST">
<p>
User: <input type="text" name="user" /><br />
Attributes: <input type="text" name="attributes" />
<input type="hidden" name="service" value="<?php echo htmlspecialchars($_GET['service']); ?>" />
</p>

<p><input type="submit" /></p>
</form>
