var express = require('express');
var app = express();
var path  = require("path");
var exec = require('child_process').exec;

app.use(express.static("styles"));

app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.get('/', function(req, res) {
  //res.sendFile(path.join(__dirname+'/index.html'));
  res.render('index', { title: 'Login'});
});

app.get('/login', function(req, res) {
	var account = req.query.account;
	var password = req.query.password;
	var child = exec('java -cp ".:./h2.jar" h2db login ' + account, function (error, stdout, stderr){
		if(stdout != password) res.redirect('/');
		else res.redirect('/show?account=' + account);
	});
});

app.get('/show', function(req, res, next) {
	var account = req.query.account;
	var child = exec('java -cp ".:./h2.jar" h2db load ' + account, function (error, stdout, stderr){
		var data = [];
		row = stdout.split(";");
		for(var r = 0; r < row.length - 1; r++){
			data.push([]);
			var col = row[r].split(",");
			for(var c = 0; c < col.length; c++) data[data.length - 1].push(col[c]);
		}
		res.render('main', { title: 'Data', data: data, user: account });
	});
});

app.get('/delete', function(req, res, next) {
	var id = req.query.id;
	var account = req.query.account;
	var child = exec('java -cp ".:./h2.jar" h2db delete ' + id, function (error, stdout, stderr){
		res.redirect('/show?account=' + account);
	});
});

app.get('/add', function(req, res, next) {
	var account = req.query.account;
	var item = req.query.item;
	var amount = req.query.amount;
	var child = exec('java -cp ".:./h2.jar" h2db add ' + account + ' ' + item + ' ' + amount, function (error, stdout, stderr){
		res.redirect('/show?account=' + account);
	});
});

app.get('/update', function(req, res, next) {
	var account = req.query.account;
	var id = req.query.id;
	var amount = req.query.amount;
	var child = exec('java -cp ".:./h2.jar" h2db update ' + amount + ' ' + id, function (error, stdout, stderr){
		res.redirect('/show?account=' + account);
	});
});

app.listen(3000, function() {
  console.log('Example app listening on port 3000!');
});
