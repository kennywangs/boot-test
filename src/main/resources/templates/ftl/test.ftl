<!DOCTYPE>
<html>
<head>
    <title>
        freemarker test
    </title>
</head>
<body>
<h1>Hello ${name} from resource freemark!</h1>
<p>host: ${host}</p>
<p>simple convert date: ${conver('simpleDate',today)!""}</p>
<p>date: ${today?datetime!""}</p>
<p>number: ${double?string(",##0.#")!"0"}</p>
<p>number: ${number!"0"}</p>
<p>null: ${qqq!"aaa"}</p>
<p>content: 这是一段中文</p>
</body>
</html>