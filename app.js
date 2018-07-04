var express = require('express');
var app = express();
var path  = require("path");
var exec = require('child_process').exec;
const bodyParser = require("body-parser");

app.use(bodyParser.urlencoded({
    extended: true
}));

app.use(bodyParser.json());
app.use(express.static("styles"));
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.get('/', function(req, res) {
  res.render('index', { title: 'Login', message: ''});
});

app.post('/login', function(req, res) {
	var account = req.body.account;
	var password = req.body.password;
	if(account.length < 3 || password.length < 3) res.render('index', { title: 'Login', message: 'Account or Passwrod too short.'});
	var child = exec('java -cp ".:./h2.jar" h2db login ' + account, function (error, stdout, stderr){
		if(stdout.length == 0) res.render('index', { title: 'Login', message: 'Account does not exist.'});
		else if(stdout != password) res.render('index', { title: 'Login', message: 'Wrong password.'});
		else res.redirect(307, '/show');
	});
});

app.get('/register', function(req, res) {
	var account = req.query.account;
	var password = req.query.password;
	if(account.length < 3 || password.length < 3) res.render('index', { title: 'Login', message: 'Account or Passwrod too short.'});
	else{
		var child = exec('java -cp ".:./h2.jar" h2db register ' + account + ' ' + password, function (error, stdout, stderr){
			if(stdout == 'used') res.render('index', { title: 'Login', message: 'Account already exists.'});
			else res.redirect('/');
		});
	}
});

app.post('/show', function(req, res, next) {
	var account = req.body.account;
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

app.post('/delete', function(req, res, next) {
	var id = req.body.id;
	var account = req.body.account;
	var child = exec('java -cp ".:./h2.jar" h2db delete ' + id, function (error, stdout, stderr){
		res.redirect(307, '/show');
	});
});

app.post('/add', function(req, res, next) {
	var account = req.body.account;
	var item = req.body.item;
	var amount = req.body.amount;
	var child = exec('java -cp ".:./h2.jar" h2db add ' + account + ' ' + item + ' ' + amount, function (error, stdout, stderr){
		res.redirect(307, '/show');
	});
});

app.post('/update', function(req, res, next) {
	var account = req.body.account;
	var id = req.body.id;
	var amount = req.body.amount;
	var child = exec('java -cp ".:./h2.jar" h2db update ' + amount + ' ' + id, function (error, stdout, stderr){
		res.redirect(307, '/show');
	});
});

app.listen(3000, function() {
  console.log('Example app listening on port 3000!');
});
