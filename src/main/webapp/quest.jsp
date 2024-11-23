<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Квест - путь Junior Java разработчика</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>

<div class="container">
    <h1>Шаг ${step}</h1>
    <p>
    <h2>${head_text}</h2></p>
    <div class ="info_text">${info_text}</div>
    <br>
    <form action="quest" method="get">
        <input type="hidden" name="step" value="${step}"/>

        <!-- Варианты -->
        <label class="radio-option">
            <input type="radio" name="answer" value="1" required> ${var1}
        </label><br>
        <label class="radio-option">
            <input type="radio" name="answer" value="2" required> ${var2}
        </label><br>
        <label class="radio-option">
            <input type="radio" name="answer" value="3" required> ${var3}
        </label><br>

        <!-- Кнопка -->
        <button type="submit" class="submit-btn">Ответить</button>
    </form>
</div>

</body>
</html>
