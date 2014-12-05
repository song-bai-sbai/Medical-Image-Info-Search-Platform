<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*,tpt.info.PageSelectionInfo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<link rel="stylesheet" href="assets/css/amazeui.min.css" />

<script src="assets/js/zepto.min.js"></script>
<script src="assets/js/amazeui.min.js"></script>

<link class="include" rel="stylesheet" type="text/css"
	href="dist/jquery.jqplot.min.css" />

<script type="text/javascript" src="dist/jquery.min.js"></script>
<script src="dist/jquery.jqplot.js"></script>
<script type="text/javascript"
	src="dist/plugins/jqplot.barRenderer.min.js"></script>
<script type="text/javascript"
	src="dist/plugins/jqplot.pieRenderer.min.js"></script>
<script type="text/javascript"
	src="dist/plugins/jqplot.categoryAxisRenderer.min.js"></script>
<script type="text/javascript"
	src="dist/plugins/jqplot.pointLabels.min.js"></script>

<script language="javascript" src="js/rstListJS.js"></script>

<title>Search Result</title>

<!-- Bootstrap Core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">

<!-- Custom CSS -->
<link href="css/landing-page.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="css/font-awesome.min.css" rel="stylesheet" type="text/css">
<link
	href="http://fonts.googleapis.com/css?family=Lato:300,400,700,300italic,400italic,700italic"
	rel="stylesheet" type="text/css">

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>
<body>
	<!-- Navigation -->
	<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
	<div class="container">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#bs-example-navbar-collapse-1">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="#">Project 4</a>
		</div>
		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse"
			id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav navbar-right">
				<li><a href="#">About</a></li>
				<li><a href="#">Services</a></li>
				<li><a href="#">Contact</a></li>
			</ul>
		</div>
		<!-- /.navbar-collapse -->
	</div>
	<!-- /.container --> </nav>

	<!-- Page Content -->

	<!-- /.content-section-a -->

	<div class="content-section-a">

		<div class="container">
			<div class="row">
				<div class="col-lg-5 col-sm-6">
					<h2 class="section-heading">Search Result</h2>
					<h3>Statistic info</h3>
					<p id="text"></p>
				</div>
				<div class="col-lg-5 col-lg-offset-2 col-sm-6">
					<p>Please select a feature to view statistic info:</p>
					<form class="am-form">
						<div class="am-form-group">
							<div id="sfeature">
								<select id="feature">
									<option value="111">testOp</option>
									<option value="121">testOp2</option>
								</select>
							</div>
						</div>
					</form>
					<button class="btn btn-default btn-lg" onclick="getHisotgram()">View
						statistic info</button>
						<div id="chartDiv">
					<div id="chart1"
						style="margin-top: 20px; margin-left: 20px; width: 300px; height: 300px;"></div>
					</div>
				</div>

				<!-- /.container -->
			</div>

			<div class="content-section-b">
				<div class="container">
					<div class="row">
						<div class="col-lg-5 col-sm-6">
							<div>
								<span>Please select slide to view: </span><span id="fileSelect"></span>
								<form class="am-form">
									<div class="am-form-group">
										<div id="sfile">
											<select id="file">
												<option value="111">testOp</option>
												<option value="121">testOp2</option>
											</select>
										</div>

									</div>
								</form>
							</div>
						</div>
						<div class="col-lg-5 col-lg-offset-2 col-sm-6">
							<button class="btn btn-default btn-lg" onclick="getImg()">View
								the slide</button>
						</div>
					</div>

				</div>
				<!-- /.container -->

			</div>
			<!-- /.content-section-a -->

			<div class="content-section-b">

				<div class="container">

					<div class="row">
						<div class="col-lg-5 col-lg-offset-1 col-sm-push-6  col-sm-6">
							<p>Nuclei in this slide:</p>
							<table id="rstTable"
								class="am-table am-table-bd am-table-bdrs am-table-striped am-table-hover">
							</table>
						</div>
						<div class="col-lg-5 col-sm-pull-6  col-sm-6">
							<p>Slide image:</p>
							<div id="srcimg">
								<img alt="" src="img/empty.jpeg" height="400" width="400">
							</div>
							<div id="down"></div>
						</div>
					</div>

				</div>
				<!-- /.container -->

			</div>
		</div>

		<!-- Footer -->
		<footer>
		<div class="container">
			<div class="row">
				<div class="col-lg-12">
					<ul class="list-inline">
						<li><a href="#home">Home</a></li>
						<li class="footer-menu-divider">&sdot;</li>
						<li><a href="#about">About</a></li>
						<li class="footer-menu-divider">&sdot;</li>
						<li><a href="#services">Services</a></li>
						<li class="footer-menu-divider">&sdot;</li>
						<li><a href="#contact">Contact</a></li>
					</ul>
					<p class="copyright text-muted small">Copyright &copy; Song
						2014. All Rights Reserved</p>
				</div>
			</div>
		</div>
		</footer>

		<!-- jQuery -->
		<!-- <script src="js/jquery.js"></script> -->

		<!-- Bootstrap Core JavaScript -->
		<script src="js/bootstrap.min.js"></script>

		<!-- This is loading -->
		<div class="am-modal am-modal-loading am-modal-no-btn" tabindex="-1"
			id="my-modal-loading">
			<div class="am-modal-dialog">
				<div class="am-modal-hd">Search...</div>
				<div class="am-modal-bd">
					<span class="am-icon-spinner am-icon-spin"></span>
				</div>
			</div>
		</div>
</body>

</html>
