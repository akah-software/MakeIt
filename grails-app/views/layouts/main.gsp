<!DOCTYPE html>
<html>
<head>
    <title><g:layoutTitle default="Grails"/></title>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'twitter-bootstrap/bootstrap.min.css')}" type="text/css" />
    <script language="JavaScript" src="${resource(dir: 'js', file: 'twitter-bootstrap/bootstrap.min.js')}"></script>
    <script language="JavaScript" src="${resource(dir: 'js', file: 'twitter-bootstrap/jquery-2.0.3.min.js')}"></script>
</head>
<body>
<nav class="navbar navbar-fixed-top navbar-inverse" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/MakeIt">MakeIt!</a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse navbar-ex1-collapse">
            <ul class="nav navbar-nav">
                <li>
                    <g:link controller="event" action="list">All events</g:link>
                </li>
                <g:if test="${session.user}">
                    <li>
                        <g:link controller="event" action="list" params="[filter: 'my']">My events</g:link>
                    </li>
                    <li>
                        <g:link controller="event" action="list" params="[filter: 'involved']">Involved events</g:link>
                    </li>
                </g:if>
                <li>
                    <g:if test="${session.user}">
                        <g:link controller="user" action="myAccount">${session.user.loginName}</g:link>
                    </g:if>
                    <g:else>
                        <g:link controller="user" action="login">Sign in</g:link>
                    </g:else>
                </li>
            </ul>
        </div>
        <!-- /.navbar-collapse -->
    </div>
    <!-- /.container -->
</nav>
<g:layoutBody/>
<hr>
<footer>
    <div class="row text-center">
        <p>Copyright &copy; MakeIt 2013</p>
    </div>
</footer>
</body>
</html>