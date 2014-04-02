<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="layout" content="main"/>
    <title>MakeIt</title>
</head>
<body>
<div class="container">
    <div class="jumbotron hero-spacer">
        <h1>Welcome to MakeIt!</h1>
        <br />
        <p>MakeIt! is a social network aimed to gather people in amazing events. </p>
        <br />
        <p>New to MakeIt ? <a href="user/create" class="btn btn-primary btn-large">Sign up</a>
        </p>
    </div>
    <hr>
    <div class="row">
        <div class="col-lg-12">
            <h3>//For dev team, insert your content here ;)</h3>
        </div>
    </div>
    <!-- /.row -->
    <div class="row-fluid" style="text-align: center; margin-bottom: 5em; ">
        <div style="padding: 1em;">
            <h3>Available Controllers</h3>
            <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
                <li class="controller"><g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link></li>
            </g:each>
        </div>
    </div>
</div>
<!-- /.container -->
</body>
</html>