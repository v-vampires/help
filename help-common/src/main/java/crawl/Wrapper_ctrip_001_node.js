/**
 * 将该文件放置在node目下 通过node Wrapper_ctrip_001_node.js
 */
var http = require('http');
var url = require('url');
var querystring = require("querystring");
var server = http.createServer(function (req, res) {
    req.setEncoding('utf-8');
    var postData = "";
    req.addListener("data", function (postDataChunk) {// 数据块接收中
        postData += postDataChunk;
    });
    req.addListener("end", function () {// 数据接收完毕，执行回调函数
        var params = querystring.parse(postData);
        var rs = executeJs(params["js"]);
        res.writeHead(200, {
            "Content-Type": "text/plain;charset=utf-8"
        });
        res.end(rs);
    });
});
server.listen(3000, function () {
    console.log('Server is start ... Listen port 3000...');
});
function executeJs(js) {
    var result = eval(js);
    console.log("executeResult:" + result);
    return result;
}